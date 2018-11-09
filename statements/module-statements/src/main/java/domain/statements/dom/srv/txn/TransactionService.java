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
package domain.statements.dom.srv.txn;

import java.math.BigDecimal;
import java.util.Date;

import javax.jdo.JDOQLTypedQuery;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.events.domain.ActionDomainEvent;

import domain.statements.dom.impl.ref.TransactionType;
import domain.statements.dom.impl.txn.QTransaction;
import domain.statements.dom.impl.txn.StatementSource;
import domain.statements.dom.impl.txn.Transaction;
import domain.statements.dom.srv.AbstractService;

@DomainService(
        nature = NatureOfService.DOMAIN,
        objectType = "statements.TransactionService",
        repositoryFor = Transaction.class
)
public class TransactionService extends AbstractService {

    @SuppressWarnings("serial")
	public static class CreateDomainEvent extends ActionDomainEvent<TransactionService> {}
    
    @Action(domainEvent = CreateDomainEvent.class)
    public Transaction create(StatementSource statementSource, TransactionType transactionType, Date date, BigDecimal amount, String narration, String reference) {
        Transaction transaction = new Transaction();
        transaction.setType(transactionType);
        transaction.setSource(statementSource);
        transaction.setAmount(amount);
        transaction.setNarration(narration);
        transaction.setReference(reference);
        transaction.setTransactionDate(date);
        transaction.setValueDate(date);
		return repositoryService.persist(transaction);
    }

    @Programmatic
	public Transaction findByReferenceExact(String reference) {
    	JDOQLTypedQuery<Transaction> q = isisJdoSupport.newTypesafeQuery(Transaction.class);
        final QTransaction cand = QTransaction.candidate();
        q = q.filter(cand.reference.eq(q.stringParameter("reference"))
        );
        return q.setParameter("reference", reference)
                .executeUnique();
	}

}
