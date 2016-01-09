package tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class DBSCAN implements ClusteringInterface {
	private Vector<Story> corpus = null;
	private StoryLinkDetector storyLinkDetector = null;
	private double minSimilarity = 0.0;
	private int minPts = 0;

	public DBSCAN(Vector<Story> corpus, StoryLinkDetector storyLinkDetector) {
		this.corpus = corpus;
		this.storyLinkDetector = storyLinkDetector;
	}

	@Override
	public void train(HashMap<String, String> parameters) {
		double minSimilarity = Double.parseDouble(parameters.get("minSimilarity"));
		int minPts = Integer.parseInt(parameters.get("minPts"));
		System.out.println("Parameters: ");
		System.out.println("> minSimilarity = " + minSimilarity);
		System.out.println("> minPts = " + minPts);
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
	public ArrayList<Integer> doClustering() {

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

}
