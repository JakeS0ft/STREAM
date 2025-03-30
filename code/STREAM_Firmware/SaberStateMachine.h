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
 This file is part of the STREAM saber control program (STREAM).

 Copyright (C) 2017-2018 Jacob "JakeSoft" Martin.

 The STREAM software is distributed in the hope that it will be
 useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*/
/*
 * SaberStateMachine.h
 *
 *  Created on: Mar 22, 2017
 *      Author: JakeSoft
 */

#ifndef SABERSTATEMACHINE_H_
#define SABERSTATEMACHINE_H_

#include <USaber.h>
#include <AccentLED.h>
#include "StateMachine.h"
#include "Button.h"
#include "StreamControl.h"
#include "MenuStateMachine.h"
#include "PowerManager.h"

/**
 * Enumeration of all possible saber states.
 */
enum ESaberState
{
	eeBoot,
	eeOff,
	eePoweringUp,
	eeOnIdle,
	eeSwing,
	eePostSwing,
	eeClash,
	eePostClash,
	eeLockup,
	eeBlaster,
	eePoweringDown,
	eeSwitchProfile,
	eeMenu,
	eeSleep,
	eeFlashDriveMode,
	eeStreamMode
};

/**
 * This class serves as the primary state machine for the saber controlling
 * all higher-level functionality.
 */
class SaberStateMachine : public StateMachine
{
public:

	/**
	 * Constructor.
	 *   Args:
	 *     apAccentLED - Accent LED handler
	 *     apSettingsManager - Settings manager
	 *     apPowerMan - Power manager (for sleep mode)
	 */
	SaberStateMachine(IAccentLED* apAccentLED,
					  SettingsManager* apSettingsManager,
					  PowerManager* apPowerMan);
	/**
	 * Initialize components and get ready to run.
	 */
	void Init();

	/**
	 * Operates the saber state machine.
	 * Call this every cycle from the main loop.
	 */
	void Body();

	/**
	 * Sets S.T.R.E.A.M controller.
	 * Args:
	 *   aSc - Controller to use
	 */
	void SetStreamControl(StreamControl* aSc);

	/**
	 * Check if saber state is S.T.R.E.A.M mode
	 */
	bool IsInStreamMode()
	{
		return (ESaberState::eeStreamMode == mState);
	}

private:

	StreamControl* mpStreamControl; //S.T.R.E.A.M.
	IAccentLED* mpAccent;

	unsigned long mLastClashTime; //Time when the last clash event occurred
	unsigned long mLastSwingTime; //Time when the last swing event occurred
	unsigned long mLastBlasterTime; //Time when last blaster event occurred

	bool mBlasterMode;
	bool mOneButtonLockupMode;

	//State machine for the menu system
	MenuStateMachine* mpMenu;

	bool mHumRelaunched;
};

#endif /* SABERSTATEMACHINE_H_ */
