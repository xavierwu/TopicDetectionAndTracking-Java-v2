package tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Aggregation implements ClusteringInterface {
	private Vector<Story> corpus = null;
	private StoryLinkDetector storyLinkDetector = null;
	double threshold = 0.0;

	public Aggregation(Vector<Story> corpus, StoryLinkDetector storyLinkDetector) {
		this.corpus = corpus;
		this.storyLinkDetector = storyLinkDetector;
	}

	@Override
	public void train(HashMap<String, String> parameters) {
		double threshold = Double.parseDouble(parameters.get("threshold"));
		System.out.println("Parameters: ");
		System.out.println("> threshold = " + threshold);
	}

	/**
	 * Sort the stories in the corpus. From the oldest to the newest, check if
	 * current story is similar to any of the story before, assign the topicID
	 * of the most similar story to the current story.
	 * 
	 * @param simMeasure
	 *            0 for tfidf, 1 for lda.
	 * @param threshold
	 * @return numOfTopics
	 */
	public ArrayList<Integer> doClustering() {
		ArrayList<Integer> partition = new ArrayList<Integer>();
		// sort the stories
		Story.sort(corpus);

		// iteration
		int numOfTopics = 0;
		Story curStory = null;
		Story prevStory = null;
		double similarity = 0.0;
		double tmpMaxSimilarity = 0.0;
		System.out.println("=== aggDetection start");
		for (int i = 0; i < corpus.size(); ++i) {
			if (i % 1000 == 0)
				System.out.println(i + "/" + corpus.size());
			curStory = corpus.get(i);
			tmpMaxSimilarity = -1.0;
			for (int j = 0; j < i; ++j) {
				prevStory = corpus.get(j);
				similarity = storyLinkDetector.getSimilarity(curStory, prevStory);
				if (similarity >= threshold && similarity > tmpMaxSimilarity) {
					curStory.setTopicID(prevStory.getTopicID());
					tmpMaxSimilarity = similarity;
				}
			}

			if (tmpMaxSimilarity < 0) {
				curStory.setTopicID(numOfTopics);
				numOfTopics++;
			}
		}
		System.out.println("=== aggDetection end");
		for (int i = 0; i < corpus.size(); ++i)
			partition.add(corpus.get(i).getTopicID());
		return partition;

	}

}
