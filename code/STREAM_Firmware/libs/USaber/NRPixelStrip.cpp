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
*
*  This code is based on research and demo by Josh Levine (a.k.a. [Josh], a.k.a. bigjosh)
* <https://github.com/bigjosh/SimpleNeoPixelDemo>
******************************************************************************/

/*
 * NRPixelStrip.cpp
 *
 *  Created on: Nov 29, 2018
 *      Authors: JakeSoft and Josh Levine
 */


#include <Arduino.h>
#include "support/NRPixelStrip/NRPixelStrip.h"

NRPixelStrip::NRPixelStrip()
{
	//Do nothing
}

NRPixelStrip::~NRPixelStrip()
{
	//Do nothing
}

void NRPixelStrip::sendBit( bool aBitVal )
{
	// Actually send a bit to the string. We must to drop to asm to enusre that the complier does
	// not reorder things and make it so the delay happens in the wrong place.
    if (  aBitVal ) {				// 0 bit

		asm volatile (
			"sbi %[port], %[bit] \n\t"				// Set the output bit
			".rept %[onCycles] \n\t"                                // Execute NOPs to delay exactly the specified number of cycles
			"nop \n\t"
			".endr \n\t"
			"cbi %[port], %[bit] \n\t"                              // Clear the output bit
			".rept %[offCycles] \n\t"                               // Execute NOPs to delay exactly the specified number of cycles
			"nop \n\t"
			".endr \n\t"
			::
			[port]		"I" (_SFR_IO_ADDR(PIXEL_PORT)),
			[bit]		"I" (PIXEL_BIT),
			[onCycles]	"I" (NS_TO_CYCLES(T1H) - 2),		// 1-bit width less overhead  for the actual bit setting, note that this delay could be longer and everything would still work
			[offCycles] 	"I" (NS_TO_CYCLES(T1L) - 2)			// Minimum interbit delay. Note that we probably don't need this at all since the loop overhead will be enough, but here for correctness

		);

    } else {					// 1 bit

		// **************************************************************************
		// This line is really the only tight goldilocks timing in the whole program!
		// **************************************************************************
		asm volatile (
			"sbi %[port], %[bit] \n\t"				// Set the output bit
			".rept %[onCycles] \n\t"				// Now timing actually matters. The 0-bit must be long enough to be detected but not too long or it will be a 1-bit
			"nop \n\t"                              // Execute NOPs to delay exactly the specified number of cycles
			".endr \n\t"
			"cbi %[port], %[bit] \n\t"              // Clear the output bit
			".rept %[offCycles] \n\t"               // Execute NOPs to delay exactly the specified number of cycles
			"nop \n\t"
			".endr \n\t"
			::
			[port]		"I" (_SFR_IO_ADDR(PORTB)),
			[bit]		"I" (PIXEL_BIT),
			[onCycles]	"I" (NS_TO_CYCLES(T0H) - 2),
			[offCycles]	"I" (NS_TO_CYCLES(T0L) - 2)

		);

    }

    // Note that the inter-bit gap can be as long as you want as long as it doesn't exceed the 5us reset timeout (which is A long time)
    // Here I have been generous and not tried to squeeze the gap tight but instead erred on the side of lots of extra time.
    // This has thenice side effect of avoid glitches on very long strings becuase


}

void NRPixelStrip::sendByte( unsigned char aByte )
{

    for( unsigned char bit = 0 ; bit < 8 ; bit++ ) {

      sendBit( bitRead( aByte , 7 ) );                // Neopixel wants bit in highest-to-lowest order
                                                     // so send highest bit (bit #7 in an 8-bit byte since they start at 0)
      aByte <<= 1;                                    // and then shift left so bit 6 moves into 7, 5 moves into 6, etc

    }
}

// Set the specified pin up as digital out
void NRPixelStrip::Init()
{

	//pinMode(mPixelPin, OUTPUT);
	bitSet( PIXEL_DDR , PIXEL_BIT );

}

void NRPixelStrip::SendPixel( unsigned char aRed, unsigned char aGreen , unsigned char aBlue )
{

  sendByte(aGreen);          // Neopixel wants colors in green then red then blue order
  sendByte(aRed);
  sendByte(aBlue);

}

void NRPixelStrip::SendPixels(unsigned char aRed, unsigned char aGreen, unsigned char aBlue, int aNumPixels)
{
	for(int lPxlIdx = 0; lPxlIdx < aNumPixels; lPxlIdx++)
	{
		SendPixel(aRed, aGreen, aBlue);
	}
}

void NRPixelStrip::Show()
{
	//This doesn't work. Tried on Pro-Mini 8MHz, even tried doubling original timing from 6000 to 12000.
	//_delay_us( (RES / 1000UL) + 1);				// Round up since the delay must be _at_least_ this long (too short might not work, too long not a problem)

	//Using because delayMicroseconds() and timing different than Josh's original code
	//because _delay_us() with the original timing doesn't work for unknown reasons
	delayMicroseconds(RES);

	if(mUseSlowFrames)
	{
		delay(1);
	}
}

void NRPixelStrip::SetUseSlowFrames(bool aUse)
{
	mUseSlowFrames = aUse;
}
