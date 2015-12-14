package tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author Zewei Wu
 */
public class ClusteringEnsembler {
	// [partitionID][storyID]=topicID
	ArrayList<ArrayList<Integer>> partitions = null;
	Vector<Story> corpus = null;
	StoryLinkDetector storyLinkDetector;

	public ClusteringEnsembler(Vector<Story> corpus, StoryLinkDetector storyLinkDetector) {
		this.corpus = corpus;
		this.storyLinkDetector = storyLinkDetector;
		this.partitions = new ArrayList<ArrayList<Integer>>();
	}

	public ArrayList<Integer> doClustering(String methodName, HashMap<String, Double> parameters) {
		// TODO Clustering.doClustering()
		return null;
	}

	/**
	 * Set this.partitions;
	 */
	public void doGeneration(String methodName) {
	}

	/**
	 * set this.corpus[].topicID
	 * 
	 * @param methodName
	 */
	public void doConsensus(String methodName) {
		if ("EA-SL".equalsIgnoreCase(methodName)) {
			// TODO EA-SL
		} else {

		}
	}

	public void votingKMeans(int numOfPartitions, int numOfTopics, int numOfLoops) {
		ArrayList<Integer> partition = null;
		for (int curPartition = 0; curPartition < numOfPartitions; ++curPartition) {
			partition = new ArrayList<Integer>();
//			KMeans(corpus, numOfTopics, numOfLoops, partition);
			partitions.add(partition);
		}
	}
}
