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

import Util.ConfigLoader;
import Util.Converter;
import Util.GuiConfigManager;
import Util.SoundSettingsSaver;
import Util.tGuiConfig;

/**
 *
 * @author Jake
 */
public class StreamGuiMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //TestFirmwareUploader();
        //TestConverter();
        //TestSoundSettingsTextConverter();
        //TestShort2Uint();
        //TestSoundSettingsSaver();
        //TestConfigLoader();
        //TestGuiConfigManager();
        
        //Normal operation (non-test)
        Operate();
    }
    
    /**
     * @brief Normal operation. (Non-test)
     */
    public static void Operate()
    {
        if(!LicenseManager.LicenseFileExists())
        {
            ShowEula();
        }
        StartNormally();
    }
    
    public static void ShowEula()
    {
        mEulaScreen = new EulaScreen();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               mEulaScreen.setVisible(true);
            }
        });
        
        //Wait for user to accept the license or decline
        while(false == LicenseManager.GetAgreed())
        {
            try
            {
                Thread.sleep(500);
            }
            catch(InterruptedException lEx)
            {
                //Who cares?
            }
        }
    }
    
    public static void StartNormally()
    {
        mEulaScreen = null;
        
        //Load the GUI config
        GuiConfigManager.GetInstance().LoadGuiConfig("GuiConfig.txt");
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConnectScreen().setVisible(true);
            }
        });
    }
    
    public static void LoadGuiConfig()
    {
        
    }
    
    public static void TestFirmwareUploader()
    {
        FirmwareUploader lcInstaller = new FirmwareUploader("COM3", "D:\\sloeber\\workspace\\Lightsaber\\StarDrive\\Release\\StarDrive.hex");
        
        boolean lbSuccess = lcInstaller.Upload();
        
        if(lbSuccess)
        {
            System.out.println("Success.");
        }
        else
        {
            System.out.println("Failed.");
        }
    }
    
    public static void TestConverter()
    {
        for(int highByte = 0; highByte < 256; highByte++)
        {
            for (int lowByte = 0; lowByte < 256; lowByte++)
            {
                short lShort = Converter.bytes2short((byte) lowByte, (byte) highByte);

                System.out.print("Short Conversion:");
                System.out.print((int) highByte);
                System.out.print(",");
                System.out.print((int) lowByte);
                System.out.print(":");

                System.out.println(lShort);
            }
        }
    }
    
    public static void TestSoundSettingsTextConverter()
    {
        Settings.tSoundParameters lParameters = new Settings.tSoundParameters();
        
        //Test each parameter is set correctly
        lParameters.mBlasterFlashTime = 1;
        lParameters.mBlasterSuppressTime = 2;
        lParameters.mClashSwingSuppressTime = 3;
        lParameters.mHumRelaunchInterval = 4;
        lParameters.mMaxSwingInterval = 5;
        lParameters.mMinSwingInterval = 6;
        lParameters.mPowerOnBladeDelay = 7;
        lParameters.mPowerOnTime = 8;
        lParameters.mPowerOffTime = 9;
        
        String lcSoundParamStr = SoundSettingsSaver.SoundSettingsToString(lParameters);
        System.out.println(lcSoundParamStr);
        
        //Test upper bound of each parameter
        lParameters.mBlasterFlashTime = 32767;
        lParameters.mBlasterSuppressTime = 32767;
        lParameters.mClashSwingSuppressTime = 32767;
        lParameters.mHumRelaunchInterval = 32767;
        lParameters.mMaxSwingInterval = 32767;
        lParameters.mMinSwingInterval = 32767;
        lParameters.mPowerOnBladeDelay = 32767;
        lParameters.mPowerOnTime = 32767;
        lParameters.mPowerOffTime = 32767;
        
        lcSoundParamStr = SoundSettingsSaver.SoundSettingsToString(lParameters);
        System.out.println(lcSoundParamStr);
        
    }
    
    public static void DumpShortIntCompare(short aShortVal, int aIntVal)
    {
        System.out.print("ShortValue: ");
        System.out.print(aShortVal);
        System.out.print("-->");
        System.out.print(aIntVal);
        System.out.print("\n");
    }
    
    public static void TestShort2Uint()
    {
        short lShortValue = 0;
        int lIntValue = Converter.short2int(lShortValue);
        DumpShortIntCompare(lShortValue, lIntValue);
    
        lShortValue = 255;
        lIntValue = Converter.short2int(lShortValue);
        DumpShortIntCompare(lShortValue, lIntValue);
        
        lShortValue = 256;
        lIntValue = Converter.short2int(lShortValue);
        DumpShortIntCompare(lShortValue, lIntValue);
        
        lShortValue = 32767;
        lIntValue = Converter.short2int(lShortValue);
        DumpShortIntCompare(lShortValue, lIntValue);
        

        lShortValue = new Integer(32768).shortValue();
        lIntValue = Converter.short2int(lShortValue);
        DumpShortIntCompare(lShortValue, lIntValue);
    }
    
    public static void DumpSoundSettings(Settings.tSoundParameters aParams)
    {
        String lcOutput = "mBlasterFlashTime=";
        lcOutput += aParams.mBlasterFlashTime;
        lcOutput += "\n";

        lcOutput += "mBlasterSuppressTime=";
        lcOutput += aParams.mBlasterSuppressTime;
        lcOutput += "\n";
        
        lcOutput += "mClashSwingSuppressTime=";
        lcOutput +=aParams.mClashSwingSuppressTime;
        lcOutput += "\n";
        
        lcOutput += "mHumRelaunchInterval=";
        lcOutput +=aParams.mHumRelaunchInterval;
        lcOutput += "\n";
        
        lcOutput += "mMaxSwingInterval=";
        lcOutput +=aParams.mMaxSwingInterval;
        lcOutput += "\n";
        
        lcOutput += "mMinSwingInterval=";
        lcOutput +=aParams.mMinSwingInterval;
        lcOutput += "\n";
        
        lcOutput += "mPowerOnBladeDelay=";
        lcOutput +=aParams.mPowerOnBladeDelay;
        lcOutput += "\n";
        
        lcOutput += "mPowerOnTime=";
        lcOutput +=aParams.mPowerOnTime;
        lcOutput += "\n";
        
        lcOutput += "mPowerOffTime=";
        lcOutput +=aParams.mPowerOffTime;
        lcOutput += "\n";
        
        System.out.print(lcOutput);
    }
    
    public static void TestSoundSettingsSaver()
    {
        Settings.tSoundParameters lParameters = new Settings.tSoundParameters();
        
        Integer lHumRelaunchInterval = 65535;
        
        lParameters.mBlasterFlashTime = 1;
        lParameters.mBlasterSuppressTime = 2;
        lParameters.mClashSwingSuppressTime = 3;
        lParameters.mHumRelaunchInterval = lHumRelaunchInterval.shortValue();
        lParameters.mMaxSwingInterval = 5;
        lParameters.mMinSwingInterval = 6;
        lParameters.mPowerOnBladeDelay = 7;
        lParameters.mPowerOnTime = 8;
        lParameters.mPowerOffTime = 9;
        
        Util.SoundSettingsSaver lSaver = new Util.SoundSettingsSaver("SoundTest.txt", lParameters);
        
        if(lSaver.Save())
        {
            System.out.println("Save success.");
        }
        else
        {
            System.out.println("Save failed.");
        }
        
        Util.SoundSettingsLoader lLoader = new Util.SoundSettingsLoader("SoundTest.txt");
        
        if(lLoader.Load())
        {
            System.out.println("Load success.");
        }
        else
        {
            System.out.println("Load failed.");
        }
        
        Settings.tSoundParameters lLoadedParameters = new Settings.tSoundParameters();
        lLoader.FillInParameters(lLoadedParameters);
        
        DumpSoundSettings(lLoadedParameters);
    }
    
    public static void TestConfigLoader()
    {
        ConfigLoader lCfgLoader = new ConfigLoader("GuiConfig.txt");
        lCfgLoader.Load();
        
        tGuiConfig lConfig = new tGuiConfig();
        
        lCfgLoader.FillInGuiConfig(lConfig);
        
        System.out.println("GuiConfig:");
        System.out.println("ChannelLimits:" + lConfig.mChannel1Limmit + "/" + lConfig.mChannel2Limmit + "/" + lConfig.mChannel3Limmit);
    }
    
    public static void TestGuiConfigManager()
    {
        GuiConfigManager.GetInstance().LoadGuiConfig("GuiConfig.txt");
        tGuiConfig lConfig = GuiConfigManager.GetInstance().GetGuiConfig();
        
        System.out.println("GuiConfig from GuiConfigManager:");
        System.out.println("ChannelLimits:" + lConfig.mChannel1Limmit + "/" + lConfig.mChannel2Limmit + "/" + lConfig.mChannel3Limmit);
    }
    static EulaScreen mEulaScreen;
}
