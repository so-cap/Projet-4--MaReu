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
public class RoomsAvailabilityServiceTests {
    private ArrayList<RoomsPerHour> mRoomsPerHour = new ArrayList<>();

    // for expected data
    private ArrayList<String>  mHoursList = DI.getNewHoursList();
    private ArrayList<String> mRoomsList = DI.getNewRoomsList();

    @Before
    public void setup(){
        RoomsAvailabilityService.initRoomsAndHours();
    }

    @Test
    public void getHoursAndRoomsAvailabilityWithSuccess(){
        mRoomsPerHour = RoomsAvailabilityService.getRoomsPerHourList();
        assertThat(mRoomsPerHour.get(0).getHour().getValue(), equalTo(mHoursList.get(0)));
        assertThat(mRoomsPerHour.get(11).getHour().getValue(), equalTo(mHoursList.get(11)));

        assertThat(mRoomsPerHour.get(0).getRooms(), equalTo(mRoomsList));
        assertThat(mRoomsPerHour.get(3).getRooms(), equalTo(mRoomsList));
    }

    @Test
    public void updateHoursAvailabilityWithSuccess(){
        mRoomsPerHour = RoomsAvailabilityService.getRoomsPerHourList();

        // Index 2 = "10h00" beforehand
        assertThat(mRoomsPerHour.get(2).getHour().getValue(), equalTo("10h00"));


        //deleting all the rooms at index 2
        while(mRoomsPerHour.get(2).getRooms().size() > 0){
            mRoomsPerHour.get(2).getRooms().remove(0);
        }

        // Updating list
        RoomsAvailabilityService.updateAvailableHours(mRoomsPerHour);

        //Index 2 = "11h00" after removing hour availability
        assertThat(mRoomsPerHour.get(2).getHour().getValue(), equalTo("11h00"));
    }

}
