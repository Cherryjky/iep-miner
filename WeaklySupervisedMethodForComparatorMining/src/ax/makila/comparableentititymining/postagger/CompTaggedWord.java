package ax.makila.comparableentititymining.postagger;

import edu.stanford.nlp.ling.TaggedWord;

/**
 * An extended version of the TaggedWord class in the Stanford PosTagger package. Adds
 * Extra tags for comparators which means that a word has a value, a tag and a compTag (if
 * the word is a comparator).
 * @author fredrik
 *
 */
public class CompTaggedWord {
	/**
	 * The tag for a comparative word
	 */
	public static final String COMP_TAG = "$c";
	private final TaggedWord tag;
	private String compTag;

	/**
	 * Constructor. Basically gives the possibility to clone a CompTaggedWord
	 * @param comp The CompTaggedWord to be cloned
	 */
	public CompTaggedWord(CompTaggedWord comp) {
		this.tag = comp.tag;
		this.compTag = comp.compTag;
	}

	/**
	 * Constructor. Creates a new CompTaggedWord from an existing TaggedWord <tt>tag</tt>
	 * @param tag A TaggedWord.
	 */
	public CompTaggedWord(TaggedWord tag) {
		this.tag = tag;
	}

	/**
	 * Constructor. Creates a new CompTaggedWord from an existing TaggedWord <tt>tag</tt> as
	 * well as sets its comparator tag to <tt>compTag</tt>.
	 * @param tag A TaggedWord.
	 * @param compTag The comparator tag for the CompTaggedWord
	 */
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

	/**
	 * Returns the comparator tag for a word.
	 * @return
	 */
	public String getCompTag() {
		return compTag;
	}

	/**
	 * Sets the comparator tag for a word.
	 * @param compTag The comparator tag for a word
	 */
	public void setCompTag(String compTag) {
		this.compTag = compTag;
	}

	/**
	 * Sets the POS tag for a word
	 * @param pos The POS tag for a word
	 */
	public void setTag(String pos) {
		tag.setTag(pos);
	}

	/**
	 * Sets the value for a word. Basically this changes the word as the value is considered
	 * to be a word's text representation.
	 * @param value The new value for a word.
	 */
	public void setValue(String value) {
		tag.setValue(value);
	}

	/**
	 * Gets the tag of a word.
	 * @return
	 */
	public String tag() {
		return tag.tag();
	}

	@Override
	public String toString() {
		return tag.toString();

	}

	/**
	 * Gets the value of a word. A value is considered to be the word's text representation.
	 * @return The value of a word.
	 */
	public String value() {
		return tag.value();
	}





}
