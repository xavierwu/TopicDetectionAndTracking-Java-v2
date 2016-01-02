/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.io.BufferedReader;
import java.io.FileReader;
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
	// Important data structures.
	private Vector<Story> corpus = new Vector<Story>();
	private Glossary glossary = new Glossary();
	private Vector<Story> actualFirstStories = new Vector<Story>();
	private Vector<Story> firstStories = new Vector<Story>();
	TopicDetector topicDetector = new TopicDetector(corpus, glossary);
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
	public Main(String dataFilesdir, String glossaryFile, String tfidfFile, String matrixFile, String ansFile) {
		this.dataFilesdir = dataFilesdir;
		System.out.println("\n====== Start initializing ======");

		glossary.load(glossaryFile);
		DataPreprocessor.recoverCorpusFromTFIDF(corpus, tfidfFile);
		DataPreprocessor.loadMatrix(corpus, matrixFile);
		DataPreprocessor.readAnswer_v2(actualFirstStories, ansFile);

		System.out.println("corpus: " + corpus.size());
		assert(corpus.size() > 0);
		System.out.println("glossary: " + glossary.size());
		assert(glossary.size() > 0);
		System.out.println("actualFirstStories: " + actualFirstStories.size());
		assert(actualFirstStories.size() > 0);

		System.out.println("====== Done initializing ======\n");
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

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
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
		JSONObject responseJSONObject = topicDetector.getMethodList();
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
		JSONObject responseJSONObject = topicDetector.prepareTopicDetection(methodID);
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
		MethodName methodName = MethodName.valueOf(methodID);
		if (methodName == MethodName.Original_Subtopic || methodName == MethodName.Original_Subtopic_Weight
		|| methodName == MethodName.Improved_Subtopic || methodName == MethodName.Improved_Subtopic_Weight
		|| methodName == MethodName.Improved_Subtopic_Weight_Agg) {
			normCdet = methodName..getBestNormCdet();
			PMiss = methodName.getBestPMiss();
			PFa = methodName.getBestPFa();
			System.out.println("normCdet = " + normCdet);
			System.out.println("PMiss = " + PMiss);
			System.out.println("PFa = " + PFa);
			System.out.println("=== Evaluation End");
			System.out.println();

			responseJSONObject.put("normCdet", normCdet);
			responseJSONObject.put("PMiss", PMiss);
			responseJSONObject.put("PFa", PFa);

			try {
				BufferedReader reader = new BufferedReader(new FileReader(this.dataFilesdir + "/../" + methodName.getName() + ".dat"));
				String line = null;
				int myRow = 0;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(" ");
					if (parts.length == 1) {
						break;
					}
					firstStories.add(corpus.get(Integer.parseInt(parts[1])));
					for (int i = 2; i < parts.length - 1; ++i) {
						// corpus.get(storyCount).addWord(Integer.parseInt(parts[i]));
						corpus.get(Integer.parseInt(parts[i])).setTopicID(myRow);
					}
					myRow++;
				}
				System.out.println("Done!");
				reader.close();
			} catch (Exception e) {
				System.out.println("Exception!");
				e.printStackTrace();
			}

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
		} else {
			System.out.println();
			System.out.println("=== Topic Detection Start");
			numOfTopics = topicDetector.doTopicDetection(request);
			System.out.println("numOfTopics = " + numOfTopics);
			System.out.println("=== Topic Detection End");
			System.out.println();

			System.out.println();
			System.out.println("=== First Story Detection Start");
			FirstStoryDetector firstStoryDetector = new FirstStoryDetector(corpus);
			firstStories = firstStoryDetector.doFirstStoryDetection(numOfTopics);
			System.out.println("firstStories: " + firstStories.size());
			System.out.println("actualFirstStories: " + actualFirstStories.size());
			System.out.println("=== First Story Detection End");
			System.out.println();

			System.out.println();
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
			System.out.println();

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
