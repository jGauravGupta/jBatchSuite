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
package org.netbeans.jbatch.modeler.specification.model.job.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.jbatch.modeler.core.widget.ActivityWidget;
import org.netbeans.jbatch.modeler.core.widget.BaseElementWidget;
import org.netbeans.jbatch.modeler.core.widget.FlowNodeWidget;
import org.netbeans.jbatch.modeler.core.widget.GatewayWidget;
import org.netbeans.jbatch.modeler.core.widget.SequenceFlowWidget;
import org.netbeans.jbatch.modeler.spec.Decision;
import org.netbeans.jbatch.modeler.spec.Job;
import org.netbeans.jbatch.modeler.spec.Listener;
import org.netbeans.jbatch.modeler.spec.Listeners;
import org.netbeans.jbatch.modeler.spec.Properties;
import org.netbeans.jbatch.modeler.spec.Property;
import org.netbeans.jbatch.modeler.spec.Step;
import org.netbeans.jbatch.modeler.spec.core.Definitions;
import org.netbeans.jbatch.modeler.spec.core.ExtensionElements;
import org.netbeans.jbatch.modeler.spec.core.FlowNode;
import org.netbeans.jbatch.modeler.spec.core.SequenceFlow;
import org.netbeans.jbatch.modeler.spec.design.BatchDiagram;
import org.netbeans.jbatch.modeler.spec.design.BatchEdge;
import org.netbeans.jbatch.modeler.spec.design.BatchLabel;
import org.netbeans.jbatch.modeler.spec.design.BatchPlane;
import org.netbeans.jbatch.modeler.spec.design.BatchShape;
import org.netbeans.jbatch.modeler.spec.design.Bounds;
import org.netbeans.jbatch.modeler.spec.design.DiagramElement;
import org.netbeans.jbatch.modeler.specification.util.JavaBatchModelUtil;
import org.netbeans.jbatch.modeler.widget.properties.user_interface.listener.ListenerPanel;
import org.netbeans.jbatch.modeler.widget.properties.user_interface.property.PropertyPanel;
import org.netbeans.modeler.anchors.CustomPathAnchor;
import org.netbeans.modeler.anchors.CustomRectangularAnchor;
import org.netbeans.modeler.config.document.IModelerDocument;
import org.netbeans.modeler.config.document.ModelerDocumentFactory;
import org.netbeans.modeler.config.palette.SubCategoryNodeConfig;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.core.exception.InvalidElmentException;
import org.netbeans.modeler.core.exception.ModelerException;
import org.netbeans.modeler.properties.nentity.Column;
import org.netbeans.modeler.properties.nentity.NAttributeEntity;
import org.netbeans.modeler.properties.nentity.NEntityDataListener;
import org.netbeans.modeler.properties.nentity.NEntityPropertySupport;
import org.netbeans.modeler.shape.Border;
import org.netbeans.modeler.shape.GradientPaint;
import org.netbeans.modeler.shape.InnerShapeContext;
import org.netbeans.modeler.shape.OuterShapeContext;
import org.netbeans.modeler.shape.ShapeDesign;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.INModelerScene;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.netbeans.modeler.specification.model.document.core.IFlowElement;
import org.netbeans.modeler.specification.model.document.core.IFlowNode;
import org.netbeans.modeler.specification.model.document.widget.IBaseElementWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowNodeWidget;
import org.netbeans.modeler.validation.jaxb.ValidateJAXB;
import org.netbeans.modeler.widget.edge.EdgeWidget;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.NodeWidget;
import org.netbeans.modeler.widget.node.image.NodeImageWidget;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;
import org.openide.nodes.PropertySupport;
import org.openide.util.Exceptions;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.w3c.dom.Element;
//Sub_Commented
//Boundary_Commneted
//Artifact_Commneted

public class JobUtil extends JavaBatchModelUtil {

    private static JAXBContext jobContext;
    private static Unmarshaller jobUnmarshaller;
    private static Marshaller jobMarshaller;
    InputOutput io = IOProvider.getDefault().getIO("jBatch Console", false);

    @Override
    public void loadModelerFile(ModelerFile file) {
        try {
            IModelerScene scene = file.getModelerScene();
            File savedFile = file.getFile();
            if (jobContext == null) {
                jobContext = JAXBContext.newInstance(new Class<?>[]{ShapeDesign.class, Definitions.class});
            }
            if (jobUnmarshaller == null) {
                jobUnmarshaller = jobContext.createUnmarshaller();
                jobUnmarshaller.setEventHandler(new ValidateJAXB());
            }

            Definitions definition_Load = jobUnmarshaller.unmarshal(new StreamSource(savedFile), Definitions.class).getValue();

            Job job = definition_Load.getJob();

            scene.setRootElementSpec(job);

            BatchDiagram diagram = new BatchDiagram();
            diagram.setId(NBModelerUtil.getAutoGeneratedStringId());
            BatchPlane plane = new BatchPlane();
            plane.setId(NBModelerUtil.getAutoGeneratedStringId());
            diagram.setBatchPlane(plane);

            for (BatchDiagram diagram_Tmp : definition_Load.getBatchDiagram()) {
                if (diagram_Tmp instanceof BatchDiagram) {
                    BatchPlane tmpPlane = diagram_Tmp.getBatchPlane();
                    for (DiagramElement element : tmpPlane.getDiagramElement()) {
                        plane.getDiagramElement().add(element);
                    }
                }
            }
            definition_Load.getBatchDiagram().removeAll(definition_Load.getBatchDiagram());
            definition_Load.getBatchDiagram().add(diagram);

            file.getModelerDiagramModel().setDefinitionElement(definition_Load);
            file.getModelerDiagramModel().setRootElement(job);
            file.getModelerDiagramModel().setDiagramElement(diagram);

//ELEMENT_UPGRADE
            for (IFlowElement flowElement_Load : new CopyOnWriteArrayList<IFlowElement>(job.getDecisionOrFlowOrSplit())) {
                loadFlowNode(scene, (Widget) scene, flowElement_Load);
            }

//            for (IFlowElement flowElement_Load : new CopyOnWriteArrayList<IFlowElement>(job.getFlowElement())) {  //Boundary_Commneted
//                loadBoundaryEvent(scene, flowElement_Load);
//            }
            for (IFlowElement flowElement_Load : new CopyOnWriteArrayList<IFlowElement>(job.getSequenceFlow())) {
                loadEdge(scene, flowElement_Load);
            }

//            for (IArtifact artifact_Load : new CopyOnWriteArrayList<IArtifact>(job.getArtifact())) {  //Artifact_Commneted
//                loadArtifact(scene, artifact_Load);
//            }
            for (DiagramElement diagramElement_Tmp : diagram.getBatchPlane().getDiagramElement()) {
                loadDiagram(scene, diagram, diagramElement_Tmp);
            }
        } catch (JAXBException e) {
            io.getOut().println("Exception: " + e.toString());
            e.printStackTrace();
            System.out.println("Document XML Not Exist");
        }

    }

    private void loadFlowNode(IModelerScene scene, Widget parentWidget, IFlowElement flowElement) {
        IModelerDocument document = null;
        ModelerDocumentFactory modelerDocumentFactory = scene.getModelerFile().getVendorSpecification().getModelerDocumentFactory();
        if (flowElement instanceof FlowNode) {
            try {
//                if (flowElement instanceof TSubProcess) {  //Sub_Commented
//                    if (flowElement instanceof TTransaction) {
//                        document = modelerDocumentFactory.getModelerDocument(flowElement);
//                    } else {
//                        if (((TSubProcess) flowElement).getTriggeredByEvent()) {
//                            document = modelerDocumentFactory.getModelerDocument(flowElement, "triggeredByEvent");
//                        } else {
//                            document = modelerDocumentFactory.getModelerDocument(flowElement);
//                        }
//                    }
//                } else {

//                }
                if (flowElement instanceof Step) {
                    Step step = (Step) flowElement;
                    if (step.getChunk() == null && step.getBatchlet() != null) {
                        document = modelerDocumentFactory.getModelerDocument(flowElement, "Batchlet");
                    } else if (step.getChunk() != null && step.getBatchlet() == null) {
                        document = modelerDocumentFactory.getModelerDocument(flowElement, "Chunk");
                    } else {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                } else if (flowElement instanceof Decision) {
                    document = modelerDocumentFactory.getModelerDocument(flowElement);
                } else {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

            } catch (ModelerException ex) {
                Exceptions.printStackTrace(ex);
            }

            SubCategoryNodeConfig subCategoryNodeConfig = scene.getModelerFile().getVendorSpecification().getPaletteConfig().findSubCategoryNodeConfig(document);

            NodeWidgetInfo nodeWidgetInfo = new NodeWidgetInfo(flowElement.getId(), subCategoryNodeConfig, new Point(0, 0));
            nodeWidgetInfo.setName(flowElement.getName());
            nodeWidgetInfo.setExist(Boolean.TRUE);//to Load Batch
            nodeWidgetInfo.setBaseElementSpec(flowElement);//to Load Batch
            NodeWidget nodeWidget = (NodeWidget) scene.createNodeWidget(nodeWidgetInfo);
            if (flowElement.getName() != null) {
                nodeWidget.setLabel(flowElement.getName());
            }
            //clear incomming & outgoing it will added on sequenceflow auto connection
            ((FlowNode) flowElement).getIncoming().clear();
            ((FlowNode) flowElement).getOutgoing().clear();

//            if (parentWidget instanceof SubProcessWidget) {//Sub_Commented
//                // Move FlowSpec from SubProcesss To Process because it will be moved from Process to SubProcess in next call SubProcessWidget.addFlowNodeWidget
//                ((TSubProcess) ((SubProcessWidget) parentWidget).getBaseElementSpec()).removeFlowElement(flowElement);
//                scene.getRootElementSpec().addBaseElement(flowElement);
//
//                ((SubProcessWidget) parentWidget).addFlowNodeWidget((FlowNodeWidget) nodeWidget);
//            }
//
//            if (flowElement instanceof TSubProcess) {//Sub_Commented
//                for (IFlowElement flowElementChild : new CopyOnWriteArrayList<IFlowElement>(((TSubProcess) flowElement).getFlowElement())) {
//                    loadFlowNode(scene, (FlowNodeWidget) nodeWidget, flowElementChild);
//                }
//            }
        }
    }

//    private void loadBoundaryEvent(IModelerScene scene, IFlowElement flowElement) {//Boundary_Commneted
//        if (flowElement instanceof TBoundaryEvent) {
//            TBoundaryEvent boundaryEvent = (TBoundaryEvent) flowElement;
//            BoundaryEventWidget boundaryEventWidget = (BoundaryEventWidget) scene.getBaseElement(boundaryEvent.getId());
//            ActivityWidget activityWidget = (ActivityWidget) scene.getBaseElement(boundaryEvent.getAttachedToRef());
//            boundaryEventWidget.setActivityWidget(activityWidget);
//            activityWidget.addBoundaryEventWidget(boundaryEventWidget);
//        } else if (flowElement instanceof TSubProcess) {
//            for (IFlowElement flowElementChild : ((TSubProcess) flowElement).getFlowElement()) {
//                loadBoundaryEvent(scene, flowElementChild);
//            }
//        }
//    }
    private void loadEdge(IModelerScene scene, IBaseElement baseElement) {
        EdgeWidgetInfo edgeWidgetInfo = new EdgeWidgetInfo();
        edgeWidgetInfo.setId(baseElement.getId());
        edgeWidgetInfo.setExist(Boolean.TRUE);//to Load Batch
        edgeWidgetInfo.setBaseElementSpec(baseElement);
        if (baseElement instanceof SequenceFlow) {
            SequenceFlow sequenceFlow = (SequenceFlow) baseElement;
            edgeWidgetInfo.setName(((SequenceFlow) baseElement).getName());
            edgeWidgetInfo.setType("SEQUENCEFLOW");

            NodeWidget sourceNodeWidget = (NodeWidget) scene.findBaseElement(sequenceFlow.getSourceRef());//REMOVE_PRE  must be getFlowElement
            NodeWidget targetNodeWidget = (NodeWidget) scene.findBaseElement(sequenceFlow.getTargetRef());//REMOVE_PRE  must be getFlowElement
            edgeWidgetInfo.setSource(sourceNodeWidget.getNodeWidgetInfo().getId());
            edgeWidgetInfo.setTarget(targetNodeWidget.getNodeWidgetInfo().getId());

            // edge.setName("C" + ((BatchScene)scene).getEdgeCounter());
            IEdgeWidget edgeWidget = scene.createEdgeWidget(edgeWidgetInfo);
            if (((SequenceFlow) baseElement).getName() != null) {
                edgeWidget.setLabel(((SequenceFlow) baseElement).getName());
            }
            ((INModelerScene) scene).setEdgeWidgetSource(edgeWidgetInfo, sourceNodeWidget.getNodeWidgetInfo());
            ((INModelerScene) scene).setEdgeWidgetTarget(edgeWidgetInfo, targetNodeWidget.getNodeWidgetInfo());

        }
//        else if (baseElement instanceof TAssociation) {//Artifact_Commneted
//            TAssociation association = (TAssociation) baseElement;
//            edgeWidgetInfo.setType("ASSOCIATION");
//
//            NodeWidget sourceNodeWidget = (NodeWidget) scene.findBaseElement(association.getSourceRef());//REMOVE_PRE  must be getFlowElement
//            NodeWidget targetNodeWidget = (NodeWidget) scene.findBaseElement(association.getTargetRef());//REMOVE_PRE  must be getFlowElement
//            edgeWidgetInfo.setSource(sourceNodeWidget.getNodeWidgetInfo().getId());
//            edgeWidgetInfo.setTarget(targetNodeWidget.getNodeWidgetInfo().getId());
//
//            // edge.setName("C" + ((BatchScene)scene).getEdgeCounter());
//            IEdgeWidget edgeWidget = scene.createEdgeWidget(edgeWidgetInfo);
//            ((INModelerScene) scene).setEdgeWidgetSource(edgeWidgetInfo, sourceNodeWidget.getNodeWidgetInfo());
//            ((INModelerScene) scene).setEdgeWidgetTarget(edgeWidgetInfo, targetNodeWidget.getNodeWidgetInfo());
//
//        } else if (baseElement instanceof TSubProcess) { //Sub_Commented
//            for (IFlowElement flowElementChild : ((TSubProcess) baseElement).getFlowElement()) {
//                loadEdge(scene, flowElementChild);
//            }
//        }
    }

//    private void loadArtifact(IModelerScene scene,/* Widget parentWidget,*/ IArtifact artifact) { //Artifact_Commneted
//        IModelerDocument document = null;
//        ModelerDocumentFactory modelerDocumentFactory = scene.getModelerFile().getVendorSpecification().getModelerDocumentFactory();
//
//        if (artifact instanceof TTextAnnotation || artifact instanceof TGroup) {
//            try {
//                document = modelerDocumentFactory.getModelerDocument(artifact);
//            } catch (ModelerException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//            SubCategoryNodeConfig subCategoryNodeConfig = scene.getModelerFile().getVendorSpecification().getPaletteConfig().findSubCategoryNodeConfig(document);
//
//            NodeWidgetInfo nodeWidgetInfo = new NodeWidgetInfo(artifact.getId(), subCategoryNodeConfig, new Point(0, 0));
//            nodeWidgetInfo.setExist(Boolean.TRUE);//to Load Batch
//            nodeWidgetInfo.setBaseElementSpec(artifact);//to Load Batch
//            NodeWidget nodeWidget = (NodeWidget) scene.createNodeWidget(nodeWidgetInfo);
////            nodeWidget.setLabel(/*No Name Exist for Artifact*/);
//
//        } else if (artifact instanceof TAssociation) {
//            loadEdge(scene, artifact);
//        }
//    }
    private void loadDiagram(IModelerScene scene, BatchDiagram diagram, DiagramElement diagramElement) {
//       BatchProcessUtil util = new BatchProcessUtil();
        if (diagramElement instanceof BatchShape) {
            BatchShape shape = (BatchShape) diagramElement;
            Bounds bounds = shape.getBounds();
            Widget widget = (Widget) scene.findBaseElement(shape.getBatchElement());
            if (widget != null) {
                if (widget instanceof NodeWidget) { //reverse ref
                    NodeWidget nodeWidget = (NodeWidget) widget;
                    NodeImageWidget imageWidget = nodeWidget.getNodeImageWidget();
                    imageWidget.updateWidget((int) bounds.getWidth(), (int) bounds.getHeight(), (int) bounds.getWidth(), (int) bounds.getHeight());
                    imageWidget.setPreferredSize(new Dimension((int) bounds.getWidth(), (int) bounds.getHeight()));

//                    if (nodeWidget instanceof IFlowNodeWidget && ((IFlowNodeWidget) nodeWidget).getFlowElementsContainer() instanceof SubProcessWidget) {//Sub_Commented
//                        SubProcessWidget parentWidget = (SubProcessWidget) ((IFlowNodeWidget) nodeWidget).getFlowElementsContainer();
//                        BatchShape parentShape = (BatchShape) diagram.getBatchPlane().getBatchShape(parentWidget.getBaseElementSpec().getId());
//                        Bounds parentbounds = parentShape.getBounds();
//                        Point location = new Point((int) (bounds.getX() - parentbounds.getX()), (int) (bounds.getY() - parentbounds.getY()));
//                        nodeWidget.setPreferredLocation(nodeWidget.convertSceneToLocal(location));
//                    } else {
                    Point location = new Point((int) bounds.getX(), (int) bounds.getY());
                    nodeWidget.setPreferredLocation(location);
//                    }

                    if (shape.getBatchLabel() != null) {
                        Bounds bound = shape.getBatchLabel().getBounds();
                        nodeWidget.getLabelManager().getLabelWidget().getParentWidget().setPreferredBounds(
                                nodeWidget.getLabelManager().getLabelWidget().getParentWidget().convertSceneToLocal(bound.toRectangle()));
                    } else {
                        if (nodeWidget.getLabelManager() != null) {
                            nodeWidget.getScene().validate();
                            nodeWidget.getLabelManager().setDefaultPosition(); //if location not found in di then set default position to nodewidget
                        }
                    }

                    nodeWidget.setActiveStatus(false);//Active Status is used to prevent reloading SVGDocument until complete document is loaded

                    ShapeDesign shapeDesign = null; //// BatchShapeDesign XML Location Change Here
                    if (nodeWidget instanceof FlowNodeWidget) {
                        FlowNode flowNode = (FlowNode) ((FlowNodeWidget) nodeWidget).getBaseElementSpec();
                        ExtensionElements extensionElements = flowNode.getExtensionElements();
                        if (extensionElements != null) {
                            for (Object obj : extensionElements.getAny()) {
                                if (obj instanceof Element) { //if ShapeDesign is not in JAXB Context
                                    Element element = (Element) obj;
                                    if ("ShapeDesign".equals(element.getNodeName())) {
                                        try {
                                            shapeDesign = jobUnmarshaller.unmarshal((Element) extensionElements.getAny().get(0), ShapeDesign.class).getValue();
                                            shapeDesign.afterUnmarshal();
                                        } catch (JAXBException ex) {
                                            Exceptions.printStackTrace(ex);
                                        }
                                    }
                                } else if (obj instanceof ShapeDesign) {
                                    shapeDesign = (ShapeDesign) obj;
                                }
                            }
                        }
                    }
                    if (shapeDesign != null) {
                        nodeWidget = (NodeWidget) updateNodeWidgetDesign(shapeDesign, nodeWidget);
                    }
                    nodeWidget.setActiveStatus(true);
                    nodeWidget.reloadSVGDocument();
                } else {
                    throw new InvalidElmentException("Invalid Batch Element : " + widget);
                }
            }
        } else if (diagramElement instanceof BatchEdge) {
            BatchEdge edge = (BatchEdge) diagramElement;
            Widget widget = (Widget) scene.getBaseElement(edge.getBatchElement());
            if (widget != null && widget instanceof EdgeWidget) {
                if (widget instanceof SequenceFlowWidget) {
                    SequenceFlowWidget sequenceFlowWidget = (SequenceFlowWidget) widget;
                    sequenceFlowWidget.setControlPoints(edge.getWaypointCollection(), true);
                    if (edge.getBatchLabel() != null) {
                        Bounds bound = edge.getBatchLabel().getBounds();
                        sequenceFlowWidget.getLabelManager().getLabelWidget().getParentWidget().setPreferredLocation(
                                sequenceFlowWidget.getLabelManager().getLabelWidget().convertSceneToLocal(bound.toPoint()));
                    }
                } //                else if (widget instanceof AssociationWidget) {//Artifact_Commneted
                //                    AssociationWidget associationWidget = (AssociationWidget) widget;
                //                    associationWidget.setControlPoints(edge.getWaypointCollection(), true);
                //                }
                else {
                    throw new InvalidElmentException("Invalid Batch Element");
                }
            }

        }
    }

    public void saveModelerFile(ModelerFile file) {
        try {
            updateBatchDiagram(file);
            File savedFile = file.getFile();

            if (jobContext == null) {
                jobContext = JAXBContext.newInstance(new Class<?>[]{ShapeDesign.class,Definitions.class});
            }
            if (jobMarshaller == null) {
                jobMarshaller = jobContext.createMarshaller();
            }

            // output pretty printed
            jobMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            jobMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.omg.org/spec/Batch/20100524/MODEL http://www.omg.org/spec/Batch/2.0/20100501/Batch20.xsd");
            jobMarshaller.setEventHandler(new ValidateJAXB());
            jobMarshaller.marshal(file.getDefinitionElement(), System.out);
            StringWriter sw = new StringWriter();
            jobMarshaller.marshal(file.getDefinitionElement(), sw);

            FileUtils.writeStringToFile(savedFile, sw.toString().replaceFirst("xmlns:ns[A-Za-z\\d]{0,3}=\"http://www.omg.org/spec/Batch/20100524/MODEL\"",
                    "xmlns=\"http://www.omg.org/spec/Batch/20100524/MODEL\""));
        } catch (JAXBException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    public static ShapeDesign getBatchShapeDesign(NodeWidget nodeWidget) {
        ShapeDesign shapeDesign = new ShapeDesign();
        shapeDesign.setOuterShapeContext(new OuterShapeContext(
                new GradientPaint(nodeWidget.getOuterElementStartBackgroundColor(), nodeWidget.getOuterElementStartOffset(),
                        nodeWidget.getOuterElementEndBackgroundColor(), nodeWidget.getOuterElementEndOffset()),
                new Border(nodeWidget.getOuterElementBorderColor(), nodeWidget.getOuterElementBorderWidth())));
        shapeDesign.setInnerShapeContext(new InnerShapeContext(
                new GradientPaint(nodeWidget.getInnerElementStartBackgroundColor(), nodeWidget.getInnerElementStartOffset(),
                        nodeWidget.getInnerElementEndBackgroundColor(), nodeWidget.getInnerElementEndOffset()),
                new Border(nodeWidget.getInnerElementBorderColor(), nodeWidget.getInnerElementBorderWidth())));
        shapeDesign.beforeMarshal();
        return shapeDesign;
    }

    @Override
    public INodeWidget updateNodeWidgetDesign(ShapeDesign shapeDesign, INodeWidget inodeWidget) {
        NodeWidget nodeWidget = (NodeWidget) inodeWidget;
        //ELEMENT_UPGRADE
        if (shapeDesign != null) {
            if (shapeDesign.getOuterShapeContext() != null) {
                if (shapeDesign.getOuterShapeContext().getBackground() != null) {
                    nodeWidget.setOuterElementStartBackgroundColor(shapeDesign.getOuterShapeContext().getBackground().getStartColor());
                    nodeWidget.setOuterElementEndBackgroundColor(shapeDesign.getOuterShapeContext().getBackground().getEndColor());
                }
                if (shapeDesign.getOuterShapeContext().getBorder() != null) {
                    nodeWidget.setOuterElementBorderColor(shapeDesign.getOuterShapeContext().getBorder().getColor());
                    nodeWidget.setOuterElementBorderWidth(shapeDesign.getOuterShapeContext().getBorder().getWidth());
                }
            }
            if (shapeDesign.getInnerShapeContext() != null) {
                if (shapeDesign.getInnerShapeContext().getBackground() != null) {
                    nodeWidget.setInnerElementStartBackgroundColor(shapeDesign.getInnerShapeContext().getBackground().getStartColor());
                    nodeWidget.setInnerElementEndBackgroundColor(shapeDesign.getInnerShapeContext().getBackground().getEndColor());
                }
                if (shapeDesign.getInnerShapeContext().getBorder() != null) {
                    nodeWidget.setInnerElementBorderColor(shapeDesign.getInnerShapeContext().getBorder().getColor());
                    nodeWidget.setInnerElementBorderWidth(shapeDesign.getInnerShapeContext().getBorder().getWidth());
                }
            }
        }

        return nodeWidget;
    }

    public static void updateDiagramFlowElement(BatchPlane plane, Widget widget) {
        //Diagram Model
        if (widget instanceof NodeWidget) { //reverse ref
            NodeWidget nodeWidget = (NodeWidget) widget;

            Rectangle rec = nodeWidget.getSceneViewBound();

            BatchShape shape = new BatchShape();
            shape.setBounds(new Bounds(rec));//(new Bounds(flowNodeWidget.getBounds()));
            shape.setBatchElement(((BaseElementWidget) nodeWidget).getId());
            shape.setId(((BaseElementWidget) nodeWidget).getId() + "_gui");
            if (nodeWidget.getLabelManager() != null && nodeWidget.getLabelManager().isVisible() && nodeWidget.getLabelManager().getLabel() != null && !nodeWidget.getLabelManager().getLabel().trim().isEmpty()) {
                Rectangle bound = nodeWidget.getLabelManager().getLabelWidget().getParentWidget().getPreferredBounds();
                bound = nodeWidget.getLabelManager().getLabelWidget().getParentWidget().convertLocalToScene(bound);

                Rectangle rec_label = new Rectangle(bound.x, bound.y, (int) bound.getWidth(), (int) bound.getHeight());

                BatchLabel label = new BatchLabel();
                label.setBounds(new Bounds(rec_label));
                shape.setBatchLabel(label);
            }
            plane.addDiagramElement(shape);

            ShapeDesign shapeDesign = null;// BatchShapeDesign XML Location Change Here
            if (nodeWidget instanceof FlowNodeWidget) {
                FlowNode flowNode = (FlowNode) ((FlowNodeWidget) nodeWidget).getBaseElementSpec();
                if (flowNode.getExtensionElements() == null) {
                    flowNode.setExtensionElements(new ExtensionElements());
                }
                ExtensionElements extensionElements = flowNode.getExtensionElements();
                for (Object obj : extensionElements.getAny()) {
                    if (obj instanceof Element) { //first time save
                        Element element = (Element) obj;
                        if ("ShapeDesign".equals(element.getNodeName())) {
                            shapeDesign = getBatchShapeDesign(nodeWidget);
                            extensionElements.getAny().remove(obj);
                            extensionElements.getAny().add(shapeDesign);
                            break;
                        }
                    } else if (obj instanceof ShapeDesign) {
                        shapeDesign = getBatchShapeDesign(nodeWidget);
                        extensionElements.getAny().remove(obj);
                        extensionElements.getAny().add(shapeDesign);
                        break;
                    }
                }
            }

            if (shapeDesign == null) {
                if (nodeWidget instanceof FlowNodeWidget) {
                    FlowNode flowNode = (FlowNode) ((FlowNodeWidget) nodeWidget).getBaseElementSpec();
                    ExtensionElements extensionElements = flowNode.getExtensionElements();
                    shapeDesign = getBatchShapeDesign(nodeWidget);
                    extensionElements.getAny().add(shapeDesign);
                }
            }

//            if (nodeWidget instanceof SubProcessWidget) {   //Sub_Commented
//                SubProcessWidget subProcessWidget = (SubProcessWidget) nodeWidget;
//                for (FlowElementWidget flowElementChildrenWidget : subProcessWidget.getFlowElements()) {
//                    updateDiagramFlowElement(plane, (Widget) flowElementChildrenWidget);
//                }
//            }
        } else if (widget instanceof EdgeWidget) {
            EdgeWidget edgeWidget = (EdgeWidget) widget;
            BatchEdge edge = new BatchEdge();
            for (java.awt.Point point : edgeWidget.getControlPoints()) {
                edge.addWaypoint(point);
            }
            edge.setBatchElement(((BaseElementWidget) edgeWidget).getId());
            edge.setId(((BaseElementWidget) edgeWidget).getId() + "_gui");

            if (widget instanceof SequenceFlowWidget) {
                if (edgeWidget.getLabelManager() != null && edgeWidget.getLabelManager().isVisible() && edgeWidget.getLabelManager().getLabel() != null && !edgeWidget.getLabelManager().getLabel().trim().isEmpty()) {
                    Rectangle bound = edgeWidget.getLabelManager().getLabelWidget().getParentWidget().getPreferredBounds();
                    bound = edgeWidget.getLabelManager().getLabelWidget().getParentWidget().convertLocalToScene(bound);

                    Rectangle rec = new Rectangle(bound.x, bound.y, (int) bound.getWidth(), (int) bound.getHeight());

                    BatchLabel label = new BatchLabel();
                    label.setBounds(new Bounds(rec));
                    edge.setBatchLabel(label);
                }
            }
            plane.addDiagramElement(edge);

        } else {
            throw new InvalidElmentException("Invalid Batch Element");
        }

    }

    public static void updateBatchDiagram(ModelerFile file) {
        BatchPlane plane = ((BatchDiagram) file.getDiagramElement()).getBatchPlane();
        plane.getDiagramElement().clear();
        IModelerScene processScene = file.getModelerScene();
        for (IBaseElementWidget flowElementWidget : processScene.getBaseElements()) {
            updateDiagramFlowElement(plane, (Widget) flowElementWidget);
        }
    }

    /*---------------------------------Save File End---------------------------------*/
    @Override
    public Anchor getAnchor(INodeWidget inodeWidget) {
        NodeWidget nodeWidget = (NodeWidget) inodeWidget;
        Anchor sourceAnchor;
        if (nodeWidget instanceof FlowNodeWidget) {
//            if (nodeWidget instanceof EventWidget) {
//                sourceAnchor = new CustomCircularAnchor(nodeWidget);//, (int) nodeWidgetInfo.getDimension().getWidth() / 2);
//            } else
            if (nodeWidget instanceof ActivityWidget) {
                sourceAnchor = new CustomRectangularAnchor(nodeWidget, 0, true);
            } else if (nodeWidget instanceof GatewayWidget) {
                sourceAnchor = new CustomPathAnchor(nodeWidget, true);
            } else {
                throw new InvalidElmentException("Invalid Batch Process Element : " + nodeWidget);
            }
        } //        else if (nodeWidget instanceof ArtifactWidget) { //Artifact_Commneted
        //            sourceAnchor = new CustomRectangularAnchor(nodeWidget, 0, true);
        //        }
        else {
            throw new InvalidElmentException("Invalid Batch Process Element : " + nodeWidget);
        }
        return sourceAnchor;
    }

//    public static TEventDefinition getEventDefinition(TEvent event) {
//        List<TEventDefinition> eventDefinitions = event.getEventDefinition();
//        TEventDefinition eventDefinition = null;
//        if (eventDefinitions.isEmpty()) {
//            eventDefinition = null;
//        } else if (eventDefinitions.size() == 1) {
//            eventDefinition = eventDefinitions.get(0);
//        } else if (eventDefinitions.size() > 1) {  // Temp Solution select 1 st event def untill multiple is not supported
//            //Multiple Pending
//            eventDefinition = eventDefinitions.get(0);
//        }
//
//        return eventDefinition;
//    }
    @Override
    public void transformNode(IFlowNodeWidget flowNodeWidget, IModelerDocument document) {
        IModelerScene scene = flowNodeWidget.getModelerScene();

        NodeWidget sourceNodeWidget = (NodeWidget) flowNodeWidget;
        NodeWidgetInfo sourceNodeWidgetInfo = sourceNodeWidget.getNodeWidgetInfo();
        NodeWidgetInfo targetNodeWidgetInfo = null;

        targetNodeWidgetInfo = sourceNodeWidgetInfo.cloneNodeWidgetInfo();

        targetNodeWidgetInfo.setExist(Boolean.FALSE);
        SubCategoryNodeConfig subCategoryNodeConfig = scene.getModelerFile().getVendorSpecification().getPaletteConfig().findSubCategoryNodeConfig(document);

        targetNodeWidgetInfo.setSubCategoryNodeConfig(subCategoryNodeConfig);

        INodeWidget targetNodeWidget = scene.createNodeWidget(targetNodeWidgetInfo);
        try {
            BeanUtils.copyProperties(((IFlowNodeWidget) targetNodeWidget).getBaseElementSpec(), ((IFlowNodeWidget) sourceNodeWidget).getBaseElementSpec());
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }

        ((IFlowNode) ((IFlowNodeWidget) targetNodeWidget).getBaseElementSpec()).getIncoming().clear();
        ((IFlowNode) ((IFlowNodeWidget) targetNodeWidget).getBaseElementSpec()).getOutgoing().clear();
        //clear incoming and outgoing reference because it is reconnected by following using visual api

        /* BUG : On transform , widget is Selected with resize border then [NodeWidget + border] width is calculated as bound */
        /*BUG Fix Start : Hide Resize border of all selected NodeWidget*/
        sourceNodeWidget.hideResizeBorder();
        scene.validate();
        /*BUG Fix End*/
        Rectangle bound = sourceNodeWidget.getSceneViewBound();
        Point location = sourceNodeWidget.getPreferredLocation();
        targetNodeWidgetInfo.setDimension(new Dimension(bound.width, bound.height));
        ((NodeWidget) targetNodeWidget).getNodeImageWidget().setDimension(new Dimension(bound.width, bound.height));

//        if (((IFlowNodeWidget) sourceNodeWidget).getFlowElementsContainer() instanceof IModelerSubScene) { //Sub_Commented
//            IModelerSubScene modelerSubScene = (IModelerSubScene) ((IFlowNodeWidget) sourceNodeWidget).getFlowElementsContainer();
//            ((NodeWidget) targetNodeWidget).setPreferredLocation(modelerSubScene.convertLocalToScene(location));
//            ((SubProcessWidget) modelerSubScene).moveFlowNodeWidget((FlowNodeWidget) targetNodeWidget);
//        } else {
        targetNodeWidget.setPreferredLocation(location);
//        }

        if (flowNodeWidget instanceof FlowNodeWidget) {
            for (SequenceFlowWidget sequenceFlowWidget : new CopyOnWriteArrayList<SequenceFlowWidget>(((FlowNodeWidget) flowNodeWidget).getIncomingSequenceFlows())) {
                NBModelerUtil.dettachEdgeTargetAnchor(scene, sequenceFlowWidget, sourceNodeWidget);
                NBModelerUtil.attachEdgeTargetAnchor(scene, sequenceFlowWidget, targetNodeWidget);
            }
            for (SequenceFlowWidget sequenceFlowWidget : new CopyOnWriteArrayList<SequenceFlowWidget>(((FlowNodeWidget) flowNodeWidget).getOutgoingSequenceFlows())) {
                NBModelerUtil.dettachEdgeSourceAnchor(scene, sequenceFlowWidget, sourceNodeWidget);
                NBModelerUtil.attachEdgeSourceAnchor(scene, sequenceFlowWidget, targetNodeWidget);
            }
        }

        String name = ((IFlowNode) ((IFlowNodeWidget) targetNodeWidget).getBaseElementSpec()).getName();
        if (name != null && !name.trim().isEmpty()) {
            targetNodeWidget.setLabel(name);
        }

        sourceNodeWidget.remove();
        scene.validate();

    }

    public static PropertySupport addListener(final ModelerFile modelerFile, final Listeners listeners) {
        final NAttributeEntity attributeEntity = new NAttributeEntity("Listeners", "Listeners", "");
        attributeEntity.setCountDisplay(new String[]{"No Listeners set", "One Listener set", "Listeners set"});

        List<Column> columns = new ArrayList<Column>();
        columns.add(new Column("OBJECT", false, true, Object.class));
        columns.add(new Column("Reference", false, String.class));
//        columns.add(new Column("Value", false, String.class));
        attributeEntity.setColumns(columns);
        attributeEntity.setCustomDialog(new ListenerPanel(modelerFile));
        attributeEntity.setTableDataListener(new NEntityDataListener() {

            List<Object[]> data;
            int count;

            @Override
            public void initCount() {
                count = listeners.getListener().size();
            }

            @Override
            public int getCount() {
                return count;
            }

            @Override
            public void initData() {
                List<Listener> listenerList = listeners.getListener();
                List<Object[]> data_local = new LinkedList<Object[]>();
                Iterator<Listener> itr = listenerList.iterator();
                while (itr.hasNext()) {
                    Listener listener = itr.next();
                    Object[] row = new Object[attributeEntity.getColumns().size()];
                    row[0] = listener;
                    row[1] = listener.getRef();
//                    row[2] = listener.getValue();
                    data_local.add(row);
                }
                this.data = data_local;
            }

            @Override
            public List<Object[]> getData() {
                return data;
            }

            @Override
            public void setData(List data) {
                List<Listener> listenerList = new ArrayList<Listener>();
                for (Object[] row : (List<Object[]>) data) {
                    Listener listener;
                    listener = (Listener) row[0];
                    listenerList.add(listener);
                }
                listeners.setListener(listenerList);
                this.data = data;
            }
        });
        return new NEntityPropertySupport(modelerFile, attributeEntity);
    }

    public static PropertySupport addProperty(final ModelerFile modelerFile, final Properties properties) {
        final NAttributeEntity attributeEntity = new NAttributeEntity("Properties", "Properties", "");
        attributeEntity.setCountDisplay(new String[]{"No Properties set", "One Property set", "Properties set"});

        List<Column> columns = new ArrayList<Column>();
        columns.add(new Column("OBJECT", false, true, Object.class));
        columns.add(new Column("Name", false, String.class));
        columns.add(new Column("Value", false, String.class));
        attributeEntity.setColumns(columns);
        attributeEntity.setCustomDialog(new PropertyPanel(modelerFile));
        attributeEntity.setTableDataListener(new NEntityDataListener() {

            List<Object[]> data;
            int count;

            @Override
            public void initCount() {
                count = properties.getProperty().size();
            }

            @Override
            public int getCount() {
                return count;
            }

            @Override
            public void initData() {
                List<Property> propertyList = properties.getProperty();
                List<Object[]> data_local = new LinkedList<Object[]>();
                Iterator<Property> itr = propertyList.iterator();
                while (itr.hasNext()) {
                    Property property = itr.next();
                    Object[] row = new Object[attributeEntity.getColumns().size()];
                    row[0] = property;
                    row[1] = property.getName();
                    row[2] = property.getValue();
                    data_local.add(row);
                }
                this.data = data_local;
            }

            @Override
            public List<Object[]> getData() {
                return data;
            }

            @Override
            public void setData(List data) {
                List<Property> propretyList = new ArrayList<Property>();
                for (Object[] row : (List<Object[]>) data) {
                    Property property;
                    property = (Property) row[0];
                    propretyList.add(property);
                }
                properties.setProperty(propretyList);
                this.data = data;
            }
        });
        return new NEntityPropertySupport(modelerFile, attributeEntity);
    }

}
