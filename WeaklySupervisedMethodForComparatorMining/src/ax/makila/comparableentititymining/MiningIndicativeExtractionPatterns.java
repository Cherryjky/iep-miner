package ax.makila.comparableentititymining;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ax.makila.comparableentititymining.postagger.CompTaggedWord;
import ax.makila.comparableentititymining.sequentialpatterns.Sequence;
import ax.makila.comparableentititymining.sequentialpatterns.SequentialPattern;
import ax.makila.comparableentititymining.sequentialpatterns.patterns.SpecializedSequence;
import edu.stanford.nlp.util.Timing;

/**
 * Class for mining indicative extraction patterns (IEP).
 * @author fredrik
 *
 */
public class MiningIndicativeExtractionPatterns {
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
	public static boolean isMatchingPatterns(Set<SequentialPattern> iep, Sequence q) {
		for (SequentialPattern p : iep) {
			if (q.matches(p)) {
				return true;
			}
		}
		return false;
	}
	public Set<SequentialPattern> iep = new HashSet<SequentialPattern>();
	public static List<Sequence> questions;

	/**
	 * The initial IEP to start the bootstrapping method
	 */
	//"#start $c/NN vs/CC $c/NN?/. #end"
	static final SequentialPattern initialIEP = new SpecializedSequence("#start $c/NN vs/CC $c/NN?/. #end");

	/**
	 * Main method.
	 * 
	 * @param argsv Input
	 */
	public static void main(String argsv[]) {
		// Used to initiate the bootstrapping
		@SuppressWarnings("unused")
		MiningIndicativeExtractionPatterns miner = new MiningIndicativeExtractionPatterns();

	}

	/**
	 * Constructor. Initializes values and starts the
	 * bootstrapping method of extracting IEPs.
	 * 
	 * @param iep
	 *            The initial seed IEP
	 */
	@SuppressWarnings("static-access")
	public MiningIndicativeExtractionPatterns() {
		BufferedReader in = null;
		BufferedReader comp = null;
		BufferedReader nonComp = null;
		try {
			in = new BufferedReader(new FileReader("bigdata.txt"));
			comp = new BufferedReader(new FileReader("all_comparative.txt"));
			nonComp = new BufferedReader(new FileReader("all_noncomparative.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line;
		List<String> yahooAnswers = new ArrayList<String>();
		List<String> comparative = new ArrayList<String>();
		List<String> nonComparative = new ArrayList<String>();
		Timing t = new Timing();
		t.startDoing("Reading data");
		try {
			while((line = in.readLine()) != null) {
				yahooAnswers.add(line);
			}
			in.close();
			while((line  = comp.readLine()) != null) {
				comparative.add(line);
			}
			comp.close();
			while((line = nonComp.readLine()) != null) {
				nonComparative.add(line);
			}
			nonComp.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		t.done();
		List<Sequence> comparativeQuestions;
		List<Sequence> nonComparativeQuestions;
		t = new Timing();
		t.startDoing("Preprocessing questions");
		questions = preProcessQuestions(yahooAnswers);
		t.done();

		t = new Timing();
		t.startDoing("Preprocessing comparative questions");
		comparativeQuestions = preProcessQuestions(comparative);
		t.done();

		t = new Timing();
		t.startDoing("Preprocessing non-comparative questions");
		nonComparativeQuestions = preProcessQuestions(nonComparative);
		t.done();

		Set<Pair<CompTaggedWord, CompTaggedWord>> seedPairs = extractSeedComparators(initialIEP, questions);
		System.out.println("SEEDPAIRS " + seedPairs.size());
		Timing total = new Timing();
		total.start();
		iepMining(questions, seedPairs);
		total.done("IEP mining");

		t = new Timing();
		t.startDoing("Calculating precision");
		Set<Pair<CompTaggedWord, CompTaggedWord>> set = new HashSet<Pair<CompTaggedWord,CompTaggedWord>>();
		double counter = 0;
		double tot = questions.size();
		int steps = (int) tot/10;
		for(Sequence q : questions) {
			if(counter % steps == 0 || counter == tot - 1) {
				int percent = (int) (counter/(tot - 1)*100);
				System.out.print(percent + "% ");
			}
			counter++;
			Pair<CompTaggedWord, CompTaggedWord> p = extractComparableComparators(iep, q);
			if(p != null) {
				set.add(p);
			}
		}

		List<Sequence> identifiedComparativeQuestions = comparativeQuestionIdentify(set, questions);
		for(Sequence seq : identifiedComparativeQuestions) {
			questions.remove(seq);
		}

		double truePositive = 0;
		double trueNegative = 0;
		double falseNegative = 0;
		for(Sequence idd : identifiedComparativeQuestions) {
			if(comparativeQuestions.contains(idd)) {
				truePositive++;
			}
			if(nonComparativeQuestions.contains(idd)) {
				trueNegative++;
			}
		}

		for(Sequence seq : questions) {
			if(nonComparativeQuestions.contains(seq)) {
				falseNegative++;
			}
		}
		t.done();
		double precision = truePositive/(truePositive + trueNegative);
		double recall = truePositive/(truePositive + falseNegative);
		double fScore = 2 * ((precision*recall) / (precision + recall));
		System.out.println("Precision: " + precision);
		System.out.println("Recall   : " + recall);
		System.out.println("F-score  : " + fScore);

	}

	/**
	 * Identifies questions as comparative or not. For a given set of questions
	 * <tt>questions</tt> and a set of seed comparator pairs, <tt>pairs</tt> pairs.
	 * for each pair in <tt>pairs</tt>, if a questions contains the pair it's regarded as
	 * a comparative questions and the question is added to a comparative question
	 * repository.
	 * 
	 * @param pairs
	 *            The seed comparator pairs that are used to identify
	 *            comparative questions
	 * @param questions
	 *            A set of questions to be compared with the comparator pairs.
	 * @return All questions considered to be comparative questions with regard
	 *         to the comparator pairs.
	 */
	public List<Sequence> comparativeQuestionIdentify(
			Set<Pair<CompTaggedWord, CompTaggedWord>> pairs, List<Sequence> questions) {
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

	/**
	 * Extracts comparative pairs. Given a set of identified IEPs <tt>iep</tt> and a
	 * sequence <tt>question</tt> the comparators are extracted from the sentence. As a
	 * sequence can match many IEPs, the longest-matching strategy is used as the longest
	 * matching IEP is considered to be the most reliable one to extract comparators.
	 * @param iep A set of IEPs
	 * @param question A sequence to extract
	 * @return A comparator pair extracted from the <tt>question</tt>
	 */
	public Pair<CompTaggedWord, CompTaggedWord> extractComparableComparators(Set<SequentialPattern> iep, Sequence question) {
		//The maximum length strategy is used to extract the comparators. The longest matching pattern is
		//considered to be more reliable
		SequentialPattern winner = null;

		for(SequentialPattern pattern : iep) {
			if(question.matches(pattern)) {
				if(winner == null) {
					winner = pattern;
				}
				else if(pattern.length() > winner.length()) {
					winner = pattern;
				}
			}
		}

		List<Pair<CompTaggedWord, CompTaggedWord>> list = null;

		if(winner != null) {
			list = question.getPairs(winner);
		}

		if(list != null) {
			return list.get(0);
		}
		else {
			return null;
		}
	}

	/**
	 * Extracts seed comparator pairs from a set of questions. Given an initial IEP used
	 * for bootstrapping it iterates over all of the questions and try to match the IEP
	 * to the question. If it matches, comparator pairs is extracted from the sentence.
	 * 
	 * @param bootstrappingIEP The initial IEP for bootstrapping
	 * @param questions The list of questions from which comparative pairs are extracted from
	 * @return A list containing comparator pairs extracted from the questions.
	 */
	private Set<Pair<CompTaggedWord, CompTaggedWord>> extractSeedComparators(
			SequentialPattern bootstrappingIEP, List<Sequence> questions) {
		Set<Pair<CompTaggedWord, CompTaggedWord>> pairs = new HashSet<Pair<CompTaggedWord,CompTaggedWord>>();
		for (Sequence q : questions) {
			if(q.matches(bootstrappingIEP)) {
				pairs.addAll(q.getPairs(bootstrappingIEP));
			}
		}
		return pairs;
	}

	/**
	 * Generates patterns through a weekly supervised bootstrapping approach.
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
	 * from the questions collection. The method has two key steps: 1) pattern generation
	 * 2) pattern evaluation
	 * 
	 * @see PatternGeneration
	 * @see PatternEvaluation
	 * 
	 * @param questions The set of questions to generate patterns from
	 * @param seedComparatorPairs A collection of initial seed comparator pairs
	 * @return A list of reliable IEP.
	 */
	private Set<SequentialPattern> iepMining(List<Sequence> questions, Set<Pair<CompTaggedWord, CompTaggedWord>> seedComparatorPairs) {
		//Get seed comparator pairs
		Set<Pair<CompTaggedWord, CompTaggedWord>> newComparatorPairs = new HashSet<Pair<CompTaggedWord,CompTaggedWord>>(seedComparatorPairs);
		//Contains the patterns generated during each iteration
		Set<SequentialPattern> newPatterns = new HashSet<SequentialPattern>();
		//All the questions identified as comparative
		Set<Sequence> comparativeQuestionSet = new HashSet<Sequence>();
		iep.add(initialIEP);
		int iteration = 0;
		do {
			iteration++;
			System.out.println("Start iteration " + iteration);
			//Adds all ieps found in the previous iteration
			iep.addAll(newPatterns);

			//Identifies questions the comparative pairs found so far
			List<Sequence> newComparativeQuestions = comparativeQuestionIdentify(newComparatorPairs, questions);
			//Adds identified comparative questions to the comparative questionset
			comparativeQuestionSet.addAll(newComparativeQuestions);



			//If a pattern already matches an existing IEP, remove it from the comparative question set
			for(Sequence seq : questions) {
				if(isMatchingPatterns(iep, seq)) {
					if(comparativeQuestionSet.contains(seq)) {
						comparativeQuestionSet.remove(seq);
					}
				}
			}

			//Get some hot new pattern to play with
			System.out.println("Number of comparative questions for pattern generation: " + comparativeQuestionSet.size());
			newPatterns = PatternGeneration.mineGoodPatterns(newComparatorPairs, comparativeQuestionSet, questions);

			System.out.println("PATTERN SIZE! " + newPatterns.size());
			//Remove the comparator pairs found in the previous iteration
			newComparatorPairs.clear();

			//Extract comparator pairs
			for (Sequence q : questions) {
				Pair<CompTaggedWord, CompTaggedWord> comparatorPairs = extractComparableComparators(iep, q);
				if (comparatorPairs != null && !seedComparatorPairs.contains(comparatorPairs)) {
					newComparatorPairs.add(comparatorPairs);
				}
			}

		} while (newPatterns.size() != 0 || iteration <= 10);

		return iep;
	}



	/**
	 * Preprocessed a list strings. The method iterates
	 * over all the entries in <tt>questions</tt> and create Sequence objects from them.
	 * Also makes sure that none of the questions contains any strange characters.
	 * 
	 * @see Sequence
	 * 
	 * @param questions The list of questions to be preprocessed
	 * @return A list of sequences
	 */
	private List<Sequence> preProcessQuestions(List<String> questions) {
		List<Sequence> sequences = new ArrayList<Sequence>();
		double total = questions.size();
		int steps = (int) total/10;
		int counter = 0;
		for(String q : questions) {
			if(counter % steps == 0 || counter == total - 1) {
				int percent = (int) ((counter/(total - 1))*100);
				System.out.print(percent + "% ");
			}
			counter++;
			char[] arr = q.toCharArray();
			for(char c : arr) {
				if(c != (char) (byte) c) {
					q = q.replace(""+c, "");
				}
			}
			Sequence sequence = new Sequence(q);
			sequence.set();
			sequences.add(sequence);
		}
		return sequences;
	}

}
