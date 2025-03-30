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
public class tLockupTestMsg extends CmdMsgBase {
    
    public tLockupTestMsg()
    {
       mMsgId = (byte)MessageIds.eeCmdLockupTest.ordinal(); 
    }
    
    @Override
    public ArrayList<Byte> GetBytes()
    {   
        ArrayList<Byte> lBytes = new ArrayList();
        
        lBytes.add(mMsgId); //1        
        lBytes.add(mTestLenSec); //2
        lBytes.add(mColorIntensity); //3
        lBytes.add(mFlickerIntensity);//4
        lBytes.addAll(Util.Converter.short2bytes(mFramePeriod));
        
        for(byte lCurByte : mMainColorChannels)
        {
            lBytes.add(lCurByte);
        }

        for(byte lCurByte : mLockupColorChannels)
        {
            lBytes.add(lCurByte);
        }
                
        return lBytes;
    }
    
    public byte mMsgId;

    public byte mTestLenSec;       //Length of test (seconds)
    public byte mColorIntensity;   //Color Intensity (%);
    public byte mFlickerIntensity; //Flicker Intensity (%)
    public short mFramePeriod;     //Fame period (10 to 1000 ms)

    public byte[] mMainColorChannels = new byte[3];
    public byte[] mLockupColorChannels = new byte[3];
}
