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
			System.out.println(methodName + " is called.");
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
			System.out.println("=> Start partition: " + (curPartition + 1) + "/" + numOfPartitions);
			parameters.put("numOfTopics", (double) numOfTopics);
			parameters.put("numOfLoops", (double) numOfLoops);
			partition = clustering.doClustering("KMeans", parameters);
			partitions.add(partition);
		}

		// of length numOfTopics
		ArrayList<Integer> topicCount = new ArrayList<Integer>();
		for (int i = 0; i < numOfTopics; ++i)
			topicCount.add(0);
		for (int tmpStoryId = 0; tmpStoryId < corpus.size(); ++tmpStoryId) {
			for (int i = 0; i < numOfTopics; ++i)
				topicCount.set(i, 0);
			for (int tmpPartitionId = 0; tmpPartitionId < partitions.size(); ++tmpPartitionId) {
				int tmpTopic = partitions.get(tmpPartitionId).get(tmpStoryId);
				topicCount.set(tmpTopic, topicCount.get(tmpTopic) + 1);
			}
			int maxTopic = 0, max = 0;
			for (int i = 0; i < numOfTopics; ++i)
				if (max < topicCount.get(i)) {
					max = topicCount.get(i);
					maxTopic = i;
				}
			resultPartition.add(maxTopic);
		}

		return resultPartition;
	}
}
