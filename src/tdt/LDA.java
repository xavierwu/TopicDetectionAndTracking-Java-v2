package tdt;

import java.util.Random;
import java.util.Vector;

public class LDA {

	private Vector<Story> corpus = null;
	private Glossary glossary = null;

	private int numOfTopics = 0; // To be initialized
	private int numOfIterations = 0; // To be initialized

	private double LAMBDA = 0.0;
	// private double DELTA = 0.0;
	private double ALPHA = 0.0;
	private double BETA = 0.0;

	private int[][] words_of_matrix = null;
	private int[] num_of_every_topic = null;
	private int[][] topics_of_matrix = null; // the topics for each token
												// corresponding to matrix;
	private int[][] word_topic_matrix = null; // num_of_unique_words *
												// num_of_topics
	private double[][] prob_topic_word_matrix = null; // num_of_topics x
														// num_of_unique_words
	// private double[] similarities = null;
	private double[] prob_of_topics = null; // the probability array for each
											// topic over all topics

	public LDA(Vector<Story> corpus, Glossary glossary) {
		this.corpus = corpus;
		this.glossary = glossary;
	}

	/**
	 * Extracting topics
	 */
	public void train(int numOfTopics, int numOfIterations, double LAMBDA,
			// double DELTA,
			double ALPHA, double BETA) {
		this.numOfTopics = numOfTopics;
		this.numOfIterations = numOfIterations;
		this.ALPHA = ALPHA;
		this.BETA = BETA;
		// this.DELTA = DELTA;
		this.LAMBDA = LAMBDA;

		word_topic_matrix = new int[glossary.size()][numOfTopics]; // wordTopic
		num_of_every_topic = new int[numOfTopics]; //
		prob_topic_word_matrix = new double[numOfTopics][glossary.size()]; //
		prob_of_topics = new double[numOfTopics];
		// similarities = new double[corpus.size()];
		words_of_matrix = new int[corpus.size()][]; // [storyID][wordIndexOfTheStory]
													// = wordID(glossary)
		topics_of_matrix = new int[corpus.size()][];

		System.out.println("Start extracting topics...");

		System.out.println("Start setting matrix...");
			for (int storyIndex = 0; storyIndex < corpus.size(); ++storyIndex) {
				Vector<Integer> content = corpus.get(storyIndex).getWords();
				System.out.println("# "+content.size());
				words_of_matrix[storyIndex] = new int[content.size()];
				topics_of_matrix[storyIndex] = new int[content.size()];
				for (int wordIndex = 0; wordIndex < content.size(); ++wordIndex) {
					System.out.println("## "+wordIndex);
					words_of_matrix[storyIndex][wordIndex] = content.get(wordIndex);
				}
			}
		
		System.out.println("Setting matrix done.");

		System.out.println("Start Gibbs sampling...");
		GibbsSamplerLDA();
		System.out.println("Gibbs Sampling done.");

		System.out.println("Extracting topics done.");
	}

	public double getSimilarity(Story story1, Story story2) {
		double sim = 0.0;

		for (int i = 0; i < numOfTopics; i++)
			sim += SimilarityOnEachTopic(story1, story2, i) * this.prob_of_topics[i];

		return sim;
	}

	// compute the similarity between two documents over one topic
	public double SimilarityOnEachTopic(Story story1, Story story2, int topicIndex) {
		double[] A = new double[glossary.size()];
		double[] B = new double[glossary.size()];

		for (int i = 0; i < story1.getWords().size(); i++) {
			int i_word = story1.getWords().get(i);

			if (i_word > -1) {
				A[i_word] = prob_topic_word_matrix[topicIndex][i_word];
			}
		}

		for (int i = 0; i < story2.getWords().size(); i++) {
			int i_word = story2.getWords().get(i);

			if (i_word > -1) {
				B[i_word] = prob_topic_word_matrix[topicIndex][i_word];
			}
		}

		return Cosine(A, B);
	}

	public double Cosine(double[] a, double[] b) {
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

		return innerProduct / (aLength * bLength);
	}

	/**
	 * Gibbs sampling Set topics_of_matrix[][] Set word_topic_matrix[,],
	 * document_topic_matrix[,]
	 */
	private void GibbsSamplerLDA() {
		int[][] document_topic_matrix = new int[corpus.size()][this.numOfTopics];
		Random random = new Random();

		// Randomly set topics for unique words and documents
		for (int i = 0; i < corpus.size(); i++)
			for (int j = 0; j < words_of_matrix[i].length; j++) {
				// pick a random topic, marked as 0 to num_of_topics-1
				int tmp_topic = (int) (numOfTopics * random.nextDouble());
				topics_of_matrix[i][j] = tmp_topic;
				word_topic_matrix[words_of_matrix[i][j]][tmp_topic]++;
				document_topic_matrix[i][tmp_topic]++;
				this.num_of_every_topic[tmp_topic]++;
			}

		// Randomly decide the order of documents to be sampled.
		int[] order = new int[corpus.size()];
		for (int i = 0; i < corpus.size(); i++)
			order[i] = i;
		for (int i = 0; i < (corpus.size() - 1); i++) {
			// pick a random integer between i and nw
			int rp = i + (int) ((corpus.size() - i) * random.nextDouble());
			int temp = order[rp];
			order[rp] = order[i];
			order[i] = temp;
		}

		double[] probs = new double[numOfTopics];
		for (int iter = 0; iter < numOfIterations; iter++) {
			if (iter % 10 == 0)
				System.out.println(iter + " " + numOfIterations);
			for (int _i = 0; _i < corpus.size(); _i++) {
				int cur_doc = order[_i]; // current document
				for (int j = 0; j < words_of_matrix[cur_doc].length; j++) {
					int cur_word = words_of_matrix[cur_doc][j]; // current word
					// remove current topic
					int tmp_topic = topics_of_matrix[cur_doc][j];
					word_topic_matrix[cur_word][tmp_topic]--;
					num_of_every_topic[tmp_topic]--;
					document_topic_matrix[cur_doc][tmp_topic]--;

					// Calculate probabilities.
					double total_prob = 0.0;
					if (j == 0) { // for the first word of the document
						for (int k = 0; k < numOfTopics; k++) {
							probs[k] = (word_topic_matrix[cur_word][k] + BETA)
									* (document_topic_matrix[cur_doc][k] + ALPHA)
									/ (num_of_every_topic[k] + glossary.size() * BETA);
							total_prob += probs[k];
						}
					} else {
						for (int k = 0; k < numOfTopics; k++) {
							int prev_word = words_of_matrix[cur_doc][j - 1];
							probs[k] = LAMBDA
									* ((word_topic_matrix[prev_word][k] + BETA)
											* (document_topic_matrix[cur_doc][k] + ALPHA)
											/ (num_of_every_topic[k] + glossary.size() * BETA))
									+ (word_topic_matrix[cur_word][k] + BETA)
											* (document_topic_matrix[cur_doc][k] + ALPHA)
											/ (num_of_every_topic[k] + glossary.size() * BETA);
							total_prob += probs[k];
						}
					}

					// sample a topic from the distribution
					double r = total_prob * random.nextDouble();
					double max = probs[0];
					tmp_topic = 0;
					while (max < r) {
						tmp_topic++;
						max += probs[tmp_topic];
					}

					topics_of_matrix[cur_doc][j] = tmp_topic;
					word_topic_matrix[words_of_matrix[cur_doc][j]][tmp_topic]++;
					document_topic_matrix[cur_doc][tmp_topic]++;
					num_of_every_topic[tmp_topic]++;
				}
			}
		}
	}
}
