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

import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import org.openide.loaders.TemplateWizard;

public abstract class WizardPanel extends JPanel {

    private TemplateWizard wizard;

    public WizardPanel(TemplateWizard wizard) {
        this.wizard = wizard;
    }

    public abstract void store(TemplateWizard wizard);

    public abstract boolean validateTemplate(TemplateWizard wizard);

    public abstract void addChangeListener(ChangeListener l);

    public abstract void removeChangeListener(ChangeListener l);

    /**
     * @return the wizard
     */
    public TemplateWizard getWizard() {
        return wizard;
    }

    /**
     * @param wizard the wizard to set
     */
    public void setWizard(TemplateWizard wizard) {
        this.wizard = wizard;
    }

}
