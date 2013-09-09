package ax.makila.comparableentititymining.sequentialpatterns.patterns;

import ax.makila.comparableentititymining.sequentialpatterns.Sequence;

public class LexicalSequence extends Sequence implements Pattern {
	public LexicalSequence(String seq) {
		super(seq);
	}

	@Override
	public boolean isLexical() {
		return true;
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