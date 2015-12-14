package tdt;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Zewei Wu
 */
public class ClusteringEnsembler {
	// [partitionID][storyID]=topicID
	ArrayList<ArrayList<Integer>> partitions = null;
	Vector<Story> corpus = null;

	public ClusteringEnsembler(Vector<Story> corpus) {
		this.corpus = corpus;
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
			KMeans(corpus, numOfTopics, numOfLoops, partition);
			partitions.add(partition);
		}
	}
}
