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

import com.fazecast.jSerialComm.SerialPort;
import CommandMessages.CmdMsgBase;
import java.util.ArrayList;
/**
 *
 * @author Jake
 */
public class CommandMessageSender
{
    public CommandMessageSender(SerialPort aPort)
    {
        mSerialPort = aPort;
    }
    
    /**
     * Sends a single byte message.
     * @param aByte - Byte to send
     */
    public void SendOneByteMessage(Byte aByte)
    {
        CommandMessages.OneByteMsg lMsg = new CommandMessages.OneByteMsg(aByte);
        SendMessage(lMsg);
    }
    
    /**
     * Sends a command message.
     * @param aMsgOut 
     */
    public void SendMessage(CmdMsgBase aMsgOut)
    {
        SendMessage(aMsgOut.GetBytes());
    }
    
    /**
     * Sends raw bytes.
     * @param aBytes 
     */
    public synchronized void SendMessage(ArrayList<Byte> aBytes)
    {   
        //Create the send buffer of raw bytes
        byte[] lSendBuffer = new byte[aBytes.size()];
        for(int lIdx = 0; lIdx < aBytes.size(); lIdx++)
        {       
           lSendBuffer[lIdx] = aBytes.get(lIdx); 
        }
        System.out.println("");
        
        System.out.print("Sending bytes:");
        for(byte lSendByte : lSendBuffer)
        {
            System.out.print(lSendByte);
            System.out.print(",");
        }
        System.out.println("");
        
        //Send the buffer
        mSerialPort.writeBytes(lSendBuffer, lSendBuffer.length);
                
    }
    
    //Serial port object to handle sending data
    SerialPort mSerialPort;    
}
