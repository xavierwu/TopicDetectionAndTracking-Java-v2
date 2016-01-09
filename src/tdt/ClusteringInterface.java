package tdt;

import java.util.ArrayList;
import java.util.HashMap;

public interface ClusteringInterface {
	public void train(HashMap<String, String> parameters);

	public ArrayList<Integer> doClustering();
}
