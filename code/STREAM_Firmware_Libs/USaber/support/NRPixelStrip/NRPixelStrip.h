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
 * NRPixelStrip.h
 *
 *  Created on: Nov 29, 2018
 *      Author: JakeSoft
 */

#include <Arduino.h>

#ifndef NRPIXELSTRIP_H_
#define NRPIXELSTRIP_H_

#define PIXEL_DDR   DDRB   // Port of the pin the pixels are connected to
#define PIXEL_PORT  PORTB  // Port of the pin the pixels are connected to (Pins 8 through 13)

/**
 * BIT within the AVR port used for data.
 * 0 = Arduino pin 8
 * 1 = Arduino pin 9
 * 2 = Arduino pin 10
 * 3 = Arduino pin 11
 * 4 = Arduino pin 12
 * 5 = Arduino pin 13
 */
#define PIXEL_BIT   5      // Bit of the pin the pixels are connected to (Pin 13)

// Timing constraints taken from the WS2812 datasheets
#define T1H  900    // Width of a 1 bit in ns
#define T1L  600    // Width of a 1 bit in ns
#define T0H  400    // Width of a 0 bit in ns
#define T0L  900    // Width of a 0 bit in ns
//#define RES 6000U    // Width of the low gap between bits to cause a frame to latch
#define RES 120 //Frame latch delay (microseconds) Note: Above commented out ns timing did not work

// Macros for using nanoseconds to generate actual CPU delays
#define NS_PER_SEC (1000000000L)          // Note that this has to be SIGNED since we want to be able to check for negative values of derivatives
#define CYCLES_PER_SEC (F_CPU)
#define NS_PER_CYCLE ( NS_PER_SEC / CYCLES_PER_SEC )
#define NS_TO_CYCLES(n) ( (n) / NS_PER_CYCLE )

/**
 * This class provides a no-RAM interface for controlling a WS2812
 * (NeoPixel) strip of addressable LEDs.
 */
class NRPixelStrip
{
public:
	/**
	 * Constructor.
	 */
	NRPixelStrip();

	/**
	 * Destructor.
	 */
	~NRPixelStrip();

	/**
	 * Initializes the I/O pin used to send pixel data. Call this first
	 * before trying to send commands to the pixel strip.
	 */
	void Init();

	/**
	 * Sends a command for a single pixel. This can be used in a loop to send
	 * multiple pixels.
	 * Note: Disable interrupts before calling or timing may be skewed and
	 * you may see unexpected results.
	 * Args:
	 *   aRed - Red value
	 *   aGreen - Green value
	 *   aBlue - Blue value
	 */
	void SendPixel( unsigned char aRed, unsigned char aGreen , unsigned char aBlue );

	/**
	 * Sends the same command to multiple pixels. Useful if you want to light up an entire
	 * region or strip with the same color.
	 * Note: Disable interrupts before calling or timing may be skewed and
	 * you may see unexpected results.
	 * Args:
	 *   aRed - Red value
	 *   aGreen - Green value
	 *   aBlue - Blue value
	 */
	void SendPixels(unsigned char aRed, unsigned char aGreen , unsigned char aBlue, int aNumPixels);

	/**
	 * Waits long enough without sending any bits to cause the pixels to
	 * latch and display the last sent frame.
	 */
	void Show();

	/**
	 * Enable/Disable slow frame rate.
	 * Args:
	 *   aUse - TRUE = Use slow frames by adding an extra millisecond delay between frames
	 *          FALSE (Default) = Don't add any extra delay between frames
	 */
	void SetUseSlowFrames(bool aUse);

protected:

	/**
	 * Send a single bit.
	 * The gawd-awful assembly is limited to this single function.
	 * Args:
	 *   aBitVal - Value to send (TRUE = 1, FALSE = 0)
	 */
	void sendBit( bool aBitVal );

	/**
	 * Send a single byte
	 * Args:
	 *   aByte - Byte to send
	 */
	void sendByte( unsigned char aByte );

	// Flags if slow frames should be used.
	// Sometimes necessary for low-speed strips that run out of spec timings
	bool mUseSlowFrames;

};



#endif /* NRPIXELSTRIP_H_ */
