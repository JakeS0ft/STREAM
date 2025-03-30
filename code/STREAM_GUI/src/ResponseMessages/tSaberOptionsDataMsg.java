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
package ResponseMessages;

import java.util.Queue;

/**
 *
 * @author Jake
 */
public class tSaberOptionsDataMsg  extends ResponseMessageBase
{
    public tSaberOptionsDataMsg(Queue<Byte> aaBytes)
    {
        mMsgId = aaBytes.remove();
        
        mOptions.mExitLockupOnSwing = aaBytes.remove();
        mOptions.mAutoPowerOffTimeSec = aaBytes.remove();

        //Fetch power off switch time
        byte lLowByte = aaBytes.remove();
        byte lHighByte = aaBytes.remove();
        mOptions.mPowerOffSwitchHoldTimeMs =
                Util.Converter.bytes2short(lLowByte,
                                           lHighByte);
       
        //Fetch menu hold switch time
        lLowByte = aaBytes.remove();
        lHighByte = aaBytes.remove();
        mOptions.mMenuSwitchHoldTimeMs =
                Util.Converter.bytes2short(lLowByte,
                                           lHighByte);
        
        //Fetch sleep time
        mOptions.mSleepTimeMins = aaBytes.remove();
        
        //Fetch One button blaster enable flag
        mOptions.mOneButtonBlasterEnabled = aaBytes.remove();
        
        //Fetch one button lockup enabled flag
        mOptions.mOneButtonLockupEnabled = aaBytes.remove();
        
        //Fetch use font boot sound flag
        mOptions.mUseFontBootSounds = aaBytes.remove();
        
        //Fetch blade length
        mOptions.mBladeLength = aaBytes.remove();
    }
    
    public Settings.tOptions mOptions = new Settings.tOptions();
}
