package com.repgraph.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.repgraph.services.FileService;
import com.repgraph.services.LibraryService;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class LandingController {

    @Autowired
    private FileService _fileService;

    @Autowired
    private LibraryService _libraryService;

    @GetMapping({ "/", "/landing" })
    public String index(Model model) {
        _libraryService.clearLibrary();
        model.addAttribute("cached", _fileService.checkCache());
        JSONArray filenames = new JSONArray();
        for (String filename : _fileService.getFilenames()) {
            JSONArray subcontainer = new JSONArray();
            subcontainer.add(filename);
            filenames.add(subcontainer);
        }
        model.addAttribute("savedFilenames", filenames.toJSONString());
        return "LandingView";
    }

    @PostMapping("/landing/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile multipartFile) {
        File file = new File("data/cache/uploaded_dmrs.tmp");
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("{\"success\": true}");
    }

    @GetMapping("/landing/cancelUpload")
    public ResponseEntity<String> cancelUpload() {
        File file = new File("dateea/cache/uploaded_dmrs.tmp");
        file.delete();
        _libraryService.clearLibrary();
        return ResponseEntity.ok("{\"success\": true}");
    }

    @GetMapping("/landing/stored")
    public ResponseEntity<String> storedFiles() {
        JSONArray filenames = new JSONArray();
        for (String filename : _fileService.getFilenames()) {
            JSONArray subcontainer = new JSONArray();
            subcontainer.add(filename);
            filenames.add(subcontainer);
        }
        return ResponseEntity.ok(filenames.toJSONString());
    }

    @GetMapping("/landing/save/{filename}")
    public ResponseEntity<String> save(@PathVariable(value = "filename", required = true) String filename) {
        boolean flag = _fileService.saveFile(filename);
        return ResponseEntity.ok("{\"success\": "+flag+"}");
    }

    @GetMapping("/landing/load/{filename}")
    public ResponseEntity<String> loadFile(@PathVariable(value = "filename", required = true) String filename) {
        boolean flag = _fileService.loadFile(filename);
        return ResponseEntity.ok("{\"success\": "+flag+"}");
    }

    @GetMapping("/landing/delete/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable(value = "filename", required = true) String filename) {
        boolean flag = _fileService.deleteFile(filename);
        return ResponseEntity.ok("{\"success\": "+flag+"}");
    }

    @GetMapping("/landing/loadUpload")
    public ResponseEntity<String> loadUpload(){
        boolean flag = _fileService.completeUpload();
        return ResponseEntity.ok("{\"success\": "+flag+"}");
    }

    @GetMapping("/landing/loadCache")
    public ResponseEntity<String> loadCache(){
        if (!_fileService.checkCache()){
            return ResponseEntity.ok("{\"success\": "+false+"}");
        }
        boolean flag = _fileService.loadCache();
        return ResponseEntity.ok("{\"success\": "+flag+"}");
    }

}