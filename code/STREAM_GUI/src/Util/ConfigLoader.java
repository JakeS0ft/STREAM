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
public class ConfigLoader {
    public ConfigLoader(String aConfigFile)
    {
        mConfigFilePath = aConfigFile;
        mGuiConfig = new tGuiConfig();
        mFileLines = new ArrayList();
    }
    
   public synchronized boolean Load()
    {
        mFileLines.clear();       
        boolean lbSuccess = false;
        
        //reading file line by line in Java using BufferedReader      
        FileInputStream lFileInputStream = null;
        BufferedReader lFileReader = null;
     
        try
        {
            lFileInputStream = new FileInputStream(mConfigFilePath);
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
        }
        
        return lcSettingStr;
    }
    
    public void FillInGuiConfig(tGuiConfig aGuiConfig)
    {
        String laLimmitsStr[] = new String[3];     

        laLimmitsStr[0] = FindSetting(Converter.CHANNEL1_LIMMIT_STR, "255");
        laLimmitsStr[1] = FindSetting(Converter.CHANNEL2_LIMMIT_STR, "255");
        laLimmitsStr[2] = FindSetting(Converter.CHANNEL3_LIMMIT_STR, "255");
        
        try
        {
            aGuiConfig.mChannel1Limmit = Integer.parseInt(laLimmitsStr[0]);
            aGuiConfig.mChannel2Limmit = Integer.parseInt(laLimmitsStr[1]);
            aGuiConfig.mChannel3Limmit = Integer.parseInt(laLimmitsStr[2]);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    
    protected String mConfigFilePath;
    protected ArrayList<String> mFileLines;
    tGuiConfig mGuiConfig;
}
