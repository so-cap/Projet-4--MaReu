package com.sophie.mareu.service;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.controller.RoomsPerHour;

import java.util.ArrayList;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class RoomsAvailabilityService {
    private static ArrayList<RoomsPerHour> mRoomsPerHourList = new ArrayList<>();

    public static void initRoomsAndHours(){
        // use tags for each hour view (get size of array, then loop to setTag on button.
        RoomsPerHour roomsPerHour;
        ArrayList<String> hoursList = DI.getNewHoursList();
        ArrayList<String> roomsList = DI.getNewRoomsList();

        if(!(mRoomsPerHourList.isEmpty()))
            mRoomsPerHourList.clear();

        for (int position = 0; position < (hoursList.size()); position++) {
            roomsPerHour = new RoomsPerHour(hoursList.get(position), new ArrayList<>(roomsList));

            mRoomsPerHourList.add(roomsPerHour);
        }
    }

    public static ArrayList<RoomsPerHour> getRoomsPerHourList(){
        return mRoomsPerHourList;
    }

    public static void updateAvailableHours(ArrayList<RoomsPerHour> availableHoursAndRooms) {
        mRoomsPerHourList = availableHoursAndRooms;

        // delete hour availability if all the rooms are taken
        for(int position = 0; position != mRoomsPerHourList.size(); position++) {
            if (mRoomsPerHourList.get(position).getRooms().isEmpty()){
                mRoomsPerHourList.remove(position);
                break;
            }
        }
    }
}


