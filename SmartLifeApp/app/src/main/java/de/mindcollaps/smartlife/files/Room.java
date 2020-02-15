package de.mindcollaps.smartlife.files;

import java.util.ArrayList;

public class Room {
    
    private String name;
    private ArrayList<Device> devices = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    public void addDevide(Device device){
        devices.add(device);
    }
}
