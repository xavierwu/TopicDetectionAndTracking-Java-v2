/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.util.Vector;

/**
 * @author Zewei Wu
 */
public class FirstStoryDetector {
	Vector<Story> corpus = null;

	public FirstStoryDetector(Vector<Story> corpus) {
		this.corpus = corpus;
	}

	// TODO is it reasonable???
	public Vector<Story> doFirstStoryDetection(int numOfTopics) {
		Vector<Story> firstStories = new Vector<Story>();
		for (int curTopic = 0; curTopic < numOfTopics; ++curTopic) {
			Story firstStoryOfCurTopic = null;
			for (Story curStory : corpus) {
				if (curStory.getTopicID() == curTopic) {
					if (firstStoryOfCurTopic == null
							|| curStory.getTimeStamp().compareTo(firstStoryOfCurTopic.getTimeStamp()) < 0)
						firstStoryOfCurTopic = curStory;
				}
			}
			if (firstStoryOfCurTopic != null)
				firstStories.add(firstStoryOfCurTopic);
		}
		return firstStories;
	}
}