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

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import domain.statements.dom.impl.ref.Category;
import domain.statements.dom.impl.ref.SubCategory;
import domain.statements.dom.impl.txn.StatementSource;
import domain.statements.dom.impl.txn.Transaction;
import domain.statements.dom.impl.view.StatementsMenu;
import domain.statements.fixture.ref.Category_persona;
import domain.statements.fixture.ref.SubCategory_persona;
import domain.statements.fixture.txn.StatementSource_persona;
import domain.statements.integtests.SimpleModuleIntegTestAbstract;

public class Statements_IntegTest extends SimpleModuleIntegTestAbstract {

    public static class ListAll extends Statements_IntegTest {

        @Test
        public void happyCase() {

            // given
            fixtureScripts.runFixtureScript(new Category_persona.PersistAll());
            fixtureScripts.runFixtureScript(new SubCategory_persona.PersistAll());
            fixtureScripts.runFixtureScript(new StatementSource_persona.PersistAll());
            transactionService.nextTransaction();

            // when
            List<Category> categories = wrap(menu).categories();
            List<SubCategory> subCategories = wrap(menu).subCategories();
            List<StatementSource> statementSources = wrap(menu).statementSources();
            List<Transaction> transactions = wrap(menu).transactions();
            
            // then
            assertThat(categories).hasSize(Category_persona.values().length);
            assertThat(subCategories).hasSize(SubCategory_persona.values().length);
            assertThat(statementSources).hasSize(StatementSource_persona.values().length);
            assertThat(transactions).hasSize(0);
        }

        @Test
        public void whenNone() {

            // when
            List<Category> categories = wrap(menu).categories();
            List<SubCategory> subCategories = wrap(menu).subCategories();
            List<StatementSource> statementSources = wrap(menu).statementSources();
            List<Transaction> transactions = wrap(menu).transactions();
            
            // then
            assertThat(categories).hasSize(0);
            assertThat(subCategories).hasSize(0);
            assertThat(statementSources).hasSize(0);
            assertThat(transactions).hasSize(0);
        }
    }

    @Inject
    StatementsMenu menu;

}