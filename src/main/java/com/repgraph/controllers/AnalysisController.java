package com.repgraph.controllers;

import com.repgraph.services.AnalysisService;
import com.repgraph.services.LibraryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnalysisController {

    @Autowired
    private LibraryService _libraryService;

    @Autowired
    private AnalysisService _analysisService;

    @GetMapping({ "/analysis" })
    public String index(Model model) {
        if (_libraryService.getSelectedGraph() == null){
            return "redirect:/library";
        }
        //updates properties to currently selected graph.
        _analysisService.updateProperties();
        model.addAttribute("longestPath", _analysisService.getLongestPath());
        String weaklyConnectedMsg = _analysisService.isConnected() ? "Graph is connected."
            : "Graph is not connected.";
        model.addAttribute("weaklyConnected", weaklyConnectedMsg);
        String planarityMsg = _analysisService.isPlanar() ? "Graph is planar."
            : "Graph is not planar.";
        model.addAttribute("isPlanar", planarityMsg);
        return "AnalysisView";
    }

    @GetMapping("/analysis/getVisualization/{analysisType}")
    public ResponseEntity<String> getVisualization(@PathVariable(value = "analysisType", required = true) String analysisType,
    @RequestParam(value = "graphId", required = false) String graphId){
        if (_libraryService.getSelectedGraph() == null){
            return ResponseEntity.badRequest().body("No graph selected.");
        }
        if (analysisType.equals("graphComparison")){
            _libraryService.getSelectedGraph().compareGraph(_libraryService.getGraphById(graphId));
        }
        else if (analysisType.equals("longestPath")){
            _analysisService.getLongestPath();
        }
        else if (analysisType.equals("cutVertices")){
            _analysisService.getLongestPath();
            _analysisService.findCutVertices();
        }
        String jsonResponse = graphId == null ? _libraryService.getSelectedGraph().getVisualisationJson(true, analysisType)
            : _libraryService.getGraphById(graphId).getVisualisationJson(true, analysisType);
        return ResponseEntity.ok(jsonResponse);
    }

    @GetMapping("/analysis/getSubgraphViz/{nodeId}")
    public ResponseEntity<String> getSubgraph(@PathVariable(value = "nodeId", required = true) String nodeId){
        if (_libraryService.getSelectedGraph() == null){
            return ResponseEntity.badRequest().body("No graph selected.");
        }
        String jsonResponse = _libraryService.getSelectedGraph().getSubgraphVisualisation("subgraph",nodeId);
        return ResponseEntity.ok(jsonResponse);
    }

    @GetMapping("/analysis/browseMatches/{nodeId}")
    public ResponseEntity<String> browseMatches(@PathVariable(value = "nodeId", required = true) String nodeId) {
        return ResponseEntity.ok(_libraryService.getBrowsingJson(_analysisService.searchPattern(nodeId)));
    }

    @GetMapping("/analysis/browseNodeMatches/{nodeIds}")
    public ResponseEntity<String> browseNodeMatches(
            @PathVariable(value = "nodeIds", required = true) String[] nodeIds) {
        return ResponseEntity.ok(_libraryService.getBrowsingJson(_analysisService.searchMatches(nodeIds)));
    }

}