package uk.org.padma.yogaglossary;

import java.util.ArrayList;

public class GEntry {
    public String term;
    public ArrayList<String> definition;
    public String search;

    public GEntry(){
        this.definition = new ArrayList<String>();
    }
}

