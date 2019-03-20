/**
 * 
 */
package statements.application.categorizer.tools;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.deeplearning4j.text.documentiterator.LabelAwareDocumentIterator;

import domain.statements.dom.impl.txn.Transaction;
import lombok.NonNull;
import statements.application.categorizer.tools.Categorizer.ICategorizerDataHandler;

/**
 * @author jayeshecs
 *
 */
public class CategoryAwareParagraphIterator<R, L> implements LabelAwareDocumentIterator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Pattern wordSeparatorPattern = Pattern.compile("[\\.:@$#,_[-]\"\'\\(\\)\\[\\]|/?!;]+");
	
	private ICategorizerDataHandler<R, L> handler;

	private Collection<R> records;

	private Iterator<R> iterator;

	private R currentRecord;
	
	/**
	 * @param handler
	 */
	public CategoryAwareParagraphIterator(ICategorizerDataHandler<R, L> handler, Collection<R> records) {
		this.handler = handler;
		this.records = records;
	}

	private void ensureIterator() {
		if (iterator != null) {
			return ;
		}
		synchronized (records) {
			if (iterator != null) {
				return ;
			}
			this.iterator = records.iterator();
		}
	}

	/* (non-Javadoc)
	 * @see org.deeplearning4j.text.documentiterator.DocumentIterator#nextDocument()
	 */
	@Override
	public InputStream nextDocument() {
		ensureIterator();
		currentRecord = iterator.next();
		@NonNull
		String narration = handler.getParagraph(currentRecord);
		narration = wordSeparatorPattern.matcher(narration).replaceAll(" ");
		String[] words = narration.split(" ");
		for(int i = 0; i < words.length; ++i) {
			if (words[i].length() < 3) {
				words[i] = "";
			}
		}
		narration = String.join(" ", words);
		return new ByteArrayInputStream(narration.getBytes());
	}

	/* (non-Javadoc)
	 * @see org.deeplearning4j.text.documentiterator.DocumentIterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		ensureIterator();
		return iterator.hasNext();
	}

	/* (non-Javadoc)
	 * @see org.deeplearning4j.text.documentiterator.DocumentIterator#reset()
	 */
	@Override
	public void reset() {
		iterator = null;
		ensureIterator();
	}

	/* (non-Javadoc)
	 * @see org.deeplearning4j.text.documentiterator.LabelAwareDocumentIterator#currentLabel()
	 */
	@Override
	public String currentLabel() {
		// TODO Auto-generated method stub
		return null;
	}

}
