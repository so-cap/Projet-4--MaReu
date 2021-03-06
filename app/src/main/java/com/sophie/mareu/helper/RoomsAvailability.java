package com.sophie.mareu.helper;

import com.sophie.mareu.model.RoomsPerHour;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class RoomsAvailability implements Serializable {
    private ArrayList<RoomsPerHour> roomsPerHourList = new ArrayList<>();

    public void initRoomsPerHourList(ArrayList<String> hours, ArrayList<String> rooms){
        RoomsPerHour roomsPerHour;

        if(!(roomsPerHourList.isEmpty())) roomsPerHourList.clear();

        for (int position = 0; position < (hours.size()); position++) {
            roomsPerHour = new RoomsPerHour(hours.get(position), new ArrayList<>(rooms));
            roomsPerHourList.add(roomsPerHour);
        }
    }

    public ArrayList<RoomsPerHour> getRoomsPerHourList(){
        return roomsPerHourList;
    }

    public void updateAvailableHoursAndRooms(ArrayList<RoomsPerHour> availableHoursAndRooms) {
        roomsPerHourList = availableHoursAndRooms;

        for (int position = 0; position < roomsPerHourList.size(); position++) {
            if (roomsPerHourList.get(position).getRooms().isEmpty()) {
                roomsPerHourList.remove(position);
                break;
            }
        }
    }
}


