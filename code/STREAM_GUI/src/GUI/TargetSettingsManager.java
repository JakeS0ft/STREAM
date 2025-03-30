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

import CommandMessages.CommandMessageSender;
import ResponseMessages.ResponseMessageBase;
import ResponseMessages.ResponseMessageReceiver;
import ResponseMessages.ResponseMessageSubscriber;
import java.util.ArrayList;

/**
 * @brief This target manages loading and sending EEPROM settings images
 * from the target.
 * @author Jake
 */
public class TargetSettingsManager implements ResponseMessageSubscriber {
   
    public TargetSettingsManager(CommandMessageSender aMessageSender,
                                 ResponseMessageReceiver aMessageRcvr)
    {
        mMessageSndr = aMessageSender;
        mMessageRcvr = aMessageRcvr;
        
        mFirmwareVersion = null;
        mProtocolVersion = null;
    }
    
    public void Init()
    {
        mMessageRcvr.Subscribe(this);
    }
    
    public void SendSettingsToTarget()
    {
        CommandMessages.tSetBladeConfigMsg lBladeConfigMsg =
                new CommandMessages.tSetBladeConfigMsg();
        CommandMessages.tSetMotionConfigMsg lMotionConfigMsg =
                new CommandMessages.tSetMotionConfigMsg();
        CommandMessages.tSetSoundConfigMsg lSoundConfigMsg =
                new CommandMessages.tSetSoundConfigMsg();
        CommandMessages.tSetColorPresetsMsg lColorPresetsMsg =
                new CommandMessages.tSetColorPresetsMsg();
        CommandMessages.tSetOptionsConfigMsg lOptionsConfigMsg =
                new CommandMessages.tSetOptionsConfigMsg();
        
        lBladeConfigMsg.mParameters = mBladeConfig;
        lMotionConfigMsg.mParameters = mMotionConfig;
        lSoundConfigMsg.mParameters = mSoundConfig;
        lColorPresetsMsg.maColorPresets = maBladeColors;
        lOptionsConfigMsg.mOptions = mOptions;
        
        System.out.println("Sending Motion settings...");
        mMessageSndr.SendMessage(lMotionConfigMsg);
        Sleep(1000);
        
        System.out.println("Sending Sound settings...");
        mMessageSndr.SendMessage(lSoundConfigMsg);
        Sleep(1000);
        
        System.out.println("Sending blade settings...");
        mMessageSndr.SendMessage(lBladeConfigMsg);
        Sleep(500);
        
        System.out.println("Sending color presets...");
        mMessageSndr.SendMessage(lColorPresetsMsg);
        Sleep(500);
        
        System.out.println("Sending options...");
        mMessageSndr.SendMessage(lOptionsConfigMsg);
        Sleep(500);
        
        //Send command to write settings to EEPROM on the saber
        Integer lSaveMsgId = CommandMessages.MessageIds.eeCmdSaveSettngs.ordinal();
        mMessageSndr.SendOneByteMessage(lSaveMsgId.byteValue());
        System.out.println("Settings upload complete.");
    }
    
    public void FetchProtocolVersionFromTarget()
    {
        Integer lMsgId = CommandMessages.MessageIds.eeCmdRequestProtocolVersion.ordinal();
        mMessageSndr.SendOneByteMessage(lMsgId.byteValue());
        
        for(int lTries = 0;
            lTries < 10 && null == mProtocolVersion;
            lTries++)
        {
            Sleep(500);
            if(null == mProtocolVersion)
            {
                System.out.println("Waiting for protocol version data...");
            }
        }
    }
    
    public void FetchFirmwareVersionFromTarget()
    {
        Integer lMsgId = CommandMessages.MessageIds.eeCmdRequestFirmwareVersion.ordinal();
        mMessageSndr.SendOneByteMessage(lMsgId.byteValue());
        
        for(int lTries = 0;
            lTries < 10 && null == mFirmwareVersion;
            lTries++)
        {
            Sleep(500);
            if(null == mFirmwareVersion)
            {
                System.out.println("Waiting for firmware version data...");
            }
        }
    }
        
    public void FetchSettingsFromTarget()
    {
        //Blank out any stored settings
        mBladeConfig = null;
        mMotionConfig = null;
        mSoundConfig = null;
        maBladeColors = null;
        mSaberInfo = null;
        
        Sleep(100);
        //Request Blade config
        Integer lMsgId = CommandMessages.MessageIds.eeCmdRequestBladeConfig.ordinal();
        mMessageSndr.SendOneByteMessage(lMsgId.byteValue());
        
        //Request Motion config
        lMsgId = CommandMessages.MessageIds.eeCmdRequestMotionConfig.ordinal();
        mMessageSndr.SendOneByteMessage(lMsgId.byteValue());
        
        //Request Sound config
        lMsgId = CommandMessages.MessageIds.eeCmdRequestSoundConfig.ordinal();
        mMessageSndr.SendOneByteMessage(lMsgId.byteValue());
        
        //Request blade color config
        lMsgId = CommandMessages.MessageIds.eeCmdRequestColorPresets.ordinal();
        mMessageSndr.SendOneByteMessage(lMsgId.byteValue());
        
        //Request saber info
        lMsgId = CommandMessages.MessageIds.eeCmdRequestSaberInfo.ordinal();
        mMessageSndr.SendOneByteMessage(lMsgId.byteValue());
        
        //Request Optoins config
        lMsgId = CommandMessages.MessageIds.eeCmdRequestOptionsConfig.ordinal();
        mMessageSndr.SendOneByteMessage(lMsgId.byteValue());
        
        while(null == mBladeConfig)
        {
            System.out.println("Waiting for blade settings...");
            Sleep(500);
        }
        while(null == mSoundConfig)
        {
            System.out.println("Waiting for sound settings...");
            Sleep(500);
        }
        while(null == mMotionConfig)
        {
            System.out.println("Waiting for motion settings...");
            Sleep(500);
        }
        while(null == maBladeColors)
        {
            System.out.println("Waiting for blade color settings...");
            Sleep(500);
        }
        while(null == this.mSaberInfo)
        {
            System.out.println("Waiting for saber info...");
            Sleep(500);
        }
        while(null == this.mOptions)
        {
            System.out.println("Waiting for saber options...");
            Sleep(500);
        }
        System.out.println("Finshed loading settings from target.");
    }
        
    @Override
    public void NotifyMessage(ResponseMessageBase aMsg)
    {
        System.out.print("Target settings notified of message with MsgId=");
        System.out.println(aMsg.mMsgId);
        
        if (ResponseMessages.MessageIds.eeBladeConfigData.ordinal() == aMsg.mMsgId)
        {
            System.out.println("Loading blade config from target...");
            ResponseMessages.tBladeConfigDataMsg lBladeDataMsg =
                    (ResponseMessages.tBladeConfigDataMsg)aMsg;
            SetBladeConfig(lBladeDataMsg.mParameters);
            
            PrintBladeConfig();
        }
        else if(ResponseMessages.MessageIds.eeMotionConfigData.ordinal() == aMsg.mMsgId)
        {
            System.out.println("Loading motion config from target.");
            ResponseMessages.tMotionConfigDataMsg lMotionConfigMsg =
                    (ResponseMessages.tMotionConfigDataMsg) aMsg;
            SetMotionConfig(lMotionConfigMsg.mParameters);
            
            PrintMotionConfig();
        }
        else if(ResponseMessages.MessageIds.eeSoundConfigData.ordinal() == aMsg.mMsgId)
        {
            System.out.println("Loading sound config from target.");
            ResponseMessages.tSoundConfigDataMsg lSoundConfigMsg =
                    (ResponseMessages.tSoundConfigDataMsg)aMsg;
            SetSoundConfig(lSoundConfigMsg.mParameters);
            
            PrintSoundConfig();
        }
        else if(ResponseMessages.MessageIds.eeColorPresetsConfigData.ordinal() == aMsg.mMsgId)
        {
            System.out.println("Loading color presets from target.");
            ResponseMessages.tColorPresetsDataMsg lColorPresetsMsg =
                    (ResponseMessages.tColorPresetsDataMsg)aMsg;
            
            System.out.print("Message says there are " );
            System.out.print(lColorPresetsMsg.mNumColorPresets);
            System.out.print(" color presets.");
            System.out.print(" Actual presets size is ");
            System.out.print(lColorPresetsMsg.maProfiles.size());
            System.out.println(".");
            
            SetColorPresets(lColorPresetsMsg.maProfiles);
        }
        else if(ResponseMessages.MessageIds.eeSaberInfoData.ordinal() == aMsg.mMsgId)
        {
            System.out.println("Loading saber info from target.");
            ResponseMessages.tSaberInfoDataMsg lSaberInfoMsg =
                    (ResponseMessages.tSaberInfoDataMsg)aMsg;
            
            mSaberInfo = new Settings.tSaberInfo();
            mSaberInfo.mMaxVolume =  lSaberInfoMsg.mMaxVolume;
            mSaberInfo.mNumBladeChannels = lSaberInfoMsg.mNumBladeChannels;
            mSaberInfo.mNumBladeFlickers = lSaberInfoMsg.mNumBladeFlickers;
            mSaberInfo.mNumberOfColorPresets = lSaberInfoMsg.mNumberOfColorPresets;
            mSaberInfo.mNumberOfProfiles = lSaberInfoMsg.mNumberOfProfiles;
            mSaberInfo.mSelectedProfileIndex = lSaberInfoMsg.mSelecedProfileIndex;
            
            System.out.print("MaxVolume            : "); System.out.println(mSaberInfo.mMaxVolume);
            System.out.print("NumBladeChannels     : "); System.out.println(mSaberInfo.mNumBladeChannels);
            System.out.print("NumBladeFlickers     : "); System.out.println(mSaberInfo.mNumBladeFlickers);
            System.out.print("NumberOfColorPresets : "); System.out.println(mSaberInfo.mNumberOfColorPresets);
            System.out.print("NumberOfProfiles     : "); System.out.println(mSaberInfo.mNumberOfProfiles);
            System.out.print("SelectedProfileIndex : "); System.out.println(mSaberInfo.mSelectedProfileIndex);
        }
        else if(ResponseMessages.MessageIds.eeSaberOptionsData.ordinal() == aMsg.mMsgId)
        {
            System.out.println("Loading saber options from target.");
            
            ResponseMessages.tSaberOptionsDataMsg lOptionsMsg =
                    (ResponseMessages.tSaberOptionsDataMsg)aMsg;
            
            //Save the data from the message
            mOptions = lOptionsMsg.mOptions;
            
            System.out.print("AutoPowerOffTimeSec   : "); System.out.println(mOptions.mAutoPowerOffTimeSec);
            System.out.print("ExitLockupOnSwing     : ");System.out.println(mOptions.mExitLockupOnSwing);
            System.out.print("MenuSwitchHoldTime    : ");System.out.println(mOptions.mMenuSwitchHoldTimeMs);
            System.out.print("PowerOffSwitchHoldTime: ");System.out.println(mOptions.mPowerOffSwitchHoldTimeMs);
            System.out.print("SleepTimeMins         : ");System.out.println(mOptions.mSleepTimeMins);
            System.out.print("OneButtonBlasterEnable: ");System.out.println(mOptions.mOneButtonBlasterEnabled);
            System.out.print("OneButtonLockupEnable : ");System.out.println(mOptions.mOneButtonLockupEnabled);
        }
        else if(ResponseMessages.MessageIds.eeProtocolVersionData.ordinal() == aMsg.mMsgId)
        {
            System.out.println("Recieved protocol version from target.");
            
            ResponseMessages.tProtocolVersionDataMsg lProtocolMsg =
                    (ResponseMessages.tProtocolVersionDataMsg)aMsg;
            
            Byte lPmaj = lProtocolMsg.mVersionMajor;
            Byte lPmin = lProtocolMsg.mVersionMinor;
            
            mProtocolVersion = lPmaj.toString();
            mProtocolVersion += ".";
            mProtocolVersion += lPmin.toString();
            
            System.out.println("Protocol Version : " + mProtocolVersion);
        }
        else if(ResponseMessages.MessageIds.eeFirmwareVersionData.ordinal() == aMsg.mMsgId)
        {
            System.out.println("Recieved firmware version from target.");
            
            ResponseMessages.tFirmwareVersionDataMsg lFirmwareMsg =
                    (ResponseMessages.tFirmwareVersionDataMsg)aMsg;
            
            Byte lVmaj = lFirmwareMsg.mVersionMajor;
            Byte lVmin = lFirmwareMsg.mVersionMinor;
            
            mFirmwareVersion = lVmaj.toString();
            mFirmwareVersion += ".";
            mFirmwareVersion += lVmin.toString();
            
            System.out.println("Firmware Version : " + mFirmwareVersion);
        }
    }
    
    public void SetColorPresets(ArrayList<Settings.tBladeColor> aColorList)
    {
        maBladeColors = aColorList;
    }
    
    public Settings.tBladeParameters GetBladeConfig()
    {
        return mBladeConfig;
    }
    
    public Settings.tMotionParameters GetMotionConfig()
    {
        return mMotionConfig;
    }
    
    public Settings.tSoundParameters GetSoundConfig()
    {
        return mSoundConfig;
    }
    
    public ArrayList<Settings.tBladeColor> GetBladeColors()
    {
        return maBladeColors;
    }
        
    public void SetBladeConfig(Settings.tBladeParameters aBladeConfig)
    {
        mBladeConfig = aBladeConfig;
    }
    
    public void SetMotionConfig(Settings.tMotionParameters aMotionConfig)
    {
       mMotionConfig = aMotionConfig;
    }
    
    public void SetSoundConfig(Settings.tSoundParameters aSoundConfig)
    {
        mSoundConfig = aSoundConfig;
    }
    
    public String GetProtocolVersion()
    {
        return mProtocolVersion;
    }
    
    public String GetFirmwareVersion()
    {
        return mFirmwareVersion;
    }
    
    private void Sleep(long aMillis)
    {
        try
        {
            Thread.sleep(aMillis);
        }
        catch(InterruptedException lEx)
        {
            lEx.printStackTrace();
        }
    }
    
    public void PrintBladeConfig()
    {
        if (null == mBladeConfig) {
            System.out.println("Blade config is null.");
            return;
        }

        System.out.println("Blade Parameters");
        System.out.println("----------------------");
        System.out.print("mMainColorIndex:  ");
        System.out.println(mBladeConfig.mMainColorIndex);
        System.out.print("mFlashColorIndex: ");
        System.out.println(mBladeConfig.mFlashColorIndex);
        System.out.print("mFlickerIndex:    ");
        System.out.println(mBladeConfig.mFlickerIndex);
        System.out.print("mLockupIntensity: ");
        System.out.println(mBladeConfig.mLockupColorIntensity);
    }

    public void PrintSoundConfig()
    {
           
            System.out.println("Sound Parameters");
            System.out.println("----------------------");;
            
            System.out.print("PowerOnBladeDelay     =");
            System.out.println(mSoundConfig.mPowerOnBladeDelay);
            System.out.print("PowerOnTime           =");
            System.out.println(mSoundConfig.mPowerOnTime);
            System.out.print("PowerOffTime          =");
            System.out.println(mSoundConfig.mPowerOffTime);
            System.out.print("BlasterFlashTime      =");
            System.out.println(mSoundConfig.mBlasterFlashTime);
            System.out.print("BlasterSuppressTime   =");
            System.out.println(mSoundConfig.mBlasterSuppressTime);
            System.out.print("ClashSwingSuppressTime=");
            System.out.println(mSoundConfig.mClashSwingSuppressTime);
            System.out.print("MinSwingInterval      =");
            System.out.println(mSoundConfig.mMinSwingInterval);
            System.out.print("MaxSwingInterval      =");
            System.out.println(mSoundConfig.mMaxSwingInterval);
    }
    
    public void PrintMotionConfig()
    {
        System.out.println("Motion Parameters");
        System.out.println("----------------------");

        System.out.print("SwingLargeTol=");
        System.out.println(mMotionConfig.mSwingLargeTol);
        System.out.print("SwingMediumTol=");
        System.out.println(mMotionConfig.mSwingMediumTol);
        System.out.print("SwingSmallTol=");
        System.out.println(mMotionConfig.mSwingSmallTol);
        System.out.print("ClashTol=");
        System.out.println(mMotionConfig.mClashTol);
        System.out.print("TwistTol=");
        System.out.println(mMotionConfig.mTwistTol);        
    }
    
    public void SetSaberInfo(Settings.tSaberInfo aSaberInfo)
    {
        mSaberInfo = aSaberInfo;
    }
    
    public Settings.tSaberInfo GetSaberInfo()
    {
        return mSaberInfo;
    }
    
    public Settings.tOptions GetOptions()
    {
        return this.mOptions;
    }
    
    public void SetOptions(Settings.tOptions aOptions)
    {
        mOptions = aOptions;
    }
    
    //Protocol Version
    protected String mProtocolVersion;
    
    //Fimrware Version
    protected String mFirmwareVersion;
    
    //Stored config data
    protected Settings.tBladeParameters mBladeConfig;
    protected Settings.tMotionParameters mMotionConfig;
    protected Settings.tSoundParameters mSoundConfig;
    protected ArrayList<Settings.tBladeColor> maBladeColors;
    
    //Saber info data
    protected Settings.tSaberInfo mSaberInfo;
    //Options data
    protected Settings.tOptions mOptions;
    
    //Communications objects
    protected CommandMessageSender mMessageSndr;
    protected ResponseMessageReceiver mMessageRcvr;
    
}

