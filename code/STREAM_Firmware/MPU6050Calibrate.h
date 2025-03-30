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
 * MPU6050Calibrate.h
 *
 *  Created on: Sep 13, 2018
 *      Author: JakeSoft
 */

#ifndef MPU6050CALIBRATE_H_
#define MPU6050CALIBRATE_H_

#include <Arduino.h>

class Mpu6050AdvancedMotionManager;

void SetGyroCalibration(const uint16_t& aXOffset, const uint16_t& aYOffset, const uint16_t& aZOffset);

void SetAccelCalibration(const uint16_t& aXOffset, const uint16_t& aYOffset, const uint16_t& aZOffset);

void LoadCalFromEEPROM();

#endif /* MPU6050CALIBRATE_H_ */
