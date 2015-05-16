// Implementation of Edge objects for Graph class
// package GraphGame;

public class Edge<V> {
	
	// Instance variables
	private Vertex<V> first; 
	private Vertex<V> second;
	private boolean marker;


	/*     CONSTRUCTOR     */     

	// Sets end points of edge, which is unfrozen by default
	public Edge(Vertex<V> u, Vertex<V> v) {

		first = u;
		second = v; 
		marker = false;
	}


	/*     METHODS     */

	// Returns first endpoint
	public Vertex<V> getEndPoint1() { return first; }

	// Returns second endpoint
	public Vertex<V> getEndPoint2() { return second; }

	// Sets the marker for this edge to either true or false
	public void setMarker(boolean mark) { marker = mark; }

	// Returns marker of edge (ie. true = frozen or false = unfrozen)
	public boolean getMarker() { return marker; }

} // End of class