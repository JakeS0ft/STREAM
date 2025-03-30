/******************************************************************************
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 ******************************************************************************/
/*
 * SleepManager.cpp
 *
 *  Created on: Apr 13, 2018
 *      Author: JakeSoft
 *
 *  This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.
 *  To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/.
 */

#include "Arduino.h"
#include "SleepManager.h"
#include <avr/sleep.h>

/*** UNCOMMENT THESE IF NOT DEFINED IN SKETCH
ISR (PCINT0_vect) // handle pin change interrupt for D8 to D13 here
{
	//Do nothing
}

ISR (PCINT1_vect) // handle pin change interrupt for A0 to A5 here
{
	//Do nothing
}

ISR (PCINT2_vect) // handle pin change interrupt for D0 to D7 here
{
	//Do nothing
}
***/

SleepManager::SleepManager() :
mpWakeCallback(nullptr),
mpSleepCallback(nullptr)
{

}

SleepManager::~SleepManager()
{
	//Do nothing
}

void SleepManager::AddWakeInputPin(unsigned char aPinNumber)
{
	//Install Pin change interrupt for a pin
	*digitalPinToPCMSK(aPinNumber) |= bit (digitalPinToPCMSKbit(aPinNumber));  // enable pin
	PCIFR  |= bit (digitalPinToPCICRbit(aPinNumber)); // clear any outstanding interrupt
	PCICR  |= bit (digitalPinToPCICRbit(aPinNumber)); // enable interrupt for the group
}

void SleepManager::SetWakeupCallback(WAKECALLBACK apCallbackFunction)
{
	mpWakeCallback = apCallbackFunction;
}

void SleepManager::SetSleepCallback(SLEEPCALLBACK apCallbackFunction)
{
	mpSleepCallback = apCallbackFunction;
}

void SleepManager::Sleep()
{
	//Call the sleep callback function to perform any pre-sleep operations
	if(nullptr != mpSleepCallback)
	{
		mpSleepCallback();
	}

	/*
	 * Change this if you'd like to use other sleep modes.
	 * Possible sleep modes are:
	 *     SLEEP_MODE_IDLE
	 *     SLEEP_MODE_ADC
	 *     SLEEP_MODE_PWR_SAVE
	 *     SLEEP_MODE_STANDBY
	 *     SLEEP_MODE_PWR_DOWN
	 */
	set_sleep_mode(SLEEP_MODE_STANDBY);   // set the sleep mode

	sleep_enable();          // enable the sleep bit in the MCUCR register

	sleep_mode();            // Put the CPU to sleep
	// Execution starts here when CPU wakes up

	sleep_disable();         //Disable sleep mode

	//Call the wakeup callback function
	if(nullptr != this->mpWakeCallback)
	{
		mpWakeCallback();
	}
}
