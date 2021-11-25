package com.repgraph.models;

import org.json.simple.JSONObject;

public class Token {
    // potentially unnecessary, could just refer to array index
    private long index;
    private String form;
    private String lemma;
    private String cArg;

    /**
     * Constructor which takes a Json object representation of a node and extracts
     * the index, form, lemma and carg
     *
     * @param t the Json object representation of the token
     */
    public Token(JSONObject t) {
        index = (long) t.get("index");
        form = (String) t.get("form");
        lemma = (String) t.get("lemma");
        cArg = (String) t.get("carg");
    }

    /**
     * Constructor for testing which takes in an index and assigns it to the token's
     * index
     *
     * @param index
     */
    public Token(int index) {
        this.index = index;
    }

    /**
     * Constructor for testing which takes in a form and assigns it
     * @param form the form for the token
     */
    public Token(String form){
        this.form = form;
    }

    /**
     * Returns the index of the token
     * @return the index of the token
     */
    public long getIndex() {
        return index;
    }

    /**
     * Returns the lemma of the token
     * @return the token's lemma
     */
    public String getLemma(){
        return lemma;
    }

    /**
     * Returns the form of the token
     * @return the token's form
     */
    public String getForm(){
        return form;
    }

    /**
     * Returns the carg of the token
     * @return the token's carg
     */
    public String getCarg(){
        return cArg;
    }

    /**
     * Overriding equals method for token class. Two tokens are equal if their forms are equal
     * @return whether the two objects are equal or not
     */
    public boolean equals(Object o){
        // If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
        } 
        // Check if o is an instance of Token or not
        if (!(o instanceof Token)) { 
            return false; 
        } 
        // typecast o to Token and compare form
        Token other = (Token) o;  
        return form.equals(other.getForm());
    }

    /**
     * Method to override toString() for the Token class
     *
     * @return a string representation of token comprised of its lemma, form, carg
     *         and index
     */
    public String toString() {
        return "Lemma: " + lemma + " Form: " + form + " Carg: " + cArg + " id: " + index;
    }
}
