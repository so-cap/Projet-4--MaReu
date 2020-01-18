package com.sophie.mareu.controller;

import com.sophie.mareu.DI.DI;

import java.util.AbstractMap;
import java.util.ArrayList;

public class RoomsPerHour {
    private AbstractMap.SimpleEntry<Integer, String> hour;
    private ArrayList<String> rooms = new ArrayList<>();
    private static int key = 0;

    RoomsPerHour() {}

    public RoomsPerHour(String hour, ArrayList<String> rooms) {
        this.hour = new AbstractMap.SimpleEntry<>(key++, hour);
        this.rooms = rooms;
        if (key == DI.getNewHoursList().size()) key = 0;
    }

    public AbstractMap.SimpleEntry<Integer, String> getHour() {
        return hour;
    }

    void setHour(Integer key, String hour) {
        this.hour = new AbstractMap.SimpleEntry<>(key, hour);
    }

    public ArrayList<String> getRooms() {
        return rooms;
    }

    void addRoom(String room){
        rooms.add(room);
    }
}