/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Zitong Wang
 */
public class Glossary {

	public Glossary() {
	}

	public int size() {
		return glossaryStringToInt.size();
	}

	public boolean containsWordID(int wordID) {
		return glossaryIntToString.containsKey(wordID);
	}

	public boolean containsWord(String word) {
		return glossaryStringToInt.containsKey(word);
	}

	public String getWord(int wordID) {
		return glossaryIntToString.get(wordID);
	}

	public Integer getWordID(String word) {
		return glossaryStringToInt.get(word);
	}

	public int getIDF(int wordID) {
		return idf.get(wordID);
	}

	public void raiseIDF(int wordID) {
		idf.put(wordID, idf.get(wordID) + 1);
	}

	public void insertWord(String word) {
		if (!glossaryStringToInt.containsKey(word)) {
			int index = size();
			glossaryIntToString.put(index, word);
			glossaryStringToInt.put(word, index);
			idf.put(index, 1);
		}
	}

	public void save(String glossaryFile) {
		System.out.println("Start saving glossary...");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(glossaryFile));
			for (Integer wordID : glossaryIntToString.keySet())
				writer.append(wordID.toString() + " " + idf.get(wordID) + " " + glossaryIntToString.get(wordID) + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done!");
	}

	private HashMap<Integer, String> glossaryIntToString = new HashMap<Integer, String>();
	private HashMap<String, Integer> glossaryStringToInt = new HashMap<String, Integer>();
	private HashMap<Integer, Integer> idf = new HashMap<Integer, Integer>();
}
