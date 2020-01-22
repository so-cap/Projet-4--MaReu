package com.sophie.mareu;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.controller.service.MeetingsController;
import com.sophie.mareu.controller.service.RoomsAvailabilityServiceImpl;
import com.sophie.mareu.model.RoomsPerHour;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class RoomsAvailabilityApiServiceTests {
    private ArrayList<RoomsPerHour> roomsPerHour = new ArrayList<>();
    private RoomsAvailabilityServiceImpl roomsAvailability;
    private MeetingsController meetingsController = DI.getNewMeetingsController();

    // for expected data
    private ArrayList<String> hours = DI.getDummyHoursList();
    private ArrayList<String> rooms = DI.getDummyRoomsList();

    @Before
    public void setup(){
        meetingsController.setHoursAndRooms(hours, rooms);
        roomsAvailability = new RoomsAvailabilityServiceImpl();
    }

    @Test
    public void initRoomsAvailabilityWithSuccess(){
        roomsAvailability.initRoomsPerHourList(hours, rooms);
        roomsPerHour = roomsAvailability.getRoomsPerHourList();

        assertThat(roomsPerHour.get(0).getHour().getValue(), equalTo(hours.get(0)));
        assertThat(roomsPerHour.get(4).getHour().getValue(), equalTo(hours.get(4)));

        assertThat(roomsPerHour.get(0).getRooms(), equalTo(rooms));
        assertThat(roomsPerHour.get(4).getRooms(), equalTo(rooms));
    }

    @Test
    public void updateHoursAvailabilityWithSuccess(){
        roomsAvailability.initRoomsPerHourList(hours, rooms);
        roomsPerHour = roomsAvailability.getRoomsPerHourList();

        // Index 1 = "10h00" beforehand
        assertThat(roomsPerHour.get(1).getHour().getValue(), equalTo("9h00"));

        //deleting all the rooms at index 1
        while(roomsPerHour.get(1).getRooms().size() > 0){
            roomsPerHour.get(1).getRooms().remove(0);
        }

        // Updating list
        roomsAvailability.updateAvailableHours(roomsPerHour);

        //Index 1 = "11h00" after removing hour availability
        assertThat(roomsPerHour.get(1).getHour().getValue(), equalTo("10h00"));
    }

}
