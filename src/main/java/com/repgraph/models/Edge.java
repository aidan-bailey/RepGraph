package com.repgraph.models;

import java.util.List;

import org.json.simple.JSONObject;

public class Edge {
    private String postLabel;
    private String label;
    private Node dest;

    //booleans for node comparisons. Also used for marking edges in pattern matching
    private boolean edgeMatch;

    /**
     * Constructor which takes in a JSON object containing a Json representation of
     * an edge and a list of nodes. The postlabel and label are taken from the Json
     * object. The index of the dest node is taken from the Json object and used to
     * find and assign the destination node for the edge from the node list
     * 
     * @param e the Json object representation of an edge
     * @param n the list of nodes containing the destination node for the edge
     */
    public Edge(JSONObject e, List<Node> n) {
        postLabel = (String) e.get("post-label");
        label = (String) e.get("label");
        dest = n.get((int) (long) e.get("target"));
    }

    /**
     * Constructor provided for testing. Takes in a destination node and assigns it
     * to the dest node
     * 
     * @param dest the destination node for the edge
     */
    public Edge(Node dest) {
        this.dest = dest;
        this.label = "";
    }

    /**
     * Constructor for testing which takes in a label and destination node and assigns
     * it to the edge's label and destination node
     * 
     * @param label the label for the edge
     * @param dest the destination node for the edge
     */
    public Edge(String label, Node dest){
        this.label = label;
        this.dest = dest;
    }

    /**
     * Sets the edge match variable
     * @param match the value the edge match variable is to be set to
     */
    public void setEdgeMatch(boolean match){
        edgeMatch = match;
    }

    /**
     * Returns the dest node for the edge
     * @return the destination node for the edge
     */
    public Node getDest() {
        return dest;
    }

    /**
     * Returns the value of the edge match variable
     * @return the value of the edge match variable
     */
    public boolean getEdgeMatch(){
        return edgeMatch;
    }

    /**
     * Returns the label for the edge
     * @return the edge label
     */
    public String getLabel(){
        return label;
    }

    public String getFullLabel(){
        return label + "/" + postLabel;
    }

    /**
     * Overriding equals method for edge class. Two edges are equal if the label and destination nodes' spans are equal
     * Assumed to only be called when source nodes are equal (assuming source equal)
     * Edges are equal if their source and destination nodes span the same tokens and the edge label matches
     * @return whether the two objects are equal or not
     */
    public boolean equals(Object o){
        // If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
        } 
        // Check if o is an instance of Edge or not
        if (!(o instanceof Edge)) { 
            return false; 
        } 
        // typecast o to Edge and compare label and token span of the two destination nodes 
        Edge other = (Edge) o;  
        return label.equals(other.getLabel()) && dest.compareTokenSpan(other.getDest());
    }

    /**
     * Method to override toString() for the Edge class
     * @return a string representation of edge comprised of its postlabel, label and destination node
     */
    public String toString() {
        return "Post: " + postLabel + " label: " + label + "dest: " + dest;
    }

}
