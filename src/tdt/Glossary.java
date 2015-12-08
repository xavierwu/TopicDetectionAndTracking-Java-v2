/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
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

	public int getDocumentCount(int wordID) {
		return documentCount.get(wordID);
	}

	public void raiseDocumentCount(int wordID) {
		if (documentCount.containsKey(wordID)) {
			documentCount.put(wordID, documentCount.get(wordID) + 1);
		} else {
			documentCount.put(wordID, 1);
		}
	}

	public void insertWord(String word) {
		if (!glossaryStringToInt.containsKey(word)) {
			int index = size();
			glossaryIntToString.put(index, word);
			glossaryStringToInt.put(word, index);
			documentCount.put(index, 0);
		}
	}

	public void save(String glossaryFile) {
		System.out.println("Start saving glossary...");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(glossaryFile));
			for (Integer wordID : glossaryIntToString.keySet())
				writer.append(wordID.toString() + " " + String.format("%.4f", idf.get(wordID)) + " "
						+ glossaryIntToString.get(wordID) + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done!");
	}

	public void load(String glossaryFile) {
		System.out.println("Loading glossary from: " + glossaryFile);
		BufferedReader reader = null;
		String line = null;
		try {
			reader = new BufferedReader(new FileReader(glossaryFile));
			glossaryIntToString.clear();
			glossaryStringToInt.clear();
			documentCount.clear();
			idf.clear();
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(" ");
				if (parts.length == 3) {
					int wordID = Integer.parseInt(parts[0]);
					double tmpIdf = Double.parseDouble(parts[1]);
					String word = parts[2];
					glossaryIntToString.put(wordID, word);
					glossaryStringToInt.put(word, wordID);
					idf.put(wordID, tmpIdf);
				}else if (parts.length == 2){
					int wordID = Integer.parseInt(parts[0]);
					double tmpIdf = Double.parseDouble(parts[1]);
					String word = " ";
					glossaryIntToString.put(wordID, word);
					glossaryStringToInt.put(word, wordID);
					idf.put(wordID, tmpIdf);
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done!");
	}

	public void calculateIDF(int numOfDocuments) {
		double tmp = Math.log(2);
		for (int wordID : documentCount.keySet()) {
			idf.put(wordID, Math.log(numOfDocuments / (double) documentCount.get(wordID)) / tmp);
		}
	}

	public double getIDF(int wordID) {
		return idf.get(wordID);
	}

	private HashMap<Integer, String> glossaryIntToString = new HashMap<Integer, String>();
	private HashMap<String, Integer> glossaryStringToInt = new HashMap<String, Integer>();
	private HashMap<Integer, Integer> documentCount = new HashMap<Integer, Integer>();
	private HashMap<Integer, Double> idf = new HashMap<Integer, Double>();
}
