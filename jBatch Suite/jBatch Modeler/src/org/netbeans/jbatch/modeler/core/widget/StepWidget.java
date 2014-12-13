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
package org.netbeans.jbatch.modeler.core.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.jbatch.modeler.spec.Analyzer;
import org.netbeans.jbatch.modeler.spec.Collector;
import org.netbeans.jbatch.modeler.spec.Partition;
import org.netbeans.jbatch.modeler.spec.PartitionMapper;
import org.netbeans.jbatch.modeler.widget.properties.user_interface.partition.PartitionPropertiesPanel;
import org.netbeans.jbatch.modeler.spec.PartitionPlan;
import org.netbeans.jbatch.modeler.spec.PartitionReducer;
import org.netbeans.jbatch.modeler.spec.Properties;
import org.netbeans.jbatch.modeler.spec.Property;
import org.netbeans.jbatch.modeler.spec.Step;
import org.netbeans.jbatch.modeler.specification.model.job.util.JobUtil;
import org.netbeans.modeler.config.element.ElementConfigFactory;
import org.netbeans.modeler.properties.nentity.Column;
import org.netbeans.modeler.properties.nentity.NAttributeEntity;
import org.netbeans.modeler.properties.nentity.NEntityDataListener;
import org.netbeans.modeler.properties.nentity.NEntityPropertySupport;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;
import org.netbeans.modeler.widget.properties.generic.ElementCustomPropertySupport;
import org.netbeans.modeler.widget.properties.handler.PropertyChangeListener;
import org.netbeans.modeler.widget.properties.handler.PropertyVisibilityHandler;
import org.openide.nodes.PropertySupport;
import org.openide.util.Exceptions;

public class StepWidget extends ActivityWidget {
    
    
    private PropertyVisibilityHandler planVisibilityHandler = new PropertyVisibilityHandler<String>() {
            @Override
            public boolean isVisible() {
                Step stepSpec = (Step) StepWidget.this.getBaseElementSpec();
                if(!stepSpec.getPartitionAllowed()){
                    return false;
                }
                if(stepSpec.getPartition()!=null && stepSpec.getPartition().getRuntimeMapping()){
                    return false;
                } 
                return true;
            }
        };
       private PropertyVisibilityHandler mapperVisibilityHandler = new PropertyVisibilityHandler<String>() {
            @Override
            public boolean isVisible() {
                Step stepSpec = (Step) StepWidget.this.getBaseElementSpec();
                if(!stepSpec.getPartitionAllowed()){
                    return false;
                }
                if(stepSpec.getPartition()!=null && stepSpec.getPartition().getRuntimeMapping()){
                    return true;
                } 
                return false;
            }
        };
       private final PropertyVisibilityHandler partitionVisibilityHandler = new PropertyVisibilityHandler<String>() {
            @Override
            public boolean isVisible() {
                Step stepSpec = (Step) StepWidget.this.getBaseElementSpec();
                if(!stepSpec.getPartitionAllowed()){
                    return false;
                }
                return true;
            }
        };

    private final  PropertyChangeListener partitionChangeListener = new PropertyChangeListener<Boolean>() {
            @Override
            public void changePerformed(Boolean value) {
              StepWidget.this.refreshProperties();
            }
        };
   
    public StepWidget(IModelerScene scene, NodeWidgetInfo node) {
        super(scene, node);
    }

    @Override
    public void createPropertySet(ElementPropertySet set) {
        set.createPropertySet(this, this.getBaseElementSpec(), getPropertyChangeListeners());
        Step stepSpec = (Step) this.getBaseElementSpec();
        set.put("BASIC_PROP", new NEntityPropertySupport(this.getModelerScene().getModelerFile(),JobUtil.addProperty(stepSpec.getProperties())));
        set.put("BASIC_PROP", JobUtil.addListener(this.getModelerScene().getModelerFile(), stepSpec.getListeners()));
        try {
         if(stepSpec.getPartition()==null){
            stepSpec.setPartition(new Partition());
        } 
        if(stepSpec.getPartition().getPlan()==null){
            stepSpec.getPartition().setPlan(new PartitionPlan());
        }    
         set.put("PARTITION_PROP", new ElementCustomPropertySupport(this.getModelerScene().getModelerFile(), stepSpec, Boolean.class,
                    "partitionAllowed", "Enable", "", partitionChangeListener)); 
        
         set.put("PARTITION_PROP", new ElementCustomPropertySupport(this.getModelerScene().getModelerFile(), stepSpec.getPartition(), Boolean.class,
                    "runtimeMapping", "Is Runtime Partiion Mapping", "", partitionChangeListener,partitionVisibilityHandler)); 
        
        set.put("PARTITION_PROP", new ElementCustomPropertySupport(this.getModelerScene().getModelerFile(), stepSpec.getPartition().getPlan(), String.class,
                    "threads", "Partition Thread", "", null , planVisibilityHandler)); 
        
        set.put("PARTITION_PROP", getPartitionProperty("planProperties", "Partition Properties", "", this.getModelerScene(), stepSpec.getPartition().getPlan(),planVisibilityHandler));
        
        
        if (stepSpec.getPartition().getMapper() == null) {
            stepSpec.getPartition().setMapper(new PartitionMapper());
        }
        if (stepSpec.getPartition().getMapper().getProperties() == null) {
            stepSpec.getPartition().getMapper().setProperties(new Properties());
        }
        set.put("PARTITION_PROP", new ElementCustomPropertySupport(this.getModelerScene().getModelerFile(), stepSpec.getPartition().getMapper(), String.class,
                    "mapperRef", "Mapper Reference", "", null,mapperVisibilityHandler));
        set.put("PARTITION_PROP",new NEntityPropertySupport(this.getModelerScene().getModelerFile(),JobUtil.addProperty(stepSpec.getPartition().getMapper().getProperties() , 
                "mapperProperties", "Mapper Properties", ""),mapperVisibilityHandler));
    
        if (stepSpec.getPartition().getReducer() == null) {
            stepSpec.getPartition().setReducer(new PartitionReducer());
        }
        if (stepSpec.getPartition().getReducer().getProperties() == null) {
            stepSpec.getPartition().getReducer().setProperties(new Properties());
        }
        set.put("PARTITION_PROP", new ElementCustomPropertySupport(this.getModelerScene().getModelerFile(), stepSpec.getPartition().getReducer(), String.class,
                    "reducerRef", "Reducer Reference", "", null , partitionVisibilityHandler));
        set.put("PARTITION_PROP",new NEntityPropertySupport(this.getModelerScene().getModelerFile(),JobUtil.addProperty(stepSpec.getPartition().getReducer().getProperties(),
                "reducerProperties", "Reducer Properties", "") , partitionVisibilityHandler));
        
        if (stepSpec.getPartition().getAnalyzer() == null) {
            stepSpec.getPartition().setAnalyzer(new Analyzer());
        }
        if (stepSpec.getPartition().getAnalyzer().getProperties() == null) {
            stepSpec.getPartition().getAnalyzer().setProperties(new Properties());
        }
        set.put("PARTITION_PROP", new ElementCustomPropertySupport(this.getModelerScene().getModelerFile(), stepSpec.getPartition().getAnalyzer(), String.class,
                    "analyzerRef", "Analyzer Reference", "", null,partitionVisibilityHandler));
        set.put("PARTITION_PROP",new NEntityPropertySupport(this.getModelerScene().getModelerFile(),JobUtil.addProperty(stepSpec.getPartition().getAnalyzer().getProperties(),
                "analyzerProperties", "Analyzer Properties", ""  ),partitionVisibilityHandler));
        
        if (stepSpec.getPartition().getCollector() == null) {
            stepSpec.getPartition().setCollector(new Collector());
        }
        if (stepSpec.getPartition().getCollector().getProperties() == null) {
            stepSpec.getPartition().getCollector().setProperties(new Properties());
        }
        set.put("PARTITION_PROP", new ElementCustomPropertySupport(this.getModelerScene().getModelerFile(), stepSpec.getPartition().getCollector(), String.class,
                    "collectorRef", "Collector Reference", "", null,partitionVisibilityHandler));
        set.put("PARTITION_PROP",new NEntityPropertySupport(this.getModelerScene().getModelerFile(),JobUtil.addProperty(stepSpec.getPartition().getCollector().getProperties(),
                "collectorProperties", "Collector Properties", ""  ),partitionVisibilityHandler));
        
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        } catch (NoSuchFieldException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    
     public PropertySupport getPartitionProperty(String id, String name, String desc, IModelerScene modelerScene, final PartitionPlan partitionPlan , PropertyVisibilityHandler propertyVisibilityHandler) {
        final NAttributeEntity attributeEntity = new NAttributeEntity(id, name, desc);
        attributeEntity.setCountDisplay(new String[]{"No Properties set", "One Property set", "Properties set"});

        List<Column> columns = new ArrayList<Column>();
        columns.add(new Column("OBJECT", false, true, Object.class));
        columns.add(new Column("Partition", false, String.class,true , "Partition"));
        columns.add(new Column("Properties", false, String.class));
        
//        columns.add(new Column("Size", false, String.class));

        attributeEntity.setColumns(columns);
        attributeEntity.setCustomDialog(new PartitionPropertiesPanel());

        attributeEntity.setTableDataListener(new NEntityDataListener() {
            List<Object[]> data;
            int count;

            @Override
            public void initCount() {
                count = partitionPlan.getProperties().size();
            }

            @Override
            public int getCount() {
                return count;
            }

            @Override
            public void initData() {
                List<Properties> propertiesList = partitionPlan.getProperties();
                List<Object[]> data_local = new LinkedList<Object[]>();
                Iterator<Properties> itr = propertiesList.iterator();
                int i=0;
                while (itr.hasNext()) {
                    Properties properties = itr.next();
                    Object[] row = new Object[attributeEntity.getColumns().size()];
                    row[0] = properties;
                    
//                    row[1] = "Partition " + ++i;
                    
                    StringBuilder text = new StringBuilder();
                    for (Property property : properties.getProperty()) {
                        text.append(property.getName()).append(",");
                    }
                    if(text.length()>0){
                    text.setLength(text.length() - 1);
                    }
                    row[2] = text.toString();

//                    int size = properties.getProperty().size();
//                    if (size == 0) {
//                        row[2] = "No Properties Exist";
//                    } else if (size == 1) {
//                        row[2] = "One Property Exist";
//                    } else if (size > 1) {
//                        row[2] = size + " Properties Exist";
//                    }
                    
                    data_local.add(row);
                }
                this.data = data_local;
            }

            @Override
            public List<Object[]> getData() {
                return data;
            }

            @Override
            public void setData(List data) {
                partitionPlan.getProperties().clear();
                for (Object[] row : (List<Object[]>) data) {
                    partitionPlan.getProperties().add((Properties) row[0]);
                }
               initData();
            }

        });

        return new NEntityPropertySupport(modelerScene.getModelerFile(), attributeEntity, propertyVisibilityHandler);
    }


}
