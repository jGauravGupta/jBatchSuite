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
package org.netbeans.jbatch.modeler.source.generator.adaptor.orm;

import org.netbeans.api.project.Project;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.jbatch.code.generator.job.src.JobSourceGenerator;
import org.netbeans.jbatch.code.generator.job.xml.JobXMLGenerator;
import org.netbeans.jbatch.modeler.spec.core.Definitions;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.task.ITaskSupervisor;

public class Job2Java {

    private Definitions definitions = null;
    private ModelerFile modelerFile;
    private ITaskSupervisor task;
    private Project project;
    private SourceGroup sourceGroup;

    public void generateSource(ITaskSupervisor task, Project project, SourceGroup sourceGroup, ModelerFile modelerFile) {
        this.task = task;
        this.project = project;
        this.sourceGroup = sourceGroup;
        this.modelerFile = modelerFile;
        this.definitions = (Definitions) modelerFile.getDefinitionElement();

        generateJobSource();
        generateJobXML();
    }

    private void generateJobXML() {
        JobXMLGenerator jobXMLGenerator = new JobXMLGenerator();
        jobXMLGenerator.generateJobXML(task, project, sourceGroup, modelerFile, definitions.getName(), definitions.getJob());

    }

    private void generateJobSource() {
        JobSourceGenerator jobSourceGenerator = new JobSourceGenerator();
        jobSourceGenerator.generateSource(task, project, sourceGroup, modelerFile, definitions.getPackage(), definitions.getJob());

    }
}
