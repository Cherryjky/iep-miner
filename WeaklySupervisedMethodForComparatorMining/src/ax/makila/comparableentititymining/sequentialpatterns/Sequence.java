package ax.makila.comparableentititymining.sequentialpatterns;

import java.util.ArrayList;
import java.util.List;

import ax.makila.comparableentititymining.postagger.StanfordPosTagger;
import edu.stanford.nlp.ling.TaggedWord;

public class Sequence implements SequentialPattern {

	public String sequence = null;
	public List<String> tokenizedSequence = new ArrayList<String>();
	public List<TaggedWord> posTags = new ArrayList<TaggedWord>();
	public List<ArrayList<TaggedWord>> taggedWords = null;

	public Sequence(String seq) {
		sequence = seq;
		List<List<String>> tokens = StanfordPosTagger.tokenizeStringMergeComp(sequence);
		for(List<String> token : tokens) {
			tokenizedSequence.addAll(token);
		}
		List<ArrayList<TaggedWord>> taggedWords = StanfordPosTagger.tagStringHandleIdentifier(sequence);
		for(List<TaggedWord> pos : taggedWords) {
			posTags.addAll(pos);
		}
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
	public List<TaggedWord> getPosTags() {
		return posTags;
	}

	@Override
	public List<String> getTokenizedSequence() {
		return tokenizedSequence;
	}

	@Override
	public void setPosTags(List<TaggedWord> posTags) {
		this.posTags = posTags;
		
	}

	@Override
	public List<ArrayList<TaggedWord>> getTaggedWords() {
		return taggedWords;
	}

}
