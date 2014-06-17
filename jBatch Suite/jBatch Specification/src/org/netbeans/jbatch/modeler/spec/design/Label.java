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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Label", propOrder = {
    "bounds"
})
@XmlSeeAlso({
    BatchLabel.class
})
public abstract class Label
        extends Node {

    @XmlElement(name = "Bounds")
    protected Bounds bounds;

    /**
     * Gets the value of the bounds property.
     *
     * @return possible object is {@link Bounds }
     *
     */
    public Bounds getBounds() {
        return bounds;
    }

    /**
     * Sets the value of the bounds property.
     *
     * @param value allowed object is {@link Bounds }
     *
     */
    public void setBounds(Bounds value) {
        this.bounds = value;
    }

}
