/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.view;

import automater.TextValue;
import automater.utilities.SimpleCallback;
import automater.utilities.StringFormatting;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Bytevi
 */
public class PlayMacroOptionsDialog extends javax.swing.JDialog {
    // UI callbacks
    public SimpleCallback onCancelButtonCallback = SimpleCallback.createDoNothing();
    public SimpleCallback onOKButtonCallback = SimpleCallback.createDoNothing();
    
    /**
     * Creates new form PlayMacroOptionsDialog
     */
    public PlayMacroOptionsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setup();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startNotificationCheck = new javax.swing.JCheckBox();
        stopNotificationCheck = new javax.swing.JCheckBox();
        playSpeedLabel = new javax.swing.JLabel();
        playSpeedField = new javax.swing.JTextField();
        repeatLabel = new javax.swing.JLabel();
        repeatField = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        repeatNotificationCheck = new javax.swing.JCheckBox();
        repeatForeverCheck = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });

        startNotificationCheck.setSelected(true);
        startNotificationCheck.setText("Start notification");

        stopNotificationCheck.setSelected(true);
        stopNotificationCheck.setText("Stop notification");

        playSpeedLabel.setText("Play speed");

        playSpeedField.setText("1.0");

        repeatLabel.setText("Repeat");

        repeatField.setText("0");

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        repeatNotificationCheck.setText("Repeat notification");

        repeatForeverCheck.setText("Repeat Forever");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(startNotificationCheck)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(playSpeedLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(playSpeedField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(repeatLabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(stopNotificationCheck)
                                .addGap(18, 18, 18)
                                .addComponent(repeatNotificationCheck))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(repeatField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(repeatForeverCheck))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startNotificationCheck)
                    .addComponent(stopNotificationCheck)
                    .addComponent(repeatNotificationCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playSpeedLabel)
                    .addComponent(playSpeedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(repeatLabel)
                    .addComponent(repeatField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(repeatForeverCheck))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        onOKButtonCallback.perform();
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        onCancelButtonCallback.perform();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        onCancelButtonCallback.perform();
    }//GEN-LAST:event_formWindowDeactivated

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PlayMacroOptionsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PlayMacroOptionsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PlayMacroOptionsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlayMacroOptionsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PlayMacroOptionsDialog dialog = new PlayMacroOptionsDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    private void setup() {
        this.setTitle(TextValue.getText(TextValue.PlayOptions_Title));
        
        startNotificationCheck.setText(TextValue.getText(TextValue.PlayOptions_NotificationPlay));
        stopNotificationCheck.setText(TextValue.getText(TextValue.PlayOptions_NotificationStop));
        repeatNotificationCheck.setText(TextValue.getText(TextValue.PlayOptions_NotificationRepeat));
        
        playSpeedLabel.setText(TextValue.getText(TextValue.PlayOptions_PlaySpeed));
        repeatLabel.setText(TextValue.getText(TextValue.PlayOptions_Repeat));
        repeatForeverCheck.setText(TextValue.getText(TextValue.PlayOptions_RepeatForever));
        
        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateOkButtonState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateOkButtonState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateOkButtonState();
            }
        };
        
        playSpeedField.getDocument().addDocumentListener(listener);
        repeatField.getDocument().addDocumentListener(listener);
    }
    
    // # Public
    
    public boolean isNotificationStartChecked()
    {
        return startNotificationCheck.isSelected();
    }
    
    public boolean isNotificationStopChecked()
    {
        return stopNotificationCheck.isSelected();
    }
    
    public boolean isNotificationRepeatChecked()
    {
        return repeatNotificationCheck.isSelected();
    }
    
    public double getPlaySpeedValue()
    {
        if (optionsAreValid)
        {
            return Double.parseDouble(playSpeedField.getText());
        }
        
        return 0;
    }
    
    public int getRepeatValue()
    {
        if (optionsAreValid)
        {
            return Integer.parseInt(repeatField.getText());
        }
        
        return 0;
    }
    
    public boolean isRepeatForeverChecked()
    {
        return repeatForeverCheck.isSelected();
    }
    
    // # Private
    
    private void updateOkButtonState()
    {
        optionsAreValid = true;
        
        if (!StringFormatting.isStringANumber(playSpeedField.getText()))
        {
            optionsAreValid = false;
        } 
        else if (playSpeedField.getText().length() > 3)
        {
            optionsAreValid = false;
        } else 
        {
            double playSpeedValue = getPlaySpeedValue();
            
            if (playSpeedValue <= 0.0)
            {
                optionsAreValid = false;
            }
        }
        
        if (!StringFormatting.isStringANonNegativeInt(repeatField.getText()))
        {
            optionsAreValid = false;
        }
        
        okButton.setEnabled(optionsAreValid);
    }
    
    private boolean optionsAreValid = true;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField playSpeedField;
    private javax.swing.JLabel playSpeedLabel;
    private javax.swing.JTextField repeatField;
    private javax.swing.JCheckBox repeatForeverCheck;
    private javax.swing.JLabel repeatLabel;
    private javax.swing.JCheckBox repeatNotificationCheck;
    private javax.swing.JCheckBox startNotificationCheck;
    private javax.swing.JCheckBox stopNotificationCheck;
    // End of variables declaration//GEN-END:variables
}
