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

/**
 *
 * @author Jake
 */
public class tSaberInfoDataMsg extends ResponseMessageBase
{
    public tSaberInfoDataMsg(ArrayList<Byte> aBytes)
    {
        mMsgId = aBytes.get(0);
        mNumberOfProfiles = aBytes.get(1);
        mNumberOfColorPresets = aBytes.get(2);
        mNumBladeFlickers = aBytes.get(3);
        mNumBladeChannels = aBytes.get(4);
        mMaxVolume = aBytes.get(5);
        mSelecedProfileIndex = aBytes.get(6);
    }
    
    public tSaberInfoDataMsg(Queue<Byte> aaBytes)
    {
        mMsgId = aaBytes.remove();
        mNumberOfProfiles = aaBytes.remove();
        mNumberOfColorPresets = aaBytes.remove();
        mNumBladeFlickers = aaBytes.remove();
        mNumBladeChannels = aaBytes.remove();
        mMaxVolume = aaBytes.remove();
        mSelecedProfileIndex = aaBytes.remove();
    }
    
    public byte mNumberOfProfiles;
    public byte mNumberOfColorPresets;
    public byte mNumBladeFlickers;
    public byte mNumBladeChannels;
    public byte mMaxVolume;
    public byte mSelecedProfileIndex;
}
