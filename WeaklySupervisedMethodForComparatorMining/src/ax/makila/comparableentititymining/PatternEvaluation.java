package ax.makila.comparableentititymining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ax.makila.comparableentititymining.postagger.CompTaggedWord;
import ax.makila.comparableentititymining.sequentialpatterns.Sequence;
import ax.makila.comparableentititymining.sequentialpatterns.SequentialPattern;
import edu.stanford.nlp.util.Timing;

public class PatternEvaluation {
	public List<Sequence> questionSet = null;
	public Set<Pair<CompTaggedWord, CompTaggedWord>> reliablePairRepository = new HashSet<Pair<CompTaggedWord,CompTaggedWord>>();
	public Set<Pair<CompTaggedWord, CompTaggedWord>> likelyReliablePairRepository = new HashSet<Pair<CompTaggedWord,CompTaggedWord>>();
	public List<SequentialPattern> patternSet;
	/**
	 * Threshold value for the reliability score of a pattern.
	 */
	public static final double gamma = 0.4;
	/**
	 * Used as interpolating variable when calculating reliability score.
	 */
	public static final double lambda = 0.5;
	/**
	 * Used as a threshold for the support of likely reliable comparator pairs.
	 */
	public static final double alpha = 0.0;

	/**
	 * Constructor.
	 * @param pairs The pairs to be considered reliable pairs
	 * @param patterns The patterns to be evaluated
	 * @param questionSet The complete question set of all questions.
	 */
	public PatternEvaluation(Set<Pair<CompTaggedWord, CompTaggedWord>> pairs,
			Set<SequentialPattern> patterns, List<Sequence> questionSet) {
		this.reliablePairRepository = pairs;
		this.patternSet = new ArrayList<SequentialPattern>(patterns);
		this.questionSet = questionSet;

		support(patternSet);
	}

	/**
	 * Gets the current pattern at <tt>index</tt>
	 * 
	 * @param index The index from where to get the pattern from
	 * @return The pattern at index
	 */
	public SequentialPattern get(int index) {
		return patternSet.get(index);
	}


	/**
	 * Returns true if a pattern is reliable, else false. A pattern is reliable if it's
	 * reliability score is more than a threshold {@value #gamma}. The reliability score
	 * is determined by formula (1), (2), (3) and (4) in the paper.
	 * 
	 * @param pattern The pattern to be evaluated.
	 * @return true if reliability score for patterns is higher than threshold else false.
	 */
	public boolean isReliable(int index) {
		SequentialPattern pat = get(index);
		double score = reliabilityScore(pat);
		return score > gamma;
	}

	/**
	 * Returns a reliability score for a candidate <tt>pattern</tt> at iteration
	 * k. <tt>pattern</tt> can extract known reliable comparator pairs in
	 * {@link #reliablePairRepository}. This can suffer
	 * from incomplete knowledge about reliable comparator pairs. For example, very
	 * few reliable pairs are generally discovered in early stage of
	 * bootstrapping. In this case, the score might be underestimated which
	 * could affect the effectives of distinguishing IEPs from nonreliable
	 * patterns. This problem is migitated by a lookahead procedure.
	 * 
	 * Using likely reliable pairs, lookahead reliability score is defined
	 * as the number of likely pairs in {@link #likelyReliablePairRepository} that the
	 * pattern can extract. These pairs are generated through the support function
	 * {@link #support(List)}.
	 * 
	 * By interpolating the reliability score and the lookahead reliability score
	 * the final reliability score can be given as
	 * lambda*reliabilityScore + (1-lambda)*lookAheadScore. Lambda {@link #lambda} has
	 * been set empirically.
	 * 
	 * @param pattern
	 *            The pattern for which the reliability score will be generated
	 * @return The reliability score for the pattern
	 */
	public double reliabilityScore(SequentialPattern pattern) {
		double counterRel = 0;
		double counterAll = 0;
		double counterLik = 0;

		for(Sequence seq : questionSet) {
			List<Pair<CompTaggedWord, CompTaggedWord>> extracted = seq.getPairs(pattern);
			if(extracted != null) {
				for(Pair<CompTaggedWord, CompTaggedWord> p : extracted) {
					for(Pair<CompTaggedWord, CompTaggedWord> pair : reliablePairRepository) {
						if(pair.equals(p)) {
							counterRel++;
						}
					}
					for(Pair<CompTaggedWord, CompTaggedWord> pair : likelyReliablePairRepository) {
						if(pair.equals(p)) {
							counterLik++;
						}
					}
				}
			}
			if(seq.matches(pattern)) {
				counterAll++;
			}
		}

		if(counterAll == 0) {
			return 0;
		}
		else {
			double relScore = counterRel / counterAll;
			double lookAheadScore = counterLik / counterAll;
			return lambda * relScore + ((1 - lambda) * lookAheadScore);
		}
	}


	/**
	 * Support is defined as the support S for comparator pair cp in which
	 * can be extracted by a pattern in <tt>patterns</tt> and does not exist in the
	 * current reliable set, {@link #reliablePairRepository}. Pairs that occur
	 * more frequent than a threshold {@value #alpha} are added to to
	 * {@link #likelyReliablePairRepository}.
	 * 
	 * @param patterns A list of A candidate patterns
	 */
	@SuppressWarnings("static-access")
	public void support(List<SequentialPattern> patterns) {
		Timing t = new Timing();
		t.startDoing("Calculating support");
		Map<Pair<CompTaggedWord, CompTaggedWord>, Integer> candidates = new HashMap<Pair<CompTaggedWord, CompTaggedWord>, Integer>();
		double counter = 0;
		double total = patterns.size();
		int steps = (int) total/10;
		for(SequentialPattern seq : patterns) {
			if(counter % steps == 0 || counter == total - 1) {
				int percent = (int) ((counter/(total-1))*100);
				System.out.print(percent + "% ");
			}
			counter++;
			int rand = 1 + (int)(Math.random() * ((Math.min(1000, questionSet.size()) - 1) + 1));
			for(int i = 0; i < questionSet.size(); i += rand) {
				if(rand > questionSet.size()) {
					break;
				}
				Sequence q = questionSet.get(i);
				rand = 1 + (int)(Math.random() * ((Math.min(1000, questionSet.size()) - 1) + 1));
				List<Pair<CompTaggedWord, CompTaggedWord>> pairs = q.getPairs(seq);
				if(pairs != null) {
					for(Pair<CompTaggedWord, CompTaggedWord> pair : pairs) {
						if(!reliablePairRepository.contains(pair)) {
							if(candidates.containsKey(pair)) {
								int count = candidates.get(pair);
								if((count + 1) > alpha) {
									likelyReliablePairRepository.add(pair);
								}
								candidates.put(pair, count++);
							}
							else {
								candidates.put(pair, 1);
							}
						}
					}
				}
			}
		}
		t.done();
	}
}
