package com.sophie.mareu;

import com.sophie.mareu.di.DI;
import com.sophie.mareu.helper.MeetingsHandler;
import com.sophie.mareu.helper.RoomsAvailability;
import com.sophie.mareu.model.RoomsPerHour;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class RoomsAvailabilityTests {
    private ArrayList<RoomsPerHour> roomsPerHour = new ArrayList<>();
    private RoomsAvailability roomsAvailability;
    private MeetingsHandler meetingsHandler = DI.getNewMeetingsHandler();
    // for expected data
    private ArrayList<String> hours = DI.getDummyHoursList();
    private ArrayList<String> rooms = DI.getDummyRoomsList();

    @Before
    public void setup(){
        meetingsHandler.setHoursAndRooms(hours, rooms);
        roomsAvailability = new RoomsAvailability();
    }

    @Test
    public void initRoomsAvailabilityWithSuccess(){
        roomsAvailability.initRoomsPerHourList(hours, rooms);
        roomsPerHour = roomsAvailability.getRoomsPerHourList();

        assertEquals(roomsPerHour.get(0).getHour().getValue(), hours.get(0));
        assertEquals(roomsPerHour.get(4).getHour().getValue(), hours.get(4));

        assertEquals(roomsPerHour.get(0).getRooms(), rooms);
        assertEquals(roomsPerHour.get(4).getRooms(), rooms);
    }

    @Test
    public void updateHoursAvailabilityWithSuccess(){
        roomsAvailability.initRoomsPerHourList(hours, rooms);
        roomsPerHour = roomsAvailability.getRoomsPerHourList();

        // Index 1 = "10h00" beforehand
        assertEquals(roomsPerHour.get(1).getHour().getValue(),"9h00");

        //deleting all the rooms at index 1
        while(roomsPerHour.get(1).getRooms().size() > 0){
            roomsPerHour.get(1).getRooms().remove(0);
        }

        // Updating list
        roomsAvailability.updateAvailableHoursAndRooms(roomsPerHour);

        //Index 1 = "11h00" after removing hour availability
        assertEquals(roomsPerHour.get(1).getHour().getValue(), "10h00");
    }

}
