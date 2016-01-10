package tdt;

import java.util.Vector;

public enum EnsemblerName {
	VotingEnsembler(ClusteringName.KMeans),
	EASLEnsembler(ClusteringName.KMeans);

	private ClusteringName clusteringName = null;

	private EnsemblerName(ClusteringName clusteringName) {
		this.clusteringName = clusteringName;
	}

	public EnsemblerInterface getEnsemblerInterface(Vector<Story> corpus,
			StoryLinkDetector storyLinkDetector) {
		switch (this) {
		case VotingEnsembler:
			return new VotingEnsembler(VotingEnsembler, corpus,
					storyLinkDetector);
		case EASLEnsembler:
			return new EASLEnsembler(EASLEnsembler, corpus, storyLinkDetector);
		default:
			return null;
		}
	}

	public ClusteringName getClusteringName() {
		return this.clusteringName;
	}
}
