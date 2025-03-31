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
 * StreamControl.cpp
 *
 *  Created on: May 17, 2017
 *      Author: JakeSoft
 */

#include "StreamControl.h"
#include <Arduino.h>
#include "StreamMessages.h"
#include "Utility.h"
#include "SaberActions.h"
#include "Globals.h"

StreamControl::StreamControl(SettingsManager* apSettingsManager,
		                       IBladeManager* apBlade,
							   ASoundPlayer* apSoundPlayer,
							   AMotionManager* apMotionManager) :
mDisconnect(false),
mBladeControlEnabled(false),
mFirmwareVersionMajor(0),
mFirmwareVersionMinor(1),
mBladeEffect(0),
mFlickerEnabled(false),
mIsConnected(false)
{
	//Do nothing, just initializers
}

StreamControl::~StreamControl()
{
	//Do nothing
}

void StreamControl::Init()
{

	mDisconnect = false;
	mIsConnected = true;
	//Dump everything off the serial buffer and get ready for commands
	Serial.flush();
}

void StreamControl::Operate()
{
	if(this->mFlickerEnabled)
    {
    	gpBlade->ApplyFlicker(mBladeEffect);
    }

	RcvCmd();
}

void StreamControl::RcvCmd()
{

	if (Serial.available() > 0)
    {
    	// read the incoming byte:
        char lCmdByte = (char)Serial.read();

        switch(lCmdByte)
        {
        case CommandMessages::eeCmdDisconnect:
        	ProcessDisconnect();
        	break;
        case CommandMessages::eeCmdSetMotionConfig:
        	ProcessSetMotionConfig();
        	break;
        case CommandMessages::eeCmdSetSoundConfig:
        	ProcessSetSoundConfig();
			break;
        case CommandMessages::eeCmdSetBladeConfig:
        	ProcessSetBladeConfig();
			break;
        case CommandMessages::eeCmdRequestMotionConfig:
        	ProcessRequestMotionConfig();
			break;
        case CommandMessages::eeCmdRequestSoundConfig:
        	ProcessRequestSoundConfig();
			break;
        case CommandMessages::eeCmdRequestBladeConfig:
        	ProcessRequestBladeConfig();
			break;
        case CommandMessages::eeCmdRequestAck:
        	ProcessRequestAck();
			break;
        case CommandMessages::eeCmdRequestProtocolVersion:
        {
        	ResponseMessages::tProtocolVersionDataMsg lsMsg;
        	lsMsg.mVersionMajor = PROTOCOLVERSIONMAJOR;
        	lsMsg.mVersionMinor = PROTOCOLVERSIONMINOR;

        	char* lsMsgPtr = (char*) &lsMsg;
        	Serial.write(lsMsgPtr, sizeof(ResponseMessages::tProtocolVersionDataMsg));
        }
        	break;
        case CommandMessages::eeCmdRequestFirmwareVersion:
        {
        	ResponseMessages::tFirmwareVersionDataMsg lsMsg;
        	lsMsg.mVersionMajor = mFirmwareVersionMajor;
        	lsMsg.mVersionMinor = mFirmwareVersionMinor;

        	char* lsMsgPtr = (char*) &lsMsg;
        	Serial.write(lsMsgPtr, sizeof(ResponseMessages::tFirmwareVersionDataMsg));
        }
        	break;
        case CommandMessages::eeCmdBladeControlEnable:
        	ProcessBladeControlEnable();
        	break;
        case CommandMessages::eeCmdBladeControlSetEffect:
        	ProcessBladeControlSetEffect();
        	break;
        case CommandMessages::eeCmdBladeControlSetChannel:
        	ProcessBladeControlSetChannel();
        	break;
        case CommandMessages::eeCmdBladeControlPower:
        	ProcessBladeControlPower();
        	break;
        case CommandMessages::eeCmdSoundControlEnable:
        	break;
        case CommandMessages::eeCmdSoundControlSetVolume:
        	break;
        case CommandMessages::eeCmdLoadDefaultSettings:
        	gSettingsManager.LoadDefaultSettings();
        	break;
        case CommandMessages::eeCmdSaveSettngs:
        	ProcessSaveSettings();
        	break;
        case CommandMessages::eeCmdPowerUpPowerDownTest:
        	ProcessPowerUpPowerDown();
        	break;
        case CommandMessages::eeCmdRequestColorPresets:
        	ProcessRequestColorPresets();
        	break;
        case CommandMessages::eeCmdSetColorPresets:
        	ProcessSetColorPresets();
        	break;
        case CommandMessages::eeCmdHumRestartTest:
        	ProcessHumRestartTest();
        	break;
        case CommandMessages::eeCmdRequestSaberInfo:
        	ProcessRequestSaberInfo();
        	break;
        case CommandMessages::eeCmdSwitchProfile:
        	ProcessSwitchProfile();
        	break;
        case CommandMessages::eeCmdRequestOptionsConfig:
        	ProcessRequestOptions();
        	break;
        case CommandMessages::eeCmdSetOptionsConfig:
        	ProcessSetOptions();
        	break;
        case CommandMessages::eeCmdLockupTest:
        	ProcessLockupTest();
        	break;
        default:
        	break;
        	//Read everything to clear the buffer of meaningless data
        	//that may be associated with this unknown command
        	while(Serial.available() > 0)
        	{
        		Serial.read();
        	}
        }

    }
}

void StreamControl::SendAck(uint8_t aMsgId)
{
	ResponseMessages::tAckMsg lAckMsg;
	lAckMsg.mAckMsgPayload = aMsgId;

	char* lpMsgPtr = (char*) &lAckMsg;
	Serial.write(lpMsgPtr, sizeof(ResponseMessages::tAckMsg));
}

void StreamControl::ProcessRequestAck()
{
	SendAck(CommandMessages::eeCmdRequestAck);
}

void StreamControl::ProcessDisconnect()
{
	Disconnect();
}

void StreamControl::ProcessLoadDefaults()
{
	Serial.println("Command: Load defaults.");
	//gSettingsManager.LoadDefaultSettings();
}

void StreamControl::ProcessBladeControlEnable()
{
	//Read the rest of the message off the serial buffer
	CommandMessages::tBladeControlEnableMsg* lBladeControlEnableMsg;
	char* lpMsgPtr = (char*) &lBladeControlEnableMsg;
	Serial.readBytes( &lpMsgPtr[1], (sizeof(CommandMessages::tBladeControlEnableMsg) - 1) );

	if(lBladeControlEnableMsg->mEnable >= 1)
	{
		mBladeControlEnabled = true;
	}
	else
	{
		mBladeControlEnabled = false;
	}

}

void StreamControl::ProcessBladeControlSetChannel()
{
	//Read the rest of the message off the serial buffer
	const int lMsgSize = sizeof(CommandMessages::tBladeControlSetChannelMsg);
	CommandMessages::tBladeControlSetChannelMsg lSetChannelMsg;
	char laReadBuffer[lMsgSize];

	Serial.readBytes(laReadBuffer, lMsgSize );

	memcpy(&lSetChannelMsg, laReadBuffer, lMsgSize);


	gpBlade->SetChannel(lSetChannelMsg.mValue,
							  (int)lSetChannelMsg.mChannel);
}

void StreamControl::ProcessBladeControlPower()
{
	//Read the rest of the message off the serial buffer
	CommandMessages::tBladeControlSetPowerMsg lSetPwrMsg;
	char* lpMsgPtr = (char*) &lSetPwrMsg;
	Serial.readBytes(&lpMsgPtr[1], (sizeof(CommandMessages::tBladeControlSetPowerMsg) - 1) );

	if(lSetPwrMsg.mPower >= 1)
	{
		gpBlade->On();
	}
	else
	{
		gpBlade->Off();
	}
}

void StreamControl::ProcessBladeControlSetEffect()
{
	//Read the rest of the message off the serial buffer
	CommandMessages::tBladeControlSetEffectMsg lSetEffectMsg;
	char* lpMsgPtr = (char*) &lSetEffectMsg;
	Serial.readBytes(&lpMsgPtr[1], (sizeof(CommandMessages::tBladeControlSetEffectMsg) - 1) );

	mBladeEffect = (int)lSetEffectMsg.mEffect;
	mFlickerEnabled = (lSetEffectMsg.mEnableNow != 0);
}

void StreamControl::ProcessPowerUpPowerDown()
{
	//Read the rest of the message off the serial buffer
	CommandMessages::tPowerUpPowerDownTestMsg lMsg;
	char* lpMsgPtr = (char*) &lMsg;
	Serial.readBytes(&lpMsgPtr[1], (sizeof(CommandMessages::tPowerUpPowerDownTestMsg) - 1) );

	SaberActions::PerformPowerUp(gpSoundPlayer, gpBlade, lMsg.mBladeColor, lMsg.mPowerOnDelay, lMsg.mPowerupTime);

	//Stay on and apply blade flicker until commanded timeout
	unsigned long lWaitStartTime = millis();
	unsigned long lOnDuration = (unsigned long)lMsg.mOnDuration;
	lOnDuration *= 1000U;
	while(millis() - lWaitStartTime < lOnDuration)
	{
		gpBlade->ApplyFlicker(lMsg.mFlickerType);
	}

	SaberActions::PerformPowerDown(gpSoundPlayer, gpBlade, lMsg.mPowerdownTime);

	gpBlade->Off();
}

void StreamControl::ProcessSetMotionConfig()
{
	//Read the rest of the message off the serial buffer
	CommandMessages::tSetMotionConfigMsg lSetMotionConfigMsg;
	char* lpMsgPtr = (char*) &lSetMotionConfigMsg;
	Serial.readBytes( &lpMsgPtr[1], (sizeof(CommandMessages::tSetMotionConfigMsg) - 1) );

	//Fetch a pointer to the current motion settings
	tMotionParameters* lpMotionParameters =	gSettingsManager.GetMotionSettings();

	//Overwrite the current settings with data from the message
	memcpy(lpMotionParameters, &lSetMotionConfigMsg.mParameters, sizeof(tMotionParameters));
}

void StreamControl::ProcessSetSoundConfig()
{
	//delay(1000);

	//Read the size bytes (and drop them on the floor)
	uint16_t lSize;
	Serial.readBytes((char*)&lSize, 2);

	//Fetch a pointer to the current sound settings
    tTimingParameters* lpSoundParameters = gSettingsManager.GetSoundSettings();

    //Read data from the message to overwrite the current settings
    Serial.readBytes((char*)lpSoundParameters, sizeof(tTimingParameters));

/*
	//Read the rest of the message off the serial buffer
	CommandMessages::tSetSoundConfigMsg lSetSoundConfigMsg;

	memset(&lSetSoundConfigMsg, 0, sizeof(CommandMessages::tSetSoundConfigMsg));

	char* lpMsgPtr = (char*) &lSetSoundConfigMsg;
	Serial.readBytes( &lpMsgPtr[1], (sizeof(CommandMessages::tSetSoundConfigMsg) - 1) );

	//Fetch a pointer to the current sound settings
	tSoundParameters* lpSoundParameters = gSettingsManager.GetSoundSettings();

	//Overwrite the current settings with data from the message
	memcpy(lpSoundParameters, &lSetSoundConfigMsg.mParameters, sizeof(tSoundParameters));
*/
}

void StreamControl::ProcessSetBladeConfig()
{
	//Read the size bytes (and drop them on the floor)
	uint16_t lSize;
	Serial.readBytes((char*)&lSize, 2);

	//Read blade data directly into the current profile
	tProfile* lpLocalProfile = gSettingsManager.GetSelectedPofile();
	Serial.readBytes((char*)(&(lpLocalProfile->mBlade)), sizeof(tBladeParameters));

	//Read the rest of the message off the serial buffer
//	CommandMessages::tSetBladeConfigMsg lSetBladeConfigMsg;
//	char* lpMsgPtr = (char*) &lSetBladeConfigMsg;
//	Serial.readBytes( &lpMsgPtr[1], (sizeof(CommandMessages::tSetBladeConfigMsg) - 1) );
//
//	//Fetch a pointer to the current settings
//	tProfile* lpLocalProfile = gSettingsManager.GetSelectedPofile();
//
//	lpLocalProfile->mBlade.mFlashColorIndex = lSetBladeConfigMsg.mParameters.mFlashColorIndex;
//	lpLocalProfile->mBlade.mFlickerIndex = lSetBladeConfigMsg.mParameters.mFlickerIndex;
//	lpLocalProfile->mBlade.mMainColorIndex = lSetBladeConfigMsg.mParameters.mMainColorIndex;
//	lpLocalProfile->mBlade.mLockupIntensity = lSetBladeConfigMsg.mParameters.mLockupIntensity;

}


void StreamControl::ProcessRequestMotionConfig()
{
	//Fetch current settings
	tMotionParameters* lpMotionSettings = gSettingsManager.GetMotionSettings();

	//Create response message
	ResponseMessages::tMotionConfigDataMsg lsMsg;
	lsMsg.mSize = sizeof(tMotionParameters);
	//Copy the settings into the message
	memcpy(&lsMsg.mParameters, lpMotionSettings, sizeof(tMotionParameters));

	//Send the response message
	Serial.write( (const char*)&lsMsg, sizeof(ResponseMessages::tMotionConfigDataMsg));

}

void StreamControl::ProcessRequestSoundConfig()
{
	//Fetch current settings
	tTimingParameters* lpSoundSettings = gSettingsManager.GetSoundSettings();

	//Create response message
	ResponseMessages::tSoundConfigDataMsg lsMsg;
	lsMsg.mSize = sizeof(tTimingParameters);
	//Copy the settings into the message
	memcpy(&lsMsg.mParameters, lpSoundSettings, sizeof(tTimingParameters));

	//Send the response message
	Serial.write( (const char*)&lsMsg, sizeof(ResponseMessages::tSoundConfigDataMsg));
}

void StreamControl::ProcessRequestBladeConfig()
{
	//Fetch current settings
	tBladeParameters* lpBladeSettings = gSettingsManager.GetBladeSettings();

	//Create response message
	ResponseMessages::tBladeConfigDataMsg lsMsg;
	lsMsg.mSize = sizeof(tBladeParameters);

	//Copy the settings into the message
	memcpy(&lsMsg.mParameters, lpBladeSettings, sizeof(tBladeParameters));

	//Send the response message
	Serial.write( (const char*)&lsMsg, sizeof(ResponseMessages::tBladeConfigDataMsg));
}

void StreamControl::ProcessRequestColorPresets()
{
	//Fetch current settings
	tSaberSettings* lpSettings = gSettingsManager.GetSettings();

	//Create a response message
	ResponseMessages::tColorPresetsDataMsg lsMsg;
	lsMsg.mSize = sizeof(ResponseMessages::tColorPresetsDataMsg);
	lsMsg.mNumPresets = MAX_COLOR_PRESETS;

//	memcpy(&lsMsg.maColorPresets, lpBladeColors, sizeof(tBladeColor*MAX_COLOR_PRESETS));

	//Explicitly set color presets
	for(int lnIdx = 0; lnIdx < MAX_COLOR_PRESETS; lnIdx++)
	{
		lsMsg.maColorPresets[lnIdx].mChannel1
		= lpSettings->maColorPresets[lnIdx].mChannel1;
		lsMsg.maColorPresets[lnIdx].mChannel2
		= lpSettings->maColorPresets[lnIdx].mChannel2;
		lsMsg.maColorPresets[lnIdx].mChannel3
		= lpSettings->maColorPresets[lnIdx].mChannel3;
	}

	//Send the response message
	Serial.write( (const char*)&lsMsg, sizeof(ResponseMessages::tColorPresetsDataMsg));
}

void StreamControl::ProcessSetColorPresets()
{
	//Fetch current settings
	tSaberSettings* lpSettings = gSettingsManager.GetSettings();

	delay(1000);
	//Read the rest of the message off the serial buffer
	CommandMessages::tSetColorPresetsMsg lSetColorPresetsMsg;

	//Fetch the number of presets
	Serial.readBytes(&lSetColorPresetsMsg.mNumPresets, 1);

	//Read the presets off the serial buffer
	for(int lnIdx = 0;
		lnIdx < lSetColorPresetsMsg.mNumPresets && lnIdx < MAX_COLOR_PRESETS;
		lnIdx++)
	{
		Serial.readBytes((char*) &lSetColorPresetsMsg.maColorPresets[lnIdx], sizeof(tBladeColor));
	}

	//Now overwrite current presets with commanded settings
	for(int lnIdx = 0;
	    lnIdx < lSetColorPresetsMsg.mNumPresets && lnIdx < MAX_COLOR_PRESETS;
		lnIdx++)
	{
		lpSettings->maColorPresets[lnIdx] = lSetColorPresetsMsg.maColorPresets[lnIdx];
	}

}

void StreamControl::ProcessHumRestartTest()
{
	CommandMessages::tHumRepeatIntervalTestMsg lsMsg;

	//Read the rest of the message off the serial buffer
	Serial.readBytes((char*)&lsMsg.mInterval, sizeof(uint16_t));

	gpSoundPlayer->PlaySound(eePowerUpSnd, 0);
	delay(lsMsg.mInterval);
	gpSoundPlayer->PlaySound(eeHumSnd, 0);
	delay(lsMsg.mInterval/2);
	gpSoundPlayer->PlayRandomSound(eeClashSnd);
	delay(lsMsg.mInterval);
	gpSoundPlayer->PlaySound(eeHumSnd, 0);
	delay(lsMsg.mInterval/2);
	gpSoundPlayer->PlayRandomSound(eeSwingSnd);
	delay(lsMsg.mInterval);
	gpSoundPlayer->PlaySound(eeHumSnd, 0);
	delay(lsMsg.mInterval/2);
	gpSoundPlayer->PlaySound(eePowerDownSnd, 0);
	delay(100);
}

void StreamControl::ProcessSaveSettings()
{
	//Write settings to EEPROM
	gSettingsManager.Save();

	//Set new blade length
	gpBladeEffects->SetBladeLength((int)gSettingsManager.GetOptions()->mBladeLength);

	//Blink the blade so user knows save happened
	SaberActions::SetBladeToMainColor(gpBlade, &gSettingsManager);
	delay(200);
	gpBlade->Off();
}

void StreamControl::ProcessRequestSaberInfo()
{
	//Create response message
	ResponseMessages::tSaberInfoDataMsg lsMsg;
	lsMsg.mNumBladeChannels = (uint8_t)gpBlade->GetFeatures().Channels;
	lsMsg.mNumBladeFlickers = (uint8_t)gpBlade->GetFeatures().Flickers;
	lsMsg.mMaxVolume = gpSoundPlayer->Features().MaxVolume;
	lsMsg.mSelectedProfileIndex = (uint8_t)gSettingsManager.GetSelectedProfileIndex();

	//Send the response message
	Serial.write( (const char*)&lsMsg, sizeof(ResponseMessages::tSaberInfoDataMsg));
}

void StreamControl::ProcessSwitchProfile()
{
	CommandMessages::tSwitchProfileMsg lsMsg;

//	lsMsg.mMsgId = CommandMessages::eeCmdSwitchProfile;
	Serial.readBytes(&lsMsg.mProfileIdx, sizeof(uint8_t));

	SaberActions::SwitchProfile((int)lsMsg.mProfileIdx,
			                    &gSettingsManager,
								gpSoundPlayer,
								&gMotion);
}

void StreamControl::ProcessRequestOptions()
{
	//Create response message
	ResponseMessages::tSaberOptionsDataMsg lsMsg;

	//Copy current options in to the message
	memcpy(&lsMsg.mOptions, gSettingsManager.GetOptions(), sizeof(tOptions));

	//Send the response message
	Serial.write( (const char*)&lsMsg, sizeof(ResponseMessages::tSaberOptionsDataMsg));
}

void StreamControl::ProcessSetOptions()
{
	//Fetch pointer to current options
	tOptions* lpOptions = gSettingsManager.GetOptions();

	//Read the rest of the message off the buffer and overwrite options with
	//contents of the message
	Serial.readBytes((unsigned char*)lpOptions, sizeof(tOptions));
}

void StreamControl::ProcessLockupTest()
{
	//Read the rest of the message into a local
	CommandMessages::tLockupTestMsg lLockupTestMsg;
	char* lpMsgPtr = (char*) &lLockupTestMsg;
	Serial.readBytes( &lpMsgPtr[1], (sizeof(CommandMessages::tLockupTestMsg) - 1) );

	tBladeColor lMainColor;
	memcpy(&lMainColor, lLockupTestMsg.mMainColorChannels, 3);

	tBladeColor lLockupColor;
	memcpy(&lLockupColor, lLockupTestMsg.mLockupColorChannels, 3);

	unsigned long lStartTime = millis();

	while(millis() - lStartTime < (lLockupTestMsg.mTestLenSec * 1000UL) )
	{
		SaberActions::ApplyLockupEffect(gpBlade,
									lMainColor,
									lLockupColor,
									lLockupTestMsg.mColorIntensity,
									lLockupTestMsg.mFlickerIntensity,
									lLockupTestMsg.mFramePeriod);
     }

}
