package com.repgraph.services;

import java.io.File;
import java.util.ArrayList;

import com.repgraph.models.DMRSLibrary;
import com.repgraph.models.Graph;

/**
 * Library service interface
 */
public interface ILibraryService {

    /**
     * Loads dmrs file into library
     * 
     * @param uploadedFile uploaded file variable
     * @return boolean stipulating whether the file was successfully loaded and
     *         parsed or not
     */
    public abstract boolean loadFile(File uploadedFile);

    /**
     * Gets graph by library index
     * 
     * @param index library index of desired graph
     * @return desired graph or null when graph is not found
     */
    public abstract Graph getGraphByIndex(int index);

    /**
     * Gets graph by dmrs Id
     * 
     * @param id dmrs Id of desired graph
     * @return desired graph or null when graph is not found
     */
    public Graph getGraphById(String id);


    /**
     * Returns library browsing json
     * 
     * @return library browsing json containing Id's and Sentences
     */
    public abstract String getBrowsingJson();

    /**
     * Returns library browsing json for specified graphs
     * 
     * @param matchedGraphs
     * @return returns json string containing headers for matched graphs
     */
    public String getBrowsingJson(ArrayList<Graph> matchedGraphs);

    /**
     * Select a graph to analyse
     * 
     * @param id id of selected graph
     */
    public abstract void selectGraph(String id);

    /**
     * Return currently selected graph
     * 
     * @return Currently selected graph
     */
    public abstract Graph getSelectedGraph();

    /**
     * Returns dmrs library object
     * 
     * @return drms library object
     */
    public abstract DMRSLibrary getDmrsLibrary();

    /**
     * Checks if library is empty
     * 
     * @return true if library is empty, false if not
     */
    public abstract boolean isEmpty();

    /**
     * Clears selected graph
     */
    public abstract void clearSelected();

    /**
     * Clears dmrs library of all stored graphs
     */
    public abstract void clearLibrary();
}