package ax.makila.comparableentititymining;

import java.util.ArrayList;
import java.util.List;

public class PatternEvaluation {
	String[] questionSet = null;
	List<String> reliablePairRepository = new ArrayList<String>();

	/**
	 * A pattern is reliable if it's reliability score is more than a threshold
	 * gamma. The reliability score is determined by formula (1), (2), (3) and
	 * (4) in the paper.
	 * 
	 * @param pattern
	 *            The pattern to be evaluated
	 * @return true if reliability score for patterns is higher than threshold
	 *         gamma, else false.
	 */
	@SuppressWarnings("unused")
	private boolean isReliable(String pattern) {
		return false;
	}

	/**
	 * NQ(x) means the number of questions satisfying a condition x. The
	 * condition pi -> cpj denotes that cpj can be extracted from a question by
	 * applying pattern pi. If cpj = '*', then the number returned is any
	 * question containing pattern pi.
	 * 
	 * @param pi
	 *            A reliable pattern that can generate cpj
	 * @param cpj
	 *            A set of reliable comparator pairs
	 * @return The number of questions that pi can extract cpj from
	 */
	@SuppressWarnings("unused")
	private int nq(String pi, String cpj) {
		int counter = 0;
		if (cpj.equals("*")) {
			for (String q : questionSet) {
				if (q.contains(pi)) {
					counter++;
				}
			}
		} else {
			for (String q : questionSet) {
				// if(extract(pi, questionSet).equals(cpj)) {
				counter++;
				// }
			}
		}
		return counter;
	}

	/**
	 * Support is defined as the support S for comparator pair cpi_roof which
	 * can be extracted by P_roof^k and does not exist in the current reliable
	 * set.
	 * 
	 * @param pk_roof
	 *            A candidate pattern which can extract cpi_roof
	 * @param cpi_roof
	 *            The comparator pair which can be extracted by pk_roof and does
	 *            not exist in {@link #reliablePairRepository}
	 * @return The support count for pattern p given the definition above
	 */
	@SuppressWarnings("unused")
	private int support(String pk_roof, String cpi_roof) {
		int counter = 0;
		for (String q : questionSet) {
			// if(extract(pk_roof, q).equals(cpi_roof)) {
			if (!reliablePairRepository.contains(cpi_roof)) {
				counter++;
			}
			// }
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
	@SuppressWarnings("unused")
	private double rkpi(String pi) {
		int cpiExtract = 0;
		int patternContains = 0;
		for (String q : questionSet) {
			String cpj = null;// = extract(pi, q);
			cpiExtract += nq(pi, cpj);
			if (q.contains(pi)) {
				patternContains++;
			}
		}
		double score = (double) cpiExtract / (double) patternContains;
		return score;
	}

}
