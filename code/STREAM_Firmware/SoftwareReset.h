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
 * SoftwareReset.h
 *
 *  Created on: Jul 24, 2017
 *      Author: JakeSoft
 */

#ifndef SOFTWARERESET_H_
#define SOFTWARERESET_H_


#include <avr/wdt.h> //for watchdog timer
#include <Arduino.h> //for delay()

//Watchdog timer disable function
void wdt_init(void) __attribute__((naked)) __attribute__((section(".init3")));

void wdt_init(void)
{
    MCUSR = 0;
    wdt_disable();

    return;
}

// Resets the processor
void software_Reset()
// Restarts program from beginning but
// does not reset the peripherals and registers
{
	delay(100);
	wdt_enable(WDTO_500MS);
	while(true){}
}


#endif /* SOFTWARERESET_H_ */
