package ax.makila.comparableentititymining;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import ax.makila.comparableentititymining.postagger.CompTaggedWord;
import ax.makila.comparableentititymining.sequentialpatterns.Sequence;
import ax.makila.comparableentititymining.sequentialpatterns.SequentialPattern;
import ax.makila.comparableentititymining.sequentialpatterns.patterns.SpecializedSequence;

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
	/**
	 * Comparator token
	 */
	public static final String comparator = "$c";
	/**
	 * Start of sentence token
	 */
	public static final String start = "#start";
	/**
	 * End of sentence token
	 */
	public static final String end = "#end";
	/**
	 * Contains all the questions for mining
	 */
	public static final String[] yahooAnswers = {
		// comparable questions x 19
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"Is it better to be unhealthy and happy or good looking and stressed?",
		"are there more delicious chocolate in cookies or in old car batteris",
		"What is better national guard or reserve?",
		"What is the better dog food pedigree or eukanuba?",
		"What do you prefer short boots or long boots for riding and why?",
		"Why are games on iPhone better than Android?",
		"What airline is better for traveling to south america american airlines or continental?",
		"What book is better, Maximum ride the angel experiment or maximum ride schools out forever?",
		"Which Burberry face of the year do you prefer, Rosie Huntington or Emma Watson?",
		"Madrid vs. Barcelona: Which city is better to shop for designer brands?",
		"London vs. Paris: Which city is better(cheaper) for shopping?",
		"In the world, which is better 'sheeple' or 'goats'? (see details please)?",
		"Do you prefer Old Spice or Axe?",
		"Green vs pink?",
		"Heaven vs hell?",
		"Catapult vs ballista?",
		"Pasta vs pizza?",
		"Sonic vs Mario?",
		"cats vs dogs?",
		"sega vs nintendo?",
		"nike vs adidas?",
		"google vs yahoo?",
		"samsung vs apple?",
		"bamboo vs oak?",
		"hot vs cold?",
		"old vs young?",
		"$c vs $c?",
		"new vs second-hand?",
		"water vs coffee?",
		"pikachu vs charizard?",
		"pikachu vs bulbasaur?",
		"Enemies vs friends?",
		"Cool vs hot?",
		"derp vs herp?",
		"sheeps vs cows?",
		"sonic vs knuckles?",
		"sonic vs mario?",
		"android vs ios?",
		"Is that really a fight of cats vs dogs?",
		"iPhone vs Galaxy?",
		"Which City Is Better, NYC or Paris?",
		// Non-comparable questions x 18
		"How to convince my parents to buy me a iphone or samsung galaxy s4?",
		"Can I buy an iPhone at the apple store and then use it for cricket?",
		"What kind of car is a total chick magnet?",
		"Is the 1998 trail blazer 4x4?",
		"What's a good anime to watch?",
		"Does it affect you big time if you don't pass the CAHSEE exam the 1st time?",
		"Whats the used value on a jackson SLAT3-7 guitar?" ,
		"Merger of Cooperative bank into nationalized bank?",
		"Is there a laser dome in or around NJ?",
		"how can I return me deleted emails?",
		"Anne Bradstreet's poetry? PLEASE in need of HELP?",
		"White specks in stool, should I be concerned?",
		"I believe I've seen death and god through pot?",
		"I'm really tired and I need some time managment/ staying up tips please help!?",
		"I have lots of lung mucus! Going on for 3 months now!?",
		"is muscle confusion a good topic for a research paper on mma?",
		"Is Social Security an outdated form of social welfare? Is it time to just mandate people to save at banks?",
	"What does it mean when a guy doesn't text you back?" };

	/**
	 * The initial IEP to start the bootstrapping method
	 */
	//"#start $c/NN vs/CC $c/NN?/. #end"
	static final SequentialPattern initialIEP = new SpecializedSequence("#start $c/NN vs $c/NN?/. #end");

	/**
	 * Main method
	 * 
	 * @param argsv Input
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
		List<Sequence> questions = preProcessQuestions(yahooAnswers);
		extractSeedComparators(initialIEP, questions);
		iepMining(questions);
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

	@SuppressWarnings("unused")
	private List<String> extractComparableComparators(List<String> iep, String q) {
		// TODO Auto-generated method stub
		return null;
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
	private List<Pair<CompTaggedWord, CompTaggedWord>> extractSeedComparators(
			SequentialPattern bootstrappingIEP, List<Sequence> questions) {
		List<Pair<CompTaggedWord, CompTaggedWord>> pairs = new ArrayList<Pair<CompTaggedWord,CompTaggedWord>>();
		//TODO: Remember to do phrase chunking for specialized and generalized patterns!!!
		for (Sequence q : questions) {
			if(q.matches(bootstrappingIEP)) {
				pairs.addAll(q.getPairs(bootstrappingIEP));
			}
		}
		return pairs;
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
	private List<String> iepMining(List<Sequence> questions) {
		System.out.println("Start");
		//Get seed comparator pairs
		//List<Pair<String, String>> seedComparatorPairs = extractSeedComparators(initialIEP, questionArchive);
		//List<Pair<String, String>> newComparatorPairs = new ArrayList<Pair<String, String>>(seedComparatorPairs);
		//Contains the patterns generated during each iteration
		List<String> newPatterns = new ArrayList<String>();
		//All the questions identified as comparative
		List<String> comparativeQuestionSet = new ArrayList<String>();
		//All patterns gathered from the previous iteration
		List<String> iep = new ArrayList<String>();
		do {
			iep.addAll(newPatterns);
			// List<String> newComparativeQuestions =
			// comparativeQuestionIdentify(seedComparatorPairs,
			// questionArchive);
			// comparativeQuestionSet.addAll(newComparativeQuestions);
			for (int i = 0; i < questions.size(); i++) {
				if (isMatchingPatterns(iep, questions.get(i))) {
					comparativeQuestionSet.remove(i);
				}
			}
			// newPatterns =
			PatternGeneration.mineGoodPatterns(null, questions);
			/*//newComparatorPairs.clear();
			for (String q : questionArchive) {
				List<String> comparatorPairs = extractComparableComparators(
						iep, q);
				if (comparatorPairs != null&& newComparatorPairs.containsAll(comparatorPairs)) {
					// seedComparatorPairs.addAll(comparatorPairs);
				}
			}
			 */
		} while (newPatterns.size() != 0);
		return iep;
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
	private boolean isMatchingPatterns(List<String> iep, Sequence q) {
		for (String p : iep) {
			if (q.text().matches(p)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	private String phraseChunker(Sequence pattern) {
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

		List<List<CompTaggedWord>> tagged = pattern.getTaggedWords();
		StringBuilder sb = new StringBuilder();
		for (List<CompTaggedWord> list : tagged) {
			ListIterator<CompTaggedWord> tokens = list.listIterator();
			while (tokens.hasNext()) {
				CompTaggedWord word = tokens.next();
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
					CompTaggedWord nextWord = tokens.next(); // Peek ahead
					if (nextWord.tag().matches(rule3b.pattern())) {
						tokens.remove();
						CompTaggedWord current = tokens.previous(); // Move back
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
					CompTaggedWord nextWord = tokens.next();
					if (nextWord.tag().matches(rule4b.pattern())) {
						tokens.remove();
						CompTaggedWord current = tokens.previous();
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
					CompTaggedWord nextWord = tokens.next();
					if (nextWord.tag().matches(rule5b.pattern())) {
						tokens.remove();
						CompTaggedWord current = tokens.previous();
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
					CompTaggedWord nextWord = tokens.next();
					if (nextWord.tag().matches(rule6b.pattern())) {
						tokens.remove();
						CompTaggedWord current = tokens.previous();
						current.setValue(current.value() + " "
								+ nextWord.value());
						current.setTag("JJS");
					} else {
						tokens.previous();
					}
				}

			}
			//pattern.updateStringRepresentation();

		}
		return pattern.toString();
	}

	private List<Sequence> preProcessQuestions(String[] questions) {
		List<Sequence> sequences = new ArrayList<Sequence>();
		for(String q : questions) {
			Sequence sequence = new Sequence(q);
			sequence.set();
			sequences.add(sequence);
		}
		return sequences;
	}

}
