package com.repgraph.models;
import java.util.*;

public class NodePosition {

    private Graph g;
    //highest layer is at the lowest index i.e. index 0
    private ArrayList<ArrayList<Node>> layers;
    //maximum number of nodes in a layer
    final private int maxLayerLength = 10;

    public NodePosition(Graph g) {
        this.g = g;
        layers = new ArrayList<>();
        findLayers();
        sortLayers();
    }

    /**
     * Find a layered representation of the graph such that source nodes are always
     * above destination nodes in the layers. Calls static topological order method 
     * in properties class
     */
    public void findLayers() {
        layers.add(new ArrayList<Node>());
        Stack<Node> topologicalOrder = Properties.orderTopological(g);
        //if the graph is not empty
        if (!topologicalOrder.isEmpty()) {
            layers.get(0).add(topologicalOrder.pop());
            while (!topologicalOrder.isEmpty()) {
                Node processNode = topologicalOrder.pop();
                int insertIndex = 0;
                //find layer for insertion
                for (int j = 0; j < layers.size(); j++) {
                    //if the layer contains a node which contains an edge for which the process node is a destination node, change insert index to be level below
                    if (containsSource(layers.get(j), processNode)) {
                        insertIndex = j+1;
                    }
                }
                //insert node into a layer less than or equal to the insertIndex which has enough space
                insertIntoLayer(processNode, insertIndex);
                // if (insertIndex == layers.size()){
                //     layers.add(new ArrayList<>());
                // }
                // layers.get(insertIndex).add(processNode);
            }
        }
    }

    //check if the layer of nodes contains a node which contains an edge for which the other node is a destination
    private boolean containsSource(ArrayList<Node> layerNodes, Node node) {
        for (Node source : layerNodes) {
            for (Edge e : source.getEdges()) {
                //compare references to see if it is the same node
                if (e.getDest() == node) {
                    return true;
                }
            }
        }
        return false;
    }

    private void insertIntoLayer(Node node, int index){
        //if all the layers are full, make new layer
        if (index >= layers.size()){
            ArrayList<Node> newLayer = new ArrayList<>();
            newLayer.add(node);
            layers.add(newLayer);
        } else {
            //if there is space in the layer, add the node. Else recurse down until
            //find an empty enough layer
            if (layers.get(index).size() < maxLayerLength){
                layers.get(index).add(node);
            } else {
                insertIntoLayer(node, index+1);
            }
        }
    }

    /**
     * Get the array containing the layer representation of the given graph
     * 
     * @return
     */
    public ArrayList<ArrayList<Node>> getLayers() {
        return layers;
    }

    /**
     * Sorts each layer of nodes by their start and end token indexes, arranged in ascending order
     */
    public void sortLayers(){
        for (ArrayList<Node> arr: layers){
            Collections.sort(arr);
        }
    }


}
