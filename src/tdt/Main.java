/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

/**
 * @author Zewei Wu
 */
public class Main {
	Vector<Story> corpus = new Vector<Story>();
	Glossary glossary = new Glossary();
	Vector<Story> actualFirstStories = new Vector<Story>();
	boolean isInitialized = false;

	// Used to record the files list containing a certain word.
	HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices = new HashMap<Integer, HashSet<Integer>>();
	int numOfTopics = 0;
	Vector<Story> firstStories = new Vector<Story>();
	double normCdet = 0.0;
	double PMiss = 0.0;
	double PFa = 0.0;

	/**
	 * @param args
	 *            Unused.
	 */
	// public static void main(String[] args) {
	//
	// // String tknDir = "Dataset/mttkn/";
	// // String bndDir = "Dataset/mttkn_bnd/";
	// // String sgmDir = "Dataset/sgm/";
	// // String ansFile = "Dataset/answer.txt";
	//
	// // String datasetDir = args[0];
	// String datasetDir =
	// "D:/Jee_workspace/TopicDetectionAndTracking/Dataset/";
	// String sgmDir = datasetDir + "sgm/";
	// String ansFile = datasetDir + "answer.txt";
	// String tfidfFile = datasetDir + "tfidf.dat";
	// String resultFile = datasetDir + "result.dat";
	//
	// System.out.println("====== Data Preprocessing Start ======");
	// DataPreprocessor dataPreprocessor = new DataPreprocessor();
	// // dataPreprocessor.doDataPreprocessing(corpus, glossary,
	// // wordIDToStoryIndices, actualFirstStories, tknDir, bndDir, ansFile);
	// dataPreprocessor.doDataPreprocessing(corpus, glossary,
	// wordIDToStoryIndices, actualFirstStories, sgmDir,
	// ansFile);
	// System.out.println("====== Data Preprocessing End ======");
	//
	// System.out.println();
	// System.out.println("corpus.size() = " + corpus.size());
	// assert(corpus.size() > 0);
	// System.out.println("glossary.size() = " + glossary.size());
	// assert(glossary.size() > 0);
	// System.out.println("wordIDToStoryIndices.size() = " +
	// wordIDToStoryIndices.size());
	// assert(wordIDToStoryIndices.size() > 0);
	// System.out.println("actualFirstStories.size() = " +
	// actualFirstStories.size());
	// assert(actualFirstStories.size() > 0);
	// System.out.println();
	//
	// System.out.println("====== Story Link Detection Start ======");
	// StoryLinkDetector.doStoryLinkDetection(corpus, wordIDToStoryIndices,
	// tfidfFile, false);
	// System.out.println("====== Story Link Detection End ======");
	//
	// System.out.println("====== Topic Detection Start ======");
	// TopicDetector topicDetector = new TopicDetector();
	// int numOfTopics = topicDetector.doTopicDetection(corpus);
	// System.out.println("====== Topic Detection End ======");
	//
	// System.out.println();
	// System.out.println("numOfTopics = " + numOfTopics);
	// assert(numOfTopics > 0);
	// System.out.println();
	//
	// System.out.println("====== First Story Detection Start ======");
	// Vector<Story> firstStories =
	// FirstStoryDetector.doFirstStoryDetection(corpus, numOfTopics);
	// System.out.println("====== First Story Detection End ======");
	//
	// System.out.println();
	// System.out.println("firstStories.size() = " + firstStories.size());
	// assert(firstStories.size() == numOfTopics);
	// System.out.println();
	//
	// System.out.println("====== Presentation Start ======");
	// Presentator.doPresentation(firstStories, corpus, glossary, numOfTopics,
	// resultFile);
	// System.out.println("====== Presentation End ======");
	//
	// System.out.println("====== Evaluation Start ======");
	// double normCdet = Evaluator.doEvaluation(corpus, actualFirstStories,
	// firstStories);
	// System.out.println("normCdet = " + normCdet);
	// System.out.println("====== Evaluation End ======");
	// }

	// <%!Vector<tdt.Story> corpus =
	// Main.getCorpus("D:/Jee_workspace/TopicDetectionAndTracking/Dataset/");%>
	// public static Vector<Story> getCorpus(String datasetDir) {
	// String sgmDir = datasetDir + "sgm/";
	// String ansFile = datasetDir + "answer.txt";
	//
	// System.out.println("====== Data Preprocessing Start ======");
	// DataPreprocessor dataPreprocessor = new DataPreprocessor();
	// // dataPreprocessor.doDataPreprocessing(corpus, glossary,
	// // wordIDToStoryIndices, actualFirstStories, tknDir, bndDir, ansFile);
	// dataPreprocessor.doDataPreprocessing(corpus, glossary,
	// wordIDToStoryIndices, actualFirstStories, sgmDir,
	// ansFile);
	// System.out.println("====== Data Preprocessing End ======");
	// return corpus;
	// }

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("doGet");

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		// response.setContentType("text/html; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println("doGet");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	public void initialize() {
		if (isInitialized)
			return;

		System.out.println("====== Start initializing ======");
		String datasetDir = "D:/Jee_workspace/TopicDetectionAndTracking/Dataset/";
		String sgmDir = datasetDir + "sgm/";
		String ansFile = datasetDir + "answer.txt";

		System.out.println("=== Data Preprocessing Start");
		DataPreprocessor dataPreprocessor = new DataPreprocessor();
		dataPreprocessor.doDataPreprocessing(corpus, glossary, wordIDToStoryIndices, actualFirstStories, sgmDir,
				ansFile);
		System.out.println("=== Data Preprocessing End");

		System.out.println("corpus.size() = " + corpus.size());
		assert(corpus.size() > 0);
		System.out.println("glossary.size() = " + glossary.size());
		assert(glossary.size() > 0);
		System.out.println("wordIDToStoryIndices.size() = " + wordIDToStoryIndices.size());
		assert(wordIDToStoryIndices.size() > 0);
		System.out.println("actualFirstStories.size() = " + actualFirstStories.size());
		assert(actualFirstStories.size() > 0);

		System.out.println("====== Done initializing ======");
		isInitialized = true;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("doPost");

		String action = request.getParameter("action");
		System.out.println(action);
		JSONObject responseJSONObject = new JSONObject();
		JSONObject tmp = null;
		if ("getEvaluation".equalsIgnoreCase(action)) {
			responseJSONObject = getEvaluation();
		} else if ("setParameters".equalsIgnoreCase(action)) {
			int methodID = Integer.parseInt(request.getParameter("methodID"));
			System.out.println("methodID = " + methodID);
			responseJSONObject = do_setParameters(methodID);
		} else if ("commitParameters".equalsIgnoreCase(action)) {
			int methodID = Integer.parseInt(request.getParameter("methodID"));
			System.out.println("methodID = " + methodID);
			
			String datasetDir = "D:/Jee_workspace/TopicDetectionAndTracking/Dataset/";
			String tfidfFile = datasetDir + "tfidf.dat";
			System.out.println("=== Story Link Detection Start");
			StoryLinkDetector.doStoryLinkDetection(corpus, wordIDToStoryIndices, tfidfFile, true, methodID);
			System.out.println("=== Story Link Detection End");
			
			System.out.println("=== Topic Detection Start");
			TopicDetector topicDetector = new TopicDetector();
			switch (methodID) {
			case 0:
				int numOfTopics = Integer.parseInt(request.getParameter("numOfTopics"));
				int numOfLoops = Integer.parseInt(request.getParameter("numOfLoops"));
				System.out.println("Parameters: ");
				System.out.println("> numOfTopics = " + numOfTopics);
				System.out.println("> numOfLoops = " + numOfLoops);
				topicDetector.KMeans(corpus, numOfTopics, numOfLoops);
				break;
			case 1:
				double minSimilarity = Double.parseDouble(request.getParameter("minSimilarity"));
				int minPts = Integer.parseInt(request.getParameter("minPts"));
				System.out.println("Parameters: ");
				System.out.println("> minSimilarity = " + minSimilarity);
				System.out.println("> minPts = " + minPts);
				topicDetector.DBSCAN(corpus, minSimilarity, minPts);
				break;
			default:
				break;
			}
			System.out.println("=== Topic Detection End");

			System.out.println("numOfTopics = " + numOfTopics);
			assert(numOfTopics > 0);

			System.out.println("=== First Story Detection Start");
			firstStories.clear();
			firstStories = FirstStoryDetector.doFirstStoryDetection(corpus, numOfTopics);
			System.out.println("=== First Story Detection End");

			System.out.println("firstStories.size() = " + firstStories.size());
			assert(firstStories.size() == numOfTopics);

			System.out.println("=== Evaluation Start");
			normCdet = Evaluator.getNormCdet(corpus, actualFirstStories, firstStories);
			PMiss = Evaluator.getPMiss(corpus, actualFirstStories, firstStories);
			PFa = Evaluator.getPFa(corpus, actualFirstStories, firstStories);
			System.out.println("normCdet = " + normCdet);
			System.out.println("PMiss = " + PMiss);
			System.out.println("PFa = " + PFa);
			System.out.println("=== Evaluation End");
			
		} else if ("getTopics".equalsIgnoreCase(action)) {
			// client choose a topic, response with a cluster of stroies of a
			// certain topic
			int methodID = Integer.parseInt(request.getParameter("methodID"));
			System.out.println("methodID = " + methodID);
			chooseAlgorithm(methodID);

			// {topicNum:, 0:{title:, source:, date:}, 1:{title:, source:,
			// date:}, ...}
			int topicNum = firstStories.size();
			responseJSONObject.put("topicNum", topicNum);
			for (int i = 0; i < topicNum; ++i) {
				tmp = new JSONObject();
				tmp.put("topicID", firstStories.get(i).getTopicID());
				tmp.put("title", firstStories.get(i).getTitle(glossary));
				tmp.put("source", firstStories.get(i).getSource());
				tmp.put("date", firstStories.get(i).getTimeStamp());
				responseJSONObject.put(i, tmp);
			}
		} else if ("getStories".equalsIgnoreCase(action)) {
			// client send a story id, response with the content of the story
			// {storyNum:, 0:{title:, source:, date:}, ...}

			int topicID = Integer.parseInt(request.getParameter("topicID"));

			Vector<Integer> stories = new Vector<Integer>();
			for (int i = 0; i < corpus.size(); ++i)
				if (corpus.get(i).getTopicID() == topicID)
					stories.add(i);

			int storyNum = stories.size();
			responseJSONObject.put("storyNum", storyNum);
			for (int i = 0; i < storyNum; ++i) {
				tmp = new JSONObject();
				tmp.put("storyID", stories.get(i)); // the story id in corpus
				tmp.put("title", corpus.get(stories.get(i)).getTitle(glossary));
				tmp.put("source", corpus.get(stories.get(i)).getSource());
				tmp.put("date", corpus.get(stories.get(i)).getTimeStamp());
				responseJSONObject.put(i, tmp);
			}
		} else if ("getContent".equalsIgnoreCase(action)) {
			int storyID = Integer.parseInt(request.getParameter("storyID"));
			responseJSONObject.put("content", corpus.get(storyID).getContent(glossary));
		} else {
			// invalid request
			response.setStatus(400);
			return;
		}

		// String[] datasetDir = new String[] {
		// "D:/Jee_workspace/TopicDetectionAndTracking/Dataset/" };
		// tdt.Main.main(datasetDir);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		// response.setContentType("text/html; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(responseJSONObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	private JSONObject do_setParameters(int methodID) {
		// {numOfParameters:, 0:{parameter:, value:}, ...}
		JSONObject responseJSONObject = new JSONObject();
		JSONObject tmp = null;
		int numOfParameters = 0;
		switch (methodID) {
		case 0:
			numOfParameters = 2;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfTopics");
			tmp.put("value", 250);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfLoops");
			tmp.put("value", 10);
			responseJSONObject.put(1, tmp);
			break;
		case 1:
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
		default:
			break;
		}
		return responseJSONObject;
	}

	/**
	 * Choose an algorithm of certain methodID, and use it to get a
	 * corresponding result.
	 * 
	 * @param methodID
	 */
	private void chooseAlgorithm(int methodID) {
		String datasetDir = "D:/Jee_workspace/TopicDetectionAndTracking/Dataset/";
		// String tfidfFile = datasetDir + "tfidf.dat";

		// TODO
		// System.out.println("=== Story Link Detection Start");
		// StoryLinkDetector.doStoryLinkDetection(corpus, wordIDToStoryIndices,
		// tfidfFile, false, methodID);
		// System.out.println("=== Story Link Detection End");

		System.out.println("=== Topic Detection Start");
		TopicDetector topicDetector = new TopicDetector();
		numOfTopics = topicDetector.doTopicDetection(corpus, methodID);
		System.out.println("=== Topic Detection End");

		System.out.println("numOfTopics = " + numOfTopics);
		assert(numOfTopics > 0);

		System.out.println("=== First Story Detection Start");
		firstStories.clear();
		firstStories = FirstStoryDetector.doFirstStoryDetection(corpus, numOfTopics);
		System.out.println("=== First Story Detection End");

		System.out.println("firstStories.size() = " + firstStories.size());
		assert(firstStories.size() == numOfTopics);

		System.out.println("=== Evaluation Start");
		normCdet = Evaluator.getNormCdet(corpus, actualFirstStories, firstStories);
		PMiss = Evaluator.getPMiss(corpus, actualFirstStories, firstStories);
		PFa = Evaluator.getPFa(corpus, actualFirstStories, firstStories);
		System.out.println("normCdet = " + normCdet);
		System.out.println("PMiss = " + PMiss);
		System.out.println("PFa = " + PFa);
		System.out.println("=== Evaluation End");
	}

	/**
	 * Response with the evaluations of different algorithms
	 * 
	 * @return {algorithmCount:, 0:{methodID:, algorithm:, overall:,
	 *         falsePositive:, falseNegative:}, ...}
	 */
	private JSONObject getEvaluation() {
		// TODO Evaluations calculation, offline?? and how to get fp, fn?
		JSONObject responseJSONObject = new JSONObject();
		JSONObject tmp = null;

		int algorithmNum = 2;
		responseJSONObject.put("algorithmCount", algorithmNum);

		tmp = new JSONObject();
		tmp.put("methodID", 0);
		tmp.put("algorithm", "tfidf_KMeans");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(0, tmp);

		tmp = new JSONObject();
		tmp.put("methodID", 1);
		tmp.put("algorithm", "tfidf_DBSCAN");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(1, tmp);

		return responseJSONObject;
	}

}