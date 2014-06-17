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

@XmlRootElement(name = "textAnnotation")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tTextAnnotation", propOrder = {
    "text"
})
public class TextAnnotation
        extends Artifact {

    protected Text text;
    @XmlAttribute
    protected String textFormat = "text/plain";

    /**
     * Gets the value of the text property.
     *
     * @return possible object is {@link TText }
     *
     */
    public Text getText() {
        if (text == null) {
            text = new Text();
        }
        return text;
    }

    /**
     * Sets the value of the text property.
     *
     * @param value allowed object is {@link TText }
     *
     */
    public void setText(Text value) {
        this.text = value;
    }

    /**
     * Gets the value of the textFormat property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getTextFormat() {
        if (textFormat == null) {
            return "text/plain";
        } else {
            return textFormat;
        }
    }

    /**
     * Sets the value of the textFormat property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setTextFormat(String value) {
        this.textFormat = value;
    }
}
