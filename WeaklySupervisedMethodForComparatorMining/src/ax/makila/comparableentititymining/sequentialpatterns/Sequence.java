package ax.makila.comparableentititymining.sequentialpatterns;

import java.util.List;

import ax.makila.comparableentititymining.postagger.CompTaggedWord;
import ax.makila.comparableentititymining.postagger.StanfordPosTagger;

public class Sequence implements SequentialPattern {

	public String sequence = null;
	public List<List<CompTaggedWord>> taggedWords = null;

	public Sequence(String seq) {
		sequence = seq;
		//List<List<String>> tokens = StanfordPosTagger.tokenizeStringMergeComp(sequence);
		//taggedWords = StanfordPosTagger.tagStringHandleIdentifier(sequence);
	}
	

	@Override
	public boolean equals(Object other) {
		return sequence.equals(other);
	}

	@Override
	public String toString() {
		return sequence;
	}

	@Override
	public boolean isLexical() {
		return false;
	}

	@Override
	public boolean isGeneralized() {
		return false;
	}

	@Override
	public boolean isSpecialized() {
		return false;
	}

	
	@Override
	public List<List<CompTaggedWord>> getTaggedWords() {
		return taggedWords;
	}


	@Override
	public void setTaggedWords(String sequence) {
		taggedWords = StanfordPosTagger.tagStringHandleIdentifier(sequence);
	}

}
