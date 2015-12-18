package tdt;

import java.util.HashMap;

import net.sf.json.JSONObject;

/**
 * @author Zewei Wu
 *
 */
public enum MethodName {
	TFIDF_KMeans("TFIDF_KMeans", 5.9, 1.0, 1.0),
	LDA_KMeans("LDA_KMeans", 5.9, 1.0, 1.0),
	pLSA_KMeans("pLSA_KMeans", 5.9, 1.0, 1.0),
	TFIDF_DBSCAN("TFIDF_DBSCAN", 5.9, 1.0, 1.0),
	LDA_DBSCAN("LDA_DBSCAN", 5.9, 1.0, 1.0),
	pLSA_DBSCAN("pLSA_DBSCAN", 5.9, 1.0, 1.0),
	TFIDF_AggDetection("TFIDF_AggDetection", 5.9, 1.0, 1.0),
	LDA_AggDetection("LDA_AggDetection", 5.9, 1.0, 1.0),
	pLSA_AggDetection("pLSA_AggDetection", 5.9, 1.0, 1.0),
	TFIDF_VotingKMeans("TFIDF_VotingKMeans", 5.9, 1.0, 1.0),
	LDA_VotingKMeans("LDA_VotingKMeans", 5.9, 1.0, 1.0),
	pLSA_VotingKMeans("pLSA_VotingKMeans", 5.9, 1.0, 1.0);

	private String name;
	private double bestNormCdet;
	private double bestPMiss;
	private double bestPFa;

	private MethodName(String name, double bestNormCdet, double bestPMiss, double bestPFa) {
		this.name = name;
		this.bestNormCdet = bestNormCdet;
		this.bestPMiss = bestPMiss;
		this.bestPFa = bestPFa;
	}

	public static MethodName valueOf(int methodID) {
		return MethodName.values()[methodID];
	}

	public HashMap<String, Double> getBestParameters() {
		HashMap<String, Double> parameters = new HashMap<String, Double>();
		switch (this) { // choose similarity algorithm
		case LDA_KMeans:
		case LDA_DBSCAN:
		case LDA_AggDetection:
		case LDA_VotingKMeans:
			parameters.put("lda.numOfTopics", (double) 63);
			parameters.put("lda.numOfIterations", (double) 5);
			parameters.put("lda.lambda", 0.5);
			parameters.put("lda.alpha", 0.5);
			parameters.put("lda.beta", 0.01);
			break;
		case pLSA_KMeans:
		case pLSA_DBSCAN:
		case pLSA_AggDetection:
		case pLSA_VotingKMeans:
			parameters.put("plsa.numOfTopics", (double) 63);
			parameters.put("plsa.numOfIterations", (double) 5);
			break;
		default:
			break;
		}

		switch (this) {// choose clustering algorithm
		case TFIDF_KMeans:
		case LDA_KMeans:
		case pLSA_KMeans:
			parameters.put("numOfTopics", (double) 63);
			parameters.put("numOfLoops", (double) 5);
			break;
		case TFIDF_DBSCAN:
		case LDA_DBSCAN:
		case pLSA_DBSCAN:
			parameters.put("minSimilarity", 0.5);
			parameters.put("minPts", (double) 5);
			break;
		case TFIDF_AggDetection:
		case LDA_AggDetection:
		case pLSA_AggDetection:
			parameters.put("threshold", 0.144);
			break;
		case TFIDF_VotingKMeans:
		case LDA_VotingKMeans:
		case pLSA_VotingKMeans:
			parameters.put("numOfPartitions", (double) 63);
			parameters.put("numOfTopics", (double) 63);
			parameters.put("numOfLoops", (double) 5);
			break;
		}
		return parameters;
	}

	public String getName() {
		return this.name;
	}

	public int getMethodID() {
		return this.ordinal();
	}

	public double getBestNormCdet() {
		return this.bestNormCdet;
	}

	public double getBestPMiss() {
		return this.bestPMiss;
	}

	public double getBestPFa() {
		return this.bestPFa;
	}

}
