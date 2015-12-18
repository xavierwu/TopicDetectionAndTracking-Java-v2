package tdt;

import java.util.HashMap;

import net.sf.json.JSONObject;

/**
 * @author Zewei Wu
 *
 */
public enum MethodName {
	TFIDF_KMeans("", 0, 0, 0),
	LDA_KMeans("", 0, 0, 0),
	pLSA_KMeans("", 0, 0, 0),
	TFIDF_DBSCAN("", 0, 0, 0),
	LDA_DBSCAN("", 0, 0, 0),
	pLSA_DBSCAN("", 0, 0, 0),
	TFIDF_AggDetection("", 0, 0, 0),
	LDA_AggDetection("", 0, 0, 0),
	pLSA_AggDetection("", 0, 0, 0),
	TFIDF_VotingKMeans("", 0, 0, 0),
	LDA_VotingKMeans("", 0, 0, 0),
	pLSA_VotingKMeans("", 0, 0, 0);

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
	
	public static MethodName valueOf(int methodID){
		return MethodName.values()[methodID];
	}

	public HashMap<String, Double> getBestParameters() {
		HashMap<String, Double> parameters = new HashMap<String, Double>();
		switch (this) {
		case TFIDF_KMeans:
			parameters.put("numOfTopics", 63.0);
			parameters.put("numOfLoops", 5.0);
			break;
		case LDA_KMeans:
			parameters.put("lda.numOfTopics", 63.0);
			parameters.put("lda.numOfIteration", 5.0);
			parameters.put("lda.lambda", 0.5);
			parameters.put("lda.alpha", 0.5);
			parameters.put("lda.beta", 0.01);
			parameters.put("numOfTopics", 63.0);
			parameters.put("numOfLoops", 5.0);
			break;
		case pLSA_KMeans:
			parameters.put("plsa.numOfTopics", 63.0);
			parameters.put("plsa.numOfIteration", 5.0);
			parameters.put("numOfTopics", 63.0);
			parameters.put("numOfLoops", 5.0);
			break;
		case TFIDF_DBSCAN:
			break;
		case LDA_DBSCAN:
			break;
		case pLSA_DBSCAN:
			break;
		case TFIDF_AggDetection:
			break;
		case LDA_AggDetection:
			break;
		case pLSA_AggDetection:
			break;
		case TFIDF_VotingKMeans:
			break;
		case LDA_VotingKMeans:
			break;
		case pLSA_VotingKMeans:
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
	

			
		
		case 3: // tfidf_DBSCAN
			numOfParameters = 2;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "minSimilarity");
			tmp.put("value", 0.98);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "minPts");
			tmp.put("value", 5);
			responseJSONObject.put(1, tmp);
			break;
		case 4: // lda_DBSCAN
			numOfParameters = 7;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.numOfTopics");
			tmp.put("value", 63);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.numOfIterations");
			tmp.put("value", 5);
			responseJSONObject.put(1, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.lambda");
			tmp.put("value", 0.5);
			responseJSONObject.put(2, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.alpha");
			tmp.put("value", 0.5);
			responseJSONObject.put(3, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.beta");
			tmp.put("value", 0.01);
			responseJSONObject.put(4, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "minSimilarity");
			tmp.put("value", 0.98);
			responseJSONObject.put(5, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "minPts");
			tmp.put("value", 5);
			responseJSONObject.put(6, tmp);
			break;
		case 5: // plsa_DBSCAN
			numOfParameters = 4;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "plsa.numOfTopics");
			tmp.put("value", 63);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "plsa.numOfIterations");
			tmp.put("value", 5);
			responseJSONObject.put(1, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "minSimilarity");
			tmp.put("value", 0.98);
			responseJSONObject.put(2, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "minPts");
			tmp.put("value", 5);
			responseJSONObject.put(3, tmp);
			break;
		case 6: // tfidf_aggDetection
			numOfParameters = 1;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "threshold");
			tmp.put("value", 0.144);
			responseJSONObject.put(0, tmp);
			break;
		case 7: // lda_aggDetection
			numOfParameters = 6;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.numOfTopics");
			tmp.put("value", 36);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.numOfIterations");
			tmp.put("value", 5);
			responseJSONObject.put(1, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.lambda");
			tmp.put("value", 0.5);
			responseJSONObject.put(2, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.alpha");
			tmp.put("value", 0.5);
			responseJSONObject.put(3, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.beta");
			tmp.put("value", 0.01);
			responseJSONObject.put(4, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "threshold");
			tmp.put("value", 0.144);
			responseJSONObject.put(5, tmp);
			break;
		case 8: // plsa_aggDetection
			numOfParameters = 3;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "plsa.numOfTopics");
			tmp.put("value", 63);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "plsa.numOfIterations");
			tmp.put("value", 5);
			responseJSONObject.put(1, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "threshold");
			tmp.put("value", 0.144);
			responseJSONObject.put(2, tmp);
			break;
		case 9: // tfidf_votingKMeans
			numOfParameters = 3;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfPartitions");
			tmp.put("value", 5);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfTopics");
			tmp.put("value", 36);
			responseJSONObject.put(1, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfLoops");
			tmp.put("value", 5);
			responseJSONObject.put(2, tmp);
			break;
		case 10: // lda_votingKMeans
			numOfParameters = 8;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.numOfTopics");
			tmp.put("value", 63);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.numOfIterations");
			tmp.put("value", 5);
			responseJSONObject.put(1, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.lambda");
			tmp.put("value", 0.5);
			responseJSONObject.put(2, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.alpha");
			tmp.put("value", 0.5);
			responseJSONObject.put(3, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "lda.beta");
			tmp.put("value", 0.01);
			responseJSONObject.put(4, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfPartitions");
			tmp.put("value", 5);
			responseJSONObject.put(5, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfTopics");
			tmp.put("value", 36);
			responseJSONObject.put(6, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfLoops");
			tmp.put("value", 5);
			responseJSONObject.put(7, tmp);
			break;
		case 11: // plsa_votingKMeans
			numOfParameters = 5;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "plsa.numOfTopics");
			tmp.put("value", 63);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "plsa.numOfIterations");
			tmp.put("value", 5);
			responseJSONObject.put(1, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfPartitions");
			tmp.put("value", 5);
			responseJSONObject.put(2, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfTopics");
			tmp.put("value", 63);
			responseJSONObject.put(3, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfLoops");
			tmp.put("value", 5);
			responseJSONObject.put(4, tmp);
			break;
		default:
			break;
		}
	 * */
	
}
