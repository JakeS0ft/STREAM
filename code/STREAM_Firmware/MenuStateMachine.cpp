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
 * MenuStateMachine.cpp
 *
 *  Created on: Jul 27, 2017
 *      Author: JakeSoft
 */

#include "MenuStateMachine.h"
#include "SaberActions.h"

#define MENU_SWITCH_DEBOUCE 100

MenuStateMachine::MenuStateMachine(SettingsManager* apSettings,
			         IBladeManager* apBlade,
					 ASoundPlayer* apSoundPlayer,
					 AMotionManager* apMotionManger,
					 Button* apActButton,
					 Button* apAuxButton) :
   mpSettings(apSettings),
   mpBlade(apBlade),
   mpSoundPlayer(apSoundPlayer),
   mpMotionManger(apMotionManger),
   mpActButton(apActButton),
   mpAuxButton(apAuxButton),
   mIsDone(false)
{
	//Do nothing
}

MenuStateMachine::~MenuStateMachine()
{
	//Do nothing
}

void MenuStateMachine::Init()
{
	mpActButton->WaitForRelease();
}

void MenuStateMachine::Body()
{
	mpActButton->Update();
	mpAuxButton->Update();
	mpMotionManger->Update();

	//Fetch Settings
	tSaberSettings* lpSettings = mpSettings->GetSettings();

	switch(mState)
	{
	case eeInit:
		mpSoundPlayer->PlaySound(eeMenuSoundSnd, eeDIYinoSnd);
		delay(2000);

		//Wait for user to let off the button
		mpActButton->WaitForRelease();
		mpAuxButton->WaitForRelease();

		ChangeState(EMenuStates::eeSetVolume);

		break;
	case eeSetVolume:
	{
		if(mIsNewState)
		{
			mpSoundPlayer->PlaySound(eeMenuSoundSnd, eeSetSoundVolumeSnd);
			delay(1500);
			mpSoundPlayer->PlaySound(eeHumSnd, 0);
		}

		unsigned char lnMaxVolume = mpSoundPlayer->Features().MaxVolume;
		//Increase volume with loop-around
		if(mpActButton->IsPulseEdge(MENU_SWITCH_DEBOUCE))
		{
			if(lpSettings->mSoundVolume < lnMaxVolume)
			{
				lpSettings->mSoundVolume++;
			}
			else //Loop around
			{
				lpSettings->mSoundVolume = 5;
			}
//			Serial.print("Max volume is:");
//			Serial.print( (int)mpSoundPlayer->Features().MaxVolume);
//			Serial.print(" Setting volume to:");
//			Serial.println((int)lpSettings->mSoundVolume);
			mpSoundPlayer->SetVolume(lpSettings->mSoundVolume);
			delay(150);

			if(lnMaxVolume == lpSettings->mSoundVolume)
			{
				mpSoundPlayer->PlaySound(eeMenuSoundSnd, eeMaxVolumeSnd);
				delay(1500);
				mpSoundPlayer->PlaySound(eeHumSnd, 0);
				delay(150);
			}
		}
		//Decrease volume with loop-around
		else if(mpAuxButton->IsPulseEdge(MENU_SWITCH_DEBOUCE))
		{
			if(lpSettings->mSoundVolume > 5)
			{
				lpSettings->mSoundVolume--;
			}
			else //Loop around
			{
				lpSettings->mSoundVolume = lnMaxVolume;
			}

			mpSoundPlayer->SetVolume(lpSettings->mSoundVolume);
			delay(150);
			if(lnMaxVolume == lpSettings->mSoundVolume)
			{
				mpSoundPlayer->PlaySound(eeMenuSoundSnd, eeMaxVolumeSnd);
				delay(1500);
				mpSoundPlayer->PlaySound(eeHumSnd, 0);
				delay(150);
			}
		}
		else
		{
			CheckForNextOption(eeSetBladeColor, eeMenuSoundSnd, eeSetMainBladeColorSnd);
		}

	}
		break;
	case eeSetBladeColor:
	{
		//Fetch the blade parameters
		tBladeParameters* lpBladeParams = mpSettings->GetBladeSettings();
		if(mIsNewState)
		{
//			mpBlade->SetChannel(mpSettings->GetNormalBladeColor().mChannel1, 0);
//			mpBlade->SetChannel(mpSettings->GetNormalBladeColor().mChannel2, 1);
//			mpBlade->SetChannel(mpSettings->GetNormalBladeColor().mChannel3, 2);
//			mpBlade->On();
			SaberActions::SetBladeToMainColor(mpBlade, mpSettings);

		}
		else if(mpActButton->IsPulseEdge(MENU_SWITCH_DEBOUCE)) //Advance color index
		{
			if(mpSettings->GetBladeSettings()->mMainColorIndex <  MAX_COLOR_PRESETS-1)
			{
				lpBladeParams->mMainColorIndex++;
			}
			else
			{
				lpBladeParams->mMainColorIndex = 0;
			}

//			mpBlade->SetChannel(mpSettings->GetNormalBladeColor().mChannel1, 0);
//			mpBlade->SetChannel(mpSettings->GetNormalBladeColor().mChannel2, 1);
//			mpBlade->SetChannel(mpSettings->GetNormalBladeColor().mChannel3, 2);
//			mpBlade->On();
			SaberActions::SetBladeToMainColor(mpBlade, mpSettings);

			delay(100);
		}
		else if(mpAuxButton->IsPulseEdge(MENU_SWITCH_DEBOUCE)) //Decrease color index
		{
			if(mpSettings->GetBladeSettings()->mMainColorIndex >  0)
			{
				lpBladeParams->mMainColorIndex--;
			}
			else
			{
				lpBladeParams->mMainColorIndex = MAX_COLOR_PRESETS-1;
			}

//			mpBlade->SetChannel(mpSettings->GetNormalBladeColor().mChannel1, 0);
//			mpBlade->SetChannel(mpSettings->GetNormalBladeColor().mChannel2, 1);
//			mpBlade->SetChannel(mpSettings->GetNormalBladeColor().mChannel3, 2);
//			mpBlade->On();
			SaberActions::SetBladeToMainColor(mpBlade, mpSettings);
		}
		else if(mpMotionManger->IsClash())
		{
			mpSoundPlayer->PlayRandomSound(eeClashSnd);
			if((int)lpBladeParams->mFlickerIndex < mpBlade->GetFeatures().Flickers)
			{
				lpBladeParams->mFlickerIndex++;
			}
			else
			{
				lpBladeParams->mFlickerIndex = 0;
			}
			delay(500);
			mpSoundPlayer->Stop();
		}
		else if(CheckForNextOption(eeSetFlashColor, eeMenuSoundSnd, eeSetMainBladeFlashColorSnd))
		{
			mpBlade->Off();
		}
		else
		{
			mpBlade->ApplyFlicker((int)lpBladeParams->mFlickerIndex);
		}
	}
		break;
	case eeSetFlashColor:
	{
		//Fetch the blade parameters
		tBladeParameters* lpBladeParams = mpSettings->GetBladeSettings();

		if(mIsNewState)
		{
//			mpBlade->SetChannel(mpSettings->GetFlashBladeColor().mChannel1, 0);
//			mpBlade->SetChannel(mpSettings->GetFlashBladeColor().mChannel2, 1);
//			mpBlade->SetChannel(mpSettings->GetFlashBladeColor().mChannel3, 2);
//			mpBlade->On();
			SaberActions::SetBladeToFlashColor(mpBlade, mpSettings);
		}
		else if(mpActButton->IsPulseEdge(MENU_SWITCH_DEBOUCE)) //Advance color index
		{
			if(mpSettings->GetBladeSettings()->mFlashColorIndex <  MAX_COLOR_PRESETS-1)
			{
				lpBladeParams->mFlashColorIndex++;
			}
			else
			{
				lpBladeParams->mFlashColorIndex = 0;
			}

//			mpBlade->SetChannel(mpSettings->GetFlashBladeColor().mChannel1, 0);
//			mpBlade->SetChannel(mpSettings->GetFlashBladeColor().mChannel2, 1);
//			mpBlade->SetChannel(mpSettings->GetFlashBladeColor().mChannel3, 2);
//			mpBlade->On();
			SaberActions::SetBladeToFlashColor(mpBlade, mpSettings);

			delay(100);
		}
		else if(mpAuxButton->IsPulseEdge(MENU_SWITCH_DEBOUCE)) //Decrease color index
		{
			if(mpSettings->GetBladeSettings()->mFlashColorIndex >  0)
			{
				lpBladeParams->mFlashColorIndex--;
			}
			else
			{
				lpBladeParams->mFlashColorIndex = MAX_COLOR_PRESETS-1;
			}

			SaberActions::SetBladeToFlashColor(mpBlade, mpSettings);
		}
		else if(CheckForNextOption(eeSetSwingSensitivty, eeMenuSoundSnd, eeSetSwingSensitivitySnd))
		{
			mpBlade->Off();
		}
	}
		break;
	case eeSetSwingSensitivty:
	{
		tProfile* lpProfile = mpSettings->GetSelectedPofile();
		if(mpActButton->IsPulseEdge(MENU_SWITCH_DEBOUCE))
		{
			if(lpProfile->mSwingPresetIndex <  9)
			{
				lpProfile->mSwingPresetIndex++;
			}
			else
			{
				lpProfile->mSwingPresetIndex = 0;
			}
		}
		else if(mpAuxButton->IsPulseEdge(MENU_SWITCH_DEBOUCE))
		{
			if(lpProfile->mSwingPresetIndex > 0)
			{
				lpProfile->mSwingPresetIndex--;
			}
			else
			{
				lpProfile->mSwingPresetIndex = 9;
			}
		}

		//Play sound to announce the new setting
		if(mpActButton->IsPulseEdge(MENU_SWITCH_DEBOUCE) || mpAuxButton->IsPulseEdge(MENU_SWITCH_DEBOUCE))
		{
			if(0 == lpProfile->mSwingPresetIndex)
			{
				mpSoundPlayer->PlaySound(eeMenuSoundSnd, eeUserCustomSnd);
			}
			else
			{
				mpSoundPlayer->PlaySound(eeMenuSoundSnd, eeNumber0Snd+lpProfile->mSwingPresetIndex);
			}
			delay(100);
		}
		else
		{
			//CheckForNextOption(eeSaveAndExit, eePowerDownSnd, 0);
			CheckForNextOption(eeSetClashSensitivity, eeMenuSoundSnd, eeSetClashSensitivitySnd);
		}

	}
		break;
	case eeSetClashSensitivity:
	{
		tProfile* lpProfile = mpSettings->GetSelectedPofile();

		if(mpActButton->IsPulseEdge(MENU_SWITCH_DEBOUCE))
		{

			if(lpProfile->mClashPresetIndex <  9)
			{
				lpProfile->mClashPresetIndex++;
			}
			else
			{
				lpProfile->mClashPresetIndex = 0;
			}


		}
		else if(mpAuxButton->IsPulseEdge())
		{
			if(lpProfile->mClashPresetIndex >  0)
			{
				lpProfile->mClashPresetIndex--;
			}
			else
			{
				lpProfile->mClashPresetIndex = 9;
			}
		}

		//Play sound to announce the new setting
		if(mpActButton->IsPulseEdge(MENU_SWITCH_DEBOUCE) || mpAuxButton->IsPulseEdge(MENU_SWITCH_DEBOUCE))
		{
			if(0 == lpProfile->mClashPresetIndex)
			{
				mpSoundPlayer->PlaySound(eeMenuSoundSnd, eeUserCustomSnd);
			}
			else
			{
				mpSoundPlayer->PlaySound(eeMenuSoundSnd, eeNumber0Snd+lpProfile->mClashPresetIndex);
			}
			delay(100);
		}
		else
		{
			CheckForNextOption(eeSaveAndExit, eePowerDownSnd, 0);
		}
	}
		break;
	case eeSaveAndExit:
		mpSettings->Save();
		mpSoundPlayer->PlaySound(ESoundTypes::eeBootSnd, 0);
		ChangeState(eeDone);
		mpActButton->WaitForRelease();
		break;
	case eeDone:
		mIsDone = true;
		break;
	}
}

bool MenuStateMachine::CheckForNextOption(EMenuStates aNextState, ESoundTypes aSoundType, unsigned char aIndex)
{
	bool lNextOption = false;

	if(mpActButton->IsHeld() && !mpActButton->IsPulseEdge(MENU_SWITCH_DEBOUCE))
	{
		if(mpActButton->GetHeldTime() > 1000)
		{
			lNextOption = true;

			mpSoundPlayer->PlaySound(aSoundType, aIndex);
			delay(100);

			//Wait for user to let off the button
			mpActButton->WaitForRelease();

			ChangeState(aNextState);
		}

	}

	return lNextOption;
}

bool MenuStateMachine::IsDone()
{
	return mIsDone;
}
