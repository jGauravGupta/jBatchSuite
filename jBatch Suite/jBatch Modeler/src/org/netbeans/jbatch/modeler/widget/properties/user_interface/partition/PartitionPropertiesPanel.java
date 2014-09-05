/**
 * Copyright [2014] Gaurav Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.netbeans.jbatch.modeler.widget.properties.user_interface.partition;

import org.netbeans.jbatch.modeler.spec.Properties;
import org.netbeans.jbatch.modeler.spec.Property;
import org.netbeans.jbatch.modeler.specification.model.job.util.JobUtil;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.entity.Entity;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.entity.RowValue;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.internal.EntityComponent;
import org.netbeans.modeler.properties.nentity.NAttributeEntity;

public class PartitionPropertiesPanel extends EntityComponent<Properties> {

    private Properties properties;
    private NAttributeEntity attributeEntity;

    public PartitionPropertiesPanel() {
        super("", true);
        initComponents();
    }

    @Override
    public void init() {
      
    }

    @Override
    public void createEntity(Class<? extends Entity> entityWrapperType) {
        this.setTitle("Create new Partiton");
        if (entityWrapperType == RowValue.class) {
            this.setEntity(new RowValue(new Object[4]));
        }
        properties = new Properties();

        initCustomNAttributeEditor();
        attributeEntity = JobUtil.addProperty(properties);
        customNAttributeClientEditor.setAttributeEntity(attributeEntity);
    }

    @Override
    public void updateEntity(Entity<Properties> entityValue) {
        this.setTitle("Update Partiton Property");
        if (entityValue.getClass() == RowValue.class) {
            this.setEntity(entityValue);
            Object[] row = ((RowValue) entityValue).getRow();
            properties = (Properties) row[0];
        }

        initCustomNAttributeEditor();
        attributeEntity = JobUtil.addProperty(properties);//getQueryHint();
        customNAttributeClientEditor.setAttributeEntity(attributeEntity);
    }


    void initCustomNAttributeEditor() {
        properties_LayeredPane.removeAll();
        customNAttributeClientEditor = new org.netbeans.modeler.properties.nentity.NEntityEditor();
        javax.swing.GroupLayout queryHint_LayeredPaneLayout = new javax.swing.GroupLayout(properties_LayeredPane);
        properties_LayeredPane.setLayout(queryHint_LayeredPaneLayout);
        queryHint_LayeredPaneLayout.setHorizontalGroup(
                queryHint_LayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(customNAttributeClientEditor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
        );
        queryHint_LayeredPaneLayout.setVerticalGroup(
                queryHint_LayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(queryHint_LayeredPaneLayout.createSequentialGroup()
                        .addComponent(customNAttributeClientEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
        );
        properties_LayeredPane.setLayer(customNAttributeClientEditor, javax.swing.JLayeredPane.DEFAULT_LAYER);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        root_jLayeredPane = new javax.swing.JLayeredPane();
        action_jLayeredPane = new javax.swing.JLayeredPane();
        save_Button = new javax.swing.JButton();
        cancel_Button = new javax.swing.JButton();
        properties_LayeredPane = new javax.swing.JLayeredPane();
        customNAttributeClientEditor = new org.netbeans.modeler.properties.nentity.NEntityEditor();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        org.openide.awt.Mnemonics.setLocalizedText(save_Button, org.openide.util.NbBundle.getMessage(PartitionPropertiesPanel.class, "PartitionPropertiesPanel.save_Button.text")); // NOI18N
        save_Button.setToolTipText(org.openide.util.NbBundle.getMessage(PartitionPropertiesPanel.class, "PartitionPropertiesPanel.save_Button.toolTipText")); // NOI18N
        save_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save_ButtonActionPerformed(evt);
            }
        });
        action_jLayeredPane.add(save_Button);
        save_Button.setBounds(0, 0, 70, 23);

        org.openide.awt.Mnemonics.setLocalizedText(cancel_Button, org.openide.util.NbBundle.getMessage(PartitionPropertiesPanel.class, "PartitionPropertiesPanel.cancel_Button.text")); // NOI18N
        cancel_Button.setToolTipText(org.openide.util.NbBundle.getMessage(PartitionPropertiesPanel.class, "PartitionPropertiesPanel.cancel_Button.toolTipText")); // NOI18N
        cancel_Button.setPreferredSize(new java.awt.Dimension(60, 23));
        cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel_ButtonActionPerformed(evt);
            }
        });
        action_jLayeredPane.add(cancel_Button);
        cancel_Button.setBounds(80, 0, 70, 23);

        properties_LayeredPane.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(PartitionPropertiesPanel.class, "PartitionPropertiesPanel.properties_LayeredPane.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12), new java.awt.Color(102, 102, 102))); // NOI18N
        properties_LayeredPane.setPreferredSize(new java.awt.Dimension(460, 30));

        javax.swing.GroupLayout properties_LayeredPaneLayout = new javax.swing.GroupLayout(properties_LayeredPane);
        properties_LayeredPane.setLayout(properties_LayeredPaneLayout);
        properties_LayeredPaneLayout.setHorizontalGroup(
            properties_LayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(customNAttributeClientEditor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );
        properties_LayeredPaneLayout.setVerticalGroup(
            properties_LayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(properties_LayeredPaneLayout.createSequentialGroup()
                .addComponent(customNAttributeClientEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 28, Short.MAX_VALUE))
        );
        properties_LayeredPane.setLayer(customNAttributeClientEditor, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout root_jLayeredPaneLayout = new javax.swing.GroupLayout(root_jLayeredPane);
        root_jLayeredPane.setLayout(root_jLayeredPaneLayout);
        root_jLayeredPaneLayout.setHorizontalGroup(
            root_jLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, root_jLayeredPaneLayout.createSequentialGroup()
                .addContainerGap(364, Short.MAX_VALUE)
                .addComponent(action_jLayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
            .addGroup(root_jLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(root_jLayeredPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(properties_LayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(18, Short.MAX_VALUE)))
        );
        root_jLayeredPaneLayout.setVerticalGroup(
            root_jLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, root_jLayeredPaneLayout.createSequentialGroup()
                .addContainerGap(275, Short.MAX_VALUE)
                .addComponent(action_jLayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(root_jLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(root_jLayeredPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(properties_LayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(49, Short.MAX_VALUE)))
        );
        root_jLayeredPane.setLayer(action_jLayeredPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        root_jLayeredPane.setLayer(properties_LayeredPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(root_jLayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(root_jLayeredPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel_ButtonActionPerformed
        cancelActionPerformed(evt);
    }//GEN-LAST:event_cancel_ButtonActionPerformed

    private void save_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save_ButtonActionPerformed
        if (!validateField()) {
            return;
        }
         
//        if (this.getEntity().getClass() == RowValue.class) {
//            Object[] row = ((RowValue) this.getEntity()).getRow();
//            if (row[0] == null) {
//                properties = new Properties();
//            } else {
//                properties = (Properties) row[0];
//            }
//        }

        if (this.getEntity().getClass() == RowValue.class) {
            Object[] row = ((RowValue) this.getEntity()).getRow();
            row[0] = properties;
            
            StringBuilder text = new StringBuilder();
            for(Property property : properties.getProperty()){
               text.append(property.getName()).append(",");
            }
            if(text.length()>0){
               text.setLength(text.length() - 1);
                    }
            row[2] = text.toString();
            
//            int size = properties.getProperty().size();
//            if(size == 0){
//                row[2] = "No Properties Exist";
//            } else if(size == 1){
//                row[2] = "One Property Exist";
//            } else if(size > 1){
//                row[2] = size + " Properties Exist";
//            } 
            
        }
        attributeEntity.getTableDataListener().setData(customNAttributeClientEditor.getSavedModel());
       saveActionPerformed(evt);
    }//GEN-LAST:event_save_ButtonActionPerformed

    private boolean validateField() {
        return true;
    }

     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane action_jLayeredPane;
    private javax.swing.JButton cancel_Button;
    private org.netbeans.modeler.properties.nentity.NEntityEditor customNAttributeClientEditor;
    private javax.swing.JLayeredPane properties_LayeredPane;
    private javax.swing.JLayeredPane root_jLayeredPane;
    private javax.swing.JButton save_Button;
    // End of variables declaration//GEN-END:variables
}