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
	MethodName methodName = null;
	HashMap<String, String> parameters = null;

	public ClusteringEnsembler(Vector<Story> corpus, StoryLinkDetector storyLinkDetector) {
		this.corpus = corpus;
		this.storyLinkDetector = storyLinkDetector;
		this.partitions = new ArrayList<ArrayList<Integer>>();
	}

	public ArrayList<Integer> doClustering(MethodName methodName, HashMap<String, String> parameters) {
		this.methodName = methodName;
		this.parameters = parameters;
		this.doGeneration();
		return this.doConsensus();
	}

	/**
	 * Set this.partitions
	 * 
	 * @param methodName
	 * @param parameters
	 * @return this.partitions
	 */
	private ArrayList<ArrayList<Integer>> doGeneration() {
		partitions.clear();
		ArrayList<Integer> partition = null;
		Clustering clustering = new Clustering(corpus, storyLinkDetector);
		int numOfPartitions = Integer.parseInt(parameters.get("numOfPartitions"));
		for (int curPartition = 0; curPartition < numOfPartitions; ++curPartition) {
			partition = clustering.doClustering(methodName, parameters);
			partitions.add(partition);
		}
		return partitions;
	}

	/**
	 * set this.corpus[].topicID
	 * 
	 * @param methodName
	 */
	private ArrayList<Integer> doConsensus() {
		if (partitions.isEmpty())
			return null;

		ArrayList<Integer> resultPartition = null;
		switch (methodName) {
		case TFIDF_VotingKMeans:
		case LDA_VotingKMeans:
		case pLSA_VotingKMeans:
			resultPartition = votingKMeans();
			break;
		default:
			return null;
		}
		return resultPartition;
	}

	private ArrayList<Integer> votingKMeans() {
		ArrayList<Integer> resultPartition = new ArrayList<Integer>();
		ArrayList<Integer> topicCount = new ArrayList<Integer>();
		int numOfTopics = Integer.parseInt(this.parameters.get("numOfTopics"));
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

	private ArrayList<Integer> do_EA_SL() {
		// TODO EA-SL
		return null;
	}

	/**
	 * @deprecated
	 * @param methodName
	 * @param numOfPartitions
	 * @param numOfTopics
	 * @param numOfLoops
	 * @return
	 */
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

	public static double[][] generateCAMatrix(ArrayList<ArrayList<Integer>> partitions) {
		double[][] CAMatrix = new double[partitions.get(0).size()][partitions.get(0).size()];
		for (int i = 0; i < partitions.get(0).size(); ++i)
			CAMatrix[i][i] = 0;
		for (ArrayList<Integer> partition : partitions) {
			for (int i = 0; i < partition.size(); ++i)
				for (int j = i + 1; j < partition.size(); ++j) {
					CAMatrix[i][j] += (partition.get(i) == partition.get(j) ? 1 : 0);
					CAMatrix[j][i] = CAMatrix[i][j];
				}
		}
		for (int i = 0; i < partitions.get(0).size(); ++i)
			for (int j = i + 1; j < partitions.get(0).size(); ++j)
				CAMatrix[i][j] /= partitions.size();
		return CAMatrix;
	}

}