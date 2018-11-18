/**
 * 
 */
package statements.application.reader;

import java.io.File;
import java.util.Properties;

import domain.statements.dom.impl.txn.Transaction;
import statements.application.api.IReaderCallback;

/**
 * Statment reader implementation for no-operation i.e. it will not do anything.
 * This will be used when configured reader type is with either no setting or setting corresponds to class that is not found.
 * 
 * @author Prajapati
 */
public class NoopStatmentReader extends AbstractStatementReader {

	/* (non-Javadoc)
	 * @see statements.application.api.IStatementReader#initialize(java.util.Properties)
	 */
	@Override
	public void initialize(Properties config) {
		// DO NOTHING
	}

	/* (non-Javadoc)
	 * @see statements.application.api.IStatementReader#read(java.io.File, statements.application.api.IReaderCallback)
	 */
	@Override
	public void read(File inputFile, IReaderCallback<Transaction> readerCallback) {
		// DO NOTHING
	}

}
