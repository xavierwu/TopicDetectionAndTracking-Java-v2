package tdt;

import java.util.ArrayList;
import java.util.HashMap;

public interface EnsemblerInterface {
	public void train(HashMap<String, String> parameters);

	public default ArrayList<Integer> doClustering() {
		ArrayList<ArrayList<Integer>> partitions = doGeneration();
		ArrayList<Integer> resultPartition = doConsensus(partitions);
		return resultPartition;
	}

	public ArrayList<ArrayList<Integer>> doGeneration();

	public ArrayList<Integer> doConsensus(
			ArrayList<ArrayList<Integer>> partitions);
}
