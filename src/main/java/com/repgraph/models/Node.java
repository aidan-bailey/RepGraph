package com.repgraph.models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node>{
    private int id;
    private String label;
    // potentially just change into start and end token values
    private ArrayList<Token> tokens;
    // store adjacent nodes for graph property calculations, treating as undirected
    private ArrayList<Node> adjacent;
    // store outgoing edges for nodes
    private ArrayList<Edge> edges;
    private boolean abstractNode;

    //boolean values for comparison
    private boolean labelMatch;
    private boolean spanMatch;

    /**
     * Constructor which takes in a Json object representation of a node and a list
     * of tokens. It then extracts the id, label and whether the node is abstract
     * from the object. It also extracts and stores the tokens associated with the
     * node
     * 
     * @param n the Json object repsentation of the node
     * @param t the list of tokens containing the tokens associated with the node
     */
    public Node(JSONObject n, List<Token> t) {
        tokens = new ArrayList<>();
        adjacent = new ArrayList<>();
        edges = new ArrayList<>();

        id = (int) (long) n.get("id");
        label = (String) n.get("label");
        // abstract nodes start with underscore
        if (label.charAt(0) != '_') {
            abstractNode = true;
        } else {
            abstractNode = false;
        }
        JSONArray tokenArr = (JSONArray) n.get("anchors");
        // if there are tokens associated with the node. tokenArr might not exist of
        // might be empty
        if (tokenArr != null && !tokenArr.isEmpty()) {
            JSONObject jAnchor = (JSONObject) tokenArr.get(0);
            // add tokens in the anchor range
            for (int j = (int) (long) jAnchor.get("from"); j <= (int) (long) jAnchor.get("end"); j++) {
                tokens.add(t.get(j));
            }
        }
    }

    /**
     * Constructor for testing which takes in the id of the node and assign it to
     * the node id
     * 
     * @param id the id for the node
     */
    public Node(int id) {
        this.id = id;
        this.label = "";
        edges = new ArrayList<>();
        adjacent = new ArrayList<>();
        tokens = new ArrayList<>();
    }

    /**
     * Constructor for testing pattern matching which takes in a label and assigns
     * it to the node label
     * 
     * @param label the label for the node
     */
    public Node(String label){
        this.label = label;
        edges = new ArrayList<>();
        adjacent = new ArrayList<>();
    }

    /**
     * Add a token to the token list for the node
     * 
     * @param t the token to be added to the token list
     */
    public void addToken(Token t) {
        tokens.add(t);
    }

    /**
     * Add a node to the adjacency list for the node. This means the the node is
     * either the source or destination of an edge associated with the given node
     * 
     * @param node the node which is adjacent
     */
    public void addAdjacent(Node node) {
        adjacent.add(node);
    }

    /**
     * Add a directed edge to the node, for which the node is the source node. This
     * takes in a Json representation of the edge and a list of nodes and creates a
     * new edge and adds the destination node to the adjacency list
     * 
     * @param j the Json representation of the edge
     * @param n the list of nodes containing the destination node for the edge
     */
    public void addEdge(JSONObject j, List<Node> n) {
        edges.add(new Edge(j, n));
        addAdjacent(n.get((int) (long) j.get("target")));
    }

    /**
     * Add and edge object to the list of edges for the node, for which the node is
     * the source. The edge is added to the edge list and the destination node for
     * the edge is added to the adjacency list. This is mainly for testing.
     * 
     * @param e the edge to be added to the list, for which the node is a source
     */
    // For testing
    public void addEdge(Edge e) {
        edges.add(e);
        adjacent.add(e.getDest());
    }

    /**
     * Expected that either the label, span or both of the other node matches the current node. This methods checks whether the node labels
     * or spans match and then assign labelMatch and spanMatch for this node and the other node accordingly. It then checks the edges of 
     * both the nodes and records which are the same and which are different
     * @param other the other node with either the same label, token span or both
     */
    public void compareNode(Node other){
        //stores whether the span of the two current nodes match as spanMatch might be true from a different node
        boolean matchSpan = false;
        //only need change variables to true. Already set to false in Graph class
        //if labels don't match, then the span must match. Avoids redoing span check which would have been done in equals
        if (!(label.equals(other.getLabel()))){
            matchSpan = true;
            spanMatch = true;
            //assign variables for node being compared to
            other.setSpanMatch(true);
        } else{
            //must compute whether spans match
            labelMatch = true;
            other.setLableMatch(true);
            matchSpan = compareTokenSpan(other);
            if (matchSpan){
                spanMatch = true;
                other.setSpanMatch(true);
            }
        }
        //check edges of the two nodes only if their token spans match. For edges to match both the source and destination node spans must match and therefor
        //if matchSpan is not true there is not point in doing further comparison
        if (matchSpan){
            compareEdges(other);
        }
    }
        
    /**
     * Compare the edge lists of two nodes and record the edges that match and do not match. Assumes the two nodes with edges being compared have the same token
     * spans
     * @param other the other node with the same token span
     */    
    public void compareEdges(Node other){
        //If the other node has any edges i.e. if edges array is not null or empty, must check whether any match
        //only need change variables to true, as default to false
        if (other.getNumberEdges() > 0){
            ArrayList<Edge> otherEdges = other.getEdges();
            for (Edge e: edges){
                //if the index is greater than or equal to 0, the edge is in the other nodes edge list and thus there is a match
                int otherIndex = otherEdges.indexOf(e);
                if ( otherIndex >= 0){
                    e.setEdgeMatch(true);
                    otherEdges.get(otherIndex).setEdgeMatch(true);
                }
            }
        }
    }   

    /**
     * Check whether two nodes span the same range of tokens. This check whether they both contain equal tokens in their list
     * @param other the other node
     * @return whether the two nodes span the same range of tokens
     */
    public boolean compareTokenSpan(Node other){
        ArrayList<Token> otherTokens = other.getTokens();
        //if there aren't the same amount of tokens they cannot have the same span
        if (otherTokens.size() != tokens.size()){
            return false;
        }
        for (int i =0; i<tokens.size(); i++){
            //return false if tokens do not match for each token in the two arrays
            if (!(tokens.get(i).equals(otherTokens.get(i)))){
                return false;
            }
        }
        return true;
    }

    /**
     * Set the labelMatch variable
     * @param match variable for lableMatch to be set to
     */
    public void setLableMatch(boolean match){
        labelMatch = match;
    }

    /**
     * Set the spanMatch variable
     * @param match variable for spanMatch to be set to
     */
    public void setSpanMatch(boolean match){
        spanMatch = match;
    }

    /**
     * Return the value of the labelMatch variable
     * @return labelMatch variable
     */
    public boolean getLableMatch(){
        return labelMatch;
    }

    /**
     * Return the value of the spanMatch variable
     * @return spanMatch variable
     */
    public boolean getSpanMatch(){
        return spanMatch;
    }

    /**
     * Return the adjacency list for the node
     * 
     * @return the list containing all the node adjacent to the node, independent of
     *         the direction of the edges
     */
    public List<Node> getAdjacent() {
        return adjacent;
    }

    /**
     * Return the id for the node
     * 
     * @return the id for the node
     */
    public int getID() {
        return id;
    }

     /**
     * Return the label for the node
     * 
     * @return the label for the node
     */
    public String getLabel() {
        return label;
    }

    /**
     * Return the list of edges for which the node is the source
     * 
     * @return the list of edges for which the node is the source
     */
    public ArrayList<Edge> getEdges() {
        return edges;
    }

    /**
     * Return the span (list of tokens)
     * 
     * @return the span (list of tokens)
     */
    public ArrayList<Token> getTokens(){
        return tokens;
    }

    /**
     * Return the number of edges in the graph, or -1 if the edge list is null
     * 
     * @return the number of edges or -1 if the number of edges is null
     */
    public int getNumberEdges(){
        if (edges == null){
            return -1;
        }
        return edges.size();
    }

    /**
     * Return the number of tokens that relate to the node, i.e. the size of the tokens list
     * 
     * @return the number of edges for which the node is the source
     */
    public int getNumTokens() {
        return tokens.size();
    }

    /**
     * Return the index of the first token associated with the node. If no such
     * index exists, -1 is returned
     * 
     * @return the index of the first token associated with the node, -1 if the
     *         token list is empty
     */
    public long getStartTokenIndex() {
        if (!tokens.isEmpty()) {
            return tokens.get(0).getIndex();
        }
        return -1;
    }

    /**
     * Return the index of the last token associated with the node. If no such
     * index exists, -1 is returned
     *
     * @return the index of the last token associated with the node, -1 if the
     * token list is empty
     */
    public long getLastTokenIndex(){
        if (!tokens.isEmpty()) {
            return tokens.get(tokens.size()-1).getIndex();
        }
        return -1;
    }

    /**
     * Override compareTo method for ordering nodes for visualization. A node 
     * comes before another node if its start token is less than the other
     * nodes start token or their start tokens are equal but the nodes last
     * token comes before the other nodes last token
     * 
     * @param other the other node the node is being compared to
     * @return whether the current node is less, greater or equal to the other node
     */
    public int compareTo(Node other) {
        if (other.getStartTokenIndex() < getStartTokenIndex()) return 1;
        if (other.getStartTokenIndex() > getStartTokenIndex()) return -1;
        //if start spans are equal, compare end spans
        if (other.getLastTokenIndex() < getLastTokenIndex()) return 1;
        if (other.getLastTokenIndex() > getLastTokenIndex()) return -1;
        return 0;
    }

    /**
     * Overriding equals method for node class. Two nodes are equal if either their labels match or their node spans match. This is used
     * for finding the indexes of nodes in the array for comparison
     * 
     * @return whether the two objects are equal or not
     */
    public boolean equals(Object o){
        // If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
        } 
        // Check if o is an instance of Node or not
        if (!(o instanceof Node)) { 
            return false; 
        } 
        // typecast o to Node and compare labels and spans
        Node other = (Node) o;  
        return label.equals(other.getLabel()) || compareTokenSpan(other);
    }

    /**
     * Return a boolean indicating if the current node is an abstract or surface node.
     * 
     * @return true if abstract node, false if surface node.
     */
    public boolean isAbstractNode(){
        return abstractNode;
    }

    /**
     * Method to override toString() for the Node class
     * 
     * @return a string representation of node comprised of the id, tokenspan, label
     *         and whether the node is abstract
     */
    public String toString() {
        String tokenSpan = !tokens.isEmpty()
                ? tokens.get(0).getIndex() + " - " + tokens.get(tokens.size() - 1).getIndex()
                : "No associated token span";
        return "ID: " + id + " Token span:  " + tokenSpan + " label: " + label + " abstract: " + abstractNode;
    }
}
