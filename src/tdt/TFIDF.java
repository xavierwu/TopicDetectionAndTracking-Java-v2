package tdt;

import java.util.HashMap;
import java.util.Vector;
import java.util.Map.Entry;

//TODO
public class TFIDF implements SimilarityInterface {
	public TFIDF(Vector<Story> corpus, Glossary glossary) {

	}

	@Override
	public void train(HashMap<String, String> parameters) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getSimilarity(Story story1, Story story2) {
		return getCosineSimilarity(story1, story2);
	}

	/**
	 * @param story1
	 * @param story2
	 * @return the cosine similarity between two stories, using the tf-idf
	 *         vectors in them.
	 */
	public static double getCosineSimilarity(Story story1, Story story2) {
		double similarity = 0.0;
		double innerProduct = 0.0;
		double squareSum1 = 0.0;
		double squareSum2 = 0.0;

		HashMap<Integer, Double> tfidf1 = story1.getTfidf();
		HashMap<Integer, Double> tfidf2 = story2.getTfidf();

		for (Entry<Integer, Double> entry : tfidf1.entrySet()) {
			int key = entry.getKey();
			double value = entry.getValue();
			if (tfidf2.containsKey(key))
				innerProduct += value * tfidf2.get(key);
			squareSum1 += value * value;
		}
		for (Entry<Integer, Double> entry : tfidf2.entrySet())
			squareSum2 += entry.getValue() * entry.getValue();

		if (Double.compare(innerProduct, 0.0) == 0)
			return 0.0;
		double tmp1 = Math.sqrt(squareSum1 * squareSum2);
		similarity = innerProduct / tmp1;

		return similarity;
	}

}
