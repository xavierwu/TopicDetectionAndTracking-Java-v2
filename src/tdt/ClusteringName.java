package tdt;

import java.util.Vector;

public enum ClusteringName {
	KMeans, DBSCAN, Aggregation;

	public ClusteringInterface getClusteringInterface(Vector<Story> corpus, StoryLinkDetector storyLinkDetector) {
		switch (this) {
		case KMeans:
			return new KMeans(corpus, storyLinkDetector);
		case DBSCAN:
			return new DBSCAN(corpus, storyLinkDetector);
		case Aggregation:
			return new Aggregation(corpus, storyLinkDetector);
		default:
			return null;
		}
	}
}
