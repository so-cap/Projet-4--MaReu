package com.sophie.mareu;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class RoomsAvailabilityTests {
    private RoomsAvailability mRoomsAvailability = new RoomsAvailability();
    private ArrayList<RoomsAvailability.AvailabilityPerHour> roomsPerHour = new ArrayList<>();

    // for expected data
    private ArrayList<Integer>  mHoursList = new ArrayList<>(Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19));
    private ArrayList<String> mRoomsList = new ArrayList<>(Arrays.asList("PEACH","LUIGI","MARIO","BOWSER","WALUIGI","DAISY","WARIO","ROSALINA","TOAD","YOSHI"));

    @Before
    public void setup(){
        mRoomsAvailability.initRoomsAndHours();
    }

    @Test
    public void getRoomsAndHoursAvailabilityWithSuccess(){
        roomsPerHour = mRoomsAvailability.getAvailableRoomsPerHour();
        assertThat(roomsPerHour.get(0).getHour(), equalTo(mHoursList.get(0)));
        assertThat(roomsPerHour.get(11).getHour(), equalTo(mHoursList.get(11)));

        assertThat(roomsPerHour.get(0).getRooms(), equalTo(mRoomsList));
        assertThat(roomsPerHour.get(3).getRooms(), equalTo(mRoomsList));
    }

    @Test
    public void deletingARoomWithSuccess(){
        roomsPerHour = mRoomsAvailability.getAvailableRoomsPerHour();

        roomsPerHour.get(3).getRooms().remove(3);
        assertThat(roomsPerHour.get(3).getRooms(), not(hasItem("BOWSER")));
    }

    @Test
    public void updateHoursAvailabilityWithSuccess(){
        roomsPerHour = mRoomsAvailability.getAvailableRoomsPerHour();

        // Index 2 = 10 beforehand
        assertThat(roomsPerHour.get(2).getHour(), equalTo(10));


        //deleting all the rooms at index 2
        while(roomsPerHour.get(2).getRooms().size() > 0){
            System.out.println(roomsPerHour.get(1).getRooms().size());
            roomsPerHour.get(2).getRooms().remove(0);
        }

        // Updating list
        mRoomsAvailability.setAvailableRoomsPerHours(roomsPerHour);

        //Index 2 = 11 after removing hour availability
        assertThat(roomsPerHour.get(2).getHour(), equalTo(11));

    }

}
