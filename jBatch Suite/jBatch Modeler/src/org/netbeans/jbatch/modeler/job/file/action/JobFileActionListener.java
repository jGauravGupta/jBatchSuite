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
package org.netbeans.jbatch.modeler.job.file.action;

import org.netbeans.jbatch.modeler.job.file.JobFileDataObject;
import org.netbeans.jbatch.modeler.specification.JavaBatchSpecification;
import org.netbeans.jbatch.modeler.specification.component.job.JobComponent;
import org.netbeans.jbatch.modeler.specification.model.job.JobDiagramModel;
import org.netbeans.jbatch.modeler.specification.model.job.engine.JobDiagramEngine;
import org.netbeans.jbatch.modeler.specification.model.job.scene.JobScene;
import org.netbeans.jbatch.modeler.specification.model.job.util.JobUtil;
import org.netbeans.jbatch.modeler.widget.connection.relation.RelationValidator;
import org.netbeans.modeler.component.IModelerPanel;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.specification.annotaton.DiagramModel;
import org.netbeans.modeler.specification.annotaton.ModelerConfig;
import org.netbeans.modeler.specification.annotaton.Vendor;
import org.netbeans.modeler.specification.model.ModelerSpecificationDiagramModel;
import org.netbeans.modeler.specification.model.file.action.ModelerFileActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Build", id = "JobFileActionListener")
@ActionRegistration(displayName = "#CTL_JobFileActionListener") //Loaders/text/job+xml/Actions
@ActionReference(path = "Loaders/text/job+xml/Actions", position = 0, separatorAfter = +50)
@Messages("CTL_JobFileActionListener=Edit in Modeler")
@ModelerConfig(palette = "org/netbeans/jbatch/modeler/resource/document/JobPaletteConfig.xml",
        document = "org/netbeans/jbatch/modeler/resource/document/JobDocumentConfig.xml",
        element = "org/netbeans/jbatch/modeler/resource/document/ElementConfig.xml")
@Vendor(id = "jBatch", version = 1.0F, name = "jBatch", displayName = "Java Batch Application 1.0")
@DiagramModel(id = "Job", name = "Job")
public class JobFileActionListener extends ModelerFileActionListener {

    private String definitionId, definitionName, definitionTooltip;

    public JobFileActionListener(JobFileDataObject context) {
        super(context);
    }

    @Override
    public void initSpecification(ModelerFile modelerFile) {
        modelerFile.addAttribute("definitionId", this.getDefinitionId());
        if (this.getDefinitionName() != null) {
            modelerFile.setName(this.getDefinitionName());
            modelerFile.setTooltip(this.getDefinitionTooltip());
        }
        modelerFile.setModelerVendorSpecification(new JavaBatchSpecification());
        ModelerSpecificationDiagramModel diagramModel = new JobDiagramModel();
        modelerFile.getVendorSpecification().setModelerSpecificationDiagramModel(diagramModel);

        diagramModel.setModelerUtil(new JobUtil());
        diagramModel.setModelerDiagramEngine(new JobDiagramEngine());
        diagramModel.setModelerScene(new JobScene());
        diagramModel.setModelerPanelTopComponent((IModelerPanel) new JobComponent());
        diagramModel.setRelationValidator(new RelationValidator());

    }

    /**
     * @return the definitionId
     */
    public String getDefinitionId() {
        return definitionId;
    }

    /**
     * @param definitionId the definitionId to set
     */
    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    /**
     * @return the definitionName
     */
    public String getDefinitionName() {
        return definitionName;
    }

    /**
     * @param definitionName the definitionName to set
     */
    public void setDefinitionName(String definitionName) {
        this.definitionName = definitionName;
    }

    /**
     * @return the definitionTooltip
     */
    public String getDefinitionTooltip() {
        return definitionTooltip;
    }

    /**
     * @param definitionTooltip the definitionTooltip to set
     */
    public void setDefinitionTooltip(String definitionTooltip) {
        this.definitionTooltip = definitionTooltip;
    }

}
