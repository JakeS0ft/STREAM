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
package GUI;

import java.io.IOException;

/**
 *
 * @author Jake
 */
public class FirmwareUploader
{
public FirmwareUploader(String aComPort, String aHexFile)
{
    mComPortStr = aComPort;
    mHexFileStr = aHexFile;
    mBaudRate = Integer.parseInt("57600");
    mLogFileNameStr = null;
}

public void SetBaudRate(Integer aBaudRate)
{
    mBaudRate = aBaudRate;
}

public void SetLogFileName(String aLogFileName)
{
    mLogFileNameStr = aLogFileName;
 }

public boolean Upload()
{
    boolean lbSuccess = true;
    //Base command
    String lcCmd = "avrdude -patmega328p -carduino";
    //Add the selected com port
    lcCmd += " -P" + mComPortStr;
    //Add the baud rate
    //lcCmd += " -b57600";
    lcCmd += " -b";
    lcCmd += mBaudRate.toString();

    //Add rest of the command before the hex file
    lcCmd += " -D -Uflash:w:";
    
//    if(!mSlowBaudRate)
//    {
//        lcCmd = "avrdude -patmega328p -carduino -PCOM3 -b57600 -D -Uflash:w:";
//    }
//    else
//    {
//        lcCmd = "avrdude -patmega328p -carduino -PCOM3 -D -Uflash:w:";
//    }
    //Add hex file
    lcCmd += mHexFileStr;
    lcCmd += ":i";
    
    if(null != mLogFileNameStr)
    {
        lcCmd += " > ";
        lcCmd += mLogFileNameStr;
    }
    
    System.out.println("Exec: '" + lcCmd + "'");
    java.lang.Runtime rt = java.lang.Runtime.getRuntime();
    try
    {
        rt.exec(lcCmd);
    }
    catch (IOException lEx)
    {
        System.out.println(lEx.getMessage());
        lbSuccess = false;
    }
    catch (Exception lOtherEx)
    {
        lbSuccess = false;
    }
    
    return lbSuccess;
}

protected String mComPortStr;
protected String mHexFileStr;
protected Integer mBaudRate;
protected String mLogFileNameStr;
        
}
