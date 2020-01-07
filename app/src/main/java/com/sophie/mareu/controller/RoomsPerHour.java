package com.sophie.mareu.controller;

import android.util.Log;

import java.util.AbstractMap;
import java.util.ArrayList;

public class RoomsPerHour {
    private AbstractMap.SimpleEntry<Integer, String> hour;
    private ArrayList<String> rooms;
    private static int key = 0;

    public RoomsPerHour() {
    }

    private static final String TAG = "LOGGRoomsPerHour";
    public RoomsPerHour(String hour, ArrayList<String> rooms) {
        this.hour = new AbstractMap.SimpleEntry<>(key++, hour);
        this.rooms = rooms;
        if (key == 12) key = 0;
    }

    public AbstractMap.SimpleEntry<Integer, String> getHour() {
        return hour;
    }

    public void setHour(Integer key, String hour) {
        this.hour = new AbstractMap.SimpleEntry<>(key, hour);
    }

    public ArrayList<String> getRooms() {
        return rooms;
    }
}