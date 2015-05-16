// Implementation of Graph object
// package GraphGame;

import java.util.Iterator;
import java.util.LinkedList;

public class Graph<V> implements GraphInterface<V> {

	// Instance variables
	private LinkedList<Vertex<V>> vertices;
	private int numVertices;
	private int numEdges;


	/*     CONSTRUCTOR     */

	// Initializes graph object with empty vertex list & resets vertex/edge counts 
	public Graph() {

		vertices = new LinkedList<Vertex<V>>();
		numVertices = 0; 
		numEdges = 0;
	}


	/*     METHODS     */		

	// Adds a new vertex storing object o, increments vertex count, then returns the newly created vertex
	public Vertex<V> insertVertex(V o) {

		Vertex<V> vertex = new Vertex<V>(o);
		vertices.add(vertex);
		numVertices++;
		return vertex;
	}

	// Returns number of vertices in graph
	public int getNumVertices() { return numVertices; }

	// Returns number of edges in graph
	public int getNumEdges() { return numEdges; }

	// Returns the edge between vertices u and v, or null if the edge between u and v does not exist
	// Throws exception if either u or v are null pointers
	public Edge<V> findEdge(Vertex<V> u, Vertex<V> v) throws GraphException {

		if (v == null || u == null)
			throw new GraphException("Vertex does not exist");

		Edge<V> edge;
		Vertex<V> first;
		Vertex<V> second;
		Iterator<Edge<V>> iter = v.incidentEdges(); 
		
		// Iterate through adjacency list of one of the vertices (ie. vertex v) & check if edge exists between vertices u and v
		while (iter.hasNext()) {
			edge = iter.next();
			first = edge.getEndPoint1();		// Get endpoints of each edge in list
			second = edge.getEndPoint2();

			if (first == v && second == u || first == u && second == v) 	// Check if endpoints match vertices u and v
				return edge;
		}

		return null;
	}

	// Returns true if u and v are adjacent, false otherwise
	// Throws exception if either u or v are null pointers
	public boolean areAdjacent(Vertex<V> v, Vertex<V> u) throws GraphException {

		if (v == null || u == null)
			throw new GraphException("Vertex does not exist");

		if (v.isAdjacent(u))
			return true;

		return false;
	}

	// Inserts an edge between u and v in the graph and returns the newly inserted edge
	// Throws exception if this edge already exists in the graph, or if u and v are the same vertex (no self-loops are allowed)
	public Edge<V> insertEdge(Vertex<V> v, Vertex<V> u) throws GraphException {

		if (v == u)
			throw new GraphException("Vertices are the same");

		if (v == null || u == null)
			throw new GraphException("Vertex does not exist");

		if (findEdge(v, u) != null)
			throw new GraphException("Edge already exists"); 

		Edge<V> edge = new Edge<V>(v, u);
		v.addAdjacent(edge);				// Add edge to adjacency lists of both endpoints
		u.addAdjacent(edge);

		numEdges++;							// Increment edge count
		return edge;

	}

	// Deletes edge e from the graph
	// Throws exception if edge e is null
	public void deleteEdge(Edge<V> e) throws GraphException {

		if (e == null)
			throw new GraphException("Edge does not exist");

		Vertex<V> first = e.getEndPoint1();		// Get both endpoints
		Vertex<V> second = e.getEndPoint2();

		first.removeAdjacent(e);				// Remove edge from adjacency list of each endpoint
		second.removeAdjacent(e);

		numEdges--;								// Decrement edge count
	}

	// Returns the endpoint opposite of v in the edge e
	public Vertex<V> giveOpposite(Vertex<V> v, Edge<V> e) {

		Vertex<V> first = e.getEndPoint1(); 	// Get both endpoints
		Vertex<V> second = e.getEndPoint2(); 
		
		if (v == first)							// Return endpoint opposite of v
			return second; 
		else if (v == second)
			return first;
		else 
			return null;
	}

	// Returns iterator over the vertex objects
	public Iterator<Vertex<V>> vertices() { return vertices.listIterator(); }

	// Returns iterator over the edge objects
	public Iterator<Edge<V>> edges() {

		// Mark all vertices as unvisited
		Iterator<Vertex<V>> iter = vertices();
		while(iter.hasNext()) { iter.next().setMarker(false); }

		Vertex<V> vertex, first, second;
		Edge<V> edge;
		LinkedList<Edge<V>> edges = new LinkedList<Edge<V>>();		// List will hold all unique edges

		Iterator<Edge<V>> edgeIter;
		Iterator<Vertex<V>> vertexIter = vertices();

		// Iterate through all vertices
		while(vertexIter.hasNext()) {

			vertex = vertexIter.next();
			edgeIter = vertex.incidentEdges();
			
			// Iterate through all edges in each vertex's adjacency list
			while(edgeIter.hasNext()) {
			
				edge = edgeIter.next();
				first = edge.getEndPoint1();		// Get endpoints of each edge
				second = edge.getEndPoint2();

				if (!first.getMarker() && !second.getMarker())		// If both endpoints are unvisited, 
					edges.add(edge);								// then edge has not been accounted for yet, so add to edge list
			}

			vertex.setMarker(true);		// Mark vertex as being visited
		}

		return edges.listIterator();	// Return iterator over edge list
	}

} // End of class

