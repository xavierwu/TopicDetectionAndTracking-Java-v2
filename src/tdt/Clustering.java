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
	Vector<Story> corpus = null;
	StoryLinkDetector storyLinkDetector = null;

	public Clustering(Vector<Story> corpus, StoryLinkDetector storyLinkDetector) {
		this.corpus = corpus;
		this.storyLinkDetector = storyLinkDetector;
	}

	public ArrayList<Integer> doClustering(MethodName methodName, HashMap<String, String> parameters) {
		ArrayList<Integer> partition = null;
		switch (methodName) {
		case TFIDF_KMeans:
		case LDA_KMeans:
		case pLSA_KMeans:
		case TFIDF_VotingKMeans:
		case LDA_VotingKMeans:
		case pLSA_VotingKMeans:
		case TFIDF_EA_SL:
		case LDA_EA_SL:
		case pLSA_EA_SL:
			KMeans kmeans = new KMeans(corpus, storyLinkDetector, parameters);
			partition = kmeans.doClustering();
			break;
		case TFIDF_DBSCAN:
		case LDA_DBSCAN:
		case pLSA_DBSCAN:
			DBSCAN dbscan = new DBSCAN(corpus, storyLinkDetector, parameters);
			partition = dbscan.doClustering();
			break;
		case TFIDF_AggDetection:
		case LDA_AggDetection:
		case pLSA_AggDetection:
			Aggregation aggregation = new Aggregation(corpus, storyLinkDetector, parameters);
			partition = aggregation.doClustering();
			break;
		default:
			break;
		}
		storyLinkDetector.disableLDA();
		storyLinkDetector.disablePlsa();
		return partition;
	}

}
