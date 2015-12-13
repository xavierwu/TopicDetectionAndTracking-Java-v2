/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

/**
 * This class is used to classify the corpus. Give every story an unique topic
 * ID. This is the core step of the whole project. We will try to find the best
 * algorithm for the purpose of classification.
 * 
 * @author Zhaoqi Wang, Zewei Wu
 */
class TopicDetector {
	private Vector<Story> corpus = null;
	private StoryLinkDetector storyLinkDetector = null;

	public TopicDetector(Vector<Story> corpus, Glossary glossary) {
		this.corpus = corpus;
		this.storyLinkDetector = new StoryLinkDetector();
		this.storyLinkDetector.enableLDA(corpus, glossary);
	}

	public JSONObject getMethodList() {
		JSONObject responseJSONObject = new JSONObject();
		JSONObject tmp = null;

		int methodID = 0;
		// tfidf_KMeans
		tmp = new JSONObject();
		methodID = 0;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "tfidf_KMeans");
		tmp.put("normCdet", 4.5889);
		tmp.put("PMiss", 0.7778);
		tmp.put("PFa", 0.7778);
		responseJSONObject.put(methodID, tmp);
		// lda_KMeans
		tmp = new JSONObject();
		methodID = 1;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "lda_KMeans");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);

		// tfidf_DBSCAN
		tmp = new JSONObject();
		methodID = 2;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "tfidf_DBSCAN");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);
		// lda_DBSCAN
		tmp = new JSONObject();
		methodID = 3;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "lda_DBSCAN");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);

		// tfidf_aggDetection
		tmp = new JSONObject();
		methodID = 4;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "tfidf_aggDetection");
		tmp.put("normCdet", 3.7694);
		tmp.put("PMiss", 0.6389);
		tmp.put("PFa", 0.6389);
		responseJSONObject.put(methodID, tmp);
		// lda_aggDetection
		tmp = new JSONObject();
		methodID = 5;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "lda_aggDetection");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);

		// tfidf_votingKMeans
		tmp = new JSONObject();
		methodID = 6;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "tfidf_votingKMeans");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);

		responseJSONObject.put("algorithmCount", methodID + 1);
		return responseJSONObject;
	}

	public JSONObject prepareTopicDetection(int methodID) {
		JSONObject responseJSONObject = new JSONObject();
		JSONObject tmp = null;
		int numOfParameters = 0;
		switch (methodID) {
		case 0: // tfidf_KMeans
			numOfParameters = 2;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfTopics");
			tmp.put("value", 36);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfLoops");
			tmp.put("value", 5);
			responseJSONObject.put(1, tmp);
			break;
		case 1: // lda_KMeans
			numOfParameters = 7;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.numOfTopics");
			tmp.put("value", 36);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.numOfIterations");
			tmp.put("value", 5);
			responseJSONObject.put(1, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.lambda");
			tmp.put("value", 0.5);
			responseJSONObject.put(2, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.alpha");
			tmp.put("value", 0.5);
			responseJSONObject.put(3, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.beta");
			tmp.put("value", 0.01);
			responseJSONObject.put(4, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfTopics");
			tmp.put("value", 36);
			responseJSONObject.put(5, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfLoops");
			tmp.put("value", 5);
			responseJSONObject.put(6, tmp);
			break;
		case 2: // tfidf_DBSCAN
			numOfParameters = 2;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "minSimilarity");
			tmp.put("value", 0.98);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "minPts");
			tmp.put("value", 5);
			responseJSONObject.put(1, tmp);
			break;
		case 3: // lda_DBSCAN
			numOfParameters = 7;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.numOfTopics");
			tmp.put("value", 36);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.numOfIterations");
			tmp.put("value", 5);
			responseJSONObject.put(1, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.lambda");
			tmp.put("value", 0.5);
			responseJSONObject.put(2, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.alpha");
			tmp.put("value", 0.5);
			responseJSONObject.put(3, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.beta");
			tmp.put("value", 0.01);
			responseJSONObject.put(4, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "minSimilarity");
			tmp.put("value", 0.98);
			responseJSONObject.put(5, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "minPts");
			tmp.put("value", 5);
			responseJSONObject.put(6, tmp);
			break;
		case 4: // tfidf_aggDetection
			numOfParameters = 1;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "threshold");
			tmp.put("value", 0.144);
			responseJSONObject.put(0, tmp);
			break;
		case 5: // lda_aggDetection
			numOfParameters = 6;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.numOfTopics");
			tmp.put("value", 36);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.numOfIterations");
			tmp.put("value", 5);
			responseJSONObject.put(1, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.lambda");
			tmp.put("value", 0.5);
			responseJSONObject.put(2, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.alpha");
			tmp.put("value", 0.5);
			responseJSONObject.put(3, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.beta");
			tmp.put("value", 0.01);
			responseJSONObject.put(4, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "threshold");
			tmp.put("value", 0.144);
			responseJSONObject.put(5, tmp);
			break;
		case 6: // tfidf_votingKMeans
			numOfParameters = 3;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfPartitions");
			tmp.put("value", 5);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfTopics");
			tmp.put("value", 36);
			responseJSONObject.put(1, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfLoops");
			tmp.put("value", 5);
			responseJSONObject.put(2, tmp);
			break;
		default:
			break;
		}
		return responseJSONObject;
	}

	public int doTopicDetection(HttpServletRequest request) {
		int numOfTopics = 0;

		int methodID = Integer.parseInt(request.getParameter("methodID"));
		System.out.println("methodID = " + methodID);

		if (methodID == 1 || methodID == 3 || methodID == 5) { // lda
			int ldaNumOfTopics = Integer.parseInt(request.getParameter("lda.numOfTopics"));
			int ldaNumOfIterations = Integer.parseInt(request.getParameter("lda.numOfIterations"));
			double ldaLAMBDA = Double.parseDouble(request.getParameter("lda.lambda"));
			double ldaALPHA = Double.parseDouble(request.getParameter("lda.alpha"));
			double ldaBETA = Double.parseDouble(request.getParameter("lda.beta"));
			storyLinkDetector.trainLDA(ldaNumOfTopics, ldaNumOfIterations, ldaLAMBDA, ldaALPHA, ldaBETA);
		}

		if (methodID == 0 || methodID == 1) { // k-means
			numOfTopics = Integer.parseInt(request.getParameter("numOfTopics"));
			int numOfLoops = Integer.parseInt(request.getParameter("numOfLoops"));
			System.out.println("Parameters: ");
			System.out.println("> numOfTopics = " + numOfTopics);
			System.out.println("> numOfLoops = " + numOfLoops);
			KMeans(numOfTopics, numOfLoops);
		} else if (methodID == 2 || methodID == 3) {// DBSCAN
			double minSimilarity = Double.parseDouble(request.getParameter("minSimilarity"));
			int minPts = Integer.parseInt(request.getParameter("minPts"));
			System.out.println("Parameters: ");
			System.out.println("> minSimilarity = " + minSimilarity);
			System.out.println("> minPts = " + minPts);
			numOfTopics = DBSCAN(minSimilarity, minPts);
		} else if (methodID == 4 || methodID == 5) {// aggDetection
			double threshold = Double.parseDouble(request.getParameter("threshold"));
			System.out.println("Parameters: ");
			System.out.println("> threshold = " + threshold);
			numOfTopics = aggDetection(threshold);
		} else if (methodID == 6) { // votingKMeans
			int numOfPartitions = Integer.parseInt(request.getParameter("numOfPartitions"));
			numOfTopics = Integer.parseInt(request.getParameter("numOfTopics"));
			int numOfLoops = Integer.parseInt(request.getParameter("numOfLoops"));
			System.out.println("Parameters: ");
			System.out.println("> numOfPartitions = " + numOfPartitions);
			System.out.println("> numOfTopics = " + numOfTopics);
			System.out.println("> numOfLoops = " + numOfLoops);
			votingKMeans(numOfPartitions, numOfTopics, numOfLoops);
		}

		return numOfTopics;
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
	 */
	private void KMeans(int numOfTopics, int numOfLoops) {
		Vector<Story> centroids = new Vector<Story>();
		Story tmp = null;
		HashMap<Integer, Double> tfidf = null;
		
		System.out.println("=== KMeans start");
		/* Initialization */
		for (int i = 0; i < numOfTopics; i++) {
			tmp = new Story();
			tmp.setTfidf(corpus.get(i).getTfidf());
			tmp.setTopicID(i);
//			tmp.setStoryID(corpus.size() + i);
			tmp.setProbOfTopics(corpus.get(i).getProbOfTopics());
			centroids.addElement(tmp);
		}
		

		for (int loopCnt = 0; loopCnt < numOfLoops; ++loopCnt) {			
			System.out.println(loopCnt + "/" + numOfLoops);	
			
			double[][] totalProbOfTopics = new double[centroids.size()][numOfTopics];
			
			/* Assignment step. */
			for (Story curStory : corpus) {
				double maxSimilarity = 0;
				for (Story curCentroid : centroids) {
//					System.out.print("storyID:" + curCentroid.getStoryID() + " topicID:" + curCentroid.getTopicID());
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
						
						for (int topicIndex = 0; topicIndex < numOfTopics; ++topicIndex) {
							totalProbOfTopics[curTopic][topicIndex] 
									+= story.getProbOfTopics().get(topicIndex).doubleValue();
						}
					}
				}
				
				for (int wordID : tfidf.keySet()) {
					tfidf.put(wordID, tfidf.get(wordID) / cnt);
				}

				ArrayList<Double> probOfTopics = new ArrayList<Double>();
				for (int i = 0; i < totalProbOfTopics[curTopic].length; ++i) {
					probOfTopics.add(totalProbOfTopics[curTopic][i] / cnt);
				}
				tmp.setProbOfTopics(probOfTopics);
				
				tmp.setTfidf(tfidf);
				centroids.addElement(tmp);
			}
		}
		System.out.println("=== KMeans end");
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
	private int DBSCAN(double minSimilarity, int minPts) {
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
	private int aggDetection(double threshold) {
		// sort the stories
		Story.sort(corpus);

		// iteration
		int numOfTopics = 0;
		Story curStory = null;
		Story prevStory = null;
		double similarity = 0.0;
		double tmpMaxSimilarity = 0.0;
		for (int i = 0; i < corpus.size(); ++i) {
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
		return numOfTopics;
	}

	private void votingKMeans(int numOfPartitions, int numOfTopics, int numOfLoops) {
		// TODO Auto-generated method stub

	}
}
