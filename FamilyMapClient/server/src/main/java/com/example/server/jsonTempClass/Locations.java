package com.example.server.jsonTempClass;

import java.util.ArrayList;

/**
 * a data member that stores an array of classes Location
 */
public class Locations {
    ArrayList<Location> data;

    public Locations(ArrayList<Location> data) {
        this.data = data;
    }

    public ArrayList<Location> getData() {
        return data;
    }

    public void setData(ArrayList<Location> data) {
        this.data = data;
    }
}
