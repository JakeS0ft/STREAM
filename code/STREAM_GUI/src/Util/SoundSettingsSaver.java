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

import Settings.tSoundParameters;
import static Util.Converter.BLASTERFLASHTIME_STR;
import static Util.Converter.BLASTERSUPPRESSTIME_STR;
import static Util.Converter.CLASHSWINGSUPPRESSTIME_STR;
import static Util.Converter.HUMRELAUNCHINTERVAL_STR;
import static Util.Converter.MAXSWINGINTERVAL_STR;
import static Util.Converter.MINSWINGINTERVAL_STR;
import static Util.Converter.POWEROFFTIME_STR;
import static Util.Converter.POWERONBLADEDELAY_STR;
import static Util.Converter.POWERONTIME_STR;
import java.io.PrintWriter;

/**
 *
 * @author Jake
 */
public class SoundSettingsSaver 
{

    public SoundSettingsSaver(String aFileOut, Settings.tSoundParameters aSettings)
    {
        mFileOutStr = aFileOut;
        mParams = aSettings;
    }
    
    public boolean Save()
    {
        boolean lbSuccess = false;
        
        String lcFileData = SoundSettingsToString(mParams);
        
        PrintWriter lcWriter = null;
        
        try
        {
            lcWriter = new PrintWriter(mFileOutStr);
            lcWriter.print(lcFileData);
            lbSuccess = true;
        }
        catch(java.io.FileNotFoundException lex)
        {
            lex.printStackTrace();
        }
        finally
        {
            if(null != lcWriter)
            {
                lcWriter.close();
            }
        }
        
        return lbSuccess;
    }
    
    public static String SoundSettingsToString(tSoundParameters aParams)
    {
        Short lPowerOnBladeDelay = aParams.mPowerOnBladeDelay;
        Short lPowerOnTime = aParams.mPowerOnTime;
        Short lPowerOffTime = aParams.mPowerOffTime;
        Short lBlasterFlashTime = aParams.mBlasterFlashTime;
        Short lBlasterSuppressTime = aParams.mBlasterSuppressTime;
        Short lClashSwingSuppressTime = aParams.mClashSwingSuppressTime;
        Short lMinSwingInterval = aParams.mMinSwingInterval;
        Short lMaxSwingInterval = aParams.mMaxSwingInterval;
        //Handle this with speical processing so we can treat it like an unsigned int_16
        //We don't want stupid negitve numbers written to the file.
        Integer lHumRelaunchInterval = Converter.short2int(aParams.mHumRelaunchInterval);
        
        final String lPrefixStr = "SoundParameters";
        
        String lReturnStr = "[Sound Timings]" + System.getProperty("line.separator");
        lReturnStr += BuildSettingString(lPrefixStr, BLASTERFLASHTIME_STR, lBlasterFlashTime.toString());
        lReturnStr += BuildSettingString(lPrefixStr, BLASTERSUPPRESSTIME_STR, lBlasterSuppressTime.toString());
        lReturnStr += BuildSettingString(lPrefixStr, CLASHSWINGSUPPRESSTIME_STR, lClashSwingSuppressTime.toString());
        lReturnStr += BuildSettingString(lPrefixStr, HUMRELAUNCHINTERVAL_STR, lHumRelaunchInterval.toString());
        lReturnStr += BuildSettingString(lPrefixStr, MAXSWINGINTERVAL_STR, lMaxSwingInterval.toString());
        lReturnStr += BuildSettingString(lPrefixStr, MINSWINGINTERVAL_STR, lMinSwingInterval.toString());
        lReturnStr += BuildSettingString(lPrefixStr, POWERONBLADEDELAY_STR, lPowerOnBladeDelay.toString());
        lReturnStr += BuildSettingString(lPrefixStr, POWERONTIME_STR, lPowerOnTime.toString());
        lReturnStr += BuildSettingString(lPrefixStr, POWEROFFTIME_STR, lPowerOffTime.toString());
        
        return lReturnStr;
    }
    
    /**
     * @brief Builds a single line setting suitable for file save operations.
     * @param aPrefix - Prefix (example, "MySettingType")
     * @param aName - Setting name (example, "MySpecificType")
     * @param aValue - Value of setting (example, 3)
     * @return String of the form aPrefix.aName=aValue
     */
    public static String BuildSettingString(String aPrefix, String aName, String aValue)
    {
        String aOutStr = aPrefix;
        aOutStr += ".";
        aOutStr += aName;
        aOutStr += "=";
        aOutStr += aValue;
        aOutStr += ";";
        aOutStr += System.getProperty("line.separator");
        
        return aOutStr;
    }
    
    protected String mFileOutStr;
    protected Settings.tSoundParameters mParams;
}
