package tdt;

import java.util.HashMap;

/**
 * @author Zewei Wu
 *
 */
public enum MethodName {
	TFIDF_KMeans("TFIDF_KMeans", 2.19, 0.38, 0.37),
	LDA_KMeans("LDA_KMeans", 0.71, 0.51, 3.23),
	pLSA_KMeans("pLSA_KMeans", 4.86, 0.82, 0.82),
	TFIDF_DBSCAN("TFIDF_DBSCAN", 0.42, 0.31, 0.02),
	LDA_DBSCAN("LDA_DBSCAN", 5.9, 1.0, 1.0),
	pLSA_DBSCAN("pLSA_DBSCAN", 5.9, 1.0, 1.0),
	TFIDF_AggDetection("TFIDF_AggDetection", 0.28, 0.19, 0.01),
	LDA_AggDetection("LDA_AggDetection", 5.9, 1.0, 1.0),
	pLSA_AggDetection("pLSA_AggDetection", 5.9, 1.0, 1.0),
	TFIDF_VotingKMeans("TFIDF_VotingKMeans", 2.24, 0.38, 0.38),
	LDA_VotingKMeans("LDA_VotingKMeans", 5.9, 1.0, 1.0),
	pLSA_VotingKMeans("pLSA_VotingKMeans", 5.9, 1.0, 1.0),
	TFIDF_EA_SL("TFIDF_EA_SL", 5.9, 1.0, 1.0),
	LDA_EA_SL("LDA_EA_SL", 5.9, 1.0, 1.0),
	pLSA_EA_SL("pLSA_EA_SL", 5.9, 1.0, 1.0),
	Original_Subtopic("Original_Subtopic", 0.73, 0.73, 0),
	Original_Subtopic_Weight("Original_Subtopic_Weight", 0.69, 0.69, 0),
	Improved_Subtopic("Improved_Subtopic", 0.58, 0.35, 0.05),
	Improved_Subtopic_Weight("Improved_Subtopic_Weight", 0.61, 0.30, 0.06),
	Improved_Subtopic_Weight_Agg("Improved_Subtopic_Weight_Agg", 0.41, 0.30, 0.02),
	Improved_Agg("Improved_Agg", 0.19, 0.19, 0);

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

	public HashMap<String, String> getBestParameters() {
		HashMap<String, String> parameters = new HashMap<String, String>();
		switch (this) { // choose similarity algorithm
		case LDA_KMeans:
		case LDA_DBSCAN:
		case LDA_AggDetection:
		case LDA_VotingKMeans:
		case LDA_EA_SL:
			parameters.put("lda.numOfTopics", String.valueOf(63));
			parameters.put("lda.numOfIterations", String.valueOf(5));
			parameters.put("lda.lambda", String.valueOf(0.5));
			parameters.put("lda.alpha", String.valueOf(0.5));
			parameters.put("lda.beta", String.valueOf(0.01));
			break;
		case pLSA_KMeans:
		case pLSA_DBSCAN:
		case pLSA_AggDetection:
		case pLSA_VotingKMeans:
		case pLSA_EA_SL:
			parameters.put("plsa.numOfTopics", String.valueOf(63));
			parameters.put("plsa.numOfIterations", String.valueOf(5));
			break;
		default:
			break;
		}

		switch (this) {// choose clustering algorithm
		case TFIDF_KMeans:
		case LDA_KMeans:
		case pLSA_KMeans:
			parameters.put("numOfTopics", String.valueOf(63));
			parameters.put("numOfLoops", String.valueOf(5));
			break;
		case TFIDF_DBSCAN:
		case LDA_DBSCAN:
		case pLSA_DBSCAN:
			parameters.put("minSimilarity", String.valueOf(0.5));
			parameters.put("minPts", String.valueOf(5));
			break;
		case TFIDF_AggDetection:
		case LDA_AggDetection:
		case pLSA_AggDetection:
			parameters.put("threshold", String.valueOf(0.144));
			break;
		case TFIDF_VotingKMeans:
		case LDA_VotingKMeans:
		case pLSA_VotingKMeans:
			parameters.put("numOfPartitions", String.valueOf(5));
			parameters.put("numOfTopics", String.valueOf(63));
			parameters.put("numOfLoops", String.valueOf(5));
			break;
		case TFIDF_EA_SL:
		case LDA_EA_SL:
		case pLSA_EA_SL:
			parameters.put("numOfPartitions", String.valueOf(5));
			parameters.put("numOfTopics", String.valueOf(63));
			parameters.put("numOfLoops", String.valueOf(5));
			parameters.put("threshold", String.valueOf(0.5));
			break;
		case Original_Subtopic:
			parameters.put("lemda", String.valueOf(0.15));
			parameters.put("theta", String.valueOf(4.0));
			break;
		case Original_Subtopic_Weight:
			parameters.put("lemda", String.valueOf(0.15));
			parameters.put("theta", String.valueOf(4.0));
			parameters.put("firstWeight", String.valueOf(0.6));
			parameters.put("lastWeight", String.valueOf(0.8));
			break;	
		case Improved_Subtopic:
			parameters.put("lemda", String.valueOf(0.67));
			parameters.put("theta", String.valueOf(8.0));
			break;
		case Improved_Subtopic_Weight:
			parameters.put("lemda", String.valueOf(0.66));
			parameters.put("theta", String.valueOf(9.0));
			parameters.put("firstWeight", String.valueOf(0.9));
			parameters.put("lastWeight", String.valueOf(0.8));
			break;	
		case Improved_Subtopic_Weight_Agg:
			parameters.put("lemda", String.valueOf(0.66));
			parameters.put("theta", String.valueOf(9.0));
			parameters.put("firstWeight", String.valueOf(0.9));
			parameters.put("lastWeight", String.valueOf(0.8));
			break;	
		case Improved_Agg:
			parameters.put("theta", String.valueOf(0.12));
			break;
		default:
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
