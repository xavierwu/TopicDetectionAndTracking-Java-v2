/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

/**
 * @author Zewei Wu
 */
public class Main {
	// Set by parameters of constructor.
	private String dataFilesdir = null;
	private String glossaryFile = null;
	private String tfidfFile = null;
	private String ansFile = null;
	// Important data structures.
	private Vector<Story> corpus = new Vector<Story>();
	private Glossary glossary = new Glossary();
	private Vector<Story> actualFirstStories = new Vector<Story>();
	private Vector<Story> firstStories = new Vector<Story>();
	// Results
	private int numOfTopics = 0;
	private double normCdet = 0.0;
	private double PMiss = 0.0;
	private double PFa = 0.0;

	/**
	 * Initialization of our program, everything about data pre-processing
	 * should be put here.
	 * 
	 * @param dataFilesdir
	 * @param glossaryFile
	 * @param tfidfFile
	 * @param ansFile
	 */
	public Main(String dataFilesdir, String glossaryFile, String tfidfFile, String ansFile) {
		this.dataFilesdir = dataFilesdir;
		this.glossaryFile = glossaryFile;
		this.tfidfFile = tfidfFile;
		this.ansFile = ansFile;
		System.out.println("====== Start initializing ======");

		glossary.load(glossaryFile);
		DataPreprocessor.recoverCorpusFromTFIDF(corpus, tfidfFile);
		DataPreprocessor.readAnswer_v2(actualFirstStories, ansFile);

		System.out.println("corpus: " + corpus.size());
		assert(corpus.size() > 0);
		System.out.println("glossary: " + glossary.size());
		assert(glossary.size() > 0);
		System.out.println("actualFirstStories: " + actualFirstStories.size());
		assert(actualFirstStories.size() > 0);

		System.out.println("====== Done initializing ======");
	}

	/**
	 * Deal with a get request. Currently, it just print a short message.
	 * 
	 * @param request
	 * @param response
	 */
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

	/**
	 * Deal with a post request.
	 * 
	 * @param request
	 * @param response
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String action = request.getParameter("action");
		System.out.println("doPost: " + action);

		JSONObject responseJSONObject = new JSONObject();
		if ("getEvaluation".equalsIgnoreCase(action)) {
			responseJSONObject = do_getEvaluation();
		} else if ("setParameters".equalsIgnoreCase(action)) {
			responseJSONObject = do_setParameters(request);
		} else if ("commitParameters".equalsIgnoreCase(action)) {
			responseJSONObject = do_commitParameters(request);
		} else if ("getStories".equalsIgnoreCase(action)) {
			responseJSONObject = do_getStories(request);
		} else if ("getContent".equalsIgnoreCase(action)) {
			responseJSONObject = do_getContent(request);
		} else {
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

	/**
	 * Response with the evaluations of different algorithms.
	 * 
	 * @return {algorithmCount:, 0:{methodID:, algorithm:, overall:, normCdet:,
	 *         PMiss:, PFa:}, 1:{...}, ...}
	 */
	private JSONObject do_getEvaluation() {
		// TODO This method should return the best results of all algorithms.

		JSONObject responseJSONObject = new JSONObject();
		JSONObject tmp = null;

		int algorithmNum = 6;
		responseJSONObject.put("algorithmCount", algorithmNum);

		int methodID = 0;
		// tfidf_KMeans
		tmp = new JSONObject();
		methodID = 0;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "tfidf_KMeans");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);
		// plsa_KMeans
		tmp = new JSONObject();
		methodID = 1;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "plsa_KMeans");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);

		// tfidf_DBSCAN
		tmp = new JSONObject();
		methodID = 2;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "tfidf_DBSCAN");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);
		// plsa_DBSCAN
		tmp = new JSONObject();
		methodID = 3;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "plsa_DBSCAN");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);

		// tfidf_aggDetection
		tmp = new JSONObject();
		methodID = 4;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "tfidf_aggDetection");
		tmp.put("normCdet", 3.7694);
		tmp.put("PMiss", 0.6389);
		tmp.put("PFa", 0.6389);
		responseJSONObject.put(methodID, tmp);
		// plsa_aggDetection
		tmp = new JSONObject();
		methodID = 5;
		tmp.put("methodID", methodID);
		tmp.put("algorithm", "plsa_aggDetection");
		tmp.put("normCdet", 5.9);
		tmp.put("PMiss", 1.0);
		tmp.put("PFa", 1.0);
		responseJSONObject.put(methodID, tmp);

		return responseJSONObject;
	}

	/**
	 * Help doPost() to deal with the "setParameters" request.
	 * 
	 * @param request
	 *            It should contain a JSON with a key "methodID". And it should
	 *            have a key-value pair <"action":"setParameters">, though in
	 *            this method we don't check it.
	 * @return {numOfParameters:, 0:{parameter:, value:}, 1{...}, ...}
	 */
	private JSONObject do_setParameters(HttpServletRequest request) {
		int methodID = Integer.parseInt(request.getParameter("methodID"));
		System.out.println("methodID = " + methodID);
		// {numOfParameters:, 0:{parameter:, value:}, ...}
		JSONObject responseJSONObject = new JSONObject();
		JSONObject tmp = null;
		int numOfParameters = 0;
		switch (methodID) {
		// TODO how to set parameters for plsa?
		case 0: // tfidf_KMeans
		case 1: // plsa_KMeans
			numOfParameters = 2;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfTopics");
			tmp.put("value", 36);
			responseJSONObject.put(0, tmp);

			tmp = new JSONObject();
			tmp.put("parameter", "numOfLoops");
			tmp.put("value", 5);
			responseJSONObject.put(1, tmp);
			break;
		case 2: // tfidf_DBSCAN
		case 3: // plsa_DBSCAN
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
		case 4: // tfidf_aggDetection
		case 5: // plsa_aggDetection
			numOfParameters = 1;
			responseJSONObject.put("numOfParameters", numOfParameters);

			tmp = new JSONObject();
			tmp.put("parameter", "threshold");
			tmp.put("value", 0.144);
			responseJSONObject.put(0, tmp);
			break;
		default:
			break;
		}
		return responseJSONObject;
	}

	/**
	 * Help doPost() to deal with the "commitParameters" request.
	 * 
	 * @param request
	 *            It's required to have a JSON with a key "methodID", and
	 *            expected parameters for a certain method.
	 * @return {normCdet:, PMiss:, PFa:, topicNum:, 0:{topicID, title:, source:,
	 *         date:}, 1:{...}, ...}
	 */
	private JSONObject do_commitParameters(HttpServletRequest request) {
		JSONObject responseJSONObject = new JSONObject();
		int methodID = Integer.parseInt(request.getParameter("methodID"));
		System.out.println("methodID = " + methodID);

		System.out.println("=== Topic Detection Start");
		TopicDetector topicDetector = new TopicDetector();
		int numOfLoops = 0;
		double minSimilarity = 0.0;
		int minPts = 0;
		double threshold = 0.0;
		switch (methodID) {
		// TODO maybe i should throw a exception if the required parameters are
		// not set?
		case 0:// tfidf_KMeans
			numOfTopics = Integer.parseInt(request.getParameter("numOfTopics"));
			numOfLoops = Integer.parseInt(request.getParameter("numOfLoops"));
			System.out.println("Parameters: ");
			System.out.println("> numOfTopics = " + numOfTopics);
			System.out.println("> numOfLoops = " + numOfLoops);
			topicDetector.KMeans(corpus, 0, numOfTopics, numOfLoops);
			break;
		case 1: // plsa_KMeans
			numOfTopics = Integer.parseInt(request.getParameter("numOfTopics"));
			numOfLoops = Integer.parseInt(request.getParameter("numOfLoops"));
			System.out.println("Parameters: ");
			System.out.println("> numOfTopics = " + numOfTopics);
			System.out.println("> numOfLoops = " + numOfLoops);
			topicDetector.KMeans(corpus, 1, numOfTopics, numOfLoops);
			break;
		case 2: // tfidf_DBSCAN
			minSimilarity = Double.parseDouble(request.getParameter("minSimilarity"));
			minPts = Integer.parseInt(request.getParameter("minPts"));
			System.out.println("Parameters: ");
			System.out.println("> minSimilarity = " + minSimilarity);
			System.out.println("> minPts = " + minPts);
			numOfTopics = topicDetector.DBSCAN(corpus, 0, minSimilarity, minPts);
			break;
		case 3: // plsa_DBSCAN
			minSimilarity = Double.parseDouble(request.getParameter("minSimilarity"));
			minPts = Integer.parseInt(request.getParameter("minPts"));
			System.out.println("Parameters: ");
			System.out.println("> minSimilarity = " + minSimilarity);
			System.out.println("> minPts = " + minPts);
			numOfTopics = topicDetector.DBSCAN(corpus, 1, minSimilarity, minPts);
			break;
		case 4: // tfidf_aggDetection
			threshold = Double.parseDouble(request.getParameter("threshold"));
			System.out.println("Parameters: ");
			System.out.println("> threshold = " + threshold);
			numOfTopics = topicDetector.aggDetection(corpus, 0, threshold);
			break;
		case 5:// plsa_aggDetection
			threshold = Double.parseDouble(request.getParameter("threshold"));
			System.out.println("Parameters: ");
			System.out.println("> threshold = " + threshold);
			numOfTopics = topicDetector.aggDetection(corpus, 1, threshold);
			break;
		default:
			break;
		}
		System.out.println("=== Topic Detection End");

		// TODO this method seems to be too large?

		System.out.println("numOfTopics = " + numOfTopics);
		assert(numOfTopics > 0);

		System.out.println("=== First Story Detection Start");
		firstStories.clear();
		firstStories = FirstStoryDetector.doFirstStoryDetection(corpus, numOfTopics);
		System.out.println("=== First Story Detection End");

		System.out.println("firstStories: " + firstStories.size());
		// for (Story story : firstStories)
		// System.out.println(story.getTimeStamp());
		System.out.println("actualFirstStories: " + actualFirstStories.size());
		// for (Story story : actualFirstStories)
		// System.out.println(story.getTimeStamp());

		System.out.println("=== Evaluation Start");
		Evaluator evaluator = new Evaluator();
		evaluator.doEvaluation_v3(corpus, actualFirstStories, firstStories);
		normCdet = evaluator.getNormCdet();
		PMiss = evaluator.getPMiss();
		PFa = evaluator.getPFa();
		System.out.println("normCdet = " + normCdet);
		System.out.println("PMiss = " + PMiss);
		System.out.println("PFa = " + PFa);
		System.out.println("=== Evaluation End");
		responseJSONObject.put("normCdet", normCdet);
		responseJSONObject.put("PMiss", PMiss);
		responseJSONObject.put("PFa", PFa);

		// {normCdet:, PMiss:, PFa:, topicNum:, 0:{topicID, title:, source:,
		// date:}, ...}
		int topicNum = firstStories.size();
		JSONObject tmp = null;
		responseJSONObject.put("topicNum", topicNum);
		for (int i = 0; i < topicNum; ++i) {
			tmp = new JSONObject();
			tmp.put("topicID", firstStories.get(i).getTopicID());
			tmp.put("title", firstStories.get(i).getTitle(dataFilesdir));
			tmp.put("source", firstStories.get(i).getSource());
			tmp.put("date", firstStories.get(i).getTimeStamp());
			responseJSONObject.put(i, tmp);
		}
		return responseJSONObject;
	}

	/**
	 * Help doPost() to deal with the "getStories" request.
	 * 
	 * @param request
	 *            It's required to have a JSON with key "topicID"
	 * @return {storyNum:, 0:{storyID:, title:, source:, date:}, 1:{...}, ...}
	 */
	private JSONObject do_getStories(HttpServletRequest request) {
		JSONObject responseJSONObject = new JSONObject();
		JSONObject tmp = null;
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
			// TODO how to get a reasonable title?
			tmp.put("title", corpus.get(stories.get(i)).getTitle(dataFilesdir));
			tmp.put("source", corpus.get(stories.get(i)).getSource());
			tmp.put("date", corpus.get(stories.get(i)).getTimeStamp());
			responseJSONObject.put(i, tmp);
		}
		return responseJSONObject;
	}

	/**
	 * Help doPost() to deal with the "getContent" request.
	 * 
	 * @param request
	 *            It's required to have a JSON with a key "storyID".
	 * @return {content:}
	 */
	private JSONObject do_getContent(HttpServletRequest request) {
		JSONObject responseJSONObject = new JSONObject();
		int storyID = Integer.parseInt(request.getParameter("storyID"));
		// TODO how to get a reasonable content, that is not stemmed?
		responseJSONObject.put("content", corpus.get(storyID).getContent(glossary));
		return responseJSONObject;
	}

}