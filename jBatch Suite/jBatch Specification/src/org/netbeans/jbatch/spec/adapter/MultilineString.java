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
package org.netbeans.jbatch.spec.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MultilineString extends XmlAdapter<String, String> {

    @Override
    public String marshal(String input) throws Exception {
        return input == null ? null : input.replaceAll("\n", "\\\\n");//"&#xA;"
    }

    @Override
    public String unmarshal(String output) throws Exception {
        return output == null ? null : output.replaceAll("\\\\n", "\n");//"&#xA;"
    }
}
