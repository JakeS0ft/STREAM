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
import Settings.tBladeColor;

/**
 *
 * @author Jake
 */
public class tPowerUpPowerDownTestMsg extends CmdMsgBase
{
    public tPowerUpPowerDownTestMsg()
    {
        mMsgId = (byte)MessageIds.eeCmdPowerUpPowerDownTest.ordinal();
    }
    
    @Override
    public ArrayList<Byte> GetBytes()
    {   
        ArrayList<Byte> lBytes = new ArrayList();
        
        lBytes.add(mMsgId); //1
        //Blade ramp delay
        lBytes.add((byte)(mPowerOnDelay & 0xF)); //Low Byte
        lBytes.add((byte)(mPowerOnDelay >> 8)); //High byte
        //Power Up Time
        lBytes.add((byte)(mPowerupTime & 0xF)); //Low byte
        lBytes.add((byte)(mPowerupTime >> 8)); //High byte
        //Power Down Time
        lBytes.add((byte)(mPowerdownTime & 0xF)); //Low byte
        lBytes.add((byte)(mPowerdownTime >> 8)); //High byte        
        //Blade flicker type
        lBytes.add(mFlickerType);
        //On duration
        lBytes.add(mOnDuration);
        
        lBytes.add(mBladeColor.mChannel1);
        lBytes.add(mBladeColor.mChannel2);
        lBytes.add(mBladeColor.mChannel3);
        
        return lBytes;
    }
    public short mPowerOnDelay; //Blade ramp delay
    public short mPowerupTime; //Blade ramp up time (milliseconds)
    public short mPowerdownTime; //Blade ramp down time (millseconds)
    public byte mFlickerType; //Blade effect to apply while on
    public byte mOnDuration; //How long to stay on (seconds)
    public Settings.tBladeColor mBladeColor = new Settings.tBladeColor();
}
