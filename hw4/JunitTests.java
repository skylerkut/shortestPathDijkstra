package edu.iastate.coms3110.hw4;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class JunitTests {

	private Graph<String> g;
	private String v1 = "a";
	private String v2 = "b";
	private Map<Tuple<String, String>, Double> weights;

	@Before
	public void setUp() {
		g = new DirectedGraph<>();
		g.addVertex(v1);
		g.addVertex(v2);
		weights = new HashMap<>();
	}

	@Test
	public void testDist() {
		g.addEdge(v1, v2);
		weights.put(Tuple.create(v1, v2), 2.0);

		var distsAndPreds = g.dijkstras(v1, weights);
		Map<String, Double> dists = distsAndPreds.getFirst();

		System.out.println("\nTest 1 Dists " + dists);

		// Assert distances for each vertices
		assertEquals(0.0, dists.get(v1), 0); // Distance from v1 to v1 should be 0.0
		assertEquals(2.0, dists.get(v2), 0); // Distance from v1 to v2 should be 2.0
	}

	@Test
	public void testPred() {
		g.addEdge(v1, v2);
		weights.put(Tuple.create(v1, v2), 2.0);

		var distsAndPreds = g.dijkstras(v1, weights);
		Map<String, String> preds = distsAndPreds.getSecond();

		System.out.println("\nTest 1 Preds " + preds);

		assertEquals(1, preds.size()); // One mapping for one pair of vertices
		assertTrue(preds.containsKey(v2)); // Ensure v2 is in the map
		assertEquals("a", preds.get(v2)); // v2's predecessor should be v1
	}

	@Test
	public void testDist2() {

		g.addEdge(v1, v2);
		g.addEdge(v2, v1);

		weights.put(Tuple.create(v1, v2), 2.0);
		weights.put(Tuple.create(v2, v1), 3.0);

		var distsAndPreds = g.dijkstras(v2, weights);
		Map<String, Double> dists = distsAndPreds.getFirst();

		System.out.println("Test 2 Dists " + dists);

		// Assert distances for each vertices
		assertEquals(3.0, dists.get(v1), 0);
		assertEquals(0.0, dists.get(v2), 0);

	}

	@Test
	public void testPred2() {
		g.addEdge(v1, v2);
		g.addEdge(v2, v1);

		weights.put(Tuple.create(v1, v2), 2.0);
		weights.put(Tuple.create(v2, v1), 3.0);

		var distsAndPreds = g.dijkstras(v2, weights);
		Map<String, String> preds = distsAndPreds.getSecond();

		System.out.println("\nTest 2 Preds " + preds);

		assertEquals(1, preds.size()); // One mapping for one pair of vertices
		assertTrue(preds.containsKey(v1)); // Ensure v1 is in the map
		assertEquals("b", preds.get(v1)); // v1's predecessor should be v2
	}

	@Test
	public void testHeap() {
		HashMap<String, Integer> m = new HashMap<String, Integer>();
		String y = "obj2";
		String x = "obj1";

		int j = 11;
		int k = 12;

		m.put(y, k);
		m.put(x, j);

		var q = new BinaryMinHeap<String>((l, r) -> Integer.compare(m.get(l), m.get(r)));

		q.add(x);
		q.add(y);
		m.put(y, k - 2);
		q.keyDecreased(y);
		assertEquals("obj2", q.extractMin());
	}

}
