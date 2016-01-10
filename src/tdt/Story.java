/**
 * Created on: Jul 29, 2015
 */

package tdt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * @author Zewei Wu
 */
public class Story {
	/**
	 * Default value of 'topicID', meaning the story is clustered.
	 */
	private final static int DEFAULT_TOPIC_ID = -1;
	/**
	 * Default value of 'storyID', meaning the story is just a tmp story.
	 */
	private final static int DEFAULT_STORY_ID = -1;

	/**
	 * 
	 * @param corpus
	 */
	public static void sort(Vector<Story> corpus) {
		StoryComparator comparator = new StoryComparator();
		corpus.sort(comparator);
	}

	/**
	 * The position of this story, in the corpus.
	 */
	private int storyID = DEFAULT_STORY_ID;
	/**
	 * the index of each plain word, refer to the glossary
	 */
	private Vector<Integer> words = null;
	/**
	 * 
	 */
	private String source = "unknown";
	/**
	 * yyyymmdd.hhmm.XXXX
	 */
	private String timeStamp = "";
	/**
	 * Indicate the topicID of this story.
	 */
	private int topicID = DEFAULT_TOPIC_ID;
	/**
	 * Before using, make sure setWordsCount is invoked.
	 */
	private HashMap<Integer, Integer> wordsCount = null;
	/**
	 * Before using, make sure setTermFrequency() is invoked.
	 */
	private HashMap<Integer, Double> termFrequency = null;
	/**
	 * <word id, tfidf>. Before using, make sure setTFIDFBasedOnCorpus() is
	 * invoked
	 */
	private HashMap<Integer, Double> tfidf = null;
	/**
	 * Mainly used in LDA
	 */
	private ArrayList<Double> probOfTopics = null;
	/**
	 * No matter what similarity measurement is used, use this as feature
	 * vector.
	 */
	private HashMap<Integer, Double> featureVector = null;
	/**
	 * original content
	 */
	private String originalContent = "";

	/**
	 * UNSUGGESTED: Default constructor, used only for temporary story.
	 */
	public Story() {
		this.words = new Vector<Integer>();
		this.timeStamp = null;
	}

	/**
	 * @param timestamp
	 */
	public Story(String timestamp) {
		this.words = new Vector<Integer>();
		this.timeStamp = timestamp;
	}

	/**
	 * 
	 * @param source
	 * @param timestamp
	 */
	public Story(String source, String timestamp) {
		this.words = new Vector<Integer>();
		this.source = source;
		this.timeStamp = timestamp;
	}

	/**
	 * Add a word to the words
	 */
	public void addWord(int wordID) {
		this.words.add(wordID);
	}

	/**
	 * 
	 */
	public void clearProbOfTopics() {
		this.probOfTopics.clear();
	}

	/**
	 * @param wordID
	 * @return true if this story contains a certain word.
	 */
	public boolean containsWordID(int wordID) {
		if (!this.wordsCount.isEmpty()) {
			return this.wordsCount.containsKey(wordID);
		} else {
			for (int curWordID : this.words) {
				if (curWordID == wordID)
					return true;
			}
			return false;
		}
	}

	/**
	 * 
	 * @param glossary
	 * @return
	 */
	public String getContent(Glossary glossary) {
		String result = "";
		for (int wordID : this.words)
			if (glossary.containsWordID(wordID))
				result += " " + glossary.getWord(wordID);
		return result;
	}

	/**
	 * 
	 * @return originalContent
	 */
	public String getOriginalContent() {
		return this.originalContent;
	}

	/**
	 * 
	 * @return probability of every topics
	 */
	public ArrayList<Double> getProbOfTopics() {
		return probOfTopics;
	}

	/**
	 * 
	 * @return
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @return the storyID
	 */
	public int getStoryID() {
		return storyID;
	}

	/**
	 * @return the termFrequency
	 */
	public HashMap<Integer, Double> getTermFrequency() {
		return termFrequency;
	}

	/**
	 * @return the tfidf
	 */
	public HashMap<Integer, Double> getTfidf() {
		return tfidf;
	}

	/**
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * 
	 * @param glossary
	 * @return
	 */
	public String getTitle(Glossary glossary) {
		String result = "";
		for (int i = 0; i < this.words.size() && i < 15; ++i) {
			int wordID = this.words.get(i);
			if (glossary.containsWordID(wordID))
				result += " " + glossary.getWord(wordID);
		}
		return result;
	}

	/**
	 * 
	 * @param dataFilesDir
	 * @return
	 */
	public String getTitle(String dataFilesDir) {
		String result = "";
		String file = dataFilesDir + this.getTimeStamp() + "_"
				+ this.getSource();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			int counter = 0;
			while ((line = reader.readLine()) != null) {
				result += line + " ";
				counter++;
				if (counter == 15)
					break;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @return the topicID
	 */
	public int getTopicID() {
		return topicID;
	}

	/**
	 * @param index
	 * @return a word in 'words'
	 */
	public int getWord(int index) {
		return this.words.get(index);
	}

	/**
	 * @return the words
	 */
	public Vector<Integer> getWords() {
		return words;
	}

	/**
	 * Before using this function, make sure the initWordsCount() was called.
	 * 
	 * @return the wordsCount
	 */
	public HashMap<Integer, Integer> getWordsCount() {
		return wordsCount;
	}

	/**
	 * Initialize probOfTopics.
	 */
	public void initProbOfTopics() {
		this.probOfTopics = new ArrayList<Double>();
	}

	/**
	 * Initialize the termFrequency
	 */
	public void initTermFrequency() {
		if (this.wordsCount == null || this.wordsCount.isEmpty())
			this.initWordsCount();

		this.termFrequency = new HashMap<Integer, Double>();

		double length = this.words.size();
		for (Entry<Integer, Integer> pair : this.wordsCount.entrySet()) {
			this.termFrequency.put(pair.getKey(), pair.getValue() / length);
		}
	}

	/**
	 * Initialize the tfidf.
	 */
	public void initTfidf() {
		this.tfidf = new HashMap<Integer, Double>();
	}

	/**
	 * Initialize the wordsCount
	 */
	public void initWordsCount() {
		this.wordsCount = new HashMap<Integer, Integer>();
		for (int curWordID : this.words) {
			if (this.wordsCount.containsKey(curWordID)) {
				this.wordsCount.put(curWordID,
						this.wordsCount.get(curWordID) + 1);
			} else {
				this.wordsCount.put(curWordID, 1);
			}
		}
	}

	/**
	 * @return true if the story is already clustered.
	 */
	public boolean isClustered() {
		return this.topicID != Story.DEFAULT_TOPIC_ID;
	}

	/**
	 * 
	 * @param originalContent
	 */
	public void setOriginalContent(String originalContent) {
		this.originalContent = originalContent;
	}

	/**
	 * 
	 * @param probOfTopics
	 */
	public void setProbOfTopics(ArrayList<Double> probOfTopics) {
		this.probOfTopics = probOfTopics;
	}

	/**
	 * 
	 * @param source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @param storyID
	 *            the storyID to set
	 */
	public void setStoryID(int storyID) {
		this.storyID = storyID;
	}

	/**
	 * WARNING: USE initTermFrequency() INSTEAD.
	 * 
	 * @param termFrequency
	 *            the termFrequency to set
	 */
	public void setTermFrequency(HashMap<Integer, Double> termFrequency) {
		this.termFrequency = termFrequency;
	}

	/**
	 * @param tfidf
	 *            the tfidf to set
	 */
	public void setTfidf(HashMap<Integer, Double> tfidf) {
		this.tfidf = tfidf;
	}

	/**
	 * calculate tfidf of this story, based on the corpus.
	 * 
	 * @param corpus
	 * @param wordIDToStoryIndices
	 */
	public void setTfidfBasedOnCorpus(Vector<Story> corpus,
			HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices) {
		if (this.termFrequency == null || this.termFrequency.isEmpty())
			this.initTermFrequency();

		this.tfidf = new HashMap<Integer, Double>();

		for (Entry<Integer, Double> pair : this.termFrequency.entrySet()) {
			int wordID = pair.getKey();
			double tf = pair.getValue();
			if (wordIDToStoryIndices.containsKey(wordID)) {
				double idf = 0.0;
				double storiesWithWord = 0.0;
				storiesWithWord = wordIDToStoryIndices.get(wordID).size();
				idf = Math.log(corpus.size() / storiesWithWord);
				this.tfidf.put(wordID, tf * idf);
			}
		}
	}

	/**
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @param topicID
	 *            the topicID to set
	 */
	public void setTopicID(int topicID) {
		this.topicID = topicID;
	}

	/**
	 * USE initWordsCount() INSTEAD. Set the wordsCount directly.
	 * 
	 * @param wordsCount
	 *            the wordsCount to set
	 */
	public void setWordsCount(HashMap<Integer, Integer> wordsCount) {
		this.wordsCount = wordsCount;
	}

	/**
	 * return time stamp and words
	 */
	public String toString(Glossary glossary) {
		String result = "";
		result += this.timeStamp;
		for (int wordID : this.words) {
			if (glossary.containsWordID(wordID)) {
				result += " " + glossary.getWord(wordID);
			}
		}
		return result;
	}
}
