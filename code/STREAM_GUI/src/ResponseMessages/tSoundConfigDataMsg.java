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

import java.util.ArrayList;
import java.util.Queue;
import Util.Converter;
/**
 *
 * @author Jake
 */
public class tSoundConfigDataMsg extends ResponseMessageBase
{
    public tSoundConfigDataMsg(ArrayList<Byte> aaBytes)
    {
        System.out.print("Building sound data from ");
        System.out.print(aaBytes.size());
        System.out.println(" bytes:");
        for(int lnIdx = 0; lnIdx < aaBytes.size(); lnIdx++)
        {
            System.out.print((int)aaBytes.get(lnIdx).byteValue());
            if(lnIdx <= aaBytes.size()-2)
            {
                System.out.print(",");
            }
        }
        System.out.println("");
        
        mParameters = new Settings.tSoundParameters();
        
        mMsgId = MessageIds.eeSoundConfigData.ordinal();
        
        //Low Byte + High Byte for the size variable
        mSize = Converter.bytes2short(aaBytes.get(1), aaBytes.get(2));
        
        //Low Byte + High byte for power on blade delay
        mParameters.mPowerOnBladeDelay =
                Converter.bytes2short(aaBytes.get(3), aaBytes.get(4));
        mParameters.mPowerOnTime = 
                Converter.bytes2short(aaBytes.get(5), aaBytes.get(6));
        mParameters.mPowerOffTime = 
                Converter.bytes2short(aaBytes.get(7), aaBytes.get(8));
        mParameters.mBlasterFlashTime =
                Converter.bytes2short(aaBytes.get(9), aaBytes.get(10));
        mParameters.mBlasterSuppressTime =
                Converter.bytes2short(aaBytes.get(11), aaBytes.get(12));        
        mParameters.mClashSwingSuppressTime =
                Converter.bytes2short(aaBytes.get(13), aaBytes.get(14));
        mParameters.mMinSwingInterval =
                Converter.bytes2short(aaBytes.get(15), aaBytes.get(16));
        mParameters.mMaxSwingInterval =
                Converter.bytes2short(aaBytes.get(17), aaBytes.get(18));
        mParameters.mHumRelaunchInterval =
                Converter.bytes2short(aaBytes.get(19), aaBytes.get(20));
    }
    
    public tSoundConfigDataMsg(Queue<Byte> aaBytes)
    {
        mParameters = new Settings.tSoundParameters();
        
        mMsgId = aaBytes.remove();
        
        byte lLowByte = aaBytes.remove();
        byte lHighByte = aaBytes.remove();
        //Low Byte + High Byte for the size variable
        mSize = Converter.bytes2short(lLowByte, lHighByte);
        ShortByteCheck(lLowByte, lHighByte, mSize);
        
        ArrayList<Byte> lBytes = new ArrayList();
        byte lMsgId = (byte)mMsgId;
        lBytes.add(lMsgId);
        lBytes.add(lLowByte);
        lBytes.add(lHighByte);
        for(int lIdx = 0; lIdx < (int)mSize; lIdx++)
        {
            lBytes.add(aaBytes.remove());
        }
        
        tSoundConfigDataMsg lSoundConfigDataMsg = new tSoundConfigDataMsg(lBytes);
        
        mMsgId = lSoundConfigDataMsg.mMsgId;
        mSize = lSoundConfigDataMsg.mSize;
        mParameters = lSoundConfigDataMsg.mParameters;
               
    }
    
    private void ShortByteCheck(byte aLowByte, byte aHighByte, short aValue)
    {
        System.out.print("ShortByteCheck: ");
        System.out.print((int)aHighByte);
        System.out.print(",");
        System.out.print((int)aLowByte);
        System.out.print(":");
        System.out.println((int)aValue);
    }
    
    public short mSize;
    
    public Settings.tSoundParameters mParameters;
}
