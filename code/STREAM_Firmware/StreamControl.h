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
 * tcStreamControl.h
 *
 *  Created on: May 17, 2017
 *      Author: JakeSoft
 */

#ifndef STREAMCONTROL_H_
#define STREAMCONTROL_H_

#include <USaber.h>
#include "Settings.h"
#include "SettingsManager.h"

#define PROTOCOLVERSIONMAJOR 1
#define PROTOCOLVERSIONMINOR 5

class StreamControl
{
public:
	/**
	 * Constructor.
	 *   Args:
	 *     apSettingsManager - Settings manager to handle EEPROM load/save
	 *     apBladeManager - Pointer to active Blade object
	 *     apSoundPlayer - Pointer to active sound player object
	 */
	StreamControl(SettingsManager* apSettingsManager,
			       IBladeManager* apBlade,
				   ASoundPlayer* apSoundPlayer,
				   AMotionManager* apMotionManager);

	/**
	 * Destructor.
	 */
	virtual ~StreamControl();

	virtual void Init();

	virtual void Operate();

	/**
	 * Sets firmware version that will be reported upon request.
	 *   Args:
	 *     aMajor - Major version number
	 *     aMinor - Minor version number
	 */
	inline void SetFirmwareVersion(uint8_t aMajor, uint8_t aMinor)
	{
		mFirmwareVersionMajor = aMajor;
		mFirmwareVersionMinor = aMinor;
	}

	inline void Disconnect()
	{
		mDisconnect = true;
		mIsConnected = false;
	}

	bool IsConnected()
	{
		return mIsConnected;
	}
protected:
	/**
	 * Receive serial commands
	 */
	virtual void RcvCmd();

	virtual void SendAck(uint8_t aMsgId);

	/**
	 * Process commands
	 */
	virtual void ProcessRequestAck();

	virtual void ProcessDisconnect();

	virtual void ProcessLoadDefaults();

	virtual void ProcessBladeControlEnable();

	virtual void ProcessBladeControlSetChannel();

	virtual void ProcessBladeControlPower();

	virtual void ProcessBladeControlSetEffect();

	virtual void ProcessPowerUpPowerDown();

	virtual void ProcessSetMotionConfig();

	virtual void ProcessSetSoundConfig();

	virtual void ProcessSetBladeConfig();

	virtual void ProcessRequestMotionConfig();

	virtual void ProcessRequestSoundConfig();

	virtual void ProcessRequestBladeConfig();

	virtual void ProcessSaveSettings();

	virtual void ProcessRequestColorPresets();

	virtual void ProcessSetColorPresets();

	virtual void ProcessHumRestartTest();

	virtual void ProcessRequestSaberInfo();

	virtual void ProcessSwitchProfile();

	virtual void ProcessRequestOptions();

	virtual void ProcessSetOptions();

	virtual void ProcessLockupTest();

	bool mDisconnect;
	bool mIsConnected;
	bool mBladeControlEnabled;
	uint8_t mFirmwareVersionMajor;
	uint8_t mFirmwareVersionMinor;

	//Active blade effect parameters
	int mBladeEffect;
	bool mFlickerEnabled;

};

#endif /* STREAMCONTROL_H_ */
