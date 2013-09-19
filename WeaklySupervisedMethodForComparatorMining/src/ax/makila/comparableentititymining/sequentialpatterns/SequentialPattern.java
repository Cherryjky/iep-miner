package ax.makila.comparableentititymining.sequentialpatterns;

import java.util.List;

import ax.makila.comparableentititymining.postagger.CompTaggedWord;

public interface SequentialPattern {
	public List<String> getReplacedComparatorSequence();

	public String getReplacedComparatorText();

	public List<String> getSequence();

	public List<List<CompTaggedWord>> getTaggedWords();

	public List<String> getTokenizedVersion();

	public boolean isGeneralized();

	public boolean isLexical();

	public boolean isSpecialized();

	public boolean matches(SequentialPattern pattern);

	public void setTaggedWords(String comp);

	public String text();
}
