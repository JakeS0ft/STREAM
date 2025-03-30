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

import Util.Converter;
import java.awt.Color;

/**
 *
 * @author Jake
 */
public class OptionsScreen extends javax.swing.JFrame {

    /**
     * Creates new form OptionsScreen
     */
    public OptionsScreen(TargetSettingsManager aSettingsMgr) {
        initComponents();
                
        this.setTitle("JakeSoft's STREAM " + Constants.VERSION + " - Options");
        mSettingsMgr = aSettingsMgr;
        
        this.setBackground(Color.black);
        this.getContentPane().setBackground(Color.black);
        
        UpdateDisplay();
    }

    public void SetHomeScreen(HomeScreen aHomeScreen)
    {
        mHomeScreen = aHomeScreen;
    }
    
    private void UpdateDisplay()
    {
        Integer lAutoPowerOffTime;
        boolean lExitLockupOnSwing;
        Integer lMenuSwitchHoldTime;
        Integer lPowerOffSwitchHoldTime;
        Integer lSleepTime;
        boolean lOneButtonBlaster;
        boolean lOneButtonLockup;
        boolean lUseFontBootSounds;
        Integer lBladeLength;
        
        lAutoPowerOffTime = (int)mSettingsMgr.GetOptions().mAutoPowerOffTimeSec;
        if(mSettingsMgr.GetOptions().mExitLockupOnSwing > 0)
        {
            lExitLockupOnSwing = true;
        }
        else
        {
            lExitLockupOnSwing = false;
        }
        lMenuSwitchHoldTime = (int)mSettingsMgr.GetOptions().mMenuSwitchHoldTimeMs;
        lPowerOffSwitchHoldTime = (int)mSettingsMgr.GetOptions().mPowerOffSwitchHoldTimeMs;
        lSleepTime = Converter.byte2int(mSettingsMgr.GetOptions().mSleepTimeMins);
        lOneButtonBlaster = (mSettingsMgr.GetOptions().mOneButtonBlasterEnabled > 0);
        lOneButtonLockup = (mSettingsMgr.GetOptions().mOneButtonLockupEnabled > 0);
        lUseFontBootSounds = (mSettingsMgr.GetOptions().mUseFontBootSounds > 0);
        lBladeLength = Converter.byte2int(mSettingsMgr.GetOptions().mBladeLength);
        
        mAutoPowerOffSpinner.setValue(lAutoPowerOffTime);
        mAutomaticPowerOffCheckbox.setSelected((lAutoPowerOffTime > 0));
        mExitLockupOnSwingCheckbox.setSelected(lExitLockupOnSwing);
        mPowerDownSwitchHoldTimeSpinner.setValue(lPowerOffSwitchHoldTime);
        mAutoPowerOffSpinner.setEnabled(mAutomaticPowerOffCheckbox.isSelected());
        mUseFontBootSoundsCheckbox.setSelected(lUseFontBootSounds);
                
        if( lSleepTime.intValue() == 255 || lSleepTime == 0)
        {
            mEnableDeepSleepCheckbox.setSelected(false);
            mDeepSleepSpinner.setEnabled(false);
            System.out.println("Deep sleep is disabled in settings.");
        }
        else
        {
            mEnableDeepSleepCheckbox.setSelected(true);
            mDeepSleepSpinner.setEnabled(true);
            System.out.println("Deep sleep is enabled in settings.");
        }
        mDeepSleepSpinner.setValue(lSleepTime);
        
        mOneButtonBlasterCheckbox.setSelected(lOneButtonBlaster);
        mOneButtonLockupCheckbox.setSelected(lOneButtonLockup);
        mBladeLengthSpinner.setValue(lBladeLength);
        
    }
    
    private void UpdateSettingsManager()
    {
        Integer lAutoPowerOffTime;
        boolean lExitLockupOnSwing;
        //Integer lMenuSwitchHoldTime;
        Integer lPowerOffSwitchHoldTime;
        boolean lEnableAutoPowerOff;
        boolean lEnableDeepSleep;
        Integer lDeepSleepTime;
        boolean lEnableOneButtonBlaster;
        boolean lEnableOneButtonLockup;
        boolean lUseFontBootSounds;
        Integer lBladeLength;
        
        lAutoPowerOffTime = (Integer)mAutoPowerOffSpinner.getValue();
        lExitLockupOnSwing = mExitLockupOnSwingCheckbox.isSelected();
        lPowerOffSwitchHoldTime = (Integer)mPowerDownSwitchHoldTimeSpinner.getValue();
        lEnableAutoPowerOff = mAutomaticPowerOffCheckbox.isSelected();
        lEnableDeepSleep = mEnableDeepSleepCheckbox.isSelected();
        lDeepSleepTime = (Integer)mDeepSleepSpinner.getValue();
        lEnableOneButtonBlaster = mOneButtonBlasterCheckbox.isSelected();
        lEnableOneButtonLockup = mOneButtonLockupCheckbox.isSelected();
        lUseFontBootSounds = mUseFontBootSoundsCheckbox.isSelected();
        lBladeLength = (Integer)mBladeLengthSpinner.getValue();
        
        //Set auto power off time
        if(lEnableAutoPowerOff)
        {
            mSettingsMgr.GetOptions().mAutoPowerOffTimeSec = lAutoPowerOffTime.byteValue();
        }
        else
        {
            mSettingsMgr.GetOptions().mAutoPowerOffTimeSec = 0;
        }
        
        //Set Exit lockup on swing option
        if(lExitLockupOnSwing)
        {
            mSettingsMgr.GetOptions().mExitLockupOnSwing = 1;
        }
        else
        {
            mSettingsMgr.GetOptions().mExitLockupOnSwing = 0;
        }
        
        //Set switch hold for power off time
        mSettingsMgr.GetOptions().mPowerOffSwitchHoldTimeMs = lPowerOffSwitchHoldTime.shortValue();
        
        //Set deep sleep time
        if(lEnableDeepSleep)
        {
            mSettingsMgr.GetOptions().mSleepTimeMins = lDeepSleepTime.byteValue();
        }
        else
        {
            mSettingsMgr.GetOptions().mSleepTimeMins = (byte)255;
        }
        
        //Set one-button blaster enable flag
        if(lEnableOneButtonBlaster)
        {
            mSettingsMgr.GetOptions().mOneButtonBlasterEnabled = 1;
        }
        else
        {
             mSettingsMgr.GetOptions().mOneButtonBlasterEnabled = 0;
        }
        
        //Set one-button lockup enable flag
        if(lEnableOneButtonLockup)
        {
            mSettingsMgr.GetOptions().mOneButtonLockupEnabled = 1;
        }
        else
        {
             mSettingsMgr.GetOptions().mOneButtonLockupEnabled = 0;
        }
        
        //Set use font boot sounds enable flag
        if(lUseFontBootSounds)
        {
            mSettingsMgr.GetOptions().mUseFontBootSounds = 1;
        }
        else
        {
            mSettingsMgr.GetOptions().mUseFontBootSounds = 0;
        }
        
        //Set blade length
        mSettingsMgr.GetOptions().mBladeLength = lBladeLength.byteValue();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mExitLockupOnSwingCheckbox = new javax.swing.JCheckBox();
        mAutomaticPowerOffCheckbox = new javax.swing.JCheckBox();
        mAutoPowerOffSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        mPowerDownSwitchHoldTimeSpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        mOkButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        mEnableDeepSleepCheckbox = new javax.swing.JCheckBox();
        mDeepSleepSpinner = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        mOneButtonLockupCheckbox = new javax.swing.JCheckBox();
        mOneButtonBlasterCheckbox = new javax.swing.JCheckBox();
        mUseFontBootSoundsCheckbox = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        mBladeLengthSpinner = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        mExitLockupOnSwingCheckbox.setBackground(new java.awt.Color(0, 0, 0));
        mExitLockupOnSwingCheckbox.setForeground(new java.awt.Color(255, 255, 255));
        mExitLockupOnSwingCheckbox.setText("Exit one-button lockup on swing");
        mExitLockupOnSwingCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mExitLockupOnSwingCheckboxActionPerformed(evt);
            }
        });

        mAutomaticPowerOffCheckbox.setBackground(new java.awt.Color(0, 0, 0));
        mAutomaticPowerOffCheckbox.setForeground(new java.awt.Color(255, 255, 255));
        mAutomaticPowerOffCheckbox.setText("Power off after");
        mAutomaticPowerOffCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mAutomaticPowerOffCheckboxActionPerformed(evt);
            }
        });

        mAutoPowerOffSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("seconds of idle time.");

        mPowerDownSwitchHoldTimeSpinner.setModel(new javax.swing.SpinnerNumberModel(1000, 1000, 20000, 100));

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Hold switch for");

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("milliseconds to power off.");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Options_RollOver.png"))); // NOI18N

        mOkButton.setText("OK");
        mOkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mOkButtonActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setForeground(new java.awt.Color(102, 255, 255));
        jLabel4.setText("Options will be applied to all profiles.");

        mEnableDeepSleepCheckbox.setBackground(new java.awt.Color(0, 0, 0));
        mEnableDeepSleepCheckbox.setForeground(new java.awt.Color(255, 255, 255));
        mEnableDeepSleepCheckbox.setText("Deep sleep after");
        mEnableDeepSleepCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mEnableDeepSleepCheckboxActionPerformed(evt);
            }
        });

        mDeepSleepSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 30, 5));

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("minutes.");

        mOneButtonLockupCheckbox.setBackground(new java.awt.Color(0, 0, 0));
        mOneButtonLockupCheckbox.setForeground(new java.awt.Color(255, 255, 255));
        mOneButtonLockupCheckbox.setText("One-button lockup");
        mOneButtonLockupCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mOneButtonLockupCheckboxActionPerformed(evt);
            }
        });

        mOneButtonBlasterCheckbox.setBackground(new java.awt.Color(0, 0, 0));
        mOneButtonBlasterCheckbox.setForeground(new java.awt.Color(255, 255, 255));
        mOneButtonBlasterCheckbox.setText("One-button blaster blocks");

        mUseFontBootSoundsCheckbox.setBackground(new java.awt.Color(0, 0, 0));
        mUseFontBootSoundsCheckbox.setForeground(new java.awt.Color(255, 255, 255));
        mUseFontBootSoundsCheckbox.setText("Use Font boot sounds");

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Blade length");

        mBladeLengthSpinner.setModel(new javax.swing.SpinnerNumberModel(10, 10, 255, 1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mExitLockupOnSwingCheckbox)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mPowerDownSwitchHoldTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(mAutomaticPowerOffCheckbox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mAutoPowerOffSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel1)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(mEnableDeepSleepCheckbox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mDeepSleepSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5))
                            .addComponent(mOneButtonLockupCheckbox)
                            .addComponent(mUseFontBootSoundsCheckbox)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(mBladeLengthSpinner))
                                .addComponent(mOneButtonBlasterCheckbox, javax.swing.GroupLayout.Alignment.LEADING))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(184, 184, 184)
                        .addComponent(mOkButton)))
                .addContainerGap(251, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel4)
                        .addGap(20, 20, 20)
                        .addComponent(mExitLockupOnSwingCheckbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mAutomaticPowerOffCheckbox)
                            .addComponent(mAutoPowerOffSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mPowerDownSwitchHoldTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mEnableDeepSleepCheckbox)
                            .addComponent(mDeepSleepSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mOneButtonBlasterCheckbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mOneButtonLockupCheckbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mUseFontBootSoundsCheckbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(mBladeLengthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(mOkButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(jLabel6))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 18, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mOkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mOkButtonActionPerformed
        //Save user settings
        UpdateSettingsManager();
        
        setVisible(false);
        
        if(null != mHomeScreen)
        {
            mHomeScreen.setVisible(true);
            mHomeScreen.setBounds(this.getBounds());
        }
    }//GEN-LAST:event_mOkButtonActionPerformed

    private void mAutomaticPowerOffCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mAutomaticPowerOffCheckboxActionPerformed
        if(mAutomaticPowerOffCheckbox.isSelected())
        {
            mAutoPowerOffSpinner.setEnabled(true);
            mAutoPowerOffSpinner.setValue(30);
        }
        else
        {
            mAutoPowerOffSpinner.setEnabled(false);
            mAutoPowerOffSpinner.setValue(0);
        }
    }//GEN-LAST:event_mAutomaticPowerOffCheckboxActionPerformed

    private void mEnableDeepSleepCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mEnableDeepSleepCheckboxActionPerformed
        if(mEnableDeepSleepCheckbox.isSelected())
        {
            mDeepSleepSpinner.setEnabled(true);
            mDeepSleepSpinner.setValue(1);
        }
        else
        {
            mDeepSleepSpinner.setEnabled(false);
            mDeepSleepSpinner.setValue(0);
        }
    }//GEN-LAST:event_mEnableDeepSleepCheckboxActionPerformed

    private void mExitLockupOnSwingCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mExitLockupOnSwingCheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mExitLockupOnSwingCheckboxActionPerformed

    private void mOneButtonLockupCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mOneButtonLockupCheckboxActionPerformed
        //Do nothing
    }//GEN-LAST:event_mOneButtonLockupCheckboxActionPerformed

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
//            java.util.logging.Logger.getLogger(OptionsScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(OptionsScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(OptionsScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(OptionsScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new OptionsScreen().setVisible(true);
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner mAutoPowerOffSpinner;
    private javax.swing.JCheckBox mAutomaticPowerOffCheckbox;
    private javax.swing.JSpinner mBladeLengthSpinner;
    private javax.swing.JSpinner mDeepSleepSpinner;
    private javax.swing.JCheckBox mEnableDeepSleepCheckbox;
    private javax.swing.JCheckBox mExitLockupOnSwingCheckbox;
    private javax.swing.JButton mOkButton;
    private javax.swing.JCheckBox mOneButtonBlasterCheckbox;
    private javax.swing.JCheckBox mOneButtonLockupCheckbox;
    private javax.swing.JSpinner mPowerDownSwitchHoldTimeSpinner;
    private javax.swing.JCheckBox mUseFontBootSoundsCheckbox;
    // End of variables declaration//GEN-END:variables

    protected TargetSettingsManager mSettingsMgr;
    protected HomeScreen mHomeScreen;
}
