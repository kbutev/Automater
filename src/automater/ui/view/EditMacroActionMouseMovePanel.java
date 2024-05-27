/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.ui.view;

import automater.utilities.Callback;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Byti
 */
public class EditMacroActionMouseMovePanel extends javax.swing.JPanel {
    // UI callbacks
    public Callback<String> onXValueChangedCallback = Callback.createDoNothing();
    public Callback<String> onYValueChangedCallback = Callback.createDoNothing();

    /**
     * Creates new form EditMacroActionMouseMovePanel
     */
    public EditMacroActionMouseMovePanel() {
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

        xLabel = new javax.swing.JLabel();
        xField = new javax.swing.JTextField();
        yLabel = new javax.swing.JLabel();
        yField = new javax.swing.JTextField();

        xLabel.setText("X");

        xField.setText("0");

        yLabel.setText("Y");

        yField.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(yLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(xLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(xField, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(yField))
                .addContainerGap(314, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xLabel)
                    .addComponent(xField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void setup()
    {
        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onXValueChangedCallback.perform(xField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onXValueChangedCallback.perform(xField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onXValueChangedCallback.perform(xField.getText());
            }
        };
        
        xField.getDocument().addDocumentListener(listener);
        
        listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onYValueChangedCallback.perform(yField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onYValueChangedCallback.perform(yField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onYValueChangedCallback.perform(yField.getText());
            }
        };
        
        yField.getDocument().addDocumentListener(listener);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextField xField;
    public javax.swing.JLabel xLabel;
    public javax.swing.JTextField yField;
    public javax.swing.JLabel yLabel;
    // End of variables declaration//GEN-END:variables
}
