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
	private Glossary glossary = null;

	public TopicDetector(Vector<Story> corpus, Glossary glossary) {
		this.corpus = corpus;
		this.glossary = glossary;
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

		HashMap<String, String> parameters = MethodName.valueOf(methodID).getBestParameters();
		responseJSONObject.put("numOfParameters", parameters.size());
		int ctr = 0;
		for (Entry<String, String> entry : parameters.entrySet()) {
			String parameter = entry.getKey();
			String value = entry.getValue();
			tmp = new JSONObject();
			tmp.put("parameter", parameter);
			tmp.put("value", value);
			responseJSONObject.put(ctr, tmp);
			++ctr;
		}

		return responseJSONObject;
	}

	public int doTopicDetection(HttpServletRequest request) {
		for (Story story : corpus)
			story.setTopicID(-1);

		StoryLinkDetector storyLinkDetector = new StoryLinkDetector();
		int numOfTopics = 0;
		HashMap<String, String> parameters = new HashMap<String, String>();
		Clustering clustering = null;
		ClusteringEnsembler ensembler = null;
		ArrayList<Integer> resultPartition = null;

		int methodID = Integer.parseInt(request.getParameter("methodID"));
		System.out.println("methodID = " + methodID);
		MethodName methodName = MethodName.valueOf(methodID);
		switch (methodName) {
		/* LDA */
		case LDA_KMeans:
		case LDA_DBSCAN:
		case LDA_AggDetection:
		case LDA_VotingKMeans:
			int ldaNumOfTopics = Integer.parseInt(request.getParameter("lda.numOfTopics"));
			int ldaNumOfIterations = Integer.parseInt(request.getParameter("lda.numOfIterations"));
			double ldaLAMBDA = Double.parseDouble(request.getParameter("lda.lambda"));
			double ldaALPHA = Double.parseDouble(request.getParameter("lda.alpha"));
			double ldaBETA = Double.parseDouble(request.getParameter("lda.beta"));
			storyLinkDetector.enableLDA(corpus, glossary);
			storyLinkDetector.trainLDA(ldaNumOfTopics, ldaNumOfIterations, ldaLAMBDA, ldaALPHA, ldaBETA);
			break;
		/* pLSA */
		case pLSA_KMeans:
		case pLSA_DBSCAN:
		case pLSA_AggDetection:
		case pLSA_VotingKMeans:
			int plsaNumOfTopics = Integer.parseInt(request.getParameter("plsa.numOfTopics"));
			int plsaNumOfIterations = Integer.parseInt(request.getParameter("plsa.numOfIterations"));
			storyLinkDetector.enablePlsa(corpus, glossary);
			storyLinkDetector.trainPlsa(plsaNumOfTopics, plsaNumOfIterations);
			break;
		/* TFIDF */
		default:
			break;
		}

		switch (methodName) {
		case TFIDF_KMeans:
		case LDA_KMeans:
		case pLSA_KMeans:
			numOfTopics = Integer.parseInt(request.getParameter("numOfTopics"));
			int numOfLoops = Integer.parseInt(request.getParameter("numOfLoops"));
			System.out.println("Parameters: ");
			System.out.println("> numOfTopics = " + numOfTopics);
			System.out.println("> numOfLoops = " + numOfLoops);
			parameters.put("numOfTopics", String.valueOf(numOfTopics));
			parameters.put("numOfLoops", String.valueOf(numOfLoops));
			clustering = new Clustering(corpus, storyLinkDetector);
			resultPartition = clustering.doClustering(methodName, parameters);
			break;
		case TFIDF_DBSCAN:
		case LDA_DBSCAN:
		case pLSA_DBSCAN:
			double minSimilarity = Double.parseDouble(request.getParameter("minSimilarity"));
			int minPts = Integer.parseInt(request.getParameter("minPts"));
			System.out.println("Parameters: ");
			System.out.println("> minSimilarity = " + minSimilarity);
			System.out.println("> minPts = " + minPts);
			parameters.put("minSimilarity", String.valueOf(minSimilarity));
			parameters.put("minPts", String.valueOf(minPts));
			clustering = new Clustering(corpus, storyLinkDetector);
			resultPartition = clustering.doClustering(methodName, parameters);
			break;
		case TFIDF_AggDetection:
		case LDA_AggDetection:
		case pLSA_AggDetection:
			double threshold = Double.parseDouble(request.getParameter("threshold"));
			System.out.println("Parameters: ");
			System.out.println("> threshold = " + threshold);
			parameters.put("threshold", String.valueOf(threshold));
			clustering = new Clustering(corpus, storyLinkDetector);
			resultPartition = clustering.doClustering(methodName, parameters);
			break;
		case TFIDF_VotingKMeans:
		case LDA_VotingKMeans:
		case pLSA_VotingKMeans:
			int numOfPartitions = Integer.parseInt(request.getParameter("numOfPartitions"));
			numOfTopics = Integer.parseInt(request.getParameter("numOfTopics"));
			numOfLoops = Integer.parseInt(request.getParameter("numOfLoops"));
			System.out.println("Parameters: ");
			System.out.println("> numOfPartitions = " + numOfPartitions);
			System.out.println("> numOfTopics = " + numOfTopics);
			System.out.println("> numOfLoops = " + numOfLoops);
			parameters.put("numOfPartitions", String.valueOf(numOfPartitions));
			parameters.put("numOfTopics", String.valueOf(numOfTopics));
			parameters.put("numOfLoops", String.valueOf(numOfLoops));
			System.out.println("parameters: " + parameters.size());
			ensembler = new ClusteringEnsembler(corpus, storyLinkDetector);
			resultPartition = ensembler.doClustering(methodName, parameters);
			break;
		case TFIDF_EA_SL:
		case LDA_EA_SL:
		case pLSA_EA_SL:
			numOfPartitions = Integer.parseInt(request.getParameter("numOfPartitions"));
			numOfTopics = Integer.parseInt(request.getParameter("numOfTopics"));
			numOfLoops = Integer.parseInt(request.getParameter("numOfLoops"));
			threshold = Double.parseDouble(request.getParameter("threshold"));
			System.out.println("Parameters: ");
			System.out.println("> numOfPartitions = " + numOfPartitions);
			System.out.println("> numOfTopics = " + numOfTopics);
			System.out.println("> numOfLoops = " + numOfLoops);
			System.out.println("> threshold = " + threshold);
			parameters.put("numOfPartitions", String.valueOf(numOfPartitions));
			parameters.put("numOfTopics", String.valueOf(numOfTopics));
			parameters.put("numOfLoops", String.valueOf(numOfLoops));
			parameters.put("threshold", String.valueOf(threshold));
			ensembler = new ClusteringEnsembler(corpus, storyLinkDetector);
			resultPartition = ensembler.doClustering(methodName, parameters);
			break;
		default:
			break;
		}

		numOfTopics = 0;
		for (int i = 0; i < resultPartition.size(); ++i)
			if (resultPartition.get(i) > numOfTopics)
				numOfTopics = resultPartition.get(i);
		++numOfTopics;
		return numOfTopics;
	}
}
