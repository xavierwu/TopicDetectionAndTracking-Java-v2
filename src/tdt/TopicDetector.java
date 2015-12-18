/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
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

		for (MethodName methodName : MethodName.values()) {
			tmp = new JSONObject();
			tmp.put("methodID", methodName.getMethodID());
			tmp.put("algorithm", methodName.getName());
			tmp.put("normCdet", methodName.getBestNormCdet());
			tmp.put("PMiss", methodName.getBestPMiss());
			tmp.put("PFa", methodName.getBestPFa());
			responseJSONObject.put(methodName.getMethodID(), tmp);
		}

		responseJSONObject.put("algorithmCount", MethodName.values().length);
		return responseJSONObject;
	}

	public JSONObject prepareTopicDetection(int methodID) {
		JSONObject responseJSONObject = new JSONObject();
		JSONObject tmp = null;

		HashMap<String, Double> parameters = MethodName.valueOf(methodID).getBestParameters();
		responseJSONObject.put("numOfParameters", parameters.size());
		int ctr = 0;
		for (Entry<String, Double> entry : parameters.entrySet()) {
			String parameter = entry.getKey();
			double value = entry.getValue();
			tmp = new JSONObject();
			tmp.put("parameter", parameter);
			tmp.put("value", value);
			responseJSONObject.put(ctr, tmp);
			++ctr;
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
