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
package org.netbeans.jbatch.code.templates.wizard;

import java.awt.Component;
import java.lang.reflect.Method;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardDescriptor.Panel;
import org.openide.loaders.TemplateWizard;
import org.openide.util.HelpCtx;

public class ConstraintPanel implements WizardDescriptor.Panel<WizardDescriptor>, WizardDescriptor.FinishablePanel<WizardDescriptor>, ChangeListener {

//    private ConstraintPanelVisual component;
    private TemplateWizard wizard;
    private WizardPanel panel;

    public ConstraintPanel(TemplateWizard wizard , WizardPanel panel) {
        this.wizard = wizard;
        this.panel=panel;
    }

    @Override
    public Component getComponent() {
//        if (component == null) {
//            component = new ConstraintPanelVisual(wizard);
//        }
        return panel;
    }

    @Override
    public HelpCtx getHelp() {
        return new HelpCtx(ConstraintPanel.class);
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        wizard = (TemplateWizard) settings;
    }

    @Override
    public void storeSettings(WizardDescriptor d) {
        panel.store((TemplateWizard) d);
    }

    @Override
    public boolean isValid() {
        return panel.validateTemplate(wizard);
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        getComponent();
        panel.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        getComponent();
        panel.removeChangeListener(l);
    }

    @Override
    public boolean isFinishPanel() {
        return true;
    }

    private String oldTargetName;

    @Override
    public void stateChanged(ChangeEvent e) {
        WizardDescriptor.Panel panel = (Panel) e.getSource();
        String targetName = null;
        Component gui = panel.getComponent();
        try {
            // XXX JavaTargetChooserPanel should introduce new API to get current contents
            // of its component JavaTargetChooserPanelGUI (see Issue#154655)
            Method getTargetName = gui.getClass().getMethod("getTargetName", (Class[]) null); // NOI18N
            targetName = (String) getTargetName.invoke(gui, (Object[]) null);
        } catch (Exception ex) {
            return;
        }

        if ((targetName == null) || targetName.trim().equals("") || targetName.trim().equals(oldTargetName)) {
            return;
        }
        oldTargetName = targetName;
    }

}
