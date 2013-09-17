package ax.makila.comparableentititymining;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Pair<X, Y> {
	public final X x;
	public final Y y;

	public Pair(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
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
		
        Boolean eq0 = new EqualsBuilder().append(x, pair.x).append(y, pair.y).isEquals();
        
        Boolean eq1 = new EqualsBuilder().append(x, pair.y).append(y, pair.x).isEquals();
        
        return eq0 || eq1;
        
	}
	
	@Override
	public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(x).
            append(y).
            toHashCode();
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
		}
		else return false;
	}
}
