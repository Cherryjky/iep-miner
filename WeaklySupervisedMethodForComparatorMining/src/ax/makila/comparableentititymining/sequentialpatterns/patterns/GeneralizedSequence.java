package ax.makila.comparableentititymining.sequentialpatterns.patterns;

import ax.makila.comparableentititymining.sequentialpatterns.Sequence;

public class GeneralizedSequence extends Sequence implements Pattern {
	public GeneralizedSequence(String seq) {
		super(seq);
	}

	@Override
	public boolean isLexical() {
		return false;
	}

	@Override
	public boolean isGeneralized() {
		return true;
	}

	@Override
	public boolean isSpecialized() {
		return false;
	}

}