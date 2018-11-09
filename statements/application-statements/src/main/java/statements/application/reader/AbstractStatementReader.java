/**
 * 
 */
package statements.application.reader;

import javax.inject.Inject;

import domain.statements.dom.impl.ref.StatementSourceType;
import domain.statements.dom.impl.txn.StatementSource;
import domain.statements.dom.impl.txn.Transaction;
import domain.statements.dom.srv.txn.StatementSourceService;
import domain.statements.dom.srv.txn.TransactionService;
import statements.application.api.IStatementReader;

/**
 * @author Prajapati
 *
 */
public abstract class AbstractStatementReader implements IStatementReader<Transaction> {

	@Inject
	TransactionService transactionService;

	@Inject
	StatementSourceService statementSourceService;
	
	protected StatementSource getOrCreateStatementSource(String statementSource, StatementSourceType statementSourceType) {

		StatementSource source = statementSourceService.findByNameExact(statementSource);
		if (source == null) {
			source = statementSourceService.create(statementSource, statementSourceType);
		}
		return source;
	}

}
