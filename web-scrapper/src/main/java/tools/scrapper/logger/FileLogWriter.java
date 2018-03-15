/**
 * 
 */
package tools.scrapper.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Prajapati
 *
 */
public class FileLogWriter extends ConsoleWriter {

	protected Writer logWriter;

	/**
	 * @param file
	 */
	public FileLogWriter(File file) {
		super(createFileWriter(file));
	}
	
	private static Writer createFileWriter(File file) {
		try {
			return new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			throw new RuntimeException("I/O error occurred while creating file writer for file - " + file, e);
		}
	}

}
