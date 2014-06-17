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
package org.netbeans.jbatch.modeler.specification.util;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import org.netbeans.jbatch.modeler.core.widget.BaseElementWidget;
import org.netbeans.jbatch.modeler.core.widget.BatchletWidget;
import org.netbeans.jbatch.modeler.core.widget.ChunkWidget;
import org.netbeans.jbatch.modeler.core.widget.DecisionGatewayWidget;
import org.netbeans.jbatch.modeler.core.widget.DocumentModelType;
import org.netbeans.jbatch.modeler.core.widget.FlowNodeWidget;
import org.netbeans.jbatch.modeler.core.widget.SequenceFlowWidget;
import org.netbeans.jbatch.modeler.core.widget.SplitGatewayWidget;
import org.netbeans.modeler.border.ResizeBorder;
import org.netbeans.modeler.config.document.IModelerDocument;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.core.exception.InvalidElmentException;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.entity.ComboBoxValue;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.specification.model.util.NModelerUtil;
import org.netbeans.modeler.widget.connection.relation.IRelationValidator;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.NodeWidget;
import org.netbeans.modeler.widget.node.NodeWidgetStatus;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;

public abstract class JavaBatchModelUtil implements NModelerUtil {

    public IEdgeWidget getEdgeWidget(IModelerScene scene, EdgeWidgetInfo edgeWidgetInfo) {
        IEdgeWidget edgeWidget = null;
        if (edgeWidgetInfo.getType().equals("SEQUENCEFLOW")) {
            edgeWidget = new SequenceFlowWidget(scene, edgeWidgetInfo);
        } //        else if (edgeWidgetInfo.getType().equals("ASSOCIATION")) {//Artifact_Commneted
        //            edgeWidget = new AssociationWidget(scene, edgeWidgetInfo);
        //        }
        else {
            throw new InvalidElmentException("Invalid BPMN Edge Type");
        }
        return edgeWidget;
    }

    @Override
    public IEdgeWidget attachEdgeWidget(IModelerScene scene, EdgeWidgetInfo widgetInfo) {
        IEdgeWidget edgeWidget = getEdgeWidget(scene, widgetInfo);

        return edgeWidget;
    }

    @Override
    public INodeWidget attachNodeWidget(IModelerScene scene, NodeWidgetInfo widgetInfo) {
        BaseElementWidget widget = null;
        IModelerDocument bpmnDocument = widgetInfo.getModelerDocument();
        //ELEMENT_UPGRADE
        if (bpmnDocument.getDocumentModel().equals(DocumentModelType.ACTIVITY.name())) {
            if (bpmnDocument.getId().equals("Batchlet_Activity")) {
                widget = new BatchletWidget(scene, widgetInfo);
            } else if (bpmnDocument.getId().equals("Chunk_Activity")) {
                widget = new ChunkWidget(scene, widgetInfo);
            } else {
                throw new InvalidElmentException("Invalid BPMN Element");
            }
        } else if (bpmnDocument.getDocumentModel().equals(DocumentModelType.GATEWAY.name())) {
            if (bpmnDocument.getId().equals("Decision_Gateway")) {
                widget = new DecisionGatewayWidget(scene, widgetInfo);
            } else if (bpmnDocument.getId().equals("Split_Gateway")) {
                widget = new SplitGatewayWidget(scene, widgetInfo);
            } else {
                throw new InvalidElmentException("Invalid BPMN Element");
            }

        } 
//        else if (bpmnDocument.getDocumentModel().equals(DocumentModelType.ARTIFACT.name())) { //Artifact_Commneted
//            if (bpmnDocument.getId().equals("TextAnnotation_Artifact")) {
//                widget = new TextAnnotationWidget(scene, widgetInfo);
//            } else if (bpmnDocument.getId().equals("Group_Artifact")) {
//                widget = new GroupWidget(scene, widgetInfo);
//            } else {
//                throw new InvalidElmentException("Invalid BPMN Element");
//            }
//
//        }
        else {
            throw new InvalidElmentException("Invalid BPMN Model" + bpmnDocument.getDocumentModel());
        }
        return (INodeWidget) widget;
    }

    @Override
    public String getEdgeType(INodeWidget sourceNodeWidget, INodeWidget targetNodeWidget, String connectionContextToolId) {
        String edgeType = null;
        if (sourceNodeWidget instanceof FlowNodeWidget && targetNodeWidget instanceof FlowNodeWidget) {
            edgeType = "SEQUENCEFLOW";
        } 
//        else if (sourceNodeWidget instanceof ArtifactWidget || targetNodeWidget instanceof ArtifactWidget) { //Artifact_Commneted
//            edgeType = "ASSOCIATION";
//        }
        return edgeType;
    }

    @Override
    public void dettachEdgeSourceAnchor(IModelerScene scene, IEdgeWidget edgeWidget, INodeWidget sourceNodeWidget) {
        if (sourceNodeWidget instanceof FlowNodeWidget) {
            if (edgeWidget instanceof SequenceFlowWidget) {
                ((FlowNodeWidget) sourceNodeWidget).removeOutgoingSequenceFlow((SequenceFlowWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
                ((SequenceFlowWidget) edgeWidget).setSourceNode(null);
            } 
//            else if (edgeWidget instanceof AssociationWidget) { //Artifact_Commneted
//                ((FlowNodeWidget) sourceNodeWidget).removeOutgoingAssociation((AssociationWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
//                ((AssociationWidget) edgeWidget).setSourceElementWidget(null);
//            } 
            else {
                throw new InvalidElmentException("Invalid BPMN Element");
            }

        } 
//        else if (sourceNodeWidget instanceof ArtifactWidget) { //Artifact_Commneted
//            if (sourceNodeWidget instanceof TextAnnotationWidget) {
//                if (edgeWidget instanceof AssociationWidget) {
//                    ((TextAnnotationWidget) sourceNodeWidget).removeOutgoingAssociation((AssociationWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
//                    ((AssociationWidget) edgeWidget).setSourceElementWidget(null);
//                } else {
//                    throw new InvalidElmentException("Invalid BPMN Element");
//                }
//            } else if (sourceNodeWidget instanceof GroupWidget) {
//                if (edgeWidget instanceof AssociationWidget) {
//                    ((GroupWidget) sourceNodeWidget).removeOutgoingAssociation((AssociationWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
//                    ((AssociationWidget) edgeWidget).setSourceElementWidget(null);
//                } else {
//                    throw new InvalidElmentException("Invalid BPMN Element");
//                }
//            } else {
//                throw new InvalidElmentException("Invalid BPMN Element");
//            }
//        } 
        else {
            throw new InvalidElmentException("Invalid BPMN Element");
        }
    }

    @Override
    public void dettachEdgeTargetAnchor(IModelerScene scene, IEdgeWidget edgeWidget, INodeWidget targetNodeWidget) {
        if (targetNodeWidget instanceof FlowNodeWidget) {
            if (edgeWidget instanceof SequenceFlowWidget) {
                ((FlowNodeWidget) targetNodeWidget).removeIncomingSequenceFlow((SequenceFlowWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
                ((SequenceFlowWidget) edgeWidget).setTargetNode(null);
            }
//            else if (edgeWidget instanceof AssociationWidget) { //Artifact_Commneted
//                ((FlowNodeWidget) targetNodeWidget).removeIncomingAssociation((AssociationWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
//                ((AssociationWidget) edgeWidget).setTargetElementWidget(null);
//            }
            else {
                throw new InvalidElmentException("Invalid BPMN Element");
            }

        } 
//        else if (targetNodeWidget instanceof ArtifactWidget) { //Artifact_Commneted
//            if (targetNodeWidget instanceof TextAnnotationWidget) {
//                if (edgeWidget instanceof AssociationWidget) {
//                    ((TextAnnotationWidget) targetNodeWidget).removeIncomingAssociation((AssociationWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
//                    ((AssociationWidget) edgeWidget).setTargetElementWidget(null);
//                } else {
//                    throw new InvalidElmentException("Invalid BPMN Element");
//                }
//            } else if (targetNodeWidget instanceof GroupWidget) {
//                if (edgeWidget instanceof AssociationWidget) {
//                    ((GroupWidget) targetNodeWidget).removeIncomingAssociation((AssociationWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
//                    ((AssociationWidget) edgeWidget).setTargetElementWidget(null);
//                } else {
//                    throw new InvalidElmentException("Invalid BPMN Element");
//                }
//            } else {
//                throw new InvalidElmentException("Invalid BPMN Element");
//            }
//        } 
        else {
            throw new InvalidElmentException("Invalid BPMN Element");
        }
    }

    @Override
    public void attachEdgeSourceAnchor(IModelerScene scene, IEdgeWidget edgeWidget, INodeWidget sourceNodeWidget) {

//        CustomRectangularAnchor cra = new CustomRectangularAnchor();
        if (sourceNodeWidget == null) {
            return;
        }
        if (sourceNodeWidget instanceof FlowNodeWidget) {
            if (edgeWidget instanceof SequenceFlowWidget) {
                ((FlowNodeWidget) sourceNodeWidget).addOutgoingSequenceFlow((SequenceFlowWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
                ((SequenceFlowWidget) edgeWidget).setSourceNode((FlowNodeWidget) sourceNodeWidget);
            }
//            else if (edgeWidget instanceof AssociationWidget) { //Artifact_Commneted
//                ((FlowNodeWidget) sourceNodeWidget).addOutgoingAssociation((AssociationWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
//                ((AssociationWidget) edgeWidget).setSourceElementWidget((FlowNodeWidget) sourceNodeWidget);
//            }
            else {
                throw new InvalidElmentException("Invalid BPMN Element");
            }

        } 
//        else if (sourceNodeWidget instanceof ArtifactWidget) { //Artifact_Commneted
//            if (sourceNodeWidget instanceof TextAnnotationWidget) {
//                if (edgeWidget instanceof AssociationWidget) {
//                    ((TextAnnotationWidget) sourceNodeWidget).addOutgoingAssociation((AssociationWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
//                    ((AssociationWidget) edgeWidget).setSourceElementWidget((TextAnnotationWidget) sourceNodeWidget);
//                } else {
//                    throw new InvalidElmentException("Invalid BPMN Element");
//                }
//            } else if (sourceNodeWidget instanceof GroupWidget) {
//                if (edgeWidget instanceof AssociationWidget) {
//                    ((GroupWidget) sourceNodeWidget).addOutgoingAssociation((AssociationWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
//                    ((AssociationWidget) edgeWidget).setSourceElementWidget((GroupWidget) sourceNodeWidget);
//                } else {
//                    throw new InvalidElmentException("Invalid BPMN Element");
//                }
//            } else {
//                throw new InvalidElmentException("Invalid BPMN Element");
//            }
//        }
        else {
            throw new InvalidElmentException("Invalid BPMN Element");
        }
        edgeWidget.setSourceAnchor(NBModelerUtil.getAnchor(sourceNodeWidget));
    }

    @Override
    public void attachEdgeTargetAnchor(IModelerScene scene, IEdgeWidget edgeWidget, INodeWidget targetNodeWidget) {
        IRelationValidator relationValidator = scene.getModelerFile().getVendorSpecification().getModelerDiagramModel().getRelationValidator();
        if (targetNodeWidget == null) {
            return;
        }
        if (targetNodeWidget instanceof FlowNodeWidget) {
            if (edgeWidget instanceof SequenceFlowWidget) {
                ((FlowNodeWidget) targetNodeWidget).addIncomingSequenceFlow((SequenceFlowWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
                ((SequenceFlowWidget) edgeWidget).setTargetNode((FlowNodeWidget) targetNodeWidget);
            } 
//            else if (edgeWidget instanceof AssociationWidget) {//Artifact_Commneted
//                ((FlowNodeWidget) targetNodeWidget).addIncomingAssociation((AssociationWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
//                ((AssociationWidget) edgeWidget).setTargetElementWidget((FlowNodeWidget) targetNodeWidget);
//            }
            else {
                throw new InvalidElmentException("Invalid BPMN Element");
            }

        } 
//        else if (targetNodeWidget instanceof ArtifactWidget) { //Artifact_Commneted
//            if (targetNodeWidget instanceof TextAnnotationWidget) {
//                if (edgeWidget instanceof AssociationWidget) {
//                    ((TextAnnotationWidget) targetNodeWidget).addIncomingAssociation((AssociationWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
//                    ((AssociationWidget) edgeWidget).setTargetElementWidget((TextAnnotationWidget) targetNodeWidget);
//                } else {
//                    throw new InvalidElmentException("Invalid BPMN Element");
//                }
//            } else if (targetNodeWidget instanceof GroupWidget) {
//                if (edgeWidget instanceof AssociationWidget) {
//                    ((GroupWidget) targetNodeWidget).addIncomingAssociation((AssociationWidget) edgeWidget);//.addOutgoingSequenceFlowWidget(widget);
//                    ((AssociationWidget) edgeWidget).setTargetElementWidget((GroupWidget) targetNodeWidget);
//                } else {
//                    throw new InvalidElmentException("Invalid BPMN Element");
//                }
//            } else {
//                throw new InvalidElmentException("Invalid BPMN Element");
//            }
//        } 
        else {
            throw new InvalidElmentException("Invalid BPMN Element");
        }
        edgeWidget.setTargetAnchor(NBModelerUtil.getAnchor(targetNodeWidget));


        /* Validate relation : rollback if error*/
        //ENHANCEMENT : Remove below validation already completed in  SceneConnectProvider.java
        if (edgeWidget instanceof SequenceFlowWidget) {
            FlowNodeWidget sourceNodeWidget = ((SequenceFlowWidget) edgeWidget).getSourceNode();
            if (NBModelerUtil.isValidRelationship(relationValidator, sourceNodeWidget, targetNodeWidget, edgeWidget.getEdgeWidgetInfo().getType(), false) == false) {
                edgeWidget.remove();
            }
            targetNodeWidget.setStatus(NodeWidgetStatus.NONE);
        } 
//        else if (edgeWidget instanceof AssociationWidget) { //Artifact_Commneted
//            IBaseElementWidget sourceNodeWidget = ((AssociationWidget) edgeWidget).getSourceElementWidget();
//            if (NBModelerUtil.isValidRelationship(relationValidator, (INodeWidget) sourceNodeWidget, targetNodeWidget, edgeWidget.getEdgeWidgetInfo().getType(), false) == false) {
//                edgeWidget.remove();
//            }
//            targetNodeWidget.setStatus(NodeWidgetStatus.NONE);
//        }

    }

    @Override
    public ResizeBorder getNodeBorder(INodeWidget nodeWidget) {
//        if (nodeWidget instanceof EventWidget) {
//            nodeWidget.setWidgetBorder(NodeWidget.CIRCLE_RESIZE_BORDER);
//            return NodeWidget.CIRCLE_RESIZE_BORDER;
//        } else {
            
            nodeWidget.setWidgetBorder(NodeWidget.RECTANGLE_RESIZE_BORDER);
            return NodeWidget.RECTANGLE_RESIZE_BORDER;
//        }
    }

  
}
