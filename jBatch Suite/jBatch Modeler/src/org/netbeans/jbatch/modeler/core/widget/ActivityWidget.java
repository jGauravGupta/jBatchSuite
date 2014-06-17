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

import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.config.palette.SubCategoryNodeConfig;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;
import org.netbeans.modeler.widget.transferable.MoveWidgetTransferable;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

public class ActivityWidget extends FlowNodeWidget {

//    private List<BoundaryEventWidget> boundaryEventWidgets = new ArrayList<BoundaryEventWidget>();//Boundary_Commneted
    public ActivityWidget(IModelerScene scene, NodeWidgetInfo node) {
        super(scene, node);

    }

    @Override
    public void init() {

    }

//    public WidgetAction getBoundaryAcceptProvider() { //Boundary_Commneted
//        WidgetAction acceptAction = ActionFactory.createAcceptAction(new ActivityWidget.BoundaryAcceptProvider(this));
//        return acceptAction;
//    }
    /**
     * Test if a widget is fully with in the bounds of the container widget.
     *
     * @param widget The widget to test
     * @return True if the widget is in the containers bounds.
     */
    protected boolean isFullyContained(Widget widget) {
        // Calling getPreferredBounds forces the bounds to be calculated if it
        // has not already been calculated.  For example when the Widget was
        // just created and therefore has not had a chance to be displayed.
        Rectangle area = widget.getClientArea();

        boolean retVal = false;
        if (area != null) {
            Rectangle sceneArea = widget.convertLocalToScene(area);

            Rectangle localArea = convertSceneToLocal(sceneArea);
            Rectangle myArea = getClientArea();
            retVal = myArea.contains(localArea);
        }

        return retVal;
    }

    protected boolean isWidgetMove(Transferable transferable) {
        return transferable.isDataFlavorSupported(MoveWidgetTransferable.FLAVOR);
    }

    protected boolean isPaletteItem(Transferable transferable) {
        return transferable.isDataFlavorSupported(DataFlavor.imageFlavor);
    }
//
//    /**
//     * @return the boundaryEventWidget
//     */
//    public List<BoundaryEventWidget> getBoundaryEvents() {//Boundary_Commneted
//        return boundaryEventWidgets;
//    }
//
//    /**
//     * @param boundaryEventWidget the boundaryEventWidget to set
//     */
//    public void setBoundaryEventWidgets(List<BoundaryEventWidget> boundaryEventWidget) {//Boundary_Commneted
//        this.boundaryEventWidgets = boundaryEventWidget;
//    }
//
//    public void addBoundaryEventWidget(BoundaryEventWidget boundaryEventWidget) {//Boundary_Commneted
//        this.boundaryEventWidgets.add(boundaryEventWidget);
//    }
//
//    public void removeBoundaryEventWidget(BoundaryEventWidget boundaryEventWidget) {//Boundary_Commneted
//        this.boundaryEventWidgets.remove(boundaryEventWidget);
//    }
//
//    public class BoundaryAcceptProvider implements /*Scene*/ AcceptProvider {//Boundary_Commneted
//
//        private ActivityWidget activityWidget = null;
//
//        public BoundaryAcceptProvider(ActivityWidget containerW) {
//            activityWidget = containerW;
//        }
//
//        @Override
//        public ConnectorState isAcceptable(Widget widget, Point point, Transferable transferable) {
//            ConnectorState retVal = ConnectorState.ACCEPT;
//
//            if (isWidgetMove(transferable)) {
//                Widget[] target = new Widget[]{getWidget(transferable)};
//                for (Widget curWidget : target) {
//                    if (widget == curWidget) {
//                        retVal = ConnectorState.REJECT;
//                        return retVal;
//                    }
//                    if (isFullyContained(curWidget) == false) {
//                        retVal = ConnectorState.REJECT;
//                        return retVal;
//                    }
//                    if (curWidget instanceof NodeWidget) {
//                        NodeWidget nodeWidget = (NodeWidget) curWidget;
//                        if (nodeWidget.getNodeWidgetInfo().getModelerDocument().getFlowDimension() != FlowDimensionType.BOUNDARY) {
//                            retVal = ConnectorState.REJECT;
//                            return retVal;
//                        }
//                    }
//
//                }
//            } else if (isPaletteItem(transferable)) {
//                SubCategoryNodeConfig subCategoryInfo = getSubCategory(transferable);
//                Image dragImage = subCategoryInfo.getImage();
//                JComponent view = activityWidget.getScene().getView();
//                Graphics2D g2 = (Graphics2D) view.getGraphics();
//                Rectangle visRect = view.getVisibleRect();
//                view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
//
//                point = activityWidget.convertLocalToScene(point);
//                g2.drawImage(dragImage,
//                        AffineTransform.getTranslateInstance(point.getLocation().getX(),
//                                point.getLocation().getY()),
//                        null);
//
//                if (subCategoryInfo.getModelerDocument().getFlowDimension() != FlowDimensionType.BOUNDARY) {
//                    retVal = ConnectorState.REJECT;
//                    return retVal;
//                }
//            }
//            return retVal;
//        }
//
//        @Override
//        public void accept(Widget widget, Point point, Transferable transferable) {
//            AbstractModelerScene scene = (AbstractModelerScene) widget.getScene();
//            BoundaryEventWidget boundaryWidget = null;
//            if (isWidgetMove(transferable)) {
//                Widget[] target;
//                try {
//                    target = new Widget[]{getWidget(transferable)};
//                } catch (Exception e) {
//                    target = new Widget[0];
//                }
//
//                for (Widget curWidget : target) {
//                    if (widget == curWidget) {
//                        break;
//                    }
//                    if (!isFullyContained(curWidget)) {
//                        break;
//                    }
//                    if (curWidget instanceof BoundaryEventWidget) {
//                        boundaryWidget = (BoundaryEventWidget) curWidget;
//                        if (boundaryWidget.isAutoMoveActionLocked()) {
//                            break;
//                        }
//                        boundaryWidget.getActivityWidget().removeBoundaryEventWidget(boundaryWidget);
//                    } else {
//                        break;
//                    }
//                }
//
//            } else if (isPaletteItem(transferable)) {
//                SubCategoryNodeConfig info = getSubCategory(transferable);
//                boundaryWidget = (BoundaryEventWidget) scene.createNodeWidget(new NodeWidgetInfo(NBModelerUtil.getAutoGeneratedStringId(), info, point));
//                boundaryWidget.setPreferredLocation(widget.convertLocalToScene(point));
//                boundaryWidget.getParentWidget().removeChild(boundaryWidget);
//                ((AbstractModelerScene) boundaryWidget.getScene()).getBoundaryWidgetLayer().addChild(boundaryWidget);
//
//            }
//            if (boundaryWidget != null && !boundaryWidget.isAutoMoveActionLocked()) {
//                boundaryWidget.getScene().getSceneAnimator().animatePreferredLocation(boundaryWidget, boundaryWidget.getBoundaryWidgetLocation(activityWidget));
//                boundaryWidget.setActivityWidget(activityWidget);
//                activityWidget.addBoundaryEventWidget(boundaryWidget);
//                ((TBoundaryEvent) boundaryWidget.getBaseElementSpec()).setAttachedToRef(activityWidget.getBaseElementSpec().getId());
//            }
//        }
//    }

    protected SubCategoryNodeConfig getSubCategory(Transferable transferable) {
        Object o = null;
        try {
            //o =  transferable.getTransferData(PaletteItemTransferable.FLAVOR);
            o = transferable.getTransferData(DataFlavor.imageFlavor);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (UnsupportedFlavorException ex) {
            Exceptions.printStackTrace(ex);
        }
        if (o instanceof Node) {
            SubCategoryNodeConfig subCategoryInfo = (SubCategoryNodeConfig) ((Node) o).getValue("SubCategoryInfo");
            return subCategoryInfo;
        } else {
            return null;
        }
    }

    protected Widget getWidget(Transferable transferable) {
        try {
            MoveWidgetTransferable data = (MoveWidgetTransferable) transferable.getTransferData(MoveWidgetTransferable.FLAVOR);
            return data.getWidget();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (UnsupportedFlavorException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
}
