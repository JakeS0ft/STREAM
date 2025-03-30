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
 * SaberActions.h
 *
 *  Created on: Jan 20, 2018
 *      Author: JakeSoft
 */

#ifndef SABERACTIONS_H_
#define SABERACTIONS_H_

#include <USaber.h>
#include "Settings.h"
#include "SettingsManager.h"

namespace SaberActions
{

/**
 * Sets the blade to a color.
 * Args:
 *   apBlade - Blade to use
 *   arColor - Color to use
 *   aTurnOn - Turn on the blade right away after setting the color
 */
void SetBladeColor(IBladeManager* apBlade,
		           const tBladeColor& arColor,
				   bool aTurnOn = false);

/**
 * Set blade to main color per current profile.
 * Args:
 *   apBlade - Blade to set
 *   apSettings - Settings manager to get color data from
 */
void SetBladeToMainColor(IBladeManager* apBlade,
		                 SettingsManager* apSettings);

/**
 * Set blade to flash color per current profile.
 * Args:
 *   apBlade - Blade to set
 *   apSettings - Settings manager to get color data from*
 */
void SetBladeToFlashColor(IBladeManager* apBlade,
	 	   	   	   	   	  SettingsManager* apSettings);

/**
 * Play power up sound and turn on the blade.
 *
 */
void PerformPowerUp	(ASoundPlayer* apSoundPlayer,
                     IBladeManager* apBlade,
					 const tBladeColor& arBladeColor,
					 uint16_t& arBladeDelay,
					 uint16_t& arBladeRampTime);
/**
 * Play power down sound and turn off the blade.
 */
void PerformPowerDown(ASoundPlayer* apSoundPlayer,
                     IBladeManager* apBlade,
					 uint16_t& arBladeRampTime);

/**
 * Switch current profile.
 */
void SwitchProfile(int aProfileIdx,
		           SettingsManager* apSettings,
				   ASoundPlayer* apSoundPlayer,
				   AMotionManager* apMotion);

/**
 * Apply lockup effect
 */
void ApplyLockupEffect(IBladeManager* apBlade,
        const tBladeColor& aMainColor, //Main Color
        const tBladeColor& aLockupColor, //Lockup/clash color
        const int& aColorIntensity,
		const int& aFlickerIntensity,
		const unsigned int& aFrameUpdatePeriod);

/**
 * Apply sound volume per settings
 */
void SetSoundVolume(const int aVolume,
		            ASoundPlayer* apSoundPlayer);

} //End namespace


#endif /* SABERACTIONS_H_ */
