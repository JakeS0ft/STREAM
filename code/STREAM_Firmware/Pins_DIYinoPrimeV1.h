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
/*
 * Pins_DIYinoStardust.h
 *   Defines the pre-mapped pins on a DIYino Stardust V1 prop board.
 *
 *  Created on: Mar 22, 2017
 *      Author: JakeSoft
 */

#ifndef PINS_DIYINOPRIMEV1_H_
#define PINS_DIYINOPRIMEV1_H_

#include <Arduino.h>

#define SOUND_TX_PIN 7   //Serial TX pin for sound
#define SOUND_RX_PIN 8   //Serial RX pin for sound

#define LED_LS1_PIN      3    //Low-side 1 for LED control
#define LED_LS2_PIN      5    //Low-side 2 for LED control
#define LED_LS3_PIN      6    //Low-side 3 for LED control

#define BUTTON2_PIN      4    //Input for Button 2
#define BUTTON1_PIN  	 12   //Input for Button 1

#define ACCENT1          11   //Accent LED

#define PIXEL_DATA_PIN   13   //Data pin for NeoPixel blades

#define MP3_PSWITCH_PIN  A1   //Power control for MP3 chip
#define FTDI_PSWITCH_PIN A2   //Power control for FTDI chip

#define MPU_SDA_PIN      A4   //I2C serial data line for MPU6050
#define MPU_SCL_PIN      A5   //I2C serial clock for MPU6050

#define SPK1_PIN         A6   //Speaker feedback input 1
#define SPK2_PIN         A7   //Speaker feedback input 2

#endif /* PINS_DIYINOPRIMEV1_H_ */
