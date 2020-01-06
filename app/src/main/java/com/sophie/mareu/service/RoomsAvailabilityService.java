package com.sophie.mareu.service;

import android.util.Log;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.controller.RoomsPerHour;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class RoomsAvailabilityService implements Serializable {
    private ArrayList<RoomsPerHour> mRoomsPerHourList = new ArrayList<>();


    public RoomsAvailabilityService(){
        initRoomsAndHours();
    }

    public void initRoomsAndHours(){
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

    public ArrayList<RoomsPerHour> getRoomsPerHourList(){
        return mRoomsPerHourList;
    }

    private static final String TAG = "LOGGRoomsAvailability";
    public void updateAvailableHours(ArrayList<RoomsPerHour> availableHoursAndRooms) {
        mRoomsPerHourList = availableHoursAndRooms;

        Log.d(TAG, "updateAvailableHours: " + availableHoursAndRooms.size());

        // delete hour availability if all the rooms are taken
        for(int position = 0; position < mRoomsPerHourList.size(); position++) {
            if (mRoomsPerHourList.get(position).getRooms().isEmpty()){
                mRoomsPerHourList.remove(position);
                break;
            }
        }
    }
}


