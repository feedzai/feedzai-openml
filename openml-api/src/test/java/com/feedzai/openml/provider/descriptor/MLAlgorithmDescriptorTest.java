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

import com.feedzai.openml.provider.descriptor.MLAlgorithmDescriptor;
import com.feedzai.openml.provider.descriptor.MachineLearningAlgorithmType;
import com.feedzai.openml.provider.descriptor.ModelParameter;
import com.feedzai.openml.provider.descriptor.fieldtype.FreeTextFieldType;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link MLAlgorithmDescriptor}.
 *
 * @author Nuno Diegues (nuno.diegues@feedzai.com)
 * @since 0.1.0
 */
public class MLAlgorithmDescriptorTest {

    /**
     * A valid URL to use.
     */
    private URL url;

    /**
     * Sets up before each test.
     *
     * @throws MalformedURLException If the URL cannot be created.
     */
    @Before
    public void setUp() throws MalformedURLException {
        this.url = new URL("https://www.feedzai.com");
    }

    /**
     * Tests a valid descriptor.
     */
    @Test
    public void testDescriptor() {
        final ModelParameter modelParameter =
                new ModelParameter("param1", "desc1", "help1", true, new FreeTextFieldType("def"));

        final String descAlg1 = "alg1";
        final MLAlgorithmDescriptor desc1 = new MLAlgorithmDescriptor(
                descAlg1,
                ImmutableSet.of(modelParameter),
                MachineLearningAlgorithmType.MULTI_CLASSIFICATION,
                this.url
        );

        assertThat(desc1.getAlgorithmName())
                .isEqualTo(descAlg1);

        assertThat(desc1.getParameters())
                .containsOnly(modelParameter);

        assertThat(desc1.getAlgorithmType())
                .isEqualTo(MachineLearningAlgorithmType.MULTI_CLASSIFICATION);

        assertThat(desc1.getDocumentation())
                .isEqualTo(this.url);


        final MLAlgorithmDescriptor desc2 = new MLAlgorithmDescriptor(
                "another",
                ImmutableSet.of(modelParameter),
                MachineLearningAlgorithmType.BINARY_CLASSIFICATION,
                this.url
        );

        final MLAlgorithmDescriptor desc1Clone = new MLAlgorithmDescriptor(
                desc1.getAlgorithmName(),
                desc1.getParameters(),
                desc1.getAlgorithmType(),
                desc1.getDocumentation()
        );

        assertThat(desc1.toString())
                .isNotNull()
                .isNotEmpty()
                .isNotEqualTo(desc2.toString());

        assertThat(desc1)
                .isEqualTo(desc1)
                .isEqualTo(desc1Clone)
                .isEqualToComparingFieldByField(desc1Clone)
                .isNotEqualTo(null)
                .isNotEqualTo(desc2);

        assertThat(desc1.hashCode())
                .isEqualTo(desc1Clone.hashCode())
                .isNotEqualTo(desc2.hashCode());
    }

}
