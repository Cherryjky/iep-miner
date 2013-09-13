package ax.makila.comparableentititymining.sequentialpatterns;

import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;

public interface SequentialPattern {
	public boolean isLexical();

	public boolean isGeneralized();

	public boolean isSpecialized();
	
	public List<TaggedWord> getPosTags();
	
	public List<String> getTokenizedSequence();
	
	public void setPosTags(List<TaggedWord> posTags);
	
}
