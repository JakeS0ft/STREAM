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
package Settings;

/**
 *
 * @author Jake
 */
public class tBladeParameters
{
    public tBladeParameters()
    {
    }
    
    /**
     * Fetch blade parameters as human-readable string.
     * @return String with human-readable parameters.
     */
    public String GetText()
    {
        Integer lMainColorIndex = Util.Converter.byte2int(mMainColorIndex);
        Integer lFlashColorIndex = Util.Converter.byte2int(mFlashColorIndex);
        Integer lFlickerIndex = Util.Converter.byte2int(mFlickerIndex);
        Integer lLockupColorIntensity = Util.Converter.byte2int(mLockupColorIntensity);
        Integer lLockupFlickerIntensity = Util.Converter.byte2int(mLockupFlickerIntensity);
        Short lLockupFramePeriod = mLockupFramePeriod;
        
        String lcPrefixStr = "BladeParameters.";
        
        //TODO: Add name strings
        String lcReturnStr = lcPrefixStr + lMainColorIndex.toString() + "\n";
        lcReturnStr += lcPrefixStr + lFlashColorIndex.toString() + "\n";
        lcReturnStr += lcPrefixStr + lFlickerIndex.toString() + "\n";
        lcReturnStr += lcPrefixStr + lLockupColorIntensity.toString() + "\n";
        lcReturnStr += lcPrefixStr + lLockupFlickerIntensity.toString() + "\n";
        lcReturnStr += lcPrefixStr + lLockupFramePeriod.toString() + "\n";
        
        return lcReturnStr;
    }
    
    //Selected main blade color
    public byte mMainColorIndex;
    //Selected clash/blaster blade color
    public byte mFlashColorIndex;
    //Selected blade effect
    public byte mFlickerIndex;
    //Lockup color effect intensity
    public byte mLockupColorIntensity;
    //Lockup flicker Intensity
    public byte mLockupFlickerIntensity;
    //Lockup animation speed
    public short mLockupFramePeriod;
}
