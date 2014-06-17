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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlRootElement(name = "BatchEdge", namespace = "http://jbatchsuite.java.net")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BatchEdge", propOrder = {
    "batchLabel"
})
public class BatchEdge
        extends LabeledEdge {

    @XmlElement(name = "BatchLabel")
    protected BatchLabel batchLabel;
    @XmlAttribute
    protected String batchElement;
    @XmlAttribute
    protected String sourceElement;
    @XmlAttribute
    protected String targetElement;

    /**
     * Gets the value of the batchLabel property.
     *
     * @return possible object is {@link BatchLabel }
     *
     */
    public BatchLabel getBatchLabel() {
        return batchLabel;
    }

    /**
     * Sets the value of the batchLabel property.
     *
     * @param value allowed object is {@link BatchLabel }
     *
     */
    public void setBatchLabel(BatchLabel value) {
        this.batchLabel = value;
    }

    /**
     * Gets the value of the batchElement property.
     *
     * @return possible object is {@link QName }
     *
     */
    public String getBatchElement() {
        return batchElement;
    }

    /**
     * Sets the value of the batchElement property.
     *
     * @param value allowed object is {@link QName }
     *
     */
    public void setBatchElement(String value) {
        this.batchElement = value;
    }

    /**
     * Gets the value of the sourceElement property.
     *
     * @return possible object is {@link QName }
     *
     */
    public String getSourceElement() {
        return sourceElement;
    }

    /**
     * Sets the value of the sourceElement property.
     *
     * @param value allowed object is {@link QName }
     *
     */
    public void setSourceElement(String value) {
        this.sourceElement = value;
    }

    /**
     * Gets the value of the targetElement property.
     *
     * @return possible object is {@link QName }
     *
     */
    public String getTargetElement() {
        return targetElement;
    }

    /**
     * Sets the value of the targetElement property.
     *
     * @param value allowed object is {@link QName }
     *
     */
    public void setTargetElement(String value) {
        this.targetElement = value;
    }

}
