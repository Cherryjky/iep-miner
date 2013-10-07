package ax.makila.comparableentititymining.sequentialpatterns.patterns;

public interface Pattern {
	/**
	 * True if the pattern is generalized, else false.
	 * @return True if the pattern is generalized, else false.
	 */
	public boolean isGeneralized();

	/**
	 * True if the pattern is lexical, else false.
	 * @return True if the pattern is lexical, else false.
	 */
	public boolean isLexical();

	/**
	 * True if the patterns is specialized, else false.
	 * @return True if the patterns is specialized, else false.
	 */
	public boolean isSpecialized();
}
