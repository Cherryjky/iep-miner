package ax.makila.comparableentititymining.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ax.makila.comparableentititymining.postagger.CompTaggedWord;
import ax.makila.comparableentititymining.sequentialpatterns.Sequence;
import ax.makila.comparableentititymining.sequentialpatterns.SequentialPattern;
import ax.makila.comparableentititymining.sequentialpatterns.patterns.GeneralizedSequence;
import ax.makila.comparableentititymining.sequentialpatterns.patterns.LexicalSequence;
import ax.makila.comparableentititymining.sequentialpatterns.patterns.SpecializedSequence;
import edu.stanford.nlp.ling.TaggedWord;

public class SequenceTest {

	@Test
	public void testEqualsObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPairs() {
		Sequence seq = new Sequence("is there really a fight of pikachu vs bulbasaur?");
		seq.set();
		SequentialPattern pattern = new SpecializedSequence("#start is there/EX really a/DT fight of/IN $c/NN vs $c/NN? #end");

		seq.getPairs(pattern);
	}

	@Test
	public void testGetReplacedComparatorSequence() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetReplacedComparatorText() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSequence() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTaggedWords() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTokenizedVersion() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasReplacedComparators() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsGeneralized() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsLexical() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsSpecialized() {
		fail("Not yet implemented");
	}

	@Test
	public void testMatches() {
		Sequence seq = new Sequence("test");
		seq.set();
		SequentialPattern pattern = new LexicalSequence("$c vs $c? #end");
		assertFalse(seq.matches(pattern));

		Sequence seq2 = new Sequence("The final showdown cats vs dogs?");
		seq2.set();
		SequentialPattern pattern2 = new LexicalSequence("$c vs $c? #end");
		assertTrue(seq2.matches(pattern2));

		Sequence seq3 = new Sequence("cats vs dogs?");
		seq3.set();
		SequentialPattern pattern3 = new LexicalSequence("cats vs cows? #end");
		assertFalse(seq3.matches(pattern3));

		Sequence seq4 = new Sequence("Is there really a fight of pikachu vs bulbasaur?");
		seq4.set();
		SequentialPattern pattern4 = new SpecializedSequence("#start Is there/EX really a/DT fight of/IN $c/NN vs $c/NN? #end");
		assertTrue(seq4.matches(pattern4));

		SequentialPattern pattern5 = new GeneralizedSequence("#start Is there/EX really/RB a fight of/IN $c vs $c? #end");
		assertTrue(seq4.matches(pattern5));

		SequentialPattern pattern6 = new SpecializedSequence("really a fight of $c/NN vs/CC $c/NN?/. #end");
		assertTrue(seq4.matches(pattern6));

	}

	@Test
	public void testPhraseChunker() {
		Sequence seq = new Sequence("ballista vs catapult?");
		seq.set();
		CompTaggedWord t0 = new CompTaggedWord(new TaggedWord("rule1"));
		t0.setTag("NNPS");

		CompTaggedWord t1 = new CompTaggedWord(new TaggedWord("rule2"));
		t1.setTag("NNS");

		CompTaggedWord t2 = new CompTaggedWord(new TaggedWord("rule3a"));
		t2.setTag("NN");

		CompTaggedWord t3 = new CompTaggedWord(new TaggedWord("rule3b"));
		t3.setTag("NNS");

		CompTaggedWord t4 = new CompTaggedWord(new TaggedWord("rule4a"));
		t4.setTag("NNP");

		CompTaggedWord t5 = new CompTaggedWord(new TaggedWord("rule4b"));
		t5.setTag("NNPS");

		CompTaggedWord t6 = new CompTaggedWord(new TaggedWord("more"));
		t6.setTag("NN");

		CompTaggedWord t7 = new CompTaggedWord(new TaggedWord("beautiful"));
		t7.setTag("JJ");

		CompTaggedWord t8 = new CompTaggedWord(new TaggedWord("most"));
		t8.setTag("NN");

		CompTaggedWord t9 = new CompTaggedWord(new TaggedWord("beautiful"));
		t9.setTag("JJ");

		CompTaggedWord[] temp = {t0, t1, t2, t3, t4, t5, t6, t7, t8, t9};

		List<List<CompTaggedWord>> list = new ArrayList<List<CompTaggedWord>>();
		list.add(Arrays.asList(temp));

		seq.setTaggedWords(list);

		//Expected outcome
		CompTaggedWord e0 = new CompTaggedWord(new TaggedWord("rule1"));
		e0.setTag("NNP");

		CompTaggedWord e1 = new CompTaggedWord(new TaggedWord("rule2"));
		e1.setTag("NN");

		CompTaggedWord e2 = new CompTaggedWord(new TaggedWord("rule3a rule3b"));
		e2.setTag("NNS");

		CompTaggedWord e3 = new CompTaggedWord(new TaggedWord("rule4a rule4b"));
		e3.setTag("NNPS");

		CompTaggedWord e4 = new CompTaggedWord(new TaggedWord("more beautiful"));
		e4.setTag("JJR");

		CompTaggedWord e5 = new CompTaggedWord(new TaggedWord("most beautiful"));
		e5.setTag("JJS");

		List<CompTaggedWord> expected = new ArrayList<CompTaggedWord>(
				Arrays.asList(new CompTaggedWord[]{e0, e1, e2, e3, e4, e5}));

		seq.phraseChunker();
		List<List<CompTaggedWord>> output = seq.phraseChunkedTags;
		List<CompTaggedWord> out = output.get(0);

		assertArrayEquals(expected.toArray(new CompTaggedWord[expected.size()]),
				out.toArray(new CompTaggedWord[out.size()]));
	}

	@Test
	public void testReplaceComparators() {
		fail("Not yet implemented");
	}

	@Test
	public void testSequence() {
		fail("Not yet implemented");
	}

	@Test
	public void testSet() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTaggedWords() {
		fail("Not yet implemented");
	}

	@Test
	public void testText() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

}
