package ax.makila.comparableentititymining.sequentialpatterns;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;
import ax.makila.comparableentititymining.postagger.StanfordPosTagger;

public class Sequence implements SequentialPattern {

	public String sequence = null;
	public List<List<String>> tokenizedSequence = null;
	public List<ArrayList<TaggedWord>> posTags = null;

	public Sequence(String seq) {
		sequence = seq;
		tokenizedSequence = StanfordPosTagger.tokenizeStringMergeComp(sequence);
		posTags = StanfordPosTagger.tagStringHandleIdentifier(sequence);
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
	public List<ArrayList<TaggedWord>> getPosTags() {
		return posTags;
	}

	@Override
	public List<List<String>> getTokenizedSequence() {
		return tokenizedSequence;
	}

	@Override
	public void setPosTags(List<ArrayList<TaggedWord>> posTags) {
		this.posTags = posTags;
		
	}

}
