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
		tokenizedSequence = StanfordPosTagger.tokenizeString(sequence);
		posTags = StanfordPosTagger.tagString(sequence);
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

}
