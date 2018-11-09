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
package statements.application.services.homepage;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.factory.FactoryService;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.value.Blob;

import com.google.common.io.Files;

import domain.statements.dom.impl.cfg.StatementReader;
import domain.statements.dom.impl.txn.Transaction;
import domain.statements.dom.srv.txn.TransactionService;
import lombok.extern.slf4j.Slf4j;
import statements.application.api.IReaderCallback;
import statements.application.api.IStatementReader;

@DomainObject(
        nature = Nature.VIEW_MODEL,
        objectType = "statements.application.services.homepage.HomePageViewModel"
)
@Slf4j
public class HomePageViewModel {

    public TranslatableString title() {
        return TranslatableString.tr("All transactions");
    }

    public List<Transaction> getTransactions() {
        return transactionService.listAll();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public void loadStatement(
    		@Parameter(optionality = Optionality.MANDATORY)
    		@ParameterLayout(named = "Reader")
    		StatementReader reader, 
    		@Parameter(optionality = Optionality.MANDATORY)
    		@ParameterLayout(named = "Statement File: ")
    		Blob file
    ) {
    	try {
	    	@SuppressWarnings("unchecked")
	    	IStatementReader<Transaction> stmtReader = (IStatementReader<Transaction>) factoryService.instantiate(reader.getReaderType().getClazz());
	    	Properties properties = new Properties();
	    	properties.putAll(reader.getProperties());
			stmtReader.initialize(properties);
			File tempFile = File.createTempFile("stmt", "reader");
			Files.write(file.getBytes(), tempFile);
			stmtReader.read(tempFile, new IReaderCallback<Transaction>() {
				
				@Override
				public void process(Collection<Transaction> records) {
					int existingCount = 0;
					for (Transaction record : records) {
						if (record.getId() == null) {
							Transaction transaction = transactionService.getTransactionByRawdata(record.getRawdata());
							if (transaction != null) {
								// skip transaction because it is already present
								existingCount++;
								continue ;
							}
						}
						transactionService.save(record);
					}
					// flush transactions
					txnService.flushTransaction();
					log.info(String.format("Inserted %d of %d transactions", (records.size() - existingCount), records.size()));
				}
			});
    	} catch (Exception e) {
    		log.error(String.format("Exception occurred while load transactions from statement file - %s", file.getName()), e);
    		messageService.informUser(String.format("Fail to load statement file %s using reader %s", file.getName(), reader.getName()));
    	}
    }

    @javax.inject.Inject
    MessageService messageService;

    @javax.inject.Inject
    TransactionService transactionService;

    @javax.inject.Inject
    org.apache.isis.applib.services.xactn.TransactionService txnService;
    
    @javax.inject.Inject
    FactoryService factoryService;
}
