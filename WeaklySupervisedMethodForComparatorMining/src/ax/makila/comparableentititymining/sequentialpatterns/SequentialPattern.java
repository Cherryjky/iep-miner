package ax.makila.comparableentititymining.sequentialpatterns;

import java.util.List;

import ax.makila.comparableentititymining.postagger.CompTaggedWord;

public interface SequentialPattern extends Comparable<SequentialPattern> {
	/**
	 * Getter for comparatorReplacedSequence
	 * @return comparatorReplacedSequence
	 */
	public List<String> getReplacedComparatorSequence();

	/**
	 * Getter for comparatorReplacedText
	 * @return comparatorReplacedText
	 */
	public String getReplacedComparatorText();

	/**
	 * Getter for sequence
	 * @return sequence.
	 */
	public List<String> getSequence();

	/**
	 * Getter for taggedWords
	 * @return taggedWords
	 */
	public List<List<CompTaggedWord>> getTaggedWords();

	/**
	 * Getter for a tokenized version of the input string
	 * @return A tokenized string
	 */
	public List<String> getTokenizedVersion();

	/**
	 * Getter for tokens
	 * @return Tokens
	 */
	public List<List<String>> getTokens();

	/**
	 * True if the {@link Sequence} has got its comparators replaced by $c, else false.
	 * @return True if comparators replaced, else false.
	 */
	public boolean hasReplacedComparators();

	/**
	 * True if a pattern is a generalized pattern, else false.
	 * @return True if a pattern is a generalized pattern, else false.
	 */
	public boolean isGeneralized();

	/**
	 * True if a pattern is a lexical pattern, else false.
	 * @return True if a pattern is a lexical pattern, else false.
	 */
	public boolean isLexical();

	/**
	 * True if a pattern is a specialized pattern, else false.
	 * @return True if a pattern is a specialized pattern, else false.
	 */
	public boolean isSpecialized();

	/**
	 * Gets the length of the tokenized array
	 * @return The length of the tokenized array
	 */
	public int length();

	/**
	 * Checks if the {@link Sequence} matches the <tt>pattern</tt>. This method is
	 * similar to how a String can match against a regular expression, which mean that it's
	 * enough for any part of the sequence to match against the entire of the pattern.
	 * @param pattern The pattern that is checked if it matches the sequence.
	 * @return True if the pattern matches, else false.
	 */
	public boolean matches(SequentialPattern pattern);

	/**
	 * Setter for taggedWords.
	 * @param comp The new value for taggedWords.
	 */
	public void setTaggedWords(List<List<CompTaggedWord>> comp);

	/**
	 * Returns the text representation of the sequence.
	 * @return The text representation of the sequence.
	 */
	public String text();
}
