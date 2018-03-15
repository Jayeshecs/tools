/**
 * 
 */
package tools.scrapper.logger;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Prajapati
 *
 */
public class CompositeLogWriter extends Writer implements ILogWriter {

	private List<ILogWriter> writerList;
	
	public CompositeLogWriter(ILogWriter... writerList) {
		this.writerList = new LinkedList<>();
		for (ILogWriter writer : writerList) {
			this.writerList.add(writer);
		}
	}

	@Override
	public void info(String error, Object... args) {
		for (ILogWriter writer : writerList) {
			writer.info(error, args);
		}
	}

	@Override
	public void error(String error, Object... args) {
		for (ILogWriter writer : writerList) {
			writer.info(error, args);
		}
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		for (ILogWriter writer : writerList) {
			writer.getWriter().write(cbuf, off, len);
		}
	}

	@Override
	public void flush() throws IOException {
		for (ILogWriter writer : writerList) {
			writer.getWriter().flush();
		}
	}

	@Override
	public void close() throws IOException {
		for (ILogWriter writer : writerList) {
			writer.getWriter().close();
		}
	}

	@Override
	public Writer append(CharSequence csq) throws IOException {
		for (ILogWriter writer : writerList) {
			writer.getWriter().append(csq);
		}
		return this;
	}

	@Override
	public Writer append(CharSequence csq, int start, int end) throws IOException {
		for (ILogWriter writer : writerList) {
			writer.getWriter().append(csq, start, end);
		}
		return this;
	}

	@Override
	public Writer append(char c) throws IOException {
		for (ILogWriter writer : writerList) {
			writer.getWriter().append(c);
		}
		return this;
	}

	@Override
	public Writer getWriter() {
		return this;
	}
}
