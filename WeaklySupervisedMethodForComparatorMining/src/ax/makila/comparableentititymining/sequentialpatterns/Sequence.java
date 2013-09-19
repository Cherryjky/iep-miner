package ax.makila.comparableentititymining.sequentialpatterns;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import ax.makila.comparableentititymining.Pair;
import ax.makila.comparableentititymining.postagger.CompTaggedWord;
import ax.makila.comparableentititymining.postagger.StanfordPosTagger;

public class Sequence implements SequentialPattern {

	private List<String> sequence;
	private String toString;
	private final List<List<String>> tokenizedComparatorSequences = new ArrayList<List<String>>();
	private List<String> comparatorReplacedSequence;
	private String comparatorReplaced;
	private List<List<CompTaggedWord>> taggedWords;
	private final String text;
	private List<List<String>> tokens;
	private final List<String> tokenized = new ArrayList<String>();

	public Sequence(String text) {
		this.text = text;
		StringTokenizer tokenizer = new StringTokenizer(text);
		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if(token.matches( "^.+?[\\?\\.,\\!;:].*?$")) {
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
	public boolean equals(Object other) {
		return text.equals(other);
	}

	private Pair<CompTaggedWord, CompTaggedWord> getPairFromSentence(SequentialPattern pattern,
			List<CompTaggedWord> comp) {
		int startIndex = 0;
		int tokenIndex = 0;
		List<String> tokenized = pattern.getTokenizedVersion();
		List<Pair<CompTaggedWord, Integer>> candidates = new ArrayList<Pair<CompTaggedWord, Integer>>();
		int candidateIndex = 0;
		boolean cont = true;
		while(cont) {
			innerloop:
				for(int i = startIndex; i < comp.size(); i++) {
					CompTaggedWord word = comp.get(i);
					String token = tokenized.get(tokenIndex);
					if(token.contains("$c")) {
						if(pattern.isLexical() || pattern.isGeneralized()) {
							candidates.add(candidateIndex, new Pair<CompTaggedWord, Integer>(word, i));
							candidateIndex++;
							tokenIndex++;
						}
						else {
							String[] split = token.split("\\/");
							if(word.tag().equals(split[1])) {
								candidates.add(candidateIndex, new Pair<CompTaggedWord, Integer>(word, i));
								candidateIndex++;
								tokenIndex++;
							}
							else {
								startIndex++;
								tokenIndex = 0;
								candidateIndex = 0;
								break innerloop;
							}
						}
					}
					else if(word.value().equals(token) || word.tag().equals(token)) {
						tokenIndex++;
					}
					else {
						candidateIndex = 0;
						startIndex++;
						tokenIndex = 0;
						break innerloop;
					}

				}
		cont = false;
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

	public List<Pair<CompTaggedWord, CompTaggedWord>> getPairs(SequentialPattern pattern) {
		List<Pair<CompTaggedWord, CompTaggedWord>> pairs = new ArrayList<Pair<CompTaggedWord,CompTaggedWord>>();
		for(List<CompTaggedWord> comp : taggedWords) {
			if(matchesInner(pattern, comp)) {
				Pair<CompTaggedWord, CompTaggedWord> pair = getPairFromSentence(pattern, comp);
				pairs.add(pair);
			}
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
	public boolean matches(SequentialPattern pattern) {
		for(List<CompTaggedWord> innerList : taggedWords) {
			if(matchesInner(pattern, innerList)) {
				return true;
			}
		}
		return false;
	}

	private boolean matchesInner(SequentialPattern pattern, List<CompTaggedWord> comp) {
		List<String> tokenized = pattern.getTokenizedVersion();
		int startIndex = 0;
		int tokenIndex = 0;
		boolean match = false;
		boolean cont = true;
		while(cont) {
			innerloop:
				for(int i = startIndex; i < comp.size(); i++) {
					CompTaggedWord word = comp.get(i);
					String token = tokenized.get(tokenIndex);
					if(pattern.isLexical()) {
						if(tokenIndex < tokenized.size()) {
							if(token.equals("$c") || word.value().equals(token)) {
								match = true;
								tokenIndex++;
							}
							else {
								tokenIndex = 0;
								startIndex++;
								match = false;
								break innerloop;
							}
						}
						else {
							cont = false;
							break;
						}
					}
					else if(pattern.isGeneralized()) {
						if(tokenIndex < tokenized.size()) {
							if(token.equals("$c") || word.value().equals(token) || word.tag().equals(token)) {
								match = true;
								tokenIndex++;
							}
							else {
								tokenIndex = 0;
								startIndex++;
								match = false;
								break innerloop;
							}
						}
						else {
							cont = false;
							break;
						}
					}
					else if(pattern.isSpecialized()) {
						if(tokenIndex < tokenized.size()) {
							String[] split = token.split("\\/");
							if(split.length > 1) {
								if(token.contains("$c")) {
									if(word.tag().equals(split[1])) {
										match = true;
										tokenIndex++;
									}
									else {
										//System.out.println(word + " failed to match " + token);
										tokenIndex = 0;
										startIndex++;
										match = false;
										break innerloop;
									}
								}
								else if(word.value().equals(split[0]) || word.tag().equals(split[1])) {
									//System.out.println(word + " matches " + token);
									match = true;
									tokenIndex++;
								}
								else {
									tokenIndex = 0;
									startIndex++;
									match = false;
									break innerloop;
								}
							}
							else {
								if(token.equals("$c") || word.value().equals(token) || word.tag().equals(token)) {
									match = true;
									tokenIndex++;
								}
								else {
									tokenIndex = 0;
									startIndex++;
									match = false;
									break innerloop;
								}
							}
						}
						else {
							cont = false;
							break;
						}
					}
				}
		cont = false;
		}
		//System.out.println(comp.toString() + " matches " + match);
		return match;
	}

	public void set() {
		//A tokenized version of the input text with #start and #end added
		tokens = StanfordPosTagger.tokenizeStringMergeCompAddLimiters(text);
		toString = StanfordPosTagger.tokensToString(tokens);
		sequence = StanfordPosTagger.tokensToSequence(tokens);
		taggedWords = StanfordPosTagger.tagStringHandleIdentifier(text);

	}

	public void setComparatorReplaced(List<String> replacedComparators) {
		List<List<String>> temp = new ArrayList<List<String>>();
		temp.add(replacedComparators);
		String text = StanfordPosTagger.tokensToString(temp);
		List<List<String>> tokens = StanfordPosTagger.tokenizeStringMergeComp(text);
		comparatorReplacedSequence = StanfordPosTagger.tokensToSequence(tokens);
	}

	@Override
	public void setTaggedWords(String sequence) {
		taggedWords = StanfordPosTagger.tagStringHandleIdentifier(sequence);
	}

	@Override
	public String text() {
		return text;
	}

	@Override
	public String toString() {
		if(tokens == null) {
			tokens = StanfordPosTagger.tokenizeStringMergeCompAddLimiters(text);
		}
		if(toString == null) {
			toString = StanfordPosTagger.tokensToString(tokens);
		}
		return toString;
	}



}
