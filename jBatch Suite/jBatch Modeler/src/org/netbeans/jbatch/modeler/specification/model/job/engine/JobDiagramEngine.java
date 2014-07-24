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
package org.netbeans.jbatch.modeler.specification.model.job.engine;

import java.awt.Point;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.jbatch.modeler.core.widget.FlowWidget;
import org.netbeans.modeler.core.engine.ModelerDiagramEngine;
import static org.netbeans.modeler.core.engine.ModelerDiagramEngine.alignStrategyProvider;
import org.netbeans.modeler.provider.NodeWidgetSelectProvider;
import org.netbeans.jbatch.modeler.provider.extension.MultiMoveProvider;
import org.netbeans.jbatch.modeler.provider.extension.NodeWidgetResizeProvider;
import org.netbeans.modeler.provider.node.move.MoveAction;
import org.netbeans.modeler.tool.DesignerTools;
import org.netbeans.modeler.widget.action.doubleclick.DoubleClickAction;
import org.netbeans.modeler.widget.action.doubleclick.DoubleClickProvider;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.NodeWidget;

public class JobDiagramEngine extends ModelerDiagramEngine {

    public JobDiagramEngine() {
    }

    @Override
    public void setNodeWidgetAction(final INodeWidget nodeWidget) {
        WidgetAction doubleClickAction = null;
        
        if(nodeWidget instanceof FlowWidget){
            doubleClickAction = new DoubleClickAction(new DoubleClickProvider() {
            @Override
            public void onDoubleClick(Widget widget, Point point, boolean bln) {
               ((FlowWidget)widget).openDiagram();
            }
        });
        }else {
            doubleClickAction = new DoubleClickAction(new DoubleClickProvider() {
            @Override
            public void onDoubleClick(Widget widget, Point point, boolean bln) {
                nodeWidget.showProperties();
                nodeWidget.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);
            }
        }); 
        }
        
        WidgetAction selectAction = ActionFactory.createSelectAction(new NodeWidgetSelectProvider(nodeWidget.getModelerScene()));
        
       
//            WidgetAction alignWithMoveAction = ActionFactory.createAlignWithMoveAction(nodeWidget.getModelerScene().getMainLayer(), nodeWidget.getModelerScene().getInterractionLayer(), ActionFactory.createDefaultAlignWithMoveDecorator());
        WidgetAction moveAction = new MoveAction(nodeWidget,
                null, new MultiMoveProvider(nodeWidget.getModelerScene()),
                alignStrategyProvider, alignStrategyProvider);//ActionFactory.createMoveAction(null, new MultiMoveProvider(nodeWidget.getModelerScene()));
//            WidgetAction alignWithResizeAction = ActionFactory.createAlignWithResizeAction ( nodeWidget.getBPMNScene().getMainLayer(), nodeWidget.getBPMNScene().getInterractionLayer(), null, false);
        WidgetAction popupMenuAction = ActionFactory.createPopupMenuAction(nodeWidget.getPopupMenuProvider());
        NodeWidgetResizeProvider nodeWidgetResizeProvider = new NodeWidgetResizeProvider();
        WidgetAction resizeAction = ActionFactory.createResizeAction(nodeWidgetResizeProvider, nodeWidgetResizeProvider);
        WidgetAction snapMoveAction = ActionFactory.createMoveAction(ActionFactory.createSnapToGridMoveStrategy(5, 5), null);

        WidgetAction.Chain selectActionTool = nodeWidget.createActions(DesignerTools.SELECT);
//        if (nodeWidget instanceof ActivityWidget) { //Boundary_Commneted
//            selectActionTool.addAction(((ActivityWidget) nodeWidget).getBoundaryAcceptProvider());
//        }
//        if (nodeWidget instanceof SubProcessWidget) {//Sub_Commented
//            selectActionTool.addAction(((SubProcessWidget) nodeWidget).getAcceptProvider());
//        }

        selectActionTool.addAction(doubleClickAction);

//             selectActionTool.addAction(new MoveNodeKeyAction(moveStrategy, moveProvider));//Subprocesss Support
        selectActionTool.addAction(selectAction);
        //selectActionTool.addAction(deleteAction);
        //selectActionTool.addAction(((GraphScene) scene).createObjectHoverAction());
        //getLabelWidget().getActions().addAction(editorAction);
        //selectActionTool.addAction(ActionFactory.createExtendedConnectAction(connectionLayer, new SequenceFlowConnectionProvider(scene)));
        //selectActionTool.addAction (new NodeWidgetLockAction ());

//            if (!(nodeWidget instanceof LaneWidget) && !(nodeWidget instanceof LaneSetWidget) && !(nodeWidget instanceof LaneSetBodyWidget)  && !(nodeWidget instanceof LaneSetHeadWidget)) {
//                 selectActionTool.addAction(alignWithMoveAction);
        selectActionTool.addAction(moveAction);
        selectActionTool.addAction(getScene().createWidgetHoverAction());
        selectActionTool.addAction(popupMenuAction);
//            }

//             if(nodeWidget instanceof LaneSetHeadWidget){
//                  selectActionTool.addAction(ActionFactory.createMoveAction(null, new PoolMultiMoveProvider(nodeWidget.getBPMNScene())));
//             }
//            selectActionTool.addAction(new MoveAction(moveStrategy, moveProvider));//Subprocesss Support
        ((NodeWidget) nodeWidget).getActions().addAction(resizeAction);

//            if (!(nodeWidget instanceof LaneWidget) && !(nodeWidget instanceof LaneSetWidget) && !(nodeWidget instanceof LaneSetBodyWidget)  && !(nodeWidget instanceof LaneSetHeadWidget)) {
        selectActionTool.addAction(snapMoveAction);
//            }

//       WidgetAction.Chain paletteActionTool = this.createActions(DesignerTools.CONTEXT_PALETTE);
//          paletteActionTool.addAction (new NodeWidgetAction ());
//       paletteActionTool.addAction (new NodeWidgetLockAction ());
    }
}
