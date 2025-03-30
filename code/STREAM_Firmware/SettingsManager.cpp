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
 * SettingsManager.cpp
 *
 *  Created on: Aug 16, 2017
 *      Author: Jake
 */

#include "SettingsManager.h"
#include <EEPROM.h>

SettingsManager::SettingsManager()
{
	//Do nothing
}

SettingsManager::~SettingsManager()
{
	//Do nothing
}

int SettingsManager::Save()
{
	  unsigned char* lpnSaveByte;
	  lpnSaveByte = (unsigned char*)(&mSettings); //save data from the memory map
	  unsigned int lnLoopIndex;

	  for(lnLoopIndex = 0; lnLoopIndex < sizeof(tSaberSettings); lnLoopIndex++)
	  {
		  //Write the new byte to EEPROM, but only if it's changed
		  if(EEPROM.read(lnLoopIndex) != lpnSaveByte[lnLoopIndex])
		  {
			  EEPROM.write(lnLoopIndex, lpnSaveByte[lnLoopIndex]);
		  }
	  }

	  return(lnLoopIndex);
}

tSaberSettings* SettingsManager::GetSettings()
{
	return &mSettings;
}

int SettingsManager::Load()
{
	  unsigned char* lpnLoadByte;
	  lpnLoadByte = (unsigned char*)(&mSettings); //load data to the memory map
	  unsigned int lnLoopIndex;

	  for(lnLoopIndex = 0; lnLoopIndex < sizeof(tSaberSettings); lnLoopIndex++)
	  {
	     lpnLoadByte[lnLoopIndex] = (unsigned char)(EEPROM.read(lnLoopIndex));
	  }

	  return(lnLoopIndex);
}

//Return a pointer to motion parameter settings
tMotionParameters* SettingsManager::GetMotionSettings()
{
	return &mSettings.mMotion;
}

//Return a pointer to sound parameter settings
tTimingParameters* SettingsManager::GetSoundSettings()
{
	return &mSettings.maProfiles[mSettings.mSelectedProfileIndex].mSound;
}

//Return a pointer to blade settings
tBladeParameters* SettingsManager::GetBladeSettings()
{
	return &mSettings.maProfiles[mSettings.mSelectedProfileIndex].mBlade;
}

tOptions* SettingsManager::GetOptions()
{
	return &mSettings.mOptions;
}

void SettingsManager::LoadDefaultSettings()
{

	/**
	 * Validation variables
	 */
	mSettings.mCheckByteZero = SETTINGS_START;
	mSettings.mCheckByteLast = SETTINGS_END;
	mSettings.mSettingsSize = sizeof(tSaberSettings);

	mSettings.mSelectedProfileIndex = 0;
	mSettings.mSoundVolume = 20;

	/**
	 * Options
	 */
	mSettings.mOptions.mAutoPowerOffTimeSec = 0; //Never
	mSettings.mOptions.mExitLockupOnSwing = 1; //Yes
	mSettings.mOptions.mMenuSwitchHoldTimeMs = 3000; //3 seconds
	mSettings.mOptions.mPowerOffSwitchHoldTimeMs = 2500; //2.5 seconds
	mSettings.mOptions.mSleepTimeMins = 10; //10 minutes
	mSettings.mOptions.mOneButtonBlasterEnabled = 1; //Enabled
	mSettings.mOptions.mOneButtonLockupEnabled = 1; //Enabled
	mSettings.mOptions.mUseFontBootSounds = 1; //Enabled
	mSettings.mOptions.mBladeLength = 120;

	/**
	 * Custom Motion parameters
	 */
	mSettings.mMotion.mSwingLargeTol = 512;
	mSettings.mMotion.mSwingMediumTol = 128;
	mSettings.mMotion.mSwingSmallTol = 50;
	mSettings.mMotion.mClashTol = 60;
	mSettings.mMotion.mTwistTol = 128;

	/**
	 * Profiles
	 */
	for(int lIdx = 0; lIdx < MAX_PROFILES; lIdx++)
	{
		mSettings.maProfiles[lIdx].mClashPresetIndex = 6;
		mSettings.maProfiles[lIdx].mSwingPresetIndex = 7;

		/**
		 * Sound and Sound Synchronization Parameters
		 */
		mSettings.maProfiles[lIdx].mSound.mPowerOnBladeDelay = 0;
		mSettings.maProfiles[lIdx].mSound.mPowerOnTime = 1000;
		mSettings.maProfiles[lIdx].mSound.mPowerOffTime = 1000;
		mSettings.maProfiles[lIdx].mSound.mBlasterFlashTime = 100;
		mSettings.maProfiles[lIdx].mSound.mBlasterSuppressTime = 250;
		mSettings.maProfiles[lIdx].mSound.mClashSwingSuppressTime = 1000;
		mSettings.maProfiles[lIdx].mSound.mMinSwingInterval = 100;
		mSettings.maProfiles[lIdx].mSound.mMaxSwingInterval = 1200;
		mSettings.maProfiles[lIdx].mSound.mHumRelaunchInterval = 8000; //8 seconds

		/**
		 * Blade Parameters
		 */
		//Selected main blade color
		mSettings.maProfiles[lIdx].mBlade.mMainColorIndex = 0;
		mSettings.maProfiles[lIdx].mBlade.mFlashColorIndex = 2;
		mSettings.maProfiles[lIdx].mBlade.mFlickerIndex = 0;
		mSettings.maProfiles[lIdx].mBlade.mLockupColorIntensity = 100;
		mSettings.maProfiles[lIdx].mBlade.mLockupFlickerIntensity = 0;
		mSettings.maProfiles[lIdx].mBlade.mLockupFramePeriod = 20;
	}

	for(int lnIdx = 0; lnIdx < MAX_COLOR_PRESETS; lnIdx++)
	{
		memset(&mSettings.maColorPresets[lnIdx], 0, sizeof(tBladeColor));
	}

	mSettings.maColorPresets[0].mChannel1 = 255;
	mSettings.maColorPresets[0].mChannel2 = 0;
	mSettings.maColorPresets[0].mChannel3 = 0;

	mSettings.maColorPresets[1].mChannel1 = 255;
	mSettings.maColorPresets[1].mChannel2 = 127;
	mSettings.maColorPresets[1].mChannel3 = 0;

	mSettings.maColorPresets[2].mChannel1 = 255;
	mSettings.maColorPresets[2].mChannel2 = 255;
	mSettings.maColorPresets[2].mChannel3 = 0;

	mSettings.maColorPresets[3].mChannel1 = 127;
	mSettings.maColorPresets[3].mChannel2 = 255;
	mSettings.maColorPresets[3].mChannel3 = 0;

	mSettings.maColorPresets[4].mChannel1 = 0;
	mSettings.maColorPresets[4].mChannel2 = 255;
	mSettings.maColorPresets[4].mChannel3 = 0;

	mSettings.maColorPresets[5].mChannel1 = 0;
	mSettings.maColorPresets[5].mChannel2 = 255;
	mSettings.maColorPresets[5].mChannel3 = 127;

	mSettings.maColorPresets[6].mChannel1 = 0;
	mSettings.maColorPresets[6].mChannel2 = 255;
	mSettings.maColorPresets[6].mChannel3 = 255;

	mSettings.maColorPresets[7].mChannel1 = 0;
	mSettings.maColorPresets[7].mChannel2 = 127;
	mSettings.maColorPresets[7].mChannel3 = 255;

	mSettings.maColorPresets[8].mChannel1 = 0;
	mSettings.maColorPresets[8].mChannel2 = 0;
	mSettings.maColorPresets[8].mChannel3 = 255;

	mSettings.maColorPresets[9].mChannel1 = 127;
	mSettings.maColorPresets[9].mChannel2 = 0;
	mSettings.maColorPresets[9].mChannel3 = 255;

	mSettings.maColorPresets[10].mChannel1 = 255;
	mSettings.maColorPresets[10].mChannel2 = 0;
	mSettings.maColorPresets[10].mChannel3 = 255;

	mSettings.maColorPresets[11].mChannel1 = 255;
	mSettings.maColorPresets[11].mChannel2 = 0;
	mSettings.maColorPresets[11].mChannel3 = 127;

}

//Fetch the selected profile index
int SettingsManager::GetSelectedProfileIndex()
{
	return (int)(mSettings.mSelectedProfileIndex);
}

//Set the selected profile
void SettingsManager::SetSelectedProfile(int aIdx)
{
	mSettings.mSelectedProfileIndex = (uint8_t)aIdx;
}

//Scroll to next available profile
void SettingsManager::SelectNextProfile()
{
	if(mSettings.mSelectedProfileIndex < MAX_PROFILES-1)
	{
		mSettings.mSelectedProfileIndex++;
	}
	else
	{
		mSettings.mSelectedProfileIndex = 0;
	}
}

tProfile* SettingsManager::GetSelectedPofile()
{
	return &mSettings.maProfiles[mSettings.mSelectedProfileIndex];
}

const tBladeColor SettingsManager::GetNormalBladeColor()
{
	return mSettings.maColorPresets[GetSelectedPofile()->mBlade.mMainColorIndex];
}

const tBladeColor SettingsManager::GetFlashBladeColor()
{
	return mSettings.maColorPresets[GetSelectedPofile()->mBlade.mFlashColorIndex];
}

unsigned long SettingsManager::GetHumRelaunchInterval()
{
	return (unsigned long)(GetSelectedPofile()->mSound.mHumRelaunchInterval);
}

bool SettingsManager::IsSettingsValid()
{
	bool lValid = true;

	//Check start byte
	lValid &= (SETTINGS_START == mSettings.mCheckByteZero);

	//Check if size is correct
	lValid &= (sizeof(tSaberSettings) == mSettings.mSettingsSize);

	//Check sound volume validity
	lValid &= (mSettings.mSoundVolume >= 5 && mSettings.mSoundVolume <= 30);
	//Check that a valid profile is selected
	lValid &= mSettings.mSelectedProfileIndex < MAX_PROFILES;
	//Check that exit lockup on swing flag is valid
	lValid &= (mSettings.mOptions.mExitLockupOnSwing <= 1);

	//Check end byte
	lValid &= (SETTINGS_END == mSettings.mCheckByteLast);

	return lValid;
}

//unsigned long SettingsManager::CalculateChecksum()
//{
//	uint32_t lCheckSum = 0;
//	unsigned char* lCurByte;
//	lCurByte = (unsigned char*)(&mSettings); //point to 1st byte of settings
//
//
//	for(unsigned int lnLoopIndex = 0; lnLoopIndex < sizeof(tSaberSettings - sizeof(uint32_t)); lnLoopIndex++)
//	{
//		lCheckSum += (unsigned long)(*lCurByte);
//	}
//
//	return lCheckSum;
//}
