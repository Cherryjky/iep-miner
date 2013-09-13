package ax.makila.comparableentititymining.sequentialpatterns;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;

public interface SequentialPattern {
	public boolean isLexical();

	public boolean isGeneralized();

	public boolean isSpecialized();
	
	public List<ArrayList<TaggedWord>> getPosTags();
	
	public List<List<String>> getTokenizedSequence();
	
	public void setPosTags(List<ArrayList<TaggedWord>> posTags);
	
}
