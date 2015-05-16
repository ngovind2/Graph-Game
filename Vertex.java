// Implementation of Vertex objects for Graph class
// package GraphGame;

import java.util.Iterator;
import java.util.LinkedList;

public class Vertex<V> {
	
	// Instance variables
	private V object;
	private boolean marker;
	private LinkedList<Edge<V>> adjList;


	/*     CONSTRUCTOR     */

	// Stores an object of type V at the vertex, which has an empty adjacency list & is unvisited by default
	public Vertex(V objectIn) { 

		object = objectIn;
		marker = false;
		adjList = new LinkedList<Edge<V>>(); 
	}


	/*     METHODS     */

	// Returns the object stored at the vertex
	public V getObject() { return object; }

	// Adds specified edge to the vertex's adjacency list
	// Throws an exception if this edge already exists in the adjacency list
	public void addAdjacent(Edge<V> e) throws GraphException {

		if ( adjList.contains(e) || adjList.contains(new Edge<V>(e.getEndPoint2(), e.getEndPoint1())) )
			throw new GraphException("Edge already exists in adjacency list");

		adjList.add(e);
	}

	// Returns an iterator over the edge objects incident on the vertex
	public Iterator<Edge<V>> incidentEdges() { return adjList.listIterator(); }

	// Returns true if the vertex is adjacent to vertex v, false otherwise
	public boolean isAdjacent(Vertex<V> v) { 

		Edge<V> edge;
		Iterator<Edge<V>> edgeIter = incidentEdges();

		// Iterate through edges in current vertex's adjacency list
		while (edgeIter.hasNext()){
			edge = edgeIter.next();

			// Return true if any edges incident on current vertex have vertex v as opposite endpoint
			if (edge.getEndPoint1() == v || edge.getEndPoint2() == v)
				return true;
		}

		return false; 
	}
	
	// Removes specified edge from adjacency list
	// Throws exception if edge e is not actually incident on the vertex
	public void removeAdjacent(Edge<V> e) throws GraphException {

		// In undirected graph, edge e can also be represented by alternate edge with flipped endpoints
		Edge<V> alternate = new Edge<V>(e.getEndPoint2(), e.getEndPoint1());

		// Check if either edge e or its alternate is present in current vertex's adjacency list
		boolean eFound = adjList.contains(e); 
		boolean altFound = adjList.contains(alternate); 

		// Throw exception if neither were found
		if (!eFound && !altFound)
			throw new GraphException("Edge not adjacent to vertex");

		// Otherwise, remove edge that was found from list
		if (eFound)
			adjList.remove(e);
 		else
			adjList.remove(alternate);
	} 

	// Sets the marker for this vertex (ie. true = visited or false = unvisited)
	public void setMarker(boolean mark) { marker = mark; }

	// Returns the marker for this vertex
	public boolean getMarker() { return marker; }

} // End of class