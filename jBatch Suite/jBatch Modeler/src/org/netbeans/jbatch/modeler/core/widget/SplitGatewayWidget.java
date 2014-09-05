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

import org.netbeans.jbatch.core.widget.enums.SplitType;
import org.netbeans.jbatch.modeler.spec.core.SplitterInputConnection;
import org.netbeans.jbatch.modeler.spec.core.SplitterOutputConnection;
import org.netbeans.jbatch.modeler.specification.model.job.util.JobUtil;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.context.action.SceneConnectProvider;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;

/**
 *
 *
 *
 *
 */
public class SplitGatewayWidget extends GatewayWidget {

    public SplitGatewayWidget(IModelerScene scene, NodeWidgetInfo node) {
        super(scene, node);
    }
    private SplitType type;

    /**
     * @return the type
     */
    public SplitType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(SplitType type) {
        this.type = type;
    }

    public static void createSiblingConnection(IEdgeWidget edgeWidget, INodeWidget targetNodeWidget) {
        if (targetNodeWidget instanceof FlowWidget && edgeWidget instanceof SplitterInputConnectionWidget) {
            SplitterInputConnectionWidget inputConnectionWidget = (SplitterInputConnectionWidget) edgeWidget;
            SplitterInputConnection inputConnectionSpec = (SplitterInputConnection) inputConnectionWidget.getBaseElementSpec();
            if (inputConnectionSpec.getOutputConnectionRef() == null) {
                if (inputConnectionWidget.getSourceNode() instanceof DivergeSplitGatewayWidget) {
                    DivergeSplitGatewayWidget divergeWidget = (DivergeSplitGatewayWidget) inputConnectionWidget.getSourceNode();
                    if (!JobUtil.isConnectionExist((FlowWidget) targetNodeWidget, divergeWidget.getConvergeSplitGatewayWidget())) {
                        SceneConnectProvider connectProvider = new SceneConnectProvider();// ModelerUtil.getEdgeType() will decide EdgeType
                        SplitterOutputConnectionWidget outputConnectionWidget = (SplitterOutputConnectionWidget) connectProvider.createConnection(targetNodeWidget.getModelerScene(), targetNodeWidget, divergeWidget.getConvergeSplitGatewayWidget());
                        if (outputConnectionWidget != null) {
                            outputConnectionWidget.setInputConnectionWidget(inputConnectionWidget);
                            inputConnectionWidget.setOutputConnectionWidget(outputConnectionWidget);
                        }
                    }
                }
            }
        } else if (targetNodeWidget instanceof ConvergeSplitGatewayWidget && edgeWidget instanceof SplitterOutputConnectionWidget) {
            SplitterOutputConnectionWidget outputConnectionWidget = (SplitterOutputConnectionWidget) edgeWidget;
            SplitterOutputConnection outputConnectionSpec = (SplitterOutputConnection) outputConnectionWidget.getBaseElementSpec();
            if (outputConnectionSpec.getInputConnectionRef() == null) {
                if (outputConnectionWidget.getSourceNode() instanceof FlowWidget) {
                    FlowWidget flowWidget = (FlowWidget) outputConnectionWidget.getSourceNode();
                    DivergeSplitGatewayWidget divergeWidget = ((ConvergeSplitGatewayWidget) targetNodeWidget).getDivergeSplitGatewayWidget();
                    if (!JobUtil.isConnectionExist(divergeWidget, flowWidget)) {
                        SceneConnectProvider connectProvider = new SceneConnectProvider();// ModelerUtil.getEdgeType() will decide EdgeType
                        SplitterInputConnectionWidget inputConnectionWidget = (SplitterInputConnectionWidget) connectProvider.createConnection(targetNodeWidget.getModelerScene(), divergeWidget, flowWidget);
                        if (inputConnectionWidget != null) {
                            outputConnectionWidget.setInputConnectionWidget(inputConnectionWidget);
                            inputConnectionWidget.setOutputConnectionWidget(outputConnectionWidget);
                        }
                    }
                }
            }
        }
    }
}
