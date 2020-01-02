package com.sophie.mareu;

import android.widget.ScrollView;

import com.sophie.mareu.DI.DI;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class RoomsAvailability {
     private ArrayList<String> mHoursList;
     private ArrayList<String> mRoomsList;
     private static ArrayList<AvailabilityPerHour> mAvailableRoomsPerHour = new ArrayList<>();

    public class AvailabilityPerHour{
        String hour;
        ArrayList<String> rooms;

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

    public void initRoomsAndHours(){
        // use tags for each hour view (get size of array, then loop to setTag on button.
        AvailabilityPerHour availabilityPerHour;
        mHoursList = DI.getNewHoursList();
        mRoomsList = DI.getNewRoomsList();

        for(int position = 0; position < (mHoursList.size()); position++) {
            availabilityPerHour = new AvailabilityPerHour(mHoursList.get(position),new ArrayList<>(mRoomsList));
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
            if (mAvailableRoomsPerHour.get(position).getRooms().isEmpty())
                mAvailableRoomsPerHour.remove(position);
        }
    }
}


