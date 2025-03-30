/*
 This file is part of the STREAM Graphical User Interface program (STREAM GUI).

 STREAM GUI is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as
 published by the Free Software Foundation, either version 3 of the License,
 or (at your option) any later version.

 The STREAM GUI software is distributed in the hope that it will be
 useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with the software.  If not, see <http://www.gnu.org/licenses/>.
*/
package Settings;

/**
 *
 * @author Jake
 */
public class tSoundParameters {
    
    //time in milliseconds after power-on sound start playing
    //before the LED for blade starts ramping
    public short mPowerOnBladeDelay;
    //time in milliseconds for power on blade ramp to complete
    public short mPowerOnTime;
    //time in milliseconds for power off blade ramp to complete
    public short mPowerOffTime;
    //time in milliseconds for blaster blade pulse
    public short mBlasterFlashTime;
    //time after a blaster block before another blaster event can occur
    public short mBlasterSuppressTime;
    //time after a clash for swing sounds to be suppressed
    public short mClashSwingSuppressTime;
    //minimum time between swing sounds
    public short mMinSwingInterval;
    //maximum time after swing before a new swing is played
    public short mMaxSwingInterval;
    //idle time before hum is played again (milliseconds)
    public short mHumRelaunchInterval;
}
