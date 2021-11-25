package com.repgraph.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * File service implementation
 */
@Service
public class FileService implements IFileService {

    private final static String CACHE_PATH = "data/cache";
    private final static String SAVED_PATH = "data/saved";

    @Autowired
    private LibraryService _libraryService;

    /**
     * Deletes specified file from the server
     * 
     * @param filename name of file to be deleted
     * @return returns whether or not the deletion was successful
     */
    @Override
    public boolean deleteFile(String filename) {
        File saveFile = new File(SAVED_PATH + "/" + filename);
        if (!saveFile.exists()) {
            return false;
        }
        return saveFile.delete();
    }

    /**
     * Completes upload procedure by loading uploaded graph
     * 
     * @return returns whether or not loading the uploaded graph was successful
     */
    @Override
    public boolean completeUpload() {
        File tempFile = new File(CACHE_PATH + "/uploaded_dmrs.tmp");
        if (!tempFile.exists()){
            return false;
        }
        boolean flag =_libraryService.loadFile(tempFile);
        tempFile.delete();
        return flag;
    }

    /**
     * Loads specified file into library
     * 
     * @param filename name of file to be loaded
     * @return returns whether or not file was loaded successfully
     */
    @Override
    public boolean loadFile(String filename) {
        File saveFile = new File(SAVED_PATH + "/" + filename);
        if (!saveFile.exists()){
            return false;
        }
        return _libraryService.loadFile(saveFile);
    }

    /**
     * Saves recently uploaded file with specified name
     * 
     * @param filename name with which to save file
     * @return returns whether or not file was successfully saved
     */
    @Override
    public boolean saveFile(String filename) {
        File tempFile = new File(CACHE_PATH + "/uploaded_dmrs.tmp");
        File saveFile = new File(SAVED_PATH + "/" + filename);
        try {
            Files.copy(tempFile.toPath(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Retreives all saved files
     * 
     * @return list of saved filenames
     */
    @Override
    public String[] getFilenames() {
        return new File(SAVED_PATH).list();
    }

    /**
     * Loads cached file
     * 
     * @return returns whether or not the cache was successfully loaded
     */
    @Override
    public boolean loadCache(){
        File file = new File(CACHE_PATH + "/cache.tmp");
        return _libraryService.loadFile(file);
    }
    
    /**
     * Checks if cache exists
     * 
     * @return whether or not a cached file exists
     */
    public boolean checkCache(){
        File file = new File(CACHE_PATH + "/cache.tmp");
        return file.exists();
    }
}