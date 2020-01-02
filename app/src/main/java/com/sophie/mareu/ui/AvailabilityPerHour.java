package com.sophie.mareu.ui;

import java.util.ArrayList;

public class AvailabilityPerHour{
    private String hour;
    private ArrayList<String> rooms;

    public AvailabilityPerHour(String hour, ArrayList<String> rooms){
        this.hour = hour;
        this.rooms = rooms;
    }

    public String getHour(){
        return hour;
    }

    public ArrayList<String> getRooms(){
        return rooms;
    }
}