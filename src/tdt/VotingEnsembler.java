package tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class VotingEnsembler implements EnsemblerInterface {
	private EnsemblerName ensemblerName = null;
	private Vector<Story> corpus = null;
	private StoryLinkDetector storyLinkDetector = null;
	private HashMap<String, String> parameters = null;
	private int numOfPartitions = 0;
	private int numOfTopics = 0;
	private int numOfLoops = 0;

	public VotingEnsembler(EnsemblerName ensemblerName, Vector<Story> corpus,
			StoryLinkDetector storyLinkDetector) {
		this.ensemblerName = ensemblerName;
		this.corpus = corpus;
		this.storyLinkDetector = storyLinkDetector;
	}

	@Override
	public void train(HashMap<String, String> parameters) {
		this.parameters = parameters;
		this.numOfPartitions = Integer
				.parseInt(parameters.get("numOfPartitions"));
		this.numOfTopics = Integer.parseInt(parameters.get("numOfTopics"));
		this.numOfLoops = Integer.parseInt(parameters.get("numOfLoops"));
		System.out.println("Parameters: ");
		System.out.println("> numOfPartitions = " + numOfPartitions);
		System.out.println("> numOfTopics = " + numOfTopics);
		System.out.println("> numOfLoops = " + numOfLoops);
	}

	@Override
	public ArrayList<ArrayList<Integer>> doGeneration() {
		System.out.println("Start doGeneration of " + ensemblerName + "...");
		ArrayList<ArrayList<Integer>> partitions = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> partition = null;
		Clustering clustering = null;
		for (int curPartition = 0; curPartition < numOfPartitions; ++curPartition) {
			System.out.println("Generating partition: " + curPartition + "/"
					+ numOfPartitions);
			clustering = new Clustering(ensemblerName.getClusteringName(),
					corpus, storyLinkDetector);
			clustering.train(this.parameters);
			partition = clustering.doClustering();
			partitions.add(partition);
		}
		System.out.println("Done.");
		return partitions;
	}

	@Override
	public ArrayList<Integer> doConsensus(
			ArrayList<ArrayList<Integer>> partitions) {
		System.out.println("Start votingKMeans...");
		ArrayList<Integer> resultPartition = new ArrayList<Integer>();
		ArrayList<Integer> topicCount = new ArrayList<Integer>();
		int numOfTopics = Integer.parseInt(parameters.get("numOfTopics"));
		for (int i = 0; i < numOfTopics; ++i)
			topicCount.add(0);
		for (int tmpStoryId = 0; tmpStoryId < corpus.size(); ++tmpStoryId) {
			for (int i = 0; i < numOfTopics; ++i)
				topicCount.set(i, 0);
			for (int tmpPartitionId = 0; tmpPartitionId < partitions
					.size(); ++tmpPartitionId) {
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
		System.out.println("Done.");
		return resultPartition;
	}

}
