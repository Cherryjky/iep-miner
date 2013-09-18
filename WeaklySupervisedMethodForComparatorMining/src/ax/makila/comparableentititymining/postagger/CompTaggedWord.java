package ax.makila.comparableentititymining.postagger;

import edu.stanford.nlp.ling.TaggedWord;

public class CompTaggedWord {
	public static final String COMP_TAG = "$c";
	private TaggedWord tag;
	private String compTag;
	
	public CompTaggedWord(TaggedWord tag) {
		this.tag = tag;
	}
	
	public CompTaggedWord(TaggedWord tag, String compTag) {
		this.tag = tag;
		this.compTag = compTag;
	}
	
	public void setCompTag(String compTag) {
		this.compTag = compTag;
	}
	
	public String getCompTag() {
		return compTag;
	}
	
	public String value() {
		return tag.value();
	}
	
	public void setValue(String value) {
		tag.setValue(value);
	}
	
	public String tag() {
		return tag.tag();
	}
	
	public void setTag(String pos) {
		tag.setTag(pos);
	}
	
	@Override
	public String toString() {
		if(compTag == null) {
			return tag.toString();
		}
		else {
			return tag.toString("/") + compTag;
		}
		
	}
	
	
	

	
}
