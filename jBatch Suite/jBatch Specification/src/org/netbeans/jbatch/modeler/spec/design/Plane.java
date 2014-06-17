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
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Plane complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * <complexType name="Plane">
 *   <complexContent>
 *     <extension base="{http://www.omg.org/spec/DD/20100524/DI}Node">
 *       <sequence>
 *         <element ref="{http://www.omg.org/spec/DD/20100524/DI}DiagramElement" maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *       <anyAttribute processContents='lax' namespace='##other'/>
 *     </extension>
 *   </complexContent>
 * </complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Plane", propOrder = {
    "diagramElement"
})
@XmlSeeAlso({
    BatchPlane.class
})
public abstract class Plane
        extends Node {

    @XmlElements({
        @XmlElement(name = "batch-edge", namespace = "http://jbatchsuite.java.net", type = BatchEdge.class),
        @XmlElement(name = "batch-shape", namespace = "http://jbatchsuite.java.net", type = BatchShape.class)
    })
    protected List<DiagramElement> diagramElement;

    /**
     * Gets the value of the diagramElement property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the diagramElement property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDiagramElement().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list null null null null     {@link JAXBElement }{@code <}{@link DiagramElement }{@code >}
     * {@link JAXBElement }{@code <}{@link BatchShape }{@code >}
     * {@link JAXBElement }{@code <}{@link BatchEdge }{@code >}
     *
     *
     */
    public List<DiagramElement> getDiagramElement() {
        if (diagramElement == null) {
            diagramElement = new ArrayList<DiagramElement>();
        }
        return this.diagramElement;
    }

    public BatchShape getBatchShape(String batchElement) {
        BatchShape batchShape = null;
        if (diagramElement != null) {
            for (DiagramElement diagramElement_TMP : diagramElement) {
                if (diagramElement_TMP instanceof BatchShape) {
                    BatchShape batchShape_TMP = (BatchShape) diagramElement_TMP;
                    if (batchShape_TMP.getBatchElement().equals(batchElement)) {
                        batchShape = batchShape_TMP;
                        break;
                    }
                }
            }
        }
        return batchShape;
    }

    public void addDiagramElement(DiagramElement diagramElement_In) {
        if (diagramElement == null) {
            diagramElement = new ArrayList<DiagramElement>();
        }
        this.diagramElement.add(diagramElement_In);
    }

}
