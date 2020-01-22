package com.sophie.mareu.DI;

import com.sophie.mareu.controller.MeetingsController;
import com.sophie.mareu.controller.RoomsAvailabilityController;
import com.sophie.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by SOPHIE on 31/12/2019.
 */
public class DI {
    private static MeetingsController meetingsController = new MeetingsController();
    private static List<Meeting> dummyMeetings = DummyMeetingsGenerator.getDummyMeetings();

    public static List<Meeting> getDummyMeetings(){
        return dummyMeetings;
    }

    public static RoomsAvailabilityController getNewRoomsAvailabilityController(){
        return new RoomsAvailabilityController();
    }

    public static MeetingsController getMeetingsController(){
        return meetingsController;
    }


    // For testing
    public static MeetingsController getNewMeetingsController(){
        return new MeetingsController();
    }

    public static ArrayList<String> getDummyHoursList(){
        return new ArrayList<>(Arrays.asList("8h00", "9h00", "10h00", "11h00", "12h00"));
    }

     public static ArrayList<String> getDummyRoomsList(){
        return new ArrayList<>(Arrays.asList("Peach", "Mario", "Bowser","Luigi", "Waluigi", "Yoshi"));
    }
}
