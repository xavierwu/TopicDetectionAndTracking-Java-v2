package tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;

/**
 * This class implement a basic plsa model.
 * 
 * @author Zewei Wu
 */
public class PLSA implements SimilarityInterface {
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

	private int[][] wordPosition = null;

	/**
	 * 
	 * @param corpus
	 * @param glossary
	 */
	public PLSA(Vector<Story> corpus, Glossary glossary) {
		this.corpus = corpus;
		this.glossary = glossary;
	}

	/**
	 * @param numOfTopics
	 * @param maxIter
	 */
	public void train(int numOfTopics, int maxIter) {
		this.numOfTopics = numOfTopics;
		docTermMatrix = new int[corpus.size()][];

		docTopicPros = new double[corpus.size()][numOfTopics];
		topicTermPros = new double[numOfTopics][glossary.size()];

		wordPosition = new int[glossary.size()][corpus.size()];

		docTermTopicPros = new double[corpus.size()][][];

		for (int word = 0; word < glossary.size(); ++word) {
			for (int storyIndex = 0; storyIndex < corpus.size(); ++storyIndex) {
				wordPosition[word][storyIndex] = -1;
			}
		}

		for (int storyIndex = 0; storyIndex < corpus.size(); ++storyIndex) {
			for (int wordIndex = 0; wordIndex < corpus.get(storyIndex).getWords().size(); ++wordIndex) {
				int word = corpus.get(storyIndex).getWords().get(wordIndex);
				wordPosition[word][storyIndex] = wordIndex;
			}
		}

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

		for (int storyIndex = 0; storyIndex < corpus.size(); ++storyIndex) {
			Vector<Integer> words = corpus.get(storyIndex).getWords();
			int[] tf = new int[words.size()];

			Iterator<Entry<Integer, Double>> it = corpus.get(storyIndex).getTfidf().entrySet().iterator();
			int index = 0;
			while (it.hasNext()) {
				Entry<Integer, Double> entry = (Entry<Integer, Double>) it.next();
				tf[index] = entry.getKey();
				++index;
			}

			// index = 0;
			// for (Integer wordIndex: words) {
			// docTermMatrix[storyIndex][wordIndex] += tf[index];
			// ++index;
			// }

			docTermTopicPros[storyIndex] = new double[words.size()][numOfTopics];

			docTermMatrix[storyIndex] = new int[words.size()];
			docTermMatrix[storyIndex] = tf;

			// double[] tfidf = new double[words.size()];
			//
			// Iterator<Entry<Integer, Double>> it =
			// corpus.get(storyIndex).getTfidf().entrySet().iterator();
			// int index = 0;
			// while (it.hasNext()) {
			// Entry<Integer, Double> entry = (Entry<Integer, Double>)
			// it.next();
			// tfidf[index] = entry.getKey() * entry.getValue();
			// ++index;
			// }
		}

		System.out.println("Initialization finished.");

		// use em to estimate params
		for (int i = 0; i < maxIter; i++) {
			System.out.println("em:" + i + "/" + maxIter);
			em();
		}
		System.out.println("done");

		// Set Story.probOfTopics
		for (int storyIndex = 0; storyIndex < corpus.size(); ++storyIndex) {
			ArrayList<Double> probOfTopics = new ArrayList<Double>();
			for (int topicIndex = 0; topicIndex < numOfTopics; ++topicIndex) {
				probOfTopics.add(docTopicPros[storyIndex][topicIndex]);
			}
			corpus.get(storyIndex).setProbOfTopics(probOfTopics);
		}

		// free memory
		docTermMatrix = null;
		topicTermPros = null;
		docTermTopicPros = null;
		System.gc();
	}

	/**
	 * Return the similarity between two stories, using the trained plsa model,
	 * Please make sure the train() is already invoked, before using this
	 * function. Similarity = sum_z (Similarity_z * P(z))
	 * 
	 * 
	 * @see Plsa.getSimilarity(Story, Story, int)
	 * @param story1
	 * @param story2
	 */
	public double getSimilarity(Story story1, Story story2) {
		// return Cosine(docTopicPros[story1.getStoryID()],
		// docTopicPros[story2.getStoryID()]);
		double[] A = new double[numOfTopics];
		double[] B = new double[numOfTopics];

		for (int i = 0; i < story1.getProbOfTopics().size(); ++i) {
			A[i] = story1.getProbOfTopics().get(i).doubleValue();
		}

		for (int i = 0; i < story2.getProbOfTopics().size(); ++i) {
			B[i] = story2.getProbOfTopics().get(i).doubleValue();
		}

		return Cosine(A, B);
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private double Cosine(double[] a, double[] b) {
		double innerProduct = 0.0;
		for (int i = 0; i < a.length; i++)
			innerProduct += a[i] * b[i];

		double aLength = 0.0;
		for (int i = 0; i < a.length; i++)
			aLength += a[i] * a[i];
		aLength = Math.sqrt(aLength);

		double bLength = 0.0;
		for (int i = 0; i < b.length; i++)
			bLength += b[i] * b[i];
		bLength = Math.sqrt(bLength);

		// System.out.println(innerProduct / (aLength * bLength));
		return innerProduct / (aLength * bLength);
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
		// for (int docIndex = 0; docIndex < corpus.size(); docIndex++) {
		// for (int wordIndex = 0; wordIndex < glossary.size(); wordIndex++) {
		// double total = 0.0;
		// double[] perTopicPro = new double[numOfTopics];
		// for (int topicIndex = 0; topicIndex < numOfTopics; topicIndex++) {
		// double numerator = docTopicPros[docIndex][topicIndex] *
		// topicTermPros[topicIndex][wordIndex];
		// total += numerator;
		// perTopicPro[topicIndex] = numerator;
		// }
		//
		// if (total == 0.0) {
		// total = avoidZero(total);
		// }
		//
		// for (int topicIndex = 0; topicIndex < numOfTopics; topicIndex++) {
		// docTermTopicPros[docIndex][wordIndex][topicIndex] =
		// perTopicPro[topicIndex] / total;
		// }
		// }
		// }

		System.out.println("E step.");
		for (int docIndex = 0; docIndex < corpus.size(); docIndex++) {
			for (int wordIndex = 0; wordIndex < corpus.get(docIndex).getWords().size(); wordIndex++) {
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

		System.out.println("M step.");
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

			for (int word = 0; word < glossary.size(); word++) {
				double numerator = 0.0;

				for (int docIndex = 0; docIndex < corpus.size(); docIndex++) {
					int wordIndex = wordPosition[word][docIndex];

					if (wordIndex != -1) {
						numerator += docTermMatrix[docIndex][wordIndex]
								* docTermTopicPros[docIndex][wordIndex][topicIndex];
					}
				}

				topicTermPros[topicIndex][word] = numerator;

				totalDenominator += numerator;
			}

			if (totalDenominator == 0.0) {
				totalDenominator = avoidZero(totalDenominator);
			}

			for (int wordIndex = 0; wordIndex < glossary.size(); wordIndex++) {
				topicTermPros[topicIndex][wordIndex] = topicTermPros[topicIndex][wordIndex] / totalDenominator;
			}
		}

		System.out.println("Updating.");
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

				for (int wordIndex = 0; wordIndex < corpus.get(docIndex).getWords().size(); wordIndex++) {
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
	 * Get a normalize array
	 * 
	 * @param size
	 * @return
	 */
	private double[] randomProbilities(int size) {
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
	 * avoid zero number.if input number is zero, we will return a magic number.
	 */
	private final static double MAGICNUM = 0.0000000000000001;

	public double avoidZero(double num) {
		if (num == 0.0) {
			return MAGICNUM;
		}

		return num;
	}

	public void clear() {

	}

	@Override
	public void train(HashMap<String, String> parameters) {
		// TODO Auto-generated method stub
		
	}

}
