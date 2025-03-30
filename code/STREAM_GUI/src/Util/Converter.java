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
package Util;
import java.util.ArrayList;
import Settings.tSoundParameters;
/**
 *
 * @author Jake
 */
public class Converter
{
    public static int byte2int(byte aByte)
    {
        int lValue = (int)aByte;

        if(lValue < 0)
        {
            lValue = lValue & 0xFF;
        }
        
        return lValue;
    }
    
    public static short bytes2short(byte aLowByte, byte aHighByte)
    {
        int lLowByteInt = byte2int(aLowByte);
        int lHighByteInt = byte2int(aHighByte);
        
        int lValueInt = lLowByteInt + (lHighByteInt << 8);
        
        short lValueShort = (short)lValueInt;
        
        return lValueShort;
    }
    
    public static ArrayList<Byte> short2bytes(short aValue)
    {
        ArrayList<Byte> lBytes = new ArrayList();
        
        Integer lLowByte = ((int)aValue) & 0xFF;
        Integer lHighByte =((int) aValue) >>> 8;
        
        lBytes.add(lLowByte.byteValue());
        lBytes.add(lHighByte.byteValue());
        
        return lBytes;
    }
    
    public static int short2int(short aValue)
    {
        ArrayList<Byte> lShortBytes = short2bytes(aValue);
        
        int lLowByte = (int)lShortBytes.get(0);
        int lHighByte = (int)lShortBytes.get(1);
        
        //Chop off the sign bits
        lLowByte = lLowByte & 0xFF;
        lHighByte = lHighByte & 0xFF;
        
        int lIntValue = lLowByte + (lHighByte << 8);
                
        return lIntValue;
    }
    
    //Sound timings string constants
    public static final String POWERONBLADEDELAY_STR = "PowerOnBladeDelay";
    public static final String POWERONTIME_STR = "PowerOnTime";
    public static final String POWEROFFTIME_STR = "PowerOffTime";
    public static final String BLASTERFLASHTIME_STR = "BlasterFlashTime";
    public static final String BLASTERSUPPRESSTIME_STR = "BlasterSuppressTime";
    public static final String CLASHSWINGSUPPRESSTIME_STR = "ClashSwingSuppressTime";
    public static final String MAXSWINGINTERVAL_STR = "MaxSwingInterval";
    public static final String MINSWINGINTERVAL_STR = "MinSwingInterval";
    public static final String HUMRELAUNCHINTERVAL_STR = "HumRelaunchInterval";
    
    //Config string constants
    public static final String CHANNEL1_LIMMIT_STR = "Channel1_Limmit";
    public static final String CHANNEL2_LIMMIT_STR = "Channel2_Limmit";
    public static final String CHANNEL3_LIMMIT_STR = "Channel3_Limmit";
    
}
