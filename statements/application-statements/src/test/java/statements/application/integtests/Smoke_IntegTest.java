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
package statements.application.integtests;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import domain.statements.dom.impl.ref.Category;
import domain.statements.dom.impl.ref.SubCategory;
import domain.statements.dom.impl.ref.TransactionType;
import domain.statements.dom.impl.txn.StatementSource;
import domain.statements.dom.impl.txn.Transaction;
import domain.statements.dom.srv.ref.CategoryService;
import domain.statements.dom.srv.ref.SubCategoryService;
import domain.statements.dom.srv.txn.StatementSourceService;
import domain.statements.dom.srv.txn.TransactionService;
import domain.statements.fixture.ref.Category_persona;
import domain.statements.fixture.ref.SubCategory_persona;
import domain.statements.fixture.txn.StatementSource_persona;

public class Smoke_IntegTest extends DomainAppIntegTestAbstract {

    @Inject
    TransactionService menu;
    
    @Inject
    StatementSourceService statementSourceService;
    
    @Inject
    CategoryService categoryService;
    
    @Inject
    SubCategoryService subCategoryService;

    @Test
    public void create() {

        // when
        List<Transaction> all = wrap(menu).listAll();

        // then
        assertThat(all).isEmpty();



        // when
        StatementSource hdfc1668 = statementSourceService.findByNameExact(StatementSource_persona.HDFC_1668.name());
        final Transaction salary = wrap(menu).create(hdfc1668, TransactionType.CREDIT, new Date(), new BigDecimal(100000.0), "Salary", "ref0001");
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAll();
        assertThat(all).hasSize(1);
        assertThat(all).contains(salary);



        // when
        final Transaction household = wrap(menu).create(hdfc1668, TransactionType.DEBIT, new Date(), new BigDecimal(15000.0), "Hoursehold", "ref0002");
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAll();
        assertThat(all).hasSize(2);
        assertThat(all).contains(salary, household);



        // when
        Category cIncome = categoryService.findByNameExact(Category_persona.INCOME.name());
        SubCategory scSalary = subCategoryService.findByNameExact(SubCategory_persona.SALARY.name());
        Transaction wrap = wrap(salary);
		wrap.setCategory(cIncome);
		wrap.setSubCategory(scSalary);
        transactionService.flushTransaction();

        // then
        assertThat(wrap(salary).getCategory()).isEqualTo(cIncome);
        assertThat(wrap(salary).getSubCategory()).isEqualTo(scSalary);

        // when
        wrap(household).delete();
        transactionService.flushTransaction();


        all = wrap(menu).listAll();
        assertThat(all).hasSize(1);

    }

}

