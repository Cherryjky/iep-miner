package ax.makila.comparableentititymining.sequentialpatterns.patterns;

import java.util.List;
import java.util.ListIterator;

import ax.makila.comparableentititymining.postagger.StanfordPosTagger;
import ax.makila.comparableentititymining.sequentialpatterns.Sequence;

public class SpecializedSequence extends Sequence implements Pattern {
	public SpecializedSequence(String seq) {
		super(seq);
		tokens = StanfordPosTagger.tokenizeStringMergeComp(text);
		toString = StanfordPosTagger.tokensToString(tokens);
		List<String> tokenizedVer = getTokenizedVersion();

		ListIterator<String> it = tokenizedVer.listIterator();
		while(it.hasNext()) {
			String current = it.next();
			if((current.equals("?") || current.equals("!") || current.equals(".")) && it.hasNext()) {
				String next = it.next();
				if(next.equals("/") && it.hasNext()) {
					String superNext = it.next();
					if(superNext.equals(".") && it.hasNext()) {
						it.remove();
						it.previous();
						it.remove();
						it.previous();
						it.set(current + next + superNext);
					}
				}
			}
		}
		tokenized = tokenizedVer;


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
