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
import org.netbeans.jbatch.modeler.core.widget.SplitGatewayWidget;
import org.netbeans.modeler.core.engine.ModelerDiagramEngine;
import static org.netbeans.modeler.core.engine.ModelerDiagramEngine.alignStrategyProvider;
import org.netbeans.modeler.provider.NodeWidgetSelectProvider;
import org.netbeans.jbatch.modeler.provider.extension.MultiMoveProvider;
import org.netbeans.jbatch.modeler.provider.extension.NodeWidgetResizeProvider;
import org.netbeans.jbatch.modeler.provider.extension.SplitMoveProvider;
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

        if (nodeWidget instanceof FlowWidget) {
            doubleClickAction = new DoubleClickAction(new DoubleClickProvider() {
                @Override
                public void onDoubleClick(Widget widget, Point point, boolean bln) {
                    ((FlowWidget) widget).openDiagram();
                }
            });
        } else {
            doubleClickAction = new DoubleClickAction(new DoubleClickProvider() {
                @Override
                public void onDoubleClick(Widget widget, Point point, boolean bln) {
                    nodeWidget.showProperties();
                    nodeWidget.getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);
                }
            });
        }
        WidgetAction selectAction = ActionFactory.createSelectAction(new NodeWidgetSelectProvider(nodeWidget.getModelerScene()));
        WidgetAction moveAction = null;
        if (nodeWidget instanceof SplitGatewayWidget) {
            moveAction = new MoveAction(nodeWidget, null, new SplitMoveProvider(nodeWidget.getModelerScene()), alignStrategyProvider, alignStrategyProvider);
        } else {
            moveAction = new MoveAction(nodeWidget, null, new MultiMoveProvider(nodeWidget.getModelerScene()), alignStrategyProvider, alignStrategyProvider);
        }
        WidgetAction popupMenuAction = ActionFactory.createPopupMenuAction(nodeWidget.getPopupMenuProvider());
        NodeWidgetResizeProvider nodeWidgetResizeProvider = new NodeWidgetResizeProvider();
        WidgetAction resizeAction = ActionFactory.createResizeAction(nodeWidgetResizeProvider, nodeWidgetResizeProvider);
        WidgetAction snapMoveAction = ActionFactory.createMoveAction(ActionFactory.createSnapToGridMoveStrategy(5, 5), null);
        WidgetAction.Chain selectActionTool = nodeWidget.createActions(DesignerTools.SELECT);
        selectActionTool.addAction(doubleClickAction);
        selectActionTool.addAction(selectAction);
        selectActionTool.addAction(moveAction);
        selectActionTool.addAction(getScene().createWidgetHoverAction());
        selectActionTool.addAction(popupMenuAction);
        ((NodeWidget) nodeWidget).getActions().addAction(resizeAction);
        selectActionTool.addAction(snapMoveAction);
    }
}
