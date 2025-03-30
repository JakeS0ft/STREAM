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
 * StateMachine.h
 *
 *  Created on: Mar 23, 2017
 *      Author: JakeSoft
 */

#ifndef STATEMACHINE_H_
#define STATEMACHINE_H_

#include <Arduino.h> //for millis()

/**
 * Class defines a generic state machine base class. Derived classes should
 * implement the Init() and Body() methods.
 */
class StateMachine
{
public:

	/**
	 * Constructor.
	 */
	StateMachine() :
	mState(0),
	mLastState(0),
	mIsNewState(false),
	mStateChangeTime(0)

	{

	}

	/**
	 * Destructor.
	 */
	virtual ~StateMachine()
	{
		//Do nothing
	}

	/**
	 * Subclasses should implement any initialization routines that must occur
	 * prior to operation of the state machine.
	 */
	virtual void Init() = 0;

	/**
	 * Subclasses should implement the main body here that is suitable to be
	 * called in a loop to operate the state machine. Operate() will call this
	 * method after it performs state machine bookkeeping operations.
	 */
	virtual void Body() = 0;

	/**
	 * Call this method from a loop to operate the state machine. This method
	 * will perform state management bookkeeping as well as call the Body()
	 * method. The Body() method is to be implemented by subclasses to create
	 * custom behavior.
	 */
	inline void Operate()
	{
		//Update the flag that indicates a state change occurred last cycle
		if(mLastState != mState)
		{
			mIsNewState = true;
		}
		else
		{
			mIsNewState = false;
		}

		//Store the state for next cycle
		mLastState = mState;

		//Call the user-defined operations
		Body();
	}

	/**
	 * Change to a new state.
	 *   Args:
	 *     aState - New state to change to.
	 */
	inline void ChangeState(const int& aState)
	{
		mState = aState;
		mStateChangeTime = millis();
	}

	/**
	 * Get how long the current state has been active.
	 * Return: Milliseconds since last state change.
	 */
	inline unsigned long TimeInState()
	{
		return (millis() - mStateChangeTime);
	}

protected:
	int mState; //Current state ( set this only with ChangeState() )
	int mLastState; //Last state
	bool mIsNewState; //Flag set to true during first cycle after state change

	unsigned long mStateChangeTime; //Time when state changed
};


#endif /* STATEMACHINE_H_ */
