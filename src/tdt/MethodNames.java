package tdt;

/**
 * @author Zewei Wu
 *
 */
public enum MethodNames {
	TFIDF_KMeans("", 0, 0, 0),
	LDA_KMeans("", 0, 0, 0),
	pLSA_KMeans("", 0, 0, 0),
	TFIDF_DBSCAN("", 0, 0, 0),
	LDA_DBSCAN("", 0, 0, 0),
	pLSA_DBSCAN("", 0, 0, 0),
	TFIDF_AggDetection("", 0, 0, 0),
	LDA_AggDetection("", 0, 0, 0),
	pLSA_AggDetection("", 0, 0, 0),
	TFIDF_VotingKMeans("", 0, 0, 0),
	LDA_VotingKMeans("", 0, 0, 0),
	pLSA_VotingKMeans("", 0, 0, 0);

	private String name;
	private double bestNormCdet;
	private double bestPMiss;
	private double bestPFa;

	private MethodNames(String name, double bestNormCdet, double bestPMiss, double bestPFa) {
		this.name = name;
		this.bestNormCdet = bestNormCdet;
		this.bestPMiss = bestPMiss;
		this.bestPFa = bestPFa;
	}

	public String getName() {
		return this.name;
	}

	public MethodNames parseMethodName(String methodName) {
		for (MethodNames tmpName : MethodNames.values()) {
			if (tmpName.getName().equalsIgnoreCase(methodName))
				return tmpName;
		}
		return null;
	}

	public double getBestNormCdet() {
		return this.bestNormCdet;
	}

	public double getBestPMiss() {
		return this.bestPMiss;
	}

	public double getBestPFa() {
		return this.bestPFa;
	}
}
