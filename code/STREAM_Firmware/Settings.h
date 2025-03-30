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
 * Settings.h
 *
 *  Created on: Mar 25, 2017
 *      Author: JakeSoft
 */

#ifndef SETTINGS_H_
#define SETTINGS_H_

#include <USaber.h>
#include "StreamBuildConfig.h"

#define SETTINGS_START (0b10101010)
#define SETTINGS_END   (0b01010101)

#define DEFAULT_BLADE_LEN 120

//Defines blade color for an RGB blade
struct tBladeColor
{
	uint8_t mChannel1; //Red
	uint8_t mChannel2; //Green
	uint8_t mChannel3; //Blue
};

struct tBladeParameters
{
	//Selected main blade color
	uint8_t mMainColorIndex;
	//Selected clash/blaster blade color
	uint8_t mFlashColorIndex;
	//Selected blade effect
	uint8_t mFlickerIndex;
	//Lockup color effect intensity
	uint8_t mLockupColorIntensity;
	//Lockup flicker Intensity
	uint8_t mLockupFlickerIntensity;
	//Lockup animation speed
	uint16_t mLockupFramePeriod;
};

struct tMotionPreset
{
	//Tolerance for large swings
	uint16_t mSwingLargeTol;
	//Tolerance for medium swings
	uint16_t mSwingMediumTol;
	//Tolerance for small swings
	uint16_t mSwingSmallTol;
	//Tolerance for clashes
	uint16_t mClashTol;
	//Tolerance for twist
	uint16_t mTwistTol;
};

struct tMotionParameters
{
	//Tolerance for large swings
	uint16_t mSwingLargeTol;
	//Tolerance for medium swings
	uint16_t mSwingMediumTol;
	//Tolerance for small swings
	uint16_t mSwingSmallTol;
	//Tolerance for clashes
	uint16_t mClashTol;
	//Tolerance for twist
	uint16_t mTwistTol;
};

struct tTimingParameters
{
	/**
	 * Sound and Sound Synchronization Parameters (18 bytes)
	 */
	//time in milliseconds after power-on sound start playing
	//before the LED for blade starts ramping
	uint16_t mPowerOnBladeDelay;
	//time in milliseconds for power on blade ramp to complete
	uint16_t mPowerOnTime;
	//time in milliseconds for power off blade ramp to complete
	uint16_t mPowerOffTime;
	//time in milliseconds for blaster blade pulse
	uint16_t mBlasterFlashTime;
	//time after a blaster block before another blaster event can occur
	uint16_t mBlasterSuppressTime;
	//time after a clash for swing sounds to be suppressed
	uint16_t mClashSwingSuppressTime;
	//minimum time between swing sounds
	uint16_t mMinSwingInterval;
	//maximum time after swing before a new swing is played
	uint16_t mMaxSwingInterval;
	//idle time before hum plays again
	uint16_t mHumRelaunchInterval;
};

struct tProfile
{
	tTimingParameters mSound;
	tBladeParameters mBlade;

	uint8_t mSwingPresetIndex; //Index for swing sensitivity
	uint8_t mClashPresetIndex; //Index for clash sensitivity
};

struct tOptions
{
	uint8_t mExitLockupOnSwing; //Exit lockup when swing detected
	uint8_t mAutoPowerOffTimeSec; //Seconds to power off when idle
	uint16_t mPowerOffSwitchHoldTimeMs; //Milliseconds to hold switch to power off
	uint16_t mMenuSwitchHoldTimeMs; //Milliseconds to hold switch to enter menu mode
	uint8_t mSleepTimeMins;
	uint8_t mOneButtonBlasterEnabled;
	uint8_t mOneButtonLockupEnabled;
	uint8_t mUseFontBootSounds;
	uint8_t mBladeLength; //Blade length for pixel blades (ignored for RGB)
};

//Defines all settings to be saved in EEPROM
struct tSaberSettings
{
	uint8_t mCheckByteZero;
	uint16_t mSettingsSize;

	uint8_t mSoundVolume;

	tOptions mOptions;

	//User-defined custom motion settings
	tMotionParameters mMotion;

	tBladeColor maColorPresets[MAX_COLOR_PRESETS]; //Color presets (36 bytes)

	uint8_t mSelectedProfileIndex;
	tProfile maProfiles[MAX_PROFILES];

	uint8_t mCheckByteLast;

};

#endif /* SETTINGS_H_ */
