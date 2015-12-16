/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * This class is used to calculate the similarity between two stories. After
 * creating an instance, use the doStoryLinkDetection() firstly to detect the
 * links among stories of the corpus, so that you could use the other methods
 * normally, e.g., getCosineSimilarity().
 * 
 * @author Zewei Wu
 */
public class StoryLinkDetector {
	private Plsa plsa = null;
	private boolean isPlsaEnabled = false;
	private boolean isPlsaTrained = false;
	private LDA lda = null;
	private boolean isLDAEnabled = false;
	private boolean isLDATrained = false;

	public StoryLinkDetector() {
	}

	public void enablePlsa(Vector<Story> corpus, Glossary glossary) {
		this.plsa = new Plsa(corpus, glossary);
		this.isPlsaEnabled = true;
	}

	public void trainPlsa(int plsaNumOfTopics, int plsaMaxIter) {
		if (isPlsaEnabled) {
			plsa.train(plsaNumOfTopics, plsaMaxIter);
			this.isPlsaTrained = true;
		}
	}
	
	public void disablePlsa() {
		this.isPlsaEnabled = false;
	}

	public void enableLDA(Vector<Story> corpus, Glossary glossary) {
		this.lda = new LDA(corpus, glossary);
		this.isLDAEnabled = true;
	}

	public void trainLDA(int ldaNumOfTopics, int ldaNumOfIterations, double ldaLAMBDA, double ldaALPHA, double ldaBETA) {
		if (isLDAEnabled) {
			lda.train(ldaNumOfTopics, ldaNumOfIterations, ldaLAMBDA, ldaALPHA, ldaBETA);
			this.isLDATrained = true;
		}
	}
	
	public void disableLDA(Vector<Story> corpus) {
		this.isLDAEnabled = false;
		for (Story story: corpus) {
			story.clearProbOfTopics();
		}
 	}

	public double getSimilarity(Story story1, Story story2) {
		return isLDATrained ? lda.getSimilarity(story1, story2) : getCosineSimilarity(story1, story2);
		// return isPlsaTrained ? plsa.getSimilarity(story1, story2) :
		// getCosineSimilarity(story1, story2);
	}

	/**
	 * @param story1
	 * @param story2
	 * @return the cosine similarity between two stories, using the tf-idf
	 *         vectors in them.
	 */
	public static double getCosineSimilarity(Story story1, Story story2) {
		double similarity = 0.0;
		double innerProduct = 0.0;
		double squareSum1 = 0.0;
		double squareSum2 = 0.0;

		HashMap<Integer, Double> tfidf1 = story1.getTfidf();
		HashMap<Integer, Double> tfidf2 = story2.getTfidf();

		for (Entry<Integer, Double> entry : tfidf1.entrySet()) {
			int key = entry.getKey();
			double value = entry.getValue();
			if (tfidf2.containsKey(key))
				innerProduct += value * tfidf2.get(key);
			squareSum1 += value * value;
		}
		for (Entry<Integer, Double> entry : tfidf2.entrySet())
			squareSum2 += entry.getValue() * entry.getValue();

		if (Double.compare(innerProduct, 0.0) == 0)
			return 0.0;
		double tmp1 = Math.sqrt(squareSum1 * squareSum2);
		similarity = innerProduct / tmp1;

		return similarity;
	}

	/**
	 * Preparing for the similarity calculation, e.g., calculating tfidf's.
	 * 
	 * @deprecated
	 * @param corpus
	 *            The 'tfidf' member of the corpus would be set here.
	 * @param wordIDToStoryIndices
	 *            Used to help calculate the idf of 'tfidf'.
	 */
	public static void doStoryLinkDetection(Vector<Story> corpus,
			HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices, String tfidfFile, boolean isToLoadTfidf,
			int methodID) {
		prepareTFIDF(corpus, wordIDToStoryIndices, tfidfFile, isToLoadTfidf);
	}

	/**
	 * Preparing for the similarity calculation, e.g., calculating tfidf's.
	 * 
	 * @deprecated
	 * @param corpus
	 *            The 'tfidf' member of the corpus would be set here.
	 * @param wordIDToStoryIndices
	 *            Used to help calculate the idf of 'tfidf'.
	 */
	public static void doStoryLinkDetection(Vector<Story> corpus,
			HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices, String tfidfFile, boolean isToLoadTfidf) {
		prepareTFIDF(corpus, wordIDToStoryIndices, tfidfFile, isToLoadTfidf);
	}

	// ---------- PRIVATE -------------------------------------------------
	/**
	 * Calculating tfidf's of stories in corpus
	 * 
	 * @deprecated
	 * @param corpus
	 * @param wordIDToStoryIndices
	 * @throws IOException
	 */
	private static void prepareTFIDF(Vector<Story> corpus, HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices,
			String tfidfFile, boolean isToLoadTfidf) {
		if (isToLoadTfidf) { // load tfidf from tfidfFile
			loadTFIDF(corpus, tfidfFile);
		} else { // save tfidf to tfidfFile
			setTFIDFOfCorpus(corpus, wordIDToStoryIndices);
			saveTFIDF(corpus, tfidfFile);
		}
	}

	/**
	 * Set 'tfidf' for all stories in corpus.
	 * 
	 * @deprecated
	 * @param corpus
	 * @param storiesIndexWithCertainWord
	 */
	private static void setTFIDFOfCorpus(Vector<Story> corpus,
			HashMap<Integer, HashSet<Integer>> storiesIndexWithCertainWord) {
		System.out.println("Calculating tfidf......");
		for (int count = 0; count < corpus.size(); ++count) {
			if (count % 100 == 0)
				System.out.println(count + " / " + corpus.size());
			corpus.get(count).setTfidfBasedOnCorpus(corpus, storiesIndexWithCertainWord);
		}
		System.out.println("Done.");

	}

	/**
	 * Save the tfidf's of corpus to tfidfFile
	 * 
	 * @deprecated
	 * @param corpus
	 * @param tfidfFile
	 */
	private static void saveTFIDF(Vector<Story> corpus, String tfidfFile) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(tfidfFile);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			for (Story curStory : corpus) {
				HashMap<Integer, Double> tfidf = curStory.getTfidf();
				for (Entry<Integer, Double> pair : tfidf.entrySet()) {
					osw.append(pair.getKey() + ":" + pair.getValue() + " ");
				}
				osw.append("\n");
			}
			osw.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Load the tfidf's of corpus from tfidfFile
	 * 
	 * @deprecated
	 * @param corpus
	 * @param tfidfFile
	 * @throws IOException
	 */
	private static void loadTFIDF(Vector<Story> corpus, String tfidfFile) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(tfidfFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			HashMap<Integer, Double> tfidf;
			int i = 0;
			while ((line = br.readLine()) != null) {
				tfidf = new HashMap<Integer, Double>();
				String[] pairs = line.split(" ");
				for (String pair : pairs) {
					String[] i2d = pair.split(":");
					tfidf.put(Integer.parseInt(i2d[0]), Double.parseDouble(i2d[1]));
				}
				corpus.get(i).setTfidf(tfidf);
			}
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
