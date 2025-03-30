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

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import Util.SoundSettingsLoader;
import Util.SoundSettingsSaver;
import java.lang.reflect.Field;
import javax.swing.JTextField;
import javax.swing.plaf.metal.MetalFileChooserUI;

/**
 *
 * @author Jake
 */
public class SoundConfigScreen extends javax.swing.JFrame {

    /**
     * Creates new form SoundConfigScreen
     */
    public SoundConfigScreen(TargetSettingsManager aSettingMgr) {

        initComponents();
        this.setTitle("JakeSoft's STREAM " + Constants.VERSION + " - Sound");
        
        mSettingsMgr = aSettingMgr;
        UpdateDisplay();
        
        this.setBackground(Color.black);
        this.getContentPane().setBackground(Color.black);
        this.mVolumeLabel.setVisible(false);
        this.mVolumeSpinner.setVisible(false);
        
    }

    public void SetSettingsMgr(TargetSettingsManager aSettingMgr)
    {
        mSettingsMgr = aSettingMgr;
        UpdateDisplay();
    }
    
    public void SetHomeScreen(HomeScreen aHomeScreen)
    {
        mHomeScreen = aHomeScreen;
        setBounds(mHomeScreen.getBounds());
    }
 
    public void SetCommandSender(CommandMessages.CommandMessageSender aMessageSndr)
    {
        mMessageSndr = aMessageSndr;
    }
    
    private void UpdateSettingsMgr()
    {
        Integer lBlasterFlashTime = (Integer)mBlasterFlashTimeSpinner.getValue();
        Integer lBlasterSuppressTime = (Integer)mBlasterSuppressTime.getValue();
        Integer lClashSwingSuppressTime = (Integer)mClashSwingSuppressTime.getValue();
        Integer lMaxSwingInterval = (Integer)mMaxSwingInterval.getValue();
        Integer lMinSwingInterval = (Integer)mMinSwingInterval.getValue();
        Integer lPowerOffTime = (Integer)mPowerOffTimeSpinner.getValue();
        Integer lPowerOnTime = (Integer)mPowerOnTimeSpinner.getValue();
        Integer lPowerOnDelay = (Integer)mPowerOnBladeDelaySpinner.getValue();
        Integer mVolume = (Integer)mVolumeSpinner.getValue();
        Integer lHumRelaunchInterval = (Integer)mHumRelaunchIntervalSpinner.getValue();

        if(null != mSettingsMgr)
        {
            Settings.tSoundParameters lSoundParams =
                    mSettingsMgr.GetSoundConfig();
            
            lSoundParams.mBlasterFlashTime = lBlasterFlashTime.shortValue();
            lSoundParams.mBlasterSuppressTime = lBlasterSuppressTime.shortValue();
            lSoundParams.mClashSwingSuppressTime = lClashSwingSuppressTime.shortValue();
            lSoundParams.mMaxSwingInterval = lMaxSwingInterval.shortValue();
            lSoundParams.mMinSwingInterval = lMinSwingInterval.shortValue();
            lSoundParams.mPowerOffTime = lPowerOffTime.shortValue();
            lSoundParams.mPowerOnTime = lPowerOnTime.shortValue();
            lSoundParams.mPowerOnBladeDelay = lPowerOnDelay.shortValue();
            lSoundParams.mHumRelaunchInterval = lHumRelaunchInterval.shortValue();
            
            mSettingsMgr.SetSoundConfig(lSoundParams);
            
            DumpShortValue("lSoundParams.mBlasterFlashTime", lSoundParams.mBlasterFlashTime);
            DumpShortValue("lSoundParams.mBlasterSuppressTime", lSoundParams.mBlasterSuppressTime);
            DumpShortValue("lSoundParams.mClashSwingSuppressTime",lSoundParams.mClashSwingSuppressTime);
            DumpShortValue("lSoundParams.mMaxSwingInterval",lSoundParams.mMaxSwingInterval);
            DumpShortValue("lSoundParams.mMinSwingInterval",lSoundParams.mMinSwingInterval);
            DumpShortValue("lSoundParams.mPowerOffTime",lSoundParams.mPowerOffTime);
            DumpShortValue("lSoundParams.mPowerOnTime",lSoundParams.mPowerOnTime);
            DumpShortValue("lSoundParams.mPowerOnBladeDelay",lSoundParams.mPowerOnBladeDelay);
            System.out.print("lSoundParams.mSoundVolume:");
            System.out.println(mVolume);
        }
        else
        {
            System.out.println("Not saving sound configs. Mgr is null.");
        }
    }
    
    private void DumpShortValue(String aValueName, short aValue)
    {
        System.out.print(aValueName);
        System.out.print(":");
        System.out.println(aValue);
    }
    
    public void UpdateDisplay()
    {
       if(null != mSettingsMgr)
        {
            Settings.tSoundParameters lSoundParams =
                    mSettingsMgr.GetSoundConfig();
            
            Integer lBlasterFlashTime = (int) lSoundParams.mBlasterFlashTime;
            Integer lBlasterSuppressTime = (int) lSoundParams.mBlasterSuppressTime;
            Integer lClashSwingSuppressTime = (int) lSoundParams.mClashSwingSuppressTime;
            Integer lMaxSwingInterval = (int) lSoundParams.mMaxSwingInterval;
            Integer lMinSwingInterval = (int) lSoundParams.mMinSwingInterval;
            Integer lPowerOffTime = (int) lSoundParams.mPowerOffTime;
            Integer lPowerOnTime = (int) lSoundParams.mPowerOnTime;
            Integer lPowerOnDelay = (int) lSoundParams.mPowerOnBladeDelay;
            Integer lHumRelaunchInterval = (int) lSoundParams.mHumRelaunchInterval;
            
            mBlasterFlashTimeSpinner.setValue(lBlasterFlashTime);
            mBlasterSuppressTime.setValue(lBlasterSuppressTime);
            mClashSwingSuppressTime.setValue(lClashSwingSuppressTime);
            mMaxSwingInterval.setValue(lMaxSwingInterval);
            mMinSwingInterval.setValue(lMinSwingInterval);
            mPowerOffTimeSpinner.setValue(lPowerOffTime);
            mPowerOnTimeSpinner.setValue(lPowerOnTime);
            mPowerOnBladeDelaySpinner.setValue(lPowerOnDelay);
            mHumRelaunchIntervalSpinner.setValue(lHumRelaunchInterval);
        }
        else
        {
            System.out.println("Not updating sound display. Mgr is null.");
        }
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mVolumeSpinner = new javax.swing.JSpinner();
        mPowerOnBladeDelaySpinner = new javax.swing.JSpinner();
        mPowerOnTimeSpinner = new javax.swing.JSpinner();
        mPowerOffTimeSpinner = new javax.swing.JSpinner();
        mBlasterFlashTimeSpinner = new javax.swing.JSpinner();
        mVolumeLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        mBlasterSuppressTime = new javax.swing.JSpinner();
        mClashSwingSuppressTime = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        mMinSwingInterval = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        mMaxSwingInterval = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        mPreviewPoweronPowerOffButton = new javax.swing.JButton();
        mOkButton = new javax.swing.JButton();
        mIconLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        mHumRelaunchIntervalSpinner = new javax.swing.JSpinner();
        mPreviewHumRelaunchButton = new javax.swing.JButton();
        mLoadButton = new javax.swing.JButton();
        mSaveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        mVolumeSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 30, 1));

        mPowerOnBladeDelaySpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 32767, 1));

        mPowerOnTimeSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 32767, 1));

        mPowerOffTimeSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 32767, 1));

        mBlasterFlashTimeSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 2000, 1));

        mVolumeLabel.setForeground(new java.awt.Color(255, 255, 255));
        mVolumeLabel.setText("Volume");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Power On Blade Delay");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Power On Time");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Power Off Time");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Blaster Flash Time");

        mBlasterSuppressTime.setModel(new javax.swing.SpinnerNumberModel(0, 0, 32767, 1));

        mClashSwingSuppressTime.setModel(new javax.swing.SpinnerNumberModel(0, 0, 32767, 1));

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Swing Suppress After Clash");

        mMinSwingInterval.setModel(new javax.swing.SpinnerNumberModel(0, 0, 32767, 1));

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Min Swing Interval");

        mMaxSwingInterval.setModel(new javax.swing.SpinnerNumberModel(0, 0, 32767, 1));

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Swing Repeat Interval");

        mPreviewPoweronPowerOffButton.setText("Preview");
        mPreviewPoweronPowerOffButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mPreviewPoweronPowerOffButtonActionPerformed(evt);
            }
        });

        mOkButton.setText("OK");
        mOkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mOkButtonActionPerformed(evt);
            }
        });

        mIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/SoundTiming_RollOver.png"))); // NOI18N

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Blaster Swing Suppress Time");

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Hum Relaunch Interval");

        mHumRelaunchIntervalSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 32767, 1));

        mPreviewHumRelaunchButton.setText("Preview");
        mPreviewHumRelaunchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mPreviewHumRelaunchButtonActionPerformed(evt);
            }
        });

        mLoadButton.setText("Load");
        mLoadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mLoadButtonActionPerformed(evt);
            }
        });

        mSaveButton.setText("Save");
        mSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(mIconLabel)
                .addGap(35, 35, 35)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(mVolumeLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(mBlasterSuppressTime, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mClashSwingSuppressTime, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mMinSwingInterval, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(mPowerOffTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(mBlasterFlashTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(mPowerOnTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(mPowerOnBladeDelaySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(mVolumeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(mPreviewPoweronPowerOffButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(mHumRelaunchIntervalSpinner, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(mMaxSwingInterval, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(mPreviewHumRelaunchButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap(206, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mLoadButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mSaveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(mOkButton)
                        .addGap(319, 319, 319))))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {mBlasterFlashTimeSpinner, mBlasterSuppressTime, mClashSwingSuppressTime, mMaxSwingInterval, mMinSwingInterval, mPowerOffTimeSpinner, mPowerOnBladeDelaySpinner, mPowerOnTimeSpinner, mVolumeSpinner});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mVolumeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mVolumeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mPowerOnBladeDelaySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mPowerOnTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mPowerOffTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addComponent(mPreviewPoweronPowerOffButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mBlasterFlashTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mBlasterSuppressTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mClashSwingSuppressTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mMinSwingInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mMaxSwingInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(mHumRelaunchIntervalSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mPreviewHumRelaunchButton))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mOkButton)
                    .addComponent(mLoadButton)
                    .addComponent(mSaveButton))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(mIconLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {mBlasterFlashTimeSpinner, mBlasterSuppressTime, mClashSwingSuppressTime, mMaxSwingInterval, mMinSwingInterval, mPowerOffTimeSpinner, mPowerOnBladeDelaySpinner, mPowerOnTimeSpinner, mVolumeSpinner});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mOkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mOkButtonActionPerformed
        UpdateSettingsMgr();
        
        mHomeScreen.setBounds(this.getBounds());
        mHomeScreen.setVisible(true);
        
        this.setVisible(false);
    }//GEN-LAST:event_mOkButtonActionPerformed

    private void mPreviewPoweronPowerOffButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mPreviewPoweronPowerOffButtonActionPerformed
        UpdateSettingsMgr();
        
        //Fetch current blade and color settings
        Settings.tBladeParameters lBladeConfig = mSettingsMgr.GetBladeConfig();
        int lBladeColorIndex = (int)lBladeConfig.mMainColorIndex;
        ArrayList<Settings.tBladeColor> lColorPresets = mSettingsMgr.GetBladeColors();
        
        System.out.print("Selected blade color index is ");
        System.out.println(lBladeColorIndex);
        
        CommandMessages.tBladeControlSetChannelMsg lBladeCtlMsg =
                new CommandMessages.tBladeControlSetChannelMsg();

        lBladeCtlMsg.mChannel = 0;
        lBladeCtlMsg.mValue = lColorPresets.get(lBladeColorIndex).mChannel1;        
        mMessageSndr.SendMessage(lBladeCtlMsg);
        
        lBladeCtlMsg.mChannel = 1;
        lBladeCtlMsg.mValue = lColorPresets.get(lBladeColorIndex).mChannel2;
        mMessageSndr.SendMessage(lBladeCtlMsg);
        
        lBladeCtlMsg.mChannel = 2;
        lBladeCtlMsg.mValue = lColorPresets.get(lBladeColorIndex).mChannel2;
        mMessageSndr.SendMessage(lBladeCtlMsg);
        
        System.out.print("Set blade channels:");
        System.out.print(lColorPresets.get(lBladeColorIndex).mChannel1);
        System.out.print("/");
        System.out.print(lColorPresets.get(lBladeColorIndex).mChannel2);
        System.out.print("/");
        System.out.print(lColorPresets.get(lBladeColorIndex).mChannel3);
        System.out.println("");

        
        CommandMessages.tPowerUpPowerDownTestMsg lsPwrTestMsg =
                new CommandMessages.tPowerUpPowerDownTestMsg();
        
        lsPwrTestMsg.mPowerOnDelay = mSettingsMgr.GetSoundConfig().mPowerOnBladeDelay;
        lsPwrTestMsg.mFlickerType = mSettingsMgr.GetBladeConfig().mFlickerIndex;
        lsPwrTestMsg.mPowerupTime = mSettingsMgr.GetSoundConfig().mPowerOnTime;
        lsPwrTestMsg.mPowerdownTime = mSettingsMgr.GetSoundConfig().mPowerOffTime;
        lsPwrTestMsg.mOnDuration = 5;
        lsPwrTestMsg.mBladeColor = mSettingsMgr.GetBladeColors().get(mSettingsMgr.GetBladeConfig().mMainColorIndex);
        
        mMessageSndr.SendMessage(lsPwrTestMsg);
        
        System.out.print("PowerOnDelay="); System.out.println(lsPwrTestMsg.mPowerOnDelay);
        System.out.print("FlickerType="); System.out.println(lsPwrTestMsg.mFlickerType);
        System.out.print("PowerupTime="); System.out.println(lsPwrTestMsg.mPowerupTime);
        System.out.print("PowerdownTime="); System.out.println(lsPwrTestMsg.mPowerdownTime);
        System.out.print("Duration="); System.out.println(lsPwrTestMsg.mOnDuration);
        
        
        long lSleepTime = (long)lsPwrTestMsg.mOnDuration;
        lSleepTime += (long)lsPwrTestMsg.mPowerupTime;
        lSleepTime += (long)lsPwrTestMsg.mPowerdownTime;
        try
        {
            System.out.print("Sleeping for ");
            System.out.print(lSleepTime);
            System.out.println(" ms...");
            Thread.sleep((long)lsPwrTestMsg.mOnDuration);
            System.out.println("Done with pwr on/off sound timing test.");
        }
        catch (InterruptedException lEx)
        {
            //Yeah, who cares?
        }
        
        CommandMessages.tBladeControlSetPowerMsg lBladePwrMsg =
                new CommandMessages.tBladeControlSetPowerMsg();
        lBladePwrMsg.mPower = 0;
        mMessageSndr.SendMessage(lBladePwrMsg);
    }//GEN-LAST:event_mPreviewPoweronPowerOffButtonActionPerformed

    private void mPreviewHumRelaunchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mPreviewHumRelaunchButtonActionPerformed
        //Fetch user value
        Integer lHumRelaunchInterval = (Integer)mHumRelaunchIntervalSpinner.getValue();
        
        //Send the message
        CommandMessages.tHumRepeatIntervalTestMsg lMsg = new CommandMessages.tHumRepeatIntervalTestMsg();
        lMsg.mInterval = lHumRelaunchInterval.shortValue();
        mMessageSndr.SendMessage(lMsg);
        
        try
        {
            Thread.sleep(lHumRelaunchInterval*5);
        }
        catch(InterruptedException lEx)
        {
            //Yeah, who cares?
        }
    }//GEN-LAST:event_mPreviewHumRelaunchButtonActionPerformed

    private void mLoadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mLoadButtonActionPerformed
        System.out.println("Load sound timings button pressed.");
        
        //Create a file chooser
        JFileChooser fileSelect = new JFileChooser();
        fileSelect.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileSelect.setApproveButtonText("Load");
        fileSelect.setDialogTitle("Load Timings");
        
        //Disable the ability to type the file name in the file chooser
        try
        {
            MetalFileChooserUI ui = (MetalFileChooserUI)fileSelect.getUI();
            Field field = MetalFileChooserUI.class.getDeclaredField("fileNameTextField");
            field.setAccessible(true);
            JTextField tf = (JTextField) field.get(ui);
            tf.setEditable(false);
            tf.setEnabled(false);
        }
        catch(NoSuchFieldException ex)
        {
            ex.printStackTrace();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        //In response to a button click:
        int returnVal = fileSelect.showOpenDialog(SoundConfigScreen.this);
        
        String lSelectedFile = null;
        
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fileSelect.getSelectedFile();
            lSelectedFile = (file.getAbsolutePath());            
        } else {
            System.out.println("Open command cancelled by user.");
        }
        
        if(null != lSelectedFile)
        {
            SoundSettingsLoader lcLoader = new SoundSettingsLoader(lSelectedFile);
            
            boolean lbLoadSuccess = false;
            lbLoadSuccess = lcLoader.Load();
                        
            //Load was successful
            if(lbLoadSuccess)
            {
                //Fill in the settings manager data with what was loaded from disk
                lcLoader.FillInParameters(this.mSettingsMgr.GetSoundConfig());
                System.out.println("Sound timing settings load SUCCESS!");
                UpdateDisplay();
                
                //There was a problem, so default were used. Notify the user
                if (lcLoader.DefaultsWereUsed()) {
                    System.out.println("Some data missing from sound timings file.");

                    String lcMessage = "The selected file was missing some expected data.\n";
                    lcMessage += "Defaults were loaded instead for :\n";

                    for (String lcDef : lcLoader.GetDefaultsUsed()) {
                        lcMessage += lcDef;
                        lcMessage += "\n";
                    }

                    JOptionPane.showMessageDialog(this,
                            lcMessage,
                            "Missing Data",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    System.out.println("All expected sound timing data was parsed.");
                }
            }
            else
            {
                System.out.println("Sound timing settings load FAILED!");
                JOptionPane.showMessageDialog(this,
                        "Load FAILED!\n Did you select a valid file?",
                        "Load Fail",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            System.out.println("No sound timing file selected.");
        }
        
    }//GEN-LAST:event_mLoadButtonActionPerformed

    private void mSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mSaveButtonActionPerformed
        System.out.println("Save sound timings button pressed.");
        
        //Create a file chooser
        JFileChooser fileSelect = new JFileChooser();
        fileSelect.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileSelect.setApproveButtonText("Save");
        fileSelect.setDialogTitle("Save Timings");
        
        //In response to a button click:
        int returnVal = fileSelect.showOpenDialog(SoundConfigScreen.this);
        
        String lSelectedFilePathStr = null;
        
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fileSelect.getSelectedFile();
            lSelectedFilePathStr = (file.getAbsolutePath());            
        } else {
            System.out.println("Open command cancelled by user.");
        }
        
        
        File lFileObj = new File(lSelectedFilePathStr);
        
        if(null != lSelectedFilePathStr && !lFileObj.exists())
        {
            SoundSettingsSaver lSaver =
                    new SoundSettingsSaver(lSelectedFilePathStr, mSettingsMgr.GetSoundConfig());
            
            boolean lbSuccess = lSaver.Save();
            
            if(!lbSuccess)
            {
                JOptionPane.showMessageDialog(this,
                        "Sound timings save operation FAILED!",
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(this,
                        "Sound timings saveed successfully.",
                        "Save Success",
                        JOptionPane.INFORMATION_MESSAGE);                
            }
                
        }
        else if(lFileObj.exists())
        {
            JOptionPane.showMessageDialog(this,
                    "File already exits. Choose a new file name.",
                    "File Exists",
                    JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_mSaveButtonActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(SoundConfigScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(SoundConfigScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(SoundConfigScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(SoundConfigScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new SoundConfigScreen().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner mBlasterFlashTimeSpinner;
    private javax.swing.JSpinner mBlasterSuppressTime;
    private javax.swing.JSpinner mClashSwingSuppressTime;
    private javax.swing.JSpinner mHumRelaunchIntervalSpinner;
    private javax.swing.JLabel mIconLabel;
    private javax.swing.JButton mLoadButton;
    private javax.swing.JSpinner mMaxSwingInterval;
    private javax.swing.JSpinner mMinSwingInterval;
    private javax.swing.JButton mOkButton;
    private javax.swing.JSpinner mPowerOffTimeSpinner;
    private javax.swing.JSpinner mPowerOnBladeDelaySpinner;
    private javax.swing.JSpinner mPowerOnTimeSpinner;
    private javax.swing.JButton mPreviewHumRelaunchButton;
    private javax.swing.JButton mPreviewPoweronPowerOffButton;
    private javax.swing.JButton mSaveButton;
    private javax.swing.JLabel mVolumeLabel;
    private javax.swing.JSpinner mVolumeSpinner;
    // End of variables declaration//GEN-END:variables

    TargetSettingsManager mSettingsMgr;
    HomeScreen mHomeScreen;
    CommandMessages.CommandMessageSender mMessageSndr;
}
