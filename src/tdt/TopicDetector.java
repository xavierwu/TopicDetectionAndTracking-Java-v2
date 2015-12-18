/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

/**
 * This class is used to classify the corpus. Give every story an unique topic
 * ID. This is the core step of the whole project. We will try to find the best
 * algorithm for the purpose of classification.
 * 
 * @author Zhaoqi Wang, Zewei Wu
 */
class TopicDetector {
	private Vector<Story> corpus = null;
	private StoryLinkDetector storyLinkDetector = null;

	public TopicDetector(Vector<Story> corpus, Glossary glossary) {
		this.corpus = corpus;
		this.storyLinkDetector = new StoryLinkDetector();
		this.storyLinkDetector.enableLDA(corpus, glossary);
		this.storyLinkDetector.enablePlsa(corpus, glossary);
	}

	public JSONObject getMethodList() {
		JSONObject responseJSONObject = new JSONObject();
		JSONObject tmp = null;

		int methodID = 0;
		// tfidf_KMeans
		tmp = new JSONObject();
		methodID = 0;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "tfidf_KMeans");
		tmp.put("normCdet", 4.5889);
		tmp.put("PMiss", 0.7778);
		tmp.put("PFa", 0.7778);
		responseJSONObject.put(methodID, tmp);
		// lda_KMeans
		tmp = new JSONObject();
		methodID = 1;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "lda_KMeans");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);
		// plsa_KMeans
		tmp = new JSONObject();
		methodID = 2;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "plsa_KMeans");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);

		// tfidf_DBSCAN
		tmp = new JSONObject();
		methodID = 3;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "tfidf_DBSCAN");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);
		// lda_DBSCAN
		tmp = new JSONObject();
		methodID = 4;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "lda_DBSCAN");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);
		// plsa_DBSCAN
		tmp = new JSONObject();
		methodID = 5;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "plsa_DBSCAN");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);

		// tfidf_aggDetection
		tmp = new JSONObject();
		methodID = 6;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "tfidf_aggDetection");
		tmp.put("normCdet", 3.7694);
		tmp.put("PMiss", 0.6389);
		tmp.put("PFa", 0.6389);
		responseJSONObject.put(methodID, tmp);
		// lda_aggDetection
		tmp = new JSONObject();
		methodID = 7;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "lda_aggDetection");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);
		// plsa_aggDetection
		tmp = new JSONObject();
		methodID = 8;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "plsa_aggDetection");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);

		// tfidf_votingKMeans
		tmp = new JSONObject();
		methodID = 9;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "tfidf_votingKMeans");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);
		// lda_votingKMeans
		tmp = new JSONObject();
		methodID = 10;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "lda_votingKMeans");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);
		// plsa_votingKMeans
		tmp = new JSONObject();
		methodID = 11;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "plsa_votingKMeans");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);

		responseJSONObject.put("algorithmCount", methodID + 1);
		return responseJSONObject;
	}

	public JSONObject prepareTopicDetection(int methodID) {
		JSONObject responseJSONObject = new JSONObject();
		JSONObject tmp = null;
		int numOfParameters = 0;
		switch (methodID) {
		case 0: // tfidf_KMeans
			numOfParameters = 2;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfTopics");
			tmp.put("value", 63);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfLoops");
			tmp.put("value", 5);
			responseJSONObject.put(1, tmp);
			break;
		case 1: // lda_KMeans
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
			tmp.put("parameter", "numOfTopics");
			tmp.put("value", 63);
			responseJSONObject.put(5, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfLoops");
			tmp.put("value", 5);
			responseJSONObject.put(6, tmp);
			break;
		case 2: // plsa_KMeans
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
			tmp.put("parameter", "numOfTopics");
			tmp.put("value", 63);
			responseJSONObject.put(2, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfLoops");
			tmp.put("value", 5);
			responseJSONObject.put(3, tmp);
			break;
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
		return responseJSONObject;
	}

	public int doTopicDetection(HttpServletRequest request) {
		int numOfTopics = 0;
		String methodName = "";
		HashMap<String, Double> parameters = new HashMap<String, Double>();
		Clustering clustering = null;
		ClusteringEnsembler ensembler = null;
		ArrayList<Integer> resultPartition = null;

		int methodID = Integer.parseInt(request.getParameter("methodID"));
		System.out.println("methodID = " + methodID);

		if (methodID == 1 || methodID == 4 || methodID == 7 || methodID == 10) { // lda
			int ldaNumOfTopics = Integer.parseInt(request.getParameter("lda.numOfTopics"));
			int ldaNumOfIterations = Integer.parseInt(request.getParameter("lda.numOfIterations"));
			double ldaLAMBDA = Double.parseDouble(request.getParameter("lda.lambda"));
			double ldaALPHA = Double.parseDouble(request.getParameter("lda.alpha"));
			double ldaBETA = Double.parseDouble(request.getParameter("lda.beta"));
			storyLinkDetector.trainLDA(ldaNumOfTopics, ldaNumOfIterations, ldaLAMBDA, ldaALPHA, ldaBETA);
		} else if (methodID == 2 || methodID == 5 || methodID == 8 || methodID == 11) {// plsa
			int plsaNumOfTopics = Integer.parseInt(request.getParameter("plsa.numOfTopics"));
			int plsaNumOfIterations = Integer.parseInt(request.getParameter("plsa.numOfIterations"));
			storyLinkDetector.trainPlsa(plsaNumOfTopics, plsaNumOfIterations);
		}

		if (methodID >= 0 && methodID <= 8) {// clustering
			clustering = new Clustering(corpus, storyLinkDetector);
		} else if (methodID >= 9 && methodID <= 11) { // clustering ensemble
			ensembler = new ClusteringEnsembler(corpus, storyLinkDetector);
		}
		if (methodID == 0 || methodID == 1 || methodID == 2) { // k-means
			numOfTopics = Integer.parseInt(request.getParameter("numOfTopics"));
			int numOfLoops = Integer.parseInt(request.getParameter("numOfLoops"));
			System.out.println("Parameters: ");
			System.out.println("> numOfTopics = " + numOfTopics);
			System.out.println("> numOfLoops = " + numOfLoops);
			methodName = "kmeans";
			parameters.put("numOfTopics", (double) numOfTopics);
			parameters.put("numOfLoops", (double) numOfLoops);
			resultPartition = clustering.doClustering(methodName, parameters);
		} else if (methodID == 3 || methodID == 4 || methodID == 5) {// DBSCAN
			double minSimilarity = Double.parseDouble(request.getParameter("minSimilarity"));
			int minPts = Integer.parseInt(request.getParameter("minPts"));
			System.out.println("Parameters: ");
			System.out.println("> minSimilarity = " + minSimilarity);
			System.out.println("> minPts = " + minPts);
			methodName = "DBSCAN";
			parameters.put("minSimilarity", minSimilarity);
			parameters.put("minPts", (double) minPts);
			resultPartition = clustering.doClustering(methodName, parameters);
		} else if (methodID == 6 || methodID == 7 || methodID == 8) {// aggDetection
			double threshold = Double.parseDouble(request.getParameter("threshold"));
			System.out.println("Parameters: ");
			System.out.println("> threshold = " + threshold);
			methodName = "aggDetection";
			parameters.put("threshold", threshold);
			resultPartition = clustering.doClustering(methodName, parameters);
		} else if (methodID == 9 || methodID == 10 || methodID == 11) { // votingKMeans
			int numOfPartitions = Integer.parseInt(request.getParameter("numOfPartitions"));
			numOfTopics = Integer.parseInt(request.getParameter("numOfTopics"));
			int numOfLoops = Integer.parseInt(request.getParameter("numOfLoops"));
			System.out.println("Parameters: ");
			System.out.println("> numOfPartitions = " + numOfPartitions);
			System.out.println("> numOfTopics = " + numOfTopics);
			System.out.println("> numOfLoops = " + numOfLoops);
			methodName = "votingKMeans";
			parameters.put("numOfPartitions", (double) numOfPartitions);
			parameters.put("numOfTopics", (double) numOfTopics);
			parameters.put("numOfLoops", (double) numOfLoops);
			System.out.println("parameters: " + parameters.size());
			resultPartition = ensembler.doClustering(methodName, parameters);
		}
		numOfTopics = resultPartition.size();
		return numOfTopics;
	}
}
