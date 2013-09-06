package makila.comparableentititymining.sequentialpatterns.patterns;

import makila.comparableentititymining.sequentialpatterns.Sequence;

public class SpecializedSequence extends Sequence implements Pattern {
	public SpecializedSequence(String seq) {
		super(seq);
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
		return true;
	}
}
