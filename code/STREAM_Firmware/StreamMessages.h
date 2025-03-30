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
 * StreamMessages.h
 *
 *  Created on: May 21, 2017
 *      Author: JakeSoft
 */

#ifndef STREAMMESSAGES_H_
#define STREAMMESSAGES_H_

#include <Arduino.h>
#include "Settings.h" //For settings payloads

//Define messages that saber serial control can respond with
namespace ResponseMessages
{

/**
 * Define all possible response message IDs
 */
enum MessageIds
{
	eeAck,				   		//0
	eeMotionConfigData,    		//1
	eeSoundConfigData,     		//2
	eeBladeConfigData,     		//3
	eeProtocolVersionData, 		//4
	eeFirmwareVersionData, 		//5
	eeProductIdData,       		//6
	eeColorPresetsConfigData, 	//7
	eeSaberInfoData,            //8
	eeSaberOptionsData          //9
};

//Ack message
struct tAckMsg
{
	uint8_t mMsgId = eeAck;
	uint8_t mAckMsgPayload;
};

struct tMotionConfigDataMsg
{
	uint8_t mMsgId = eeMotionConfigData;
	uint16_t mSize;

	tMotionParameters mParameters;
};

struct tSoundConfigDataMsg
{
	uint8_t mMsgId = eeSoundConfigData;
	uint16_t mSize;

	tTimingParameters mParameters;
};

struct tBladeConfigDataMsg
{
	uint8_t mMsgId = eeBladeConfigData;
	uint16_t mSize;

	tBladeParameters mParameters;
};

struct tProtocolVersionDataMsg
{
	uint8_t mMsgId = eeProtocolVersionData;
	uint8_t mVersionMajor;
	uint8_t mVersionMinor;
};

struct tFirmwareVersionDataMsg
{
	uint8_t mMsgId = eeFirmwareVersionData;
	uint8_t mVersionMajor;
	uint8_t mVersionMinor;
};

struct tProductIdVersionDataMsg
{
	uint8_t mMsgId = eeProductIdData;
	uint8_t mSize;
	const char* mpProductIdStr;
};

struct tColorPresetsDataMsg
{
	uint8_t mMsgId = eeColorPresetsConfigData; // 1 byte
	uint16_t mSize;							   // 2 bytes

	uint8_t mNumPresets; //Number of color presets (1 byte)
	tBladeColor maColorPresets[MAX_COLOR_PRESETS]; //3*12=36 bytes
};

struct tSaberInfoDataMsg
{
	uint8_t mMsgId = eeSaberInfoData;
	uint8_t mNumberOfProfiles = MAX_PROFILES;
	uint8_t mNumberOfColorPresets = MAX_COLOR_PRESETS;
	uint8_t mNumBladeFlickers;
	uint8_t mNumBladeChannels;
	uint8_t mMaxVolume;
	uint8_t mSelectedProfileIndex;

//	uint8_t mProtocolVersionMajor;
//	uint8_t mProtocolVersionMinor;
//	uint8_t mProtocolVersionSubMinor;
//
//	uint8_t mFirmwareVersionMajor;
//	uint8_t mFirmwareVersionMinor
//	uint8_t mFirmwareVersionSubMinor;

};

struct tSaberOptionsDataMsg
{
	uint8_t mMsgId = eeSaberOptionsData;

	tOptions mOptions;
};

} //End Response Messages


//Define incoming messages that saber serial can service
namespace CommandMessages
{

/**
 * Defines all possible Command Message IDs
 */
enum MessageIds
{
    eeCmdDisconnect,            //0
    eeCmdSetMotionConfig,       //1
    eeCmdSetSoundConfig,        //2
    eeCmdSetBladeConfig,        //3
    eeCmdRequestMotionConfig,   //4
    eeCmdRequestSoundConfig,    //5
    eeCmdRequestBladeConfig,    //6
    eeCmdRequestAck,            //7
    eeCmdRequestProtocolVersion,//8
    eeCmdRequestFirmwareVersion,//9
    eeCmdRequestProductId,      //10
    eeCmdBladeControlEnable,    //11
    eeCmdBladeControlSetEffect, //12
    eeCmdBladeControlSetChannel,//13
    eeCmdBladeControlPower,     //14
    eeCmdSoundControlEnable,    //15
    eeCmdSoundControlSetVolume, //16
    eeCmdLoadDefaultSettings,   //17
    eeCmdSaveSettngs,           //18
    eeCmdPowerUpPowerDownTest,  //19
    eeCmdSoundControlPlaySound, //20
    eeCmdSoundControlStop,      //21
	eeCmdRequestColorPresets,	//22
	eeCmdSetColorPresets,       //23
	eeCmdHumRestartTest,        //24
	eeCmdRequestSaberInfo,      //25
	eeCmdSwitchProfile,         //26
	eeCmdRequestOptionsConfig,  //27
	eeCmdSetOptionsConfig,      //28
	eeCmdLockupTest             //29
};


struct tSetMotionConfigMsg
{
	uint8_t mMsgId;
	uint16_t mSize;

	tMotionParameters mParameters;

};

struct tSetSoundConfigMsg
{
	uint8_t mMsgId;
	uint16_t mSize;

	tTimingParameters mParameters;
};

struct tSetBladeConfigMsg
{
	uint8_t mMsgId;
	uint16_t mSize;

	tBladeParameters mParameters;
};

struct tBladeControlEnableMsg
{
	uint8_t mMsgId;
	uint8_t mEnable;
};

struct tBladeControlSetEffectMsg
{
	uint8_t mMsgId;
	uint8_t mEffect;
	uint8_t mEnableNow;
};

struct tBladeControlSetChannelMsg
{
	uint16_t mChannel;
	uint8_t mValue;
};

struct tBladeControlSetPowerMsg
{
	uint8_t mMsgId;
	uint8_t mPower;
};

struct tSoundControlEnableMsg
{
	uint8_t mMsgId;
	uint8_t mEnable;
};

struct tSoundControlSetVolumeMsg
{
	uint8_t mMsgId;
	uint8_t mVolume;
};

struct tPowerUpPowerDownTestMsg
{
	uint8_t mMsgId;

	uint16_t mPowerOnDelay; //Blade ramp delay
	uint16_t mPowerupTime; //Blade ramp up time (milliseconds)
	uint16_t mPowerdownTime; //Blade ramp down time (millseconds)
	uint8_t mFlickerType; //Blade effect to apply while on
	uint8_t mOnDuration; //How long to stay on (seconds)

	tBladeColor mBladeColor;
};

struct tSoundControlPlaySound
{
	uint8_t mMsgId;
	uint16_t mSoundType;
	uint16_t mSoundIndex;
};

struct tSoundControlStop
{
	uint8_t mMsgId;
};

struct tSetColorPresetsMsg
{
	uint8_t mMsgId;

	uint8_t mNumPresets; //Number of color presets (1 byte)
	tBladeColor maColorPresets[MAX_COLOR_PRESETS]; //3*12=36 bytes
};

struct tHumRepeatIntervalTestMsg
{
	uint8_t mMsgId;

	uint16_t mInterval;
};

struct tSwitchProfileMsg
{
	uint8_t mMsgId;

	uint8_t mProfileIdx;
};

struct tSetOptionsConfigMsg
{
	uint8_t mMsgId;

	tOptions mOptions;
};

struct tLockupTestMsg
{
	uint8_t mMsgId;

	uint8_t mTestLenSec;       //Length of test (seconds)
	uint8_t mColorIntensity;   //Color Intensity (%);
	uint8_t mFlickerIntensity; //Flicker Intensity (%)
	uint16_t mFramePeriod;     //Fame period (10 to 1000 ms)

	uint8_t mMainColorChannels[3];
	uint8_t mLockupColorChannels[3];
};

} //End Command Messages

#endif /* STREAMMESSAGES_H_ */
