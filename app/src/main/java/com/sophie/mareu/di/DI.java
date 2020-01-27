package com.sophie.mareu.di;

import com.sophie.mareu.helper.MeetingsHandler;
import com.sophie.mareu.helper.RoomsAvailability;
import com.sophie.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by SOPHIE on 31/12/2019.
 */
public class DI {
    private static MeetingsHandler meetingsHandler = new MeetingsHandler();
    private static List<Meeting> dummyMeetings = DummyMeetingsGenerator.getDummyMeetings();

    public static List<Meeting> getDummyMeetings(){
        return dummyMeetings;
    }

    public static RoomsAvailability getNewRoomsAvailability(){
        return new RoomsAvailability();
    }

    public static MeetingsHandler getMeetingsHandler(){
        return meetingsHandler;
    }

    // For testing
    public static MeetingsHandler getNewMeetingsHandler(){
        return new MeetingsHandler();
    }

    public static ArrayList<String> getDummyHoursList(){
        return new ArrayList<>(Arrays.asList("8h00", "9h00", "10h00", "11h00", "12h00"));
    }

     public static ArrayList<String> getDummyRoomsList(){
        return new ArrayList<>(Arrays.asList("Peach", "Mario", "Bowser","Luigi", "Waluigi", "Yoshi"));
    }

    // To be able to do tests correctly no matter the hour of running the test.
    public static Date getTodaysDateWithoutTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
