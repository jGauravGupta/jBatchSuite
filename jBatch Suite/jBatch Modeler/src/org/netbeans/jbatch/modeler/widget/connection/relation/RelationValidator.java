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
package org.netbeans.jbatch.modeler.widget.connection.relation;

import org.netbeans.jbatch.modeler.core.widget.ConvergeSplitGatewayWidget;
import org.netbeans.jbatch.modeler.core.widget.DivergeSplitGatewayWidget;
import org.netbeans.jbatch.modeler.core.widget.EndEventWidget;
import org.netbeans.jbatch.modeler.core.widget.FlowWidget;
import org.netbeans.jbatch.modeler.core.widget.SplitGatewayWidget;
import org.netbeans.jbatch.modeler.core.widget.SplitterInputConnectionWidget;
import org.netbeans.jbatch.modeler.core.widget.StartEventWidget;
import org.netbeans.jbatch.modeler.core.widget.StepWidget;
import org.netbeans.modeler.widget.connection.relation.IRelationProxy;
import org.netbeans.modeler.widget.connection.relation.IRelationValidator;

/**
 * RelationValidator manages the dispatch of relation validation events, while
 * also having the meta layer validate the proposed relationship against rules.
 */
public class RelationValidator implements IRelationValidator {

    /**
     * Called to validate the proposed relationship. Calling this method will
     * result in the firing of the IRelationValidatorEventsSink methods.
     *
     * @param proxy[in] The proxy to validate
     *
     * @return HRESULT
     *
     */
    @Override
    public boolean validateRelation(IRelationProxy proxy) {
        if (proxy.getTarget() == proxy.getSource()) {
            return false;
        } else if (proxy.getTarget() instanceof StartEventWidget || proxy.getSource() instanceof EndEventWidget) {
            return false;
        } else if (proxy.getSource() instanceof StartEventWidget && proxy.getTarget() instanceof EndEventWidget) {
            return false;
        } else if (proxy.getSource() instanceof SplitGatewayWidget && proxy.getTarget() instanceof SplitGatewayWidget) {
            return false;
        }

        if (proxy.getSource() instanceof StepWidget) {
            StepWidget stepWidget = (StepWidget) proxy.getSource();
            if (!stepWidget.getOutgoingSequenceFlows().isEmpty()) {
                return false;
            }
        } else if (proxy.getSource() instanceof FlowWidget) {
            FlowWidget sourceFlowWidget = (FlowWidget) proxy.getSource();
            if (sourceFlowWidget.isAddedToSplitter()) {
                return false;
            }
        } else if (proxy.getSource() instanceof DivergeSplitGatewayWidget) {
            if (proxy.getTarget() instanceof FlowWidget) {
                FlowWidget flowWidget = (FlowWidget) proxy.getTarget();
                if (!flowWidget.getIncomingSequenceFlows().isEmpty()) { //is not connected to any other
                    return false;
                }
//                for (SequenceFlowWidget sequenceFlowWidget : flowWidget.getOutgoingSequenceFlows()) {
//                    if (!(sequenceFlowWidget instanceof SplitterOutputConnectionWidget)) {
//                        return false;
//                    }
//                }
                if (flowWidget.isAddedToSplitter()) {
                    return false;
                }
            } else {
                return false;
            }

        }

        if (proxy.getTarget() instanceof FlowWidget) {
            FlowWidget targetFlowWidget = (FlowWidget) proxy.getTarget();
            if (targetFlowWidget.isAddedToSplitter()) {
                return false;
            }
        } else if (proxy.getTarget() instanceof ConvergeSplitGatewayWidget) {
            if (proxy.getSource() instanceof FlowWidget) {
                FlowWidget flowWidget = (FlowWidget) proxy.getSource();
                if (!flowWidget.getOutgoingSequenceFlows().isEmpty()) { //is not connected to any other
                    return false;
                }
                if (flowWidget.isAddedToSplitter()) {
                    return false;
                } else if (!flowWidget.getIncomingSequenceFlows().isEmpty() && !(flowWidget.getIncomingSequenceFlows().get(0) instanceof SplitterInputConnectionWidget)) {
                    return false;
                }
//                if (!flowWidget.getOutgoingSequenceFlows().isEmpty()) {
//                    return false;
//                }
//                for (SequenceFlowWidget sequenceFlowWidget : flowWidget.getIncomingSequenceFlows()) {
//                    if (!(sequenceFlowWidget instanceof SplitterInputConnectionWidget)) {
//                        return false;
//                    }
//                }
            } else {
                return false;
            }
        }
        return true;
    }
}
