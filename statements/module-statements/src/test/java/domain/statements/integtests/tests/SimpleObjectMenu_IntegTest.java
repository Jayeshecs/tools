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
package domain.statements.integtests.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.inject.Inject;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;

import com.google.common.base.Throwables;

import domain.statements.dom.impl.SimpleObject;
import domain.statements.dom.impl.SimpleObjects;
import domain.statements.fixture.SimpleObject_persona;
import domain.statements.integtests.SimpleModuleIntegTestAbstract;

public class SimpleObjectMenu_IntegTest extends SimpleModuleIntegTestAbstract {

    public static class ListAll extends SimpleObjectMenu_IntegTest {

        @Test
        public void happyCase() {

            // given
            fixtureScripts.runFixtureScript(new SimpleObject_persona.PersistAll());
            transactionService.nextTransaction();

            // when
            final List<SimpleObject> all = wrap(menu).listAll();

            // then
            assertThat(all).hasSize(SimpleObject_persona.values().length);
        }

        @Test
        public void whenNone() {

            // when
            final List<SimpleObject> all = wrap(menu).listAll();

            // then
            assertThat(all).hasSize(0);
        }
    }

    public static class Create extends SimpleObjectMenu_IntegTest {

        @Test
        public void happyCase() {

            wrap(menu).create("Faz");

            // then
            final List<SimpleObject> all = wrap(menu).listAll();
            assertThat(all).hasSize(1);
        }

        @Test
        public void whenAlreadyExists() {

            // given
            fixtureScripts.runBuilderScript(SimpleObject_persona.FIZZ.builder());
            transactionService.nextTransaction();

            // expect
        	Throwable cause = assertThrows(Throwable.class, ()->{
            
                // when
                wrap(menu).create("Fizz");
                transactionService.nextTransaction();
            	
            });
        	
        	// also expect
        	MatcherAssert.assertThat(cause, 
        			causalChainContains(SQLIntegrityConstraintViolationException.class));

        }

        private static Matcher<Throwable> causalChainContains(final Class<?> cls) {
            return new TypeSafeMatcher<Throwable>() {
                @Override
                protected boolean matchesSafely(Throwable item) {
                    final List<Throwable> causalChain = Throwables.getCausalChain(item);
                    for (Throwable throwable : causalChain) {
                        if(cls.isAssignableFrom(throwable.getClass())){
                            return true;
                        }
                    }
                    return false;
                }

                //@Override
                public void describeTo(Description description) {
                    description.appendText("exception with causal chain containing " + cls.getSimpleName());
                }
            };
        }
    }

    @Inject
    SimpleObjects menu;

}