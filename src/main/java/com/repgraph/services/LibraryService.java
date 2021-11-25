package com.repgraph.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import com.repgraph.models.DMRSLibrary;
import com.repgraph.models.Graph;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Library service implementation.
 */
@Service
public class LibraryService implements ILibraryService {

    @Autowired
    private DMRSLibrary _dmrsLibrary;

    private Graph _graph;

    /**
     * Loads dmrs file into library
     * 
     * @param uploadedFile uploaded file variable
     * @return boolean stipulating whether the file was successfully loaded and
     *         parsed or not
     */
    @Override
    public boolean loadFile(File uploadedFile) {
        JSONParser jparser = new JSONParser();
        try (BufferedReader file = new BufferedReader(new FileReader(uploadedFile))) {
            String graph = "";
            while ((graph = file.readLine()) != null) {
                Object graphObj = jparser.parse(graph);
                JSONObject jGraph = (JSONObject) graphObj;
                _dmrsLibrary.getGraphs().add(new Graph(jGraph));
            }
            File cacheFile = new File("data/cache/cache.tmp");
            try {
                Files.copy(uploadedFile.toPath(), cacheFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Gets graph by library index
     * 
     * @param index library index of desired graph
     * @return desired graph or null when graph is not found
     */
    @Override
    public Graph getGraphByIndex(int index) {
        // return null if graph index requested is out of range
        return index > 0 && index < _dmrsLibrary.getGraphs().size() ? _dmrsLibrary.getGraphs().get(index) : null;
    }

    /**
     * Gets graph by dmrs Id
     * 
     * @param id dmrs Id of desired graph
     * @return desired graph or null when graph is not found
     */
    @Override
    public Graph getGraphById(String id) {
        // return null if graph index requested is out of range
        for (Graph graph : _dmrsLibrary.getGraphs())
            if (id.equals(graph.getId()))
                return graph;
        return null;
    }


    /**
     * Returns library browsing json
     * 
     * @return library browsing json containing Id's and Sentences
     */
    @Override
    public String getBrowsingJson() {
        JSONArray jsonArray = new JSONArray();
        for (Graph graph : _dmrsLibrary.getGraphs()) {
            JSONObject tableItem = new JSONObject();
            tableItem.put("id", graph.getId());
            tableItem.put("input", graph.getSentence());
            jsonArray.add(tableItem);
        }
        return jsonArray.toJSONString();
    }

    /**
     * Returns library browsing json for specified graphs
     * 
     * @param matchedGraphs
     * @return returns json string containing headers for matched graphs
     */
    @Override
    public String getBrowsingJson(ArrayList<Graph> matchedGraphs) {
        JSONArray jsonArray = new JSONArray();
        for (Graph graph : matchedGraphs) {
            JSONObject tableItem = new JSONObject();
            tableItem.put("id", graph.getId());
            tableItem.put("input", graph.getSentence());
            jsonArray.add(tableItem);
        }
        return jsonArray.toJSONString();
    }

    /**
     * Select a graph to analyse
     * 
     * @param id id of selected graph
     */
    @Override
    public void selectGraph(String id) {
        _graph = getGraphById(id);
    }

    /**
     * Return currently selected graph
     * 
     * @return currently selected graph
     */
    @Override
    public Graph getSelectedGraph() {
        return _graph;
    }

    /**
     * Returns dmrs library object
     * 
     * @return drms library object
     */
    public DMRSLibrary getDmrsLibrary() {
        return _dmrsLibrary;
    }

    /**
     * Checks if library is empty
     * 
     * @return true if library is empty, false if not
     */
    public boolean isEmpty() {
        return _dmrsLibrary.getCount() == 0;
    }

    /**
     * Clears selected graph
     */
    public void clearSelected() {
        _graph = null;
    }

    /**
     * Clears dmrs library of all stored graphs
     */
    @Override
    public void clearLibrary() {
        _graph = null;
        _dmrsLibrary.getGraphs().clear();
    }
}