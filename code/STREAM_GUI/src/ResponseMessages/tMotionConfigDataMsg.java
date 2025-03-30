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
public class tMotionConfigDataMsg extends ResponseMessageBase
{
    
    public tMotionConfigDataMsg(ArrayList<Byte> aaBytes)
    {
        mMsgId = MessageIds.eeMotionConfigData.ordinal();
        
        mSize = Converter.bytes2short(aaBytes.get(1), aaBytes.get(2));
        
        mParameters = new Settings.tMotionParameters();
        mParameters.mSwingLargeTol =
                Converter.bytes2short(aaBytes.get(3), aaBytes.get(4));
        mParameters.mSwingMediumTol = 
                Converter.bytes2short(aaBytes.get(5), aaBytes.get(6));
        mParameters.mSwingSmallTol = 
                Converter.bytes2short(aaBytes.get(7), aaBytes.get(8));
        mParameters.mClashTol =
                Converter.bytes2short(aaBytes.get(9), aaBytes.get(10));
        mParameters.mTwistTol =
                Converter.bytes2short(aaBytes.get(11), aaBytes.get(12));
    }

    public tMotionConfigDataMsg(Queue<Byte> aaBytes)
    {
        mMsgId = aaBytes.remove();
        
        mSize = Converter.bytes2short(aaBytes.remove(), aaBytes.remove());
        
        mParameters = new Settings.tMotionParameters();
        mParameters.mSwingLargeTol =
                Converter.bytes2short(aaBytes.remove(), aaBytes.remove());
        mParameters.mSwingMediumTol = 
                Converter.bytes2short(aaBytes.remove(), aaBytes.remove());
        mParameters.mSwingSmallTol = 
                Converter.bytes2short(aaBytes.remove(), aaBytes.remove());
        mParameters.mClashTol =
                Converter.bytes2short(aaBytes.remove(), aaBytes.remove());
        mParameters.mTwistTol =
                Converter.bytes2short(aaBytes.remove(), aaBytes.remove());
    }    
    
    public short mSize;
    
    public Settings.tMotionParameters mParameters;
}
