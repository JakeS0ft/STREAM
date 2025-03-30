/*
 * AccentLEDStub.h
 *
 *  Created on: May 4, 2018
 *      Author: Jake
 */

#ifndef ACCENTLEDSTUB_H_
#define ACCENTLEDSTUB_H_

#include "IAccentLED.h"

//Dummy class for when accents not used
class AccentLEDStub : public IAccentLED
{
public:
	AccentLEDStub() {}
	virtual ~AccentLEDStub() {}
	virtual void Init() {}
	virtual void SetStyle(teAccentStyle) {}
	virtual void PerformIO() {}
};


#endif /* ACCENTLEDSTUB_H_ */
