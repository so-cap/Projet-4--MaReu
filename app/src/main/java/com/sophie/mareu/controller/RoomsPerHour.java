package com.sophie.mareu.controller;

import java.util.AbstractMap;
import java.util.ArrayList;

public class RoomsPerHour {
    private AbstractMap.SimpleEntry<Integer,String> hour;
    private ArrayList<String> rooms;
    private static int key = 0;

    public RoomsPerHour(String hour, ArrayList<String> rooms){
        this.hour = new AbstractMap.SimpleEntry<>(key, hour);
        this.rooms = rooms;
        key++;
    }

    public AbstractMap.SimpleEntry<Integer, String> getHour(){
        return hour;
    }

    public ArrayList<String> getRooms(){
        return rooms;
    }
}