package ax.makila.comparableentititymining.sequentialpatterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import ax.makila.comparableentititymining.Pair;
import ax.makila.comparableentititymining.postagger.CompTaggedWord;
import ax.makila.comparableentititymining.postagger.StanfordPosTagger;
import ax.makila.comparableentititymining.sequentialpatterns.patterns.Pattern;

public class Sequence implements SequentialPattern {

	private List<String> sequence;
	protected String toString;
	private final List<List<String>> tokenizedComparatorSequences = new ArrayList<List<String>>();
	private List<String> comparatorReplacedSequence;
	private String comparatorReplaced;
	private List<List<CompTaggedWord>> taggedWords;
	protected final String text;
	protected List<List<String>> tokens;
	protected List<String> tokenized = new ArrayList<String>();
	public List<List<CompTaggedWord>> phraseChunkedTags;



	/**
	 * Constructor. Tokenizes the input string <tt>text</tt>.
	 * @param text The input string to be considered a sequence
	 */
	public Sequence(String text) {
		this.text = text;
		StringTokenizer tokenizer = new StringTokenizer(text);
		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if(token.matches( "^.+?(\\w|\\d)[\\?\\.,\\!;:].*?$")) {
				String[] split = token.split("[\\?\\.,\\!;:]");
				String subString = split[0];
				String secondString = token.substring(subString.length(), token.length());
				tokenized.add(subString);
				tokenized.add(secondString);
			}
			else {
				tokenized.add(token);
			}
		}
	}





	@Override
	public int compareTo(SequentialPattern o) {
		// TODO Auto-generated method stub
		return toString.compareTo(o.toString());
	}

	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		else if(other instanceof SequentialPattern) {
			SequentialPattern pattern = (SequentialPattern) other;
			return toString.equals(pattern.toString());
		} else {
			return false;
		}
	}

	/**
	 * Given a sequential pattern <tt>pattern</tt> and a tokenized representation of the the input text
	 * that this object contains named <tt>comp</tt>, matches against occurences of $c in <tt>pattern</tt>
	 * against each element in <tt>comp</tt>. When the input matches a part of the string, the position of
	 * $c finds the comparatives and both are then considered to be a comparative pair
	 * @param pattern A pattern used for identifying comparators by a token $c
	 * @param comp A tokenized pos-tagged representation of the input text that this object contains
	 * @return A pair of comparators
	 */
	private Pair<CompTaggedWord, CompTaggedWord> getPairFromSentence(SequentialPattern pattern,
			List<CompTaggedWord> comp) {
		int tokenIndex = 0;
		List<String> tokenized = pattern.getTokenizedVersion();
		List<Pair<CompTaggedWord, Integer>> candidates = new ArrayList<Pair<CompTaggedWord, Integer>>();
		int candidateIndex = 0;
		for(int i = 0; i < comp.size(); i++) {
			if(tokenIndex < tokenized.size()) {
				CompTaggedWord word = comp.get(i);
				String token = tokenized.get(tokenIndex);
				String[] split = token.split("\\/");
				if(split.length > 1) {
					if(split[0].equals("$c") && word.tag().equals(split[1])) {
						candidates.add(candidateIndex, new Pair<CompTaggedWord, Integer>(word, i));
						candidateIndex++;
						tokenIndex++;
					}
					else if(split[0].equals(word.value()) || split[1].equals(word.tag())) {
						tokenIndex++;
					}
					else {
						candidateIndex = 0;
						tokenIndex = 0;
					}
				}
				else {
					if(token.equals("$c")) {
						candidates.add(candidateIndex, new Pair<CompTaggedWord, Integer>(word, i));
						candidateIndex++;
						tokenIndex++;
					}
					else if(token.equals(word.value())) {
						tokenIndex++;
					}
					else {
						candidateIndex = 0;
						tokenIndex = 0;
					}
				}
			} else {
				return null;
			}
		}


		if(candidates.size() < 2) {
			return null;

		}
		int firstIndex = candidates.get(0).y;
		int secondIndex = candidates.get(1).y;

		CompTaggedWord tagged = comp.get(firstIndex);
		tagged.setCompTag(CompTaggedWord.COMP_TAG);
		comp.set(firstIndex, tagged);
		CompTaggedWord tagged2 = comp.get(secondIndex);
		tagged2.setCompTag(CompTaggedWord.COMP_TAG);
		comp.set(secondIndex, tagged2);

		List<String> replaced = new ArrayList<String>();
		for(CompTaggedWord word : comp) {
			if(word.getCompTag() != null) {
				replaced.add("$c");
			}
			else {
				replaced.add(word.value());
			}
		}
		tokenizedComparatorSequences.add(replaced);

		Pair<CompTaggedWord, CompTaggedWord> pair = new Pair<CompTaggedWord,CompTaggedWord>(candidates.get(0).x, candidates.get(1).x);

		return pair;
	}


	/**
	 * Gets comparator pairs from the question. Given a <tt>pattern</tt> all the pairs
	 * are extracted. If no pairs can be extracted, null is returned.
	 * @param pattern The pattern that can extract potential pairs
	 * @return Comparator pairs from the sentence.
	 */
	public List<Pair<CompTaggedWord, CompTaggedWord>> getPairs(SequentialPattern pattern) {
		List<Pair<CompTaggedWord, CompTaggedWord>> pairs = new ArrayList<Pair<CompTaggedWord,CompTaggedWord>>();
		List<List<CompTaggedWord>> tagged = null;
		if(pattern.isLexical()) {
			tagged = taggedWords;
		}
		else if(pattern.isGeneralized() || pattern.isSpecialized()) {
			if(phraseChunkedTags == null) {
				phraseChunker();
			}
			tagged = phraseChunkedTags;
		}
		boolean anyMatch = false;
		for(List<CompTaggedWord> comp : tagged) {
			if(matchesInner(pattern, comp)) {
				Pair<CompTaggedWord, CompTaggedWord> pair = getPairFromSentence(pattern, comp);
				if(pair != null) {
					pairs.add(pair);
					anyMatch = true;
				}

			}
		}
		if(!anyMatch) {
			return null;
		}

		comparatorReplacedSequence = StanfordPosTagger.tokensToSequence(tokenizedComparatorSequences);

		comparatorReplaced = StanfordPosTagger.tokensToString(tokenizedComparatorSequences);
		return pairs;
	}

	@Override
	public List<String> getReplacedComparatorSequence() {
		return comparatorReplacedSequence;
	}

	@Override
	public String getReplacedComparatorText() {
		return comparatorReplaced;
	}


	@Override
	public List<String> getSequence() {
		if(tokens == null) {
			tokens = StanfordPosTagger.tokenizeStringMergeCompAddLimiters(text);
		}
		if(sequence == null) {
			sequence = StanfordPosTagger.tokensToSequence(tokens);
		}
		return sequence;
	}

	@Override
	public List<List<CompTaggedWord>> getTaggedWords() {
		if(taggedWords == null) {
			taggedWords = StanfordPosTagger.tagStringHandleIdentifier(text);
		}
		return taggedWords;
	}

	@Override
	public List<String> getTokenizedVersion() {
		return tokenized;
	}




	@Override
	public List<List<String>> getTokens() {
		return tokens;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
				// if deriving: appendSuper(super.hashCode()).
				append(toString).
				toHashCode();
	}

	@Override
	public boolean hasReplacedComparators() {
		if(comparatorReplacedSequence != null) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean isGeneralized() {
		return false;
	}

	@Override
	public boolean isLexical() {
		return false;
	}


	@Override
	public boolean isSpecialized() {
		return false;
	}

	@Override
	public int length() {
		return tokenized.size();
	}

	/**
	 * Checks if the sequential pattern <tt>pattern</tt> matches the text that this object contains,
	 * If the pattern matches, returns true else false. $c is considered to be a wild-card unless it
	 * has a constraint in the case of a specialized pattern in which case the tag of the word has to
	 * match the tag of the token in the pattern.
	 * @param pattern A sequential pattern to match against the input
	 * @return True if pattern matches, else false
	 */
	@Override
	public boolean matches(SequentialPattern pattern) {
		List<List<CompTaggedWord>> comp = null;
		if(pattern.isLexical()) {
			comp = taggedWords;
		}
		else if(pattern.isGeneralized() || pattern.isSpecialized()) {
			if(phraseChunkedTags == null) {
				phraseChunker();
			}
			comp = phraseChunkedTags;
		}
		for(List<CompTaggedWord> innerList : comp) {
			if(matchesInner(pattern, innerList)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Checks if the sequential pattern <tt>pattern</tt> matches the pos-tagged, tokenized version
	 * of the text that this object contains, <tt>comp</tt>. If the pattern matches, returns true
	 * else false. $c is considered to be a wild-card unless it has a constraint in the case of
	 * a specialized pattern in which case the tag of the word has to match the tag of the token in the
	 * pattern.
	 * @param pattern A sequential pattern to match against the input
	 * @param comp A pos-tagged, tokenized representation of the input text contained in the sequence
	 * @return True if pattern matches, else false
	 */
	private boolean matchesInner(SequentialPattern pattern, List<CompTaggedWord> comp) {
		List<String> tokenized = pattern.getTokenizedVersion();
		//keeps track of where to start in the string
		int tokenIndex = 0;
		boolean match = false;
		for(int i = 0; i < comp.size(); i++) {
			CompTaggedWord word = comp.get(i);
			if(tokenIndex < tokenized.size()) {
				String token = tokenized.get(tokenIndex);
				if(pattern.isLexical()) {
					if(token.equals("$c") || word.value().equals(token)) {
						tokenIndex++;
					}
					else {
						if(tokenIndex != 0) {
							i--;
						}
						tokenIndex = 0;
					}
				}
				else if(pattern.isGeneralized()) {
					String[] split = token.split("\\/");
					if(split.length > 1) {
						if(word.value().equals(split[0]) || word.tag().equals(split[1])) {
							tokenIndex++;
						}
						else {
							if(tokenIndex != 0) {
								i--;
							}
							tokenIndex = 0;
						}
					}
					else if(token.equals("$c") || word.value().equals(token) || word.tag().equals(token)) {
						tokenIndex++;
					}
					else {
						if(tokenIndex != 0) {
							i--;
						}
						tokenIndex = 0;
					}
				}
				else if(pattern.isSpecialized()) {
					if(tokenIndex < tokenized.size()) {
						String[] split = token.split("\\/");
						//Splits $c and its POS-tag or another input that is separated from its
						//pos tag with the delimiter "/"
						if(split.length > 1) {
							if(split[0].equals("$c") && word.tag().equals(split[1])) {
								//The token follows the constraint set by the pattern

								tokenIndex++;
							}
							else if(word.value().equals(split[0]) || word.tag().equals(split[1])) {
								tokenIndex++;
							}
							else {
								if(tokenIndex != 0) {
									i--;
								}
								tokenIndex = 0;
							}
						}
						else {
							if(token.equals("$c") || word.value().equals(token)) {
								tokenIndex++;
							}
							else {
								if(tokenIndex != 0) {
									i--;
								}
								tokenIndex = 0;
							}
						}
					}
					else {
						break;
					}
				}
			}
			else {
				break;
			}
		}
		if(tokenIndex == tokenized.size()) {
			match = true;
		} else {
			match = false;
		}
		return match;
	}

	@SuppressWarnings("unused")
	public void phraseChunker() {
		/*
		 * Heuristic rules
		 * NP* -> NP
		 * NN* -> NN
		 * NN + NNS -> NNS
		 * NP + NPS -> NPS
		 * More + ADJ -> JJR
		 * Most + ADJ -> JJS
		 * ...
		 */
		List<List<CompTaggedWord>> chunked = new ArrayList<List<CompTaggedWord>>();

		List<String> adj = Arrays.asList(new String[]{"JJ", "JJR", "JJS"});

		int count = 0;
		for(List<CompTaggedWord> innerList : taggedWords) {
			count++;
			List<CompTaggedWord> chunks = new ArrayList<CompTaggedWord>();
			ListIterator<CompTaggedWord> it = innerList.listIterator();
			while(it.hasNext()) {
				CompTaggedWord c = null;
				CompTaggedWord word = it.next();
				//rule1
				if(word.tag().equals("NNPS")) {
					c = new CompTaggedWord(word);
					c.setTag("NNP");
				}
				//rule2
				if(word.tag().equals("NNS")) {
					c = new CompTaggedWord(word);
					c.setTag("NN");
				}
				if(it.hasNext()) {
					boolean goBack = true;
					CompTaggedWord next = it.next();
					//rule3
					if(word.tag().equals("NN") && next.tag().equals("NNS")) {
						c = new CompTaggedWord(word);
						c.setValue(word.value() + " " + next.value());
						c.setTag("NNS");
						goBack = false;
					}
					//rule4
					if(word.tag().equals("NNP") && next.tag().equals("NNPS")) {
						c = new CompTaggedWord(word);
						c.setValue(word.value() + " " + next.value());
						c.setTag("NNPS");
						goBack = false;
					}
					//rule 5
					if(word.value().equals("more") && adj.contains(next.tag())) {
						c = new CompTaggedWord(word);
						c.setValue(word.value() + " " + next.value());
						c.setTag("JJR");
						goBack = false;
					}
					//rule 6
					if(word.value().equals("most") && adj.contains(next.tag())) {
						c = new CompTaggedWord(word);
						c.setValue(word.value() + " " + next.value());
						c.setTag("JJS");
						goBack = false;
					}
					//no rule matching
					if(goBack) {
						it.previous();
						CompTaggedWord C = new CompTaggedWord(word);
					}
				}
				if(c == null) {
					c = new CompTaggedWord(word);
				}
				chunks.add(c);
			}
			chunked.add(chunks);
		}
		phraseChunkedTags = chunked;
	}

	/**
	 * Replaces the comparators in a sentence with markers $c. Given a <tt>pair</tt>
	 * the pair is matched againt the sentence and if both of the members of the pair
	 * matches against two words in the question, then these are replaced with $c in.
	 * The replaced question are stored in an internal representation that can be
	 * accessed through {@link #getReplacedComparatorSequence()} and {@link #getReplacedComparatorText()}.
	 * @param pair The pair to be matched against the question.
	 */
	public void replaceComparators(Pair<CompTaggedWord, CompTaggedWord> pair) {
		List<String> replaced = new ArrayList<String>();
		List<String> noReplaced = new ArrayList<String>();
		List<List<CompTaggedWord>> comp = taggedWords;
		List<List<String>> tokenizedString = tokens;
		CompTaggedWord c0 = pair.x;
		boolean checkC0 = true;
		CompTaggedWord c1 = pair.y;
		boolean checkC1 = true;
		//Sanity check
		if(comp.size() != tokenizedString.size()) {
			try {
				throw new Exception("Tagged word list and token list size mismatch!!!");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		for(int i = 0; i < comp.size(); i++) {
			List<CompTaggedWord> compInnerList = comp.get(i);
			List<String> tokenInnerList = tokenizedString.get(i);
			//Another sanity check
			if(compInnerList.size() != tokenInnerList.size()) {
				try {
					throw new Exception("Tagged word list and token list size mismatch!!!");
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
			//Ready to go!
			for(int j = 0; j < compInnerList.size(); j++) {
				CompTaggedWord tag = compInnerList.get(j);
				String token = tokenInnerList.get(j);
				if(token.equals(c0.value()) && checkC0) {
					tag.setCompTag(CompTaggedWord.COMP_TAG);
					replaced.add("$c");
					checkC0 = false;
				} else if(token.equals(c1.value()) && checkC1) {
					tag.setCompTag(CompTaggedWord.COMP_TAG);
					replaced.add("$c");
					checkC1 = false;
				}
				else {
					replaced.add(tag.value());
				}
				noReplaced.add(tag.value());
			}
			if(!checkC0 && !checkC1) {
				tokenizedComparatorSequences.add(replaced);
			}
			else {
				return;
				//tokenizedComparatorSequences.add(noReplaced);
			}
		}
		comparatorReplacedSequence = StanfordPosTagger.tokensToSequence(tokenizedComparatorSequences);

		comparatorReplaced = StanfordPosTagger.tokensToString(tokenizedComparatorSequences);
	}


	/**
	 * Initializes values necessary for a sequence. These are not necessary
	 * for a {@link Pattern}, which is why they are separated from the constructor.
	 * Initializes {@link #tokens}, {@link #toString}, {@link #sequence}, {@link #taggedWords}.
	 */
	public void set() {
		//A tokenized version of the input text with #start and #end added
		tokens = StanfordPosTagger.tokenizeStringMergeCompAddLimiters(text);
		toString = StanfordPosTagger.tokensToString(tokens);
		sequence = StanfordPosTagger.tokensToSequence(tokens);
		taggedWords = StanfordPosTagger.tagStringHandleIdentifierAddLimiters(text);

	}

	@Override
	public void setTaggedWords(List<List<CompTaggedWord>> sequence) {
		taggedWords = sequence;
	}


	@Override
	public String text() {
		return text;
	}

	@Override
	public String toString() {
		if(tokens == null) {
			tokens = StanfordPosTagger.tokenizeStringMergeComp(text);
		}
		if(toString == null) {
			String regex = "(^|.*?\\s)\\$c.*?\\s\\$c[^A-Za-z0-9_$].*?$";
			toString = StanfordPosTagger.tokensToString(tokens, regex);

		}
		return toString;
	}

}
