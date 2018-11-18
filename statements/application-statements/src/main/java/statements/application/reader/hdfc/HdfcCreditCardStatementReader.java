/**
 * 
 */
package statements.application.reader.hdfc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

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
        objectType = "statements.application.reader.hdfc.HdfcCreditCardStatementReader"
)
@Slf4j
public class HdfcCreditCardStatementReader extends AbstractStatementReader {

	private Properties config;
	private String[] passwords;

	@Override
	public void initialize(Properties config) {
		this.config = config;
		this.passwords = this.config.getProperty("passwords", "").split(",");
	}

	@Override
	public void read(File inputFile, IReaderCallback<Transaction> readerCallback) {
		SimpleDateFormat ddMMyyyy = new SimpleDateFormat(config.getProperty("dateFormat", "dd/MM/yyyy"));
		try (PDDocument document = open(inputFile);/* FileWriter fileWriter = new FileWriter(inputFile.getAbsolutePath() + ".txt")*/) {
			if (document == null) {
				throw new IllegalStateException(String.format("Failed to open '%s' as PDF file", inputFile));
			}
			/**
			 * Disable all securities
			 */
			document.setAllSecurityToBeRemoved(true);
			/**
			 * Initialize PDFTextStripper 
			 * Initialize StringWriter
			 */
			PDFTextStripper textStripper = new PDFTextStripper();
			StringWriter writer = new StringWriter();
			/**
			 * Extract all text from document
			 */
			textStripper.writeText(document, writer);
//			textStripper.writeText(document, fileWriter);
			String content = writer.getBuffer().toString();
			int indexOfStatementSource = content.indexOf("Card No: ");
			String statementSource = content.substring(indexOfStatementSource, indexOfStatementSource + 29);
			StatementSource source = getOrCreateStatementSource(statementSource, StatementSourceType.CREDIT_CARD);
			
			/**
			 * Make use of regex to locate transaction
			 */
			String regexTransaction = "(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d.*";
			int indexStartOfTransactions = content.indexOf("Date  Transaction Description Amount");
			BufferedReader reader = new BufferedReader(new StringReader(content.substring(indexStartOfTransactions)));
			Collection<Transaction> batch = new ArrayList<>();
			int attemptToReadCount = 3;
			while (reader.ready()) {
				String record = reader.readLine();
				if (record == null || record.trim().isEmpty()) {
					if (attemptToReadCount-- < 0) {
						log.warn("Looks like StringReader stream is stuck and hence breaking");
						break;
					}
					continue;
				}
				record = record.trim();
				if (record.matches(regexTransaction)) {
					/**
					 * Parsing logic
					 * DEBIT :> 18/06/2018 IGST-VPS1817063905247-RATE 18.0 -27 (Ref# 09999999980618009935453) 18.00
					 * CREDIT:> 08/07/2018 NETBANKING TRANSFER (Ref# 00000000000708013285133) 37,092.00 Cr
					 */
					Transaction transaction = new Transaction();
					transaction.setRawdata(record);
					transaction.setSource(source);
					try {
						parse(record, transaction, ddMMyyyy);
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
			}
			
			if (!batch.isEmpty()) {
				readerCallback.process(batch);
			}
		} catch (IOException e) {
			log.error(String.format("I/O error occurred while opening '%s' as PDF file", inputFile), e);
			throw new IllegalStateException(String.format("I/O error occurred while opening '%s' as PDF file", inputFile));
		}
	}

	private static void parse(String line, Transaction transaction, SimpleDateFormat ddMMyyyy) throws ParseException {
		Date date = ddMMyyyy.parse(line.substring(0, 10));
		transaction.setTransactionDate(date);
		transaction.setValueDate(date);
		line = line.substring(11);
		transaction.setType(TransactionType.DEBIT);
		if (line.endsWith("Cr")) {
			transaction.setType(TransactionType.CREDIT);
			line = line.substring(0, line.length() - " Cr".length());
		}
		/**
		 * 
		 */
		int indexOfAmount = line.lastIndexOf(' ');
		BigDecimal amount = new BigDecimal(line.substring(indexOfAmount).trim().replace(",", ""));
		transaction.setAmount(amount);
		transaction.setNarration(line = line.substring(0, indexOfAmount));
		int indexOfReference = line.indexOf("Ref#");
		if (indexOfReference != -1) {
			transaction.setReference(line.substring(indexOfReference + 5, line.lastIndexOf(')')));
		}
	}

	/**
	 * @param inputFile PDF file that may be password protected
	 * @return null if given file PDF is password protected
	 * @throws IOException
	 */
	private PDDocument open(File inputFile) throws IOException {
		/**
		 * First try to open without password
		 */
		try {
			PDDocument document = PDDocument.load(inputFile);
			return document;
		} catch (InvalidPasswordException e) {
			// DO NOTHING
			// password protected
		}
		/**
		 * Second try to open with available passwords
		 */
		for (int i = 0; i < passwords.length; ++i) {
			try {
				PDDocument document = PDDocument.load(inputFile, passwords[i]);
				return document;
			} catch (InvalidPasswordException e) {
				// DO NOTHING
				// failed to open with given password
			}
		}
		/**
		 * Unable to open given file as PDF document with/without passwords
		 */
		return null;
	}

}
