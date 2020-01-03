package com.sophie.mareu.controller;

import java.util.AbstractMap;
import java.util.ArrayList;

public class AvailabilityPerHour{
    private AbstractMap.SimpleEntry<Integer,String> hour;
    private ArrayList<String> rooms;
    private static int key = 0;

    public AvailabilityPerHour(String hour, ArrayList<String> rooms){
        this.hour = new AbstractMap.SimpleEntry<>(key, hour);
        this.rooms = rooms;
        key++;
    }

    public String getHour(){
        return hour.getValue();
    }

    public ArrayList<String> getRooms(){
        return rooms;
    }
}