/*
 * Copyright 2018 Feedzai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.feedzai.openml.provider.descriptor;

import com.feedzai.openml.provider.descriptor.fieldtype.FreeTextFieldType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link ModelParameter}.
 *
 * @author Nuno Diegues (nuno.diegues@feedzai.com)
 * @since 0.1.0
 */
public class ModelParameterTest {

    /**
     * Tests for a valid parameter.
     */
    @Test
    public void testValidParameter() {

        final String paramName = "param1";
        final String descName = "desc1";
        final String help = "help1";
        final boolean isMandatory = true;
        final FreeTextFieldType fieldType = new FreeTextFieldType("default");

        final ModelParameter modelParameter = new ModelParameter(paramName, descName, help, isMandatory, fieldType);

        assertThat(modelParameter.getName())
                .isEqualTo(paramName);

        assertThat(modelParameter.getDescription())
                .isEqualTo(descName);

        assertThat(modelParameter.getHelperDescription())
                .isEqualTo(help);

        assertThat(modelParameter.isMandatory())
                .isEqualTo(isMandatory);

        assertThat(modelParameter.getFieldType())
                .isEqualTo(fieldType);

        final ModelParameter differentParam = new ModelParameter("another param", descName, help, isMandatory, fieldType);
        assertThat(modelParameter.toString())
                .isEqualTo(modelParameter.toString())
                .isNotNull()
                .isNotEmpty()
                .isNotEqualTo(differentParam);

        final ModelParameter modelParameterClone = new ModelParameter(
                modelParameter.getName(),
                modelParameter.getDescription(),
                modelParameter.getHelperDescription(),
                modelParameter.isMandatory(),
                modelParameter.getFieldType()
        );

        assertThat(modelParameter)
                .isEqualTo(modelParameter)
                .isEqualTo(modelParameterClone)
                .isEqualToComparingFieldByField(modelParameterClone)
                .isNotEqualTo(null)
                .isNotEqualTo(differentParam);

        assertThat(modelParameter.hashCode())
                .isEqualTo(modelParameter.hashCode())
                .isEqualTo(modelParameterClone.hashCode())
                .isNotEqualTo(differentParam.hashCode());
    }
}
