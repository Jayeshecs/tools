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

import org.apache.isis.applib.fixturescripts.PersonaWithBuilderScript;
import org.apache.isis.applib.fixturescripts.PersonaWithFinder;
import org.apache.isis.applib.fixturescripts.setup.PersonaEnumPersistAll;
import org.apache.isis.applib.services.registry.ServiceRegistry;

import domain.statements.dom.impl.ref.TransactionType;
import domain.statements.dom.impl.txn.Transaction;
import domain.statements.dom.srv.txn.TransactionService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Transaction_persona implements PersonaWithBuilderScript<Transaction, TransactionBuilder>,
        PersonaWithFinder<Transaction> {

	SALARY(StatementSource_persona.HDFC_1668, TransactionType.CREDIT, new BigDecimal("100000.0"), "Salary", "ref0001"),
	HOUSEHOLD(StatementSource_persona.HDFC_1668, TransactionType.DEBIT, new BigDecimal("15000.0"), "Household", "ref0002")
	;
    private final StatementSource_persona statementSourcePersona;

    private final TransactionType transactionType;

    private final BigDecimal amount;

    private final String narration;

    private final String reference;

    public TransactionBuilder builder() {
        return new TransactionBuilder().setStatementSource(statementSourcePersona.name()).setTransactionType(transactionType).setAmount(amount).setNarration(narration).setReference(reference);
    }

    public Transaction findUsing(final ServiceRegistry serviceRegistry) {
    	TransactionService transactionService = serviceRegistry.lookupService(TransactionService.class);
        return transactionService.findByReferenceExact(reference);
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<Transaction_persona, Transaction, TransactionBuilder> {
        public PersistAll() {
            super(Transaction_persona.class);
        }
    }
}
