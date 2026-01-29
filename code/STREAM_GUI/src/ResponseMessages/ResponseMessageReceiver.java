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
import java.util.LinkedList;
import java.util.Queue;

import com.fazecast.jSerialComm.SerialPort;


/**
 *
 * @author Jake
 */
public class ResponseMessageReceiver implements Runnable {


    public ResponseMessageReceiver(SerialPort aSerialPort)
    {
        mPort = aSerialPort;
        mShutdown = false;
        mReadBuffer = new LinkedList();
        mSubscribers = new ArrayList();
    }
    
    public void Subscribe(ResponseMessageSubscriber aSubscriber)
    {
        mSubscribers.add(aSubscriber);
    }
    
    protected void NotifySubscribers(ResponseMessageBase aMessage)
    {
        for(ResponseMessageSubscriber lSub: this.mSubscribers)
        {
            lSub.NotifyMessage(aMessage);
        }
    }
    
    public void run()
    {
        System.out.println("Starting Rx thread.");
        
        while(!mShutdown)
        {
            if(mPort.bytesAvailable() > 0)
            {
                //Pull all available data
                PopualteReadBuffer();
            }
            
            while(!mReadBuffer.isEmpty())
            {
                try
                {
                    ProcessMessage();
                }
                catch(ResponseMessages.NotEnoughBytesException lEx)
                {
                    mReadBuffer.clear();
                    lEx.printStackTrace();
                }
            }
        }
        
        System.out.println("Rx thread exits.");
    }
    
    private void Sleep(long aTime)
    {
        try
        {
            Thread.sleep(aTime);
        }
        catch(InterruptedException lEx)
        {
            //Whatever.
        }
    }
    
    private synchronized void PopualteReadBuffer()
    {
        
        while (mPort.bytesAvailable() > 0)
        {
            Sleep(20);
            byte[] laBuffer = new byte[1];
            mPort.readBytes(laBuffer, 1);
            mReadBuffer.add(laBuffer[0]);
        }
        
        System.out.print("RX:");
        for(Byte lCurByte : mReadBuffer)
        {
            System.out.print(lCurByte.toString());
            System.out.print(",");
        }
        System.out.println(" ");
    }
    
    private void PrintRxBuffer()
    {
        System.out.print("RX Buf:");
        for(Byte lCurByte : mReadBuffer)
        {
            System.out.print(lCurByte.toString());
            System.out.print(",");
        }
        
        System.out.println("");
    }
    
    private void DumpBuffer()
    {
        System.out.println("START");
        while (mPort.bytesAvailable() > 0) {
            byte[] laBuffer = new byte[1];
            mPort.readBytes(laBuffer, 1);

            System.out.print(laBuffer[0]);

        }
        System.out.println("END");      
    }
    
    public void Stop()
    {
        mShutdown = true;
    }
    
    public synchronized void ProcessMessage() throws ResponseMessages.NotEnoughBytesException
    {        
        System.out.println("Processing message.");
               
        if(0 == mReadBuffer.size() )
        {
            return; //Don't process an empty buffer
        }
        
        System.out.print("Read buffer size is ");
        System.out.println(mReadBuffer.size());
                
        byte lMsgId = mReadBuffer.peek();
        
        System.out.print("Got MessageID:");
        System.out.println(lMsgId);
                
        //DumpBuffer();
        
        if(ResponseMessages.MessageIds.eeAck.ordinal() == lMsgId)
        {
            System.out.println("Ack received.");
            
            //Convert read buffer from raw bytes to a message object
            tAckMsg lAck = new tAckMsg(mReadBuffer);
            
            System.out.print("Ack Message Payload:");
            System.out.println(lAck.mAckMsgPayload);

            NotifySubscribers(lAck);
            
        }
        else if (ResponseMessages.MessageIds.eeProductIdData.ordinal() == lMsgId)
        {
            System.out.println("Product ID message received.");
            
            tProductIdVersionDataMsg lMsg = new tProductIdVersionDataMsg(mReadBuffer);
            
            System.out.print("Product ID Str Size:");
            System.out.println(lMsg.mSize);
            System.out.print("Product ID:");
            System.out.println(lMsg.mProductIdStr);
            
            NotifySubscribers(lMsg);
        }
        else if (ResponseMessages.MessageIds.eeFirmwareVersionData.ordinal() == lMsgId)
        {
            System.out.println("Firmware Version message received.");
            
            tFirmwareVersionDataMsg lMsg = new tFirmwareVersionDataMsg(mReadBuffer);
            
            System.out.print("Version: ");
            System.out.print(lMsg.mVersionMajor);
            System.out.print(".");
            System.out.println(lMsg.mVersionMinor);
            
            NotifySubscribers(lMsg);
        }
        else if (ResponseMessages.MessageIds.eeProtocolVersionData.ordinal() == lMsgId)
        {
            System.out.println("Protocol Version message received.");
            
            tProtocolVersionDataMsg lMsg = new tProtocolVersionDataMsg(mReadBuffer);
            
            System.out.print("Version: ");
            System.out.print(lMsg.mVersionMajor);
            System.out.print(".");
            System.out.println(lMsg.mVersionMinor);
            
            NotifySubscribers(lMsg);
        }
        else if (ResponseMessages.MessageIds.eeBladeConfigData.ordinal() == lMsgId)
        {
            System.out.println("Blade config data message received.");
            
            tBladeConfigDataMsg lMsg = new tBladeConfigDataMsg(mReadBuffer);
            
            NotifySubscribers(lMsg);
        }
        else if (ResponseMessages.MessageIds.eeSoundConfigData.ordinal() == lMsgId)
        {
            System.out.println("Sound config data message received.");
            
            tSoundConfigDataMsg lMsg = new tSoundConfigDataMsg(mReadBuffer);
            
            NotifySubscribers(lMsg);
        }
        else if (ResponseMessages.MessageIds.eeMotionConfigData.ordinal() == lMsgId)
        {
            System.out.println("Motion config data message received.");
            
            tMotionConfigDataMsg lMsg = new tMotionConfigDataMsg(mReadBuffer);
            
            NotifySubscribers(lMsg);
        }
        else if (ResponseMessages.MessageIds.eeColorPresetsConfigData.ordinal() == lMsgId)
        {
            System.out.println("Color presets data message received");
            
            tColorPresetsDataMsg lMsg = new tColorPresetsDataMsg(mReadBuffer);
            
            NotifySubscribers(lMsg);
        }
        else if (ResponseMessages.MessageIds.eeSaberInfoData.ordinal() == lMsgId)
        {
            System.out.println("Saber info data message received.");
            
            tSaberInfoDataMsg lMsg = new tSaberInfoDataMsg(mReadBuffer);
            
            NotifySubscribers(lMsg);
        }
        else if (ResponseMessages.MessageIds.eeSaberOptionsData.ordinal() == lMsgId)
        {
            System.out.println("Saber options data message received.");
            
            tSaberOptionsDataMsg lMsg = new tSaberOptionsDataMsg(mReadBuffer);
            
            NotifySubscribers(lMsg);
        }
        else //Just dump text
        {
            System.out.print("Bogus data:");
            while(!mReadBuffer.isEmpty())
            {
                System.out.print(mReadBuffer.remove());
            }
            System.out.println("");
        }

        
    
    }
    
    protected SerialPort mPort;
    protected boolean mShutdown;
    protected Queue<Byte> mReadBuffer;
    protected ArrayList<ResponseMessageSubscriber> mSubscribers;
}
