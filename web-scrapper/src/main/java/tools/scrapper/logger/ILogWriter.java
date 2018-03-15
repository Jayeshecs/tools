package tools.scrapper.logger;

import java.io.Writer;


/**
 * @author Prajapati
 * @see ConsoleWriter
 * @see CompositeLogWriter
 */
public interface ILogWriter {

	/**
	 * Log info message to writer
	 * @param error
	 * @param args
	 */
	void info(String error, Object... args);
	
	/**
	 * Log error message to writer
	 * @param error
	 * @param args
	 */
	void error(String error, Object... args);

	/**
	 * @return
	 */
	Writer getWriter();

}
