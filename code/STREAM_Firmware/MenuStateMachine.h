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
 * MenuStateMachine.h
 *
 *  Created on: Jul 27, 2017
 *      Author: JakeSoft
 */

#ifndef MENUSTATEMACHINE_H_
#define MENUSTATEMACHINE_H_

#include <USaber.h>
#include "StateMachine.h"
#include "Settings.h"
#include "SettingsManager.h"
#include "Button.h"

enum EMenuSoundOffsets
{
	eeNumber0Snd,
	eeNumber1Snd,
	eeNumber2Snd,
	eeNumber3Snd,
	eeNumber4Snd,
	eeNumber5Snd,
	eeNumber6Snd,
	eeNumber7Snd,
	eeNumber8Snd,
	eeNumber9Snd,
	eeBootSndPlaceholder,
	eeDIYinoSnd,
	eeSetMainBladeFlashColorSnd,
	eeSetMainBladeColorSnd,
	eeSetSoundVolumeSnd,
	eeSetSwingSensitivitySnd,
	eeConnectedSnd,
	eeDisconnectedSnd,
	eeSetClashSensitivitySnd,
	eeUserCustomSnd,
	eeStreamModeSnd,
	eeMaxVolumeSnd
};

enum EMenuStates
{
	eeInit,
	eeSetVolume,
	eeSetBladeColor,
	eeSetFlashColor,
	eeSetSwingSensitivty,
	eeSetClashSensitivity,
	eeSaveAndExit,
	eeDone
};

class MenuStateMachine : public StateMachine
{
public:
	/**
	 * Constructor.
	 *   Args:
	 *    apSettings - Pointer to settings manager
	 *    apBlade - Pointer to blade control
	 *    apSoundPlayer - Pointer to sound player
	 *    apMotionManger - Pointer to motion manager
	 *    apActButton - Pointer to activation button
	 */
	MenuStateMachine(SettingsManager* apSettings,
			         IBladeManager* apBlade,
					 ASoundPlayer* apSoundPlayer,
					 AMotionManager* apMotionManger,
					 Button* apActButton,
					 Button* apAuxButton);

	~MenuStateMachine();

	virtual void Init();

	virtual void Body();

	virtual bool IsDone();
protected:

	/**
	 * Checks activation button press to see if it's time to go the the next
	 * menu. Plays a sound if so and advances the specified menu state.
	 * Args:
	 *  aNextState - State to advance to if correct button state is detected
	 *  aSoundType - Type of sound to play
	 *  aIndex - Index of sound to play
	 *
	 *  return: TRUE if it's time to advance, FALSE otherwise
	 */
	bool CheckForNextOption(EMenuStates aNextState, ESoundTypes aSoundType, unsigned char aIndex);

	SettingsManager* mpSettings;
	IBladeManager* mpBlade;
	ASoundPlayer* mpSoundPlayer;
	AMotionManager* mpMotionManger;
	Button* mpActButton;
	Button* mpAuxButton;
	bool mIsDone;
};

#endif /* MENUSTATEMACHINE_H_ */
