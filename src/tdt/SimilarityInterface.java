package tdt;

import java.util.HashMap;

public interface SimilarityInterface {
	public void train(HashMap<String, String> parameters);
	public double getSimilarity(Story story1, Story story2);
}
