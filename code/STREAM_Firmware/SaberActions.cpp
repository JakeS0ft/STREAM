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
 * SaberActions.cpp
 *
 *  Created on: Jan 20, 2018
 *      Author: JakeSoft
 */

#include "SaberActions.h"
#include "Utility.h"

extern MPU6050LiteTolData gToleranceData;

void SaberActions::PerformPowerUp
                    (ASoundPlayer* apSoundPlayer,
                     IBladeManager* apBlade,
					 const tBladeColor& arBladeColor,
					 uint16_t& arBladeDelay,
					 uint16_t& arBladeRampTime)
{
	//Play the power up sound
	apSoundPlayer->PlaySound(ESoundTypes::eePowerUpSnd, 0);

	//Wait for pre-ramp time
	delay(arBladeDelay);

	//Set the blade color
	SetBladeColor(apBlade, arBladeColor);

	//Use blade ramp time based on power-up sound time
	while(!apBlade->PowerUp(arBladeRampTime))
	{
		//Do nothing, just wait for power up complete
	}
}

void SaberActions::SetBladeColor(IBladeManager* apBlade,
		                         const tBladeColor& arColor,
								 bool aTurnOn)
{
	apBlade->SetChannel(arColor.mChannel1, 0);
	apBlade->SetChannel(arColor.mChannel2, 1);
	apBlade->SetChannel(arColor.mChannel3, 2);

	if(aTurnOn)
	{
		apBlade->On();
	}
}

void SaberActions::SetBladeToMainColor(IBladeManager* apBlade,
		                 	 	 	   SettingsManager* apSettings)
{
	SetBladeColor(apBlade, apSettings->GetNormalBladeColor(), true);
}

void SaberActions::SetBladeToFlashColor(IBladeManager* apBlade,
	 	                                SettingsManager* apSettings)
{
	//Set the color and turn on the blade
	SetBladeColor(apBlade, apSettings->GetFlashBladeColor(), true);
}

void SaberActions::PerformPowerDown(ASoundPlayer* apSoundPlayer,
                                    IBladeManager* apBlade,
					                uint16_t& arBladeRampTime)
{
	//Play power down sound
	apSoundPlayer->PlaySound(ESoundTypes::eePowerDownSnd, 0);

	//Turn off the blade
    //Use sound timings to decide how long power-down should take
	while(!apBlade->PowerDown(arBladeRampTime))
	{
		//Do nothing, just wait for power down
	}
}

void SaberActions::SwitchProfile(int aProfileIdx,
		           SettingsManager* apSettings,
				   ASoundPlayer* apSoundPlayer,
				   AMotionManager* apMotion)
{
	apSettings->SetSelectedProfile(aProfileIdx);
	apSoundPlayer->SetFont(aProfileIdx);
	apSoundPlayer->PlaySound(eeFontIdSnd, 0);

	//Update motion settings
	Utility::SetMotionTolerances(&gToleranceData, apSettings);
	apMotion->Init(); //Reinitialize with new tolerance values

}

void SaberActions::ApplyLockupEffect(IBladeManager* apBlade,
        const tBladeColor& aMainColor, //Main Color
        const tBladeColor& aLockupColor, //Lockup/clash color
        const int& aColorIntensity,
		const int& aFlickerIntensity,
		const unsigned int& aFrameUpdatePeriod)
{
	static unsigned long sLastUpdateTime = 0;

	if(millis() - sLastUpdateTime <  aFrameUpdatePeriod)
	{
		return; //Not time to update to the next frame yet
	}
	else
	{
		sLastUpdateTime = millis();
	}

	//Decide what color the blade should be
	tBladeColor lColor = aLockupColor;
	if(random(0, 100) > aColorIntensity)
	{
		lColor = aMainColor;
	}

	//Now apply flicker (amplitude modulation)
	int lFlickerFloor = 100 - aFlickerIntensity;
	float lFlickerMult = ( (float)random(lFlickerFloor, 100) ) / 100.0;

	lColor.mChannel1 = (uint8_t) ( (float)lColor.mChannel1 * lFlickerMult );
	lColor.mChannel2 = (uint8_t) ( (float)lColor.mChannel2 * lFlickerMult );
	lColor.mChannel3 = (uint8_t) ( (float)lColor.mChannel3 * lFlickerMult );

	SetBladeColor(apBlade, lColor, true);

}

void SaberActions::SetSoundVolume(const int aVolume,
		                          ASoundPlayer* apSoundPlayer)
{
	//Fetch volume from settings
	int lnVol = aVolume;

	//Enforce bounds checks.
	//If volume settings is less than 5, it's invalid and we will use a default
	if(lnVol < 5)
	{
		lnVol = 20;
	}
	else if(lnVol > apSoundPlayer->Features().MaxVolume)
	{
		lnVol = apSoundPlayer->Features().MaxVolume;
	}

	//Give volume adjustment to the sound player
	apSoundPlayer->SetVolume(lnVol);
}
