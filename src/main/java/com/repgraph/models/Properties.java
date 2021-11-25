package com.repgraph.models;
import java.util.ArrayList;
import java.util.Stack;

import org.springframework.stereotype.Component;

/**
 * DMRS graphs is acyclic
 * https://books.google.co.za/books?id=d-JVDwAAQBAJ&pg=PT129&lpg=PT129&dq=are+dmrs+graphs+acyclic&source=bl&ots=dJBmvzPnmr&sig=ACfU3U2D2N4fOPhzWYijQxlQAJIxx7vsuA&hl=en&sa=X&ved=2ahUKEwjthaPi2NnrAhWoUBUIHTWkDE4Q6AEwEXoECAEQAQ#v=onepage&q=are%20dmrs%20graphs%20acyclic&f=false
 * Application of Graph Rewriting to Natural Language Processing By Guillaume
 * Bonfante, Bruno Guillaume, Guy Perrier
 * https://www.aclweb.org/anthology/N19-1235.pdf Neural Text Generation from
 * Rich Semantic Representations
 */

@Component
public class Properties {

    private boolean connected;
    private int longPath;
    private boolean planar;

    private Graph _graph;

    /**
     * Default constructor which sets longPath to -1
     */
    public Properties() {
        longPath = -1;
    }

    /**
     * Constructor for which calls the methods checkConnected, longestPath,
     * checkPlanarity and cutVertices to calculate the graph properties for
     * the given graph
     *
     * @param g the graph for which the graph properties will be calculated
     */
    public Properties(Graph graph) {
        _graph = graph;
        connected = checkConnected();
        //longest path check resets match variables in graph, don't call
        //again in property class or cut vertices/path will be lost
        longPath = longestPath();
        planar = checkPlanarity();
        cutVertices();
    }

    /**
     * Calculates and returns the topological ordering for the graph object. Works
     * whether the graph is connected or not. Method static so it can be used outside
     * of class. Algorithm based off:
     * https://www.geeksforgeeks.org/topological-sorting/
     * 
     * @return a stack containing the nodes of the graph in topological order
     */
    public static Stack<Node> orderTopological(Graph graph) {
        Stack<Node> out = new Stack<>();
        // keep track of which nodes have already been visited, initialized to false
        boolean[] visited = new boolean[graph.getNodeCount()];
        for (Node node : graph.getNodes()) {
            visit(out, visited, node);
        }
        return out;
    }

    //Visit method used by the topological ordering method
    private static void visit(Stack<Node> nodeStack, boolean[] visited, Node node) {
        // if node has not already been visited, meaning it is not on the stack, process
        // it and add it to the stack
        // assuming id is same as position that it is being stored in in the array
        if (!visited[node.getID()]) {
            visited[node.getID()] = true;
            // iterate through array of edges and visit each destination before adding
            // current node to stack
            for (Edge edge : node.getEdges()) {
                visit(nodeStack, visited, edge.getDest());
            }
            nodeStack.add(node);
        }
    }

    /**
     * Finds the longest directed path in the graph. Uses the the orderTopological
     * method to obtain a topological ordering for the graph. The distance is then
     * calculated between the first node in the topological ordering and every other
     * node in the graph. Since the nodes have been topologically sorted, the node
     * that is maximum distance from the first node gives us the longest path in the
     * graph. If the graph is disconnected, the same principle is applied to each
     * component of the graph and the maximum path returned is that of the longest
     * path amongst all the longest paths in all the components. Longest path is marked
     * using the label match variables in the node. Algorithm based off
     * http://www.mathcs.emory.edu/~cheung/Courses/171/Syllabus/11-Graph/Docs/longest-path-in-dag.pdf
     * 
     * @return the length of the longest path in the graph
     */
    // find longest directed path, no efficient algorithm for undirected, as may
    // become cyclic
    public int longestPath() {
        Stack<Node> nodes = Properties.orderTopological(_graph);
        // check if empty
        if (nodes.isEmpty()) {
            return -1;
        } else {
            // Initialize an array of all nodes to store distance, set all distances to -1
            int[] distance = new int[nodes.size()];
            for (int i = 0; i < distance.length; i++) {
                distance[i] = -1;
            }

            Node n = nodes.pop();
            // relying on fact that ID is same as position in array
            distance[n.getID()] = 0;
            while (!nodes.empty()) {
                n = nodes.pop();
                for (Node adj : n.getAdjacent()) {
                    // only nodes with a higher topological order i.e. the source nodes for edges
                    // with n as dest, will have a non-negative value
                    if (distance[n.getID()] < distance[adj.getID()] + 1) {
                        distance[n.getID()] = distance[adj.getID()] + 1;
                    }
                }

            }
            int max = -1;
            Node maxNode = null;
            for (int i = 0; i < distance.length; i++) {
                //find the maximum distance and the node with this distance
                if (max < distance[i]) {
                    max = distance[i];
                    maxNode = _graph.getNodes().get(i);
                }
            }
            //If there is a maximum node in the graph i.e. graph isn't empty
            // reset graph matches before marking path
            if (maxNode != null) {
                _graph.resetGraphMatches();
                markPath(maxNode, distance);
            }
            return max;
        }
    }

    //mark all the node associated with the longest path. These would be the nodes adjacent to the current node
    //with a distance of (current node distance)-1
    private void markPath(Node node, int[] distance) {
        node.setLableMatch(true);
        int currentDistance = distance[node.getID()];
        //if the node distance is 0, we have reached the first node in the path and we are done
        if (currentDistance != 0) {
            for (Node nodeAdj : node.getAdjacent()) {
                // get adj node
                
                if (distance[nodeAdj.getID()] == (currentDistance - 1)) {
                    for (Edge e: nodeAdj.getEdges()){
                        if (e.getDest().equals(node)){
                            e.setEdgeMatch(true);
                        }
                    }
                    //Could be multiple longest paths, just find and mark one
                    markPath(nodeAdj, distance);
                    return;
                }
            }
        }
    }

    /**
     * Find the graph is weakly connected. This is done by conducting a breadth
     * first search from the first node in the graph and keeping track of which
     * nodes have been visited. If any node has not been visited by the end of the
     * traversal, the graph is not weakly connected. To test for weak connectivity,
     * the adjacency lists of the nodes are used
     * 
     * @return whether the graph is weakly connected or not
     */
    // do dfs and keep track of all nodes visited in array, if one is false, not
    // connected
    public boolean checkConnected() {
        // graph empty
        if (_graph.getNodeCount() < 1) {
            return true;
        }
        boolean[] reached = new boolean[_graph.getNodeCount()];
        // choose random node to start with, just choose first
        visitConnect(reached, _graph.getNodes().get(0));
        for (boolean b : reached) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    private void visitConnect(boolean[] reached, Node n) {
        // assuming id is same as position that it is being stored in in the array
        if (!reached[n.getID()]) {
            reached[n.getID()] = true;
            // iterate through adjacent nodes (ignoring direction of edges)
            for (Node adjNode : n.getAdjacent()) {
                visitConnect(reached, adjNode);
            }
        }
    }

    /**
     * Check whether the graph is planar. This uses a special type of planarity
     * defined by the spans of the nodes. If the indexes of the first token of the
     * source and destination node of an edge overlap with the indexes of the first
     * token of the source and destination node of another edge, the graph is not
     * planar. Definintion used is from
     * https://www.aclweb.org/anthology/P10-1151.pdf Each edge is represented by the
     * indexes of their first tokens and then checked against all other edges to see
     * if the fit the criteria for crossing
     * 
     * @return whether the graph is planar or not
     */
    public boolean checkPlanarity() {
        ArrayList<TokenPairs> startEnd = new ArrayList<>();

        // add first token pairs for all edges
        for (Node n : _graph.getNodes()) {
            for (Edge e : n.getEdges()) {
                // only need consider edges joining two nodes with different first token indexes
                if (n.getStartTokenIndex() != e.getDest().getStartTokenIndex()) {
                    startEnd.add(new TokenPairs(e, n));
                }
            }
        }

        // iterate through and check if any token pairs fit the 'crossing criteria'
        for (int i = 0; i < startEnd.size(); i++) {
            for (int j = i + 1; j < startEnd.size(); j++) {
                if (startEnd.get(i).checkCrossing(startEnd.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Find and mark all the cut-vertices in the graph. Cut vertices are marked using the spanLabelMatch
     * variable in the node class. Cut vertices are only calculated for (weakly) connected graphs
     * algorithm based off https://www.geeksforgeeks.org/articulation-points-or-cut-vertices-in-a-graph/
     */
    public void cutVertices() {
        boolean[] visited = new boolean[_graph.getNodeCount()];
        //array for storing time for when node was 'discovered', initialize to -1
        int[] timeDesc = new int[_graph.getNodeCount()];
        //store earliest value reachable from a descendant of the node
        int[] low = new int[_graph.getNodeCount()];
        if (_graph.getNodeCount() != 0) {
            //do not reset graph match variables as will then reset longest path
            //as method called in constructor after longest path
            visitCutVertex(_graph.getNodes().get(0), visited, timeDesc, low, 0);
        }
        //if graph is not connected, find cut vertices in other components
        if(!connected){
            //iterate through and find nodes that have not been visited yet, check these for
            //cut vertices
            for (int i = 0; i < visited.length; i++){
                if (!visited[i]){
                    visitCutVertex(_graph.getNodes().get(i), visited, timeDesc, low, 0 );
                }
            }
        }
    }

    private void visitCutVertex(Node currentNode, boolean[] visited, int[] timeDesc, int[] low, int time) {
        //set the current node to visited
        int currentIndex = currentNode.getID();
        visited[currentIndex] = true;
        //set discovery time and low time based on parent node
        timeDesc[currentIndex] = time;
        low[currentIndex] = time;
        //number of children for the node in the DFS tree
        int children = 0;

        //iterate through adjacent nodes
        for (Node adj : currentNode.getAdjacent()) {
            int adjIndex = adj.getID();
            if (!visited[adjIndex]) {
                children++;
                visited[adjIndex] = true;
                //increment the time by one to denote child/ancestor
                visitCutVertex(adj, visited, timeDesc, low, time + 1);
                //update the low for the current node
                low[currentIndex] = Math.min(low[currentIndex], low[adjIndex]);

                //first condition for CV: root node and has two or more children
                //root index has discovery time of 0
                if (timeDesc[currentIndex] == 0 && children > 1) {
                    //isCut[currentIndex] = true;
                    currentNode.setSpanMatch(true);
                }
                //second condition for CV: not a root node and one of its children does
                //not have an edge to a earlier discovered node (has higher low value than the
                //current nodes discovery value
                if (timeDesc[currentIndex] != 0 && low[adjIndex] >= timeDesc[currentIndex]) {
                    //isCut[currentIndex] = true;
                    currentNode.setSpanMatch(true);
                }
            } else if ((timeDesc[adjIndex] - 1) != timeDesc[currentIndex]) {
                //if the node is the the current nodes parent node in the DFS tree, update the low value
                low[currentIndex] = Math.min(low[currentIndex], timeDesc[adjIndex]);
            }
        }

    }

    /**
     * Return the value of connected variable
     * 
     * @return the value of the connected variable
     */
    public boolean getConnected() {
        return connected;
    }

    /**
     * Return the value of the longest path variable
     * 
     * @return the value of the longest path variable
     */
    public int getLongestPath() {
        return longPath;
    }

    /**
     * Return the value of the planar variable
     * 
     * @return the value of the planar variable
     */
    public boolean getPlanar() {
        return planar;
    }

    // class which stores max and min of the first token for the source and
    // destination of an edge with methods to check if two token pairs are crossing
    // as well as get methods and a toString method
    private class TokenPairs {

        private long min;
        private long max;

        public TokenPairs(Edge e, Node source) {
            long sourceToken = source.getStartTokenIndex();
            long destToken = e.getDest().getStartTokenIndex();
            min = Math.min(sourceToken, destToken);
            max = Math.max(sourceToken, destToken);
        }

        public boolean checkCrossing(TokenPairs other) {
            // check the two combinations, can either have other '<' current or current '<'
            // other to get crossing
            return (min < other.getMin() && other.getMin() < max && max < other.getMax())
                    || (other.getMin() < min && min < other.getMax() && other.getMax() < max);
        }

        public long getMin() {
            return min;
        }

        public long getMax() {
            return max;
        }

        public String toString() {
            return "min: " + min + " max: " + max;
        }
    }
}
