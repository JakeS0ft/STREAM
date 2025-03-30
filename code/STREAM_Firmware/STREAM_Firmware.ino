/*
 This file is part of the STREAM saber control program (STREAM).

 Copyright (C) 2017-2019 Jacob "JakeSoft" Martin.

 The STREAM software is distributed in the hope that it will be
 useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*/
/*
 * STREAM.ino
 *
 *  Created on: May 13, 2017
 *      Author: JakeSoft
 */

#include <avr/wdt.h>
#include <Arduino.h>
#include <EEPROM.h>
#include <USaber.h>
#include <AccentLED.h>
#include "SaberStateMachine.h"
#include "Button.h"
#include "Settings.h"
#include "StreamControl.h"
#include "Utility.h"
#include "PowerManager.h"
#include "MPU6050Calibrate.h"


//**** Enable/Disable test bed hardware and settings ****
//#define CONFIG_TESTBED

//**** Select Hardware target ****
//#define HW_STARDUST
//#define HW_DIYINO_PRIME
//#define HW_JAKESOFT_DIYINO

#ifdef HW_STARDUST_V3
	#include "Pins_DIYinoStardustV3.h"
#elif defined HW_DIYINO_PRIME_V1
	#include "Pins_DIYinoPrimeV1.h"
#elif defined HW_JAKESOFT_DIYINO
	#include "Pins_JakeSoft_DIYino.h"
#elif defined HW_PROTO_V1
#include "Pins_ProtoV1.h"
#elif defined HW_BREWBOARD
	#include "Pins_Brewboard.h"
#endif

//#include "Pins_DIYinoStardust.h"
//#include "Pins_DIYino.h"
//#include "Pins_JakeSoft_DIYino.h"

//Accent LED
#ifdef HW_STARDUST_V3
	DigitalAccentLED gAccentLED(ACCENT1);
#elif defined HW_DIYINO_PRIME_V1
	PwmAccentLED gAccentLED(ACCENT1);
#elif defined HW_JAKESOFT_DIYINO
//	PwmAccentLED gAccentLED(ACCENT1);
	AccentLEDStub gAccentLED;
#elif defined HW_PROTO_V1
	AccentLEDStub gAccentLED;
#elif defined HW_BREWBOARD
	DigitalAccentLED gAccentLED(ACCENT1);
#endif

//Global configuration variables
SoundMap gSoundMap; //Sound configuration data for the sound player
//Tolerance threshold data for MPU6050 motion manager
MPU6050LiteTolData gToleranceData;

//Settings manager
SettingsManager gSettingsManager;

//Saber components
ASoundPlayer* gpSoundPlayer;
Mpu6050LiteMotionManager gMotion(&gToleranceData);

#ifdef PIXEL_BLADE
NRPixelStrip gPixelStrip;
PixelBlade gBlade(DEFAULT_BLADE_LEN, &gPixelStrip, LED_LS1_PIN, LED_LS2_PIN, LED_LS3_PIN);
#else
RGBBlade gBlade(LED_LS1_PIN, LED_LS2_PIN, LED_LS3_PIN);
EffectsBladeStubs gBladeEffects;
#endif

IBladeManager* gpBlade;
IEffectsBlade* gpBladeEffects;

//Buttons
Button gActButton(BUTTON1_PIN);
Button gAuxButton(BUTTON2_PIN);

//Power Manager
PowerManager gPowerManager;

//Serial settings interface manager
StreamControl* gpSerialSettings;

//The primary state machine for saber control
SaberStateMachine* gpStateMachine;

//The setup function is called once at startup of the sketch
void setup()
{
	wdt_reset();
	wdt_disable();

	// Only un-comment if EEPROM values are suspect
	//This line must be commented or settings will be purged after every reset.
	//ClearEEPROM();

	delay(100);

	//Setup global blade pointers
	gpBlade = &gBlade;
#ifdef PIXEL_BLADE
	gpBladeEffects = &gBlade;
	gPixelStrip.SetUseSlowFrames(true);
#else
	gpBladeEffects = &gBladeEffects;
#endif
	Serial.begin(9600); //For debugging and STREAM GUI interface

//	Serial.println("Boot");

	//Set up power management pins
	if(FTDI_PSWITCH_PIN > 0)
	{
		pinMode(FTDI_PSWITCH_PIN, OUTPUT);
	}
	if(MP3_PSWITCH_PIN > 0)
	{
		pinMode(MP3_PSWITCH_PIN, OUTPUT);
	}

	//Load Settings
	gSettingsManager.Load(); //Load settings from EEPROM
//	gSettingsManager.LoadDefaultSettings(); //Load default settings
//	gSettingsManager.Save();

	if(!gSettingsManager.IsSettingsValid())
	{
		Serial.println("Settings invalid. Loading default settings.");
		gSettingsManager.LoadDefaultSettings(); //Load default settings
		gSettingsManager.Save();
	}

	//Default all values to zero in the sound map
    //Not strictly necessary, but a good idea
//	memset(&gSoundMap, 0, sizeof(SoundMap));

	/***
	 * Set up sound map so sketch knows where sounds are on the SD card
	 * or SPI Flash and what features are supported. You only need to set the
	 * fields you intend to use. These values should be adjusted to match how
	 * your SD card or SPI Flash sounds are configured.
	 */
	gSoundMap.Features.FontIdsPerFont = 1;
	gSoundMap.Features.HumSoundsPerFont = 1;
	gSoundMap.Features.PowerUpSoundsPerFont = 1;
	gSoundMap.Features.PowerDownSoundsPerFont = 1;
	gSoundMap.Features.ClashSoundsPerFont = 8;
	gSoundMap.Features.SwingSoundsPerFont = 8;
	gSoundMap.Features.LockupSoundsPerFont = 1;
	gSoundMap.Features.BlasterSoundsPerFont = 4;
	gSoundMap.Features.ForceSoundsPerFont = 0;
	gSoundMap.Features.CustomSoundsPerFont = 1;
	gSoundMap.Features.MenuSounds = 22;

	//Menu sound locations
	gSoundMap.Locations.MenuBase = 1;
	gSoundMap.Locations.BootBase = 11;

	//Font sound locations
	gSoundMap.Locations.BaseAddr = 0;
	gSoundMap.Locations.FontIdBase = gSoundMap.Features.MenuSounds + 1;
	gSoundMap.Locations.PowerupBase = gSoundMap.Features.MenuSounds + 2;
	gSoundMap.Locations.SwingBase = gSoundMap.Features.MenuSounds + 3;
	gSoundMap.Locations.ClashBase = gSoundMap.Features.MenuSounds + 11;
	gSoundMap.Locations.LockupBase = gSoundMap.Features.MenuSounds + 19;
	gSoundMap.Locations.BlasterBase = gSoundMap.Features.MenuSounds + 20;
	gSoundMap.Locations.HumBase = gSoundMap.Features.MenuSounds + 24;
	gSoundMap.Locations.PowerdownBase = gSoundMap.Features.MenuSounds + 25;
	gSoundMap.Locations.CustomBase = gSoundMap.Features.MenuSounds + 26;

	gpSoundPlayer = new DIYinoSoundPlayer(SOUND_TX_PIN, SOUND_RX_PIN, &gSoundMap);

	//Set up power management
	PowerManager::SetPins(FTDI_PSWITCH_PIN, MP3_PSWITCH_PIN, ACCENT1);
	PowerManager::SetSerialPins(SOUND_TX_PIN, SOUND_RX_PIN);
#ifdef PIXEL_BLADE
	PowerManager::SetPixelDataPin(PIXEL_DATA_PIN);
#endif
	gPowerManager.AddWakeInputPin(BUTTON1_PIN);


	//Serial STREAM control
	gpSerialSettings = new StreamControl(&gSettingsManager, &gBlade, gpSoundPlayer, &gMotion);
	gpSerialSettings->SetFirmwareVersion(FIRMWARE_VERSION_MAJOR, FIRMWARE_VERSION_MINOR);

	Utility::SetMotionTolerances(&gToleranceData, &gSettingsManager);

	gpStateMachine = new SaberStateMachine(&gAccentLED,
									       &gSettingsManager,
										   &gPowerManager);

	gpStateMachine->Init();
	gpStateMachine->SetStreamControl(gpSerialSettings);

	//Load MPU Calibration data
#ifdef LOAD_EEPROM_CAL
	LoadCalFromEEPROM();
#endif

	//Debugging function calls. Leave commented out unless having problems
//	PrintPinoutData();
//	ColorCycleBlade();
}

// The loop function is called in an endless loop
void loop()
{
	gpStateMachine->Operate();
}

// Debugging function to check color settings
void DumpColorPresets()
{
	Serial.println("Color presets:");

	for(int i = 0; i < MAX_COLOR_PRESETS; i++)
	{
		Serial.print(gSettingsManager.GetSettings()->maColorPresets[i].mChannel1);
		Serial.print("/");
		Serial.print(gSettingsManager.GetSettings()->maColorPresets[i].mChannel2);
		Serial.print("/");
		Serial.println(gSettingsManager.GetSettings()->maColorPresets[i].mChannel3);
	}
}

// Debugging function to print pin info
void PrintPinoutData()
{
	Serial.print("BUTTON2_PIN:"); Serial.println((int)BUTTON2_PIN);
	Serial.print("BUTTON1_PIN:"); Serial.println((int)BUTTON1_PIN);
	Serial.print("ACCENT1:"); Serial.println((int)ACCENT1);
}

// Debugging function to color cycle the blade
void ColorCycleBlade()
{
	for(int lnChnl = 0; lnChnl < 3; lnChnl++)
	{
		gBlade.SetChannel(128, lnChnl);
	    gBlade.On();
		delay(250);
		gBlade.Off();
	}
}

// Debugging function to clear the EEPROM
void ClearEEPROM()
{
	//This block for DEBUG only, to clear the EEPROM
	for (int i = 0 ; i < EEPROM.length() ; i++)
	{
		EEPROM.write(i, 0);
	}
}
