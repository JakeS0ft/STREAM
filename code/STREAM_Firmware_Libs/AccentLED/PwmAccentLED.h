/*
 * AccentLED.h
 *
 *  Created on: Dec 10, 2015
 *      Author: JakeSoft
 */

#ifndef __AccentLED_H_
#define __AccentLED_H_

#include "IAccentLED.h"

/**
 * Controls an Accent LED.
 */
class PwmAccentLED : public IAccentLED {
public:
	/**
	 * Constructor.
	 * Args:
	 *   anLEDPin - Pin attached to the LED
	 */
	PwmAccentLED(int anLedPin);

	/**
	 * Destructor.
	 */
	virtual ~PwmAccentLED();

	/**
	 * Initialize the object and I/O.
	 */
	void Init();

	/**
	 * Sets accent LED behavior.
	 */
	void SetStyle(teAccentStyle);

	/**
	 * Runs the accent led so performs whatever behaivor is set.
	 */
	void PerformIO();

private:
	//Pin that controls the LED
	int mnLedPin;

	//Behavior style
	teAccentStyle meStyle;

	//Keep track of timing
	unsigned long mnTimeBookmark;

	//Power level of the accent led for analog writes
	int mnLedPowerLevel;
};

#endif /* __AccentLED_H_*/
