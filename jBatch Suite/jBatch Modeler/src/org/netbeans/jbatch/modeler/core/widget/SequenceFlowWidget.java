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

import java.awt.BasicStroke;
import java.awt.Color;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.jbatch.modeler.spec.core.FlowElement;
import org.netbeans.jbatch.modeler.spec.core.FlowNode;
import org.netbeans.jbatch.modeler.spec.core.SequenceFlow;
import org.netbeans.modeler.config.element.ElementConfigFactory;
import org.netbeans.modeler.core.exception.InvalidElmentException;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.specification.model.document.widget.IFlowElementWidget;
import org.netbeans.modeler.widget.context.ContextPaletteModel;
import org.netbeans.modeler.widget.edge.EdgeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.properties.customattr.CustomAttributeSupport;
import org.netbeans.modeler.widget.properties.generic.ElementCustomPropertySupport;
import org.netbeans.modeler.widget.properties.generic.ElementPropertySupport;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;

public class SequenceFlowWidget extends EdgeWidget implements FlowEdgeWidget {

    public SequenceFlowWidget(IModelerScene scene, EdgeWidgetInfo edge) {
        super(scene, edge);
        setStroke(new BasicStroke(1.35f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        this.addPropertyChangeListener("name", new PropertyChangeListener<String>() {
            @Override
            public void changePerformed(String value) {
                setName(value);
                SequenceFlowWidget.this.setLabel(name);
            }
        });
    }
    private IBaseElement baseElementSpec;
    private Widget flowElementsContainer; //FlowElementsContainerScenereverse ref
    private FlowNodeWidget sourceNode;
    private FlowNodeWidget targetNode;

    @Override
    public void createPropertySet(ElementPropertySet set) {
        try {
            ElementConfigFactory elementConfigFactory = this.getModelerScene().getModelerFile().getVendorSpecification().getElementConfigFactory();
            elementConfigFactory.createPropertySet(set, this.getBaseElementSpec(), getPropertyChangeListeners());

            FlowNodeWidget sourceFlowNodeWidget = this.getSourceNode();
            FlowNodeWidget targetFlowNodeWidget = this.getTargetNode();

            if (sourceFlowNodeWidget instanceof DecisionGatewayWidget || targetFlowNodeWidget instanceof EventWidget) {
                set.put("BASIC_PROP", new ElementCustomPropertySupport(this.getModelerScene().getModelerFile(), this.getBaseElementSpec(), String.class,
                        "on", "On Status", "Specifies the exit status value that activates this element. Wildcards of \"*\" and \"?\" may be used. \"*\" matches zero or more characters. \"?\" matches exactly one character. It must match an exit status value in order to have effect. This is a required attribute.",
                        new PropertyChangeListener<String>() {
                            @Override
                            public void changePerformed(String value) {
                                // manageCompensationIcon();
                            }
                        }));
            }

            set.put("OTHER_PROP", new CustomAttributeSupport(this.getModelerScene().getModelerFile(), this.getBaseElementSpec(), "Other Attributes", "Other Attributes of the BPMN Element"));
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        } catch (NoSuchFieldException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * @return the sourceNode
     */
    public FlowNodeWidget getSourceNode() {
        return sourceNode;
    }

    /**
     * @param sourceNode the sourceNode to set
     */
    public void setSourceNode(FlowNodeWidget sourceNode) {
        this.sourceNode = sourceNode;
        if (sourceNode != null) {
            IBaseElement baseElement = sourceNode.getBaseElementSpec();
            IBaseElement flowSeqElement = this.getBaseElementSpec();
            if (baseElement instanceof FlowNode && flowSeqElement instanceof SequenceFlow) {
                FlowNode flowNode = (FlowNode) baseElement;
                SequenceFlow sequenceFlow = (SequenceFlow) flowSeqElement;
                sequenceFlow.setSourceRef(flowNode.getId());
            } else {
                throw new InvalidElmentException("Invalid Batch Element");
            }
        } else {
            FlowElement flowSeqElement = (FlowElement) this.getBaseElementSpec();
            if (flowSeqElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowSeqElement;
                sequenceFlow.setSourceRef(null);
            }
        }

    }

    /**
     * @return the targetNode
     */
    public FlowNodeWidget getTargetNode() {
        return targetNode;
    }

    /**
     * @param targetNode the targetNode to set
     */
    public void setTargetNode(FlowNodeWidget targetNode) {
        this.targetNode = targetNode;
        if (targetNode != null) {
            IBaseElement baseElement = targetNode.getBaseElementSpec();
            FlowElement flowSeqElement = this.getSequenceFlowSpec();
            if (baseElement instanceof FlowNode && flowSeqElement instanceof SequenceFlow) {
                FlowNode flowNode = (FlowNode) baseElement;
                SequenceFlow sequenceFlow = (SequenceFlow) flowSeqElement;
                sequenceFlow.setTargetRef(flowNode.getId());
            } else {
                throw new InvalidElmentException("Invalid Batch Element");
            }
        } else {
            FlowElement flowSeqElement = this.getSequenceFlowSpec();
            if (flowSeqElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowSeqElement;
                sequenceFlow.setTargetRef(null);
            }
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

    public void setName(String name) {
        this.name = name;
        if (name != null && !name.trim().isEmpty()) {
            this.getSequenceFlowSpec().setName(name);
        } else {
            this.getSequenceFlowSpec().setName(null);
        }
    }

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
    private Color color;

    public Sheet.Set getVisualPropertiesSet(Sheet.Set set) throws NoSuchMethodException, NoSuchFieldException {
        set.put(new ElementPropertySupport(this, Color.class, "color", "Color", "The Line Color of the SequenceFlow Element."));
        return set;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
        this.setLineColor(color);
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

    /**
     * @return the sequenceFlowSpec
     */
    public SequenceFlow getSequenceFlowSpec() {
        return (SequenceFlow) baseElementSpec;
    }

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public IFlowElementWidget getSourceWidget() {
        return sourceNode;
    }

    @Override
    public IFlowElementWidget getTargetWidget() {
        return targetNode;
    }

    @Override
    public ContextPaletteModel getContextPaletteModel() {
        return null;
    }
}
