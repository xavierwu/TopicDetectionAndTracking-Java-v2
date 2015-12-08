package tdt;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

/**
 * This class implement a basic plsa model.
 * 
 * @author Zewei Wu
 */
public class Plsa {
	private Vector<Story> corpus = null;
	private Glossary glossary = null;
	private int numOfTopics = 0;
	// d * w
	private int[][] docTermMatrix = null;
	// p(z|d), d * z
	private double[][] docTopicPros = null;
	// p(w|z), z * w
	private double[][] topicTermPros = null;//
	// p(z|d,w), d * w * z
	private double[][][] docTermTopicPros = null;
	private double[] probOfTopic = null;//
	
	/**
	 * 
	 * @param corpus
	 * @param glossary
	 */
	public Plsa(Vector<Story> corpus, Glossary glossary) {
		this.corpus = corpus;
		this.glossary = glossary;
	}

	/**
	 * TODO train()
	 * 
	 * @param numOfTopics
	 * @param maxIter
	 */
	public void train(int numOfTopics, int maxIter) {
		this.numOfTopics = numOfTopics;
		docTermMatrix = new int[corpus.size()][glossary.size()];
		docTopicPros = new double[corpus.size()][numOfTopics];
		topicTermPros = new double[numOfTopics][glossary.size()];
		docTermTopicPros = new double[corpus.size()][glossary.size()][numOfTopics];
		probOfTopic = new double[numOfTopics];
	}

	/**
	 * Return the similarity between two stories, using the trained plsa model,
	 * Please make sure the train() is already invoked, before using this
	 * function. Similairty = sum_z (Similarity_z * P(z)) TODO
	 * getSimilarity(Story, Story)
	 * 
	 * @see Plsa.getSimilarity(Story, Story, int)
	 * @param story1
	 * @param story2
	 */
	public double getSimilarity(Story story1, Story story2) {
		// Similairty = sum_z (Similarity_z * P(z))
		double similarity = 0.0;
		for (int curTopic = 0; curTopic < numOfTopics; ++curTopic) {
			similarity += getSimilarity(story1, story2, curTopic) * probOfTopic[curTopic];
		}
		return similarity;
	}

	/**
	 * Return the similarity of two stories, based on a certain topic.
	 * Similarity_z = cosineSimilarity( P(w | z), P(w | z) )
	 * 
	 * @param story1
	 * @param story2
	 * @param topic
	 * @return
	 */
	private double getSimilarity(Story story1, Story story2, int topicID) {
		double innerProduct = 0.0;
		double squareSum1 = 0.0;
		double squareSum2 = 0.0;
		int i = 0; // TODO how to get the id of story1 in the corpus?
		int j = 0; // the id of story2 in the corpus
		for (int curTopic = 0; curTopic < numOfTopics; ++curTopic) {
			innerProduct += docTopicPros[i][curTopic] * docTopicPros[j][curTopic];
			squareSum1 += docTopicPros[i][curTopic] * docTopicPros[i][curTopic];
			squareSum2 += docTopicPros[j][curTopic] * docTopicPros[j][curTopic];
		}
		double result = innerProduct / Math.sqrt(squareSum1 * squareSum2);
		return result;
	}
	// ----------------- codes below are copied from somewhere...

	/**
	 * train plsa
	 * 
	 * @deprecated
	 * @param corpus
	 *            all documents
	 */
	public void train(Vector<Story> corpus, int maxIter) {
		if (corpus == null) {
			throw new IllegalArgumentException("The documents set must be not null!");
		}

		// statistics vocabularies
		// allWords = statisticsVocabularies(corpus);

		// element represent times the word appear in this document
		docTermMatrix = new int[corpus.size()][glossary.size()];
		// init docTermMatrix
		// for (int docIndex = 0; docIndex < corpus.size(); docIndex++) {
		// Document doc = corpus.get(docIndex);
		// for (String word : doc.getWords()) {
		// if (allWords.contains(word)) {
		// int wordIndex = allWords.indexOf(word);
		// docTermMatrix[docIndex][wordIndex] += 1;
		// }
		// }
		//
		// // free memory
		// doc.setWords(null);
		// }

		docTopicPros = new double[corpus.size()][numOfTopics];
		topicTermPros = new double[numOfTopics][glossary.size()];
		docTermTopicPros = new double[corpus.size()][glossary.size()][numOfTopics];

		// init p(z|d),for each document the constraint is sum(p(z|d))=1.0
		for (int i = 0; i < corpus.size(); i++) {
			double[] pros = randomProbilities(numOfTopics);
			for (int j = 0; j < numOfTopics; j++) {
				docTopicPros[i][j] = pros[j];
			}
		}
		// init p(w|z),for each topic the constraint is sum(p(w|z))=1.0
		for (int i = 0; i < numOfTopics; i++) {
			double[] pros = randomProbilities(glossary.size());
			for (int j = 0; j < glossary.size(); j++) {
				topicTermPros[i][j] = pros[j];
			}
		}

		// use em to estimate params
		for (int i = 0; i < maxIter; i++) {
			em();
			System.out.print(i + "-");
		}
		System.out.println("done");
	}

	/**
	 * EM algorithm
	 */
	private void em() {
		/*
		 * E-step,calculate posterior probability p(z|d,w,&),& is model
		 * params(p(z|d),p(w|z))
		 * 
		 * p(z|d,w,&)=p(z|d)*p(w|z)/sum(p(z'|d)*p(w|z')) z' represent all
		 * posible topic
		 * 
		 */
		for (int docIndex = 0; docIndex < corpus.size(); docIndex++) {
			for (int wordIndex = 0; wordIndex < glossary.size(); wordIndex++) {
				double total = 0.0;
				double[] perTopicPro = new double[numOfTopics];
				for (int topicIndex = 0; topicIndex < numOfTopics; topicIndex++) {
					double numerator = docTopicPros[docIndex][topicIndex] * topicTermPros[topicIndex][wordIndex];
					total += numerator;
					perTopicPro[topicIndex] = numerator;
				}

				if (total == 0.0) {
					total = avoidZero(total);
				}

				for (int topicIndex = 0; topicIndex < numOfTopics; topicIndex++) {
					docTermTopicPros[docIndex][wordIndex][topicIndex] = perTopicPro[topicIndex] / total;
				}
			}
		}

		// M-step
		/*
		 * update
		 * p(w|z),p(w|z)=sum(n(d',w)*p(z|d',w,&))/sum(sum(n(d',w')*p(z|d',w',&))
		 * )
		 * 
		 * d' represent all documents w' represent all vocabularies
		 * 
		 * 
		 */
		for (int topicIndex = 0; topicIndex < numOfTopics; topicIndex++) {
			double totalDenominator = 0.0;
			for (int wordIndex = 0; wordIndex < glossary.size(); wordIndex++) {
				double numerator = 0.0;
				for (int docIndex = 0; docIndex < corpus.size(); docIndex++) {
					numerator += docTermMatrix[docIndex][wordIndex] * docTermTopicPros[docIndex][wordIndex][topicIndex];
				}

				topicTermPros[topicIndex][wordIndex] = numerator;

				totalDenominator += numerator;
			}

			if (totalDenominator == 0.0) {
				totalDenominator = avoidZero(totalDenominator);
			}

			for (int wordIndex = 0; wordIndex < glossary.size(); wordIndex++) {
				topicTermPros[topicIndex][wordIndex] = topicTermPros[topicIndex][wordIndex] / totalDenominator;
			}
		}
		/*
		 * update
		 * p(z|d),p(z|d)=sum(n(d,w')*p(z|d,w'&))/sum(sum(n(d,w')*p(z'|d,w',&)))
		 * 
		 * w' represent all vocabularies z' represnet all topics
		 * 
		 */
		for (int docIndex = 0; docIndex < corpus.size(); docIndex++) {
			// actually equal sum(w) of this doc
			double totalDenominator = 0.0;
			for (int topicIndex = 0; topicIndex < numOfTopics; topicIndex++) {
				double numerator = 0.0;
				for (int wordIndex = 0; wordIndex < glossary.size(); wordIndex++) {
					numerator += docTermMatrix[docIndex][wordIndex] * docTermTopicPros[docIndex][wordIndex][topicIndex];
				}
				docTopicPros[docIndex][topicIndex] = numerator;
				totalDenominator += numerator;
			}

			if (totalDenominator == 0.0) {
				totalDenominator = avoidZero(totalDenominator);
			}

			for (int topicIndex = 0; topicIndex < numOfTopics; topicIndex++) {
				docTopicPros[docIndex][topicIndex] = docTopicPros[docIndex][topicIndex] / totalDenominator;
			}
		}
	}

	/**
	 * @deprecated
	 * @param corpus
	 * @return
	 */
	private List<String> statisticsVocabularies(Vector<Story> corpus) {
		Set<String> uniqWords = new HashSet<String>();
		// for (Document doc : corpus) {
		// for (String word : doc.getWords()) {
		// if (!uniqWords.contains(word)) {
		// uniqWords.add(word);
		// }
		// }
		// corpus.size()++;
		// }
		// glossary.size() = uniqWords.size();

		return new LinkedList<String>(uniqWords);
	}

	/**
	 * Get a normalize array
	 * 
	 * @param size
	 * @return
	 */
	public double[] randomProbilities(int size) {
		if (size < 1) {
			throw new IllegalArgumentException("The size param must be greate than zero");
		}
		double[] pros = new double[size];

		int total = 0;
		Random r = new Random();
		for (int i = 0; i < pros.length; i++) {
			// avoid zero
			pros[i] = r.nextInt(size) + 1;

			total += pros[i];
		}

		// normalize
		for (int i = 0; i < pros.length; i++) {
			pros[i] = pros[i] / total;
		}

		return pros;
	}

	/**
	 * @return
	 */
	public double[][] getDocTopics() {
		return docTopicPros;
	}

	/**
	 * @return
	 */
	public double[][] getTopicWordPros() {
		return topicTermPros;
	}

	/**
	 * Get topic number
	 * 
	 * @return
	 */
	public Integer getTopicNum() {
		return numOfTopics;
	}

	/**
	 * Get p(w|z)
	 * 
	 * @param word
	 * @return
	 */
	public double[] getTopicWordPros(String word) {
		// int index = allWords.indexOf(word);
		// if (index != -1) {
		// double[] topicWordPros = new double[numOfTopics];
		// for (int i = 0; i < numOfTopics; i++) {
		// topicWordPros[i] = topicTermPros[i][index];
		// }
		// return topicWordPros;
		// }
		//
		return null;
	}

	/**
	 * avoid zero number.if input number is zero, we will return a magic number.
	 */
	private final static double MAGICNUM = 0.0000000000000001;

	public double avoidZero(double num) {
		if (num == 0.0) {
			return MAGICNUM;
		}

		return num;
	}

}
