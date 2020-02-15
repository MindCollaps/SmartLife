package de.mindcollaps.smartlife.files;

import org.json.JSONObject;

import java.util.ArrayList;

public class Device {
    
    private String name;
    private String path;
    private ArrayList<JSONObject> features = new ArrayList<>();

    public void addFeature(JSONObject object){
        features.add(object);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<JSONObject> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<JSONObject> features) {
        this.features = features;
    }
}
