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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FileUtils;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.jbatch.modeler.archive.loader.spec.BatchArtifacts;
import org.netbeans.jbatch.modeler.archive.loader.spec.Ref;
import org.netbeans.jbatch.modeler.spec.Decision;
import org.netbeans.jbatch.modeler.spec.Flow;
import org.netbeans.jbatch.modeler.spec.Job;
import org.netbeans.jbatch.modeler.spec.Next;
import org.netbeans.jbatch.modeler.spec.Properties;
import org.netbeans.jbatch.modeler.spec.Split;
import org.netbeans.jbatch.modeler.spec.Step;
import org.netbeans.jbatch.modeler.spec.core.BatchArtifactLoader;
import org.netbeans.jbatch.modeler.spec.core.Converge;
import org.netbeans.jbatch.modeler.spec.core.Definitions;
import org.netbeans.jbatch.modeler.spec.core.Diverge;
import org.netbeans.jbatch.modeler.spec.core.EndEvent;
import org.netbeans.jbatch.modeler.spec.core.Event;
import org.netbeans.jbatch.modeler.spec.core.FlowNode;
import org.netbeans.jbatch.modeler.spec.core.Gateway;
import org.netbeans.jbatch.modeler.spec.core.KeyManager;
import org.netbeans.jbatch.modeler.spec.core.SequenceFlow;
import org.netbeans.jbatch.modeler.spec.core.StartEvent;
import org.netbeans.jbatch.modeler.spec.core.TransitionManager;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.shape.ShapeDesign;
import org.netbeans.modeler.task.ITaskSupervisor;
import org.netbeans.modeler.validation.jaxb.ValidateJAXB;
import org.openide.util.Exceptions;

public class JobXMLGenerator {

    private Project project;
    private static JAXBContext jobContext;
    private static Marshaller jobMarshaller;
    private static Unmarshaller jobUnmarshaller;

    public void generateJobXML(ITaskSupervisor task, Project project, SourceGroup sourceGroup, ModelerFile modelerFile, String fileName, Job job) {
        this.project = project;
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

            job = rectify(modelerFile, job);
            File directory;

            if (project.getClass().getName().equals("org.netbeans.modules.maven.NbMavenProjectImpl")) {
                directory = new File(project.getProjectDirectory().getPath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "META-INF" + File.separator + "batch-jobs");
            } else if (project.getClass().getName().equals("org.netbeans.modules.java.j2seproject.J2SEProject")) {
                directory = new File(project.getProjectDirectory().getPath() + File.separator + "src" + File.separator + "META-INF" + File.separator + "batch-jobs");
            } else {
                directory = new File(project.getProjectDirectory().getPath() + File.separator + "src" + File.separator + "java" + File.separator + "META-INF" + File.separator + "batch-jobs");
            }
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File savedFile = new File(directory, fileName + ".xml");

            if (!savedFile.exists()) {
                savedFile.createNewFile();
            }
//            System.out.println("savedFile : " + savedFile.getAbsolutePath());

            // output pretty printed
            jobMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            jobMarshaller.marshal(job, System.out);
            StringWriter sw = new StringWriter();
            jobMarshaller.marshal(job, sw);
            String jobText = sw.toString();

            jobText = jobText.replaceFirst("xmlns:nbm=\"http://nbmodeler.java.net\"", "")
                    .replaceFirst("xmlns:jbatchnb=\"http://jbatchsuite.java.net\"", "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee http://www.oracle.com/webfolder/technetwork/jsc/xml/ns/javaee/jobXML_1_0.xsd\"")
                    //                    .replaceFirst("xmlns:java=\"http://jcp.org/en/jsr/detail?id=270\"", "")
                    .replaceFirst(":jbatch", "").replaceAll("jbatch:", "");
            FileUtils.writeStringToFile(savedFile, jobText);

        } catch (JAXBException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    private Job rectify(ModelerFile modelerFile, Job oldJob) {
        Definitions definitionsSpec = (Definitions) modelerFile.getDefinitionElement();
        Job newJob = cloneJob(oldJob);
        for (FlowNode flowNode : newJob.getFlowNode()) {
            if (flowNode instanceof Step) {
                Step step = (Step) flowNode;
                garbageCollectStep(step);
            } else if (flowNode instanceof Flow) {
                Flow flow = (Flow) flowNode;
                Definitions definition = Definitions.load(modelerFile, flow.getId());
                if (definition != null) {
                    Job job = rectify(modelerFile, definition.getJob());
                    flow.setFlowNode(job.getFlowNode());
                }
            }
        }
        manageSplit(newJob);
        manageEvent(newJob);
        garbageCollectFlowNode(newJob);
        garbageCollectJob(newJob);

        if (definitionsSpec.getBatchArtifactLoaderType() == BatchArtifactLoader.THREAD_CONTEXT_CLASS_LOADER) {
            if (definitionsSpec.getPackage() != null && !definitionsSpec.getPackage().trim().isEmpty()) {
                for (FlowNode flowNode : newJob.getFlowNode()) {
                    if (flowNode instanceof Step) {
                        Step step = (Step) flowNode;
                        if (step.getBatchlet() != null) {
                            step.getBatchlet().setRef(definitionsSpec.getPackage() + "." + step.getBatchlet().getRef());
                        } else if (step.getChunk() != null) {
                            step.getChunk().getReader().setRef(definitionsSpec.getPackage() + "." + step.getChunk().getReader().getRef());
                            step.getChunk().getProcessor().setRef(definitionsSpec.getPackage() + "." + step.getChunk().getProcessor().getRef());
                            step.getChunk().getWriter().setRef(definitionsSpec.getPackage() + "." + step.getChunk().getWriter().getRef());
                        }
                    } else if (flowNode instanceof Decision) {
                        Decision decision = (Decision) flowNode;
                        decision.setRef(definitionsSpec.getPackage() + "." + decision.getRef());
                    }
                }
            }
        } else if (definitionsSpec.getBatchArtifactLoaderType() == BatchArtifactLoader.ARCHIVE_LOADER) {
            createArchiveLoader(definitionsSpec, newJob);
        }

        return newJob;
    }

    private void createArchiveLoader(Definitions definitionsSpec, Job job) {
        try {
            BatchArtifacts batchArtifact = new BatchArtifacts();
            for (FlowNode flowNode : job.getFlowNode()) {
                if (definitionsSpec.getPackage() != null && !definitionsSpec.getPackage().trim().isEmpty()) {
                    if (flowNode instanceof Step) {
                        Step step = (Step) flowNode;
                        if (step.getBatchlet() != null) {
                            batchArtifact.addRef(new Ref(step.getBatchlet().getRef(), definitionsSpec.getPackage() + "." + step.getBatchlet().getRef()));
                        } else if (step.getChunk() != null) {
                            batchArtifact.addRef(new Ref(step.getChunk().getReader().getRef(), definitionsSpec.getPackage() + "." + step.getChunk().getReader().getRef()));
                            batchArtifact.addRef(new Ref(step.getChunk().getProcessor().getRef(), definitionsSpec.getPackage() + "." + step.getChunk().getProcessor().getRef()));
                            batchArtifact.addRef(new Ref(step.getChunk().getWriter().getRef(), definitionsSpec.getPackage() + "." + step.getChunk().getWriter().getRef()));
                        }
                    } else if (flowNode instanceof Decision) {
                        Decision decision = (Decision) flowNode;
                        batchArtifact.addRef(new Ref(decision.getRef(), definitionsSpec.getPackage() + "." + decision.getRef()));
                    }
                } else {
                    if (flowNode instanceof Step) {
                        Step step = (Step) flowNode;
                        if (step.getBatchlet() != null) {
                            batchArtifact.addRef(new Ref(step.getBatchlet().getRef(), step.getBatchlet().getRef()));
                        } else if (step.getChunk() != null) {
                            batchArtifact.addRef(new Ref(step.getChunk().getReader().getRef(), step.getChunk().getReader().getRef()));
                            batchArtifact.addRef(new Ref(step.getChunk().getProcessor().getRef(), step.getChunk().getProcessor().getRef()));
                            batchArtifact.addRef(new Ref(step.getChunk().getWriter().getRef(), step.getChunk().getWriter().getRef()));
                        }
                    } else if (flowNode instanceof Decision) {
                        Decision decision = (Decision) flowNode;
                        batchArtifact.addRef(new Ref(decision.getRef(), decision.getRef()));
                    }
                }
            }
            File directory;
            if (project.getClass().getName().equals("org.netbeans.modules.maven.NbMavenProjectImpl")) {
                directory = new File(project.getProjectDirectory().getPath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "META-INF");
            } else {
                directory = new File(project.getProjectDirectory().getPath() + File.separator + "src" + File.separator + "java" + File.separator + "META-INF");
            }
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File savedFile = new File(directory, "batch.xml");

            if (!savedFile.exists()) {
                savedFile.createNewFile();
            }

            JAXBContext archiveContext = JAXBContext.newInstance(new Class<?>[]{BatchArtifacts.class});
            Marshaller archiveMarshaller = archiveContext.createMarshaller();
//            Unmarshaller jobUnmarshaller = jobContext.createUnmarshaller();
            archiveMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            archiveMarshaller.marshal(batchArtifact, savedFile);
        } catch (JAXBException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    private void setNextElement(SequenceFlow sequenceFlow, FlowNode sourceFlowNode, FlowNode targetFlowNode) {
        if (sourceFlowNode instanceof Step) {
            Step sourceStep = (Step) sourceFlowNode;
            if (targetFlowNode instanceof KeyManager) {
                sourceStep.setNext(((KeyManager) targetFlowNode).getKey());
            } else {
                sourceStep.setNext(targetFlowNode.getId());
            }
        } else if (sourceFlowNode instanceof Flow) {
            Flow sourceFlow = (Flow) sourceFlowNode;
            if (targetFlowNode instanceof KeyManager) {
                sourceFlow.setNext(((KeyManager) targetFlowNode).getKey());
            } else {
                sourceFlow.setNext(targetFlowNode.getId());
            }
        } else if (sourceFlowNode instanceof Decision) {
            Decision sourceDecision = (Decision) sourceFlowNode;
            Next next = new Next();
            if (targetFlowNode instanceof KeyManager) {
                next.setTo(((KeyManager) targetFlowNode).getKey());
            } else {
                next.setTo(targetFlowNode.getId());
            }
            next.setOn(sequenceFlow.getOn());
            sourceDecision.addTransitionElement(next);
        } else if (sourceFlowNode instanceof Split) {
            Split sourceSplit = (Split) sourceFlowNode;
            if (targetFlowNode instanceof KeyManager) {
                sourceSplit.setNext(((KeyManager) targetFlowNode).getKey());
            } else {
                sourceSplit.setNext(targetFlowNode.getId());
            }
        }
    }

    private void garbageCollectJob(Job job) {
        if (job.getProperties().getProperty().isEmpty()) {
            job.setProperties(null);
        }
        if (job.getListeners().getListener().isEmpty()) {
            job.setListeners(null);
        }
        job.setSequenceFlow(null);
        job.setId(job.getKey());
        job.setKey(null);
    }

    private void manageSplit(Job job) {
        for (SequenceFlow sequenceFlow : job.getSequenceFlow()) {
            FlowNode sourceFlowNode = job.findFlowNode(sequenceFlow.getSourceRef());
            FlowNode targetFlowNode = job.findFlowNode(sequenceFlow.getTargetRef());
            if (!(sourceFlowNode instanceof Diverge)
                    && !(targetFlowNode instanceof Diverge)
                    && !(sourceFlowNode instanceof Converge)
                    && !(targetFlowNode instanceof Converge)
                    && !(sourceFlowNode instanceof Event)
                    && !(targetFlowNode instanceof Event)) {
                setNextElement(sequenceFlow, sourceFlowNode, targetFlowNode);
            } else if (targetFlowNode instanceof Diverge) {
                Diverge diverge = (Diverge) targetFlowNode;
                Split split = (Split) job.findFlowNode(diverge.getKey());
                if (split == null) {
                    split = new Split();
                    split.setId(diverge.getKey());
                    job.getFlowNode().add(split);
                }
                setNextElement(sequenceFlow, sourceFlowNode, split);
            } else if (sourceFlowNode instanceof Converge) {
                Converge converge = (Converge) sourceFlowNode;
                Diverge diverge = (Diverge) job.findFlowNode(converge.getDivergeRef());
                Split split = (Split) job.findFlowNode(diverge.getKey());
                if (split == null) {
                    split = new Split();
                    split.setId(diverge.getKey());
                    job.getFlowNode().add(split);
                }
                setNextElement(sequenceFlow, split, targetFlowNode);
            } else if (sourceFlowNode instanceof Diverge) {
                Diverge diverge = (Diverge) sourceFlowNode;
                Flow flow = (Flow) targetFlowNode;
                Split split = (Split) job.findFlowNode(diverge.getKey());
                if (split == null) {
                    split = new Split();
                    split.setId(diverge.getKey());
                    job.getFlowNode().add(split);
                }
                split.addFlow(flow);
                job.removeFlow(flow);
            } else if (targetFlowNode instanceof Converge) {
                //    skip
            }
        }
    }

    private void manageEvent(Job job) {
        List<FlowNode> startFlowNodes = new ArrayList<FlowNode>();
        Iterator<FlowNode> flowNodeItr = job.getFlowNode().iterator();
        while (flowNodeItr.hasNext()) {
            FlowNode flowNode = flowNodeItr.next();
            if (flowNode instanceof Event) {
                Event event = (Event) flowNode;
                if (event instanceof EndEvent) {
                    EndEvent endEvent = (EndEvent) event;
                    Map<FlowNode, SequenceFlow> sourceFlowNodes = job.getSources(event.getId());
                    for (Entry<FlowNode, SequenceFlow> entry : sourceFlowNodes.entrySet()) {
                        FlowNode sourceFlowNode = entry.getKey();
                        SequenceFlow sequenceFlow = entry.getValue();
                        if (sourceFlowNode instanceof TransitionManager) {
                            TransitionManager transitionManager = (TransitionManager) sourceFlowNode;
                            endEvent = endEvent.getJSLInstance();
                            endEvent.setOn(sequenceFlow.getOn());
                            transitionManager.addTransitionElement(endEvent);
//                                 if (event instanceof Stop) {
//                                    Stop stop = (Stop) event;
//                                }
                        }
                    }
                } else if (event instanceof StartEvent) {
                    StartEvent startEvent = (StartEvent) event;
                    Map<FlowNode, SequenceFlow> targetFlowNodes = job.getTargets(event.getId());
                    for (Entry<FlowNode, SequenceFlow> entry : targetFlowNodes.entrySet()) {
                        FlowNode sourceFlowNode = entry.getKey();
                        startFlowNodes.add(sourceFlowNode);
                    }
                }
                flowNodeItr.remove();
            }
        }

        for (FlowNode startFlowNode : startFlowNodes) {
            job.getFlowNode().remove(startFlowNode);
            job.getFlowNode().add(0, startFlowNode);
        }
    }

    private Job cloneJob(Job job) {
        Job newJob = null;
        try {
            jobMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            jobMarshaller.marshal(job, sw);
            newJob = (Job) jobUnmarshaller.unmarshal(new StreamSource(new StringReader(sw.toString())));
        } catch (PropertyException ex) {
            Exceptions.printStackTrace(ex);
        } catch (JAXBException ex) {
            Exceptions.printStackTrace(ex);
        }
        return newJob;
    }

    private void garbageCollectStep(Step step) {
        if (step.getProperties().getProperty().isEmpty()) {
            step.setProperties(null);
        }
        if (step.getListeners().getListener().isEmpty()) {
            step.setListeners(null);
        }
        if (step.getPartitionAllowed() && step.getPartition() != null) {
            if (step.getPartition().getAnalyzer().getAnalyzerRef() == null || step.getPartition().getAnalyzer().getAnalyzerRef().trim().isEmpty()) {
                step.getPartition().setAnalyzer(null);
            } else if (step.getPartition().getAnalyzer().getProperties().getProperty().isEmpty()) {
                step.getPartition().getAnalyzer().setProperties(null);
            }
            if (step.getPartition().getCollector().getCollectorRef() == null || step.getPartition().getCollector().getCollectorRef().trim().isEmpty()) {
                step.getPartition().setCollector(null);
            } else if (step.getPartition().getCollector().getProperties().getProperty().isEmpty()) {
                step.getPartition().getCollector().setProperties(null);
            }
            if (step.getPartition().getMapper().getMapperRef() == null || step.getPartition().getMapper().getMapperRef().trim().isEmpty()) {
                step.getPartition().setMapper(null);
            } else if (step.getPartition().getMapper().getProperties().getProperty().isEmpty()) {
                step.getPartition().getMapper().setProperties(null);
            }
            if (step.getPartition().getReducer().getReducerRef() == null || step.getPartition().getReducer().getReducerRef().trim().isEmpty()) {
                step.getPartition().setReducer(null);
            } else if (step.getPartition().getReducer().getProperties().getProperty().isEmpty()) {
                step.getPartition().getReducer().setProperties(null);
            }
            if (step.getPartition().getRuntimeMapping() == Boolean.TRUE) {
                step.getPartition().setPlan(null);
            } else {
                step.getPartition().setMapper(null);
                if (step.getPartition().getPlan().getProperties() != null && !step.getPartition().getPlan().getProperties().isEmpty()) {
                    step.getPartition().getPlan().setPartitions(String.valueOf(step.getPartition().getPlan().getProperties().size()));
                    int index = 0;
                    for (Properties properties : step.getPartition().getPlan().getProperties()) {
                        properties.setPartition(String.valueOf(index++));
                    }
                } else {
                    step.getPartition().setPlan(null);
                }
            }
            step.getPartition().setRuntimeMapping(null);

            if (step.getPartition().getAnalyzer() == null && step.getPartition().getCollector() == null
                    && step.getPartition().getReducer() == null && step.getPartition().getPlan() == null && step.getPartition().getMapper() == null) {
                step.setPartition(null);
            }
        } else {
            step.setPartition(null);
        }
        step.setPartitionAllowed(null);

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

    private void garbageCollectFlowNode(Job job) {
        Iterator<FlowNode> flowNodeItr = job.getFlowNode().iterator();
        while (flowNodeItr.hasNext()) {
            FlowNode flowNode = flowNodeItr.next();
            if (flowNode instanceof Step) {
                Step step = (Step) flowNode;
                step.setId(step.getKey());
                step.setName(null);
                step.setKey(null);
            } else if (flowNode instanceof Flow) {
                Flow flow = (Flow) flowNode;
                flow.setId(flow.getKey());
                flow.setName(null);
                flow.setKey(null);
            } else if (flowNode instanceof Gateway) {
                if (flowNode instanceof Decision) {
                    Decision decision = (Decision) flowNode;
                    decision.setId(decision.getKey());
                    decision.setName(null);
                    decision.setKey(null);
                } else if (flowNode instanceof Diverge || flowNode instanceof Converge) {
                    flowNodeItr.remove();
                } else if (flowNode instanceof Split) {
                    Split split = (Split) flowNode;
                    for (Flow flow : split.getFlow()) {
                        flow.setId(flow.getKey());
                        flow.setName(null);
                        flow.setKey(null);
                        flow.setExtensionElements(null);
                        flow.setIncoming(null);
                        flow.setOutgoing(null);
                    }
                }
            }
            flowNode.setExtensionElements(null);
            flowNode.setIncoming(null);
            flowNode.setOutgoing(null);
        }
    }
}
