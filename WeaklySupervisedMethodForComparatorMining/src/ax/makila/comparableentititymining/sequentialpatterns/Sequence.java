package ax.makila.comparableentititymining.sequentialpatterns;

public class Sequence implements SequentialPattern {

	public String sequence = null;

	public Sequence(String seq) {
		sequence = seq;
	}

	@Override
	public boolean equals(Object other) {
		return sequence.equals(other);
	}

	@Override
	public String toString() {
		return sequence;
	}

	@Override
	public boolean isLexical() {
		return false;
	}

	@Override
	public boolean isGeneralized() {
		return false;
	}

	@Override
	public boolean isSpecialized() {
		return false;
	}

}
