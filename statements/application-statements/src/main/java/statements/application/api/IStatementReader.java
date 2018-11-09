package statements.application.api;

import java.io.File;
import java.util.Properties;

/**
 * Specification for statement reader
 * 
 * @author Prajapati
 * @param <T>
 */
public interface IStatementReader<T> {

	/**
	 * Initialize reader
	 * 
	 * @param config
	 */
	void initialize(Properties config);

	/**
	 * Read from inputfile and feedback transformed object to given {@link IReaderCallback}
	 * 
	 * @param inputFile
	 * @param readerCallback
	 */
	void read(File inputFile, IReaderCallback<T> readerCallback);

}
