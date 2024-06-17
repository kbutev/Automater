/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.ui.view;

import automater.datasource.StandardDescriptionDataSource;
import automater.ui.text.Strings;
import automater.ui.text.TextValue;
import automater.model.Keystroke;
import automater.utilities.AlertWindows;
import automater.utilities.Callback;
import automater.utilities.Logger;
import automater.utilities.Callback.Blank;
import java.awt.Component;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public class RecordMacroPanel extends javax.swing.JPanel implements View {

    // UI callbacks
    public Callback.Blank onSwitchToPlayButtonCallback = Callback.buildBlank();
    public Callback.Blank onBeginRecordMacroButtonCallback = Callback.buildBlank();
    public Callback.Blank onStopRecordMacroButtonCallback = Callback.buildBlank();
    public Callback.WithParameter<String> onSaveMacroButtonCallback = Callback.buildBlankWithParameter();
    
    /**
     * Creates new form RecordMacroPanel
     */
    public RecordMacroPanel() {
        initComponents();
        setup();
    }
    
    // # View
    
    @Override
    public @NotNull Component asComponent() {
        return this;
    }
    
    @Override
    public void onViewStart() {
        macroActionsList.setModel(StandardDescriptionDataSource.createGeneric());
    }
    
    @Override
    public void onViewSuspended() {
        
    }
    
    @Override
    public void onViewResume() {
        macroActionsList.setModel(StandardDescriptionDataSource.createGeneric());

        macroNameField.setText(TextValue.getText(TextValue.Record_MacroNameFieldDefaultText));
        macroDescriptionField.setText("");
    }
    
    @Override
    public void onViewTerminate() {
        
    }
    
    @Override
    public void reloadData() {
        
    }
    
    private void setup() {
        macroActionsListName.setText(TextValue.getText(TextValue.Record_MacroActionListName));

        macroNameField.setText(TextValue.getText(TextValue.Record_MacroNameFieldDefaultText));
        macroNameField.setToolTipText(TextValue.getText(TextValue.Record_MacroNameFieldTip));

        macroStateLabel.setText(TextValue.getText(TextValue.Record_IdleStatus, startHotkey));

        saveMacroButton.setText(TextValue.getText(TextValue.Record_SaveButton));
        saveMacroButton.setToolTipText(TextValue.getText(TextValue.Record_SaveButtonDisabledTip));

        updateRecordState();
    }
    
    // # Public properties getters/setters
    public boolean isRecording() {
        return _isRecording;
    }

    public String getMacroName() {
        return macroNameField.getText();
    }

    public String getMacroDescription() {
        return macroDescriptionField.getText();
    }

    public void setListDataSource(@NotNull StandardDescriptionDataSource dataSource) {
        macroActionsList.setModel(dataSource);

        scrollToBottom();
    }

    // # Public UI operations
    public void willSwitchToPlayWindow() {
        if (_isRecording) {
            Logger.warning(this, "Switching to play screen while recording is not allowed.");
        }
    }

    public void beginRecording() {
        _isRecording = true;
        _recordStartCount++;
        macroNameField.setEnabled(false);
        saveMacroButton.setEnabled(false);

        updateRecordState();
    }

    public void endRecording() {
        _isRecording = false;
        macroNameField.setEnabled(true);
        saveMacroButton.setEnabled(true);

        updateRecordState();
    }

    public void willSaveRecording() {

    }

    public void displayError(@NotNull String title, @NotNull String message) {
        AlertWindows.showErrorMessage(getParent(), title, message, "Ok");
    }

    public void setHotkeyText(@NotNull String start, @NotNull String stop) {
        startHotkey = start;
        stopHotkey = stop;

        updateRecordState();
    }

    public void scrollToBottom() {
        var sb = macrosList.getVerticalScrollBar();
        sb.setValue(sb.getMaximum());
    }

    // # Private
    private void updateRecordState() {
        if (!_isRecording) {
            macroStateLabel.setText(TextValue.getText(TextValue.Record_IdleStatus, startHotkey));
            
            if (_recordStartCount == 0) {
                recordMacroButton.setText(TextValue.getText(TextValue.Record_BeginRecordingButtonTitle));
            } else {
                recordMacroButton.setText(TextValue.getText(TextValue.Record_RedoRecordingButtonTitle));
            }

            recordMacroButton.setToolTipText(TextValue.getText(TextValue.Record_BeginRecordingButtonTip));
        } else {
            macroStateLabel.setText(TextValue.getText(TextValue.Record_RecordStatus, stopHotkey));
            
            recordMacroButton.setText(TextValue.getText(TextValue.Record_StopRecordingButtonTitle));
            recordMacroButton.setToolTipText(TextValue.getText(TextValue.Record_StopRecordingButtonTip));
        }
    }

    // Private properties
    private boolean _isRecording = false;
    private int _recordStartCount = 0;
    private @NotNull String startHotkey = Strings.DEFAULT_PLAY_OR_STOP_HOTKEY;
    private @NotNull String stopHotkey = Strings.DEFAULT_PLAY_OR_STOP_HOTKEY;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        macroNameLabel = new javax.swing.JLabel();
        descriptionMacroLabel = new javax.swing.JLabel();
        macroNameField = new javax.swing.JTextField();
        macroDescriptionField = new javax.swing.JTextField();
        macrosList = new javax.swing.JScrollPane();
        macroActionsList = new javax.swing.JList<>();
        saveMacroButton = new javax.swing.JButton();
        recordMacroButton = new javax.swing.JButton();
        macroStateLabel = new javax.swing.JLabel();
        macroActionsListName = new javax.swing.JLabel();

        jFrame1.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jFrame1.setResizable(false);

        macroNameLabel.setText("Name");

        descriptionMacroLabel.setText("Description");

        macroNameField.setText("Name");

        macroActionsList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        macrosList.setViewportView(macroActionsList);

        saveMacroButton.setText("Save");
        saveMacroButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMacroButtonActionPerformed(evt);
            }
        });

        recordMacroButton.setText("Record");
        recordMacroButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recordMacroButtonActionPerformed(evt);
            }
        });

        macroStateLabel.setText("Idle (Press F4 to RECORD/FINISH)");

        macroActionsListName.setText("Actions");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(macrosList)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(macroNameLabel)
                            .addComponent(descriptionMacroLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(macroNameField)
                            .addComponent(macroDescriptionField, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(macroStateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(recordMacroButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveMacroButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(macroActionsListName)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(macroNameLabel)
                    .addComponent(macroNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptionMacroLabel)
                    .addComponent(macroDescriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(macroActionsListName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(macrosList, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveMacroButton)
                    .addComponent(recordMacroButton)
                    .addComponent(macroStateLabel))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void recordMacroButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordMacroButtonActionPerformed
        if (!isRecording()) {
            beginRecording();
            onBeginRecordMacroButtonCallback.perform();
        } else {
            endRecording();
            onStopRecordMacroButtonCallback.perform();
        }
    }//GEN-LAST:event_recordMacroButtonActionPerformed

    private void saveMacroButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMacroButtonActionPerformed
        onSaveMacroButtonCallback.perform(macroNameField.getText());
    }//GEN-LAST:event_saveMacroButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel descriptionMacroLabel;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JList<String> macroActionsList;
    private javax.swing.JLabel macroActionsListName;
    private javax.swing.JTextField macroDescriptionField;
    private javax.swing.JTextField macroNameField;
    private javax.swing.JLabel macroNameLabel;
    private javax.swing.JLabel macroStateLabel;
    private javax.swing.JScrollPane macrosList;
    private javax.swing.JButton recordMacroButton;
    private javax.swing.JButton saveMacroButton;
    // End of variables declaration//GEN-END:variables
}
