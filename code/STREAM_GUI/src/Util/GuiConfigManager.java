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

import java.io.File;

/**
 *
 * @author Jake
 */
public class GuiConfigManager {
    private GuiConfigManager()
    {
        mGuiConfig = new tGuiConfig();
    }
    
    public static GuiConfigManager GetInstance()
    {
        //Create the singelton instance on first call
        if(null == mInstance)
        {
            mInstance = new GuiConfigManager();
        }
        
        return mInstance;
    }
    
    public void LoadGuiConfig(String aConfigFilePath)
    {
        System.out.println("");
        File lCfgFile = new File(aConfigFilePath);
        if(lCfgFile.exists())
        {
            try
            {
                ConfigLoader lCfgLoader = new ConfigLoader(aConfigFilePath);
                lCfgLoader.Load();
                lCfgLoader.FillInGuiConfig(mGuiConfig);
            }
            catch (Exception ex)
            {
                System.err.println("Failed to load GUI config.");
                ex.printStackTrace();
            }
        }
        else
        {
            System.out.println("Using GUI config defaults.");
        }
       
    }
    
    public tGuiConfig GetGuiConfig()
    {
        return mGuiConfig;
    }
    
    private tGuiConfig mGuiConfig;
    private static GuiConfigManager mInstance; //Singelton instance
}
