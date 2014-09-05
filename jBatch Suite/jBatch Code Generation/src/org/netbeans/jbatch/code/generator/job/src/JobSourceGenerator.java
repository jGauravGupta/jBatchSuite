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
package org.netbeans.jbatch.code.generator.job.src;

import freemarker.template.Configuration;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.jbatch.code.generator.util.ConverterUtil;
import org.netbeans.jbatch.code.templates.ClassPathLoader;
import org.netbeans.jbatch.code.templates.wizard.ConstraintIterator;
import org.netbeans.jbatch.modeler.spec.Decision;
import org.netbeans.jbatch.modeler.spec.Flow;
import org.netbeans.jbatch.modeler.spec.Job;
import org.netbeans.jbatch.modeler.spec.Step;
import org.netbeans.jbatch.modeler.spec.core.BatchArtifactLoader;
import org.netbeans.jbatch.modeler.spec.core.Definitions;
import org.netbeans.jbatch.modeler.spec.core.FlowNode;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.task.ITaskSupervisor;
import org.openide.filesystems.FileUtil;

public class JobSourceGenerator {

    public void generateSource(ITaskSupervisor task, Project project, SourceGroup sourceGroup, ModelerFile modelerFile, String _package, Job job) {
        Definitions definitionsSpec = (Definitions) modelerFile.getDefinitionElement();
        File destDir = FileUtil.toFile(sourceGroup.getRootFolder());

        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(ClassPathLoader.class, "");
        for (FlowNode flowNode : job.getFlowNode()) {
            if (flowNode instanceof Step) {
                Step step = (Step) flowNode;

                if (step.getPartition() != null) {
                    if (step.getPartition().getAnalyzer().getAnalyzerRef() != null && !step.getPartition().getAnalyzer().getAnalyzerRef().trim().isEmpty()) {
                        Map<String, Object> property = new HashMap<String, Object>();
                        property.put(ConstraintIterator.WizardProperties.ANALYZER, true);
                        property.put(ConstraintIterator.WizardProperties.COLLECTOR, false);
                        property.put(ConstraintIterator.WizardProperties.MAPPER, false);
                        property.put(ConstraintIterator.WizardProperties.REDUCER, false);
                        if (definitionsSpec.getBatchArtifactLoaderType() == BatchArtifactLoader.IMPLEMENTATION_SPECIFIC_LOADER) {
                            ConverterUtil.generateFile("parallelization/partition/resources/Partition.java.template",
                                    destDir.getAbsolutePath(), _package, step.getPartition().getAnalyzer().getAnalyzerRef(),
                                    step.getPartition().getAnalyzer().getAnalyzerRef(), property);
                        } else {
                            ConverterUtil.generateFile("parallelization/partition/resources/Partition.java.template",
                                    destDir.getAbsolutePath(), _package, null,
                                    step.getPartition().getAnalyzer().getAnalyzerRef(), property);
                        }
                    }
                    if (step.getPartition().getCollector().getCollectorRef() != null && !step.getPartition().getCollector().getCollectorRef().trim().isEmpty()) {
                        Map<String, Object> property = new HashMap<String, Object>();
                        property.put(ConstraintIterator.WizardProperties.ANALYZER, false);
                        property.put(ConstraintIterator.WizardProperties.COLLECTOR, true);
                        property.put(ConstraintIterator.WizardProperties.MAPPER, false);
                        property.put(ConstraintIterator.WizardProperties.REDUCER, false);

                        if (definitionsSpec.getBatchArtifactLoaderType() == BatchArtifactLoader.IMPLEMENTATION_SPECIFIC_LOADER) {
                            ConverterUtil.generateFile("parallelization/partition/resources/Partition.java.template",
                                    destDir.getAbsolutePath(), _package, step.getPartition().getCollector().getCollectorRef(),
                                    step.getPartition().getCollector().getCollectorRef(), property);
                        } else {
                            ConverterUtil.generateFile("parallelization/partition/resources/Partition.java.template",
                                    destDir.getAbsolutePath(), _package, null,
                                    step.getPartition().getCollector().getCollectorRef(), property);
                        }

                    }
                    if (step.getPartition().getReducer().getReducerRef() != null && !step.getPartition().getReducer().getReducerRef().trim().isEmpty()) {
                        Map<String, Object> property = new HashMap<String, Object>();
                        property.put(ConstraintIterator.WizardProperties.ANALYZER, false);
                        property.put(ConstraintIterator.WizardProperties.COLLECTOR, false);
                        property.put(ConstraintIterator.WizardProperties.MAPPER, false);
                        property.put(ConstraintIterator.WizardProperties.REDUCER, true);

                        if (definitionsSpec.getBatchArtifactLoaderType() == BatchArtifactLoader.IMPLEMENTATION_SPECIFIC_LOADER) {
                            ConverterUtil.generateFile("parallelization/partition/resources/Partition.java.template",
                                    destDir.getAbsolutePath(), _package, step.getPartition().getReducer().getReducerRef(),
                                    step.getPartition().getReducer().getReducerRef(), property);
                        } else {
                            ConverterUtil.generateFile("parallelization/partition/resources/Partition.java.template",
                                    destDir.getAbsolutePath(), _package, null,
                                    step.getPartition().getReducer().getReducerRef(), property);
                        }
                    }

                    if (step.getPartition().getRuntimeMapping() == Boolean.TRUE && step.getPartition().getMapper().getMapperRef() != null && !step.getPartition().getMapper().getMapperRef().trim().isEmpty()) {
                        Map<String, Object> property = new HashMap<String, Object>();
                        property.put(ConstraintIterator.WizardProperties.ANALYZER, false);
                        property.put(ConstraintIterator.WizardProperties.COLLECTOR, false);
                        property.put(ConstraintIterator.WizardProperties.MAPPER, true);
                        property.put(ConstraintIterator.WizardProperties.REDUCER, false);

                        if (definitionsSpec.getBatchArtifactLoaderType() == BatchArtifactLoader.IMPLEMENTATION_SPECIFIC_LOADER) {
                            ConverterUtil.generateFile("parallelization/partition/resources/Partition.java.template",
                                    destDir.getAbsolutePath(), _package, step.getPartition().getMapper().getMapperRef(),
                                    step.getPartition().getMapper().getMapperRef(), property);
                        } else {
                            ConverterUtil.generateFile("parallelization/partition/resources/Partition.java.template",
                                    destDir.getAbsolutePath(), _package, null,
                                    step.getPartition().getMapper().getMapperRef(), property);
                        }

                    }
                }

                if (step.getBatchlet() != null) {
                    if (step.getBatchlet().getRef() == null || step.getBatchlet().getRef().trim().isEmpty()) {
                        step.getBatchlet().setRef("Batchlet" + step.getId());
                    }

                    if (definitionsSpec.getBatchArtifactLoaderType() == BatchArtifactLoader.IMPLEMENTATION_SPECIFIC_LOADER) {
                        ConverterUtil.generateFile("step/batchlet/resources/Batchlet.java.template",
                                destDir.getAbsolutePath(), _package, step.getBatchlet().getRef(),
                                step.getBatchlet().getRef());
                    } else {
                        ConverterUtil.generateFile("step/batchlet/resources/Batchlet.java.template",
                                destDir.getAbsolutePath(), _package, null,
                                step.getBatchlet().getRef());
                    }
                } else {
                    if (step.getChunk().getReader().getRef() == null || step.getChunk().getReader().getRef().trim().isEmpty()) {
                        step.getChunk().getReader().setRef("Reader" + step.getId());
                    }

                    if (definitionsSpec.getBatchArtifactLoaderType() == BatchArtifactLoader.IMPLEMENTATION_SPECIFIC_LOADER) {
                        ConverterUtil.generateFile("step/chunk/reader/resources/ItemReader.java.template",
                                destDir.getAbsolutePath(), _package, step.getChunk().getReader().getRef(),
                                step.getChunk().getReader().getRef());
                    } else {
                        ConverterUtil.generateFile("step/chunk/reader/resources/ItemReader.java.template",
                                destDir.getAbsolutePath(), _package, null,
                                step.getChunk().getReader().getRef());
                    }
                    if (step.getChunk().getProcessor().getRef() == null || step.getChunk().getProcessor().getRef().trim().isEmpty()) {
                        step.getChunk().getProcessor().setRef("Processor" + step.getId());
                    }

                    if (definitionsSpec.getBatchArtifactLoaderType() == BatchArtifactLoader.IMPLEMENTATION_SPECIFIC_LOADER) {
                        ConverterUtil.generateFile("step/chunk/processor/resources/ItemProcessor.java.template",
                                destDir.getAbsolutePath(), _package, step.getChunk().getProcessor().getRef(),
                                step.getChunk().getProcessor().getRef());
                    } else {
                        ConverterUtil.generateFile("step/chunk/processor/resources/ItemProcessor.java.template",
                                destDir.getAbsolutePath(), _package, null,
                                step.getChunk().getProcessor().getRef());
                    }
                    if (step.getChunk().getWriter().getRef() == null || step.getChunk().getWriter().getRef().trim().isEmpty()) {
                        step.getChunk().getWriter().setRef("Writer" + step.getId());
                    }

                    if (definitionsSpec.getBatchArtifactLoaderType() == BatchArtifactLoader.IMPLEMENTATION_SPECIFIC_LOADER) {
                        ConverterUtil.generateFile("step/chunk/writer/resources/ItemWriter.java.template",
                                destDir.getAbsolutePath(), _package, step.getChunk().getWriter().getRef(),
                                step.getChunk().getWriter().getRef());
                    } else {
                        ConverterUtil.generateFile("step/chunk/writer/resources/ItemWriter.java.template",
                                destDir.getAbsolutePath(), _package, null,
                                step.getChunk().getWriter().getRef());
                    }
                }
            } else if (flowNode instanceof Decision) {
                Decision decision = (Decision) flowNode;
                if (decision.getRef() == null || decision.getRef().trim().isEmpty()) {
                    decision.setRef("Decision" + decision.getId());
                }

                if (definitionsSpec.getBatchArtifactLoaderType() == BatchArtifactLoader.IMPLEMENTATION_SPECIFIC_LOADER) {
                    ConverterUtil.generateFile("decision/resources/Decision.java.template",
                            destDir.getAbsolutePath(), _package, decision.getRef(),
                            decision.getRef());
                } else {
                    ConverterUtil.generateFile("decision/resources/Decision.java.template",
                            destDir.getAbsolutePath(), _package, null,
                            decision.getRef());
                }
            } else if (flowNode instanceof Flow) {
                Flow flow = (Flow) flowNode;
                Definitions definition = Definitions.load(modelerFile, flow.getId());
                if (definition != null) {
                    generateSource(task, project, sourceGroup, modelerFile, _package, definition.getJob());
                }
            }
        }
        Definitions definition = (Definitions) modelerFile.getDefinitionElement();

        if (project.getClass().getName().equals("org.netbeans.modules.web.project.WebProject")) {
            ConverterUtil.generateFile("tester/resources/web/BatchTester.java.template",
                    destDir.getAbsolutePath(), _package, definition.getName(), "BatchTester");
        } else {
            ConverterUtil.generateFile("tester/resources/BatchTester.java.template",
                    destDir.getAbsolutePath(), _package, definition.getName(), "BatchTester");
        }

    }

}
