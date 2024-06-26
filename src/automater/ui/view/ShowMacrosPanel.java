/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.ui.view;

import automater.datasource.StandardDescriptionDataSource;
import automater.utilities.Callback;
import java.awt.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public class ShowMacrosPanel extends javax.swing.JPanel implements View {
    
    // UI callbacks
    public Callback.Param<Integer> onSelectItem = Callback.buildBlankWithParameter();
    public Callback.Param<Integer> onClickItem = Callback.buildBlankWithParameter();
    public Callback.Param<Integer> onDoubleClickItem = Callback.buildBlankWithParameter();
    public Callback.Param<Integer> onOpenItem = Callback.buildBlankWithParameter();
    public Callback.Param<Integer> onEditItem = Callback.buildBlankWithParameter();
    public Callback.Param<Integer> onDeleteItem = Callback.buildBlankWithParameter();
    
    /**
     * Creates new form ShowMacrosPanel
     */
    public ShowMacrosPanel() {
        initComponents();
        setup();
    }
    
    private void setup() {
        macrosList.addListSelectionListener((var selection) -> {
            onSelectItem(selection.getFirstIndex());
        });
    }
    
    // - View
    
    @Override
    public @NotNull Component asComponent() {
        return this;
    }
    
    @Override
    public void reloadData() {

    }
    
    // # Public
    public void setListDataSource(@NotNull StandardDescriptionDataSource dataSource) {
        _dataSource = dataSource;

        macrosList.setModel(_dataSource);

        macrosList.clearSelection();
        disableMacroFunctionality();
    }

    public int getSelectionIndex() {
        return _selectedIndex;
    }

    // # Private
    
    private void onSelectItem(int index) {
        selectMacroAt(index);
    }
    
    private void selectMacroAt(int index) {
        if (_selectedIndex == index) {
            return;
        }

        _selectedIndex = index;

        if (index != -1) {
            enableMacroFunctionality();
            updateSelectedMacroInfo(index);
        } else {
            disableMacroFunctionality();
        }

        onSelectItem.perform(_selectedIndex);
    }

    private void enableMacroFunctionality() {
        openMacroButton.setEnabled(true);
        editMacroButton.setEnabled(true);
        deleteMacroButton.setEnabled(true);
    }

    private void disableMacroFunctionality() {
        openMacroButton.setEnabled(false);
        editMacroButton.setEnabled(false);
        deleteMacroButton.setEnabled(false);

        macroDescriptionLabel.setText("");
    }

    private void updateSelectedMacroInfo(int index) {
        
    }

    // - Private
    
    private int _selectedIndex = -1;
    private @Nullable StandardDescriptionDataSource _dataSource;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        macrosList = new javax.swing.JList<>();
        openMacroButton = new javax.swing.JButton();
        editMacroButton = new javax.swing.JButton();
        deleteMacroButton = new javax.swing.JButton();
        macroDescriptionLabel = new javax.swing.JLabel();

        macrosList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        macrosList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                macrosListMouseClicked(evt);
            }
        });
        macrosList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                macrosListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(macrosList);

        openMacroButton.setText("Open");
        openMacroButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMacroButtonActionPerformed(evt);
            }
        });

        editMacroButton.setText("Edit");
        editMacroButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editMacroButtonActionPerformed(evt);
            }
        });

        deleteMacroButton.setText("Delete");
        deleteMacroButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMacroButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(macroDescriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(openMacroButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editMacroButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteMacroButton)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(macroDescriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(openMacroButton)
                    .addComponent(editMacroButton)
                    .addComponent(deleteMacroButton))
                .addContainerGap(12, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void openMacroButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMacroButtonActionPerformed
        onOpenItem.perform(getSelectionIndex());
    }//GEN-LAST:event_openMacroButtonActionPerformed

    private void editMacroButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editMacroButtonActionPerformed
        onEditItem.perform(getSelectionIndex());
    }//GEN-LAST:event_editMacroButtonActionPerformed

    private void deleteMacroButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMacroButtonActionPerformed
        onDeleteItem.perform(getSelectionIndex());
    }//GEN-LAST:event_deleteMacroButtonActionPerformed

    private void macrosListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_macrosListMouseClicked
        selectMacroAt(macrosList.getSelectedIndex());
        
        if (evt.getClickCount() > 1) {
            onOpenItem.perform(getSelectionIndex());
        }
    }//GEN-LAST:event_macrosListMouseClicked

    private void macrosListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_macrosListValueChanged
        selectMacroAt(macrosList.getSelectedIndex());
    }//GEN-LAST:event_macrosListValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteMacroButton;
    private javax.swing.JButton editMacroButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel macroDescriptionLabel;
    private javax.swing.JList<String> macrosList;
    private javax.swing.JButton openMacroButton;
    // End of variables declaration//GEN-END:variables
}
