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

#include <avr/wdt.h>
#include "PowerManager.h"
#include "Arduino.h"


//Static initializers
char PowerManager::sFTDIPowerPin = -1;
char PowerManager::sSoundPowerPin = -1;
char PowerManager::sAccentLEDPin = -1;

char PowerManager::sSerialTxPin = -1;
char PowerManager::sSerialRxPin = -1;

char PowerManager::sPixelDataPin = -1;

void(* SoftReset) (void) = nullptr;//declare reset function at address 0

PowerManager::PowerManager()
{
	mpSleepCallback = &SleepCallback;
	mpWakeCallback = &WakeCallback;
}

void PowerManager::SleepCallback()
{
	Serial.println("Sleeping.");
	delay(250);

	//Kill the serial pins to stop them from back-feeding the
	//sound chip after it is turned off
	if(sSerialTxPin > 0)
	{
		pinMode(sSerialTxPin, OUTPUT);
		digitalWrite(sSerialTxPin, LOW);
	}
	if(sSerialRxPin > 0)
	{
		//This should already be an output
		//(Here, RX means RX on the sound module, not RX on the MCU)
		digitalWrite(sSerialRxPin, LOW);
	}

	//Turn off the FTDI power
	WritePin(sFTDIPowerPin, HIGH);

	//Turn off the sound chip power
	WritePin(sSoundPowerPin, HIGH);

	//Turn off the accent LED
	WritePin(sAccentLEDPin, LOW);

	//Set data pixel high to prevent back-feeding the pixel strip
	//though the data pin
	WritePin(sPixelDataPin, HIGH);

}

void PowerManager::WakeCallback()
{
	wdt_reset();
	wdt_enable(WDTO_8S);

	delay(100);

	//Re-enable the DATA pixel pin
	WritePin(sPixelDataPin, LOW);

	//Re-enable the serial pins back to their functional states
	if(sSerialTxPin > 0)
	{
		pinMode(sSerialTxPin, INPUT_PULLUP);
	}
	if(sSerialRxPin > 0)
	{
		digitalWrite(sSerialRxPin, HIGH);
	}

	//Turn the sound chip back on
	WritePin(sSoundPowerPin, LOW);

	//Turn the FTDI chip back on
	WritePin(sFTDIPowerPin, LOW);

	//Give time for sound chip to initialize before the state machine
	//might try to send any commands to it
	delay(300);

}

void PowerManager::SetPins(char aFTDIPowerPin, char aSoundPowerPin, char aAccentLEDPin)
{
	sFTDIPowerPin = aFTDIPowerPin;
	sSoundPowerPin = aSoundPowerPin;
	sAccentLEDPin = aAccentLEDPin;
}

void PowerManager::SetSerialPins(char aTxPin, char aRxPin)
{
	sSerialTxPin = aTxPin;
	sSerialRxPin = aRxPin;
}

void PowerManager::SetPixelDataPin(char aPixelDataPin)
{
	sPixelDataPin = aPixelDataPin;
}

void PowerManager::WritePin(char aPin, char aValue)
{
	if(aPin >= 0)
	{
		digitalWrite(aPin, aValue);
	}
}
