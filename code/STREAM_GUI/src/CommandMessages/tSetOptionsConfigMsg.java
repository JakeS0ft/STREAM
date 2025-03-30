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
package CommandMessages;

import java.util.ArrayList;

/**
 *
 * @author Jake
 */
public class tSetOptionsConfigMsg extends CmdMsgBase
{
    public tSetOptionsConfigMsg() 
    {
        mMsgId = (byte)MessageIds.eeCmdSetOptionsConfig.ordinal();
    }
    
   @Override
   public ArrayList<Byte> GetBytes()
   {
       ArrayList<Byte> lBytes = new ArrayList();
       
       lBytes.add(mMsgId);
       
       //mExitLockupOnSwing
       lBytes.add(mOptions.mExitLockupOnSwing);
       //mAutoPowerOffTimeSec
       lBytes.add(mOptions.mAutoPowerOffTimeSec);
       //mPowerOffSwitchHoldTime
       lBytes.addAll(Util.Converter.short2bytes(mOptions.mPowerOffSwitchHoldTimeMs));
       //mMenuSwitchHoldTime
       lBytes.addAll(Util.Converter.short2bytes(mOptions.mMenuSwitchHoldTimeMs));
       //mSleepTimeMins
       lBytes.add(mOptions.mSleepTimeMins);
       //mOneButtonBlasterEnabled
       lBytes.add(mOptions.mOneButtonBlasterEnabled);
       //mOneButtonLockupEnabled
       lBytes.add(mOptions.mOneButtonLockupEnabled);
       //mUseFontBootSounds
       lBytes.add(mOptions.mUseFontBootSounds);
       //mBladeLength
       lBytes.add(mOptions.mBladeLength);
       return lBytes;
       
   }
   
   public Settings.tOptions mOptions = new Settings.tOptions();
}
