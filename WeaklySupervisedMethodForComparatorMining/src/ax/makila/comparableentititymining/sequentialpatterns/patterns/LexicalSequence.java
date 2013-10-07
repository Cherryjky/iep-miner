package ax.makila.comparableentititymining.sequentialpatterns.patterns;

import ax.makila.comparableentititymining.postagger.StanfordPosTagger;
import ax.makila.comparableentititymining.sequentialpatterns.Sequence;

public class LexicalSequence extends Sequence implements Pattern {
	public LexicalSequence(String seq) {
		super(seq);
		tokens = StanfordPosTagger.tokenizeStringMergeComp(text);
		toString = StanfordPosTagger.tokensToString(tokens);
	}

	@Override
	public boolean isGeneralized() {
		return false;
	}

	@Override
	public boolean isLexical() {
		return true;
	}

	@Override
	public boolean isSpecialized() {
		return false;
	}

}