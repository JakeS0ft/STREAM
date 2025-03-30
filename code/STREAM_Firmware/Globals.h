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
 * Globals.h
 *
 *  Created on: Nov 25, 2018
 *      Author: JakeSoft
 */

#ifndef GLOBALS_H_
#define GLOBALS_H_

#include <USaber.h>
#include "StreamBuildConfig.h"
#include "StreamControl.h"
#include "PowerManager.h"
#include "Button.h"
#include "SaberStateMachine.h"

#ifdef HW_STARDUST_V3
	#include "Pins_DIYinoStardustV3.h"
#elif defined HW_DIYINO_PRIME_V1
	#include "Pins_DIYinoPrimeV1.h"
#elif defined HW_JAKESOFT_DIYINO
	#include "Pins_JakeSoft_DIYino.h"
#elif defined HW_PROTO_V1
#include "Pins_ProtoV1.h"
#endif

//Global configuration variables
extern SoundMap gSoundMap; //Sound configuration data for the sound player
//Tolerance threshold data for MPU6050 motion manager
extern MPU6050LiteTolData gToleranceData;

//Settings manager
extern SettingsManager gSettingsManager;

//Saber components
extern ASoundPlayer* gpSoundPlayer;
extern Mpu6050LiteMotionManager gMotion;
extern IBladeManager* gpBlade;
extern IEffectsBlade* gpBladeEffects;

//Buttons
extern Button gActButton;
extern Button gAuxButton;

//Power Manager
extern PowerManager gPowerManager;

//Serial settings interface manager
extern StreamControl* gpSerialSettings;

//The primary state machine for saber control
extern SaberStateMachine* gpStateMachine;

//Accent LED
#ifdef HW_STARDUST_V3
	extern DigitalAccentLED gAccentLED;
#elif defined HW_DIYINO_PRIME_V1
	extern PwmAccentLED gAccentLED;
#elif defined HW_JAKESOFT_DIYINO
	extern PwmAccentLED gAccentLED;
#elif defined HW_PROTO_V1
	extern AccentLEDStub gAccentLED;
#endif

#endif /* GLOBALS_H_ */
