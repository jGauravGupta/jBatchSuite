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
package org.netbeans.jbatch.code.templates.listener;

import org.netbeans.jbatch.code.templates.wizard.ConstraintIterator;
import org.netbeans.jbatch.code.templates.wizard.WizardPanel;
import org.openide.loaders.TemplateWizard;

public class BatchListenerIterator extends ConstraintIterator {

    @Override
    public WizardPanel getWizardPanel(TemplateWizard wizard) {
        return new ListenerPanel(wizard);
    }

}
