/*
 * AccentLED.cpp
 *
 *  Created on: Dec 10, 2015
 *      Author: Jake
 */

#include "PwmAccentLED.h"

#include <Arduino.h>

#define ACCENT1_LSOFF_OFF_TIME 4000 //How long ACCENT 1 stays off when lightsaber is off
#define ACCENT1_LSOFF_ON_TIME 100 //How long ACCENT 1 stays on when lightsaber is off

#define ACCENT1_LSON_LOW_TIME 200 //How long ACCENT 1 stays dim when lightsaber is on
#define ACCENT1_LSON_HIGH_TIME 200 //How long ACCENT 1 stays bight when lightsaber is on

#define ACCENT1_EVENPULSE_OFF_TIME 500 //How long ACCENT 1 stays off when lightsaber state is color select
#define ACCENT1_EVENPULSE_ON_TIME 500 //How long ACCENT 1 stays on when lightsaber state is color select

#define ACCENT1_MID_POWER 64
#define ACCENT1_MAX_POWER 128

PwmAccentLED::PwmAccentLED(int anLedPin)
{
	mnLedPin = anLedPin;
	meStyle = eeAccentOff;
	mnTimeBookmark = 0;
	mnLedPowerLevel = 0;
}

PwmAccentLED::~PwmAccentLED()
{
	//Do nothing
}

void PwmAccentLED::Init()
{
	pinMode(mnLedPin, OUTPUT);
	digitalWrite(mnLedPin, LOW);
}

void PwmAccentLED::SetStyle(teAccentStyle aeStyle)
{
	meStyle = aeStyle;
}

void PwmAccentLED::PerformIO()
{
	switch(meStyle)
	{
	case eeAccentOff:
		mnLedPowerLevel = 0;
		break;
	case eeAccentOn:
		mnLedPowerLevel = ACCENT1_MAX_POWER;
		break;
	case eeAccentTwoStagePulse:
		//LED is at mid power or off
		if( (millis() - mnTimeBookmark) > ACCENT1_LSON_LOW_TIME
				&& mnLedPowerLevel < ACCENT1_MAX_POWER)
		{
			mnTimeBookmark = millis();
			mnLedPowerLevel = ACCENT1_MAX_POWER;
		}
		//LED is on at full power
		else if( (millis() - mnTimeBookmark) > ACCENT1_LSON_HIGH_TIME
				&& mnLedPowerLevel > ACCENT1_MID_POWER)
		{
			mnTimeBookmark = millis();
			mnLedPowerLevel = ACCENT1_MID_POWER;
		}
		break;
	case eeAccentIdleBlink:
	    //Accent light is on, turn it off after timeout
		if( (millis() - mnTimeBookmark > ACCENT1_LSOFF_ON_TIME)
				&& 0 != mnLedPowerLevel)
		{
			mnLedPowerLevel = 0;
			mnTimeBookmark = millis();
		}
		//Accent light is off, turn it on after timeout
		else if( (millis() - mnTimeBookmark > ACCENT1_LSOFF_OFF_TIME)
				&& 0 == mnLedPowerLevel)
		{
			mnLedPowerLevel = ACCENT1_MID_POWER;
			mnTimeBookmark = millis();
		}
		break;
	case eeAccentEvenPulse:
	    //Accent light is on, turn it off after timeout
		if( (millis() - mnTimeBookmark > ACCENT1_EVENPULSE_ON_TIME)
				&& 0 != mnLedPowerLevel)
		{
			mnLedPowerLevel = 0;
			mnTimeBookmark = millis();
		}
		//Accent light is off, turn it on after timeout
		else if( (millis() - mnTimeBookmark > ACCENT1_EVENPULSE_ON_TIME)
				&& 0 == mnLedPowerLevel)
		{
			mnLedPowerLevel = ACCENT1_MID_POWER;
			mnTimeBookmark = millis();
		}
		break;
	default:
		meStyle = eeAccentOff;
		break;
	}

	analogWrite(mnLedPin, mnLedPowerLevel);
}
