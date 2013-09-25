package ax.makila.comparableentititymining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ax.makila.comparableentititymining.postagger.CompTaggedWord;
import ax.makila.comparableentititymining.sequentialpatterns.Sequence;
import ax.makila.comparableentititymining.sequentialpatterns.SequentialPattern;

public class PatternEvaluation {
	public List<Sequence> questionSet = null;
	public List<Pair<CompTaggedWord, CompTaggedWord>> reliablePairRepository = new ArrayList<Pair<CompTaggedWord,CompTaggedWord>>();
	List<Pair<CompTaggedWord, CompTaggedWord>> likelyReliablePairRepository = new ArrayList<Pair<CompTaggedWord,CompTaggedWord>>();
	public SequentialPattern pattern;
	double gamma = MiningIndicativeExtractionPatterns.gamma;
	double lambda = MiningIndicativeExtractionPatterns.lambda;
	double alpha = MiningIndicativeExtractionPatterns.alpha;

	public PatternEvaluation(List<Pair<CompTaggedWord, CompTaggedWord>> pairs,
			SequentialPattern pattern, List<Sequence> questionSet) {
		this.reliablePairRepository = pairs;
		this.pattern = pattern;
		this.questionSet = questionSet;
	}

	/**
	 * A pattern is reliable if it's reliability score is more than a threshold
	 * gamma. The reliability score is determined by formula (1), (2), (3) and
	 * (4) in the paper.
	 * 
	 * @param pattern The pattern to be evaluated
	 * @return true if reliability score for patterns is higher than threshold
	 *         gamma, else false.
	 */
	public boolean isReliable() {
		double score = reliabilityScoreFinal(pattern);
		return score > gamma;
	}

	/**
	 * Using likely reliable pairs, lookahead reliability score R^(pi) is defined
	 * as (3) in the paper.
	 * @param pi The candidate pattern to be evaluated
	 * @return The lookahead reliability score for pattern <tt>pi</tt>
	 */
	public double lookAheadReliabilityScore(SequentialPattern pattern) {
		int cpiExtract = 0;
		int patternContains = 0;
		for (Pair<CompTaggedWord, CompTaggedWord> p : likelyReliablePairRepository) {
			for(Sequence seq : questionSet) {
				List<Pair<CompTaggedWord, CompTaggedWord>> extractedPairs = seq.getPairs(pattern);
				if(extractedPairs.contains(p)) {
					cpiExtract++;
				}
			}
		}
		for(Sequence q : questionSet) {
			if(q.matches(pattern)) {
				patternContains++;
			}
		}
		double score = (double) cpiExtract / (double) patternContains;
		return score;
	}

	/**
	 * NQ(x) means the number of questions satisfying a condition x. The
	 * condition pi -> cpj denotes that cpj can be extracted from a question by
	 * applying pattern pi. If cpj = '*', then the number returned is any
	 * question containing pattern pi.
	 * 
	 * @param pi A reliable pattern that can generate cpj
	 * @param cpj A set of reliable comparator pairs
	 * @return The number of questions that pi can extract cpj from
	 */
	@SuppressWarnings("unused")
	public int numberOfQuestionsSatisfyingCondition(SequentialPattern pi, Pair<CompTaggedWord, CompTaggedWord> cpj) {
		int counter = 0;
		if (cpj.equals("*")) {
			for (Sequence q : questionSet) {
				if (q.matches(pi)) {
					counter++;
				}
			}
		} else {
			for (Sequence q : questionSet) {
				// if(extract(pi, questionSet).equals(cpj)) {
				counter++;
				// }
			}
		}
		return counter;
	}

	/**
	 * Returns a reliability score for a candidate pattern pi at iteration k. Pi
	 * can extract known reliable comparator pairs cpj. This can suffer from
	 * incomplete knowledge about reliable comparator pairs. For example, very
	 * few reliable pairs are generally discovered in early stage of
	 * bootstrapping. In this case, the score might be underestimated which
	 * could affect the effectives of distinguishing IEPs from nonreliable
	 * patterns. This problem is migitated by a lookahead procedure.
	 * 
	 * @see #lookAhead()
	 * @param pi
	 *            The pattern for which the reliability score will be generated
	 * @return The reliability score for pattern pi
	 */
	public double reliabilityScore(SequentialPattern pattern) {
		int cpiExtract = 0;
		int patternContains = 0;
		for (Pair<CompTaggedWord, CompTaggedWord> p : reliablePairRepository) {
			for(Sequence seq : questionSet) {
				List<Pair<CompTaggedWord, CompTaggedWord>> extractedPairs = seq.getPairs(pattern);
				if(extractedPairs.contains(p)) {
					cpiExtract++;
				}
			}
		}
		for(Sequence q : questionSet) {
			if(q.matches(pattern)) {
				patternContains++;
			}
		}
		if(patternContains == 0) {
			//System.out.println(pattern);
		}
		double score = (double) cpiExtract / (double) patternContains;
		return score;
	}

	/**
	 * By interpolating (1) (= {@link #reliabilityScore(String)}) and (3) (= {@link #lookAheadReliabilityScore(String)}), the final reliability score
	 * rkPi for a pattern is defined as below.
	 * @param pi A candidate pattern
	 * @return Pattern <tt>pi</tt>'s reliability score
	 */
	public double reliabilityScoreFinal(SequentialPattern pi) {
		double first = lambda * reliabilityScore(pi);
		double second = (1 - lambda) * lookAheadReliabilityScore(pi);
		return first + second;
	}

	/**
	 * Support is defined as the support S for comparator pair cpi_roof which
	 * can be extracted by P_roof^k and does not exist in the current reliable
	 * set.
	 * @param pattern A candidate pattern which can extract cpi_roof
	 * @param pair The comparator pair which can be extracted by pk_roof and does
	 *            not exist in {@link #reliablePairRepository}
	 * @return The support count for pattern p given the definition above
	 */
	public void support(SequentialPattern pattern) {
		HashMap<Pair<CompTaggedWord, CompTaggedWord>, Integer> hash = new HashMap<Pair<CompTaggedWord, CompTaggedWord>, Integer>();
		for (Sequence q : questionSet) {
			List<Pair<CompTaggedWord, CompTaggedWord>> extractedPairs = q.getPairs(pattern);
			for(Pair<CompTaggedWord, CompTaggedWord> pair : extractedPairs) {
				if (!reliablePairRepository.contains(pair)) {
					if(hash.containsKey(pair)) {
						int i = hash.get(pair);
						i++;
						hash.put(pair, i);
					}
					else {
						hash.put(pair, 1);
					}
				}
			}
		}
		for(Pair<CompTaggedWord, CompTaggedWord> pair : hash.keySet()) {
			int i = hash.get(pair);
			if(i > alpha) {
				likelyReliablePairRepository.add(pair);
			}
		}
	}
}
