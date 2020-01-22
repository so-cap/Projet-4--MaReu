package com.sophie.mareu;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.controller.MeetingsController;
import com.sophie.mareu.controller.RoomsAvailabilityController;
import com.sophie.mareu.model.RoomsPerHour;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class RoomsAvailabilityControllerTests {
    private ArrayList<RoomsPerHour> roomsPerHour = new ArrayList<>();
    private RoomsAvailabilityController roomsController;
    private MeetingsController meetingsController = DI.getNewMeetingsController();

    // for expected data
    private ArrayList<String> hours = DI.getDummyHoursList();
    private ArrayList<String> rooms = DI.getDummyRoomsList();

    @Before
    public void setup(){
        meetingsController.setHoursAndRooms(hours, rooms);
        roomsController = new RoomsAvailabilityController();
    }

    @Test
    public void initRoomsAvailabilityWithSuccess(){
        roomsController.initRoomsPerHourList(hours, rooms);
        roomsPerHour = roomsController.getRoomsPerHourList();

        assertEquals(roomsPerHour.get(0).getHour().getValue(), hours.get(0));
        assertEquals(roomsPerHour.get(4).getHour().getValue(), hours.get(4));

        assertEquals(roomsPerHour.get(0).getRooms(), rooms);
        assertEquals(roomsPerHour.get(4).getRooms(), rooms);
    }

    @Test
    public void updateHoursAvailabilityWithSuccess(){
        roomsController.initRoomsPerHourList(hours, rooms);
        roomsPerHour = roomsController.getRoomsPerHourList();

        // Index 1 = "10h00" beforehand
        assertEquals(roomsPerHour.get(1).getHour().getValue(),"9h00");

        //deleting all the rooms at index 1
        while(roomsPerHour.get(1).getRooms().size() > 0){
            roomsPerHour.get(1).getRooms().remove(0);
        }

        // Updating list
        roomsController.updateAvailableHours(roomsPerHour);

        //Index 1 = "11h00" after removing hour availability
        assertEquals(roomsPerHour.get(1).getHour().getValue(), "10h00");
    }

}
