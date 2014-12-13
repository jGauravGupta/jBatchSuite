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

import java.util.ArrayList;
import java.util.List;
import org.netbeans.jbatch.modeler.spec.Chunk;
import org.netbeans.jbatch.modeler.spec.Step;
import org.netbeans.jbatch.modeler.specification.model.job.util.JobUtil;
import org.netbeans.modeler.config.element.ElementConfigFactory;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.entity.ComboBoxValue;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.listener.ActionHandler;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.listener.ComboBoxListener;
import org.netbeans.modeler.properties.entity.custom.editor.combobox.client.support.ComboBoxPropertySupport;
import org.netbeans.modeler.properties.nentity.NEntityPropertySupport;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;

public class ChunkWidget extends StepWidget {

    public ChunkWidget(IModelerScene scene, NodeWidgetInfo node) {
        super(scene, node);
    }

    @Override
    public void createPropertySet(ElementPropertySet set) {
        super.createPropertySet(set);
        set.put("BASIC_PROP", getCheckPointPolicyProperty());
        Step stepSpec = (Step) this.getBaseElementSpec();

        set.createPropertySet( this , ((Step) this.getBaseElementSpec()).getChunk(), getPropertyChangeListeners());
        set.createPropertySet( this , ((Step) this.getBaseElementSpec()).getChunk().getReader(), getPropertyChangeListeners());
        set.put("READER_PROP",new NEntityPropertySupport(this.getModelerScene().getModelerFile(),JobUtil.addProperty(stepSpec.getChunk().getReader().getProperties())));

        set.createPropertySet( this , ((Step) this.getBaseElementSpec()).getChunk().getProcessor(), getPropertyChangeListeners());
        set.put("PROCESSOR_PROP", new NEntityPropertySupport(this.getModelerScene().getModelerFile(),JobUtil.addProperty(stepSpec.getChunk().getProcessor().getProperties())));

        set.createPropertySet( this , ((Step) this.getBaseElementSpec()).getChunk().getWriter(), getPropertyChangeListeners());
        set.put("WRITER_PROP", new NEntityPropertySupport(this.getModelerScene().getModelerFile(),JobUtil.addProperty(stepSpec.getChunk().getWriter().getProperties())));
    }

    private ComboBoxPropertySupport getCheckPointPolicyProperty() {
        final ChunkWidget chunkWidget = this;
        final Chunk chunkSpec = (Chunk) ((Step) chunkWidget.getBaseElementSpec()).getChunk();
        ComboBoxListener comboBoxListener = new ComboBoxListener() {
            @Override
            public void setItem(ComboBoxValue value) {
                chunkSpec.setCheckpointPolicy((String) value.getValue());
            }

            @Override
            public ComboBoxValue getItem() {
                if ("item".equals(chunkSpec.getCheckpointPolicy())) {
                    return new ComboBoxValue("item", "Item");
                } else if ("custom".equals(chunkSpec.getCheckpointPolicy())) {
                    return new ComboBoxValue("custom", "Custom");
                } else {
                    return new ComboBoxValue("item", "Item");
                }
            }

            @Override
            public List<ComboBoxValue> getItemList() {
                List<ComboBoxValue> values = new ArrayList<ComboBoxValue>();
                values.add(new ComboBoxValue("item", "Item"));
                values.add(new ComboBoxValue("custom", "Custom"));
                return values;
            }

            @Override
            public String getDefaultText() {
                return "Item";
            }

            @Override
            public ActionHandler getActionHandler() {
                return null;
            }
        };
        return new ComboBoxPropertySupport(this.getModelerScene().getModelerFile(), "checkpointPolicy", "Check Point Policy", "", comboBoxListener);
    }

}
