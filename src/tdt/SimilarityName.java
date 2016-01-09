package tdt;

import java.util.Vector;

public enum SimilarityName {
	TFIDF, pLSA, LDA;

	public SimilarityInterface getSimilarityInterface(Vector<Story> corpus, Glossary glossary) {
		switch (this) {
		case TFIDF:
			return new TFIDF(corpus, glossary);
		case pLSA:
			return new PLSA(corpus, glossary);
		case LDA:
			return new LDA(corpus, glossary);
		default:
			return null;
		}
	}
}
