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
package org.netbeans.jbatch.modeler.core.widget;

import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.jbatch.core.widget.context.ContextModel;
import org.netbeans.jbatch.modeler.spec.core.FlowNode;
import org.netbeans.jbatch.modeler.spec.core.SequenceFlow;
import org.netbeans.modeler.config.element.ElementConfigFactory;
import org.netbeans.modeler.core.exception.InvalidElmentException;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.specification.model.document.widget.IFlowEdgeWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowNodeWidget;
import org.netbeans.modeler.widget.context.ContextPaletteModel;
import org.netbeans.modeler.widget.node.NodeWidget;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;
import org.netbeans.modeler.widget.properties.customattr.CustomAttributeSupport;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.openide.util.Exceptions;

/**
 *
 *
 *
 *
 */
public class FlowNodeWidget extends NodeWidget implements FlowElementWidget, IFlowNodeWidget {

    public FlowNodeWidget(IModelerScene scene, NodeWidgetInfo node) {
        super(scene, node);
        this.addPropertyChangeListener("name", new PropertyChangeListener<String>() {
            @Override
            public void changePerformed(String value) {
                setName(value);
                if (value != null && !value.trim().isEmpty()) {
                    FlowNodeWidget.this.setLabel(value);
                } else {
                    FlowNodeWidget.this.setLabel("");
                }
//                if (value != null && !value.trim().isEmpty()) {
//                    FlowNodeWidget.this.setElementTextValue(value);
//                } else {
//                    FlowNodeWidget.this.setElementTextValue("");
//                }
            }
        });
    }
    private IBaseElement baseElementSpec;
    private Widget flowElementsContainer; //reverse ref
    protected List<SequenceFlowWidget> incomingSequenceFlows = new ArrayList<SequenceFlowWidget>();
    protected List<SequenceFlowWidget> outgoingSequenceFlows = new ArrayList<SequenceFlowWidget>();
//    private List<AssociationWidget> incomingAssociation = new ArrayList<AssociationWidget>();//Artifact_Commneted
//    private List<AssociationWidget> outgoingAssociation = new ArrayList<AssociationWidget>();//Artifact_Commneted

    @Override
    public List<? extends IFlowEdgeWidget> getIncommingFlowEdgeWidget() {
        return incomingSequenceFlows;
    }

    @Override
    public List<? extends IFlowEdgeWidget> getOutgoingFlowEdgeWidget() {
        return outgoingSequenceFlows;
    }

    @Override
    public void createPropertySet(ElementPropertySet set) {
        set.createPropertySet(this, this.getBaseElementSpec(), getPropertyChangeListeners());
        set.put("OTHER_PROP", new CustomAttributeSupport(this.getModelerScene().getModelerFile(), this.getBaseElementSpec(), "Other Attributes", "Other Attributes of the BPMN Element"));
    }

    @Override
    public void createVisualPropertySet(ElementPropertySet elementPropertySet) {
        try {
            createVisualOuterPropertiesSet(elementPropertySet);
            createVisualInnerPropertiesSet(elementPropertySet);
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        } catch (NoSuchFieldException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * @return the incomingSequenceFlows
     */
    public List<SequenceFlowWidget> getIncomingSequenceFlows() {
        return incomingSequenceFlows;
    }

    /**
     * @param incomingSequenceFlows the incomingSequenceFlows to set
     */
    public void setIncomingSequenceFlows(List<SequenceFlowWidget> incomingSequenceFlows) {
        this.incomingSequenceFlows = incomingSequenceFlows;
    }

    public void addIncomingSequenceFlow(SequenceFlowWidget incomingSequenceFlow) {
        this.incomingSequenceFlows.add(incomingSequenceFlow);
        IBaseElement baseElement = this.getBaseElementSpec();
        IBaseElement flowSeqElement = incomingSequenceFlow.getBaseElementSpec();
        if (baseElement instanceof FlowNode && flowSeqElement instanceof SequenceFlow) {
            FlowNode flowNode = (FlowNode) baseElement;
            SequenceFlow sequenceFlow = (SequenceFlow) flowSeqElement;
            flowNode.addIncoming(sequenceFlow.getId());
        } else {
            throw new InvalidElmentException("Invalid Batch Element");
        }
    }

    public void removeIncomingSequenceFlow(SequenceFlowWidget incomingSequenceFlow) {
        this.incomingSequenceFlows.remove(incomingSequenceFlow);
        IBaseElement baseElement = this.getBaseElementSpec();
        IBaseElement flowSeqElement = incomingSequenceFlow.getBaseElementSpec();
        if (baseElement instanceof FlowNode && flowSeqElement instanceof SequenceFlow) {
            FlowNode flowNode = (FlowNode) baseElement;
            SequenceFlow sequenceFlow = (SequenceFlow) flowSeqElement;
            flowNode.removeIncoming(sequenceFlow.getId());
        } else {
            throw new InvalidElmentException("Invalid Batch Element");
        }
    }

    /**
     * @return the outgoingSequenceFlows
     */
    public List<SequenceFlowWidget> getOutgoingSequenceFlows() {
        return outgoingSequenceFlows;
    }

    /**
     * @param outgoingSequenceFlows the outgoingSequenceFlows to set
     */
    public void setOutgoingSequenceFlows(List<SequenceFlowWidget> outgoingSequenceFlows) {
        this.outgoingSequenceFlows = outgoingSequenceFlows;
    }

    public void addOutgoingSequenceFlow(SequenceFlowWidget outgoingSequenceFlow) {
        this.outgoingSequenceFlows.add(outgoingSequenceFlow);
        IBaseElement baseElement = this.getBaseElementSpec();
        IBaseElement flowSeqElement = outgoingSequenceFlow.getBaseElementSpec();
        if (baseElement instanceof FlowNode && flowSeqElement instanceof SequenceFlow) {
            FlowNode flowNode = (FlowNode) baseElement;
            SequenceFlow sequenceFlow = (SequenceFlow) flowSeqElement;
            flowNode.addOutgoing(sequenceFlow.getId());
        } else {
            throw new InvalidElmentException("Invalid Batch Element");
        }
    }

    public void removeOutgoingSequenceFlow(SequenceFlowWidget outgoingSequenceFlow) {
        this.outgoingSequenceFlows.remove(outgoingSequenceFlow);
        IBaseElement baseElement = this.getBaseElementSpec();
        IBaseElement flowSeqElement = outgoingSequenceFlow.getBaseElementSpec();
        if (baseElement instanceof FlowNode && flowSeqElement instanceof SequenceFlow) {
            FlowNode flowNode = (FlowNode) baseElement;
            SequenceFlow sequenceFlow = (SequenceFlow) flowSeqElement;
            flowNode.removeOutgoing(sequenceFlow.getId());
        } else {
            throw new InvalidElmentException("Invalid Batch Element");
        }
    }

    protected String id;
    protected String name;
    protected String documentation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        if (name != null && !name.trim().isEmpty()) {
            ((FlowNode) FlowNodeWidget.this.getBaseElementSpec()).setName(name);
        } else {
            ((FlowNode) FlowNodeWidget.this.getBaseElementSpec()).setName(null);
        }

    }
//    @Override
//    public void setLabel(String name){
//        setName(name);
//    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    /**
     * @return the flowElementsContainer
     */
    public Widget getFlowElementsContainer() {
        return flowElementsContainer;
    }

    /**
     * @param flowElementsContainer the flowElementsContainer to set
     */
    public void setFlowElementsContainer(Widget flowElementsContainer) {
        this.flowElementsContainer = flowElementsContainer;
    }

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }

    /**
     * @return the baseElementSpec
     */
    public IBaseElement getBaseElementSpec() {
        return baseElementSpec;
    }

    /**
     * @param baseElementSpec the baseElementSpec to set
     */
    public void setBaseElementSpec(IBaseElement baseElementSpec) {
        this.baseElementSpec = baseElementSpec;
    }

//    /**
//     * @return the incomingAssociation
//     */
//    public List<AssociationWidget> getIncomingAssociation() {//Artifact_Commneted
//        return incomingAssociation;
//    }
//
//    /**
//     * @param incomingAssociation the incomingAssociation to set
//     */
//    public void setIncomingAssociation(List<AssociationWidget> incomingAssociation) {//Artifact_Commneted
//        this.incomingAssociation = incomingAssociation;
//    }
//
//    public void addIncomingAssociation(AssociationWidget outgoingAssociation) {//Artifact_Commneted
//        this.incomingAssociation.add(outgoingAssociation);
//        IBaseElement baseElement = this.getBaseElementSpec();
//        IBaseElement associationElement = outgoingAssociation.getBaseElementSpec();
//        if (baseElement instanceof FlowNode && associationElement instanceof Association) {
//            FlowNode flowNode = (FlowNode) baseElement;
//            Association association = (Association) associationElement;
//            flowNode.addIncoming(association.getId());
//        } else {
//            throw new InvalidElmentException("Invalid Batch Element");
//        }
//    }
//    public void removeIncomingAssociation(AssociationWidget outgoingAssociation) {//Artifact_Commneted
//        this.incomingAssociation.remove(outgoingAssociation);
//        IBaseElement baseElement = this.getBaseElementSpec();
//        IBaseElement associationElement = outgoingAssociation.getBaseElementSpec();
//        if (baseElement instanceof FlowNode && associationElement instanceof Association) {
//            FlowNode flowNode = (FlowNode) baseElement;
//            Association association = (Association) associationElement;
//            flowNode.removeIncoming(association.getId());
//        } else {
//            throw new InvalidElmentException("Invalid Batch Element");
//        }
//    }
//    /**
//     * @return the outgoingAssociation
//     */
//    public List<AssociationWidget> getOutgoingAssociation() {//Artifact_Commneted
//        return outgoingAssociation;
//    }
//
//    /**
//     * @param outgoingAssociation the outgoingAssociation to set
//     */
//    public void setOutgoingAssociation(List<AssociationWidget> outgoingAssociation) {//Artifact_Commneted
//        this.outgoingAssociation = outgoingAssociation;
//    }
//
//    public void addOutgoingAssociation(AssociationWidget outgoingAssociation) {//Artifact_Commneted
//        this.outgoingAssociation.add(outgoingAssociation);
//        IBaseElement baseElement = this.getBaseElementSpec();
//        IBaseElement associationElement = outgoingAssociation.getBaseElementSpec();
//        if (baseElement instanceof FlowNode && associationElement instanceof Association) {
//            FlowNode flowNode = (FlowNode) baseElement;
//            Association association = (Association) associationElement;
//            flowNode.addOutgoing(association.getId());
//        } else {
//            throw new InvalidElmentException("Invalid Batch Element");
//        }
//    }
//
//    public void removeOutgoingAssociation(AssociationWidget outgoingAssociation) {//Artifact_Commneted
//        this.outgoingAssociation.remove(outgoingAssociation);
//        IBaseElement baseElement = this.getBaseElementSpec();
//        IBaseElement associationElement = outgoingAssociation.getBaseElementSpec();
//        if (baseElement instanceof FlowNode && associationElement instanceof Association) {
//            FlowNode flowNode = (FlowNode) baseElement;
//            Association association = (Association) associationElement;
//            flowNode.removeOutgoing(association.getId());
//        } else {
//            throw new InvalidElmentException("Invalid Batch Element");
//        }
//    }
    @Override
    public ContextPaletteModel getContextPaletteModel() {
        return ContextModel.getContextPaletteModel(this);
    }

    private static final Float START_EVENT_HOVER_BORDER = 0.4F;
    private static final Float INTERMEDIATE_CATCH_EVENT_HOVER_BORDER = 0.2F;
    private static final Float INTERMEDIATE_THROW_EVENT_HOVER_BORDER = 0.2F;
    private static final Float END_EVENT_HOVER_BORDER = 0.4F;
    private static final Float TASK_HOVER_BORDER = 0.5F;
    private static final Float GATEWAY_HOVER_BORDER = 0.5F;

    public void unhoverWidget(int padding) {
        String model = this.getNodeWidgetInfo().getModelerDocument().getDocumentModel();
//        FlowDimensionType flowDimension = this.getNodeWidgetInfo().getModelerDocument().getFlowDimension();
        if (model.equals(DocumentModelType.STEP.name())) {
            decreaseBorderWidth(TASK_HOVER_BORDER + padding);
        } else if (model.equals(DocumentModelType.CONTAINER.name())) {
            decreaseBorderWidth(TASK_HOVER_BORDER + padding);
        } else if (model.equals(DocumentModelType.GATEWAY.name())) {
            decreaseBorderWidth(GATEWAY_HOVER_BORDER + padding);
        }
    }

    public void hoverWidget(int padding) {
        String model = this.getNodeWidgetInfo().getModelerDocument().getDocumentModel();
//        FlowDimensionType flowDimension = this.getNodeWidgetInfo().getModelerDocument().getFlowDimension();

        if (model.equals(DocumentModelType.STEP.name())) {
            increaseBorderWidth(TASK_HOVER_BORDER + padding);
        } else if (model.equals(DocumentModelType.CONTAINER.name())) {
            increaseBorderWidth(TASK_HOVER_BORDER + padding);
        }  else if (model.equals(DocumentModelType.GATEWAY.name())) {
            increaseBorderWidth(GATEWAY_HOVER_BORDER + padding);
        }

    }
    

    
}
