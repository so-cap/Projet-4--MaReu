package com.sophie.mareu;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.service.RoomsAvailabilityService;
import com.sophie.mareu.controller.RoomsPerHour;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class MeetingCreationEndTests {
    private ArrayList<RoomsPerHour> mRoomsPerHour = new ArrayList<>();

    // for expected data
    private ArrayList<String> mHoursList = DI.getNewHoursList();
    private ArrayList<String> mRoomsList = DI.getNewRoomsList();

    @Before
    public void setup() {
        RoomsAvailabilityService.initRoomsAndHours();
    }

    @Test
    public void deleteRoomFromListWithSuccess(){
        mRoomsPerHour = RoomsAvailabilityService.getRoomsPerHourList();

        mRoomsPerHour.get(3).getRooms().remove(3);
        assertThat(mRoomsPerHour.get(3).getRooms(), not(hasItem("BOWSER")));

        // check that the Room is still available at another hour
        assertThat(mRoomsPerHour.get(1).getRooms(), hasItem("BOWSER"));
    }


}