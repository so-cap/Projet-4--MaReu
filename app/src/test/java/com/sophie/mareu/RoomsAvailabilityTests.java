package com.sophie.mareu;


import com.sophie.mareu.DI.DI;
import com.sophie.mareu.RoomsAvailability.AvailabilityPerHour;

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
    private ArrayList<AvailabilityPerHour> mRoomsPerHour = new ArrayList<>();

    // for expected data
    private ArrayList<Integer>  mHoursList = DI.getNewHoursList();
    private ArrayList<String> mRoomsList = DI.getNewRoomsList();

    @Before
    public void setup(){
        mRoomsAvailability.initRoomsAndHours();
    }

    @Test
    public void getRoomsAndHoursAvailabilityWithSuccess(){
        mRoomsPerHour = mRoomsAvailability.getAvailableRoomsPerHour();
        assertThat(mRoomsPerHour.get(0).getHour(), equalTo(mHoursList.get(0)));
        assertThat(mRoomsPerHour.get(11).getHour(), equalTo(mHoursList.get(11)));

        assertThat(mRoomsPerHour.get(0).getRooms(), equalTo(mRoomsList));
        assertThat(mRoomsPerHour.get(3).getRooms(), equalTo(mRoomsList));
    }

    @Test
    public void deletingARoomWithSuccess(){
        mRoomsPerHour = mRoomsAvailability.getAvailableRoomsPerHour();

        mRoomsPerHour.get(3).getRooms().remove(3);
        assertThat(mRoomsPerHour.get(3).getRooms(), not(hasItem("BOWSER")));
    }

    @Test
    public void updateHoursAvailabilityWithSuccess(){
        mRoomsPerHour = mRoomsAvailability.getAvailableRoomsPerHour();

        // Index 2 = 10 beforehand
        assertThat(mRoomsPerHour.get(2).getHour(), equalTo(10));


        //deleting all the rooms at index 2
        while(mRoomsPerHour.get(2).getRooms().size() > 0){
            mRoomsPerHour.get(2).getRooms().remove(0);
        }

        // Updating list
        mRoomsAvailability.setAvailableRoomsPerHours(mRoomsPerHour);

        //Index 2 = 11 after removing hour availability
        assertThat(mRoomsPerHour.get(2).getHour(), equalTo(11));
    }

}
