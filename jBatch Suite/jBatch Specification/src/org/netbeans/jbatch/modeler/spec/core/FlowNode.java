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
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.netbeans.modeler.specification.model.document.core.IFlowNode;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tFlowNode", propOrder = {
    "incoming",
    "outgoing"
})
@XmlSeeAlso({
    Gateway.class,
    Activity.class,
    Event.class
})
public abstract class FlowNode
        extends FlowElement implements IFlowNode {

    @XmlElement
    protected List<String> incoming;//List<QName> incoming;
    @XmlElement
    protected List<String> outgoing;//List<QName> outgoing;

    /**
     * Gets the value of the incoming property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the incoming property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncoming().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link QName }
     *
     *
     */
    public List<String> getIncoming() {
        if (incoming == null) {
            incoming = new ArrayList<String>();
        }
        return this.incoming;
    }

    public void addIncoming(String incomingFlow) {
        if (incoming == null) {
            incoming = new ArrayList<String>();
        }
        this.incoming.add(incomingFlow);
    }

    public void removeIncoming(String incomingFlow) {
        if (incoming != null) {
            this.incoming.remove(incomingFlow);
        }
    }

    public void setIncoming(List<String> incoming) {
        this.incoming = incoming;
    }

    /**
     * Gets the value of the outgoing property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the outgoing property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOutgoing().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link QName }
     *
     *
     */
    public List<String> getOutgoing() {
        if (outgoing == null) {
            outgoing = new ArrayList<String>();
        }
        return this.outgoing;
    }

    public void addOutgoing(String outgoingFlow) {
        if (outgoing == null) {
            outgoing = new ArrayList<String>();
        }
        this.outgoing.add(outgoingFlow);
    }

    public void removeOutgoing(String outgoingFlow) {
        if (outgoing != null) {
            this.outgoing.remove(outgoingFlow);
        }

    }

    public void setOutgoing(List<String> outgoing) {
        this.outgoing = outgoing;
    }

}
