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
@javax.xml.bind.annotation.XmlSchema(
        namespace = "http://xmlns.jcp.org/xml/ns/javaee",
        elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED,
        xmlns = {
            //            @XmlNs(prefix = "xsi",
            //                    namespaceURI = "http://www.w3.org/2001/XMLSchema-instance"),
            //            @XmlNs(prefix = "java",
            //                    namespaceURI = "http://jcp.org/en/jsr/detail?id=270"),
            @XmlNs(prefix = "jbatch",
                    namespaceURI = "http://xmlns.jcp.org/xml/ns/javaee"),
            @XmlNs(prefix = "jbatchnb",
                    namespaceURI = "http://jbatchsuite.java.net"),
            @XmlNs(prefix = "nbm",
                    namespaceURI = "http://nbmodeler.java.net")

        })
package org.netbeans.jbatch.modeler.spec.core;

import javax.xml.bind.annotation.XmlNs;
