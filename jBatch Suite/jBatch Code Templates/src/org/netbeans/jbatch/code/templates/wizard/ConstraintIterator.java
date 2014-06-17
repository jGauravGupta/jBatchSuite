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
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.spi.java.project.support.ui.templates.JavaTemplates;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.WizardDescriptor.Panel;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.TemplateWizard;
import org.openide.util.NbBundle;

public abstract class ConstraintIterator implements TemplateWizard.Iterator {

    private transient WizardDescriptor.Panel<WizardDescriptor>[] panels;
    private int index;

    @Override
    public Set<DataObject> instantiate(TemplateWizard wizard) throws IOException {
        FileObject dir = Templates.getTargetFolder(wizard);
        DataFolder df = DataFolder.findFolder(dir);
        FileObject template = Templates.getTemplate(wizard);
        DataObject dTemplate = DataObject.find(template);
        HashMap<String, Object> templateProperties = new HashMap<String, Object>();
        templateProperties.put(WizardProperties.NAMED, wizard.getProperty(WizardProperties.NAMED));
        templateProperties.put(WizardProperties.PROCESS, wizard.getProperty(WizardProperties.PROCESS));
        templateProperties.put(WizardProperties.READ, wizard.getProperty(WizardProperties.READ));
        templateProperties.put(WizardProperties.WRITE, wizard.getProperty(WizardProperties.WRITE));
        templateProperties.put(WizardProperties.MAPPER, wizard.getProperty(WizardProperties.MAPPER));
        templateProperties.put(WizardProperties.REDUCER, wizard.getProperty(WizardProperties.REDUCER));
        templateProperties.put(WizardProperties.COLLECTOR, wizard.getProperty(WizardProperties.COLLECTOR));
        templateProperties.put(WizardProperties.ANALYZER, wizard.getProperty(WizardProperties.ANALYZER));
        templateProperties.put(WizardProperties.LISTENER, wizard.getProperty(WizardProperties.LISTENER));
        
        DataObject dobj = null;
        String constraintClass = wizard.getTargetName();
        dobj = dTemplate.createFromTemplate(df, constraintClass, templateProperties);
        return Collections.singleton(dobj);
    }

    public abstract WizardPanel getWizardPanel(TemplateWizard wizard);

    @Override
    public void initialize(TemplateWizard wizard) {
        index = 0;

        Project project = Templates.getProject(wizard);
        final WizardDescriptor.Panel<WizardDescriptor> constraintPanel = new ConstraintPanel(wizard, getWizardPanel(wizard));
        SourceGroup[] sourceGroup = ProjectUtils.getSources(project).getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);
        WizardDescriptor.Panel javaPanel;
        if (sourceGroup.length == 0) {
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, NbBundle.getMessage(ConstraintIterator.class, "MSG_No_Sources_found"));
            javaPanel = constraintPanel;
        } else {
            javaPanel = JavaTemplates.createPackageChooser(project, sourceGroup, constraintPanel, true);
            javaPanel.addChangeListener((ConstraintPanel) constraintPanel);
        }
        panels = new WizardDescriptor.Panel[]{javaPanel};

        // Creating steps.
        Object prop = wizard.getProperty(WizardDescriptor.PROP_CONTENT_DATA); // NOI18N
        String[] beforeSteps = null;
        if (prop != null && prop instanceof String[]) {
            beforeSteps = (String[]) prop;
        }
        String[] steps = createSteps(beforeSteps, panels);

        for (int i = 0; i < panels.length; i++) {
            Component c = panels[i].getComponent();
            if (steps[i] == null) {
                // Default step name to component name of panel.
                // Mainly useful for getting the name of the target
                // chooser to appear in the list of steps.
                steps[i] = c.getName();
            }
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                // Step #.
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, new Integer(i));
                // Step name (actually the whole list for reference).
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
            }
        }
    }

    private String[] createSteps(String[] before, WizardDescriptor.Panel[] panels) {
        int diff = 0;
        if (before == null) {
            before = new String[0];
        } else if (before.length > 0) {
            diff = ("...".equals(before[before.length - 1])) ? 1 : 0; // NOI18N
        }
        String[] res = new String[(before.length - diff) + panels.length];
        for (int i = 0; i < res.length; i++) {
            if (i < (before.length - diff)) {
                res[i] = before[i];
            } else {
                res[i] = panels[i - before.length + diff].getComponent().getName();
            }
        }
        return res;
    }

    @Override
    public void uninitialize(TemplateWizard wiz) {
        panels = null;
    }

    @Override
    public Panel<WizardDescriptor> current() {
        return panels[index];
    }

    @Override
    public String name() {
        return NbBundle.getMessage(ConstraintIterator.class, "TITLE_x_of_y",
                new Integer(index + 1), new Integer(panels.length));
    }

    @Override
    public boolean hasNext() {
        return index < panels.length - 1;
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    @Override
    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    public static class WizardProperties {

        public static final String NAMED = "named";    //NOI18N
        public static final String READ = "read";    //NOI18N
        public static final String PROCESS = "process";    //NOI18N
        public static final String WRITE = "write";    //NOI18N
        public static final String MAPPER = "mapper";    //NOI18N
        public static final String REDUCER = "reducer";    //NOI18N
        public static final String COLLECTOR = "collector";    //NOI18N
        public static final String ANALYZER = "analyzer";    //NOI18N
        public static final String LISTENER = "listener";    //NOI18N
        
    }
}
