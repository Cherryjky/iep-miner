package ax.makila.comparableentititymining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import ax.makila.comparableentititymining.postagger.StanfordPosTagger;
import ax.makila.comparableentititymining.postagger.StanfordPosTagger.PosTag;

import edu.stanford.nlp.ling.TaggedWord;

public class MiningIndicativeExtractionPatterns {
	// alpha, beta, gamma, lambda parameters are determined empirically
	// alpha determined by maximizing precision without hurting recall by
	// investigating frequencies of pairs in a labeled set
	/**
	 * Used as a threshold for the support of likely reliable comparator pairs
	 */
	public static final double alpha = 3.0;
	// Beta and gamma is threshold parameters
	public static final int beta = 10;
	/**
	 * Used as a threshold for determining reliable patterns
	 */
	public static final double gamma = 0.8;
	/**
	 * an interpolation parameter in (4) (see paper)
	 */
	public static final double lambda = 0.5;
	// Comparator token
	public static final String comparator = "$c";
	// Start token
	public static final String start = "#start";
	// end token
	public static final String end = "#end";
	/**
	 * Contains all the questions for mining
	 */
	public static final String[] yahooAnswers = {
			// comparable questions x 19
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start Is it better to be unhealthy and happy$c or good looking and stressed$c? #end",
			"#start are there more delicious chocolate in cookies or in old car batteris? #end",
			"#start What is better national guard or reserve? #end",
			"#start What is the better dog food pedigree or eukanuba? #end",
			"#start What do you prefer short boots or long boots for riding and why? #end",
			"#start Why are games on iPhone better than Android? #end",
			"#start What airline is better for traveling to south america american airlines or continental? #end",
			"#start What book is better, Maximum ride the angel experiment or maximum ride schools out forever? #end",
			"#start Which Burberry face of the year do you prefer, Rosie Huntington or Emma Watson? #end",
			"#start Madrid vs. Barcelona: Which city is better to shop for designer brands? #end",
			"#start London vs. Paris: Which city is better(cheaper) for shopping? #end",
			"#start In the world, which is better 'sheeple' or 'goats'? (see details please)? #end",
			"#start Do you prefer Old Spice or Axe? #end",
			"#start Green vs pink? #end",
			"#start Heaven vs hell? #end",
			"#start Catapult vs ballista? #end",
			"#start Pasta vs pizza? #end",
			"#start Sonic vs Mario? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start $c vs $c? #end",
			"#start iPhone vs Galaxy? #end",
			"#start Which City Is Better, NYC or Paris? #end",
			// Non-comparable questions x 18
			"#start How to convince my parents to buy me a iphone or samsung galaxy s4? #end",
			"#start Can I buy an iPhone at the apple store and then use it for cricket? #end",
			"#start What kind of car is a total chick magnet? #end",
			"#start Is the 1998 trail blazer 4x4? #end",
			"#start What's a good anime to watch? #end",
			"#start Does it affect you big time if you don't pass the CAHSEE exam the 1st time? #end",
			"#start Whats the used value on a jackson SLAT3-7 guitar? #end",
			"#start Merger of Cooperative bank into nationalized bank? #end",
			"#start Is there a laser dome in or around NJ? #end",
			"#start how can I return me deleted emails? #end",
			"#start Anne Bradstreet's poetry? PLEASE in need of HELP? #end",
			"#start White specks in stool, should I be concerned? #end",
			"#start I believe I've seen death and god through pot? #end",
			"#start I'm really tired and I need some time managment/ staying up tips please help!? #end",
			"#start I have lots of lung mucus! Going on for 3 months now!? #end",
			"#start is muscle confusion a good topic for a research paper on mma? #end",
			"#start Is Social Security an outdated form of social welfare? Is it time to just mandate people to save at banks? #end",
			"#start What does it mean when a guy doesn't text you back? #end" };
	/**
	 * A list representation of the yahoo answers questions.
	 */
	List<String> questionArchive = Arrays.asList(yahooAnswers);
	/**
	 * The initial IEP to start the bootstrapping method
	 */
	static final String initialIEP = "#start $c/NN vs/CC $c/NN?/. #end";

	/**
	 * Main method
	 * 
	 * @param argsv
	 *            Input
	 */
	public static void main(String argsv[]) {
		// Used to initiate the bootstrapping
		@SuppressWarnings("unused")
		MiningIndicativeExtractionPatterns miner = new MiningIndicativeExtractionPatterns();
	}

	/**
	 * Constructor for this class. Initializes values and starts the
	 * bootstrapping method of extracting IEPs.
	 * 
	 * @param iep
	 *            The initial seed IEP
	 */
	public MiningIndicativeExtractionPatterns() {
		iepMining();
	}

	/**
	 * Does the actual work of extracting IEP through a bootstrapping approach.
	 * The bootstrapping process starts with a single IEP. From it, we extract a
	 * set of initial seed comparator pairs. For each comparator pair, all
	 * questions containing the pair are retrieved from a question collection
	 * and regarded as comparative questions. From the comparative questions and
	 * comparator pairs, all possible sequential patterns are generated and
	 * evaluated by measuring their reliability score. Patterns evaluated as
	 * reliable ones are IEPs and are added into an IEP repository. Then, new
	 * comparator pairs are extracted from the collection using the latest IEPs.
	 * The new comparators are added to a reliable comparator repository and
	 * used as new seeds for pattern learning in the next iteration. All
	 * questions from which reliable comparators are extracted are removed from
	 * the collection to allow finding new patters efficiently in later
	 * iterations. The process iterates until no more new patterns can be found
	 * from the questions collection. The method has two key steps:
	 * <ol>
	 * <li>Pattern generation
	 * <li>Pattern extraction
	 * </ol>
	 * 
	 * @return A list of reliable IEP.
	 */
	private List<String> iepMining() {
		List<Pair<String, String>> seedComparatorPairs = extractSeedComparators(
				initialIEP, questionArchive);
		List<Pair<String, String>> newComparatorPairs = new ArrayList<Pair<String, String>>(
				seedComparatorPairs);
		List<String> newPatterns = new ArrayList<String>();
		List<String> comparativeQuestionSet = new ArrayList<String>();
		List<String> iep = new ArrayList<String>();
		do {
			iep.addAll(newPatterns);
			// List<String> newComparativeQuestions =
			// comparativeQuestionIdentify(seedComparatorPairs,
			// questionArchive);
			// comparativeQuestionSet.addAll(newComparativeQuestions);
			for (int i = 0; i < questionArchive.size(); i++) {
				if (isMatchingPatterns(iep, questionArchive.get(i))) {
					comparativeQuestionSet.remove(i);
				}
			}
			// newPatterns =
			// PatternExtraction.mineGoodPatterns(seedComparatorPairs,
			// comparativeQuestionSet);
			newComparatorPairs.clear();
			for (String q : questionArchive) {
				List<String> comparatorPairs = extractComparableComparators(
						iep, q);
				if (comparatorPairs != null
						&& newComparatorPairs.containsAll(comparatorPairs)) {
					// seedComparatorPairs.addAll(comparatorPairs);
				}
			}

		} while (newPatterns.size() != 0);

		return iep;
	}

	private List<String> extractComparableComparators(List<String> iep, String q) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * For a given question, return true if the question matches any pattern in
	 * iep, else false.
	 * 
	 * @param iep
	 *            A list of extracted IEP patterns
	 * @param q
	 *            A question that is compared with the IEPs
	 * @return True if question matches pattern, else false.
	 */
	private boolean isMatchingPatterns(List<String> iep, String q) {
		for (String p : iep) {
			if (q.matches(p)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * For a given set of questions and a set of seed comparator pairs, for each
	 * pair, if a text contains the pair it's regarded as a comparative
	 * question.
	 * 
	 * @param comparatorPairs
	 *            The seed comparator pairs that are used to identify
	 *            comparative questions
	 * @param questions
	 *            A set of questions to be compared with the comparator pairs.
	 * @return All questions considered to be comparative questions with regard
	 *         to the comparator pairs.
	 */
	@SuppressWarnings("unused")
	private List<String> comparativeQuestionIdentify(
			List<Pair<String, String>> comparatorPairs, String[] questions) {
		List<String> comparativeQuestions = new ArrayList<String>();
		for (String q : questions) {
			for (Pair<String, String> cp : comparatorPairs) {
				// if(q.matches(cp)) {
				comparativeQuestions.add(q);
				// }
			}
		}
		// TODO Auto-generated method stub
		return comparativeQuestions;
	}

	/**
	 * Given an initial IEP used for bootstrapping it iterates over all of the
	 * questions and try to match the IEP to the question. If it matches,
	 * comparator pairs can be extracted from the sentence
	 * 
	 * @param bootstrappingIEP
	 * @param questions
	 * @return A list containing comparator pairs extracted from the sentence
	 */
	private List<Pair<String, String>> extractSeedComparators(
			String bootstrappingIEP, List<String> questions) {
		for (String q : questions) {
			PosTag posTag = StanfordPosTagger.posTagString(q);
			System.out.println(phraseChunker(posTag));
		}
		/*
		 * if(bootstrappingIEP.isLexical()) { List<List<String>> sequences = new
		 * ArrayList<List<String>>(); for(String q : questions) {
		 * List<List<String>> tokenized = StanfordPosTagger.tokenizedString(q);
		 * for(List<String> sentence : tokenized) { sequences.add(sentence); } }
		 * } else { List<Sequence> lexPat =
		 * PatternGeneration.generateLexicalPatterns(questions); List<Sequence>
		 * genPat = PatternGeneration.generateGeneralizedPatterns(lexPat);
		 * for(Sequence seq : genPat) { List<List<String>> tokenized =
		 * StanfordPosTagger.tokenizedString(seq.toString()); for(List<String>
		 * sentence : tokenized) { if(sentence.equals(bootstrappingIEP)) { } } }
		 * }
		 */
		return null;
	}

	@SuppressWarnings("unused")
	private String phraseChunker(PosTag pattern) {
		/*
		 * Heuristic rules NP* -> NP NN* -> NN NN + NNS -> NNS NP + NPS -> NPS
		 * More + ADJ -> JJR Most + ADJ -> JJS ...
		 */
		Pattern rule1 = Pattern.compile("NP\\S");
		Pattern rule2 = Pattern.compile("NN\\S");

		Pattern rule3a = Pattern.compile("NN\\s");
		Pattern rule3b = Pattern.compile("NNS");

		Pattern rule4a = Pattern.compile("NP\\s");
		Pattern rule4b = Pattern.compile("NPS");

		Pattern rule5a = Pattern.compile("more");
		Pattern rule5b = Pattern.compile("JJ");

		Pattern rule6a = Pattern.compile("most");
		Pattern rule6b = Pattern.compile("JJ\\S");

		List<ArrayList<TaggedWord>> tagged = pattern.taggedWords;
		StringBuilder sb = new StringBuilder();
		for (ArrayList<TaggedWord> list : tagged) {
			ListIterator<TaggedWord> tokens = list.listIterator();
			while (tokens.hasNext()) {
				TaggedWord word = tokens.next();
				// Rule 1
				if (word.tag().matches(rule1.pattern())) {
					word.setTag("NP");
				}
				// Rule 2
				else if (word.tag().matches(rule2.pattern())) {
					word.setTag("NN");
				}
				// Rule 3
				else if (word.tag().matches(rule3a.pattern())
						&& tokens.hasNext()) {
					TaggedWord nextWord = tokens.next(); // Peek ahead
					if (nextWord.tag().matches(rule3b.pattern())) {
						tokens.remove();
						TaggedWord current = tokens.previous(); // Move back
																// again
						current.setValue(current.value() + " "
								+ nextWord.value());
						current.setTag("NNS");
					} else {
						tokens.previous(); // Move back
					}
				}
				// Rule 4
				else if (word.tag().matches(rule4a.pattern())
						&& tokens.hasNext()) {
					TaggedWord nextWord = tokens.next();
					if (nextWord.tag().matches(rule4b.pattern())) {
						tokens.remove();
						TaggedWord current = tokens.previous();
						current.setValue(current.value() + " "
								+ nextWord.value());
						current.setTag("NPS");
					} else {
						tokens.previous(); // Move back
					}
				}
				// Rule 5
				else if (word.value().matches(rule5a.pattern())
						&& tokens.hasNext()) {
					TaggedWord nextWord = tokens.next();
					if (nextWord.tag().matches(rule5b.pattern())) {
						tokens.remove();
						TaggedWord current = tokens.previous();
						current.setValue(current.value() + " "
								+ nextWord.value());
						current.setTag("JJR");
					} else {
						tokens.previous();
					}
				}
				// Rule 6
				else if (word.value().matches(rule6a.pattern())
						&& tokens.hasNext()) {
					TaggedWord nextWord = tokens.next();
					if (nextWord.tag().matches(rule6b.pattern())) {
						tokens.remove();
						TaggedWord current = tokens.previous();
						current.setValue(current.value() + " "
								+ nextWord.value());
						current.setTag("JJS");
					} else {
						tokens.previous();
					}
				}

			}
			pattern.updateStringRepresentation();

		}
		return pattern.toString();
	}

}
