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

/**
 *
 * @author Jake
 */

import java.util.ArrayList;
import java.util.Queue;

public class tFirmwareVersionDataMsg extends ResponseMessageBase
{
    public tFirmwareVersionDataMsg(ArrayList<Byte> aBytes)
    {
        mMsgId = MessageIds.eeFirmwareVersionData.ordinal();
        mVersionMajor = aBytes.get(1);
        mVersionMinor = aBytes.get(2);
        
    }

    public tFirmwareVersionDataMsg(Queue<Byte> aaBytes)
    {
        mMsgId = aaBytes.remove();
        mVersionMajor = aaBytes.remove();
        mVersionMinor = aaBytes.remove();
    }
    
    public byte mVersionMajor;
    public byte mVersionMinor;
}
