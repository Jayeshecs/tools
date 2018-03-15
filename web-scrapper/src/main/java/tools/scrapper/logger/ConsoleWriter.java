/**
 * 
 */
package tools.scrapper.logger;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

/**
 * @author Prajapati
 *
 */
public class ConsoleWriter extends Writer implements ILogWriter {

	private Writer logWriter;

	public ConsoleWriter() {
		this(new OutputStreamWriter(System.out));
	}
	
	/**
	 * To be used by sub-classes
	 * 
	 * @param writer
	 */
	protected ConsoleWriter(Writer writer) {
		this.logWriter = writer;
	}

	public void info(String error, Object... args) {
		try {
			Date date = new Date();
			logWriter.write(String.format("[INFO] %tY-%tm-%td %tT %s\n", date, date, date, date, String.format(error, args)));
			flush();
		} catch (IOException e) {
			// DO NOTHING
		}
	}
	
	public void error(String error, Object... args) {
		try {
			Date date = new Date();
			logWriter.write(String.format("[ERROR] %tY-%tm-%td %tT %s\n", date, date, date, date, String.format(error, args)));
			flush();
		} catch (IOException e) {
			// DO NOTHING
		}
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		logWriter.write(cbuf, off, len);
	}

	@Override
	public void flush() throws IOException {
		logWriter.flush();
	}

	@Override
	public void close() throws IOException {
		// DO NOTHING
	}

	@Override
	public Writer getWriter() {
		return this;
	}

}
