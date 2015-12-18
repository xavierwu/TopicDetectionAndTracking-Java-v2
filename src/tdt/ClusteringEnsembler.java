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

	public ArrayList<Integer> doClustering(MethodName methodName, HashMap<String, String> parameters) {
		int numOfPartitions = 0;
		ArrayList<Integer> resultPartition = null;

		switch (methodName) {
		case TFIDF_VotingKMeans:
		case LDA_VotingKMeans:
		case pLSA_VotingKMeans:
			System.out.println(methodName + " is called.");
			numOfPartitions = Integer.parseInt(parameters.get("numOfPartitions"));
			int numOfTopics = Integer.parseInt(parameters.get("numOfTopics"));
			int numOfLoops = Integer.parseInt(parameters.get("numOfLoops"));
			resultPartition = this.votingKMeans(methodName, numOfPartitions, numOfTopics, numOfLoops);
			break;
		default:
			break;
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
	}

	public ArrayList<Integer> votingKMeans(MethodName methodName, int numOfPartitions, int numOfTopics,
			int numOfLoops) {
		ArrayList<Integer> partition = null;
		ArrayList<Integer> resultPartition = new ArrayList<Integer>();
		Clustering clustering = new Clustering(corpus, storyLinkDetector);
		HashMap<String, String> parameters = new HashMap<String, String>();
		for (int curPartition = 0; curPartition < numOfPartitions; ++curPartition) {
			System.out.println("=> Start partition: " + (curPartition + 1) + "/" + numOfPartitions);
			parameters.put("numOfTopics", String.valueOf(numOfTopics));
			parameters.put("numOfLoops", String.valueOf(numOfLoops));
			partition = clustering.doClustering(methodName, parameters);
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
