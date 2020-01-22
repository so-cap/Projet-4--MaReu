package com.sophie.mareu.DI;

import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.controller.service.MeetingsController;
import com.sophie.mareu.controller.service.RoomsAvailabilityServiceImpl;
import com.sophie.mareu.controller.service.RoomsAvailabilityApiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by SOPHIE on 31/12/2019.
 */
public class DI {
    private static RoomsAvailabilityApiService roomsAvailabilityService = new RoomsAvailabilityServiceImpl();
    private static MeetingsController meetingController = new MeetingsController();
    private static List<Meeting> dummyMeetings = DummyMeetingsGenerator.getDummyMeetings();

    public static List<Meeting> getDummyMeetings(){
        return dummyMeetings;
    }

    public static RoomsAvailabilityApiService getNewRoomsAvailabilityService(){
        return new RoomsAvailabilityServiceImpl();
    }

    public static RoomsAvailabilityApiService getRoomsAvailabilityService(){
        return roomsAvailabilityService;
    }

    public static MeetingsController getMeetingsController(){
        return meetingController;
    }

    public static MeetingsController getNewMeetingsController(){
        return new MeetingsController();
    }

    // For testing
    public static ArrayList<String> getDummyHoursList(){
        return new ArrayList<>(Arrays.asList("8h00", "9h00", "10h00", "11h00", "12h00"));
    }

     public static ArrayList<String> getDummyRoomsList(){
        return new ArrayList<>(Arrays.asList("Peach", "Mario", "Bowser","Luigi", "Waluigi", "Yoshi"));
    }
}
