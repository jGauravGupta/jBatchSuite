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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import org.netbeans.jbatch.modeler.spec.Job;
import org.netbeans.jbatch.modeler.spec.design.BatchDiagram;
import org.netbeans.modeler.specification.model.document.IDefinitionElement;

@XmlRootElement(name = "definitions", namespace = "http://jbatchsuite.java.net")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "definitions", propOrder = {
    "job",
    "batchDiagram",})
public class Definitions implements IDefinitionElement {

    @XmlElement
    private Job job;

    @XmlElement(name = "batch-diagram", namespace = "http://jbatchsuite.java.net")
    private List<BatchDiagram> batchDiagram = new ArrayList<BatchDiagram>();
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute
    protected String name;
    @XmlAttribute
    private String _package;
    
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String targetNamespace = "";
    @XmlAttribute
    @XmlSchemaType(name = "anyURI")
    protected String expressionLanguage;
    @XmlAttribute
    protected String exporter;
    @XmlAttribute
    protected String exporterVersion;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the batchDiagram property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the batchDiagram property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBatchDiagram().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BatchDiagram }
     *
     *
     */
    public List<BatchDiagram> getJobDiagram() {
        if (getBatchDiagram() == null) {
            setBatchDiagram(new ArrayList<BatchDiagram>());
        }
        return this.getBatchDiagram();
    }

    public void addJobDiagram(BatchDiagram batchDiagram_In) {
        if (getBatchDiagram() == null) {
            setBatchDiagram(new ArrayList<BatchDiagram>());
        }
        this.getBatchDiagram().add(batchDiagram_In);
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the targetNamespace property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getTargetNamespace() {
        return targetNamespace;

    }

    /**
     * Sets the value of the targetNamespace property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setTargetNamespace(String value) {
        this.targetNamespace = value;
    }

    /**
     * Gets the value of the expressionLanguage property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getExpressionLanguage() {
        if (expressionLanguage == null) {
            return "http://www.w3.org/1999/XPath";
        } else {
            return expressionLanguage;
        }
    }

    /**
     * Sets the value of the expressionLanguage property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setExpressionLanguage(String value) {
        this.expressionLanguage = value;
    }

    /**
     * Gets the value of the exporter property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getExporter() {
        return exporter;
    }

    /**
     * Sets the value of the exporter property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setExporter(String value) {
        this.exporter = value;
    }

    /**
     * Gets the value of the exporterVersion property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getExporterVersion() {
        return exporterVersion;
    }

    /**
     * Sets the value of the exporterVersion property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setExporterVersion(String value) {
        this.exporterVersion = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed
     * property on this class.
     *
     * <p>
     * the map is keyed by the name of the attribute and the value is the string
     * value of the attribute.
     *
     * the map returned by this method is live, and you can add new attribute by
     * updating the map directly. Because of this design, there's no setter.
     *
     *
     * @return always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

    /**
     * @return the job
     */
    public Job getJob() {
        return job;
    }

    /**
     * @param job the job to set
     */
    public void setJob(Job job) {
        this.job = job;
    }

    /**
     * @return the batchDiagram
     */
    public List<BatchDiagram> getBatchDiagram() {
        return batchDiagram;
    }

    /**
     * @param batchDiagram the batchDiagram to set
     */
    public void setBatchDiagram(List<BatchDiagram> batchDiagram) {
        this.batchDiagram = batchDiagram;
    }

    public void addBatchDiagram(BatchDiagram batchDiagram) {
        this.batchDiagram.add(batchDiagram);
    }

    /**
     * @return the _package
     */
    public String getPackage() {
        return _package;
    }

    /**
     * @param _package the _package to set
     */
    public void setPackage(String _package) {
        this._package = _package;
    }

}
