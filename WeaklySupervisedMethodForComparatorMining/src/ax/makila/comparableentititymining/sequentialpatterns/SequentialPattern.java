package ax.makila.comparableentititymining.sequentialpatterns;

import java.util.List;

import ax.makila.comparableentititymining.postagger.CompTaggedWord;

public interface SequentialPattern {
	public boolean isLexical();

	public boolean isGeneralized();

	public boolean isSpecialized();
	
	public List<List<CompTaggedWord>> getTaggedWords();
	
	public void setTaggedWords(String comp);
	
}
