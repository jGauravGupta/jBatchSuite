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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tAssociationDirection.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * <
 * pre>
 * <simpleType name="tAssociationDirection">
 * <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 * <enumeration value="None"/>
 * <enumeration value="One"/>
 * <enumeration value="Both"/>
 * </restriction>
 * </simpleType>
 * </pre>
 *
 */
@XmlType(name = "associationDirection")
@XmlEnum
public enum AssociationDirection {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("One")
    ONE("One"),
    @XmlEnumValue("Both")
    BOTH("Both");
    private String value;

    AssociationDirection(String v) {
        value = v;
    }

    public String value() {
        return getValue();
    }

    public static AssociationDirection fromValue(String v) {
        for (AssociationDirection c : AssociationDirection.values()) {
            if (c.getValue().equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    /**
     * @return the value
     */
    public String getValue() {
        if (value == null || value.isEmpty()) {
            value = "None";
        }
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
