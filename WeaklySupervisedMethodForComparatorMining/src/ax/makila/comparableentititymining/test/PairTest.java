package ax.makila.comparableentititymining.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ax.makila.comparableentititymining.Pair;

public class PairTest {

	@Test
	public void testHashCode() {
		Pair<String, String> pair = new Pair<String, String>("x", "y");
		int hash = pair.hashCode();
		Pair<String, String> pair2 = new Pair<String, String>("x", "y");
		int hash2 = pair2.hashCode();
		Pair<String, String> pair3 = new Pair<String, String>("y", "x");
		int hash3 = pair3.hashCode();
		
		assertNotEquals(hash, hash3);
		assertEquals(hash, hash2);
		
		
	}

	@Test
	public void testPair() {
		Pair<String, String> pair = new Pair<String, String>("x", "y");
		assertNotNull(pair);
		assertNotNull(pair.x);
		assertNotNull(pair.y);
		assertEquals(pair.x, "x");
		assertEquals(pair.y, "y");
	}

	@Test
	public void testEqualsObject() {
		Pair<String, String> a = new Pair<String, String>("x", "y");
		Pair<String, String> b = new Pair<String, String>("x", "y");
		Pair<String, String> c = new Pair<String, String>("y", "x");
		Pair<String, String> d = new Pair<String, String>("y", "z");
		Pair<String, String> e = new Pair<String, String>("x", "z");
 		assertEquals(a, b);
		assertEquals(b, a);
		assertEquals(a, c);
		assertNotEquals(a, d);
		assertNotEquals(a, e);
		
		
		Pair<Integer, Integer> f = new Pair<Integer, Integer>(17, 31);
		Pair<Integer, Integer> g = new Pair<Integer, Integer>(17, 31);
		Pair<Integer, Integer> h = new Pair<Integer, Integer>(31, 17);
		Pair<Integer, Integer> i = new Pair<Integer, Integer>(31, 18);
		Pair<Integer, Integer> j = new Pair<Integer, Integer>(17, 32);
		
		assertEquals(f, g);
		assertEquals(g, f);
		assertEquals(f, h);
		assertNotEquals(f, i);
		assertNotEquals(f, j);
		
		assertNotEquals(f, null);
		assertNotEquals(null, f);
		
		
	}

	@Test
	public void testContains() {
		Pair<String, String> a = new Pair<String, String>("a", "b");
		assertTrue(a.contains("a"));
		assertFalse(a.contains("c"));
		assertFalse(a.contains(null));
		assertTrue(a.contains("b"));
	}

}
