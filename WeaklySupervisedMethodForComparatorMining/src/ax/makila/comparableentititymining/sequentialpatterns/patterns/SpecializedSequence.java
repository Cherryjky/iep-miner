package ax.makila.comparableentititymining.sequentialpatterns.patterns;

import ax.makila.comparableentititymining.postagger.StanfordPosTagger;
import ax.makila.comparableentititymining.sequentialpatterns.Sequence;

public class SpecializedSequence extends Sequence implements Pattern {
	public SpecializedSequence(String seq) {
		super(seq);
		tokens = StanfordPosTagger.tokenizeStringMergeComp(text);
		toString = StanfordPosTagger.tokensToString(tokens);
		System.out.println(toString);
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
		return true;
	}
}
