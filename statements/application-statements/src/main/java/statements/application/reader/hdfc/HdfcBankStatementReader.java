/**
 * 
 */
package statements.application.reader.hdfc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;

import domain.statements.dom.impl.ref.StatementSourceType;
import domain.statements.dom.impl.ref.TransactionType;
import domain.statements.dom.impl.txn.StatementSource;
import domain.statements.dom.impl.txn.Transaction;
import lombok.extern.slf4j.Slf4j;
import statements.application.api.IReaderCallback;
import statements.application.reader.AbstractStatementReader;

/**
 * @author Prajapati
 *
 */
@DomainObject(
        nature = Nature.VIEW_MODEL,
        objectType = "statements.application.reader.hdfc.HdfcBankStatementReader"
)
@Slf4j
public class HdfcBankStatementReader extends AbstractStatementReader {

	private Properties config;

	@Override
	public void initialize(Properties config) {
		this.config = config;
	}

	@Override
	public void read(File inputFile, IReaderCallback<Transaction> readerCallback) {
		SimpleDateFormat ddMMyy = new SimpleDateFormat(config.getProperty("dateFormat", "dd/MM/yy"));
		try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
			String line = reader.readLine();
			StatementSource source = getOrCreateStatementSource(line.trim(), StatementSourceType.SAVING_ACCOUNT);
			Collection<Transaction> batch = new ArrayList<>();
			reader.readLine(); // skip header line
			while (reader.ready()) {
				String record = reader.readLine();
				Transaction transaction = new Transaction();
				transaction.setSource(source);
				transaction.setRawdata(record);
				try {
					parse(record, transaction, ddMMyy);
				} catch (Exception e) {
					log.error(String.format("Parsing failed for record : %s", record));
					log.error(String.format("Error: %s", e.getMessage() == null ? e.getClass().getName() : e.getMessage()));
					continue ;
				}
				batch.add(transaction);
				if (batch.size() > 100) {
					readerCallback.process(batch);
					batch = new ArrayList<>();
				}
			}
			
			if (!batch.isEmpty()) {
				readerCallback.process(batch);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void parse(String record, Transaction transaction, SimpleDateFormat ddMMyy) throws ParseException {
		String[] values = record.split(",");
		transaction.setTransactionDate(ddMMyy.parse(values[0].toString())); // dd/mm/yy
		transaction.setNarration(values[1]);
		transaction.setValueDate(ddMMyy.parse(values[2].trim())); // dd/mm/yy
		BigDecimal debitAmount = new BigDecimal(values[3].trim());
		BigDecimal creditAmount = new BigDecimal(values[4].trim());
		if (debitAmount.signum() == 0) {
			transaction.setType(TransactionType.CREDIT);
			transaction.setAmount(creditAmount);
		} else {
			transaction.setType(TransactionType.DEBIT);
			transaction.setAmount(debitAmount);
		}
		transaction.setReference(values[5]);
	}

}
