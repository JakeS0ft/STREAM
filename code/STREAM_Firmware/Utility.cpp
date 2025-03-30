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
 * Utility.cpp
 *
 *  Created on: Aug 9, 2017
 *      Author: Jake
 */

#include "Utility.h"

int Utility::CalculateSwingSensitivityLarge(int aPreset)
{
	return 2550;
}

int Utility::CalculateSwingSensitivityMedium(int aPreset)
{
	return  512 - (50 * aPreset);
}

int Utility::CalculateSwingSensitivitySmall(int aPreset)
{
	return (CalculateSwingSensitivityMedium(aPreset)*2)/3;
}

void Utility::UpdateMotionSwingTolerance(MPU6050LiteTolData* apMotionTol, int aPreset)
{
	apMotionTol->mSwingLarge  = CalculateSwingSensitivityLarge(aPreset);
	apMotionTol->mSwingMedium = CalculateSwingSensitivityMedium(aPreset);
	apMotionTol->mSwingSmall = CalculateSwingSensitivitySmall(aPreset);
	apMotionTol->mTwist = apMotionTol->mSwingMedium;
}

void Utility::SetMotionTolerances(MPU6050LiteTolData* apTolData, SettingsManager* apSettings)
{
	//Calculate new swing tolerances
	int lSwingPresetIndex = apSettings->GetSelectedPofile()->mSwingPresetIndex;

	if(0 == lSwingPresetIndex) //Use user-defined values
	{
		apTolData->mSwingLarge = apSettings->GetMotionSettings()->mSwingLargeTol;
		apTolData->mSwingMedium = apSettings->GetMotionSettings()->mSwingMediumTol;
		apTolData->mSwingSmall = apSettings->GetMotionSettings()->mSwingSmallTol;
		apTolData->mTwist = apSettings->GetMotionSettings()->mTwistTol;
	}
	else //Use a factory preset
	{
		Utility::UpdateMotionSwingTolerance
			(apTolData, apSettings->GetSelectedPofile()->mSwingPresetIndex);
	}

	//Calculate new clash tolerance value
	unsigned int lClashPresetIndex = apSettings->GetSelectedPofile()->mClashPresetIndex;
	if(0 == lClashPresetIndex) //Use user-defined value
	{
		apTolData->mClash = apSettings->GetMotionSettings()->mClashTol;
	}
	else //Use a factory preset
	{
		//apTolData->mClash = 5 + 64 - (7 * lClashPresetIndex);
		//apTolData->mClash = 75 - (lClashPresetIndex * 6);

		int lSubtractValue = map(lClashPresetIndex, 1, 9, 0, 70);
		apTolData->mClash = 80 - (unsigned int)lSubtractValue; //Possible values are between 80 and 10
	}

	//TODO: Update this to work like swing tols after menu is implemented
	apTolData->mTwist = apSettings->GetMotionSettings()->mTwistTol;
}


