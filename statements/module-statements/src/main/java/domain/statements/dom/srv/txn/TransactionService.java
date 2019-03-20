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
import java.util.List;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.query.BooleanExpression;
import javax.jdo.query.Expression;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.events.domain.ActionDomainEvent;

import domain.statements.dom.impl.ref.Category;
import domain.statements.dom.impl.ref.SubCategory;
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
    	JDOQLTypedQuery<Transaction> q = isisJdoSupport.getJdoPersistenceManager().newJDOQLTypedQuery(Transaction.class);
        final QTransaction cand = QTransaction.candidate();
        q = q.filter(cand.reference.eq(q.stringParameter("reference"))
        );
        return q.setParameter("reference", reference)
                .executeUnique();
	}

    @Programmatic
	public List<Transaction> listAll() {
    	JDOQLTypedQuery<Transaction> q = isisJdoSupport.getJdoPersistenceManager().newJDOQLTypedQuery(Transaction.class);
        return q.executeList();
	}

	@Programmatic
	public List<Transaction> list(Category category, SubCategory subCategory, String narration, Boolean categorized, Date startDate, Date endDate, StatementSource statementSource) {
    	JDOQLTypedQuery<Transaction> q = isisJdoSupport.getJdoPersistenceManager().newJDOQLTypedQuery(Transaction.class);
        final QTransaction cand = QTransaction.candidate();
        
        BooleanExpression categoryExpression = null;
        BooleanExpression subCategoryExpression = null;
        BooleanExpression narrationExpression = null;
        BooleanExpression startDateExpression = null;
        BooleanExpression endDateExpression = null;
        BooleanExpression statementSourceExpression = null;
        
        if (categorized == null || categorized == true) {
        	if (category != null) {
        		categoryExpression = cand.category.eq(category);
        	}
        	if (subCategory != null) {
        		subCategoryExpression = cand.subCategory.eq(subCategory);
        	}
        } else {
    		categoryExpression = cand.category.ne((Expression<Category>)null);
    		subCategoryExpression = cand.subCategory.ne((Expression<SubCategory>)null);
        }
        
        if (narration != null && !narration.trim().isEmpty()) {
        	narrationExpression = cand.narration.indexOf(narration).gt(0L);
        }
        
        if (startDate != null) {
        	startDateExpression = cand.transactionDate.gteq(startDate);
        }
        
        if (endDate != null) {
        	endDateExpression = cand.transactionDate.lteq(endDate);
        }
        
        if (statementSource != null) {
        	statementSourceExpression = cand.source.eq(statementSource);
        }
    	
        BooleanExpression filterExpression = and(categoryExpression, subCategoryExpression, narrationExpression, startDateExpression, endDateExpression, statementSourceExpression);
    	
        if (filterExpression != null) {
    		q.filter(filterExpression);
    	}
        
        return q.executeList();
	}
	
	private BooleanExpression and(BooleanExpression... expressions) {
		BooleanExpression result = null;
		for (BooleanExpression expression : expressions) {
			if (expression == null) {
				continue ;
			}
			if (result == null) {
				result = expression;
				continue ;
			}
			result.and(expression);
		}
		return result;
	}

	public void save(Transaction record) {
		repositoryService.persist(record);
	}

	public Transaction getTransactionByRawdata(String rawdata) {
    	JDOQLTypedQuery<Transaction> q = isisJdoSupport.getJdoPersistenceManager().newJDOQLTypedQuery(Transaction.class);
        final QTransaction cand = QTransaction.candidate();
        q = q.filter(cand.rawdata.eq(q.stringParameter("rawdata"))
        );
        return q.setParameter("rawdata", rawdata)
                .executeUnique();
	}

}
