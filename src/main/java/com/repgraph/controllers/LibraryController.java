package com.repgraph.controllers;

import com.repgraph.services.LibraryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LibraryController {

    @Autowired
    private LibraryService _libraryService;

    @GetMapping("/library")
    public String index(Model model) {
        if (_libraryService.isEmpty()) {
           return "redirect:/landing";
        }
        return "LibraryView";
    }

    @GetMapping("/library/browse")
    public ResponseEntity<String> headers() {
        return ResponseEntity.ok(_libraryService.getBrowsingJson());
    }

    @GetMapping("/library/select-graph/{graphId}")
    public ResponseEntity<String> selectGraph(@PathVariable(value = "graphId", required = true) String graphId,
            @RequestParam(value = "withToken", required = true, defaultValue = "true") boolean withToken) {
        _libraryService.selectGraph(graphId);
        return ResponseEntity.ok(_libraryService.getSelectedGraph().getVisualisationJson(withToken, "none"));
    }

}