package ax.makila.comparableentititymining.postagger;

import edu.stanford.nlp.ling.TaggedWord;

public class CompTaggedWord {
	public static final String COMP_TAG = "$c";
	private final TaggedWord tag;
	private String compTag;

	public CompTaggedWord(CompTaggedWord comp) {
		this.tag = comp.tag;
		this.compTag = comp.compTag;
	}

	public CompTaggedWord(TaggedWord tag) {
		this.tag = tag;
	}

	public CompTaggedWord(TaggedWord tag, String compTag) {
		this.tag = tag;
		this.compTag = compTag;
	}

	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		else if(other.getClass() != this.getClass()) {
			return false;
		}
		else {
			CompTaggedWord comp = (CompTaggedWord) other;
			return tag.equals(comp.tag);
		}
	}

	public String getCompTag() {
		return compTag;
	}

	public void setCompTag(String compTag) {
		this.compTag = compTag;
	}

	public void setTag(String pos) {
		tag.setTag(pos);
	}

	public void setValue(String value) {
		tag.setValue(value);
	}

	public String tag() {
		return tag.tag();
	}

	@Override
	public String toString() {
		return tag.toString();

	}

	public String value() {
		return tag.value();
	}





}
