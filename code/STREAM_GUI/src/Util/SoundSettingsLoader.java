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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Jake
 */
public class SoundSettingsLoader {
    
    public SoundSettingsLoader(String acFile)
    {
        mFileInStr = acFile;
        mParams = new Settings.tSoundParameters();
        mFileLines = new ArrayList();
        mDefaultsUsed  = new ArrayList();
        mDefaultsWereUsed = false;
    }
    
    public synchronized boolean Load()
    {
        mFileLines.clear();
 //       mDefaultsWereUsed = false;
 //       mDefaultsUsed.clear();
        
        boolean lbSuccess = false;
        
        //reading file line by line in Java using BufferedReader      
        FileInputStream lFileInputStream = null;
        BufferedReader lFileReader = null;
     
        try
        {
            lFileInputStream = new FileInputStream(mFileInStr);
            lFileReader = new BufferedReader(new InputStreamReader(lFileInputStream));
         
            System.out.println("Reading File line by line using BufferedReader");
         
            String lReadLine = lFileReader.readLine();
            while(lReadLine != null){
                System.out.println(lReadLine);
                lReadLine = lFileReader.readLine();
                mFileLines.add(lReadLine);
            }
         
            lbSuccess = true;
        }
        catch (FileNotFoundException ex)
        {
             ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        } finally
        {
            try
            {
                lFileReader.close();
                lFileInputStream.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        } 
        
        return lbSuccess;
    }
    
    public synchronized void FillInParameters(Settings.tSoundParameters aParamsOut)
    {
        //Fetch settings as strings
        String lcPowerOnBladeDelayStr = FindSetting(Converter.POWERONBLADEDELAY_STR, "0");
        String lcPowerOnTimeStr = FindSetting(Converter.POWERONTIME_STR, "1000");
        String lcPowerOffTimeStr = FindSetting(Converter.POWEROFFTIME_STR, "1000");
        String lcBlasterFlashTimeStr = FindSetting(Converter.BLASTERFLASHTIME_STR, "100");
        String lcBlasterSuppressTimeStr = FindSetting(Converter.BLASTERSUPPRESSTIME_STR, "1000");
        String lcClashSwingSuppressTimeStr = FindSetting(Converter.CLASHSWINGSUPPRESSTIME_STR, "1000");
        String lcMinSwingIntervalStr = FindSetting(Converter.MINSWINGINTERVAL_STR, "100");
        String lcMaxSwingIntervalStr = FindSetting(Converter.MAXSWINGINTERVAL_STR, "1500");
        String lcHumRelaunchIntervalStr = FindSetting(Converter.HUMRELAUNCHINTERVAL_STR, "10000");
        
        //Parse strings into numeric values
        Short lPowerOnBladeDelay = Short.parseShort(lcPowerOnBladeDelayStr);
        Short lPowerOnTime = Short.parseShort(lcPowerOnTimeStr);
        Short lPowerOffTime = Short.parseShort(lcPowerOffTimeStr);
        Short lBlasterFlashTime = Short.parseShort(lcBlasterFlashTimeStr);
        Short lBlasterSuppressTime = Short.parseShort(lcBlasterSuppressTimeStr);
        Short lClashSwingSuppressTime = Short.parseShort(lcClashSwingSuppressTimeStr);
        Short lMinSwingInterval = Short.parseShort(lcMinSwingIntervalStr);
        Short lMaxSwingInterval = Short.parseShort(lcMaxSwingIntervalStr);
        Integer lHumRelaunchInterval = Integer.parseInt(lcHumRelaunchIntervalStr);
        
        //Copy parsed values into data structure
        aParamsOut.mPowerOnBladeDelay = lPowerOnBladeDelay;
        aParamsOut.mPowerOnTime = lPowerOnTime;
        aParamsOut.mPowerOffTime = lPowerOffTime;
        aParamsOut.mBlasterFlashTime = lBlasterFlashTime;
        aParamsOut.mBlasterSuppressTime = lBlasterSuppressTime;
        aParamsOut.mClashSwingSuppressTime = lClashSwingSuppressTime;
        aParamsOut.mMinSwingInterval = lMinSwingInterval;
        aParamsOut.mMaxSwingInterval = lMaxSwingInterval;
        aParamsOut.mHumRelaunchInterval = lHumRelaunchInterval.shortValue();
    }
    
    /**
     * Fetch a list of tokens for which a value could not be parsed and
     * a default value was used instead.
     * @return List of settings with default values
     */
    public synchronized ArrayList<String> GetDefaultsUsed()
    {
        return mDefaultsUsed;
    }
    
    public synchronized void GetDefaultsUsed(ArrayList<String> aDefaultsOut)
    {
        for(String lcDefUsed : this.mDefaultsUsed)
        {
            aDefaultsOut.add(lcDefUsed);
        }
    }
    
    /**
     * Search for a setting by token
     * @param aSettingTokenStr - Unique identifying token
     * @param aDefault - Default to use if token can not be found
     * @return Found setting as a string
     */
    public String FindSetting(String aSettingTokenStr, String aDefault)
    {
        String lcFound = null;
        String lcSettingStr = null;
        boolean lbFound = false;
        
        for(String lcLine : mFileLines)
        {
//            System.out.print("Tryng to match ");
//            System.out.print(aSettingTokenStr);
//            System.out.print(":");
//            System.out.println(lcLine);
            
            if(null != lcLine && lcLine.contains(aSettingTokenStr))
            {
                lcFound = lcLine;
                lbFound = true;
            }
        }
        
        if(lbFound)
        {
            int lnStartIdx = lcFound.indexOf("=") + 1;
            int lnEndIdx = lcFound.indexOf(";");
            lcSettingStr = lcFound.substring(lnStartIdx, lnEndIdx);
        }
        else
        {
            System.out.println("No match for " + aSettingTokenStr);
            lcSettingStr = aDefault;
            mDefaultsUsed.add(aSettingTokenStr);
            
            for(String lcDefaultUsed : mDefaultsUsed)
            {
                System.out.println("Defaulted fields: " + lcDefaultUsed);
            }
            mDefaultsWereUsed = true;
        }
        
//        System.out.print("Numerical Setting:");
//        System.out.println(lcSettingStr);
        
        return lcSettingStr;
    }
    
    public synchronized boolean DefaultsWereUsed()
    {
        return mDefaultsWereUsed;
    }
    
    protected Settings.tSoundParameters mParams;
    protected String mFileInStr;
    protected ArrayList<String> mFileLines;
    protected ArrayList<String> mDefaultsUsed;
    protected boolean mDefaultsWereUsed;
}
