package com.repgraph.services;

import java.util.ArrayList;

import com.repgraph.models.Graph;
import com.repgraph.models.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Analysis service implementation
 */
@Service
public class AnalysisService implements IAnalysisService {

    private Properties _properties;

    @Autowired
    private LibraryService _libraryService;

    /**
     * Check whether graph is connected
     * 
     * @return true if graph is connected, false if not
     */
    @Override
    public boolean isConnected() {
        return _properties.checkConnected();
    }

    /**
     * Check whether graph is planar
     * 
     * @return true if graph is planar, false if not
     */
    @Override
    public boolean isPlanar() {
        return _properties.checkPlanarity();
    }

    /**
     * Returns the length of the longest path
     * 
     * @return the length of the longest path
     */
    @Override
    public int getLongestPath() {
        return _properties.longestPath();
    }

    /**
     * Updates properties object with new graph
     */
    @Override
    public void updateProperties() {
        _properties = new Properties(_libraryService.getSelectedGraph());
    }

    /**
     * Returns cut vertices properties
     */
    @Override
    public void findCutVertices() {
        _properties.cutVertices();
    }

    /**
     * Find pattern in library created by a subgraph created by a node of the
     * currently selected graph
     * 
     * @param nodeId id of node to generate subgraph
     * @return returns a list of graphs containing the specified pattern
     */
    @Override
    public ArrayList<Graph> searchPattern(String nodeId) {
        return _libraryService.getDmrsLibrary().findPattern(_libraryService.getSelectedGraph().findNodeById(nodeId));
    }

    /**
     * Finds graphs in library containing nodes matching a group within the selected graph
     * 
     * @param NodeIds string list of node ids to find matches
     * @return a list of graphs with matching nodes
     */
    @Override
    public ArrayList<Graph> searchMatches(String[] nodeIds) {
        ArrayList<String> nodeList = new ArrayList<String>();
        for (String nodeId : nodeIds) {
            nodeList.add(nodeId);
        }
        return _libraryService.getDmrsLibrary().findPatternSimple(nodeList);
    }

}