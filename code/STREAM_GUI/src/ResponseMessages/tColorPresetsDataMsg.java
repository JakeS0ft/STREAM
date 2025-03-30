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

import Settings.tBladeColor;
import java.util.ArrayList;
import java.util.Queue;

/**
 *
 * @author Jake
 */
public class tColorPresetsDataMsg extends ResponseMessageBase
{
    tColorPresetsDataMsg(Queue<Byte> aaBytes)
    {
        System.out.print("Creating Color Presets data message from ");
        System.out.print(aaBytes.size());
        System.out.println(" bytes.");
        mMsgId = aaBytes.remove();

        //High byte + Low Byte for the size variable
        mSize = aaBytes.remove();
        mSize += (aaBytes.remove() << 8);
        
        //Fetch number of color presets
        mNumColorPresets = aaBytes.remove();
        
        for(int lnColorIdx = 0; lnColorIdx < (int)mNumColorPresets; lnColorIdx++)
        {
            tBladeColor tPreset = new tBladeColor();
            
            tPreset.mChannel1 = aaBytes.remove();
            tPreset.mChannel2 = aaBytes.remove();
            tPreset.mChannel3 = aaBytes.remove();
            
            System.out.print(tPreset.mChannel1);
            System.out.print("/");
            System.out.print(tPreset.mChannel2);
            System.out.print("/");
            System.out.println(tPreset.mChannel3);
            
            maProfiles.add(tPreset);
        }
    }
    
    public short mSize;
    public byte mNumColorPresets; //Number of color presets
    
    public ArrayList<tBladeColor> maProfiles = new ArrayList();
}
