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
@XmlType(name = "tGroup")
public class Group
        extends Artifact {

    @XmlAttribute
    protected QName categoryValueRef;

    /**
     * Gets the value of the categoryValueRef property.
     *
     * @return possible object is {@link QName }
     *
     */
    public QName getCategoryValueRef() {
        return categoryValueRef;
    }

    /**
     * Sets the value of the categoryValueRef property.
     *
     * @param value allowed object is {@link QName }
     *
     */
    public void setCategoryValueRef(QName value) {
        this.categoryValueRef = value;
    }

}
