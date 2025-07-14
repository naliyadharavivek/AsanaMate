package com.asana.mate;

import java.util.List;

public class YogaPose {
    public String category;
    public String description;
    public String engName;
    public String sanName;
    public List<String> imageLinks; // was Map<String, String>

    public YogaPose() {
        // Needed for Firebase
    }
}
