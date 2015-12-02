/**
 * Created on: Jul 29, 2015
 */
/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * This class is used to classify the corpus. Give every story an unique topic
 * ID. This is the core step of the whole project. We will try to find the best
 * algorithm for the purpose of classification.
 * 
 * @author Zhaoqi Wang, Zewei Wu
 */
class TopicDetector {

	/**
	 * Constructor. Nothing to be initialized for the time being.
	 */
	TopicDetector() {
	}

	/**
	 * Function to be called by other class, for the purpose of encapsulation.
	 * It uses certain methods to label these stories, make these stories have
	 * an unique topic ID. After the function being called, the whole corpus
	 * will be classified.
	 * 
	 * @deprecated
	 * @param corpus
	 * @return topic number, which is set by certain method.
	 */
	int doTopicDetection(Vector<Story> corpus, int methodID) {
		switch (methodID) {
		case 0:
			int numOfTopics = 7;
			KMeans(corpus, numOfTopics, 10);
			return numOfTopics;
		case 1:
			return DBSCAN(corpus, 0.98, 5);
		default:
			return -1;
		}
	}

	/**
	 * Function to be called by other class, for the purpose of encapsulation.
	 * It uses certain methods to label these stories, make these stories have
	 * an unique topic ID. After the function being called, the whole corpus
	 * will be classified.
	 *
	 * @deprecated
	 * @param corpus
	 * @return topic number, which is set by certain method.
	 */
	int doTopicDetection(Vector<Story> corpus) {
		/*
		 * Topic number, set to 7 for the time being. We will try to make it can
		 * be set automatically.
		 */
		int numOfTopics = 7;
		KMeans(corpus, numOfTopics, 10);
		return numOfTopics;

		// return DBSCAN(corpus, 0.98, 5);
	}

	/**
	 * K-means clustering. It aims to partition n stories into k clusters, in
	 * which each story belongs to the the cluster with nearest mean. Special
	 * Notice: the second parameter numOfTopics can't be zero.
	 * 
	 * @param corpus
	 * @param numOfTopics
	 */
	public void KMeans(Vector<Story> corpus, int numOfTopics, int numOfLoops) {
		Vector<Story> centroids = new Vector<Story>();
		Story tmp = null;
		HashMap<Integer, Double> tfidf = null;
		/* Initialization */
		for (int i = 0; i < numOfTopics; i++) {
			tmp = new Story();
			tmp.setTfidf(corpus.get(i).getTfidf());
			tmp.setTopicID(i);
			centroids.addElement(tmp);
		}

		for (int loopCnt = 0; loopCnt < numOfLoops; ++loopCnt) {
			/* Assignment step. */
			for (Story curStory : corpus) {
				double maxSimilarity = 0;
				for (Story curCentroid : centroids) {
					double similarity = StoryLinkDetector.getSimilarity(curStory, curCentroid);
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
					}
				}
				for (int wordID : tfidf.keySet())
					tfidf.put(wordID, tfidf.get(wordID) / cnt);
				tmp.setTfidf(tfidf);
				centroids.addElement(tmp);
			}
		}
	}

	/**
	 * DBSCAN clustering. DBSCAN requires two parameters: ep(eps, similarity
	 * here) and the minimum number of points required to form a dense region[a]
	 * (minPts). It starts with an arbitrary starting point that has not been
	 * visited. This point's ep-neighborhood is retrieved, and if it contains
	 * sufficiently many points, a cluster is started. Otherwise, the point is
	 * labeled as noise. Note that this point might later be found in a
	 * sufficiently sized ep-environment of a different point and hence be made
	 * part of a cluster.
	 * 
	 * If a point is found to be a dense part of a cluster, its ep-neighborhood
	 * is also part of that cluster. Hence, all points that are found within the
	 * ep-neighborhood are added, as is their own ep-neighborhood when they are
	 * also dense. This process continues until the density-connected cluster is
	 * completely found. Then, a new unvisited point is retrieved and processed,
	 * leading to the discovery of a further cluster or noise.
	 * 
	 * @param corpus
	 * @param minSimilarity
	 * @param minPts
	 */
	public int DBSCAN(Vector<Story> corpus, double minSimilarity, int minPts) {
		boolean[] isVisited = new boolean[corpus.size() + 1];
		int topicID = 0;

		for (int i = 0; i < corpus.size(); i++) {
			if (!isVisited[i]) {
				isVisited[i] = true;

				Vector<Integer> neighborPts = new Vector<Integer>();

				regionQuery(corpus, corpus.get(i), i, minSimilarity, neighborPts);

				if (neighborPts.size() < minPts) {
					corpus.get(i).setTopicID(-1);
				} else {
					expandCluster(corpus, corpus.get(i), neighborPts, topicID, minSimilarity, minPts, isVisited);
					topicID++;
				}
			}
		}
		return topicID;
	}

	/**
	 * Find the neighbors of the given story
	 * 
	 * @param corpus
	 * @param currentStory
	 * @param minSimilarity
	 * @param neighborPts
	 */
	private void regionQuery(Vector<Story> corpus, Story currentStory, int currentIndex, double minSimilarity,
			Vector<Integer> neighborPts) {
		for (int i = 0; i < corpus.size(); i++) {
			double similarity = StoryLinkDetector.getSimilarity(currentStory, corpus.get(i));

			if (similarity > minSimilarity && i != currentIndex) {
				neighborPts.add(i);
			}
		}
	}

	/**
	 * Expand the cluster.
	 * 
	 * @param corpus
	 * @param currentStory
	 * @param neighborPts
	 * @param topicID
	 * @param minSimilarity
	 * @param minPts
	 * @param isVisted
	 */
	private void expandCluster(Vector<Story> corpus, Story currentStory, Vector<Integer> neighborPts, int topicID,
			double minSimilarity, int minPts, boolean[] isVisited) {
		currentStory.setTopicID(topicID);

		for (int i = 0; i < neighborPts.size(); i++) {
			int neighborIndex = neighborPts.get(i);

			if (!isVisited[neighborIndex]) {
				isVisited[neighborIndex] = true;

				Vector<Integer> tempNeighborPts = new Vector<Integer>();

				regionQuery(corpus, corpus.get(neighborIndex), neighborIndex, minSimilarity, tempNeighborPts);

				if (tempNeighborPts.size() >= minPts) {
					for (int j = 0; j < tempNeighborPts.size(); j++) {
						if (!neighborPts.contains(tempNeighborPts.get(j))) {
							neighborPts.add(tempNeighborPts.get(j));
						}
					}
				}
			}

			if (corpus.get(neighborIndex).getTopicID() == -1) {
				corpus.get(neighborIndex).setTopicID(topicID);
			}
		}
	}

	/**
	 * Sort the stories in the corpus. From the oldest to the newest, check if
	 * current story is similar to any of the story before, assign the topicID
	 * of the most similar story to the current story.
	 * 
	 * @param corpus
	 * @param threshold
	 * @return numOfTopics
	 */
	public int aggDetection(Vector<Story> corpus, double threshold) {
		// sort the stories
		Story.sort(corpus);

		// iteration
		int numOfTopics = 0;
		Story curStory = null;
		Story tmpStory = null;
		double similarity = 0.0;
		double tmpMaxSimilarity = 0.0;
		for (int i = 0; i < corpus.size(); ++i) {
			curStory = corpus.get(i);
			tmpMaxSimilarity = -1.0;
			for (int j = 0; j < i; ++j) {
				tmpStory = corpus.get(i);
				similarity = StoryLinkDetector.getSimilarity(curStory, tmpStory);
				if (similarity >= threshold && similarity > tmpMaxSimilarity) {
					curStory.setTopicID(tmpStory.getTopicID());
					tmpMaxSimilarity = similarity;
				}
			}
			if (tmpMaxSimilarity < 0) {
				curStory.setTopicID(numOfTopics);
				numOfTopics++;
			}
		}
		return numOfTopics;
	}

	public static void main(String[] args) {
		Vector<Story> v = new Vector<Story>();
		Story tmp = null;

		tmp = new Story();
		tmp.setTimeStamp("20030401.0113.0007");
		v.addElement(tmp);
		System.out.println(tmp.getTimeStamp());

		tmp = new Story();
		tmp.setTimeStamp("20030402.0113.0007");
		v.addElement(tmp);
		System.out.println(tmp.getTimeStamp());

		tmp = new Story();
		tmp.setTimeStamp("20020401.0113.0007");
		v.addElement(tmp);
		System.out.println(tmp.getTimeStamp());

		tmp = new Story();
		tmp.setTimeStamp("20010401.0113.0007");
		v.addElement(tmp);
		System.out.println(tmp.getTimeStamp());

		tmp = new Story();
		tmp.setTimeStamp("20080401.0113.0007");
		v.addElement(tmp);
		System.out.println(tmp.getTimeStamp());

		Story.sort(v);

		System.out.println();
		for (int i = 0; i < v.size(); ++i) {
			System.out.println(v.get(i).getTimeStamp());
		}
	}
}
