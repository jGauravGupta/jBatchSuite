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
import org.apache.commons.lang.WordUtils;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.jbatch.code.generator.util.ConverterUtil;
import org.netbeans.jbatch.code.templates.ClassPathLoader;
import org.netbeans.jbatch.modeler.spec.Decision;
import org.netbeans.jbatch.modeler.spec.Job;
import org.netbeans.jbatch.modeler.spec.Step;
import org.netbeans.jbatch.modeler.spec.core.FlowNode;
import org.netbeans.modeler.task.ITaskSupervisor;
import org.openide.filesystems.FileUtil;

public class JobSourceGenerator {

//    private File destDir;
//    private ITaskSupervisor task;
//    private Project project;
//    private SourceGroup sourceGroup;
//    private String packageName = null;
    public void generateSource(ITaskSupervisor task, Project project, SourceGroup sourceGroup, String _package, Job job) {
//        this.task = task;
//        this.project = project;
//        this.sourceGroup = sourceGroup;
        File destDir = FileUtil.toFile(sourceGroup.getRootFolder());
//        this.packageName = _package;

        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(ClassPathLoader.class, "");
        for (FlowNode flowNode : job.getDecisionOrFlowOrSplit()) {
            if (flowNode instanceof Step) {
                Step step = (Step) flowNode;
                if (step.getBatchlet() != null) {
                    if (step.getBatchlet().getRef() == null || step.getBatchlet().getRef().trim().isEmpty()) {
                        step.getBatchlet().setRef("Batchlet" + step.getId());
                    }
                    ConverterUtil.generateFile("step/batchlet/resources/Batchlet.java.template",
                            destDir.getAbsolutePath(), _package, step.getBatchlet().getRef(),
                            WordUtils.capitalizeFully(step.getBatchlet().getRef()));
                } else {
                    if (step.getChunk().getReader().getRef() == null || step.getChunk().getReader().getRef().trim().isEmpty()) {
                        step.getChunk().getReader().setRef("Reader" + step.getId());
                    }
                    ConverterUtil.generateFile("step/chunk/reader/resources/ItemReader.java.template",
                            destDir.getAbsolutePath(), _package, step.getChunk().getReader().getRef(),
                            WordUtils.capitalizeFully(step.getChunk().getReader().getRef()));
                    if (step.getChunk().getProcessor().getRef() == null || step.getChunk().getProcessor().getRef().trim().isEmpty()) {
                        step.getChunk().getProcessor().setRef("Processor" + step.getId());
                    }
                    ConverterUtil.generateFile("step/chunk/processor/resources/ItemProcessor.java.template",
                            destDir.getAbsolutePath(), _package, step.getChunk().getProcessor().getRef(),
                            WordUtils.capitalizeFully(step.getChunk().getProcessor().getRef()));
                    if (step.getChunk().getWriter().getRef() == null || step.getChunk().getWriter().getRef().trim().isEmpty()) {
                        step.getChunk().getWriter().setRef("Writer" + step.getId());
                    }
                    ConverterUtil.generateFile("step/chunk/writer/resources/ItemWriter.java.template",
                            destDir.getAbsolutePath(), _package, step.getChunk().getWriter().getRef(),
                            WordUtils.capitalizeFully(step.getChunk().getWriter().getRef()));
                }
            } else if (flowNode instanceof Decision) {
                Decision decision = (Decision) flowNode;
                if (decision.getRef() == null || decision.getRef().trim().isEmpty()) {
                    decision.setRef("Decision" + decision.getId());
                }
                ConverterUtil.generateFile("decision/resources/Decision.java.template",
                        destDir.getAbsolutePath(), _package, decision.getRef(),
                        WordUtils.capitalizeFully(decision.getRef()));
            }
        }

    }

}
