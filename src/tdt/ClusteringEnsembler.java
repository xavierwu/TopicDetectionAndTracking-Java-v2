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
		int numOfPartitions = 0;
		ArrayList<Integer> resultPartition = null;
		
		if ("votingKMeans".equalsIgnoreCase(methodName)) {
			numOfPartitions = parameters.get("numOfPartitions").intValue();
			int numOfTopics = parameters.get("numOfTopics").intValue();
			int numOfLoops = parameters.get("numOfLoops").intValue();
			resultPartition = this.votingKMeans(numOfPartitions, numOfTopics, numOfLoops);
		}
		return resultPartition;
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

	public ArrayList<Integer> votingKMeans(int numOfPartitions, int numOfTopics, int numOfLoops) {
		ArrayList<Integer> partition = null;
		ArrayList<Integer> resultPartition = new ArrayList<Integer>();
		Clustering clustering = new Clustering(corpus, storyLinkDetector);
		HashMap<String, Double> parameters = new HashMap<String, Double>();
		for (int curPartition = 0; curPartition < numOfPartitions; ++curPartition) {
			partition = new ArrayList<Integer>();
			parameters.put("numOfTopics", (double)numOfTopics);
			parameters.put("numOfLoops", (double)numOfLoops);
			clustering.doClustering("KMeans", parameters);
			partitions.add(partition);
		}
		
		
		return resultPartition;
	}
}
