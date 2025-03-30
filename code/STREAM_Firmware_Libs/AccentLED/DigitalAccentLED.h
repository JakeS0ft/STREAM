/*
 * DigitalAccentLED.h
 *
 *  Created on: May 9, 2018
 *      Author: Jake
 */

#ifndef DIGITALACCENTLED_H_
#define DIGITALACCENTLED_H_

#include "IAccentLED.h"


class DigitalAccentLED : public IAccentLED
{
public:
	/**
	 * Constructor.
	 * Args:
	 *   anLEDPin - Pin attached to the LED
	 */
	DigitalAccentLED(int aLedPin);

	/**
	 * Destructor.
	 */
	virtual ~DigitalAccentLED();

	/**
	 * Initialize the object and I/O.
	 */
	virtual void Init();

	/**
	 * Sets accent LED behavior.
	 */
	virtual void SetStyle(teAccentStyle aeStyle);

	/**
	 * Runs the accent led so performs whatever behaivor is set.
	 */
	virtual void PerformIO();


protected:
	//Pin that controls the LED
	int mnLedPin;

	//Behavior style
	teAccentStyle meStyle;

	//Keep track of timing
	unsigned long mnTimeBookmark;

	//Keep track of LED power state
	unsigned char mnLedPowerLevel;
};

#endif /* DIGITALACCENTLED_H_ */
