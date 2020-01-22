package com.sophie.mareu.service;

import com.sophie.mareu.model.RoomsPerHour;
import com.sophie.mareu.service.RoomsAvailabilityApiService;
import java.util.ArrayList;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class RoomsAvailabilityServiceImpl implements RoomsAvailabilityApiService {
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

    public void updateAvailableHours(ArrayList<RoomsPerHour> availableHoursAndRooms) {
        roomsPerHourList = availableHoursAndRooms;

        // delete hour availability if all the rooms are taken
        for (int position = 0; position < roomsPerHourList.size(); position++) {
            if (roomsPerHourList.get(position).getRooms().isEmpty()) {
                roomsPerHourList.remove(position);
                break;
            }
        }
    }
}


