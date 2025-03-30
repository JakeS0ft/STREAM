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
 * StreamBuildConfig.h
 *
 *  Created on: Jan 2, 2023
 *      Author: JakeSoft
 */

#ifndef STREAMBUILDCONFIG_H_
#define STREAMBUILDCONFIG_H_

//**********************************************************
//*** Firmware version (only used for official releases) ***
//**********************************************************
#define FIRMWARE_VERSION_MAJOR 1
#define FIRMWARE_VERSION_MINOR 54

//******************************************************************
//*** Load MPU6050 calibration settings from EEPROM              ***
//*** (requires calibration before programming, not recommended) ***
//******************************************************************
//#define LOAD_EEPROM_CAL

//*************************************
//*** Number of blade color presets ***
//*************************************
#define MAX_COLOR_PRESETS 12

//*****************************
//*** Number of sound fonts ***
//*****************************
#define MAX_PROFILES 5

#ifndef HW_STARDUST_V3
 #ifndef HW_DIYINO_PRIME_V1
  #ifndef HW_JAKESOFT_DIYINO
   #ifndef HW_BREWBOARD

//****************************************************
//*** SELECT HARDWARE PINOUT (un-comment only one) ***
//****************************************************
#define HW_BREWBOARD        //DIY "home brew" with Arduino Nano and break-out boards for MPU6050 and DFPlayer
//#define HW_STARDUST_V3       //DIYino Star Dust Version 3
//#define HW_DIYINO_PRIME_V1 //DIYino Prime standard pinout
//#define HW_JAKESOFT_DIYINO //DIYino Prime with JakeSoft's custom pin-out

   #endif
  #endif
 #endif
#endif


#ifndef PIXEL_BLADE
 #ifndef RGB_BLADE

//***********************************************
//*** SELECT Blade Type (un-comment only one) ***
//***********************************************
//#define PIXEL_BLADE //Neopixel
#define RGB_BLADE     //RGB high-powered LED

 #endif
#endif


#endif /* STREAMBUILDCONFIG_H_ */
