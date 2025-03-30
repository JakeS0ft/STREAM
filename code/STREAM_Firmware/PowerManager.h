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

#ifndef POWERMANAGER_H_
#define POWERMANAGER_H_

#include <SleepManager.h>

class PowerManager : public SleepManager
{
public:
	PowerManager();

	static void SleepCallback();

	static void WakeCallback();

	static void SetPins(char aFTDIPowerPin, char aSoundPowerPin, char aAccentLEDPin);

	static void SetSerialPins(char aTxPin, char aRxPin);

	static void SetPixelDataPin(char aPixelDataPin);

protected:

	static void WritePin(char aPin, char aValue);

	static char sFTDIPowerPin;
	static char sSoundPowerPin;
	static char sAccentLEDPin;

	static char sSerialTxPin;
	static char sSerialRxPin;

	static char sPixelDataPin;
};



#endif /* POWERMANAGER_H_ */
