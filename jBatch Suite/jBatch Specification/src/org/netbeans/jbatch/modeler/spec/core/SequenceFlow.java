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
package org.netbeans.jbatch.modeler.spec.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.netbeans.modeler.specification.model.document.core.IFlowEdge;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sequenceFlow")
@XmlRootElement(name = "sequenceFlow")
@XmlSeeAlso({
    SplitterConnection.class
})
public class SequenceFlow
        extends FlowElement implements IFlowEdge {

    @XmlAttribute(required = true)
    protected String sourceRef;//Object sourceRef
    @XmlAttribute(required = true)
    protected String targetRef;//Object targetRef;

    @XmlAttribute
    private String on;

    /**
     * Gets the value of the sourceRef property.
     *
     * @return possible object is {@link Object }
     *
     */
    public String getSourceRef() {
        return sourceRef;
    }

    /**
     * Sets the value of the sourceRef property.
     *
     * @param value allowed object is {@link Object }
     *
     */
    public void setSourceRef(String value) {
        this.sourceRef = value;
    }

    /**
     * Gets the value of the targetRef property.
     *
     * @return possible object is {@link Object }
     *
     */
    public String getTargetRef() {
        return targetRef;
    }

    /**
     * Sets the value of the targetRef property.
     *
     * @param value allowed object is {@link Object }
     *
     */
    public void setTargetRef(String value) {
        this.targetRef = value;
    }

    /**
     * @return the on
     */
    public String getOn() {
        return on;
    }

    /**
     * @param on the inputStatus to set
     */
    public void setOn(String on) {
        this.on = on;
    }

}
