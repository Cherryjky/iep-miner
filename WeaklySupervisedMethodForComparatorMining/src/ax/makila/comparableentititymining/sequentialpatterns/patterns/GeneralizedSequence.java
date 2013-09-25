package ax.makila.comparableentititymining.sequentialpatterns.patterns;

import ax.makila.comparableentititymining.postagger.StanfordPosTagger;
import ax.makila.comparableentititymining.sequentialpatterns.Sequence;

public class GeneralizedSequence extends Sequence implements Pattern {
	public GeneralizedSequence(String seq) {
		super(seq);
		tokens = StanfordPosTagger.tokenizeStringMergeComp(text);
		toString = StanfordPosTagger.tokensToString(tokens);
	}

	@Override
	public boolean isGeneralized() {
		return true;
	}

	@Override
	public boolean isLexical() {
		return false;
	}

	@Override
	public boolean isSpecialized() {
		return false;
	}

}