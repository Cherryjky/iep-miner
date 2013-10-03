package ax.makila.comparableentititymining;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class for creating pairs. A pair is considered to have two members, <tt>X</tt> and
 * <tt>Y</tt> which can be of any type. The ordering of the parameters are considered
 * irrelevant and pair(X, Y) = pair(Y, X).
 * @author fredrik
 *
 * @param <X> The first member of the pair
 * @param <Y> The second member of the pair
 */
public class Pair<X, Y> {
	public X x;
	public Y y;

	/**
	 * Constructor. Stores the values <tt>x</tt> and <tt>y</tt> in the object.
	 * @param x The first member of the new pair
	 * @param y The second member of the new pair
	 */
	public Pair(X x, Y y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Checks if the pair contains <tt>o</tt>. The pair contains <tt>o</tt> if either the first
	 * or the second member of the pair matches o.
	 * @param o The object to be checked against the members of the pair
	 * @return True if o matches one of the members of the pair, else false.
	 */
	public boolean contains(Object o) {
		if(o == null) {
			return false;
		}
		else if(o.getClass() == x.getClass() || o.getClass() == y.getClass()) {
			return x.equals(o) || y.equals(o);
		} else {
			return false;
		}
	}

	/**
	 * Equals method. The ordering of the input doesn't matter. The pair pair(1, 2) is
	 * considered to be equal to pair(2, 1).
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		else if(getClass() != o.getClass()) {
			return false;
		}


		@SuppressWarnings("unchecked")
		Pair<X, Y> pair = (Pair<X, Y>) o;

		EqualsBuilder eq0 = new EqualsBuilder();
		EqualsBuilder eq1 = new EqualsBuilder();
		String x0 = null;
		String pairX = null;
		String y0 = null;
		String pairY = null;
		if(x instanceof String) {
			x0 = ((String) x).toLowerCase();
		}

		if(y instanceof String) {
			y0 = ((String) y).toLowerCase();
		}

		if(pair.x instanceof String) {
			pairX = ((String) pair.x).toLowerCase();
		}

		if(pair.y instanceof String) {
			pairY = ((String) pair.y).toLowerCase();
		}

		if(x0 != null) {
			if(pairX != null) {
				eq0.append(x0, pairX);
			}
			else {
				eq0.append(x0, pair.x);
			}
			if(pairY != null) {
				eq1.append(x0, pairY);
			}
			else {
				eq1.append(x0, pair.y);
			}
		} else {
			if(pairX != null) {
				eq0.append(x, pairX);
			}
			else {
				eq0.append(x, pair.x);
			}
			if(pairY != null) {
				eq1.append(x, pairY);
			}
			else {
				eq1.append(x, pair.y);
			}
		}

		if(y0 != null) {
			if(pairX != null) {
				eq1.append(y0, pairX);
			}
			else {
				eq1.append(y0, pair.x);
			}
			if(pairY != null) {
				eq0.append(y0, pairY);
			}
			else {
				eq0.append(y0, pair.y);
			}
		} else {
			if(pairX != null) {
				eq1.append(y, pairX);
			}
			else {
				eq1.append(y, pair.x);
			}
			if(pairY != null) {
				eq0.append(y, pairY);
			}
			else {
				eq0.append(y, pair.y);
			}
		}
		return eq0.isEquals() || eq1.isEquals();

	}

	/**
	 * Generates a hashcode for the object.
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
				// if deriving: appendSuper(super.hashCode()).
				append(x).
				append(y).
				toHashCode();
	}

	/**
	 * To string method. Gives some fancy output.
	 */
	@Override
	public String toString() {
		return "<" + x.toString() + ", " + y.toString() + ">";
	}
}
