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
package org.netbeans.jbatch.modeler.spec.design;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.netbeans.modeler.specification.model.document.IDiagramElement;

@XmlRootElement(name = "BatchDiagram")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BatchDiagram", propOrder = {
    "batchPlane",
    "batchLabelStyle"
})
public class BatchDiagram
        extends Diagram implements IDiagramElement {

    @XmlElement(name = "batch-plane", namespace = "http://jbatchsuite.java.net", required = true)
    protected BatchPlane batchPlane;
    @XmlElement(name = "BatchLabelStyle")
    protected List<LabelStyle> batchLabelStyle;


    /**
     * Gets the value of the batchPlane property.
     *
     * @return possible object is {@link BatchPlane }
     *
     */
    public BatchPlane getBatchPlane() {
        return batchPlane;
    }

    /**
     * Sets the value of the batchPlane property.
     *
     * @param value allowed object is {@link BatchPlane }
     *
     */
    public void setBatchPlane(BatchPlane value) {
        this.batchPlane = value;
    }

    /**
     * Gets the value of the batchLabelStyle property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the batchLabelStyle property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBatchLabelStyle().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LabelStyle }
     *
     *
     */
    public List<LabelStyle> getBatchLabelStyle() {
        if (batchLabelStyle == null) {
            batchLabelStyle = new ArrayList<LabelStyle>();
        }
        return this.batchLabelStyle;
    }

}
