package com.repgraph.services;

import java.util.ArrayList;

import com.repgraph.models.Graph;

/**
 * Analysis service interface
 */
public interface IAnalysisService {

    /**
     * Updates properties object with new graph
     */
    public abstract void updateProperties();

    /**
     * Check whether graph is connected 
     * 
     * @return true if graph is connected, false if not
     */
    public abstract boolean isConnected();

    /**
     * Check whether graph is planar
     * 
     * @return true if graph is planar, false if not
     */
    public abstract boolean isPlanar();


    /**
     * Returns the length of the longest path
     * 
     * @return the length of the longest path
     */
    public abstract int getLongestPath();

    /**
     * Finds cut vertices properties
     */
    public abstract void findCutVertices();

    /**
     * Find pattern in library created by a subgraph created by a node of the currently selected graph
     * 
     * @param nodeId id of node to generate subgraph
     * @return returns a list of graphs containing the specified pattern
     */
    public abstract ArrayList<Graph> searchPattern(String nodeId);

    /**
     * Finds graphs in library containing nodes matching a group within the selected graph
     * 
     * @param NodeIds string list of node ids to find matches
     * @return a list of graphs with matching nodes
     */
    public abstract ArrayList<Graph> searchMatches(String[] nodeIds);

}