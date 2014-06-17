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
package org.netbeans.jbatch.code.generator.job.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FileUtils;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.jbatch.modeler.spec.Decision;
import org.netbeans.jbatch.modeler.spec.Job;
import org.netbeans.jbatch.modeler.spec.Next;
import org.netbeans.jbatch.modeler.spec.Step;
import org.netbeans.jbatch.modeler.spec.core.FlowNode;
import org.netbeans.jbatch.modeler.spec.core.KeyManager;
import org.netbeans.jbatch.modeler.spec.core.SequenceFlow;
import org.netbeans.modeler.shape.ShapeDesign;
import org.netbeans.modeler.task.ITaskSupervisor;
import org.netbeans.modeler.validation.jaxb.ValidateJAXB;
import org.openide.util.Exceptions;

public class JobXMLGenerator {

    private static JAXBContext jobContext;
    private static Marshaller jobMarshaller;
    private static Unmarshaller jobUnmarshaller;

    public void generateJobXML(ITaskSupervisor task, Project project, SourceGroup sourceGroup, String fileName, Job job) {
        try {
            if (jobContext == null) {
                jobContext = JAXBContext.newInstance(new Class<?>[]{ShapeDesign.class, Job.class});
            }
            if (jobMarshaller == null) {
                jobMarshaller = jobContext.createMarshaller();
                jobMarshaller.setEventHandler(new ValidateJAXB());
            }
            if (jobUnmarshaller == null) {
                jobUnmarshaller = jobContext.createUnmarshaller();
                jobUnmarshaller.setEventHandler(new ValidateJAXB());
            }

            job = rectify(job);
            File savedFile;
            if (project.getClass().getName().equals("org.netbeans.modules.maven.NbMavenProjectImpl")) {
                savedFile = new File(project.getProjectDirectory().getPath() + "/src/main/resources/META-INF/batch-jobs/" + fileName + ".xml");// file.getFile();
            } else {
                savedFile = new File(project.getProjectDirectory().getPath() + "/src/java/META-INF/batch-jobs/" + fileName + ".xml");// file.getFile();
            }

            if (savedFile.exists()) {
                savedFile.createNewFile();
            }
            System.out.println("savedFile : " + savedFile.getAbsolutePath());

            // output pretty printed
            jobMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jobMarshaller.marshal(job, System.out);
            StringWriter sw = new StringWriter();
            jobMarshaller.marshal(job, sw);
            String jobText = sw.toString();

            jobText = jobText.replaceFirst("xmlns:nbm=\"http://nbmodeler.java.net\"", "")
                    .replaceFirst("xmlns:jbatchnb=\"http://jbatchsuite.java.net\"", "")
                    .replaceFirst("xmlns:java=\"http://jcp.org/en/jsr/detail?id=270\"", "")
                    .replaceFirst("xmlns:java=\"http://jcp.org/en/jsr/detail?id=270\"", "");

            FileUtils.writeStringToFile(savedFile, jobText);

        } catch (JAXBException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    private Job rectify(Job oldJob) {
        Job newJob = null;
        try {
            jobMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter sw = new StringWriter();
            jobMarshaller.marshal(oldJob, sw);
            newJob = (Job) jobUnmarshaller.unmarshal(new StreamSource(new StringReader(sw.toString())));

            if (newJob.getProperties().getProperty().isEmpty()) {
                newJob.setProperties(null);
            }
            if (newJob.getListeners().getListener().isEmpty()) {
                newJob.setListeners(null);
            }
            for (FlowNode flowNode : newJob.getDecisionOrFlowOrSplit()) {
                if (flowNode instanceof Step) {
                    Step step = (Step) flowNode;
                    if (step.getProperties().getProperty().isEmpty()) {
                        step.setProperties(null);
                    }
                    if (step.getListeners().getListener().isEmpty()) {
                        step.setListeners(null);
                    }
                    if (step.getBatchlet() != null) {
                        if (step.getBatchlet().getProperties().getProperty().isEmpty()) {
                            step.getBatchlet().setProperties(null);
                        }
                    } else if (step.getChunk() != null) {
                        if (step.getChunk().getReader().getProperties().getProperty().isEmpty()) {
                            step.getChunk().getReader().setProperties(null);
                        }
                        if (step.getChunk().getProcessor().getProperties().getProperty().isEmpty()) {
                            step.getChunk().getProcessor().setProperties(null);
                        }
                        if (step.getChunk().getWriter().getProperties().getProperty().isEmpty()) {
                            step.getChunk().getWriter().setProperties(null);
                        }
                    }
                }
            }

            for (SequenceFlow sequenceFlow : newJob.getSequenceFlow()) {
                FlowNode sourceFlowNode = newJob.findFlowNode(sequenceFlow.getSourceRef());
                FlowNode targetFlowNode = newJob.findFlowNode(sequenceFlow.getTargetRef());
                if (sourceFlowNode instanceof Step) {
                    Step sourceStep = (Step) sourceFlowNode;
                    if (targetFlowNode instanceof KeyManager) {
                        sourceStep.setNext(((KeyManager) targetFlowNode).getKey());
                    } else {
                        sourceStep.setNext(targetFlowNode.getId());
                    }
                } else if (sourceFlowNode instanceof Decision) {
                    Decision sourceDecision = (Decision) sourceFlowNode;
                    Next next = new Next();
                    if (targetFlowNode instanceof KeyManager) {
                        next.setTo(((KeyManager) targetFlowNode).getKey());
                    } else {
                        next.setTo(targetFlowNode.getId());
                    }
                    next.setOn(sequenceFlow.getInputStatus());
                    sourceDecision.addTransitionElement(next);
                }
            }

            for (FlowNode flowNode : newJob.getDecisionOrFlowOrSplit()) {
                if (flowNode instanceof Step) {
                    Step step = (Step) flowNode;
                    step.setId(step.getKey());
                    step.setKey(null);
                } else if (flowNode instanceof Decision) {
                    Decision decision = (Decision) flowNode;
                    decision.setId(decision.getKey());
                    decision.setKey(null);
                }
                flowNode.setExtensionElements(null);
                flowNode.setIncoming(null);
                flowNode.setOutgoing(null);
            }

            newJob.setSequenceFlow(null);
            newJob.setId(newJob.getKey());
            newJob.setKey(null);

        } catch (PropertyException ex) {
            Exceptions.printStackTrace(ex);
        } catch (JAXBException ex) {
            Exceptions.printStackTrace(ex);
        }
        return newJob;
    }

}
