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

import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.jbatch.modeler.spec.core.SplitterInputConnection;
import org.netbeans.jbatch.modeler.spec.core.SplitterOutputConnection;
import org.netbeans.modeler.anchorshape.IconAnchorShape;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.openide.util.ImageUtilities;

public class SplitterInputConnectionWidget extends SplitterConnectionWidget {

    private SplitterOutputConnectionWidget outputConnectionWidget;
    private static final AnchorShape SOURCE_ANCHOR_SHAPE = new IconAnchorShape(ImageUtilities.loadImage("org/netbeans/jbatch/modeler/resource/image/RECIEVER.png"), true, 16, 5);
    private static final AnchorShape TARGET_ANCHOR_SHAPE = new IconAnchorShape(ImageUtilities.loadImage("org/netbeans/jbatch/modeler/resource/image/CONNECTOR.png"), true, 16, 15);

    public SplitterInputConnectionWidget(IModelerScene scene, EdgeWidgetInfo edge) {
        super(scene, edge);
        setSourceAnchorShape(SOURCE_ANCHOR_SHAPE);
        setTargetAnchorShape(TARGET_ANCHOR_SHAPE);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (outputConnectionWidget != null) {
            outputConnectionWidget.setInputConnectionWidget(null);
            outputConnectionWidget.remove();
            outputConnectionWidget = null;
        }
    }

    /**
     * @return the outputConnectionWidget
     */
    public SplitterOutputConnectionWidget getOutputConnectionWidget() {
        return outputConnectionWidget;
    }

    /**
     * @param outputConnectionWidget the outputConnectionWidget to set
     */
    public void setOutputConnectionWidget(SplitterOutputConnectionWidget outputConnectionWidget) {
        this.outputConnectionWidget = outputConnectionWidget;
        SplitterInputConnection inputConnectionSpec = (SplitterInputConnection) this.getBaseElementSpec();
        if (outputConnectionWidget != null) {
            SplitterOutputConnection outputConnectionSpec = (SplitterOutputConnection) outputConnectionWidget.getBaseElementSpec();
            inputConnectionSpec.setOutputConnectionRef(outputConnectionSpec.getId());
        } else {
            inputConnectionSpec.setOutputConnectionRef(null);
        }
    }

}
