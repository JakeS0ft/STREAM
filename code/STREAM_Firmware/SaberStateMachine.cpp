/*
 This file is part of the STREAM saber control program (STREAM).

 STREAM is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as
 published by the Free Software Foundation, either version 3 of the License,
 or (at your option) any later version.

 The STREAM software is distributed in the hope that it will be
 useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with the STREAM software.  If not, see <http://www.gnu.org/licenses/>.
*/
/*
 This file is part of the STREAM saber control program.

 Copyright (C) 2017-2018 Jacob "JakeSoft" Martin.

 The STREAM software is distributed in the hope that it will be
 useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*/
/*
 * SaberStateMachine.cpp
 *
 *  Created on: Mar 22, 2017
 *      Author: JakeSoft
 */

#include <avr/wdt.h>
#include "Globals.h"
#include "SaberStateMachine.h"
#include "SaberActions.h"
#include "Utility.h"

#define SWITCH_DEBOUCE_TIME 75UL
#define POWER_DOWN_SWITCH_TIME 2500UL
#define MENU_SWITCH_HOLD_TIME 3000UL
#define CLASH_PULSE_TIME 100UL
#define BLASTER_SWITCH_TIME 75
#define LOCKUP_SWITCH_TIME 200
#define MIN_SLEEP_TIMEOUT 30000UL //15 seconds

SaberStateMachine::SaberStateMachine(
		              IAccentLED* apAccentLED,
					  SettingsManager* apSettingsManager,
					  PowerManager* apPowerMan) :
mpStreamControl(nullptr),
mpAccent(apAccentLED),
mLastClashTime(0),
mLastSwingTime(0),
mLastBlasterTime(0),
mBlasterMode(false),
mOneButtonLockupMode(false),
mpMenu(nullptr),
mHumRelaunched(false)
{

}

void SaberStateMachine::Init()
{
	//Set initial state to the boot-up state
	mState = eeBoot;

	//Initialize components
	gpBladeEffects->SetBladeLength((int)gSettingsManager.GetOptions()->mBladeLength);
	gpBlade->Init();
	gpSoundPlayer->Init();
	gActButton.Init();
	gAuxButton.Init();
	mpAccent->Init();

	//Give time for the MPU6050 and Sound chip to wake up
	delay(100);

	//Reload motion engine settings
	Utility::SetMotionTolerances(&gToleranceData, &gSettingsManager);
	gMotion.Init(); //Reinitialize with new tolerance values
	delay(100);
	gMotion.Init(); // First time doesn't always work when waking from deep sleep

	//Load the volume level from EEPROM settings
	SaberActions::SetSoundVolume((int)gSettingsManager.GetSettings()->mSoundVolume, gpSoundPlayer);

	delay(100);
}

void SaberStateMachine::Body()
{
	//Update motion sensing
	gMotion.Update();

	//Detect button presses
	gActButton.Update();
	gAuxButton.Update();

	//Update Accent LED
	mpAccent->PerformIO();

	//Calculate sleep timeout in milliseconds
	unsigned long lDeepSleepTimeoutMs =
			((unsigned long)gSettingsManager.GetOptions()->mSleepTimeMins)*1000UL*60UL;

	//Perform state-specific actions
	switch(mState)
	{
	case eeBoot:
		wdt_reset();
		wdt_disable();

		delay(500);
		//Set the current font based on settings
		gpSoundPlayer->SetFont(gSettingsManager.GetSelectedProfileIndex());

		//Sound chip sometimes ignores volume command after power cycle. This is meant to correct that.
		SaberActions::SetSoundVolume((int)gSettingsManager.GetSettings()->mSoundVolume, gpSoundPlayer);
		delay(100);

		//Play the boot sound
		if(gSettingsManager.GetOptions()->mUseFontBootSounds > 0)
		{
			gpSoundPlayer->PlaySound(ESoundTypes::eeCustomSnd, 0);
		}
		else
		{
			gpSoundPlayer->PlaySound(ESoundTypes::eeBootSnd, 0);
		}


		delay(100);

		ChangeState(eeOff);
		break;
	case eeOff:
		mpAccent->SetStyle(eeAccentIdleBlink);

		mBlasterMode = false;

		if(gActButton.IsPulseEdge(SWITCH_DEBOUCE_TIME) )
		{
			ChangeState(eePoweringUp);
		}
		else if(gActButton.IsHeld() && gActButton.GetHeldTime() > MENU_SWITCH_HOLD_TIME)
		{
				ChangeState(eeMenu);
		}
		else if(gActButton.IsHeld() && gMotion.IsClash())
		{
			ChangeState(eeSwitchProfile);
		}
		else if(gAuxButton.IsHeld() && gAuxButton.GetHeldTime() > 1000UL)
		{
			ChangeState(eeSwitchProfile);
		}
		else if(millis() - mStateChangeTime > MIN_SLEEP_TIMEOUT
				&& millis() - mStateChangeTime > lDeepSleepTimeoutMs
				&& gSettingsManager.GetOptions()->mSleepTimeMins != (uint8_t)255
				&& gSettingsManager.GetOptions()->mSleepTimeMins > (uint8_t)0)
		{
			ChangeState(eeSleep);
		}

		break;
	case eePoweringUp:
		//Do these actions only once upon entering this state
//		if(mIsNewState)
//		{
//			Serial.println("Powering Up"); //Debug
//		}

		//Do actual power up
		SaberActions::PerformPowerUp(gpSoundPlayer,
				                     gpBlade,
									 gSettingsManager.GetNormalBladeColor(),
									 gSettingsManager.GetSoundSettings()->mPowerOnBladeDelay,
									 gSettingsManager.GetSoundSettings()->mPowerOnTime);

		//Re-synchronize the motion manager
		for(unsigned int lUpd = 0; lUpd <= 5; lUpd++)
		{
			gMotion.Update();
			delay(1);
		}

		gActButton.WaitForRelease();

		ChangeState(eeOnIdle);
		break;
	case eeOnIdle:
		//Do these actions only once upon entering this state
		if(mIsNewState)
		{
//			Serial.println("On."); //Debug

			SaberActions::SetBladeToMainColor(gpBlade, &gSettingsManager);
			mpAccent->SetStyle(teAccentStyle::eeAccentTwoStagePulse);
			mHumRelaunched = false;
		}

		//User pressed the button, so turn off the saber, 1 second minimum
		if(gActButton.IsHeld()
			&& gActButton.GetHeldTime() >= gSettingsManager.GetOptions()->mPowerOffSwitchHoldTimeMs
			&& gActButton.GetHeldTime() >= 1000)
		{
//			Serial.println("Power off due to button press.");
			//Go to power down state
			ChangeState(eePoweringDown);
		}
		//Automatic powerdown time expired, so turn off the saber
		else if(millis() - mStateChangeTime >= 1000UL * (unsigned long)(gSettingsManager.GetOptions()->mAutoPowerOffTimeSec)
				&& gSettingsManager.GetOptions()->mAutoPowerOffTimeSec >= 10)
		{
//			Serial.println("Automatic power off.");
			ChangeState(eePoweringDown);
		}
		//Clash event detected
		else if(gMotion.IsClash())
		{
			mBlasterMode = false;

			if(gActButton.IsHeld() && gSettingsManager.GetOptions()->mOneButtonLockupEnabled)
			{
				mOneButtonLockupMode = true;
				ChangeState(eeLockup);
			}
			else
			{
				ChangeState(eeClash);
			}
		}
		else if(gActButton.IsPulseEdge(BLASTER_SWITCH_TIME)
				&& gSettingsManager.GetOptions()->mOneButtonBlasterEnabled)
		{
			mBlasterMode = !mBlasterMode; //Toggle blaster mode
			if(mBlasterMode)
			{
				ChangeState(eeBlaster);
			}
		}
		else if(gAuxButton.IsPulseEdge(BLASTER_SWITCH_TIME) )
		{
			mBlasterMode = false;
			ChangeState(eeBlaster);
		}
		else if(gAuxButton.IsHeld() && gAuxButton.GetHeldTime() > LOCKUP_SWITCH_TIME)
		{
			ChangeState(eeLockup);
		}
		//Swing event detected
		else if(gMotion.IsSwing() && gMotion.GetSwingMagnitude() > eeSmall)
		{
			if(mBlasterMode)
			{
				ChangeState(eeBlaster);
			}
			//Do a normal swing, but don't let swings interrupt blaster events
			else if(millis() - mLastBlasterTime >= gSettingsManager.GetSoundSettings()->mClashSwingSuppressTime)
			{
				ChangeState(eeSwing);
			}
		}
		//Re-launch hum
		else if(millis() - mStateChangeTime >= gSettingsManager.GetHumRelaunchInterval()
					&& !mHumRelaunched)
		{
//			Serial.println("Hum re-launch.");
			gpSoundPlayer->PlaySound(ESoundTypes::eeHumSnd, 0);
			mHumRelaunched = true;
		}
		else
		{
			gpBlade->ApplyFlicker(gSettingsManager.GetBladeSettings()->mFlickerIndex);
		}
		break;
	case eeSwing:
		gpSoundPlayer->PlayRandomSound(ESoundTypes::eeSwingSnd);
		ChangeState(eePostSwing);
		break;
	case eePostSwing:
		//User wants to power down the saber
		if(gActButton.IsHeld() && gActButton.GetHeldTime() >= POWER_DOWN_SWITCH_TIME)
		{
			ChangeState(eePoweringDown);
		}
		//A clash happened
		else if(gMotion.IsClash())
		{
			//Don't jam the sound card with too many requests
			while(millis() - mStateChangeTime <= 100UL)
			{
				delay(1);
				gMotion.Update();
			}
			if(gActButton.IsHeld() && gSettingsManager.GetOptions()->mOneButtonLockupEnabled)
			{
				ChangeState(eeLockup);
			}
			else
			{
				ChangeState(eeClash);
			}
		}
		//Blaster block happened from aux switch
		else if(gAuxButton.IsPulseEdge(SWITCH_DEBOUCE_TIME))
		{
			//Don't jam the sound card with too many requests
			while(millis() - mStateChangeTime <= 100UL)
			{
				gAuxButton.Update();
				delay(1);
			}
			mBlasterMode = false;

			ChangeState(eeBlaster);
		}
		else if(gActButton.IsPulseEdge(BLASTER_SWITCH_TIME) && gSettingsManager.GetOptions()->mOneButtonBlasterEnabled)
		{
			//Don't jam the sound card with too many requests
			while(millis() - mStateChangeTime <= 100UL)
			{
				gActButton.Update();
				delay(1);
			}

			mBlasterMode = !mBlasterMode;

			if(mBlasterMode)
			{
				ChangeState(eeBlaster);
			}
		}
		//Swing is over
		else if(!gMotion.IsSwing() && millis() - mStateChangeTime >=
					(unsigned long)gSettingsManager.GetSoundSettings()->mMinSwingInterval)
		{
			ChangeState(eeOnIdle);
		}
		//Swing has gone on for a long time, exit this state so a new swing sound can play
		else if(millis() - mStateChangeTime >
			(unsigned long)gSettingsManager.GetSoundSettings()->mMaxSwingInterval)
		{
			ChangeState(eeOnIdle);
		}
		break;
	case eeClash:
		//Do these actions only once upon entering this state
		if(mIsNewState)
		{
			//Capture the time of the clash event
			mLastClashTime = millis();

			//Play a clash sound
			gpSoundPlayer->PlayRandomSound(ESoundTypes::eeClashSnd);

			//Set blade to the flash color
			//SetBladeToFlashColor();
			SaberActions::SetBladeToFlashColor(gpBlade, &gSettingsManager);

//			Serial.print("Clash detected at sensitivity level:");
//			Serial.print(gSettingsManager.GetSelectedPofile()->mClashPresetIndex);
//			Serial.print("(");
//			Serial.print((int)gToleranceData.mClash);
//			Serial.println(")");
		}

		if(millis() - mStateChangeTime > CLASH_PULSE_TIME)
		{
			//Set blade back to the normal color
			SaberActions::SetBladeToMainColor(gpBlade, &gSettingsManager);

			ChangeState(eePostClash);
		}
		break;
	case eePostClash:
		//User pressed the button, so turn off the saber, 1 second minimum
		if(gActButton.IsHeld()
			&& gActButton.GetHeldTime() >= gSettingsManager.GetOptions()->mPowerOffSwitchHoldTimeMs
			&& gActButton.GetHeldTime() >= 1000)
		{
//			Serial.println("Power off due to button press.");
			//Go to power down state
			ChangeState(eePoweringDown);
		}
		else if(gMotion.IsClash() && millis() - mLastClashTime > 200UL)
		{
			//Respond to new clash events, but not at a rate faster than once per 200ms
			//This allows for clash to settle and avoids jamming the sound card
			ChangeState(eeClash);
		}
		//Use sound timings to decide when the clash sound is done playing
		else if(millis() - mStateChangeTime >=
				(unsigned long)gSettingsManager.GetSoundSettings()->mClashSwingSuppressTime)
		{
			ChangeState(eeOnIdle);
		}
		break;
	case eeLockup:
		if(mIsNewState)
		{
			gpSoundPlayer->PlaySound(eeLockupSnd, 0);
			//SaberActions::SetBladeToFlashColor(gpBlade, &gSettingsManager);

			//Wait for user to let off the button
			if(mOneButtonLockupMode)
			{
//				Serial.println("Lockup waiting for act button release.");
				for(int lCounter = 0; lCounter < 250; lCounter++)
				{
					gActButton.Update();
					if(!gActButton.IsHeld())
					{
						lCounter++;
					}
					else
					{
						lCounter = 0;
					}
					delay(1);

					//Apply lockup effect while we wait for user to let off the button
					SaberActions::ApplyLockupEffect(gpBlade,
							                       gSettingsManager.GetNormalBladeColor(),
												   gSettingsManager.GetFlashBladeColor(),
												   gSettingsManager.GetBladeSettings()->mLockupColorIntensity,
												   gSettingsManager.GetBladeSettings()->mLockupFlickerIntensity,
												   gSettingsManager.GetBladeSettings()->mLockupFramePeriod);
				}

			}
		}
		else if(mOneButtonLockupMode) //One-button lockup mode
		{
//			Serial.println("One button lockup.");
			if(gActButton.IsHeld() || gAuxButton.IsHeld())
			{
				if(millis() - mStateChangeTime > 1000UL)
				{
					gActButton.WaitForRelease();
					gAuxButton.WaitForRelease();
//					Serial.println("Exit lockup because of buttons.");
					SaberActions::SetBladeToMainColor(gpBlade, &gSettingsManager);
					gpSoundPlayer->PlaySound(eeHumSnd, 0);
					mOneButtonLockupMode = false;
					ChangeState(eeOnIdle);
				}
			}
			else if(gMotion.IsSwing()
					&& millis() - mStateChangeTime > 1000UL
					&& gSettingsManager.GetOptions()->mExitLockupOnSwing > 0)
			{
//				Serial.println("Exit lockup because of swing.");
				SaberActions::SetBladeToMainColor(gpBlade, &gSettingsManager);
				mOneButtonLockupMode = false;
				ChangeState(eeSwing);
			}
		}
		else //Aux-button lockup mode
		{
			if(!gAuxButton.IsHeld())
			{
				SaberActions::SetBladeToMainColor(gpBlade, &gSettingsManager);
				gpSoundPlayer->PlaySound(eeHumSnd, 0);
				mOneButtonLockupMode = false;
				gAuxButton.WaitForRelease();
				ChangeState(eeOnIdle);
			}
		}

		//None of the logic above caused us to exit this state, so apply blade effect
		if(eeLockup == mState)
		{
			//Lockup effect
			SaberActions::ApplyLockupEffect(gpBlade,
					                       gSettingsManager.GetNormalBladeColor(),
										   gSettingsManager.GetFlashBladeColor(),
										   gSettingsManager.GetBladeSettings()->mLockupColorIntensity,
										   gSettingsManager.GetBladeSettings()->mLockupFlickerIntensity,
										   gSettingsManager.GetBladeSettings()->mLockupFramePeriod);
		}

		break;
	case eeBlaster:
		if(mIsNewState)
		{
			mLastBlasterTime = millis();
			gpSoundPlayer->PlayRandomSound(eeBlasterSnd);
			//Pixel blade effects (does nothing if running as RGB)
			gpBladeEffects->SetEffectColor(gSettingsManager.GetFlashBladeColor().mChannel1,
					                       gSettingsManager.GetFlashBladeColor().mChannel2,
										   gSettingsManager.GetFlashBladeColor().mChannel3);
#ifdef PIXEL_BLADE
			gpBladeEffects->ApplyBlasterEffect(0, gSettingsManager.GetSoundSettings()->mBlasterFlashTime);
#else
			//RGB (happens to fast to be seen for pixel blades)
			SaberActions::SetBladeToFlashColor(gpBlade, &gSettingsManager);
#endif

		}
		else if(millis()-mStateChangeTime >=
				(unsigned long)gSettingsManager.GetSoundSettings()->mBlasterFlashTime)
		{
			SaberActions::SetBladeToMainColor(gpBlade, &gSettingsManager);
			if(!gMotion.IsSwing() ||
				millis() - mStateChangeTime >= (unsigned long)gSettingsManager.GetSoundSettings()->mBlasterSuppressTime)
			{
				ChangeState(eeOnIdle);
			}
		}

		if(gMotion.IsClash())
		{
			mBlasterMode = false;
			ChangeState(eeClash);
		}

		break;
	case eePoweringDown:
//		Serial.println("Powering down"); //Debug

		SaberActions::PerformPowerDown(gpSoundPlayer,
				                       gpBlade,
									   gSettingsManager.GetSoundSettings()->mPowerOffTime);

		//Wait for user to let off the button
		gActButton.WaitForRelease();

		ChangeState(eeOff);
		break;
	case eeSwitchProfile:
		if(mIsNewState)
		{
			int lNextProfileIdx = gSettingsManager.GetSelectedProfileIndex();
			lNextProfileIdx++;
			if(lNextProfileIdx >= MAX_PROFILES)
			{
				lNextProfileIdx = 0;
			}
			SaberActions::SwitchProfile(lNextProfileIdx, &gSettingsManager, gpSoundPlayer, &gMotion);
			gSettingsManager.Save();
		}

		//Wait for user to let off buttons
		gActButton.WaitForRelease();
		gAuxButton.WaitForRelease();

		ChangeState(eeOff);
		break;
	case eeMenu:
		if(mIsNewState)
		{
			gpSoundPlayer->PlaySound(ESoundTypes::eeMenuSoundSnd, 11);
			delay(1000);
		}
		//User kept holding the button, so start STREAM mode
		else if(gActButton.GetHeldTime() > MENU_SWITCH_HOLD_TIME*2)
		{
			ChangeState(eeStreamMode);
		}
		//User let off the button, so start the menu system
		else if(!gActButton.IsHeld() && !gActButton.IsPulseEdge())
		{
			mpAccent->SetStyle(teAccentStyle::eeAccentEvenPulse);

			mpMenu = new MenuStateMachine(&gSettingsManager,
										  gpBlade,
										  gpSoundPlayer,
										  &gMotion,
										  &gActButton,
										  &gAuxButton);

			//Run the menu until user has traversed all the options
			while(!mpMenu->IsDone())
			{
				mpAccent->PerformIO();
				mpMenu->Operate();
			}


			delete mpMenu; //Can the menu to free up memory

			//Set new motion parameters
			Utility::SetMotionTolerances(&gToleranceData, &gSettingsManager);
			gMotion.Init(); //Reinitialize with new tolerance values
			ChangeState(eeOff);
		}

		break;
	case eeSleep:

		//Put the MPU to sleep
		gMotion.Sleep();

		//Put everything else to sleep
		gPowerManager.Sleep();

		//Reinitialize saber components (will also wake up the MPU)
		Init();

		//Wait for user to let off the button
		gActButton.WaitForRelease(150);

		ChangeState(eeBoot);
		break;
	case eeFlashDriveMode:
		//TODO: Deactivate the FTDI chip and do whatever is needed for
		//make the Stardust act like a USB flash drive
		break;
	case eeStreamMode:
		if(mIsNewState)
		{
			mpAccent->SetStyle(teAccentStyle::eeAccentOff);
			mpAccent->PerformIO();

			gpSoundPlayer->PlaySound(ESoundTypes::eeMenuSoundSnd, eeStreamModeSnd);

			gpBlade->SetChannel(64, 0);
			gpBlade->On();

			mpStreamControl->Init(); //Initialize STREAM

			//Wait for user to let off the button
			gActButton.WaitForRelease(100);
			gpBlade->Off();
			for(int i = 0; i < 5; i++)
			{
				gpBlade->SetChannel(64, 0);
				gpBlade->On();
				delay(500);
				gpBlade->Off();
				delay(500);
			}

			//Play "connected" sound
			gpSoundPlayer->PlaySound(ESoundTypes::eeMenuSoundSnd, eeConnectedSnd);
		}
		else if(gActButton.IsHeld())
		{
			for(int i = 0; i < 5; i++)
			{
				gpBlade->SetChannel(64, 0);
				gpBlade->On();
				delay(250);
				gpBlade->Off();
				delay(250);
			}
			gActButton.WaitForRelease();

			//Set new motion parameters
			Utility::SetMotionTolerances(&gToleranceData, &gSettingsManager);
			gMotion.Init(); //Reinitialize with new tolerance values

			//Play "disconnected" sound
			gpSoundPlayer->PlaySound(ESoundTypes::eeMenuSoundSnd, eeDisconnectedSnd);

			delay(1000);
			ChangeState(eeBoot);
		}
		else //Respond to STREAM commands when the user isn't pushing any buttons
		{
			mpStreamControl->Operate();
		}

		break;
	default:
		//Do nothing
		break;
	}

}

void SaberStateMachine::SetStreamControl(StreamControl* apSc)
{
	mpStreamControl = apSc;
}

