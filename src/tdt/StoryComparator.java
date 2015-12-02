package tdt;

import java.util.Comparator;

public class StoryComparator implements Comparator<Story> {
	@Override
	public int compare(Story story1, Story story2) {
		return story1.getTimeStamp().compareTo(story2.getTimeStamp());
	}
}
