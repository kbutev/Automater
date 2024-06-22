/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.ui.view;

import automater.datasource.MacroActionDataSource;
import automater.ui.text.Strings;
import automater.ui.text.TextValue;
import automater.utilities.Callback;
import automater.utilities.ViewUtilities;
import java.awt.Component;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public class PlayMacroFrame extends javax.swing.JFrame implements View {

    // UI callbacks
    public Callback.Blank onPlayButtonCallback = Callback.buildBlank();
    public Callback.Blank onStopButtonCallback = Callback.buildBlank();
    public Callback.Blank onPauseButtonCallback = Callback.buildBlank();
    public Callback.Blank onResumeButtonCallback = Callback.buildBlank();
    
    /**
     * Creates new form OpenMacroFrame
     */
    public PlayMacroFrame() {
        initComponents();
        setup();
    }
    
    public void present() {
        setVisible(true);
    }
    
    private void setup() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        stateButton.setText(TextValue.getText(TextValue.Play_PlayButtonTitle, playHotkey));
        pauseResumeButton.setText(TextValue.getText(TextValue.Play_PauseButtonTitle, pauseHotkey));
        
        // Set selection model
        macroActionsList.setSelectionModel(selectionModel);
    }
    
    // # Public
    
    public @NotNull String getProgressValue() {
        return String.valueOf(progressBar.getValue());
    }
    
    public void setMacroName(@NotNull String name) {
        macroNameLabel.setText(name);
    }
    
    public @Nullable MacroActionDataSource getDataSource() {
        return dataSource;
    }
    
    public void setDataSource(@NotNull MacroActionDataSource dataSource) {
        this.dataSource = dataSource;
        macroActionsList.setModel(dataSource);
    }
    
    public void setHotkeys(@NotNull String play, @NotNull String stop, @NotNull String pause, @NotNull String resume) {
        playHotkey = play;
        stopHotkey = stop;
        pauseHotkey = pause;
        resumeHotkey = resume;
    }
    
    public void playRecording() {
        if (isPlaying) {
            return;
        }

        isPlaying = true;
        isPaused = false;

        stateButton.setText(TextValue.getText(TextValue.Play_StopButtonTitle, stopHotkey));
        pauseResumeButton.setText(TextValue.getText(TextValue.Play_PauseButtonTitle, pauseHotkey));
        pauseResumeButton.setEnabled(true);
        statusLabel.setText(TextValue.getText(TextValue.Play_StatusRunning, "0"));
        setProgressBarValue(0);

        ViewUtilities.setAppRedIconForFrame(this);
    }

    public void stopRecording() {
        if (!isPlaying) {
            return;
        }

        isPlaying = false;
        isPaused = false;

        stateButton.setText(TextValue.getText(TextValue.Play_PlayButtonTitle, playHotkey));
        stateButton.setEnabled(true);
        pauseResumeButton.setText(TextValue.getText(TextValue.Play_PauseButtonTitle, pauseHotkey));
        pauseResumeButton.setEnabled(false);
        statusLabel.setText(TextValue.getText(TextValue.Play_StatusIdle));

        ViewUtilities.setAppIconForFrame(this);
    }
    
    public void pauseRecording() {
        if (!isPlaying) {
            return;
        }
        
        isPaused = true;
        
        pauseResumeButton.setText(TextValue.getText(TextValue.Play_ResumeButtonTitle, resumeHotkey));
        statusLabel.setText(TextValue.getText(TextValue.Play_StatusRunning, getProgressValue()));
    }
    
    public void resumeRecording() {
        if (!isPlaying) {
            return;
        }
        
        isPaused = false;
        
        pauseResumeButton.setText(TextValue.getText(TextValue.Play_PauseButtonTitle, pauseHotkey));
        statusLabel.setText(TextValue.getText(TextValue.Play_StatusRunning, getProgressValue()));
    }

    public void finishRecording() {
        stopRecording();
    }

    public void setProgressBarValue(double value) {
        value *= 100;

        int progress = (int) value;
        progressBar.setValue(progress);
        statusLabel.setText(TextValue.getText(TextValue.Play_StatusRunning, getProgressValue()));
    }

    public void setStatus(String value) {
        statusLabel.setText(value);
    }

    public void setSelectedIndex(int index) {
        if (index >= 0 && index < macroActionsList.getModel().getSize()) {
            selectionModel.manualySelectIndex(index);
            macroActionsList.ensureIndexIsVisible(index);
        }
    }

    public void setHotkeyText(@NotNull String play, @NotNull String stop) {
        playHotkey = play;
        stopHotkey = stop;

        if (!isPlaying) {
            stateButton.setText(TextValue.getText(TextValue.Play_PlayButtonTitle, playHotkey));
        } else {
            stateButton.setText(TextValue.getText(TextValue.Play_StopButtonTitle, stopHotkey));
        }
    }
    
    // # View
    
    @Override
    public Component asComponent() {
        return this;
    }
    
    @Override
    public void reloadData() {
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        macroNameLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        macroActionsList = new javax.swing.JList<>();
        stateButton = new javax.swing.JButton();
        pauseResumeButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Play macro");
        setMaximumSize(new java.awt.Dimension(32767, 32767));
        setResizable(false);

        macroNameLabel.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        macroNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        macroNameLabel.setText("MACRO");

        macroActionsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(macroActionsList);

        stateButton.setText("Play");
        stateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stateButtonActionPerformed(evt);
            }
        });

        pauseResumeButton.setText("Pause");
        pauseResumeButton.setEnabled(false);
        pauseResumeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseResumeButtonActionPerformed(evt);
            }
        });

        statusLabel.setText("Status");
        statusLabel.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(stateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pauseResumeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(macroNameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(macroNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stateButton)
                    .addComponent(pauseResumeButton)
                    .addComponent(statusLabel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void stateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stateButtonActionPerformed
        if (!isPlaying) {
            onPlayButtonCallback.perform();
        } else {
            onStopButtonCallback.perform();
        }
    }//GEN-LAST:event_stateButtonActionPerformed

    private void pauseResumeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseResumeButtonActionPerformed
        if (!isPlaying) {
            return;
        }
        
        if (!isPaused) {
            onPauseButtonCallback.perform();
        } else {
            onResumeButtonCallback.perform();
        }
    }//GEN-LAST:event_pauseResumeButtonActionPerformed

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
            java.util.logging.Logger.getLogger(PlayMacroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PlayMacroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PlayMacroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlayMacroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new PlayMacroFrame().setVisible(true);
        });
    }
    
    // # Private
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private final @NotNull DisabledItemSelectionModel selectionModel = new DisabledItemSelectionModel();
    private @Nullable MacroActionDataSource dataSource;
    private @NotNull String playHotkey = Strings.DEFAULT_PLAY_OR_STOP_HOTKEY;
    private @NotNull String stopHotkey = Strings.DEFAULT_PLAY_OR_STOP_HOTKEY;
    private @NotNull String pauseHotkey = Strings.DEFAULT_PAUSE_OR_RESUME_HOTKEY;
    private @NotNull String resumeHotkey = Strings.DEFAULT_PAUSE_OR_RESUME_HOTKEY;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> macroActionsList;
    private javax.swing.JLabel macroNameLabel;
    private javax.swing.JButton pauseResumeButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton stateButton;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
}

class DisabledItemSelectionModel extends DefaultListSelectionModel {

    @Override
    public void setSelectionInterval(int index0, int index1) {
        super.setSelectionInterval(-1, -1);
    }

    public void manualySelectIndex(int index) {
        super.setSelectionInterval(index, index);
    }
}
