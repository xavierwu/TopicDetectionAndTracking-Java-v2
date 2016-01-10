package tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class ClusteringEnsembler {
	private boolean isTrained = false;
	private EnsemblerInterface ensemblerInterface = null;

	public ClusteringEnsembler(EnsemblerName ensemblerName,
			Vector<Story> corpus, StoryLinkDetector storyLinkDetector) {
		this.isTrained = false;
		this.ensemblerInterface = ensemblerName.getEnsemblerInterface(corpus,
				storyLinkDetector);
	}

	public void train(HashMap<String, String> parameters) {
		this.ensemblerInterface.train(parameters);
		this.isTrained = true;
	}

	public ArrayList<Integer> doClustering() {
		ArrayList<Integer> partition = null;
		if (isTrained)
			partition = this.ensemblerInterface.doClustering();
		return partition;
	}
}
