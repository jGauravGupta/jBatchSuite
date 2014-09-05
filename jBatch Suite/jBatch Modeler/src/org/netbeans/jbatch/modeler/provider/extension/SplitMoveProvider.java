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
package org.netbeans.jbatch.modeler.provider.extension;

import java.awt.Point;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.jbatch.modeler.core.widget.ConvergeSplitGatewayWidget;
import org.netbeans.jbatch.modeler.core.widget.DivergeSplitGatewayWidget;
import org.netbeans.modeler.specification.model.document.IModelerScene;

public class SplitMoveProvider extends MultiMoveProvider {

    public SplitMoveProvider(IModelerScene scene) {
        super(scene);
    }

    @Override
    public void movementStarted(Widget widget) {
        if (widget instanceof DivergeSplitGatewayWidget) {
            DivergeSplitGatewayWidget divergeWidget = (DivergeSplitGatewayWidget) widget;
            if (divergeWidget.getConvergeSplitGatewayWidget() != null) {
                divergeWidget.getConvergeSplitGatewayWidget().setAnchorState(true);
            }
            divergeWidget.showConnector();
        } else if (widget instanceof ConvergeSplitGatewayWidget) {
            ConvergeSplitGatewayWidget convergeWidget = (ConvergeSplitGatewayWidget) widget;
            if (convergeWidget.getDivergeSplitGatewayWidget() != null) {
                convergeWidget.getDivergeSplitGatewayWidget().setAnchorState(true);
            }
            convergeWidget.getDivergeSplitGatewayWidget().showConnector();
        }
    }

    @Override
    public void movementFinished(Widget widget) {
        super.movementFinished(widget);
        if (widget instanceof DivergeSplitGatewayWidget) {
            DivergeSplitGatewayWidget divergeWidget = (DivergeSplitGatewayWidget) widget;
            if (divergeWidget.getConvergeSplitGatewayWidget() != null) {
                Point convergeLocation = divergeWidget.getConvergeSplitGatewayWidget().getPreferredLocation();
                divergeWidget.getScene().getSceneAnimator().animatePreferredLocation(divergeWidget.getConvergeSplitGatewayWidget(), new Point(convergeLocation.x, (int) divergeWidget.getPreferredLocation().getY()));
            }
            divergeWidget.hideConnector();
        } else if (widget instanceof ConvergeSplitGatewayWidget) {
            ConvergeSplitGatewayWidget convergeWidget = (ConvergeSplitGatewayWidget) widget;
            if (convergeWidget.getDivergeSplitGatewayWidget() != null) {
                Point divergeLocation = convergeWidget.getDivergeSplitGatewayWidget().getPreferredLocation();
                convergeWidget.getScene().getSceneAnimator().animatePreferredLocation(convergeWidget.getDivergeSplitGatewayWidget(), new Point(divergeLocation.x, (int) convergeWidget.getPreferredLocation().getY()));
            }
            convergeWidget.getDivergeSplitGatewayWidget().hideConnector();
        }
//        if (widget instanceof DivergeSplitGatewayWidget) {
//            DivergeSplitGatewayWidget divergeWidget = (DivergeSplitGatewayWidget) widget;
//            divergeWidget.getConvergeSplitGatewayWidget().setAnchorState(false);
//        }

    }

//        @Override
//    public void setNewLocation(Widget widget, Point location) {
//        if (widget instanceof DivergeSplitGatewayWidget) {
//            DivergeSplitGatewayWidget divergeWidget = (DivergeSplitGatewayWidget) widget;
//            Point convergeLocation = divergeWidget.getConvergeSplitGatewayWidget().getPreferredLocation();
//            Point divergeLocation = divergeWidget.getPreferredLocation();
//
//            if (divergeLocation.getX() + divergeWidget.getSceneViewBound().getWidth() + 20 < convergeLocation.getX()) {
//                super.setNewLocation(widget, location);
//            } else {
//                super.setNewLocation(widget, divergeLocation);
//            }
//        } else {
//            super.setNewLocation(widget, location);
//        }
//    }
}
