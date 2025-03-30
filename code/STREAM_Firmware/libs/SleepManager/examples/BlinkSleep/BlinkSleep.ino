/******************************************************************************
 * This is free software; you can redistribute it and/or
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
/**
 * BlinkSleep.ino
 *
 * This example sketch demonstrates how to use the SleepManager library. It will
 * blink an LED for 10 seconds. After 10 seconds, the CPU will be put to sleep
 * and cause the LED to stop blinking. The CPU wakes up when a button is pressed
 * (SWITCH_PIN is pulled low) thereby causing the LED to resume blinking for
 * another 10 seconds until the button is pressed again.
 *
 * Created on: Apr 13, 2018
 * Author: JakeSoft
 *
 */

#include "Arduino.h"
#include <avr/sleep.h>
#include "SleepManager.h"

//Pin used for button or switch
#define SWITCH_PIN 12
//Pin used for LED
#define LED_PIN 13

//Keep track of last time the button was pressed
unsigned long gLastPressTime;
//Keep track of LED state
bool gLedIsOn;

//The sleep manager
SleepManager gSleepMan;

/**
 * This function called after exiting sleep mode.
 *
 * Use this to perform any desired options like turning
 * on any peripherals, sending commands to other attached
 * devices to exit sleep mode, powering on outputs that
 * may need to be turned back on, etc.
 *
 * NOTE: This is the inverse of the SleepCallback() function
 * below.
 */
void WakeupCallback()
{
	Serial.println("Wake up callback.");
}

/**
 * This function called right before entering sleep mode.
 *
 * Use this to perform any desired power-saving options
 * like turning off peripherals, sending commands to other
 * attached devices to enter sleep mode, powering off
 * outputs that don't need to be on, etc.
 *
 * NOTE: This is the inverse of the WakeupCallback() function
 * above.
 */
void SleepCallback()
{
	Serial.println("Sleep callback.");

	//Delay is to allow time for serial data to be
	//transmitted before putting the CPU to sleep
	delay(250);
}

void setup() {
	//Set up the switch pin input.
	pinMode(SWITCH_PIN, INPUT_PULLUP);

	//Set up LED pin outputs
	pinMode(LED_PIN, OUTPUT);  // LED

	//-------------------------
	// Set up the sleep manager
	//-------------------------
	// The wake pin will be monitored during sleep mode. A pin-change interrupt
	// is used to wake up the CPU when the pin state changes.
	// NOTE: Pin change interrupts group pins such that a change on any pin
	// in the group will trigger the interrupt and cause the CPU to wake up.
	// The pin groupings are:
	//   - D0 to D7
	//   - D8 to D13
	//   - A0 to A5
	//  So for example, adding wake pin D8 will also cause a change on D9
	//  to wake the CPU.
	gSleepMan.AddWakeInputPin(SWITCH_PIN);
	// Set the function to call right before putting the CPU to sleep.
	// NOTE: It is optional to set this callback. By default, the
	//       SleepManger will do nothing before putting the CPU to sleep.
	gSleepMan.SetSleepCallback(&SleepCallback);
	// Set the function to call when waking up the CPU.
	// NOTE: It is optional to set this callback. By default, the
	//      SleepManager will do nothing after waking up from sleep mode.
	gSleepMan.SetWakeupCallback(&WakeupCallback);

	gLastPressTime = 0;

	digitalWrite(LED_PIN, HIGH);

	Serial.begin(9600);

}


void loop() {

	delay(1000);

	//Check if more than 10 seconds as passed since the button was pressed
	if(millis() - gLastPressTime >= 10000)
	{
		Serial.println("Putting CPU to sleep.");
		gSleepMan.Sleep();
		Serial.println("CPU woke up.");
	}

	//Check if the button or switch is pressed
	if(LOW == digitalRead(SWITCH_PIN))
	{
		//Record the time the button was pressed
		gLastPressTime = millis();
	}

	//Toggle the LED, make it blink
	gLedIsOn = !gLedIsOn;
	if(gLedIsOn)
	{
		digitalWrite(LED_PIN, HIGH);
	}
	else
	{
		digitalWrite(LED_PIN, LOW);
	}

}

