package tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;
import java.util.Map.Entry;

public class KMeans implements ClusteringInterface {
	private Vector<Story> corpus = null;
	private StoryLinkDetector storyLinkDetector = null;
	private int numOfTopics = 0;
	private int numOfLoops = 0;

	KMeans(Vector<Story> corpus, StoryLinkDetector storyLinkDetector, HashMap<String, String> parameters) {
		this.corpus = corpus;
		this.storyLinkDetector = storyLinkDetector;
		this.numOfTopics = Integer.parseInt(parameters.get("numOfTopics"));
		this.numOfLoops = Integer.parseInt(parameters.get("numOfLoops"));
	}

	public ArrayList<Integer> doClustering() {
		Vector<Story> centroids = new Vector<Story>();
		Story tmp = null;
		HashMap<Integer, Double> tfidf = null;
		ArrayList<Integer> partition = new ArrayList<Integer>();

		System.out.println("=== KMeans start");

		/* Initialization */
		Random r = new Random();

		HashSet<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < numOfTopics; i++) {
			tmp = new Story();
			tmp.setTopicID(i);
			int randInt = r.nextInt(corpus.size());
			while (set.contains(randInt))
				randInt = r.nextInt(corpus.size());
			set.add(randInt);
			if (storyLinkDetector.isUsingLDA() || storyLinkDetector.isUsingPLSA())
				tmp.setProbOfTopics(corpus.get(randInt).getProbOfTopics());
			else
				tmp.setTfidf(corpus.get(randInt).getTfidf());

			centroids.addElement(tmp);
		}

		for (int loopCnt = 0; loopCnt < numOfLoops; ++loopCnt) {
			System.out.println(loopCnt + "/" + numOfLoops);

			double[][] totalProbOfTopics = null;
			if (storyLinkDetector.isUsingLDA() || storyLinkDetector.isUsingPLSA())
				totalProbOfTopics = new double[centroids.size()][numOfTopics];

			/* Assignment step. */
			for (Story curStory : corpus) {
				double maxSimilarity = 0;
				for (Story curCentroid : centroids) {
					// System.out.print("storyID:" + curCentroid.getStoryID() +
					// " topicID:" + curCentroid.getTopicID());
					double similarity = storyLinkDetector.getSimilarity(curStory, curCentroid);
					if (similarity > maxSimilarity) {
						maxSimilarity = similarity;
						curStory.setTopicID(curCentroid.getTopicID());
					}
				}
			}

			/* Update step. */
			centroids.clear();
			for (int curTopic = 0; curTopic < numOfTopics; curTopic++) {
				tmp = new Story();
				tmp.setTopicID(curTopic);
				tfidf = new HashMap<Integer, Double>();
				int cnt = 0;
				for (Story story : corpus) {
					if (story.getTopicID() == curTopic) {
						cnt++;

						for (Entry<Integer, Double> entry : story.getTfidf().entrySet()) {
							int wordID = entry.getKey();
							double tmpTfidf = entry.getValue();
							if (tfidf.containsKey(wordID)) {
								tfidf.put(wordID, tfidf.get(wordID) + tmpTfidf);
							} else {
								tfidf.put(wordID, tmpTfidf);
							}
						}
						if (storyLinkDetector.isUsingLDA() || storyLinkDetector.isUsingPLSA())
							for (int topicIndex = 0; topicIndex < numOfTopics; ++topicIndex) {
								totalProbOfTopics[curTopic][topicIndex] += story.getProbOfTopics().get(topicIndex)
										.doubleValue();
							}
					}
				}

				for (int wordID : tfidf.keySet()) {
					tfidf.put(wordID, tfidf.get(wordID) / cnt);
				}

				ArrayList<Double> probOfTopics = null;
				if (storyLinkDetector.isUsingLDA() || storyLinkDetector.isUsingPLSA()) {
					probOfTopics = new ArrayList<Double>();
					for (int i = 0; i < totalProbOfTopics[curTopic].length; ++i) {
						probOfTopics.add(totalProbOfTopics[curTopic][i] / cnt);
					}
					tmp.setProbOfTopics(probOfTopics);
				}

				tmp.setTfidf(tfidf);
				centroids.addElement(tmp);
			}
		}
		System.out.println("=== KMeans end");
		for (int i = 0; i < corpus.size(); ++i)
			partition.add(corpus.get(i).getTopicID());
		return partition;

	}
}
