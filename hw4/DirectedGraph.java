package edu.iastate.coms3110.hw4;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DirectedGraph<V> implements Graph<V> {
	protected Map<V, Set<V>> adjList = new HashMap<V, Set<V>>();

	/**
	 * 
	 *
	 * @return The number of vertices in the graph.
	 */
	@Override
	public int vertexCount() {
		return adjList.size();
	}

	/**
	 * 
	 *
	 * @return An object for iterating over the graph's vertex set.
	 */
	@Override
	public Iterable<V> getVertices() {
		return adjList.keySet();
	}

	/**
	 * 
	 *
	 * @return The number of edges in the graph.
	 */
	@Override
	public int edgeCount() {
		int edgeCount = 0;
		for (Set<V> n : adjList.values())
			edgeCount += n.size();
		return edgeCount;
	}

	/**
	 * Adds an edge to the graph from s to t.
	 *
	 * @param s A vertex in the graph.
	 * @param t A vertex in the graph.
	 */
	@Override
	public void addEdge(V s, V t) {
		adjList.get(s).add(t);
	}

	/**
	 * Adds a new vertex to the graph.
	 *
	 * @param u The vertex that should be added to the graph.
	 */
	@Override
	public void addVertex(V u) {
		adjList.putIfAbsent(u, new HashSet<V>());
	}

	/**
	 * Gets the neighbors of the input vertex.
	 *
	 * @param v A vertex in the graph.
	 * @return An Iterable of v's neighbors.
	 */
	@Override
	public Iterable<V> getNeighbors(V v) {
		return adjList.get(v);
	}

	/**
	 * 
	 *
	 * @param v
	 * @return True if v is a vertex present in the graph.
	 */
	@Override
	public boolean hasVertex(V v) {
		return adjList.containsKey(v);
	}

	/**
	 * 
	 *
	 * @param from A vertex that is possible in the graph.
	 * @param to   Another vertex that may be present in the graph.
	 * @return True if the graph contains an edge from 'from' to 'to', false
	 *         otherwise.
	 */
	@Override
	public boolean hasEdge(V from, V to) {
		return adjList.getOrDefault(from, new HashSet<V>()).contains(to);
	}

	@Override
	public Map<V, V> dfs() {
		Set<V> explored = new HashSet<V>();
		Map<V, V> pred = new HashMap<V, V>();
		for (V v : adjList.keySet())
			if (!explored.contains(v))
				dfs(v, explored, pred);
		return pred;
	}

	private void dfs(V u, Set<V> explored, Map<V, V> pred) {
		explored.add(u);
		for (V v : adjList.get(u)) {
			if (!explored.contains(v)) {
				pred.put(v, u);
				dfs(v, explored, pred);
			}
		}
	}

	/**
	 * The method uses Dijkstra's Algorithm to solve the single-source shortest
	 * paths problem for the give vertex. Because the graph object itself has no
	 * notion of edge weights, a weight map, equivalent to a cost function, is
	 * provided as input as well. This method should function correctly even if
	 * invoked successively with different source and/or weights.
	 *
	 * You must use the BinaryMinHeap class for the priority queue in your
	 * implementation. You also must not add duplicate vertices to the heap, but
	 * instead you use the keyDecreased method of your heap appropriately.
	 *
	 * @param source  The vertex in the graph for which shortest paths to other
	 *                vertices will be determined.
	 * @param weights A mapping from edges to their weights. Every edge in the graph
	 *                must have a finite non-negative weight entry in this Map.
	 * @return A tuple of maps. The first Map contains the length of the shortest
	 *         path to each vertex reachable from s. The second map encodes the
	 *         predecessor relation necessary for reconstructing a shortest path to
	 *         each reachable vertex.
	 */
	@Override
	public Tuple<Map<V, Double>, Map<V, V>> dijkstras(V source, Map<Tuple<V, V>, Double> weights) {
		if (!adjList.containsKey(source) || weights == null)
			return null;

		Map<V, Double> dist = new HashMap<V, Double>();
		Map<V, V> pred = new HashMap<V, V>();

		BinaryMinHeap<V> priorityQ = new BinaryMinHeap<>((v1, v2) -> Double.compare(dist.get(v1), dist.get(v2)));

		// Set initial distances as infinite
		for (V vertex : getVertices()) {
			dist.put(vertex, Double.MAX_VALUE);
			priorityQ.add(vertex);
		}
		dist.put(source, 0.0); // Source distance is 0
		priorityQ.keyDecreased(source); // Source is root in heap

		// Main loop
		while (priorityQ.size() > 0) { // Not empty
			V currentV = priorityQ.extractMin(); // Get smallest dist

			for (V neighbor : adjList.get(currentV)) {
				Tuple<V, V> edge = Tuple.create(currentV, neighbor); // Get edge
				double weight = weights.get(edge);
				double newDist = dist.get(currentV) + weight;

				if (newDist < dist.get(neighbor)) {
					dist.put(neighbor, newDist);
					pred.put(neighbor, currentV);
					priorityQ.keyDecreased(neighbor);
				}
			} // for
		} // while
//Time complexity is O((V + E) log V), which simplifies to O(E log V) when E < V^2.
		return Tuple.create(dist, pred);
	}
}
