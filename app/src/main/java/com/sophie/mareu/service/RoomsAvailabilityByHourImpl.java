package com.sophie.mareu.service;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.controller.RoomsPerHour;

import java.util.ArrayList;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class RoomsAvailabilityByHourImpl extends RoomsAvailabilityService {
    private ArrayList<RoomsPerHour> mRoomsPerHourList = new ArrayList<>();

    public RoomsAvailabilityByHourImpl(){
        initRoomsAndHours();
    }

    public void initRoomsAndHours(){
        RoomsPerHour roomsPerHour;
        ArrayList<String> hoursList = DI.getNewHoursList();
        ArrayList<String> roomsList = DI.getNewRoomsList();

        if(!(mRoomsPerHourList.isEmpty())) mRoomsPerHourList.clear();

        for (int position = 0; position < (hoursList.size()); position++) {
            roomsPerHour = new RoomsPerHour(hoursList.get(position), new ArrayList<>(roomsList));
            mRoomsPerHourList.add(roomsPerHour);
        }
    }

    public ArrayList<RoomsPerHour> getRoomsPerHourList(){
        return mRoomsPerHourList;
    }

    public void updateAvailableHours(ArrayList<RoomsPerHour> availableHoursAndRooms) {
        mRoomsPerHourList = availableHoursAndRooms;

        // delete hour availability if all the rooms are taken
        for(int position = 0; position < mRoomsPerHourList.size(); position++) {
            if (mRoomsPerHourList.get(position).getRooms().isEmpty()){
                mRoomsPerHourList.remove(position);
                break;
            }
        }
    }
}


