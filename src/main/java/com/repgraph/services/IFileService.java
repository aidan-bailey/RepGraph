package com.repgraph.services;

/**
 * File service declaration
*/
public interface IFileService {
    
    /**
     * Deletes specified file from the server
     * 
     * @param filename name of file to be deleted
     * @return returns whether or not the deletion was successful
     */
    public abstract boolean deleteFile(String filename);

    /**
     * Completes upload procedure by loading uploaded graph
     * 
     * @return returns whether or not loading the uploaded graph was successful
     */
    public abstract boolean completeUpload();

    /**
     * Loads specified file into library
     * 
     * @param filename name of file to be loaded
     * @return returns whether or not file was loaded successfully
     */
    public abstract boolean loadFile(String filename);

    /**
     * Saves recently uploaded file with specified name
     * 
     * @param filename name with which to save file
     * @return returns whether or not file was successfully saved
     */
    public abstract boolean saveFile(String filename);

    /**
     * Retreives all saved files
     * 
     * @return list of saved filenames
     */
    public abstract String[] getFilenames();

    /**
     * Loads cached file
     * 
     * @return whether or not the cache was successfully loaded
     */
    public abstract boolean loadCache();

    /**
     * Checks if cache exists
     * 
     * @return whether or not a cached file exists
     */
    public abstract boolean checkCache();

}