package tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class EASLEnsembler implements EnsemblerInterface {
	private EnsemblerName ensemblerName = null;
	private HashMap<String, String> parameters = null;
	private Vector<Story> corpus = null;
	private StoryLinkDetector storyLinkDetector = null;
	private int numOfPartitions = 0;
	private int numOfTopics = 0;
	private int numOfLoops = 0;
	private double threshold = 0.0;

	public EASLEnsembler(EnsemblerName ensemblerName, Vector<Story> corpus,
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
		this.threshold = Double.parseDouble(parameters.get("threshold"));
		System.out.println("Parameters: ");
		System.out.println("> numOfPartitions = " + this.numOfPartitions);
		System.out.println("> numOfTopics = " + this.numOfTopics);
		System.out.println("> numOfLoops = " + this.numOfLoops);
		System.out.println("> threshold = " + this.threshold);
	}

	@Override
	public ArrayList<ArrayList<Integer>> doGeneration() {
		System.out.println("Start doGeneration of " + ensemblerName + "...");
		ArrayList<ArrayList<Integer>> partitions = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> partition = null;
		Clustering clustering = null;
		int numOfPartitions = Integer
				.parseInt(parameters.get("numOfPartitions"));
		for (int curPartition = 0; curPartition < numOfPartitions; ++curPartition) {
			System.out.println("Generating partition: " + curPartition + "/"
					+ numOfPartitions);
			clustering = new Clustering(ensemblerName.getClusteringName(),
					corpus, storyLinkDetector);
			clustering.train(parameters);
			partition = clustering.doClustering();
			partitions.add(partition);
		}
		System.out.println("Done.");
		return partitions;
	}

	@Override
	public ArrayList<Integer> doConsensus(
			ArrayList<ArrayList<Integer>> partitions) {

		System.out.println("Start EA-SL...");
		ArrayList<Integer> resultPartition = new ArrayList<Integer>();
		double[][] matrix = generateCAMatrix(partitions);
		double threshold = Double.parseDouble(this.parameters.get("threshold"));
		boolean[][] isConnected = new boolean[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; ++i) {
			isConnected[i][i] = false;
			for (int j = i + 1; j < matrix[i].length; ++j) {
				isConnected[i][j] = isConnected[j][i] = (matrix[i][j] >= threshold);
			}
		}
		for (int i = 0; i < matrix.length; ++i) {
			resultPartition.add(-1);
		}
		int numOfTopics = 0;
		for (int i = 0; i < matrix.length; ++i) {
			if (resultPartition.get(i) == -1) {
				resultPartition.set(i, numOfTopics);
				for (int j = i + 1; j < matrix.length; ++j) {
					if (isConnected[i][j]) {
						resultPartition.set(j, numOfTopics);
					}
				}
				++numOfTopics;
			}
		}
		System.out.println("Done.");
		return resultPartition;

	}

	private static double[][] generateCAMatrix(
			ArrayList<ArrayList<Integer>> partitions) {
		double[][] CAMatrix = new double[partitions.get(0).size()][partitions
				.get(0).size()];
		for (int i = 0; i < partitions.get(0).size(); ++i)
			CAMatrix[i][i] = 0;
		for (ArrayList<Integer> partition : partitions) {
			for (int i = 0; i < partition.size(); ++i)
				for (int j = i + 1; j < partition.size(); ++j) {
					CAMatrix[i][j] += (partition.get(i) == partition.get(j) ? 1
							: 0);
					CAMatrix[j][i] = CAMatrix[i][j];
				}
		}
		for (int i = 0; i < partitions.get(0).size(); ++i)
			for (int j = i + 1; j < partitions.get(0).size(); ++j)
				CAMatrix[i][j] /= partitions.size();
		return CAMatrix;
	}

}
