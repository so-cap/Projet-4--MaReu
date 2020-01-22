package com.sophie.mareu.model;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.controller.MeetingsController;

import java.util.AbstractMap;
import java.util.ArrayList;

public class RoomsPerHour {
    private AbstractMap.SimpleEntry<Integer, String> hour;
    private ArrayList<String> rooms = new ArrayList<>();
    private static int key = 0;

    public RoomsPerHour() {}

    public RoomsPerHour(String hour, ArrayList<String> rooms) {
        this.hour = new AbstractMap.SimpleEntry<>(key++, hour);
        this.rooms = rooms;

        MeetingsController controller = DI.getMeetingsController();
        if (key == controller.getHours().size()) key = 0;
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

    public void addRoom(String room){
        rooms.add(room);
    }

}