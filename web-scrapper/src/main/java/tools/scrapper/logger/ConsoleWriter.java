/**
 * 
 */
package tools.scrapper.logger;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * @author Prajapati
 *
 */
public class ConsoleWriter extends Writer {

	private Writer logWriter;

	public ConsoleWriter() {
		this.logWriter = new OutputStreamWriter(System.out);
	}
	
	public void info(String error, Object... args) {
		try {
			logWriter.write(String.format("[INFO] " + error, args));
			flush();
		} catch (IOException e) {
			// DO NOTHING
		}
	}
	
	public void error(String error, Object... args) {
		try {
			logWriter.write(String.format("[ERROR] " + error, args));
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

}
