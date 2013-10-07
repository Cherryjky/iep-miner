package ax.makila.comparableentititymining.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ax.makila.comparableentititymining.Pair;
import ax.makila.comparableentititymining.postagger.CompTaggedWord;
import ax.makila.comparableentititymining.sequentialpatterns.Sequence;
import ax.makila.comparableentititymining.sequentialpatterns.SequentialPattern;
import ax.makila.comparableentititymining.sequentialpatterns.patterns.GeneralizedSequence;
import ax.makila.comparableentititymining.sequentialpatterns.patterns.LexicalSequence;
import ax.makila.comparableentititymining.sequentialpatterns.patterns.SpecializedSequence;
import edu.stanford.nlp.ling.TaggedWord;

public class SequenceTest {
	Pair<CompTaggedWord, CompTaggedWord> testPairCatsDogs;
	Pair<CompTaggedWord, CompTaggedWord> testPairPikachuBulbasaur;
	Pair<CompTaggedWord, CompTaggedWord> testPairCatsCats;

	@Before
	public void setUp() {
		TaggedWord t0 = new TaggedWord("cats");
		TaggedWord t1 = new TaggedWord("dogs");
		TaggedWord t2 = new TaggedWord("pikachu");
		TaggedWord t3 = new TaggedWord("bulbasaur");

		CompTaggedWord c0 = new CompTaggedWord(t0);
		CompTaggedWord c1 = new CompTaggedWord(t1);
		CompTaggedWord c2 = new CompTaggedWord(t2);
		CompTaggedWord c3 = new CompTaggedWord(t3);

		testPairCatsDogs = new Pair<CompTaggedWord, CompTaggedWord>(c0, c1);
		testPairPikachuBulbasaur = new Pair<CompTaggedWord, CompTaggedWord>(c2, c3);
		testPairCatsCats = new Pair<CompTaggedWord, CompTaggedWord>(c0, c0);
	}

	@Test
	public void testEqualsObject() {
		SequentialPattern pattern1 = new GeneralizedSequence("Is there really a fight of $c vs $c? #end");
		SequentialPattern pattern2 = new LexicalSequence("Is there really a fight of $c vs $c? #end");

		assertEquals(pattern1, pattern2);
	}

	@Test
	public void testGetPairs() {
		Sequence seq = new Sequence("is there really a fight of pikachu vs bulbasaur?");
		seq.set();
		SequentialPattern pattern = new SpecializedSequence("Is there really a/DT fight/NN of $c/NN vs/CC $c/NN?/. #end");

		seq.getPairs(pattern);
	}

	@Test
	public void testGetReplacedComparatorSequence() {
		Sequence seq = new Sequence("cats vs dogs");
		seq.set();
		seq.replaceComparators(testPairCatsDogs);
		List<String> s = seq.getReplacedComparatorSequence();
		String expected = "[#start $c vs $c #end]";
		assertEquals(s.toString(), expected);

		seq = new Sequence("cats vs dogs");
		seq.set();
		seq.replaceComparators(testPairPikachuBulbasaur);
		s = seq.getReplacedComparatorSequence();
		assertNull(s);

		seq = new Sequence("cats vs dogs");
		seq.set();
		seq.replaceComparators(testPairCatsCats);
		s = seq.getReplacedComparatorSequence();
		assertNull(s);
	}

	@Test
	public void testGetReplacedComparatorText() {
		Sequence seq = new Sequence("cats vs dogs");
		seq.set();
		seq.replaceComparators(testPairCatsDogs);
		String s = seq.getReplacedComparatorText();
		String expected = "#start $c vs $c #end";
		assertEquals(s, expected);

		seq = new Sequence("cats vs dogs");
		seq.set();
		seq.replaceComparators(testPairPikachuBulbasaur);
		s = seq.getReplacedComparatorText();
		expected = "#start cats vs dogs #end";
		assertNull(s);

		seq = new Sequence("cats vs dogs");
		seq.set();
		seq.replaceComparators(testPairCatsCats);
		s = seq.getReplacedComparatorText();
		assertNull(s);
	}

	@Test
	public void testGetSequence() {
		Sequence seq = new Sequence("cats vs dogs");
		seq.set();
		List<String> s = seq.getSequence();
		assertEquals("[#start cats vs dogs #end]", s.toString());

		seq = new Sequence("cats vs dogs. Pikachu is cool!");
		seq.set();
		s = seq.getSequence();
		String s0 = s.get(0);
		String s1 = s.get(1);

		assertEquals("#start cats vs dogs. #end", s0);
		assertEquals("#start Pikachu is cool! #end", s1);
	}

	@Test
	public void testGetTaggedWords() {
		Sequence seq = new Sequence("cat vs dog");
		seq.set();
		List<List<CompTaggedWord>> list = seq.getTaggedWords();
		String expected = "[[#start/#, cat/NN, vs/CC, dog/NN, #end/#]]";

		assertEquals(list.toString(), expected);

	}

	@Test
	public void testGetTokenizedVersion() {
		Sequence seq = new Sequence("cats vs dogs");
		seq.set();

		List<String> tokenized = seq.getTokenizedVersion();
		String[] expected = {"cats", "vs", "dogs"};

		assertArrayEquals(expected, tokenized.toArray(new String[tokenized.size()]));

	}

	@Test
	public void testHasReplacedComparators() {
		Sequence seq = new Sequence("cats vs dogs");
		seq.set();
		seq.replaceComparators(testPairCatsDogs);
		assertTrue(seq.hasReplacedComparators());

		seq = new Sequence("cats vs dogs");
		seq.set();
		seq.replaceComparators(testPairCatsCats);
		assertFalse(seq.hasReplacedComparators());
	}

	@Test
	public void testIsGeneralized() {
		SequentialPattern pat = new GeneralizedSequence("cat vs dog");

		assertTrue(pat.isGeneralized());
		assertFalse(pat.isSpecialized());
		assertFalse(pat.isLexical());
	}

	@Test
	public void testIsLexical() {
		SequentialPattern pat = new LexicalSequence("cat vs dog");

		assertTrue(pat.isLexical());
		assertFalse(pat.isSpecialized());
		assertFalse(pat.isGeneralized());
	}

	@Test
	public void testIsSpecialized() {
		SequentialPattern pat = new SpecializedSequence("cat vs dog");

		assertTrue(pat.isSpecialized());
		assertFalse(pat.isGeneralized());
		assertFalse(pat.isLexical());
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

		SequentialPattern pattern7 = new SpecializedSequence("Is there really/RB a/DT fight of $c/NN vs/CC $c/NN?/. #end");
		assertTrue(seq4.matches(pattern7));

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
		Sequence seq = new Sequence("cats vs dogs");
		seq.set();
		seq.replaceComparators(testPairCatsDogs);
		List<String> s = seq.getReplacedComparatorSequence();
		String expected = "[#start $c vs $c #end]";
		assertEquals(s.toString(), expected);

		seq = new Sequence("cats vs dogs");
		seq.set();
		seq.replaceComparators(testPairPikachuBulbasaur);
		s = seq.getReplacedComparatorSequence();
		expected = "[#start cats vs dogs #end]";
		assertNull(s);
		//assertEquals(s.toString(), expected);

		seq = new Sequence("cats vs dogs");
		seq.set();
		seq.replaceComparators(testPairCatsCats);
		s = seq.getReplacedComparatorSequence();

		assertNull(s);

		seq = new Sequence("cats vs dogs");
		seq.set();
		seq.replaceComparators(testPairCatsDogs);
		String s0 = seq.getReplacedComparatorText();
		expected = "#start $c vs $c #end";
		assertEquals(s0, expected);

		seq = new Sequence("cats vs dogs");
		seq.set();
		seq.replaceComparators(testPairPikachuBulbasaur);
		s0 = seq.getReplacedComparatorText();

		assertNull(s0);

		seq = new Sequence("cats vs dogs");
		seq.set();
		seq.replaceComparators(testPairCatsCats);
		s0 = seq.getReplacedComparatorText();
		assertNull(s0);

	}

	@Test
	public void testSequence() {
		Sequence seq = new Sequence("cat vs dog");

		assertNotNull(seq);
		assertNotNull(seq.text());
		assertEquals("cat vs dog", seq.text());

		assertNotNull(seq.getTokenizedVersion());
	}

	@Test
	public void testSet() {
		Sequence seq = new Sequence("cat vs dog");
		seq.set();
		List<List<String>> tokens = seq.getTokens();
		assertEquals("[[#start, cat, vs, dog, #end]]", tokens.toString());

		String toString = seq.toString();
		assertEquals("#start cat vs dog #end", toString);

		List<String> sequence = seq.getSequence();
		assertEquals("[#start cat vs dog #end]", sequence.toString());

		List<List<CompTaggedWord>> list = seq.getTaggedWords();
		String expected = "[[#start/#, cat/NN, vs/CC, dog/NN, #end/#]]";
		assertEquals(list.toString(), expected);

	}

	@Test
	public void testSetTaggedWords() {
		Sequence seq = new Sequence("cat vs dog");
		seq.set();
		List<List<CompTaggedWord>> list = seq.getTaggedWords();
		list.get(0).add(new CompTaggedWord(new TaggedWord("test")));
		seq.setTaggedWords(list);

		seq = new Sequence("cat vs dog");
		seq.set();

		assertFalse(list.equals(seq.getTaggedWords()));
	}

	@Test
	public void testText() {
		Sequence seq = new Sequence("cat vs dog");

		assertEquals("cat vs dog", seq.text());
	}

	@Test
	public void testToString() {
		SequentialPattern pattern = new SpecializedSequence("#start is there/EX really a/DT fight of/IN $c/NN vs $c/NN?/. #end");
		String expected = "#start is there/EX really a/DT fight of/IN $c/NN vs $c/NN?/. #end";
		String out = pattern.toString();
		assertEquals(expected, out);
	}

}
