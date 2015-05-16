// Implementation of graph search/traversal methods
// package GraphGame;

import java.util.Iterator;
import java.util.Stack;

public class FindPath<V> {

	/*     METHODS     */

	// Returns true if there is a ”frozen” path between vertices v and u in graph g
	public boolean markedPathExists(Graph<V> g, Vertex<V> v, Vertex<V> u) {
		
		// Iterate through graph's vertices and reset all to 'unvisited'
		Iterator<Vertex<V>> iter = g.vertices();
		while(iter.hasNext()) { iter.next().setMarker(false); }

		// Create stack to store path between start and end vertices
		Stack<Vertex<V>> s = new Stack<Vertex<V>>();
		Stack<Vertex<V>> stack = pathDFS(g, v, u, s, true);
		
		// If path exists, return true; otherwise false
		if (stack != null)
			return true;
		
		return false;

	}

	// If there is a path between vertices u and v in graph g, returns an iterator of path; otherwise, returns null
	public Iterator<Vertex<V>> givePath(Graph<V> g, Vertex<V> v, Vertex<V> u) {

		// Iterate through graph's vertices and reset all to 'unvisited'
		Iterator<Vertex<V>> iter = g.vertices();
		while(iter.hasNext()) { iter.next().setMarker(false); }

		// Create stack to store path between start and end vertices
		Stack<Vertex<V>> s = new Stack<Vertex<V>>();
		Stack<Vertex<V>> stack = pathDFS(g, v, u, s, false);
		
		// If path exists, return its iterator; otherwise null
		if (stack != null)
			return stack.iterator();
		
		return null;
	}


	/*     HELPER METHODS     */

	// Traverse graph via depth-first search to determine a path between vertices v and u; return stack holding path or empty stack if no path was found
	// Condition parameter determines whether edge status is relevant (ie. true if path must only contain frozen edges; false if irrelevant)
	private Stack<Vertex<V>> pathDFS(Graph<V> g, Vertex<V> v, Vertex<V> u, Stack<Vertex<V>> stack, boolean condition) {

		v.setMarker(true);			// Mark current vertex as 'visited'
		stack.push(v);				// Store vertex in path stack
		if (v == u) 				// If destination vertex has been reached, return current stack
			return stack;
		 
		Edge<V> edge;
		Vertex<V> w;
		Stack<Vertex<V>> result;
		Iterator<Edge<V>> iter = v.incidentEdges();

		// Iterate through edges in adjacency list of current vertex
		while(iter.hasNext()) {

			edge = iter.next();
			w = g.giveOpposite(v, edge);	// Get opposite endpoint

			// Build path via opposite endpoint if this vertex is unvisited (and potentially, if connecting edge is frozen)
			if (w.getMarker() == false && (edge.getMarker() || !condition)) {

				result = pathDFS(g, w, u, stack, condition);	// Recursively build path
				if (result != null)								// until base case (detination vertex u) is found
					return result;
			}
		}

		stack.pop();		// If destination vertex u could not be found by following this path, move to previous vertex
		return null;		// and pursue a new path via another adjacent vertex
	}

} // End of class