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
 * SettingManager.h
 *
 *  Created on: Aug 16, 2017
 *      Author: Jake
 */

#ifndef SETTINGSMANAGER_H_
#define SETTINGSMANAGER_H_

#include "Settings.h"

/**
 * This class will manage loading, saving, and access of user configurable
 * settings.
 */
class SettingsManager {
public:
	SettingsManager();
	virtual ~SettingsManager();

	// Load settings from EEPROM. Returns number of bytes loaded.
	int Load();

	// Load default settings and ignore EEPROM
	void LoadDefaultSettings();

	// Save settings to EEPROM. Returns number of bytes saved.
	int Save();

	//Return a pointer to all settings
	virtual tSaberSettings* GetSettings();

	//Return a pointer to motion parameter settings
	virtual tMotionParameters* GetMotionSettings();

	//Return a pointer to sound parameter settings
	virtual tTimingParameters* GetSoundSettings();

	//Return a pointer to blade settings
	virtual tBladeParameters* GetBladeSettings();

	//Return a pointer to general options
	virtual tOptions* GetOptions();

	//Fetch the selected profile index
	virtual int GetSelectedProfileIndex();

	//Set the selected profile
	virtual void SetSelectedProfile(int aIdx);

	//Scroll to next available profile
	virtual void SelectNextProfile();

	//Fetch currently selected user profile
	virtual tProfile* GetSelectedPofile();

	//Fetch the RGB channel values for the currently selected profile
	const tBladeColor GetNormalBladeColor();

	//Fetch the RGB channel values for the currently selected profile
	const tBladeColor GetFlashBladeColor();

	//Fetch the hum relaunch interval per current profile
	unsigned long GetHumRelaunchInterval();

	//Check settings validity
	bool IsSettingsValid();
private:
	//Calculate checksum for error checking
//	unsigned long CalculateChecksum();

	//Internal structure to store active settings
	tSaberSettings mSettings;
};


#endif /* SETTINGSMANAGER_H_ */
