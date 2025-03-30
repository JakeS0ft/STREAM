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
public class tBladeControlSetChannelMsg extends CmdMsgBase
{
    public tBladeControlSetChannelMsg()
    {
        mMsgId = (byte)MessageIds.eeCmdBladeControlSetChannel.ordinal();
    }

    public ArrayList<Byte> GetBytes()
    {
        ArrayList<Byte> lBytes = new ArrayList();
        
        lBytes.add(mMsgId);
        lBytes.add((byte)(mChannel & 0xF));
        lBytes.add((byte)(mChannel >> 8));
        lBytes.add(mValue);
        
        return lBytes;
    }
    public Short mChannel;
    public Byte mValue;  
}
