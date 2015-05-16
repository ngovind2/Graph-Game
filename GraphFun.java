// Implements game & interface using Graph-related classes
// package GraphGame;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Iterator;
import java.util.Vector;

public class GraphFun implements ActionListener {

   // Specifies 'size' of game
   private static final int NUM_HORIZONTAL = 3;
   private static final int NUM_VERTICAL  = 3;
   private static final int NUM_VERTICES = NUM_HORIZONTAL * NUM_VERTICAL;
   private static final int NUM_EDGES = NUM_VERTICES * 5;
    
   // Specifies GUI parameters
   private static final int WINDOW_WIDTH = 600;   // Width of widow game
   private static final int WINDOW_HEIGHT = 600;  // Height of window game
   private final static int CANVAS_WIDTH = 500;   // Width of graph picture in game
   private final static int CANVAS_HEIGHT = 500;  // Height of graph picture in game
   private static final int TEXT_WIDTH = 5;       // Width of text window
   private static final int OFFSET = 6;           // For vertex display

   private static final FlowLayout LAYOUT_STYLE = new FlowLayout();
   
   private final static Color SKY_BLUE = new Color(217,217,255);
   private final static Color EDGE_COLOR = new Color(117,247,55);
   
   // Commonly used strings
   private final static String Enter = new String("Enter");
   private final static String New = new String("New");
   private static final String EMPTY_STRING = "";   
    
   // Constants to denote outcome of the next move
   private final static int HUMAN_WON    = 1;
   private final static int COMPUTER_WON = 2;
   private final static int NOONE_WON    = 3;
   
   // Instance variables for GUI
   private JFrame window             = new JFrame("GraphFun");
   private JLabel nextEdgeTag        = new JLabel("Next Edge");
   private JTextField firstText      = new JTextField(TEXT_WIDTH);
   private JTextField secondText     = new JTextField(TEXT_WIDTH);
   private JLabel warning            = new JLabel(EMPTY_STRING);

   private JButton enterButton = new JButton(Enter);
   private JButton startButton = new JButton(New);
   
   private Canvas graphPad = new Canvas();


   // Instance variables for graph
   private Graph<Coordinate> graph;                    // Graph representation of the game
   private Vector<Vertex<Coordinate>>  vertexLookup;   // Lookup from integer vertex names to Vertex<Coordinate> objects
   private int startVertex;                            // Special start vertex
   private int endVertex;                              // Special end vertex
   
   
   // Constructure configures GUI
   public GraphFun() {
      
      // Configure GUI
       window.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
       window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       window.setBackground(SKY_BLUE);
       
       graphPad.setSize(CANVAS_WIDTH,CANVAS_HEIGHT);
       graphPad.setBackground(Color.WHITE);
       enterButton.addActionListener(this);
       startButton.addActionListener(this);
       
       firstText.setEditable(false);    // Fields can't be edited until user presses "new" button
       secondText.setEditable(false);
       
       // Arranges components in GUI
       window.setLayout(LAYOUT_STYLE);
       window.add(startButton);
       window.add(nextEdgeTag);
       window.add(firstText);
       window.add(secondText);
       window.add(enterButton);
       window.add(warning);
       window.add(graphPad);      // Graph's vertices and edges displayed on this canvas
              
       window.setVisible(true);   // Display GUI
   }
   
   // Handles the event when "new" or "enter" buttons are pressed
   public void actionPerformed(ActionEvent e) {
       
       // Get user's response
       if (Enter.compareTo(e.getActionCommand()) == 0 ) {   // "enter" button pressed

           try{
                Integer vertex1 = new Integer(firstText.getText());
                Integer vertex2 = new Integer(secondText.getText());
                if (vertex1 < 0 || vertex1 >= NUM_VERTICES || vertex2 < 0 || vertex2 >= NUM_VERTICES )
                    warning.setText("INVALID RANGE");
                else {
                    warning.setText(EMPTY_STRING);
                   
                    // Process user move and generate next computer move
                    nextMove(vertex1.intValue(),vertex2.intValue()); 
                }
           }
           catch(NumberFormatException exept){warning.setText("INVALID INPUT");}
	   
       }
       if (New.compareTo(e.getActionCommand()) == 0 ){  // "new" button pressed
            firstText.setEditable(true);
            secondText.setEditable(true);   
            clearBoard();
            startNewGame();
       }       
   }
   
   // Clears graph canvas
   private void  clearBoard() {
        int w = graphPad.getWidth();
        int h = graphPad.getHeight();
        Graphics g = graphPad.getGraphics();
        
        g.setColor(Color.WHITE);
        g.fillRect(0,0,w,h);
        
   }
 
   // Selects 2 new endpoints for the new game
     private void setEndVertices() {
        java.util.Random rand = new java.util.Random();
  
        startVertex = rand.nextInt(NUM_VERTICES);     
        endVertex = rand.nextInt(NUM_VERTICES);     

        int iy = endVertex/NUM_HORIZONTAL;
        int ix = endVertex-iy*NUM_HORIZONTAL;

        int iy1 = startVertex/NUM_HORIZONTAL;
        int ix1 = startVertex-iy1*NUM_HORIZONTAL;
              
        // Checks that 2 endpoints are not too close to each other
       while (Math.abs(iy-iy1) + Math.abs(ix-ix1) < NUM_HORIZONTAL )
       {
	   startVertex = rand.nextInt(NUM_VERTICES);     
	   endVertex = rand.nextInt(NUM_VERTICES);

	   iy = endVertex/NUM_HORIZONTAL;
	   ix = endVertex-iy*NUM_HORIZONTAL;

	   iy1 = startVertex/NUM_HORIZONTAL;
	   ix1 = startVertex-iy1*NUM_HORIZONTAL;
       }
     }
    
    // Generates new vertex for the game
    private Coordinate newVertex(int i) {   
       int w = graphPad.getWidth();
       int h = graphPad.getHeight();
       
       int iy = i/NUM_HORIZONTAL;
       int ix = i-iy*NUM_HORIZONTAL;
       int intervalx = CANVAS_WIDTH/(NUM_HORIZONTAL+1);
       int intervaly = CANVAS_HEIGHT/(NUM_VERTICAL+1);
       
       java.util.Random rand = new java.util.Random();
       int deltaX = rand.nextInt((intervalx*2)/3)-intervalx/3;
       int deltaY = rand.nextInt((intervaly*2)/3)-intervaly/3;
        
       int x = intervalx+ix*intervalx+deltaX;
       int y = intervaly+iy*intervaly+deltaY;
               
       return (new Coordinate(x,y,i));
   }
   
    // Prints vertex with name i at Coordinate coord
    private void paintVertex( Coordinate coord ) {
       
       int w = graphPad.getWidth();
       int h = graphPad.getHeight();
       
       int x = coord.getX();
       int y = coord.getY();
       int i = coord.getIndex();
        
        Graphics g = graphPad.getGraphics();
        
        // Ensures enpoints are in a different color from the other vertices
	if ( i == startVertex || i == endVertex )
	    g.setColor(Color.RED);
        else g.setColor(Color.BLACK);
        
	g.fillOval(x-OFFSET/2,y-OFFSET/2,OFFSET,OFFSET);
	Integer name = new Integer(i);
        g.drawString(new String(name.toString()),x-OFFSET/2,y-OFFSET/2);
   }
 
   // Chooses new vertices for the game 
   private Coordinate [] getVertices( ) {
        
        Coordinate [] toReturn = new Coordinate[NUM_VERTICES];
        for ( int i = 0; i < NUM_VERTICES; i++ )
            toReturn[i] = newVertex(i);
        return toReturn;
   }   

   // Chooses a possible set of edges for the game (not all edges are necessarily unique)
   private Pair [] getEdges(Coordinate [] vertices) {
       Pair [] toReturn = new Pair[NUM_EDGES];
       java.util.Random rand = new java.util.Random();
       
       int w = graphPad.getWidth();
       int h = graphPad.getHeight();
               
       for (int i = 0; i < NUM_EDGES; i++){
            int n1 = rand.nextInt(NUM_VERTICES);
            int n2 = rand.nextInt(NUM_VERTICES);           
                        
            int iy = n1/NUM_HORIZONTAL;
            int ix = n1-iy*NUM_HORIZONTAL;
            boolean done = false; 
            int temp = 0;

            int intervalx = CANVAS_WIDTH/(NUM_HORIZONTAL+1);
            int intervaly = CANVAS_HEIGHT/(NUM_VERTICAL+1);

            // Try for edges that are not too long, so that graph displays well
            while( ! done ){
                n2 = rand.nextInt(NUM_VERTICES);
                
                int iy2 = n2/NUM_HORIZONTAL;
                int ix2 = n2-iy2*NUM_HORIZONTAL;
                    
              // if ( Math.abs(ix-ix2) <= 1 &&Math.abs(iy-iy2) <= 2 && n1 != n2)
                if ( Math.abs(vertices[n1].getX()-vertices[n2].getX()) <= 2*intervalx &&
                     Math.abs(vertices[n1].getY()-vertices[n2].getY()) <= 2*intervaly && n1 != n2 )
                     done = true;
                temp++;
                if (temp > 20) done = true;
            }
            
            toReturn[i] = new Pair(n1,n2);
        }

        return(toReturn);
   }

   // Draws current board in the game window
   // Takes an iterator over vertices and an iterator over edges
   private void drawBoard(Iterator<Vertex<Coordinate>> vertexIt, Iterator<Edge<Coordinate>> edgeIt) {
       clearBoard();
       int w = graphPad.getWidth();
       int h = graphPad.getHeight();
       
       Graphics g = graphPad.getGraphics();
       
       // first draw edges
       while (edgeIt.hasNext() ){
            Edge<Coordinate> e = edgeIt.next();
            if (e.getMarker()) g.setColor(Color.RED);
            else g.setColor(EDGE_COLOR);
            Vertex<Coordinate> u = e.getEndPoint1();
            Vertex<Coordinate> v = e.getEndPoint2();
            g.drawLine(u.getObject().getX(),u.getObject().getY(),v.getObject().getX(),v.getObject().getY());
       }
       
       // Draws vertices
       while (vertexIt.hasNext() ){
            Vertex<Coordinate> u =  vertexIt.next();
            paintVertex(u.getObject());
       }
   }
   
   // Stops the game by setting input fields to be nonEditable
   private void stopGame() {
        firstText.setEditable(false);
        secondText.setEditable(false);
   }   

   // Displays a message on the screen
   private void displayMessage(String message) {
        warning.setText(message);
   }


  // Chooses an edge within given path to be frozen by computer
  private void freezeEdge(Iterator<Vertex<Coordinate>> pathIter) { 
    
    Edge<Coordinate> edge = null;
    Vertex<Coordinate> current, opposite;

    // Get beginning vertex of path
    current = pathIter.next();

    // Iterate through all following vertices
    while (pathIter.hasNext()) {

      // Create edge between 'current' & 'opposite' (a vertex adjacent to current)
      opposite = pathIter.next();

      // Get the connecting edge between these vertices
      try { edge = graph.findEdge(current, opposite); } 
      catch (GraphException e) { 
        displayMessage("Could not freeze edge."); 
        return; 
      }
      
      // If this edge is unfrozen, freeze it  
      if (!edge.getMarker()) {
        edge.setMarker(true);
        return;
      }

      // Otherwise, move to next vertex/edge in path
      current = opposite;       
    }
  }


	// Called automatically when the user presses the "enter" button
  // Processes the user's move (delete the edge they specify) & generates computer's next move
	private void nextMove(int i, int j) { 
      
      displayMessage("");   // Resets display message on screen

      // Gets vertex objects associated with specified numbers
      Vertex<Coordinate> vertex1 = vertexLookup.elementAt(i);   
      Vertex<Coordinate> vertex2 = vertexLookup.elementAt(j);

      // Gets edge between the chosen vertices
      Edge<Coordinate> edge = null; 

      try { edge = graph.findEdge(vertex1, vertex2); }

      // Displays error messages if edge does not exist or is frozen, or an endpoint does not exist
      catch (GraphException e) { displayMessage("This edge does not exist."); }
      
      if (edge == null) 
        displayMessage("This edge does not exist.");

      else if (edge.getMarker())
        displayMessage("This edge is frozen.");

      // Otherwise, deletes user-specified edge & generate computer's next move
      else {
        
        // Deletes edge
        try { graph.deleteEdge(edge); }
        catch (GraphException e) { displayMessage("Could not delete edge."); return; }
  
        // After deletion, checks for a path between the start & end vertices          
        FindPath<Coordinate> path = new FindPath<Coordinate>();
        Iterator<Vertex<Coordinate>> pathIter = path.givePath(graph, vertexLookup.elementAt(startVertex), vertexLookup.elementAt(endVertex));

        // If no path was found, then the user has won
        // Stop the game, redraw the display and display winning message
        if (pathIter == null) {
           stopGame();
           drawBoard(graph.vertices(), graph.edges());
           displayMessage("Congratulations, you won!");
         }

        // Otherwise, continues game & generates computer's next move using this path
        else {

          freezeEdge(pathIter);                         // Freeze an edge on the path found above 
          drawBoard(graph.vertices(), graph.edges());   // Redraw display to reflect changes

          // Checks if a complete path between the start & end vertices is frozen
          boolean win = path.markedPathExists(graph, vertexLookup.elementAt(startVertex), vertexLookup.elementAt(endVertex));
         
          // If so, the computer has won
          // Stop the game, redraw the display and display winning message
          if (win) {
            stopGame();
            displayMessage("Game over.");
          }

        }
      }       
	}

  // Creates a new game; called automatically when the user presses the "new" button
  private void startNewGame() {

       Coordinate [] vertices = getVertices();       // Gets the new vertices for the game
       Pair[]           edges = getEdges(vertices);  // Gets the new edges for the game
       setEndVertices();                             // Chooses the start and the end vertices of the game
       displayMessage("");                           // Resets the display message on screen
       graph = new Graph<Coordinate>();              // Creates new graph representation

       // Inserts vertex named "i" into graph, and stores vertex at the ith location of vertexLookup
       vertexLookup = new Vector<Vertex<Coordinate>>(NUM_VERTICES);
       for ( int i = 0; i < NUM_VERTICES; i++ )
	       vertexLookup.add(i,graph.insertVertex(vertices[i]));

       // Sets markers as false ('unvisited') for all vertices
       Iterator<Vertex<Coordinate>> iter1 = graph.vertices();
       while(iter1.hasNext()) { iter1.next().setMarker(false); }

       // Inserts all unique edges into graph
       Vertex<Coordinate> first, second;
       Edge<Coordinate> test;

       // Iterates through array containing edge specifications
       for (int i = 0; i < NUM_EDGES; i++) {

          // Get endpoints of each edge
          first = vertexLookup.elementAt(edges[i].getFirst());
          second = vertexLookup.elementAt(edges[i].getSecond());

          try { 
            
            // Test for duplicate entries: check if edge with these endpoints already exists
            test = graph.findEdge(first, second); 
            
            // If not, inserts new edge object with these endpoints into graph
            if (test == null) 
              graph.insertEdge(first, second);
          }
          catch(GraphException e) { displayMessage("Error occurred"); return; }
        
      }

       // Sets markers as false ('unfrozen') for all edges
       Iterator<Edge<Coordinate>> iter2 = graph.edges();
       while(iter2.hasNext()) { iter2.next().setMarker(false); }

      // Redraw display to reflect changes
       drawBoard(graph.vertices(), graph.edges());
   }
   
   public static void main(String[] args) throws GraphException {
      GraphFun gui = new GraphFun();
      gui.displayMessage("Click \'New\' to begin");
    }
}
