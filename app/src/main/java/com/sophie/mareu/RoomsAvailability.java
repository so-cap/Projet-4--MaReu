package com.sophie.mareu;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class RoomsAvailability {
     private ArrayList<Integer> mHoursList;
     private ArrayList<String> mRoomsList;
     private ArrayList<AvailabilityPerHour> mAvailableRoomsPerHour = new ArrayList<>();

    @Override
    public String toString() {
        return "RoomsAvailability{" +
                "mAvailableRoomsPerHour=" + mAvailableRoomsPerHour +
                '}';
    }

    public class AvailabilityPerHour{
        Integer hour;
        ArrayList<String> rooms;

        public void setHour(Integer hour) {
            this.hour = hour;
        }

        public void setRooms(ArrayList<String> rooms) {
            this.rooms = rooms;
        }

        public Integer getHour(){
            return hour;
        }

        public ArrayList<String> getRooms(){
            return rooms;
        }
    }

    // call when opening activity (onCreate)
    public void initRoomsAndHours(){
        // use tags for each hour view (get size of array, then loop to setTag on button.
        mHoursList = new ArrayList<>(Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19));
        mRoomsList = new ArrayList<>(Arrays.asList("PEACH","LUIGI","MARIO","BOWSER","WALUIGI","DAISY","WARIO","ROSALINA","TOAD","YOSHI"));

        for(int position = 0; position < (mHoursList.size()); position++) {
            AvailabilityPerHour availabilityPerHour = new AvailabilityPerHour();
            availabilityPerHour.setHour(mHoursList.get(position));
            availabilityPerHour.setRooms(new ArrayList<>(mRoomsList));
            mAvailableRoomsPerHour.add(availabilityPerHour);
        }
    }

    //display with a loop hours : getAvailableRoomsPerHour.get(position)
    public ArrayList<AvailabilityPerHour> getAvailableRoomsPerHour(){
        return mAvailableRoomsPerHour;
    }

    // call before closing last MeetingCreation fragment
    public void setAvailableRoomsPerHours(ArrayList<AvailabilityPerHour> availableHoursAndRooms) {
        mAvailableRoomsPerHour = availableHoursAndRooms;

        // delete hour availability if all the rooms are taken
        for(int position = 0; position != mAvailableRoomsPerHour.size(); position++) {
            if (mAvailableRoomsPerHour.get(position).getRooms().isEmpty())
                mAvailableRoomsPerHour.remove(position);
        }
    }
}


