/**
 * 
 */
package tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author Zewei Wu
 *
 */
public class Clustering {
	private boolean isTrained = false;
	private ClusteringInterface clusteringInterface = null;

	public Clustering(ClusteringName clusteringName, Vector<Story> corpus, StoryLinkDetector storyLinkDetector) {
		this.isTrained = false;
		this.clusteringInterface = clusteringName.getClusteringInterface(corpus, storyLinkDetector);
	}

	public void train(HashMap<String, String> parameters) {
		this.clusteringInterface.train(parameters);
		this.isTrained = true;
	}

	public ArrayList<Integer> doClustering() {
		ArrayList<Integer> partition = null;
		if (isTrained)
			partition = this.clusteringInterface.doClustering();
		return partition;
	}
}
