/*
 * DigitalAccentLED.cpp
 *
 *  Created on: May 9, 2018
 *      Author: Jake
 */



#include <Arduino.h>
#include "DigitalAccentLED.h"

#define ACCENT1_LSOFF_OFF_TIME 4000 //How long ACCENT 1 stays off when lightsaber is off
#define ACCENT1_LSOFF_ON_TIME 100 //How long ACCENT 1 stays on when lightsaber is off

#define ACCENT1_LSON_LOW_TIME 200 //How long ACCENT 1 stays dim when lightsaber is on
#define ACCENT1_LSON_HIGH_TIME 200 //How long ACCENT 1 stays bight when lightsaber is on

#define ACCENT1_EVENPULSE_OFF_TIME 500 //How long ACCENT 1 stays off when lightsaber state is color select
#define ACCENT1_EVENPULSE_ON_TIME 500 //How long ACCENT 1 stays on when lightsaber state is color select

DigitalAccentLED::DigitalAccentLED(int aLedPin)
{
	mnLedPin = aLedPin;
	meStyle = eeAccentOff;
	mnTimeBookmark = 0;
	mnLedPowerLevel = 0;
}

DigitalAccentLED::~DigitalAccentLED()
{
	//Do nothing
}

void DigitalAccentLED::DigitalAccentLED::Init()
{
	pinMode(mnLedPin, OUTPUT);
	digitalWrite(mnLedPin, LOW);
}

void DigitalAccentLED::SetStyle(teAccentStyle aeStyle)
{
	meStyle = aeStyle;
}

void DigitalAccentLED::PerformIO()
{

	switch(meStyle)
	{
	case eeAccentOff:
		mnLedPowerLevel = LOW;
		break;
	case eeAccentOn:
		mnLedPowerLevel = HIGH;
		break;
	case eeAccentTwoStagePulse:
		mnLedPowerLevel = HIGH;
		break;
	case eeAccentIdleBlink:
	    //Accent light is on, turn it off after timeout
		if( (millis() - mnTimeBookmark > ACCENT1_LSOFF_ON_TIME)
				&& LOW != mnLedPowerLevel)
		{
			mnLedPowerLevel = LOW;
			mnTimeBookmark = millis();
		}
		//Accent light is off, turn it on after timeout
		else if( (millis() - mnTimeBookmark > ACCENT1_LSOFF_OFF_TIME)
				&& LOW == mnLedPowerLevel)
		{
			mnLedPowerLevel = HIGH;
			mnTimeBookmark = millis();
		}
		break;
	case eeAccentEvenPulse:
	    //Accent light is on, turn it off after timeout
		if( (millis() - mnTimeBookmark > ACCENT1_EVENPULSE_ON_TIME)
				&& LOW != mnLedPowerLevel)
		{
			mnLedPowerLevel = LOW;
			mnTimeBookmark = millis();
		}
		//Accent light is off, turn it on after timeout
		else if( (millis() - mnTimeBookmark > ACCENT1_EVENPULSE_ON_TIME)
				&& LOW == mnLedPowerLevel)
		{
			mnLedPowerLevel = HIGH;
			mnTimeBookmark = millis();
		}
		break;
	default:
		meStyle = eeAccentOff;
		break;
	}

	digitalWrite(mnLedPin, mnLedPowerLevel);
}
