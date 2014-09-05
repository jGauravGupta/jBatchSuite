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
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "splitter-out")
@XmlRootElement(name = "splitter-out")
public class SplitterOutputConnection extends SplitterConnection {

    @XmlAttribute
    private String inputConnectionRef;

    /**
     * @return the splitterInputConnectionRef
     */
    public String getInputConnectionRef() {
        return inputConnectionRef;
    }

    /**
     * @param inputConnectionRef the splitterInputConnectionRef to set
     */
    public void setInputConnectionRef(String inputConnectionRef) {
        this.inputConnectionRef = inputConnectionRef;
    }
}
