/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package domain.statements.integtests.tests.srv;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Timestamp;

import javax.inject.Inject;

import org.apache.isis.applib.services.title.TitleService;
import org.apache.isis.applib.services.wrapper.DisabledException;
import org.apache.isis.core.metamodel.services.jdosupport.Persistable_datanucleusIdLong;
import org.apache.isis.core.metamodel.services.jdosupport.Persistable_datanucleusVersionTimestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.statements.dom.impl.ref.SubCategory;
import domain.statements.fixture.txn.SubCategory_persona;
import domain.statements.integtests.SimpleModuleIntegTestAbstract;

public class SubCategory_IntegTest extends SimpleModuleIntegTestAbstract {

	SubCategory subCategory;

    @BeforeEach
    public void setUp() {
        // given
        subCategory = fixtureScripts.runBuilderScript(SubCategory_persona.ACC_TO_ACC.builder());
    }

    public static class Name extends SubCategory_IntegTest {

        @Test
        public void accessible() {
            // when
            final String name = wrap(subCategory).getName();

            // then
            assertThat(name).isEqualTo(subCategory.getName());
        }

        @Test
        public void not_editable() {
        	
        	// expect
            assertThrows(DisabledException.class, ()->{
            
            	// when
            	wrap(subCategory).setName("new name");
            	
            });
        }

    }

    public static class UpdateName extends SubCategory_IntegTest {

        @Test
        public void can_be_updated_directly() {

            // when
            wrap(subCategory).updateName("new name");
            transactionService.nextTransaction();

            // then
            assertThat(wrap(subCategory).getName()).isEqualTo("new name");
        }
    }


    public static class Title extends SubCategory_IntegTest {

        @Inject
        TitleService titleService;

        @Test
        public void interpolatesName() {

            // given
            final String name = wrap(subCategory).getName();

            // when
            final String title = titleService.titleOf(subCategory);

            // then
            assertThat(title).isEqualTo("" + name);
        }
    }

    public static class DataNucleusId extends SubCategory_IntegTest {

        @Test
        public void should_be_populated() {
            // when
            final Long id = mixin(Persistable_datanucleusIdLong.class, subCategory).prop();

            // then
            assertThat(id).isGreaterThanOrEqualTo(0);
        }
    }

    public static class DataNucleusVersionTimestamp extends SubCategory_IntegTest {

        @Test
        public void should_be_populated() {
            // when
            final Timestamp timestamp = mixin(Persistable_datanucleusVersionTimestamp.class, subCategory).prop();
            // then
            assertThat(timestamp).isNotNull();
        }
    }

}