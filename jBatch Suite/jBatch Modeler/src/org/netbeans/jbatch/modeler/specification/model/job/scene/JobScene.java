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
package org.netbeans.jbatch.modeler.specification.model.job.scene;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import org.netbeans.jbatch.modeler.core.widget.ActivityWidget;
import org.netbeans.jbatch.modeler.core.widget.BaseElementWidget;
import org.netbeans.jbatch.modeler.core.widget.BatchletWidget;
import org.netbeans.jbatch.modeler.core.widget.ChunkWidget;
import org.netbeans.jbatch.modeler.core.widget.DecisionGatewayWidget;
import org.netbeans.jbatch.modeler.core.widget.FlowElementWidget;
import org.netbeans.jbatch.modeler.core.widget.FlowNodeWidget;
import org.netbeans.jbatch.modeler.core.widget.GatewayWidget;
import org.netbeans.jbatch.modeler.core.widget.SequenceFlowWidget;
import org.netbeans.jbatch.modeler.core.widget.SplitGatewayWidget;
import org.netbeans.jbatch.modeler.core.widget.StepWidget;
import org.netbeans.jbatch.modeler.spec.Batchlet;
import org.netbeans.jbatch.modeler.spec.Chunk;
import org.netbeans.jbatch.modeler.spec.Decision;
import org.netbeans.jbatch.modeler.spec.ItemProcessor;
import org.netbeans.jbatch.modeler.spec.ItemReader;
import org.netbeans.jbatch.modeler.spec.ItemWriter;
import org.netbeans.jbatch.modeler.spec.Job;
import org.netbeans.jbatch.modeler.spec.Listeners;
import org.netbeans.jbatch.modeler.spec.Properties;
import org.netbeans.jbatch.modeler.spec.Split;
import org.netbeans.jbatch.modeler.spec.Step;
import org.netbeans.jbatch.modeler.spec.core.FlowElement;
import org.netbeans.jbatch.modeler.spec.core.FlowNode;
import org.netbeans.jbatch.modeler.spec.core.SequenceFlow;
import org.netbeans.jbatch.modeler.spec.design.BatchDiagram;
import org.netbeans.jbatch.modeler.specification.model.job.util.JobUtil;
import org.netbeans.jbatch.modeler.generator.ui.GenerateCodeDialog;
import org.netbeans.jbatch.modeler.source.generator.task.SourceCodeGeneratorTask;
import org.netbeans.modeler.config.element.ElementConfigFactory;
import org.netbeans.modeler.core.exception.InvalidElmentException;
import org.netbeans.modeler.core.scene.ModelerScene;
import org.netbeans.modeler.specification.model.document.IDefinitionElement;
import org.netbeans.modeler.specification.model.document.IDiagramElement;
import org.netbeans.modeler.specification.model.document.IRootElement;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowNodeWidget;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.openide.util.RequestProcessor;

public class JobScene extends ModelerScene {

    private List<IFlowNodeWidget> debugNodeWidget = new ArrayList<IFlowNodeWidget>();

    public JobScene() {
        this.addPropertyChangeListener("name", new PropertyChangeListener<String>() {
            @Override
            public void changePerformed(String value) {
                setName(value);
            }
        });

        this.addPropertyChangeListener("id", new PropertyChangeListener<String>() {
            @Override
            public void changePerformed(String value) {
                IDiagramElement diagramElement = JobScene.this.getModelerFile().getDiagramElement();
                ((BatchDiagram) diagramElement).getBatchPlane().setBatchElement(value);
            }
        });
    }
    private List<FlowElementWidget> flowElements = new ArrayList<FlowElementWidget>(); // Linked hashmap to preserve order of inserted elements
//    private List<ArtifactWidget> artifacts = new ArrayList< ArtifactWidget>(); //Artifact_Commneted // Linked hashmap to preserve order of inserted elements

    @Override
    public void createPropertySet(ElementPropertySet set) {

        IRootElement rootElement = this.getModelerFile().getRootElement();
        ElementConfigFactory elementConfigFactory = this.getModelerFile().getVendorSpecification().getElementConfigFactory();
        elementConfigFactory.createPropertySet(set, rootElement, getPropertyChangeListeners());
        IDefinitionElement definitionElement = this.getModelerFile().getDefinitionElement();
        elementConfigFactory.createPropertySet(set, definitionElement, getPropertyChangeListeners());
        Job jobSpec = (Job) rootElement;
        set.put("JOB_PROP", JobUtil.addProperty(this.getModelerFile(), jobSpec.getProperties()));
        set.put("JOB_PROP", JobUtil.addListener(this.getModelerFile(), jobSpec.getListeners()));
    }

    @Override
    public void manageLayerWidget() {
//        for (ArtifactWidget artifact : this.getArtifacts()) {//Artifact_Commneted
//            if (artifact instanceof GroupWidget) {
//                ((GroupWidget) artifact).bringToBack();
//            } else if (artifact instanceof TextAnnotationWidget) {
//                ((TextAnnotationWidget) artifact).bringToFront();
//            }
//        }
        super.manageLayerWidget();
    }

    /**
     * @return the flowElements
     */
    public List<FlowElementWidget> getFlowElements() {
        return flowElements;
    }

    public FlowElementWidget getFlowElement(String id) {
        FlowElementWidget widget = null;
        for (FlowElementWidget flowElementWidget : flowElements) {
            if (flowElementWidget.getId().equals(id)) {
                widget = flowElementWidget;
                break;
            }
        }
        return widget;
    }

//    public ArtifactWidget getArtifact(String id) {//Artifact_Commneted
//        ArtifactWidget widget = null;
//        for (ArtifactWidget artifactWidget : artifacts) {
//            if (artifactWidget.getId().equals(id)) {
//                widget = artifactWidget;
//                break;
//            }
//        }
//        return widget;
//    }
    /**
     * @param flowElements the flowElements to set
     */
    public void setFlowElements(List<FlowElementWidget> flowElements) {
        this.flowElements = flowElements;
    }

    public void removeFlowElement(FlowElementWidget flowElementWidget) {
        this.flowElements.remove(flowElementWidget);
    }

    public void addFlowElement(FlowElementWidget flowElementWidget) {
        this.flowElements.add(flowElementWidget);
    }

    @Override
    public BaseElementWidget findBaseElement(String id) {
        BaseElementWidget widget = null;
        List<BaseElementWidget> baseElementWidgets = new ArrayList<BaseElementWidget>(flowElements);
//        baseElementWidgets.addAll(artifacts);//Artifact_Commneted
        for (BaseElementWidget baseElementWidget : baseElementWidgets) {
            if (baseElementWidget instanceof FlowNodeWidget) {
                if (((FlowNodeWidget) baseElementWidget).getId().equals(id)) {
                    widget = baseElementWidget;
                    break;
                }
//                else if (baseElementWidget instanceof SubProcessWidget) {//Sub_Commented
//                    widget = ((SubProcessWidget) baseElementWidget).findBaseElement(id);
//                    if (widget != null) {
//                        return widget;
//                    }
//                }
            } else if (baseElementWidget instanceof SequenceFlowWidget) {
                if (((SequenceFlowWidget) baseElementWidget).getId().equals(id)) {
                    widget = baseElementWidget;
                    break;
                }
            } //            else if (baseElementWidget instanceof ArtifactWidget) {//Artifact_Commneted
            //                if (((ArtifactWidget) baseElementWidget).getId().equals(id)) {
            //                    widget = baseElementWidget;
            //                    break;
            //                }
            //            }
            else {
                throw new InvalidElmentException("Invalid Batch Element" + baseElementWidget);
            }
        }
        return widget;
    }

    @Override
    public BaseElementWidget getBaseElement(String id) {
        BaseElementWidget widget = null;
        List<BaseElementWidget> baseElementWidgets = new ArrayList<BaseElementWidget>(flowElements);
//        baseElementWidgets.addAll(artifacts);//Artifact_Commneted
        for (BaseElementWidget baseElementWidget : baseElementWidgets) {
            if (baseElementWidget.getId().equals(id)) {
                widget = baseElementWidget;
                break;
            }
        }
        return widget;
    }

    @Override
    public List<IBaseElementWidget> getBaseElements() {
        List<IBaseElementWidget> baseElementWidgets = new ArrayList<IBaseElementWidget>(flowElements);
//        baseElementWidgets.addAll(artifacts);//Artifact_Commneted
        return baseElementWidgets;
    }

    @Override
    public void removeBaseElement(IBaseElementWidget baseElementWidget) {
        if (baseElementWidget instanceof FlowElementWidget) {
            removeFlowElement((FlowElementWidget) baseElementWidget);
        }
//        else if (baseElementWidget instanceof ArtifactWidget) {//Artifact_Commneted
//            removeArtifact((ArtifactWidget) baseElementWidget);
//        }
    }

    @Override
    public void addBaseElement(IBaseElementWidget baseElementWidget) {
        if (baseElementWidget instanceof FlowElementWidget) {
            addFlowElement((FlowElementWidget) baseElementWidget);
        }
//        else if (baseElementWidget instanceof ArtifactWidget) { //Artifact_Commneted
//            addArtifact((ArtifactWidget) baseElementWidget);
//        }
    }

    @Override
    public void deleteBaseElement(IBaseElementWidget baseElementWidget) {
        Job jobSpec = (Job) this.getModelerFile().getRootElement();
        if (baseElementWidget instanceof FlowElementWidget) {
            if (baseElementWidget instanceof FlowNodeWidget) { //reverse ref
                FlowNodeWidget flowNodeWidget = (FlowNodeWidget) baseElementWidget;
                IBaseElement baseElementSpec = flowNodeWidget.getBaseElementSpec();

                List<SequenceFlowWidget> sequenceFlowWidgetList = new ArrayList<SequenceFlowWidget>();
                sequenceFlowWidgetList.addAll(flowNodeWidget.getOutgoingSequenceFlows());
                sequenceFlowWidgetList.addAll(flowNodeWidget.getIncomingSequenceFlows());

                for (SequenceFlowWidget sequenceFlowWidget : sequenceFlowWidgetList) {
                    sequenceFlowWidget.remove();
                }

//                List<AssociationWidget> associationWidgetList = new ArrayList<AssociationWidget>();//Artifact_Commneted
//                associationWidgetList.addAll(flowNodeWidget.getOutgoingAssociation());
//                associationWidgetList.addAll(flowNodeWidget.getIncomingAssociation());
//
//                for (AssociationWidget associationWidget : associationWidgetList) {
//                    associationWidget.remove();
//                }
//                if (flowNodeWidget instanceof ActivityWidget) {//Boundary_Commneted
//                    ActivityWidget activityWidget = (ActivityWidget) flowNodeWidget;
//                    for (BoundaryEventWidget boundaryEventWidget : activityWidget.getBoundaryEvents()) {
//                        boundaryEventWidget.remove();
//                    }
//                    if (flowNodeWidget instanceof SubProcessWidget) {
//                    }
//                }
                jobSpec.removeBaseElement(baseElementSpec);
                flowNodeWidget.setFlowElementsContainer(null);
                this.flowElements.remove(flowNodeWidget);
            } else if (baseElementWidget instanceof SequenceFlowWidget) {
                SequenceFlowWidget sequenceFlowWidget = ((SequenceFlowWidget) baseElementWidget);
                SequenceFlow sequenceFlowSpec = sequenceFlowWidget.getSequenceFlowSpec();

                FlowNodeWidget sourceWidget = sequenceFlowWidget.getSourceNode();
                FlowNode sourceSpec = (FlowNode) sourceWidget.getBaseElementSpec();
                FlowNodeWidget targetWidget = sequenceFlowWidget.getTargetNode();
                FlowNode targetSpec = (FlowNode) targetWidget.getBaseElementSpec();

                sourceSpec.getOutgoing().remove(sequenceFlowSpec.getId());
                targetSpec.getIncoming().remove(sequenceFlowSpec.getId());

                sourceWidget.getOutgoingSequenceFlows().remove(sequenceFlowWidget);
                targetWidget.getIncomingSequenceFlows().remove(sequenceFlowWidget);

                jobSpec.removeBaseElement(sequenceFlowSpec);
                sequenceFlowWidget.setFlowElementsContainer(null);
                this.flowElements.remove(sequenceFlowWidget);
            } else {
                throw new InvalidElmentException("Invalid Batch Element");
            }
        } //        else if (baseElementWidget instanceof ArtifactWidget) {//Artifact_Commneted
        //            if (baseElementWidget instanceof AssociationWidget) {
        //                AssociationWidget associationWidget = ((AssociationWidget) baseElementWidget);
        //                TAssociation associationSpec = (TAssociation) associationWidget.getBaseElementSpec();
        //
        //                IBaseElementWidget sourceBaseElementWidget = associationWidget.getSourceElementWidget();
        //                if (sourceBaseElementWidget instanceof IFlowNodeWidget) {
        //                    FlowNodeWidget sourceWidget = (FlowNodeWidget) sourceBaseElementWidget;
        //                    TFlowNode sourceSpec = (TFlowNode) sourceWidget.getBaseElementSpec();
        //                    sourceSpec.getOutgoing().remove(associationSpec.getId());
        //                    sourceWidget.getOutgoingAssociation().remove(associationWidget);
        //                } else if (sourceBaseElementWidget instanceof IArtifactNodeWidget) {
        //                    IArtifactNodeWidget sourceWidget = (IArtifactNodeWidget) sourceBaseElementWidget;
        //                    sourceWidget.getOutgoingArtifactEdgeWidget().remove(associationWidget);
        //                }
        //
        //                IBaseElementWidget targetBaseElementWidget = associationWidget.getTargetElementWidget();
        //                if (targetBaseElementWidget instanceof IFlowNodeWidget) {
        //                    FlowNodeWidget targetWidget = (FlowNodeWidget) targetBaseElementWidget;
        //                    TFlowNode targetSpec = (TFlowNode) targetWidget.getBaseElementSpec();
        //                    targetSpec.getIncoming().remove(associationSpec.getId());
        //                    targetWidget.getIncomingAssociation().remove(associationWidget);
        //                } else if (targetBaseElementWidget instanceof IArtifactNodeWidget) {
        //                    IArtifactNodeWidget targetWidget = (IArtifactNodeWidget) targetBaseElementWidget;
        //                    targetWidget.getIncommingArtifactEdgeWidget().remove(associationWidget);
        //                }
        //
        //                jobSpec.removeArtifact(associationSpec);
        //                associationWidget.setFlowElementsContainer(null);
        //                this.artifacts.remove(associationWidget);
        //            } else {
        //                ArtifactWidget artifactWidget = (ArtifactWidget) baseElementWidget;
        //                TArtifact artifactSpec = (TArtifact) artifactWidget.getBaseElementSpec();
        //
        //                List<AssociationWidget> associationWidgetList = new ArrayList<AssociationWidget>();
        //                if (baseElementWidget instanceof TextAnnotationWidget) {
        //                    associationWidgetList.addAll(((TextAnnotationWidget) artifactWidget).getOutgoingAssociation());
        //                    associationWidgetList.addAll(((TextAnnotationWidget) artifactWidget).getIncomingAssociation());
        //                } else if (baseElementWidget instanceof GroupWidget) {
        //                    associationWidgetList.addAll(((GroupWidget) artifactWidget).getOutgoingAssociation());
        //                    associationWidgetList.addAll(((GroupWidget) artifactWidget).getIncomingAssociation());
        //                }
        //                for (AssociationWidget associationWidget : associationWidgetList) {
        //                    associationWidget.remove();
        //                }
        //
        //                jobSpec.removeArtifact(artifactSpec);
        //                artifactWidget.setFlowElementsContainer(null);
        //                this.artifacts.remove(artifactWidget);
        //            }
        //        }
        else {
            throw new InvalidElmentException("Invalid Batch Element");
        }

    }

    @Override
    public void createBaseElement(IBaseElementWidget baseElementWidget) {
        String baseElementId;
        Boolean isExist = false;
        if (baseElementWidget instanceof FlowElementWidget) {
            this.flowElements.add((FlowElementWidget) baseElementWidget);
            if (baseElementWidget instanceof FlowNodeWidget) { //reverse ref
                ((FlowNodeWidget) baseElementWidget).setFlowElementsContainer(this);
                baseElementId = ((FlowNodeWidget) baseElementWidget).getId();
                isExist = ((FlowNodeWidget) baseElementWidget).getNodeWidgetInfo().isExist();
            } else if (baseElementWidget instanceof SequenceFlowWidget) {
                ((SequenceFlowWidget) baseElementWidget).setFlowElementsContainer(this);
                baseElementId = ((SequenceFlowWidget) baseElementWidget).getId();
                isExist = ((SequenceFlowWidget) baseElementWidget).getEdgeWidgetInfo().isExist();
            } else {
                throw new InvalidElmentException("Invalid Batch FlowElement : " + baseElementWidget);
            }
        } //        else if (baseElementWidget instanceof ArtifactWidget) {//Artifact_Commneted
        //            this.artifacts.add((ArtifactWidget) baseElementWidget);
        //            if (baseElementWidget instanceof TextAnnotationWidget) { //reverse ref
        //                ((TextAnnotationWidget) baseElementWidget).setFlowElementsContainer(this);
        //                baseElementId = ((TextAnnotationWidget) baseElementWidget).getId();
        //                isExist = ((NodeWidget) baseElementWidget).getNodeWidgetInfo().isExist();
        //            } else if (baseElementWidget instanceof AssociationWidget) {
        //                ((AssociationWidget) baseElementWidget).setFlowElementsContainer(this);
        //                baseElementId = ((AssociationWidget) baseElementWidget).getId();
        //                isExist = ((EdgeWidget) baseElementWidget).getEdgeWidgetInfo().isExist();
        //            } else if (baseElementWidget instanceof GroupWidget) { //reverse ref
        //                ((GroupWidget) baseElementWidget).setFlowElementsContainer(this);
        //                baseElementId = ((GroupWidget) baseElementWidget).getId();
        //                isExist = ((NodeWidget) baseElementWidget).getNodeWidgetInfo().isExist();
        //            } else {
        //                throw new InvalidElmentException("Invalid Batch ArtifactWidget : " + baseElementWidget);
        //            }
        //        }
        else {
            throw new InvalidElmentException("Invalid Batch Element");
        }

        if (!isExist) {
            /*specification Start*///ELEMENT_UPGRADE
            //Process Model
            Job jobSpec = (Job) this.getModelerFile().getRootElement();
            IBaseElement baseElement = null;
            if (baseElementWidget instanceof FlowElementWidget) {
                if (baseElementWidget instanceof FlowNodeWidget) {
                    if (baseElementWidget instanceof ActivityWidget) {
                        if (baseElementWidget instanceof StepWidget) {
                            Step step = new Step();
                            step.setKey(baseElementId);
                            step.setProperties(new Properties());
                            step.setListeners(new Listeners());
                            if (baseElementWidget instanceof BatchletWidget) {
                                step.setBatchlet(new Batchlet());
                                step.getBatchlet().setProperties(new Properties());
                            } else if (baseElementWidget instanceof ChunkWidget) {
                                step.setChunk(new Chunk());
                                step.getChunk().setReader(new ItemReader());
                                step.getChunk().getReader().setProperties(new Properties());
                                step.getChunk().setProcessor(new ItemProcessor());
                                step.getChunk().getProcessor().setProperties(new Properties());
                                step.getChunk().setWriter(new ItemWriter());
                                step.getChunk().getWriter().setProperties(new Properties());
                            } else {
                                throw new InvalidElmentException("Invalid Batch Task Element : " + baseElement);
                            }
                            baseElement = step;
                        }
//                        else if (baseElementWidget instanceof SubProcessWidget) {//Sub_Commented
//                            if (baseElementWidget instanceof DefaultSubProcessWidget) {
//                                baseElement = new TSubProcess();
//                            } else if (baseElementWidget instanceof EventSubProcessWidget) {
//                                baseElement = new TSubProcess();
//                                ((TSubProcess) baseElement).setTriggeredByEvent(true);
//                            } else if (baseElementWidget instanceof TransactionSubProcessWidget) {
//                                baseElement = new TTransaction();
//                            } else if (baseElementWidget instanceof AdHocSubProcessWidget) {
//                                baseElement = new TAdHocSubProcess();
//                            }
//                        }
                    } else if (baseElementWidget instanceof GatewayWidget) {
                        if (baseElementWidget instanceof SplitGatewayWidget) {
                            baseElement = new Split();
                        } else if (baseElementWidget instanceof DecisionGatewayWidget) {
                            Decision decision = new Decision();
                            decision.setKey(baseElementId);
                            baseElement = decision;
                        } else {
                            throw new InvalidElmentException("Invalid Batch Gateway Element : " + baseElement);
                        }
                    }
                } else if (baseElementWidget instanceof SequenceFlowWidget) {
                    baseElement = new SequenceFlow();
                } else {
                    throw new InvalidElmentException("Invalid Batch Element");
                }
            } //            else if (baseElementWidget instanceof ArtifactWidget) {//Artifact_Commneted
            //                if (baseElementWidget instanceof TextAnnotationWidget) { //reverse ref
            //                    baseElement = new TTextAnnotation();
            //                } else if (baseElementWidget instanceof AssociationWidget) {
            //                    baseElement = new TAssociation();
            //                } else if (baseElementWidget instanceof GroupWidget) {
            //                    baseElement = new TGroup();
            //                } else {
            //                    throw new InvalidElmentException("Invalid Batch ArtifactWidget : " + baseElementWidget);
            //                }
            //            }
            else {
                throw new InvalidElmentException("Invalid Batch Element");
            }
            baseElementWidget.setBaseElementSpec(baseElement);
            baseElement.setId(baseElementId);

            if (baseElement instanceof FlowElement) {
                jobSpec.addBaseElement((FlowElement) baseElement);
            }
//            else if (baseElement instanceof Artifact) {//Artifact_Commneted
//                jobSpec.addArtifact((Artifact) baseElement);
//            }

            ElementConfigFactory elementConfigFactory = this.getModelerFile().getVendorSpecification().getElementConfigFactory();
            elementConfigFactory.initializeObjectValue(baseElement);

        } else {
            if (baseElementWidget instanceof FlowElementWidget) {
                if (baseElementWidget instanceof FlowNodeWidget) {
                    FlowNodeWidget flowNodeWidget = (FlowNodeWidget) baseElementWidget;
                    flowNodeWidget.setBaseElementSpec(flowNodeWidget.getNodeWidgetInfo().getBaseElementSpec());
                    //set TDefinition that is used to accessed  TError , TEsclation in TEventDefinition embedded method .
//                    if (baseElementWidget instanceof EventWidget) {
//                        List<TEventDefinition> eventDefinitions = ((TEvent) baseElementWidget.getBaseElementSpec()).getEventDefinition();
//                        if (!eventDefinitions.isEmpty() && eventDefinitions.get(0) != null) {
//                            eventDefinitions.get(0).setDefinitionElement(this.getModelerFile().getDefinitionElement());
//                        }
//                    }
                } else if (baseElementWidget instanceof SequenceFlowWidget) {
                    SequenceFlowWidget sequenceFlowWidget = (SequenceFlowWidget) baseElementWidget;//TBF_CODE
                    baseElementWidget.setBaseElementSpec(sequenceFlowWidget.getEdgeWidgetInfo().getBaseElementSpec());
                } else {
                    throw new InvalidElmentException("Invalid Batch Element");
                }
            } //            else if (baseElementWidget instanceof ArtifactWidget) {//Artifact_Commneted
            //                if (baseElementWidget instanceof TextAnnotationWidget) {
            //                    TextAnnotationWidget textAnnotationWidget = (TextAnnotationWidget) baseElementWidget;
            //                    textAnnotationWidget.setBaseElementSpec(textAnnotationWidget.getNodeWidgetInfo().getBaseElementSpec());
            //                } else if (baseElementWidget instanceof AssociationWidget) {
            //                    AssociationWidget associationWidget = (AssociationWidget) baseElementWidget;
            //                    associationWidget.setBaseElementSpec(associationWidget.getEdgeWidgetInfo().getBaseElementSpec());
            //                } else if (baseElementWidget instanceof GroupWidget) {
            //                    GroupWidget groupWidget = (GroupWidget) baseElementWidget;
            //                    groupWidget.setBaseElementSpec(groupWidget.getNodeWidgetInfo().getBaseElementSpec());
            //                } else {
            //                    throw new InvalidElmentException("Invalid Batch ArtifactWidget : " + baseElementWidget);
            //                }
            //            }
            else {
                throw new InvalidElmentException("Invalid Batch Element");
            }
        }

    }

//    /**
//     * @return the artifacts
//     */
//    public List<ArtifactWidget> getArtifacts() {//Artifact_Commneted
//        return artifacts;
//    }
//
//    /**
//     * @param artifacts the artifacts to set
//     */
//    public void setArtifacts(List<ArtifactWidget> artifacts) {//Artifact_Commneted
//        this.artifacts = artifacts;
//    }
//
//    public void addArtifact(ArtifactWidget artifactWidget) {//Artifact_Commneted
//        this.artifacts.add(artifactWidget);
//    }
//
//    public void removeArtifact(ArtifactWidget artifactWidget) {//Artifact_Commneted
//        this.artifacts.remove(artifactWidget);
//    }
    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void createVisualPropertySet(ElementPropertySet elementPropertySet) {
    }

    /**
     * @return the debugNodeWidget
     */
    public List<IFlowNodeWidget> getDebugNodeWidget() {
        return debugNodeWidget;
    }

    /**
     * @param debugNodeWidget the debugNodeWidget to set
     */
    public void setDebugNodeWidget(List<IFlowNodeWidget> debugNodeWidget) {
        this.debugNodeWidget = debugNodeWidget;
    }

    public void addDebugNodeWidget(IFlowNodeWidget debugNodeWidget) {
        this.debugNodeWidget.add(debugNodeWidget);
    }

    @Override
    protected List<JMenuItem> getPopupMenuItemList() {
        List<JMenuItem> menuList = super.getPopupMenuItemList();
        JMenuItem generateCode = new JMenuItem("Generate Source Code");
        generateCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenerateCodeDialog dialog = new GenerateCodeDialog(JobScene.this.getModelerFile().getFileObject());
                dialog.setVisible(true);
                if (dialog.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
                    RequestProcessor processor = new RequestProcessor("jpa/ExportCode"); // NOI18N
                    SourceCodeGeneratorTask task = new SourceCodeGeneratorTask(JobScene.this.getModelerFile(), dialog.getTargetPoject(), dialog.getSourceGroup());
                    processor.post(task);
                }

            }
        });
        menuList.add(0, generateCode);
        menuList.add(1, null);

        return menuList;
    }

}
