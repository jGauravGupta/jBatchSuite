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

@XmlRootElement(name = "BatchShape", namespace = "http://jbatchsuite.java.net")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BatchShape", propOrder = {
    "batchLabel"})
public class BatchShape
        extends LabeledShape {

    @XmlElement(name = "BatchLabel")
    protected BatchLabel batchLabel;
    @XmlAttribute
    protected String batchElement;
    @XmlAttribute
    protected Boolean isHorizontal;
    @XmlAttribute
    protected Boolean isExpanded;

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
     * Gets the value of the isHorizontal property.
     *
     * @return possible object is {@link Boolean }
     *
     */
    public Boolean isIsHorizontal() {
        return isHorizontal;
    }

    /**
     * Sets the value of the isHorizontal property.
     *
     * @param value allowed object is {@link Boolean }
     *
     */
    public void setIsHorizontal(Boolean value) {
        this.isHorizontal = value;
    }

    /**
     * Gets the value of the isExpanded property.
     *
     * @return possible object is {@link Boolean }
     *
     */
    public Boolean isIsExpanded() {
        return isExpanded;
    }

    /**
     * Sets the value of the isExpanded property.
     *
     * @param value allowed object is {@link Boolean }
     *
     */
    public void setIsExpanded(Boolean value) {
        this.isExpanded = value;
    }

}
