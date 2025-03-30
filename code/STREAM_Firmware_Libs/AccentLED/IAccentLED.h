/*
 * IAccentLED.h
 *
 *  Created on: May 4, 2018
 *      Author: Jake
 */

#ifndef IACCENTLED_H_
#define IACCENTLED_H_

typedef enum
{
	eeAccentOff,
	eeAccentOn,
	eeAccentTwoStagePulse,
	eeAccentIdleBlink,
	eeAccentEvenPulse
}teAccentStyle;

/**
 * Controls an Accent LED.
 */
class IAccentLED {
public:

	virtual ~IAccentLED() {}

	/**
	 * Initialize the object and I/O.
	 */
	virtual void Init() = 0;

	/**
	 * Sets accent LED behavior.
	 */
	virtual void SetStyle(teAccentStyle) = 0;

	/**
	 * Runs the accent led so performs whatever behavior is set.
	 */
	virtual void PerformIO() = 0;

};

#endif /* IACCENTLED_H_ */
