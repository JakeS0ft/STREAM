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
import ResponseMessages.ResponseMessageReceiver;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Jake
 */
public class BladeConfigScreen
        extends javax.swing.JFrame
        //implements ResponseMessages.ResponseMessageSubscriber
{

    /**
     * Creates new form BladeConfigScreen
     * @param aMessageSender - STREAM Message sender
     */
    public BladeConfigScreen(CommandMessageSender aMessageSender,
                             ResponseMessageReceiver aMessageRcvr,
                             TargetSettingsManager aSettingsMgr) {
        initComponents();
        
        this.setTitle("JakeSoft's STREAM " + Constants.VERSION + " - Blade Configuration");
        this.setBackground(Color.black);
        this.getContentPane().setBackground(Color.black);
        
        mMessageSender = aMessageSender;
        mMessageRcvr = aMessageRcvr;
        mSettingsMgr = aSettingsMgr;
        
        InitDisplay();    
        UpdateDisplay();
    }

    /**
     * Initialize the display to the saber's user settings
     */
    public void InitDisplay()
    {
        mBladeParameters = mSettingsMgr.GetBladeConfig();
        mBladeColors = mSettingsMgr.GetBladeColors();
        
        //Subscriber for messages
        //mMessageRcvr.Subscribe(this);
        
        //TODO: Set number of flickers based on saber info from settings manager
        int lNumFlickers = (int)(mSettingsMgr.GetSaberInfo().mNumBladeFlickers);
        SpinnerNumberModel lNumModel = new SpinnerNumberModel();
        lNumModel.setMinimum(0);
        lNumModel.setMaximum(lNumFlickers-1);
        lNumModel.setStepSize(1);
        mFlickerEffectSpinner.setModel(lNumModel);
        
        //Now mBladeParameters should be updated with the target selections,
        //so go ahead and pre-load those values into the screen controls
        Integer lMainColorIndex = (int) mBladeParameters.mMainColorIndex;
        Integer lFlashColorIndex = (int) mBladeParameters.mFlashColorIndex;
        Integer lFlickerIndex = (int) mBladeParameters.mFlickerIndex;
        Integer lLockupColorIntensity = (int) mBladeParameters.mLockupColorIntensity;
        Integer lLockupFlickerIntensity = (int) mBladeParameters.mLockupFlickerIntensity;
        Integer lLockupFramePeriod = (int) mBladeParameters.mLockupFramePeriod;
        
        mMainColorSpinner.setValue(lMainColorIndex);
        mFlashColorSpinner.setValue(lFlashColorIndex);
        mFlickerEffectSpinner.setValue(lFlickerIndex);
        mLockupColorIntensitySpinner.setValue(lLockupColorIntensity);
        mLockupFlickerIntensitySpinner.setValue(lLockupFlickerIntensity);
        mLockupFramePeriodSpinner.setValue(lLockupFramePeriod);

    }
    
    public void SetHomeScreen(HomeScreen aHomeScreen)
    {
        mHomeScreen = aHomeScreen;
    }
    /**
     * Update readouts for current data and user selection.
     */
    public void UpdateDisplay()
    {        
        mBladeParameters = mSettingsMgr.GetBladeConfig();
        mBladeColors = mSettingsMgr.GetBladeColors();
        
        /********
         * Update for selected main color
         */
        Integer lMainColorIndex = (Integer)mMainColorSpinner.getValue();

        Byte lChn1 = mBladeColors.get(lMainColorIndex).mChannel1;
        Byte lChn2 = mBladeColors.get(lMainColorIndex).mChannel2;
        Byte lChn3 = mBladeColors.get(lMainColorIndex).mChannel3;

        Integer lChn1Int = Util.Converter.byte2int(lChn1);
        Integer lChn2Int = Util.Converter.byte2int(lChn2);
        Integer lChn3Int = Util.Converter.byte2int(lChn3);

        mMainCh1.setText(lChn1Int.toString());
        mMainCh2.setText(lChn2Int.toString());
        mMainCh3.setText(lChn3Int.toString());

        /********
         * Update for selected flash color
         */
        Integer lFlashColorIndex = (Integer)this.mFlashColorSpinner.getValue();
       
        lChn1 = mBladeColors.get(lFlashColorIndex).mChannel1;
        lChn2 = mBladeColors.get(lFlashColorIndex).mChannel2;
        lChn3 = mBladeColors.get(lFlashColorIndex).mChannel3;

        lChn1Int = Util.Converter.byte2int(lChn1);
        lChn2Int = Util.Converter.byte2int(lChn2);
        lChn3Int = Util.Converter.byte2int(lChn3);

        mFlashCh1.setText(lChn1Int.toString());
        mFlashCh2.setText(lChn2Int.toString());
        mFlashCh3.setText(lChn3Int.toString());        
        
//        /********
//         * Update for selected lockup intensity
//         */
//        Integer lLockupInts = (Integer)this.mLockupIntensitySpinner.getValue();
//        mLockupIntensitySpinner.setValue(lLockupInts);
        
        //Enable the controls since we now have enough data to operate
        mMainColorSpinner.setEnabled(true);
        mFlashColorSpinner.setEnabled(true);
        mFlickerEffectSpinner.setEnabled(true);
        mPreviewMainColorButton.setEnabled(true);
        mPreviewFlashColorButton.setEnabled(true);
        mLockupColorIntensitySpinner.setEnabled(true);
    }
    
//    @Override
//    public void NotifyMessage(ResponseMessageBase aMsg)
//    {
//        if(aMsg.mMsgId == ResponseMessages.MessageIds.eeBladeConfigData.ordinal())
//        {
//            ResponseMessages.tBladeConfigDataMsg lMsg =
//                    (ResponseMessages.tBladeConfigDataMsg)aMsg;
//            
//            mBladeParameters = lMsg.mParameters;
//                    
//            UpdateDisplay();
//            
//
//        }
//    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mMainColorSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        mFlashColorSpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        mFlickerEffectSpinner = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        mPreviewMainColorButton = new javax.swing.JButton();
        mOkButton = new javax.swing.JButton();
        mMainCh1 = new javax.swing.JTextField();
        mMainCh2 = new javax.swing.JTextField();
        mMainCh3 = new javax.swing.JTextField();
        mFlashCh1 = new javax.swing.JTextField();
        mFlashCh2 = new javax.swing.JTextField();
        mFlashCh3 = new javax.swing.JTextField();
        mPreviewFlashColorButton = new javax.swing.JButton();
        mModifyMainColorButton = new javax.swing.JButton();
        mModifyFlashColorButton = new javax.swing.JButton();
        mIconLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        mLockupColorIntensitySpinner = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        mLockupPreviewButton = new javax.swing.JButton();
        mLockupFlickerIntensitySpinner = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        mLockupFramePeriodSpinner = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(800, 330));

        mMainColorSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 11, 1));
        mMainColorSpinner.setEnabled(false);
        mMainColorSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mMainColorSpinnerStateChanged(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Main Color");

        mFlashColorSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 11, 1));
        mFlashColorSpinner.setEnabled(false);
        mFlashColorSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mFlashColorSpinnerStateChanged(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Flash Color");

        mFlickerEffectSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 6, 1));
        mFlickerEffectSpinner.setEnabled(false);
        mFlickerEffectSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mFlickerEffectSpinnerStateChanged(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Flicker Effect");

        mPreviewMainColorButton.setText("Preview");
        mPreviewMainColorButton.setEnabled(false);
        mPreviewMainColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mPreviewMainColorButtonActionPerformed(evt);
            }
        });

        mOkButton.setText("OK");
        mOkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mOkButtonActionPerformed(evt);
            }
        });

        mMainCh1.setText("255");
        mMainCh1.setEnabled(false);

        mMainCh2.setText("255");
        mMainCh2.setEnabled(false);

        mMainCh3.setText("255");
        mMainCh3.setEnabled(false);

        mFlashCh1.setText("255");
        mFlashCh1.setEnabled(false);

        mFlashCh2.setText("255");
        mFlashCh2.setEnabled(false);

        mFlashCh3.setText("255");
        mFlashCh3.setEnabled(false);

        mPreviewFlashColorButton.setText("Preview");
        mPreviewFlashColorButton.setEnabled(false);
        mPreviewFlashColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mPreviewFlashColorButtonActionPerformed(evt);
            }
        });

        mModifyMainColorButton.setText("Modify");
        mModifyMainColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mModifyMainColorButtonActionPerformed(evt);
            }
        });

        mModifyFlashColorButton.setText("Modify");
        mModifyFlashColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mModifyFlashColorButtonActionPerformed(evt);
            }
        });

        mIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Blade_RollOver.png"))); // NOI18N

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        mLockupColorIntensitySpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 1));
        mLockupColorIntensitySpinner.setToolTipText("Lockup color intensity");
        mLockupColorIntensitySpinner.setEnabled(false);
        mLockupColorIntensitySpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mLockupColorIntensitySpinnerStateChanged(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Lockup Color Intensity");
        jLabel4.setToolTipText("");

        mLockupPreviewButton.setText("Preview");
        mLockupPreviewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mLockupPreviewButtonActionPerformed(evt);
            }
        });

        mLockupFlickerIntensitySpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 1));
        mLockupFlickerIntensitySpinner.setToolTipText("Lockup flicker intensity");
        mLockupFlickerIntensitySpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mLockupFlickerIntensitySpinnerStateChanged(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Lockup Flicker Intensity");

        mLockupFramePeriodSpinner.setModel(new javax.swing.SpinnerNumberModel(5, 5, 1500, 5));
        mLockupFramePeriodSpinner.setToolTipText("Change speed of lockup animation. Lower numbers mean faster animation.");
        mLockupFramePeriodSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mLockupFramePeriodSpinnerStateChanged(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Lockup Frame Period");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(mIconLabel)
                .addGap(35, 35, 35)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(mOkButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(mFlickerEffectSpinner)
                            .addComponent(mMainColorSpinner)
                            .addComponent(mLockupFlickerIntensitySpinner)
                            .addComponent(mLockupColorIntensitySpinner)
                            .addComponent(mFlashColorSpinner)
                            .addComponent(mLockupFramePeriodSpinner))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mFlashCh1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mMainCh1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mFlashCh2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mMainCh2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mMainCh3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mFlashCh3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(mModifyMainColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(mPreviewMainColorButton)
                        .addGap(145, 145, 145))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(79, 79, 79)
                                .addComponent(mLockupPreviewButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(mModifyFlashColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mPreviewFlashColorButton)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(mFlickerEffectSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(mMainColorSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)
                                    .addComponent(mMainCh1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(mMainCh2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(mMainCh3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(mModifyMainColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(mPreviewMainColorButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mFlashColorSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(mFlashCh1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mFlashCh2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mFlashCh3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mModifyFlashColorButton)
                            .addComponent(mPreviewFlashColorButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(mLockupColorIntensitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(mLockupFlickerIntensitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(mLockupFramePeriodSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)))
                            .addComponent(mLockupPreviewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(mOkButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(mIconLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mMainColorSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mMainColorSpinnerStateChanged
        UpdateDisplay();

        Integer lValue = (Integer)mMainColorSpinner.getValue();
        mBladeParameters.mMainColorIndex = lValue.byteValue();
    }//GEN-LAST:event_mMainColorSpinnerStateChanged

    private void mFlashColorSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mFlashColorSpinnerStateChanged
        UpdateDisplay();
        
        Integer lValue = (Integer)mFlashColorSpinner.getValue();
        mBladeParameters.mFlashColorIndex = lValue.byteValue();
    }//GEN-LAST:event_mFlashColorSpinnerStateChanged

    private void PreviewBladeColor(String aCh0Str, String aCh1Str, String aCh2Str, int aTime, boolean aTurnOffWhenDone)
    {
        
        CommandMessages.tBladeControlSetChannelMsg lSetChnlMsg =
                new CommandMessages.tBladeControlSetChannelMsg();
        
        Integer lCh0 = Integer.parseInt(aCh0Str);
        Integer lCh1 = Integer.parseInt(aCh1Str);
        Integer lCh2 = Integer.parseInt(aCh2Str);
        
        lSetChnlMsg.mChannel = 0;
        lSetChnlMsg.mValue = lCh0.byteValue();
        mMessageSender.SendMessage(lSetChnlMsg);
        
        lSetChnlMsg.mChannel = 1;
        lSetChnlMsg.mValue = lCh1.byteValue();
        mMessageSender.SendMessage(lSetChnlMsg);
        
        lSetChnlMsg.mChannel = 2;
        lSetChnlMsg.mValue = lCh2.byteValue();
        mMessageSender.SendMessage(lSetChnlMsg);            
        
        //Turn the blade on and wait for 5 seconds
        CommandMessages.tBladeControlSetPowerMsg lBladePowerMsg =
                new CommandMessages.tBladeControlSetPowerMsg();
        lBladePowerMsg.mPower = 1;
        mMessageSender.SendMessage(lBladePowerMsg);
        EasySleep(aTime);
        
        //Now turn the blade off
        if(aTurnOffWhenDone)
        {
            lBladePowerMsg.mPower = 0;
            mMessageSender.SendMessage(lBladePowerMsg);
        }
    }
    
    private void mPreviewMainColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mPreviewMainColorButtonActionPerformed
        Integer lEffect = (int)mFlickerEffectSpinner.getValue();
        
        //Command the blade effect
        CommandMessages.tBladeControlSetEffectMsg lBladeEffectMsg =
                new CommandMessages.tBladeControlSetEffectMsg(); 
        lBladeEffectMsg.mEffect = lEffect.byteValue();
        lBladeEffectMsg.mEnableNow = 1;
        
        mMessageSender.SendMessage(lBladeEffectMsg);
        
        PreviewBladeColor(mMainCh1.getText(), mMainCh2.getText(), mMainCh3.getText(), 5000, true);
        
        //Disable blade effect
        lBladeEffectMsg.mEnableNow = 0;
        mMessageSender.SendMessage(lBladeEffectMsg);
        
        //Ensure blade is off
        CommandMessages.tBladeControlSetPowerMsg lBladePwrMsg =
                new CommandMessages.tBladeControlSetPowerMsg();
        lBladePwrMsg.mPower = 0;
        mMessageSender.SendMessage(lBladePwrMsg);
    }//GEN-LAST:event_mPreviewMainColorButtonActionPerformed

    private void mPreviewFlashColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mPreviewFlashColorButtonActionPerformed
        PreviewBladeColor(mFlashCh1.getText(), mFlashCh2.getText(), mFlashCh3.getText(), 5000, true);
    }//GEN-LAST:event_mPreviewFlashColorButtonActionPerformed

    private void mOkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mOkButtonActionPerformed
        mSettingsMgr.SetBladeConfig(mBladeParameters);
        this.setVisible(false);
        if(null != mHomeScreen)
        {
            mHomeScreen.setVisible(true);
        }
    }//GEN-LAST:event_mOkButtonActionPerformed

    private void mModifyMainColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mModifyMainColorButtonActionPerformed
        OpenBladeColorScreen((Integer)mMainColorSpinner.getValue());
    }//GEN-LAST:event_mModifyMainColorButtonActionPerformed

    private void mModifyFlashColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mModifyFlashColorButtonActionPerformed
        OpenBladeColorScreen((Integer)mFlashColorSpinner.getValue());
    }//GEN-LAST:event_mModifyFlashColorButtonActionPerformed

    private void mFlickerEffectSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mFlickerEffectSpinnerStateChanged
        Integer lValue = (Integer)mFlickerEffectSpinner.getValue();
        mBladeParameters.mFlickerIndex = lValue.byteValue();
    }//GEN-LAST:event_mFlickerEffectSpinnerStateChanged

    private void mLockupColorIntensitySpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mLockupColorIntensitySpinnerStateChanged
        Integer lValue = (Integer)mLockupColorIntensitySpinner.getValue();
        mBladeParameters.mLockupColorIntensity = lValue.byteValue();
    }//GEN-LAST:event_mLockupColorIntensitySpinnerStateChanged

    private void OldLockupTest()
    {
               final int lPreviewTimeMs = 5000; // 5 second preview
        final int lUpdateIntervalMs = 50;
        Integer lLockupIntensity = (int)mLockupColorIntensitySpinner.getValue();
             
        //This loop mirrors how the firmware works for applying the lockup effect
        for(int lLoop = 0; lLoop < lPreviewTimeMs; lLoop += lUpdateIntervalMs)
        {
            int lRandomNumber = (int)(Math.random() * 100.0);
            //System.out.print("Random number: ");System.out.println(lRandomNumber);
             
            if(lRandomNumber > lLockupIntensity)
            {
                PreviewBladeColor(mMainCh1.getText(), mMainCh2.getText(), mMainCh3.getText(), 45, false);
            }
            else
            {
                PreviewBladeColor(mFlashCh1.getText(), mFlashCh2.getText(), mFlashCh3.getText(), 45, false);
            }
        }
               
        //Ensure blade is off
        CommandMessages.tBladeControlSetPowerMsg lBladePwrMsg =
                new CommandMessages.tBladeControlSetPowerMsg();
        lBladePwrMsg.mPower = 0;
        mMessageSender.SendMessage(lBladePwrMsg);
    }
    
    private void NewLockupTest()
    {
        final byte lPreviewTimeSec = 5; // 5 second preview
        //Fetch all the data from the GUI
        Integer lUpdateIntervalMs = (int)mLockupFramePeriodSpinner.getValue();
        Integer lLockupColorIntensity = (int)mLockupColorIntensitySpinner.getValue();
        Integer lLockupFlickerIntensity = (int)this.mLockupFlickerIntensitySpinner.getValue();
        
        Integer lMainColorCh0 = Integer.parseInt(this.mMainCh1.getText());
        Integer lMainColorCh1 = Integer.parseInt(this.mMainCh2.getText());
        Integer lMainColorCh2 = Integer.parseInt(this.mMainCh3.getText());
        
        Integer lLockColorCh0 = Integer.parseInt(this.mFlashCh1.getText());
        Integer lLockColorCh1 = Integer.parseInt(this.mFlashCh2.getText());
        Integer lLockColorCh2 = Integer.parseInt(this.mFlashCh3.getText());
        
        //Create lockup test message to send
        CommandMessages.tLockupTestMsg lLockupTestMsg = new CommandMessages.tLockupTestMsg();
        
        //Fill in lockup parameters
        lLockupTestMsg.mColorIntensity = lLockupColorIntensity.byteValue();
        lLockupTestMsg.mFlickerIntensity = lLockupFlickerIntensity.byteValue();
        lLockupTestMsg.mFramePeriod = lUpdateIntervalMs.shortValue();
        lLockupTestMsg.mTestLenSec = lPreviewTimeSec;
        
        //Fill out main color channels based on user selections
        lLockupTestMsg.mMainColorChannels[0] = lMainColorCh0.byteValue();
        lLockupTestMsg.mMainColorChannels[1] = lMainColorCh1.byteValue();
        lLockupTestMsg.mMainColorChannels[2] = lMainColorCh2.byteValue();
        
        //Fill out lockup color channels based on user selections
        lLockupTestMsg.mLockupColorChannels[0] = lLockColorCh0.byteValue();
        lLockupTestMsg.mLockupColorChannels[1] = lLockColorCh1.byteValue();
        lLockupTestMsg.mLockupColorChannels[2] = lLockColorCh2.byteValue();
        
        mMessageSender.SendMessage(lLockupTestMsg);
        
        //Wait for test to end
        EasySleep(lPreviewTimeSec * 1000);
        
        //Ensure blade is off
        CommandMessages.tBladeControlSetPowerMsg lBladePwrMsg =
                new CommandMessages.tBladeControlSetPowerMsg();
        lBladePwrMsg.mPower = 0;
        mMessageSender.SendMessage(lBladePwrMsg);        
    }
    
    private void mLockupPreviewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mLockupPreviewButtonActionPerformed
//        final int lPreviewTimeMs = 5000; // 5 second preview
//        final int lUpdateIntervalMs = 50;
//        Integer lLockupIntensity = (int)mLockupIntensitySpinner.getValue();
//             
//        //This loop mirrors how the firmware works for applying the lockup effect
//        for(int lLoop = 0; lLoop < lPreviewTimeMs; lLoop += lUpdateIntervalMs)
//        {
//            int lRandomNumber = (int)(Math.random() * 100.0);
//            //System.out.print("Random number: ");System.out.println(lRandomNumber);
//             
//            if(lRandomNumber > lLockupIntensity)
//            {
//                PreviewBladeColor(mMainCh1.getText(), mMainCh2.getText(), mMainCh3.getText(), 45, false);
//            }
//            else
//            {
//                PreviewBladeColor(mFlashCh1.getText(), mFlashCh2.getText(), mFlashCh3.getText(), 45, false);
//            }
//        }
//               
//        //Ensure blade is off
//        CommandMessages.tBladeControlSetPowerMsg lBladePwrMsg =
//                new CommandMessages.tBladeControlSetPowerMsg();
//        lBladePwrMsg.mPower = 0;
//        mMessageSender.SendMessage(lBladePwrMsg);
        //OldLockupTest();
        NewLockupTest();
    }//GEN-LAST:event_mLockupPreviewButtonActionPerformed

    private void mLockupFlickerIntensitySpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mLockupFlickerIntensitySpinnerStateChanged
        Integer lValue = (Integer)mLockupFlickerIntensitySpinner.getValue();
        mBladeParameters.mLockupFlickerIntensity = lValue.byteValue();
    }//GEN-LAST:event_mLockupFlickerIntensitySpinnerStateChanged

    private void mLockupFramePeriodSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mLockupFramePeriodSpinnerStateChanged
        Integer lValue = (Integer)mLockupFramePeriodSpinner.getValue();
        mBladeParameters.mLockupFramePeriod = lValue.shortValue();
    }//GEN-LAST:event_mLockupFramePeriodSpinnerStateChanged

    private void OpenBladeColorScreen(int aColorIndex)
    {
        if(null == mBladeColorScreen)
        {
            mBladeColorScreen = new BladeColorScreen(mMessageSender,
                                                     mSettingsMgr);
        }
        
        mBladeColorScreen.SetColorPresetIndex(aColorIndex);
        mBladeColorScreen.setBounds(getBounds());
        mBladeColorScreen.SetBladeConfigScreen(this);
        this.setVisible(false);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mBladeColorScreen.setVisible(true);
            }
        });
    }
    private void EasySleep(long aTime)
    {
        try
        {
            Thread.sleep(aTime);
        }
        catch (InterruptedException lEx)
        {
            lEx.printStackTrace();
        }
    }
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
//            java.util.logging.Logger.getLogger(BladeConfigScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(BladeConfigScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(BladeConfigScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(BladeConfigScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new BladeConfigScreen().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField mFlashCh1;
    private javax.swing.JTextField mFlashCh2;
    private javax.swing.JTextField mFlashCh3;
    private javax.swing.JSpinner mFlashColorSpinner;
    private javax.swing.JSpinner mFlickerEffectSpinner;
    private javax.swing.JLabel mIconLabel;
    private javax.swing.JSpinner mLockupColorIntensitySpinner;
    private javax.swing.JSpinner mLockupFlickerIntensitySpinner;
    private javax.swing.JSpinner mLockupFramePeriodSpinner;
    private javax.swing.JButton mLockupPreviewButton;
    private javax.swing.JTextField mMainCh1;
    private javax.swing.JTextField mMainCh2;
    private javax.swing.JTextField mMainCh3;
    private javax.swing.JSpinner mMainColorSpinner;
    private javax.swing.JButton mModifyFlashColorButton;
    private javax.swing.JButton mModifyMainColorButton;
    private javax.swing.JButton mOkButton;
    private javax.swing.JButton mPreviewFlashColorButton;
    private javax.swing.JButton mPreviewMainColorButton;
    // End of variables declaration//GEN-END:variables

    CommandMessageSender mMessageSender;
    ResponseMessageReceiver mMessageRcvr;
    Settings.tBladeParameters mBladeParameters;
    TargetSettingsManager mSettingsMgr;
    ArrayList<Settings.tBladeColor> mBladeColors;
    
    //Refs to other screens
    HomeScreen mHomeScreen;
    
    BladeColorScreen mBladeColorScreen;
}
