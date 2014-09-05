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

import org.netbeans.jbatch.modeler.spec.core.Converge;
import org.netbeans.jbatch.modeler.spec.core.Diverge;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;

/**
 *
 *
 *
 *
 */
public class ConvergeSplitGatewayWidget extends SplitGatewayWidget {

    private DivergeSplitGatewayWidget divergeSplitGatewayWidget;

    public ConvergeSplitGatewayWidget(IModelerScene scene, NodeWidgetInfo node) {
        super(scene, node);
    }

//    public void removeIncomingSequenceFlow(SequenceFlowWidget outgoingSequenceFlow) {
//        super.removeIncomingSequenceFlow(outgoingSequenceFlow);
//        if (this.getIncomingSequenceFlows().size() == 0) {
//            divergeSplitGatewayWidget.remove();
//        }
//    }
    @Override
    public void destroy() {
        super.destroy();
        if (divergeSplitGatewayWidget != null) {
            divergeSplitGatewayWidget.setConvergeSplitGatewayWidget(null);
            divergeSplitGatewayWidget.remove();
            divergeSplitGatewayWidget = null;
        }
    }

    /**
     * @return the divergeSplitGatewayWidget
     */
    public DivergeSplitGatewayWidget getDivergeSplitGatewayWidget() {
        return divergeSplitGatewayWidget;
    }

    /**
     * @param divergeSplitGatewayWidget the divergeSplitGatewayWidget to set
     */
    public void setDivergeSplitGatewayWidget(DivergeSplitGatewayWidget divergeSplitGatewayWidget) {
        this.divergeSplitGatewayWidget = divergeSplitGatewayWidget;
        Converge converge = (Converge) this.getBaseElementSpec();
        if (divergeSplitGatewayWidget != null) {
            Diverge diverge = (Diverge) divergeSplitGatewayWidget.getBaseElementSpec();
            converge.setDivergeRef(diverge.getId());
        } else {
            converge.setDivergeRef(null);
        }
    }
}
