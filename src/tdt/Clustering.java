/**
 * 
 */
package tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;
import java.util.Map.Entry;

/**
 * @author Zewei Wu
 *
 */
public class Clustering {
	Vector<Story> corpus = null;
	StoryLinkDetector storyLinkDetector = null;

	public Clustering(Vector<Story> corpus, StoryLinkDetector storyLinkDetector) {
		this.corpus = corpus;
		this.storyLinkDetector = storyLinkDetector;
	}

	public ArrayList<Integer> doClustering(MethodName methodName, HashMap<String, String> parameters) {
		ArrayList<Integer> partition = null;
		switch (methodName) {
		case TFIDF_KMeans:
		case LDA_KMeans:
		case pLSA_KMeans:
		case TFIDF_VotingKMeans:
		case LDA_VotingKMeans:
		case pLSA_VotingKMeans:
		case TFIDF_EA_SL:
		case LDA_EA_SL:
		case pLSA_EA_SL:
			int numOfTopics = Integer.parseInt(parameters.get("numOfTopics"));
			int numOfLoops = Integer.parseInt(parameters.get("numOfLoops"));
			partition = KMeans(numOfTopics, numOfLoops);
			break;
		case TFIDF_DBSCAN:
		case LDA_DBSCAN:
		case pLSA_DBSCAN:
			double minSimilarity = Double.parseDouble(parameters.get("minSimilarity"));
			int minPts = Integer.parseInt(parameters.get("minPts"));
			partition = DBSCAN(minSimilarity, minPts);
			break;
		case TFIDF_AggDetection:
		case LDA_AggDetection:
		case pLSA_AggDetection:
			double threshold = Double.parseDouble(parameters.get("threshold"));
			partition = aggDetection(threshold);
			break;
		default:
			break;
		}
		return partition;
	}

	/**
	 * @deprecated
	 * @param methodName
	 * @param parameters
	 * @return
	 */
	public ArrayList<Integer> doClustering(String methodName, HashMap<String, String> parameters) {
		ArrayList<Integer> partition = new ArrayList<Integer>();
		if ("KMeans".equalsIgnoreCase(methodName)) {
			int numOfTopics = Integer.parseInt(parameters.get("numOfTopics"));
			int numOfLoops = Integer.parseInt(parameters.get("numOfLoops"));
			partition = KMeans(numOfTopics, numOfLoops);
		} else if ("DBSCAN".equalsIgnoreCase(methodName)) {
			double minSimilarity = Double.parseDouble(parameters.get("minSimilarity"));
			int minPts = Integer.parseInt(parameters.get("minPts"));
			partition = DBSCAN(minSimilarity, minPts);
		} else if ("aggDetection".equalsIgnoreCase(methodName)) {
			double threshold = Double.parseDouble(parameters.get("threshold"));
			partition = aggDetection(threshold);
		}
		return partition;
	}

	/**
	 * K-means clustering. It aims to partition n stories into k clusters, in
	 * which each story belongs to the the cluster with nearest mean. Special
	 * Notice: the second parameter numOfTopics can't be zero.
	 * 
	 * @param simMeasure
	 *            The measurement of similarity, 0 for tfidf, 1 for lda.
	 * @param numOfTopics
	 * @param numOfLoops
	 * @return
	 */
	private ArrayList<Integer> KMeans(int numOfTopics, int numOfLoops) {
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
	 * @param simMeasure
	 *            0 for tfidf, 1 for lda
	 * @param minSimilarity
	 * @param minPts
	 */
	private ArrayList<Integer> DBSCAN(double minSimilarity, int minPts) {
		boolean[] isVisited = new boolean[corpus.size() + 1];
		int topicID = 0;
		ArrayList<Integer> partition = new ArrayList<Integer>();

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
		for (int i = 0; i < corpus.size(); ++i)
			partition.add(corpus.get(i).getTopicID());
		return partition;
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
			double similarity = storyLinkDetector.getSimilarity(currentStory, corpus.get(i));

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
	 * @param simMeasure
	 *            0 for tfidf, 1 for lda.
	 * @param threshold
	 * @return numOfTopics
	 */
	private ArrayList<Integer> aggDetection(double threshold) {
		ArrayList<Integer> partition = new ArrayList<Integer>();
		// sort the stories
		Story.sort(corpus);

		// iteration
		int numOfTopics = 0;
		Story curStory = null;
		Story prevStory = null;
		double similarity = 0.0;
		double tmpMaxSimilarity = 0.0;
		System.out.println("=== aggDetection start");
		for (int i = 0; i < corpus.size(); ++i) {
			if (i % 1000 == 0)
				System.out.println(i + "/" + corpus.size());
			curStory = corpus.get(i);
			tmpMaxSimilarity = -1.0;
			for (int j = 0; j < i; ++j) {
				prevStory = corpus.get(j);
				similarity = storyLinkDetector.getSimilarity(curStory, prevStory);
				if (similarity >= threshold && similarity > tmpMaxSimilarity) {
					curStory.setTopicID(prevStory.getTopicID());
					tmpMaxSimilarity = similarity;
				}
			}

			if (tmpMaxSimilarity < 0) {
				curStory.setTopicID(numOfTopics);
				numOfTopics++;
			}
		}
		System.out.println("=== aggDetection end");
		for (int i = 0; i < corpus.size(); ++i)
			partition.add(corpus.get(i).getTopicID());
		return partition;
	}
}
