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
 * MPU6050Calibrate.cpp
 *
 *  Created on: Sep 13, 2018
 *      Author: JakeSoft
 */


#include <Arduino.h>
#include "MPU6050Calibrate.h"
#include <support/I2Cdev/I2Cdev.h>
#include <EEPROM.h>
#include <USaber.h>

//#define MPUCAL_DEBUG

//Address where calibraiton data starts in the EEPROM
#define MEMORYBASEMPUCALIBOFFSET 488

//GYRO offset registers
#define MPU6050_RA_XG_OFFS_USRH     0x13 //[15:0] XG_OFFS_USR
#define MPU6050_RA_XG_OFFS_USRL     0x14
#define MPU6050_RA_YG_OFFS_USRH     0x15 //[15:0] YG_OFFS_USR
#define MPU6050_RA_YG_OFFS_USRL     0x16
#define MPU6050_RA_ZG_OFFS_USRH     0x17 //[15:0] ZG_OFFS_USR
#define MPU6050_RA_ZG_OFFS_USRL     0x18

//Accelerometer offset registers
#define MPU6050_RA_XA_OFFS_H        0x06 //[15:0] XA_OFFS
#define MPU6050_RA_XA_OFFS_L_TC     0x07
#define MPU6050_RA_YA_OFFS_H        0x08 //[15:0] YA_OFFS
#define MPU6050_RA_YA_OFFS_L_TC     0x09
#define MPU6050_RA_ZA_OFFS_H        0x0A //[15:0] ZA_OFFS
#define MPU6050_RA_ZA_OFFS_L_TC     0x0B

//I2C Address of the MPU6050
#define MPU6050_DEFAULT_ADDRESS     0x68

//void SetGyroCalibration(const uint16_t& aXOffset, const uint16_t& aYOffset, const uint16_t& aZOffset )
//{
//	I2Cdev::writeWord(MPU6050_DEFAULT_ADDRESS, MPU6050_RA_XG_OFFS_USRH, aXOffset);
//	I2Cdev::writeWord(MPU6050_DEFAULT_ADDRESS, MPU6050_RA_YG_OFFS_USRH, aYOffset);
//	I2Cdev::writeWord(MPU6050_DEFAULT_ADDRESS, MPU6050_RA_ZG_OFFS_USRH, aZOffset);
//
//#ifdef MPUCAL_DEBUG
//	Serial.println("Sent gyro calibration to MPU6050.");
//#endif
//}
//
//void SetAccelCalibration(const uint16_t& aXOffset, const uint16_t& aYOffset, const uint16_t& aZOffset )
//{
//	I2Cdev::writeWord(MPU6050_DEFAULT_ADDRESS, MPU6050_RA_XA_OFFS_H, aXOffset);
//	I2Cdev::writeWord(MPU6050_DEFAULT_ADDRESS, MPU6050_RA_YA_OFFS_H, aYOffset);
//	I2Cdev::writeWord(MPU6050_DEFAULT_ADDRESS, MPU6050_RA_ZA_OFFS_H, aZOffset);
//
//#ifdef MPUCAL_DEBUG
//	Serial.println("Sent accel calibration to MPU6050.");
//#endif
//}

uint16_t EEPROMReadInt(const int& aAddress)
{
	//Fetch high and low bytes from EEPROM
	uint8_t lLowByte = EEPROM.read(aAddress);
	uint8_t lHighByte = EEPROM.read(aAddress+1);

	//Combine the high and low bytes to a uint16
	uint16_t lReturnValue = ((uint16_t)lHighByte << 8) + (uint16_t)lLowByte;

	return lReturnValue;
}

void LoadCalFromEEPROM()
{
	//Read Accel offset x, y, then z
	int16_t lXOffset = (int16_t)EEPROMReadInt(MEMORYBASEMPUCALIBOFFSET);
	int16_t lYOffset = (int16_t)EEPROMReadInt(MEMORYBASEMPUCALIBOFFSET+2);
	int16_t lZOffset = (int16_t)EEPROMReadInt(MEMORYBASEMPUCALIBOFFSET+4);

	//Set accelerometer cal
	I2Cdev::writeWord(MPU6050_DEFAULT_ADDRESS, MPU6050_RA_XA_OFFS_H, lXOffset);
	I2Cdev::writeWord(MPU6050_DEFAULT_ADDRESS, MPU6050_RA_YA_OFFS_H, lYOffset);
	I2Cdev::writeWord(MPU6050_DEFAULT_ADDRESS, MPU6050_RA_ZA_OFFS_H, lZOffset);

	//Read Gyro offset x, y, then z
	lXOffset = (int16_t)EEPROMReadInt(MEMORYBASEMPUCALIBOFFSET+6);
	lYOffset = (int16_t)EEPROMReadInt(MEMORYBASEMPUCALIBOFFSET+8);
	lZOffset = (int16_t)EEPROMReadInt(MEMORYBASEMPUCALIBOFFSET+10);

	//Set Gyro cal
	I2Cdev::writeWord(MPU6050_DEFAULT_ADDRESS, MPU6050_RA_XG_OFFS_USRH, lXOffset);
	I2Cdev::writeWord(MPU6050_DEFAULT_ADDRESS, MPU6050_RA_YG_OFFS_USRH, lYOffset);
	I2Cdev::writeWord(MPU6050_DEFAULT_ADDRESS, MPU6050_RA_ZG_OFFS_USRH, lZOffset);

	//Send calibration based on values read from EEPROM
	//SetAccelCalibration(lAccelXOffset, lAccelYOffset, lAccelZOffset);
	//SetGyroCalibration(lGyroXOffset, lGyroYOffset, lGyroZOffset);

#ifdef MPUCAL_DEBUG
	Serial.print("Loaded offsets: ");
	Serial.print(" Ax:"); Serial.print(lAccelXOffset);
	Serial.print(" Ay:"); Serial.print(lAccelYOffset);
	Serial.print(" Az:"); Serial.print(lAccelZOffset);
	Serial.print(" Gx:"); Serial.print(lGyroXOffset);
	Serial.print(" Gy:"); Serial.print(lGyroYOffset);
	Serial.print(" Gz:"); Serial.println(lGyroZOffset);

	Serial.print("Byte Before:");Serial.print((int)lByteBefore);
	Serial.print(" Byte After :");Serial.print((int)lByteAfter);
#endif
}
