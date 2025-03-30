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
public class tProductIdVersionDataMsg extends ResponseMessageBase
{
    public tProductIdVersionDataMsg(ArrayList<Byte> aBytes) {
        mMsgId = MessageIds.eeProtocolVersionData.ordinal();
        mSize = aBytes.get(1);

        if (aBytes.size() > 1) {
            mProductIdStr = "";
            byte[] laProdNameBuffer = new byte[mSize];
            
            for (int lIdx = 2; lIdx < aBytes.size(); lIdx++) {
                byte lByte = aBytes.get(lIdx);
                laProdNameBuffer[lIdx-2] = lByte;
            }
            
            mProductIdStr = bytesToStringUTFCustom(laProdNameBuffer);
            
        } else {
            mProductIdStr = "null";
        }
    }

    public tProductIdVersionDataMsg(Queue<Byte> aBytes) {
        mMsgId = aBytes.remove();
        mSize = aBytes.remove();

        if (aBytes.size() > 1) {
            mProductIdStr = "";
            byte[] laProdNameBuffer = new byte[mSize];
            
            for (int lIdx = 0; lIdx < mSize; lIdx++) {
                byte lByte = aBytes.remove();
                laProdNameBuffer[lIdx] = lByte;
            }
            
            mProductIdStr = bytesToStringUTFCustom(laProdNameBuffer);
            
        } else {
            mProductIdStr = "null";
        }
    }
    
    /**
     * 
     * @param bytes
     * @return 
     */
    public String bytesToStringUTFCustom(byte[] bytes) {
        char[] buffer = new char[bytes.length >> 1];
        for (int i = 0; i < buffer.length; i++) {
            int bpos = i << 1;
            char c = (char) (((bytes[bpos] & 0x00FF) << 8) + (bytes[bpos + 1] & 0x00FF));
            buffer[i] = c;
        }
        return new String(buffer);
    }
    
    public byte mSize;
    public String mProductIdStr;    
}
