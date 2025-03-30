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
import Settings.tBladeColor;
import java.util.ArrayList;

/**
 *
 * @author Jake
 */
public class tSetBladeConfigMsg extends CmdMsgBase
{
    public tSetBladeConfigMsg()
    {
        mMsgId = (byte)MessageIds.eeCmdSetBladeConfig.eeCmdSetBladeConfig.ordinal();
        mParameters = new Settings.tBladeParameters();
        
        //mSize
        Integer lSize = 3;
        mSize = lSize.shortValue();
    }
    
    @Override
    public ArrayList<Byte> GetBytes()
    {
        ArrayList<Byte> lBytes = new ArrayList();
        //mMsgId
        lBytes.add(mMsgId);

        //mSize
        lBytes.add((byte)(mSize & 0xF));
        lBytes.add((byte)(mSize >> 8));
        
        //mMainColorIndex
        lBytes.add(mParameters.mMainColorIndex);
        //mFlashColorIndex
        lBytes.add(mParameters.mFlashColorIndex);
        //mFlickerIndex
        lBytes.add(mParameters.mFlickerIndex);
        //mLockupIntensity
        lBytes.add(mParameters.mLockupColorIntensity);
        //mLockupFlickerIntensity
        lBytes.add(mParameters.mLockupFlickerIntensity);
        //mLockupFramePeriod
        lBytes.addAll(Util.Converter.short2bytes(mParameters.mLockupFramePeriod));
        return lBytes;
        
    }
    
    public Short mSize;
    
    public Settings.tBladeParameters mParameters;   
}
