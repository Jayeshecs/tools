/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package domain.statements.specglue;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.core.specsupport.specs.CukeGlueAbstract2;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import domain.statements.dom.impl.ref.TransactionType;
import domain.statements.dom.impl.txn.StatementSource;
import domain.statements.dom.impl.txn.Transaction;
import domain.statements.dom.srv.txn.StatementSourceService;
import domain.statements.dom.srv.txn.TransactionService;
import domain.statements.fixture.txn.StatementSource_persona;

public class StatementsMenuGlue extends CukeGlueAbstract2 {

    @Given("^there are.* (\\d+) transactions$")
    public void there_are_N_transactions(int n) throws Throwable {
        final List<Transaction> list = wrap(transactionService).listAll();
        assertThat(list.size(), is(n));
    }
    
    @When("^.*create a .*transaction$")
    public void create_a_transaction() throws Throwable {
        StatementSource hdfc1668 = statementSourceService.findByNameExact(StatementSource_persona.HDFC_1668.name());
        wrap(transactionService).create(hdfc1668, TransactionType.DEBIT, new Date(), new BigDecimal(15000.0), "For dad", "ref0003");
    }

    @Inject
    TransactionService transactionService;
    
    @Inject
    StatementSourceService statementSourceService;

}
