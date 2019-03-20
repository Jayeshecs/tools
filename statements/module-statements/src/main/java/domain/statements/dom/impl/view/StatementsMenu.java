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
package domain.statements.dom.impl.view;

import java.util.Date;
import java.util.List;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.repository.RepositoryService;

import domain.statements.dom.impl.cfg.ReaderType;
import domain.statements.dom.impl.cfg.StatementReader;
import domain.statements.dom.impl.ref.Category;
import domain.statements.dom.impl.ref.SubCategory;
import domain.statements.dom.impl.txn.StatementSource;
import domain.statements.dom.impl.txn.Transaction;
import domain.statements.dom.srv.cfg.StatementReaderService;
import domain.statements.dom.srv.txn.TransactionService;
import domain.statements.dom.types.Name;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "statements.StatementsMenu",
        menuOrder = "2",
        repositoryFor = Transaction.class
)
@DomainServiceLayout(
        named = "Statements",
        menuOrder = "11"
)
public class StatementsMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public List<Category> categories() {
        return repositoryService.allInstances(Category.class);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    public List<SubCategory> subCategories() {
        return repositoryService.allInstances(SubCategory.class);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "4")
    public List<StatementSource> statementSources() {
        return repositoryService.allInstances(StatementSource.class);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "5")
    public List<Transaction> transactions() {
        return repositoryService.allInstances(Transaction.class);
    }
    
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "7")
    public List<ReaderType> readerTypes() {
    	return repositoryService.allInstances(ReaderType.class);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "8")
    public List<StatementReader> statementReaders() {
        return repositoryService.allInstances(StatementReader.class);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "9")
    public StatementReader createStatementReader(
    		@Parameter(optionality = Optionality.MANDATORY)
    		@Name
    		String name,
    		
    		@Parameter(optionality = Optionality.MANDATORY)
    		ReaderType readerType
    	) {
        return readerService.create(name, readerType);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "10")
    public List<Transaction> searchTransaction(
    		@Parameter(optionality = Optionality.OPTIONAL)
    		@ParameterLayout(named = "Category")
    		Category category,
    		
    		@Parameter(optionality = Optionality.OPTIONAL)
    		@ParameterLayout(named = "Sub Category")
    		SubCategory subCategory,
    		
    		@Parameter(optionality = Optionality.OPTIONAL)
    		@ParameterLayout(named = "Categorized?")
    		Boolean categorized,
    		
    		@Parameter(optionality = Optionality.OPTIONAL)
    		@ParameterLayout(named = "Narration contains")
    		String narration,
    		
    		@Parameter(optionality = Optionality.OPTIONAL)
    		@ParameterLayout(named = "Start Date >=")
    		Date startDate,
    		
    		@Parameter(optionality = Optionality.OPTIONAL)
    		@ParameterLayout(named = "End Date <=")
    		Date endDate,
    		
    		@Parameter(optionality = Optionality.OPTIONAL)
    		@ParameterLayout(named = "Statement Source")
    		StatementSource statementSource
    		) {
    	
    	List<Transaction> list = transactionService.list(category, subCategory, narration, categorized, startDate, endDate, statementSource);
    	
    	return list;
    }

    @javax.inject.Inject
    StatementReaderService readerService;

    @javax.inject.Inject
    TransactionService transactionService;

    @javax.inject.Inject
    RepositoryService repositoryService;

}
