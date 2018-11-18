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

package domain.statements.fixture.txn;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.isis.applib.fixturescripts.BuilderScriptAbstract;

import domain.statements.dom.impl.ref.TransactionType;
import domain.statements.dom.impl.txn.Transaction;
import domain.statements.dom.srv.txn.StatementSourceService;
import domain.statements.dom.srv.txn.TransactionService;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class TransactionBuilder extends BuilderScriptAbstract<Transaction, TransactionBuilder> {

    @Getter @Setter
    private TransactionType transactionType;

    @Getter @Setter
    private String statementSource;

    @Getter @Setter
    private BigDecimal amount;

    @Getter @Setter
    private String narration;

    @Getter @Setter
    private String reference;

    @Getter
    private Transaction object;

    @Override
    protected void execute(final ExecutionContext ec) {

        checkParam("transactionType", ec, TransactionType.class);

        checkParam("statementSource", ec, String.class);

        checkParam("amount", ec, BigDecimal.class);

        checkParam("narration", ec, String.class);

        checkParam("reference", ec, String.class);

        object = wrap(transactionService).create(
        		statementSourceService.findByNameExact(statementSource), 
        		transactionType, 
        		new Date(), 
        		amount, 
        		narration, 
        		reference);
    }

    @javax.inject.Inject
    TransactionService transactionService;

    @javax.inject.Inject
    StatementSourceService statementSourceService;

}
