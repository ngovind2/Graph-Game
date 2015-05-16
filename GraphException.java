// Implementation of exceptions thrown by Graph class.
// package GraphGame;

public class GraphException extends Exception {

	public GraphException() { super(); } 

	public GraphException(String errMsg) { super(errMsg); } 

	public GraphException(Throwable cause) { super(cause); }

	public GraphException(String errMsg, Throwable cause) { super(errMsg, cause); }

} // End of class