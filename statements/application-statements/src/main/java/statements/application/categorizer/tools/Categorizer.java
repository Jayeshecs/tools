/**
 * 
 */
package statements.application.categorizer.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.primitives.Pair;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jayeshecs
 *
 * @param <R> Record or Data
 * @param <L> Label
 */
@Slf4j
public class Categorizer<R, L> {
	
	/**
	 * @author jayeshecs
	 *
	 * @param <R> Record or data
	 * @param <L> Label
	 */
	public interface ICategorizerDataHandler<R, L> {
		
		/**
		 * @param <R> data
		 * @return <L> Label for given <R> data if present else return null
		 */
		L getLabel(R data);
		
		/**
		 * Label key to Label data
		 * 
		 * @param key
		 * @return <L>
		 */
		L toLabel(String key);
		
		/**
		 * @param <R> data
		 * @return sentence or paragraph or sequence of words for given <R> data
		 */
		String getParagraph(R data);
	}
	
	private ICategorizerDataHandler<R, L> handler;
	private TokenPreProcess tokenPreProcessor;
	private DefaultTokenizerFactory tokenizerFactory;

	
	/**
	 * @param labelProvider
	 * @param tokenPreProcessor
	 */
	public Categorizer(ICategorizerDataHandler<R, L> labelProvider, TokenPreProcess tokenPreProcessor) {
		this.handler = labelProvider;
		this.tokenPreProcessor = tokenPreProcessor;
		
		// initialize tokenizer factory
		tokenizerFactory = new DefaultTokenizerFactory();
		tokenizerFactory.setTokenPreProcessor(tokenPreProcessor);
	}
	
	public ParagraphVectors train(Collection<R> listAllCategorized) {

		ParagraphVectors paragraphVectors = new ParagraphVectors.Builder()
	              .learningRate(0.025)
	              .minLearningRate(0.001)
	              .batchSize(100)
	              .epochs(3)
	              .iterations(5)
	              //.layerSize(100)
	              .iterate(new CategoryAwareParagraphIterator<R, L>(this.handler, Collections.synchronizedCollection(listAllCategorized)))
	              .trainWordVectors(true)
	              .minWordFrequency(2)
	              .tokenizerFactory(tokenizerFactory)
	              .build();
		
		// start model training
		paragraphVectors.fit();

		// print vocab
		printVocab(paragraphVectors);
		
		return paragraphVectors;
	}
	
	private void printVocab(ParagraphVectors paragraphVectors) {
		VocabCache<VocabWord> vocab = paragraphVectors.getVocab();
		log.info(String.format("Vocab Detail:\n"
				+ "Total words: %d\n"
				+ "Total Docs: %d\n"
				+ "Total Word Occurances: %d\n"
				+ "Total Tokens: %d\n"
				+ "Words: %s\n"
				, vocab.numWords()
				, vocab.totalNumberOfDocs()
				, vocab.totalWordOccurrences()
				, vocab.tokens().size()
				, vocab.words()));
		for (VocabWord vocabWord : vocab.vocabWords()) {
			log.info(String.format("Is Label: %s, Word: %s (%s)"
					, vocabWord.isLabel()
					, vocabWord.getLabel()
					, vocabWord));
		}
	}
	
	/**
	 * @param paragraphVectors
	 * @param listUncategorized
	 */
	public Map<R, L> categorize(ParagraphVectors paragraphVectors, Collection<R> listUncategorized) {

		MeansBuilder meansBuilder = new MeansBuilder(
				(InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable(),
				tokenizerFactory);
		
		List<String> labels = paragraphVectors.getLabelsSource().getLabels();
		
		LabelSeeker seeker = new LabelSeeker(new ArrayList<String>(labels),
				(InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable());
		
		long count = 0;
		Map<R, L> result = new HashMap<>();
		for (R data : listUncategorized) {
			String paragraph = handler.getParagraph(data);
			INDArray vector = meansBuilder.documentAsVector(paragraph);
			Pair<String, Double> scores = seeker.getScores(vector);
			if (scores == null) {
				continue;
			}
			if (scores.getValue().isNaN()) {
				continue;
			}
			count++;
			result.put(data, handler.toLabel(scores.getKey()));
			log.info(String.format("%s = %s (%f)", paragraph, scores.getKey(), scores.getValue()));
		}
		log.info(String.format("Total %d/%d record categorized", count, listUncategorized.size()));
		return result;
	}

	public void save(ParagraphVectors model, File file) {
		try {
			WordVectorSerializer.writeParagraphVectors(model, file);
		} catch (Exception e) {
			log.error("Exception occurred while writing model", e);
		}
	}

	public ParagraphVectors loadModel(File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		try {
			ParagraphVectors result = WordVectorSerializer.readParagraphVectors(file);

			// print vocab
			printVocab(result);

			return result;
		} catch (IOException e) {
			log.error("I/O error occurred while reading paragraph vectors", e);
		}
		return null;
	}
	
}
