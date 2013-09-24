package ax.makila.comparableentititymining;

import java.util.ArrayList;
import java.util.List;

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
		"Is there really a fight of pikachu vs bulbasaur?",
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
		new MiningIndicativeExtractionPatterns();
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
		List<Pair<CompTaggedWord, CompTaggedWord>> seedPairs = extractSeedComparators(initialIEP, questions);
		iepMining(questions, seedPairs);
	}

	/**
	 * For a given set of questions and a set of seed comparator pairs, for each
	 * pair, if a text contains the pair it's regarded as a comparative
	 * question.
	 * 
	 * @param pairs
	 *            The seed comparator pairs that are used to identify
	 *            comparative questions
	 * @param questions
	 *            A set of questions to be compared with the comparator pairs.
	 * @return All questions considered to be comparative questions with regard
	 *         to the comparator pairs.
	 */
	private List<Sequence> comparativeQuestionIdentify(
			List<Pair<CompTaggedWord, CompTaggedWord>> pairs, List<Sequence> questions) {
		List<Sequence> comparativeQuestions = new ArrayList<Sequence>();
		for (Sequence question : questions) {
			for(Pair<CompTaggedWord, CompTaggedWord> pair : pairs) {
				CompTaggedWord firstWord = pair.x;
				CompTaggedWord secondWord = pair.y;
				String text = question.text();
				if(text.contains(firstWord.value()) && text.contains(secondWord.value())) {
					if(!question.hasReplacedComparators()) {
						question.replaceComparators(pair);
					}
					comparativeQuestions.add(question);
				}
			}
		}
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
	 * @param seedPairs
	 * 
	 * @return A list of reliable IEP.
	 */
	private List<String> iepMining(List<Sequence> questions, List<Pair<CompTaggedWord, CompTaggedWord>> pairs) {
		System.out.println("Start");
		//Get seed comparator pairs
		//List<Pair<String, String>> newComparatorPairs = new ArrayList<Pair<String, String>>(seedComparatorPairs);
		//Contains the patterns generated during each iteration
		List<String> newPatterns = new ArrayList<String>();
		//All the questions identified as comparative
		List<Sequence> comparativeQuestionSet = new ArrayList<Sequence>();
		//All patterns gathered from the previous iteration
		List<String> iep = new ArrayList<String>();
		do {
			iep.addAll(newPatterns);
			List<Sequence> newComparativeQuestions = comparativeQuestionIdentify(pairs, questions);
			comparativeQuestionSet.addAll(newComparativeQuestions);
			for (int i = 0; i < questions.size(); i++) {
				if (isMatchingPatterns(iep, questions.get(i))) {
					comparativeQuestionSet.remove(i);
				}
			}
			newPatterns = PatternGeneration.mineGoodPatterns(pairs, comparativeQuestionSet);
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
