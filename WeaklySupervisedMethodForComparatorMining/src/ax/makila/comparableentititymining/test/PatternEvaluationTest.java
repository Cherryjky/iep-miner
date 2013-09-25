package ax.makila.comparableentititymining.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ax.makila.comparableentititymining.Pair;
import ax.makila.comparableentititymining.PatternEvaluation;
import ax.makila.comparableentititymining.postagger.CompTaggedWord;
import ax.makila.comparableentititymining.sequentialpatterns.Sequence;
import ax.makila.comparableentititymining.sequentialpatterns.SequentialPattern;
import ax.makila.comparableentititymining.sequentialpatterns.patterns.SpecializedSequence;

public class PatternEvaluationTest {
	List<Pair<CompTaggedWord, CompTaggedWord>> pairs =
			new ArrayList<Pair<CompTaggedWord, CompTaggedWord>>();

	Pair<CompTaggedWord, CompTaggedWord> pair;

	SequentialPattern pattern = new SpecializedSequence("#start $c/NN vs $c/NN? #end");

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


	List<Sequence> questionSet = preProcessQuestions(yahooAnswers);

	private List<Sequence> preProcessQuestions(String[] questions) {
		List<Sequence> sequences = new ArrayList<Sequence>();
		for(String q : questions) {
			Sequence sequence = new Sequence(q);
			sequence.set();
			sequences.add(sequence);
		}
		return sequences;
	}

	@Before
	public void setUp() {
		for(Sequence seq : questionSet) {
			List<Pair<CompTaggedWord, CompTaggedWord>> p = seq.getPairs(pattern);
			pairs.addAll(p);
		}

		pair = pairs.get(0);
	}

	@Test
	public void testIsReliable() {
		fail("Not yet implemented");
	}

	@Test
	public void testLookAheadReliabilityScore() {
		fail("Not yet implemented");
	}

	@Test
	public void testNumberOfQuestionsSatisfyingCondition() {
		fail("Not yet implemented");
	}

	@Test
	public void testPatternEvaluation() {
		PatternEvaluation eval = new PatternEvaluation(pairs, pattern, questionSet);
		assertEquals(pairs, eval.reliablePairRepository);
		assertEquals(pattern, eval.pattern);
		assertEquals(questionSet, eval.questionSet);
	}

	@Test
	public void testReliabilityScore() {
		PatternEvaluation eval = new PatternEvaluation(pairs, pattern, questionSet);
		double score = eval.reliabilityScore(pattern);
		assertTrue(score == 1.0);
	}

	@Test
	public void testReliabilityScoreFinal() {
		fail("Not yet implemented");
	}



	@Test
	public void testSupport() {
		PatternEvaluation eval = new PatternEvaluation(pairs, pattern, questionSet);
	}


}
