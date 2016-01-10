/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.util.ArrayList;
import java.util.Enumeration;
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

		HashMap<String, String> parameters = MethodName.valueOf(methodID)
				.getBestParameters();
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

	/**
	 * 
	 * @param request
	 * @return
	 */
	public int doTopicDetection(HttpServletRequest request) {
		for (Story story : corpus)
			story.setTopicID(-1);

		int methodID = Integer.parseInt(request.getParameter("methodID"));
		System.out.println("methodID = " + methodID);

		MethodName methodName = MethodName.valueOf(methodID);

		StoryLinkDetector storyLinkDetector = new StoryLinkDetector(
				methodName.getSimilarityName(), corpus, glossary);
		storyLinkDetector.train(request);

		HashMap<String, String> parameters = new HashMap<String, String>();
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			parameters.put(name, request.getParameter(name));
		}

		ArrayList<Integer> resultPartition = null;
		if (methodName.getEnsemblerName() != null) {
			ClusteringEnsembler ensembler = new ClusteringEnsembler(
					methodName.getEnsemblerName(), corpus, storyLinkDetector);
			ensembler.train(parameters);
			resultPartition = ensembler.doClustering();
		} else if (methodName.getClusteringName() != null) {
			Clustering clustering = new Clustering(
					methodName.getClusteringName(), corpus, storyLinkDetector);
			clustering.train(parameters);
			resultPartition = clustering.doClustering();
		} else {
			return 0;
		}

		int numOfTopics = 0;
		for (int i = 0; i < resultPartition.size(); ++i)
			if (resultPartition.get(i) > numOfTopics)
				numOfTopics = resultPartition.get(i);
		++numOfTopics;
		return numOfTopics;
	}

}
