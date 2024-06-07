/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.ui.view;

import automater.utilities.Callback;
import automater.utilities.Description;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Byti
 */
public class EditMacroActionMouseKeyPanel extends javax.swing.JPanel {

    // UI callbacks
    public Callback<String> onSelectedValueCallback = Callback.createDoNothing();
    public Callback<Boolean> onPressCheckCallback = Callback.createDoNothing();

    /**
     * Creates new form EditMacroActionSpecificValuesKey
     */
    public EditMacroActionMouseKeyPanel() {
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

        mouseKeysDrop = new javax.swing.JComboBox<>();
        pressCheck = new javax.swing.JCheckBox();
        keyLabel = new javax.swing.JLabel();

        mouseKeysDrop.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        mouseKeysDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mouseKeysDropActionPerformed(evt);
            }
        });

        pressCheck.setText("Press");
        pressCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pressCheckActionPerformed(evt);
            }
        });

        keyLabel.setText("Key");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(keyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(mouseKeysDrop, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pressCheck))
                .addContainerGap(231, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(keyLabel)
                    .addComponent(mouseKeysDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pressCheck)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void pressCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pressCheckActionPerformed
        onPressCheckCallback.perform(pressCheck.isSelected());
    }//GEN-LAST:event_pressCheckActionPerformed

    private void mouseKeysDropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mouseKeysDropActionPerformed
        int selectedIndex = mouseKeysDrop.getSelectedIndex();

        if (selectedIndex == -1) {
            return;
        }

        String value = _model.dataSource.getElementAt(selectedIndex);
        onSelectedValueCallback.perform(value);
    }//GEN-LAST:event_mouseKeysDropActionPerformed

    private void setup() {

    }

    public void setSpecificValues(@NotNull List<String> values) {
        _model = new EditMacroActionMouseKeyPressModel(values);

        mouseKeysDrop.setModel(_model);
    }

    public void selectSpecificValue(@NotNull String value) {
        if (_model == null) {
            return;
        }

        int index = _model.dataSource.indexOf(value);

        if (index != -1) {
            mouseKeysDrop.setSelectedIndex(index);
        }
    }

    // # Private
    private EditMacroActionMouseKeyPressModel _model;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel keyLabel;
    public javax.swing.JComboBox<String> mouseKeysDrop;
    public javax.swing.JCheckBox pressCheck;
    // End of variables declaration//GEN-END:variables
}

class EditMacroActionMouseKeyPressModel implements ComboBoxModel {

    @NotNull public final StandardDescriptionDataSource dataSource;

    private int _selectedIndex = 0;

    public EditMacroActionMouseKeyPressModel(@NotNull List<String> values) {
        this.dataSource = parseValues(values);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        int index = dataSource.indexOf((String) anItem);
        _selectedIndex = index;
    }

    @Override
    public Object getSelectedItem() {
        if (_selectedIndex == -1) {
            return null;
        }

        return dataSource.getElementAt(_selectedIndex);
    }

    @Override
    public int getSize() {
        return dataSource.getSize();
    }

    @Override
    public Object getElementAt(int index) {
        return dataSource.getElementAt(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }

    public static @NotNull StandardDescriptionDataSource parseValues(@NotNull List<String> values) {
        ArrayList<Description> actions = new ArrayList<>();

        for (int e = 0; e < values.size(); e++) {
            actions.add(Description.createFromString(values.get(e)));
        }

        return StandardDescriptionDataSource.createDataSource(values);
    }
}
