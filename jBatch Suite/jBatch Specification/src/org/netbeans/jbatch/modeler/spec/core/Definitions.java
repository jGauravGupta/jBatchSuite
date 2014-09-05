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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FileUtils;
import org.netbeans.jbatch.modeler.spec.Job;
import org.netbeans.jbatch.modeler.spec.design.BatchDiagram;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.shape.ShapeDesign;
import org.netbeans.modeler.specification.model.document.IDefinitionElement;
import org.netbeans.modeler.validation.jaxb.ValidateJAXB;
import org.openide.util.Exceptions;

@XmlRootElement(name = "definitions", namespace = "http://jbatchsuite.java.net")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "definitions", propOrder = {
    "job",
    "batchDiagram",})
public class Definitions implements IDefinitionElement {

    @XmlAttribute
    private BatchArtifactLoader batchArtifactLoaderType;
    @XmlElement
    private Job job;
    @XmlElement(name = "batch-diagram", namespace = "http://jbatchsuite.java.net")
    private List<BatchDiagram> batchDiagram = new ArrayList<BatchDiagram>();
    @XmlAttribute
    protected String name;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute
    private String _package;
    @XmlAttribute
    @XmlSchemaType(name = "anyURI")
    protected String expressionLanguage;
    @XmlAttribute
    protected String exporter;
    @XmlAttribute
    protected String exporterVersion;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    @XmlTransient
    private List<String> garbageDefinitions = new ArrayList<String>();

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
        if (batchDiagram == null) {
            batchDiagram = new ArrayList<BatchDiagram>();
        }
        return batchDiagram;
    }

    public void addJobDiagram(BatchDiagram batchDiagram_In) {
        if (batchDiagram == null) {
            batchDiagram = new ArrayList<BatchDiagram>();
        }
        this.batchDiagram.add(batchDiagram_In);
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
//    public List<BatchDiagram> getBatchDiagram() {
//        return batchDiagram;
//    }
//
//    /**
//     * @param batchDiagram the batchDiagram to set
//     */
//    public void setBatchDiagram(List<BatchDiagram> batchDiagram) {
//        this.batchDiagram = batchDiagram;
//    }
//
//    public void addBatchDiagram(BatchDiagram batchDiagram) {
//        this.batchDiagram.add(batchDiagram);
//    }
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

    public static Definitions load(ModelerFile file, String definitionId) {
        File savedFile = file.getFile();
        Definitions definition_Load = null;
        boolean definitionExist = false;
        XMLStreamReader xsr = null;
        try {
            if (savedFile.length() != 0) {

                XMLInputFactory xif = XMLInputFactory.newFactory();
                StreamSource xml = new StreamSource(savedFile);
                xsr = xif.createXMLStreamReader(xml);
                xsr.nextTag();
                if (definitionId == null) {
                    while (xsr.hasNext() && !definitionExist) {
                        if (xsr.getEventType() == XMLStreamConstants.START_ELEMENT && xsr.getLocalName().equals("definitions") && xsr.getAttributeValue(null, "id") == null) {
                            definitionExist = true;
                        } else {
                            xsr.next();
                        }
                    }
                } else {
                    while (xsr.hasNext() && !definitionExist) {
                        if (xsr.getEventType() == XMLStreamConstants.START_ELEMENT && xsr.getLocalName().equals("definitions") && definitionId.equals(xsr.getAttributeValue(null, "id"))) {
                            definitionExist = true;
                        } else {
                            xsr.next();
                        }
                    }
                }

            }
            JAXBContext jobContext;
            Unmarshaller jobUnmarshaller;
//            if (jobContext == null) {
            jobContext = JAXBContext.newInstance(new Class<?>[]{ShapeDesign.class, Definitions.class});
//            }
//            if (jobUnmarshaller == null) {
            jobUnmarshaller = jobContext.createUnmarshaller();
            jobUnmarshaller.setEventHandler(new ValidateJAXB());
//            }
            if (definitionExist) {
                definition_Load = jobUnmarshaller.unmarshal(xsr, Definitions.class).getValue();//new StreamSource(savedFile)
            }
            if (xsr != null) {
                xsr.close();
            }

        } catch (XMLStreamException ex) {
            Exceptions.printStackTrace(ex);
        } catch (JAXBException ex) {
//            io.getOut().println("Exception: " + ex.toString());
            ex.printStackTrace();
            System.out.println("Document XML Not Exist");
        }
        return definition_Load;
    }

    public static void unload(ModelerFile file, String definitionId) {
        List<String> definitionIdList = new ArrayList<String>();
        definitionIdList.add(definitionId);
        unload(file, definitionIdList);
    }

    public static void unload(ModelerFile file, List<String> definitionIdList) {
        File savedFile = file.getFile();
        if (definitionIdList.isEmpty()) {
            return;
        }
        try {
            File cloneSavedFile = File.createTempFile("TMP", "job");
            FileUtils.copyFile(savedFile, cloneSavedFile);

            BufferedReader br = new BufferedReader(new FileReader(savedFile));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println("pre savedFile : " + line);
            }

            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            XMLStreamWriter xsw = xof.createXMLStreamWriter(new FileWriter(savedFile));
            xsw.setDefaultNamespace("http://jbatchsuite.java.net");

            xsw.writeStartDocument();
            xsw.writeStartElement("jbatchnb", "root", "http://jbatchsuite.java.net");
            xsw.writeNamespace("jbatch", "http://xmlns.jcp.org/xml/ns/javaee");
            xsw.writeNamespace("jbatchnb", "http://jbatchsuite.java.net");
            xsw.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            xsw.writeNamespace("java", "http://jcp.org/en/jsr/detail?id=270");
            xsw.writeNamespace("nbm", "http://nbmodeler.java.net");

            if (cloneSavedFile.length() != 0) {
                try {
                    XMLInputFactory xif = XMLInputFactory.newFactory();
                    StreamSource xml = new StreamSource(cloneSavedFile);
                    XMLStreamReader xsr = xif.createXMLStreamReader(xml);
                    xsr.nextTag();
                    while (xsr.getEventType() == XMLStreamConstants.START_ELEMENT) {
//                        Def   Y    N
//                        Tag   N(D) Y(D)
//                        ________________
//                              T    T
//                        ----------------
//
//                        Def   Y    N
//                        Tag   Y(S) N(S)
//                        ________________
//                              S    S
//                        ----------------
//
//                        Def   Y    N
//                        Tag   Y(D) N(S)
//                        ________________
//                              T    S
//                        ----------------
//
//                       (D) => Different
//                       (S) => Same
//                        Y => Id Exist
//                        N => Def Id is null
//                        T => Transform
//                        S => Skip

                        if (xsr.getLocalName().equals("definitions")) {
//                            if (definitionId == null) {
//                                if (xsr.getAttributeValue(null, "id") != null) {
//                                    transformXMLStream(xsr, xsw);
//                                }
//                            } else {
                            if (xsr.getAttributeValue(null, "id") == null) {
                                System.out.println("transformXMLStream " + null);
                                transformXMLStream(xsr, xsw);
                            } else {
                                if (!definitionIdList.contains(xsr.getAttributeValue(null, "id"))) {
                                    System.out.println("transformXMLStream " + xsr.getAttributeValue(null, "id"));
                                    transformXMLStream(xsr, xsw);
                                } else {
                                    System.out.println("skipXMLStream " + xsr.getAttributeValue(null, "id"));
                                    skipXMLStream(xsr);
                                }
                            }
//                            }
                        }
                        System.out.println("pre xsr.getEventType() : " + xsr.getEventType() + "  " + xsr.getLocalName());
                        xsr.nextTag();
                        System.out.println("post xsr.getEventType() : " + xsr.getEventType() + "  " + xsr.getLocalName());
                    }
                } catch (XMLStreamException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            xsw.writeEndDocument();
            xsw.close();

            br = new BufferedReader(new FileReader(savedFile));
            line = null;
            while ((line = br.readLine()) != null) {
                System.out.println("post savedFile : " + line);
            }

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (XMLStreamException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    static void transformXMLStream(XMLStreamReader xmlStreamReader, XMLStreamWriter xmlStreamWriter) {
        try {
//            TransformerFactory tf = TransformerFactory.newInstance();
//            Transformer t = tf.newTransformer();
//            StAXSource source = new StAXSource(xmlStreamReader);
//            StAXResult result = new StAXResult(xmlStreamWriter);
//            t.transform(source, result);
            System.out.println("Defnition Id : " + xmlStreamReader.getAttributeValue(null, "id"));
            boolean finish = false;
            while (xmlStreamReader.hasNext() && !finish) {
                switch (xmlStreamReader.getEventType()) {
                    case XMLEvent.START_ELEMENT:
                        String prefix = xmlStreamReader.getPrefix();
                        String namespaceURI = xmlStreamReader.getNamespaceURI();
                        if (namespaceURI != null) {
                            if (prefix != null) {
                                xmlStreamWriter.writeStartElement(xmlStreamReader.getPrefix(),
                                        xmlStreamReader.getLocalName(),
                                        xmlStreamReader.getNamespaceURI());
                            } else {
                                xmlStreamWriter.writeStartElement(xmlStreamReader.getNamespaceURI(),
                                        xmlStreamReader.getLocalName());
                            }
                        } else {
                            xmlStreamWriter.writeStartElement(xmlStreamReader.getLocalName());
                        }

                        for (int i = 0; i < xmlStreamReader.getNamespaceCount(); i++) {
                            xmlStreamWriter.writeNamespace(xmlStreamReader.getNamespacePrefix(i),
                                    xmlStreamReader.getNamespaceURI(i));
                        }
                        int count = xmlStreamReader.getAttributeCount();
                        for (int i = 0; i < count; i++) {
//                            xmlStreamWriter.writeAttribute(xmlStreamReader.getAttributePrefix(i),
//                                    xmlStreamReader.getAttributeNamespace(i),
//                                    xmlStreamReader.getAttributeLocalName(i),
//                                    xmlStreamReader.getAttributeValue(i));

                            String attrNamespaceURI = xmlStreamReader.getAttributeNamespace(i), attrPrefix = xmlStreamReader.getAttributePrefix(i);
                            if (attrNamespaceURI != null) {
                                if (attrPrefix != null) {
                                    xmlStreamWriter.writeAttribute(attrPrefix, attrNamespaceURI,
                                            xmlStreamReader.getAttributeLocalName(i),
                                            xmlStreamReader.getAttributeValue(i));
                                } else {
                                    xmlStreamWriter.writeAttribute(attrNamespaceURI,
                                            xmlStreamReader.getAttributeLocalName(i),
                                            xmlStreamReader.getAttributeValue(i));
                                }
                            } else {
                                xmlStreamWriter.writeAttribute(xmlStreamReader.getAttributeLocalName(i),
                                        xmlStreamReader.getAttributeValue(i));
                            }

                        }
                        break;
                    case XMLEvent.END_ELEMENT:
                        xmlStreamWriter.writeEndElement();
                        if (xmlStreamReader.getLocalName().equals("definitions")) {
                            finish = true;
                        }
                        break;
                    case XMLEvent.SPACE:
                    case XMLEvent.CHARACTERS:
                        xmlStreamWriter.writeCharacters(xmlStreamReader.getTextCharacters(),
                                xmlStreamReader.getTextStart(),
                                xmlStreamReader.getTextLength());
                        break;
                    case XMLEvent.PROCESSING_INSTRUCTION:
                        xmlStreamWriter.writeProcessingInstruction(xmlStreamReader.getPITarget(),
                                xmlStreamReader.getPIData());
                        break;
                    case XMLEvent.CDATA:
                        xmlStreamWriter.writeCData(xmlStreamReader.getText());
                        break;

                    case XMLEvent.COMMENT:
                        xmlStreamWriter.writeComment(xmlStreamReader.getText());
                        break;
                    case XMLEvent.ENTITY_REFERENCE:
                        xmlStreamWriter.writeEntityRef(xmlStreamReader.getLocalName());
                        break;
                    case XMLEvent.START_DOCUMENT:
                        String encoding = xmlStreamReader.getCharacterEncodingScheme();
                        String version = xmlStreamReader.getVersion();

                        if (encoding != null && version != null) {
                            xmlStreamWriter.writeStartDocument(encoding,
                                    version);
                        } else if (version != null) {
                            xmlStreamWriter.writeStartDocument(xmlStreamReader.getVersion());
                        }
                        break;
                    case XMLEvent.END_DOCUMENT:
                        xmlStreamWriter.writeEndDocument();
                        break;
                    case XMLEvent.DTD:
                        xmlStreamWriter.writeDTD(xmlStreamReader.getText());
                        break;

                }
                if (!finish) {
                    xmlStreamReader.next();
                }
            }
        } catch (XMLStreamException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    static void skipXMLStream(XMLStreamReader xmlStreamReader) {
        try {
            boolean finish = false;
            while (xmlStreamReader.hasNext() && !finish) {
                switch (xmlStreamReader.getEventType()) {
                    case XMLEvent.END_ELEMENT:
                        if (xmlStreamReader.getLocalName().equals("definitions")) {
                            finish = true;
                        }
                        break;
                }
                if (!finish) {
                    xmlStreamReader.next();
                }
            }
        } catch (XMLStreamException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * @return the garbageDefinitions
     */
    public List<String> getGarbageDefinitions() {
        return garbageDefinitions;
    }

    /**
     * @param garbageDefinitions the garbageDefinitions to set
     */
    public void setGarbageDefinitions(List<String> garbageDefinitions) {
        this.garbageDefinitions = garbageDefinitions;
    }

    public void addGarbageDefinitions(String garbageDefinition) {
        this.garbageDefinitions.add(garbageDefinition);
    }

    public void removeGarbageDefinitions(String garbageDefinition) {
        this.garbageDefinitions.remove(garbageDefinition);
    }

    /**
     * @return the batchArtifactLoaderType
     */
    public BatchArtifactLoader getBatchArtifactLoaderType() {
        if (batchArtifactLoaderType == null) {
            batchArtifactLoaderType = BatchArtifactLoader.THREAD_CONTEXT_CLASS_LOADER;
        }
        return batchArtifactLoaderType;
    }

    /**
     * @param batchArtifactLoaderType the batchArtifactLoaderType to set
     */
    public void setBatchArtifactLoaderType(BatchArtifactLoader batchArtifactLoaderType) {
        this.batchArtifactLoaderType = batchArtifactLoaderType;
    }

}
