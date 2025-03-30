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

import Settings.tBladeParameters;
import java.util.ArrayList;
import java.util.Queue;

/**
 *
 * @author Jake
 */
public class tBladeConfigDataMsg extends ResponseMessageBase
{
    public tBladeConfigDataMsg(Queue<Byte> aaBytes)
    {
        System.out.print("Creating Blade Config message from ");
        System.out.print(aaBytes.size());
        System.out.println(" bytes.");
        
        mMsgId = aaBytes.remove();

        //High byte + Low Byte for the size variable
        mSize = aaBytes.remove();
        mSize += (aaBytes.remove() << 8);

        mParameters.mMainColorIndex = aaBytes.remove();
        mParameters.mFlashColorIndex = aaBytes.remove();
        mParameters.mFlickerIndex = aaBytes.remove();
        mParameters.mLockupColorIntensity = aaBytes.remove();
        mParameters.mLockupFlickerIntensity = aaBytes.remove();
        mParameters.mLockupFramePeriod = aaBytes.remove();
        mParameters.mLockupFramePeriod += (aaBytes.remove() << 8);
    }
    
    public short mSize;
    public tBladeParameters mParameters = new tBladeParameters();
}
