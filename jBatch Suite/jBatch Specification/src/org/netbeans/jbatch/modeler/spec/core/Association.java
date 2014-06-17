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
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tAssociation")
public class Association
        extends Artifact {

    @XmlAttribute(required = true)
    protected String sourceRef;//QName sourceRef;
    @XmlAttribute(required = true)
    protected String targetRef;//QName targetRef;
    @XmlAttribute
    protected AssociationDirection associationDirection;

    /**
     * Gets the value of the sourceRef property.
     *
     * @return possible object is {@link QName }
     *
     */
    public String getSourceRef() {
        return sourceRef;
    }

    /**
     * Sets the value of the sourceRef property.
     *
     * @param value allowed object is {@link QName }
     *
     */
    public void setSourceRef(String value) {
        this.sourceRef = value;
    }

    /**
     * Gets the value of the targetRef property.
     *
     * @return possible object is {@link QName }
     *
     */
    public String getTargetRef() {
        return targetRef;
    }

    /**
     * Sets the value of the targetRef property.
     *
     * @param value allowed object is {@link QName }
     *
     */
    public void setTargetRef(String value) {
        this.targetRef = value;
    }

    /**
     * Gets the value of the associationDirection property.
     *
     * @return possible object is {@link AssociationDirection }
     *
     */
    public AssociationDirection getAssociationDirection() {
        if (associationDirection == null) {
            return AssociationDirection.NONE;
        } else {
            return associationDirection;
        }
    }

    /**
     * Sets the value of the associationDirection property.
     *
     * @param value allowed object is {@link AssociationDirection }
     *
     */
    public void setAssociationDirection(AssociationDirection value) {
        this.associationDirection = value;
    }

}
