/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.ui.view;

import automater.datasource.MacroActionDataSource;
import automater.utilities.Callback;
import java.awt.Component;
import javax.swing.JFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public class EditMacroFrame extends javax.swing.JFrame implements View {

    // UI callbacks
    public Callback.Blank onSave = Callback.buildBlank();
    public Callback.Param<Integer> onInsertItem = Callback.buildBlankWithParameter();
    public Callback.Param<Integer> onEditItem = Callback.buildBlankWithParameter();
    public Callback.Param<Integer> onCopyItem = Callback.buildBlankWithParameter();
    public Callback.Param<Integer> onDeleteItem = Callback.buildBlankWithParameter();
    public Callback.Blank onWindowClosed = Callback.buildBlank();
    
    /**
     * Creates new form EditMacroFrame
     */
    public EditMacroFrame() {
        initComponents();
        setup();
    }
    
    private void setup() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        macroActionsList.addListSelectionListener((var selection) -> {
            onSelectItem(selection.getFirstIndex());
        });
    }
    
    // # Public
    
    public int getSelectedIndex() {
        return macroActionsList.getSelectedIndex();
    }
    
    public void present() {
        setVisible(true);
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
        updateButtonsState();
    }
    
    public void updateButtonsState() {
        var isSomethingSelected = getSelectedIndex() != -1;
        
        insertButton.setEnabled(isSomethingSelected);
        editButton.setEnabled(isSomethingSelected);
        copyButton.setEnabled(isSomethingSelected);
        deleteButton.setEnabled(isSomethingSelected);
    }
    
    public void enableSaveButton(boolean enabled) {
        saveButton.setEnabled(enabled);
    }
    
    // # View
    
    @Override
    public Component asComponent() {
        return this;
    }
    
    @Override
    public void reloadData() {
        
    }
    
    private void onSelectItem(int index) {
        updateButtonsState();
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
        saveButton = new javax.swing.JButton();
        insertButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        copyButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Edit macro");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        macroNameLabel.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        macroNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        macroNameLabel.setText("MACRO");

        macroActionsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        macroActionsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                macroActionsListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(macroActionsList);

        saveButton.setText("Save Macro");
        saveButton.setEnabled(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        insertButton.setText("Insert");
        insertButton.setEnabled(false);
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });

        editButton.setText("Edit");
        editButton.setEnabled(false);
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        copyButton.setText("Copy");
        copyButton.setEnabled(false);
        copyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(macroNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(insertButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(copyButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(macroNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(insertButton)
                    .addComponent(deleteButton)
                    .addComponent(copyButton)
                    .addComponent(editButton))
                .addGap(11, 11, 11))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void macroActionsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_macroActionsListMouseClicked
        updateButtonsState();
        
        if (evt.getClickCount() > 1 && getSelectedIndex() != -1) {
            onEditItem.perform(getSelectedIndex());
        }
    }//GEN-LAST:event_macroActionsListMouseClicked

    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertButtonActionPerformed
        onInsertItem.perform(getSelectedIndex());
    }//GEN-LAST:event_insertButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        onEditItem.perform(getSelectedIndex());
    }//GEN-LAST:event_editButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        onDeleteItem.perform(getSelectedIndex());
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        onSave.perform();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        onWindowClosed.perform();
    }//GEN-LAST:event_formWindowClosed

    private void copyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyButtonActionPerformed
        onCopyItem.perform(getSelectedIndex());
    }//GEN-LAST:event_copyButtonActionPerformed

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
            java.util.logging.Logger.getLogger(EditMacroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditMacroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditMacroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditMacroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new EditMacroFrame().setVisible(true);
        });
    }
    
    private @Nullable MacroActionDataSource dataSource;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton copyButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JButton insertButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> macroActionsList;
    private javax.swing.JLabel macroNameLabel;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}
