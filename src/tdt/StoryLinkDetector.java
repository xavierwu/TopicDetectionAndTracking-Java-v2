/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

/**
 * This class is used to calculate the similarity between two stories. After
 * creating an instance, use the doStoryLinkDetection() firstly to detect the
 * links among stories of the corpus, so that you could use the other methods
 * normally, e.g., getCosineSimilarity().
 * 
 * @author Zewei Wu
 */
public class StoryLinkDetector {
	private boolean isTrained = false;
	private SimilarityInterface similarityInterface = null;

	public StoryLinkDetector(SimilarityName similarityName,
			Vector<Story> corpus, Glossary glossary) {
		this.isTrained = false;
		this.similarityInterface = similarityName.getSimilarityInterface(corpus,
				glossary);
	}

	public void train(HttpServletRequest request) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			parameters.put(name, request.getParameter(name));
		}
		this.similarityInterface.train(parameters);
		this.isTrained = true;
	}

	public double getSimilarity(Story story1, Story story2) {
		if (this.isTrained) {
			return this.similarityInterface.getSimilarity(story1, story2);
		} else {
			return 0.0;
		}
	}

	/**
	 * @param story1
	 * @param story2
	 * @return
	 */
	public static double getJaccardSimilarity(Story story1, Story story2) {
		double similarity = 0.0;
		double commonWords = 0.0;

		Vector<Integer> words1 = story1.getWords();
		Vector<Integer> words2 = story2.getWords();

		for (int i = 0; i < words1.size(); ++i) {
			for (int j = 0; j < words2.size(); ++j) {
				if (words1.get(i).equals(words2.get(j))) {
					commonWords += 2;
					break;
				}
			}
		}

		similarity = commonWords / ((double) (words1.size() + words2.size()));

		return similarity;
	}

	/**
	 * @deprecated TODO
	 * @return
	 */
	public boolean isUsingLDA() {
		return false;
	}

	/**
	 * @deprecated TODO
	 * @return
	 */
	public boolean isUsingPLSA() {
		return false;
	}

}
