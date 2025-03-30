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
public class tSetSoundConfigMsg extends CmdMsgBase
{
   public tSetSoundConfigMsg()
   {
       mMsgId = (byte)MessageIds.eeCmdSetSoundConfig.ordinal();
       mSize = 18;
       mParameters = new Settings.tSoundParameters();
   }
   
   @Override
   public ArrayList<Byte> GetBytes()
   {
       ArrayList<Byte> lBytes = new ArrayList();
       
       lBytes.add(mMsgId);
       lBytes.addAll(Util.Converter.short2bytes(mSize));
       
       lBytes.addAll(Util.Converter.short2bytes(mParameters.mPowerOnBladeDelay));
       lBytes.addAll(Util.Converter.short2bytes(mParameters.mPowerOnTime));
       lBytes.addAll(Util.Converter.short2bytes(mParameters.mPowerOffTime));
       lBytes.addAll(Util.Converter.short2bytes(mParameters.mBlasterFlashTime));
       lBytes.addAll(Util.Converter.short2bytes(mParameters.mBlasterSuppressTime));
       lBytes.addAll(Util.Converter.short2bytes(mParameters.mClashSwingSuppressTime));
       lBytes.addAll(Util.Converter.short2bytes(mParameters.mMinSwingInterval));
       lBytes.addAll(Util.Converter.short2bytes(mParameters.mMaxSwingInterval));
       lBytes.addAll(Util.Converter.short2bytes(mParameters.mHumRelaunchInterval));
       
       System.out.print("tSetSoundConfigMsg size is ");
       System.out.print(lBytes.size());
       System.out.print("tSetSoundConfigMsg byes:");
       for(Byte lCurByte : lBytes)
       {
           System.out.print(lCurByte);
           System.out.print(",");
       }
       System.out.println("");
       
       return lBytes;
   }
   
   public Short mSize;
   public Settings.tSoundParameters mParameters;
}
