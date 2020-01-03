package com.sophie.mareu.service;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.controller.AvailabilityPerHour;

import java.util.ArrayList;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class RoomsAvailability {
    private static ArrayList<AvailabilityPerHour> mAvailableRoomsPerHour = new ArrayList<>();

    public static void initRoomsAndHours(){
        // use tags for each hour view (get size of array, then loop to setTag on button.
        AvailabilityPerHour availabilityPerHour;
        ArrayList<String> hoursList = DI.getNewHoursList();
        ArrayList<String> roomsList = DI.getNewRoomsList();

        if(!(mAvailableRoomsPerHour.isEmpty()))
            mAvailableRoomsPerHour.clear();

        for (int position = 0; position < (hoursList.size()); position++) {
            availabilityPerHour = new AvailabilityPerHour(hoursList.get(position), new ArrayList<>(roomsList));
            mAvailableRoomsPerHour.add(availabilityPerHour);
        }
    }

    public static ArrayList<AvailabilityPerHour> getAvailableRoomsPerHour(){
        return mAvailableRoomsPerHour;
    }

    public static void updateAvailableHours(ArrayList<AvailabilityPerHour> availableHoursAndRooms) {
        mAvailableRoomsPerHour = availableHoursAndRooms;

        // delete hour availability if all the rooms are taken
        for(int position = 0; position != mAvailableRoomsPerHour.size(); position++) {
            if (mAvailableRoomsPerHour.get(position).getRooms().isEmpty()){
                mAvailableRoomsPerHour.remove(position);
                break;
            }
        }
    }
}


