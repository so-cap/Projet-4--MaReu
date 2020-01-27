package com.sophie.mareu;

import com.sophie.mareu.di.DI;
import com.sophie.mareu.helper.MeetingsHandler;
import com.sophie.mareu.helper.RoomsAvailability;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.model.RoomsPerHour;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertTrue;

/**
 * Created by SOPHIE on 04/01/2020.
 */
@RunWith(JUnit4.class)
public class MeetingsHandlerTests {
    private Date date = new Date();
    private Date differentDate = new Date();
    private Meeting meeting, differentMeeting;
    private RoomsAvailability roomsAvailability;
    private MeetingsHandler meetingsHandler = DI.getNewMeetingsHandler();
    private ArrayList<String> hours = DI.getDummyHoursList();
    private ArrayList<String> rooms = DI.getDummyRoomsList();

    @Before
    public void setup(){
        RoomsPerHour.setMeetingsHandler(meetingsHandler);
        meetingsHandler.setHoursAndRooms(hours, rooms);
        roomsAvailability = new RoomsAvailability();
        roomsAvailability.initRoomsPerHourList(hours,rooms);
        List<Meeting> meetings = DI.getDummyMeetings();

        meeting = meetings.get(0);
        date = meeting.getDate();

        differentMeeting = meetings.get(1);
        differentDate = differentMeeting.getDate();
    }

    @Test
    public void addMeetingWithSuccess(){
        meetingsHandler.addMeeting(meeting, date);
        meetingsHandler.addMeeting(differentMeeting, differentDate);
        assertThat(meetingsHandler.getMeetings().size(),equalTo(2));
    }

    @Test
    public void getMeetingsWithSuccess(){
        assertTrue(meetingsHandler.getMeetingsByDate(date).isEmpty());

        meetingsHandler.addMeeting(meeting, date);
        assertTrue(meetingsHandler.getMeetingsByDate(date).contains(meeting));
        assertThat(meetingsHandler.getMeetings().size(),equalTo(1));
    }

    @Test
    public void deleteRoomFromListWithSuccess(){
        ArrayList<RoomsPerHour> roomsPerHour = roomsAvailability.getRoomsPerHourList();
        roomsPerHour.get(2).getRooms().remove(2);
        assertThat(roomsPerHour.get(2).getRooms(), not(hasItem("Bowser")));
        // check that the room is still available at another hour
        assertThat(roomsPerHour.get(1).getRooms(), hasItem("Bowser"));
    }

    @Test
    public void deleteMeetingWithSuccess(){
        meetingsHandler.addMeeting(meeting, date);
        meetingsHandler.updateAvailabilityByDate(date, roomsAvailability);
        assertThat(meetingsHandler.getMeetings(), hasItem(meeting));

        meetingsHandler.deleteMeeting(meeting);
        assertThat(meetingsHandler.getMeetings(), not(hasItem(meeting)));
    }

    @Test
    public void clearAllMeetingsAndHandlersWithSuccess(){
        meetingsHandler.addMeeting(meeting, date);
        meetingsHandler.updateAvailabilityByDate(date, roomsAvailability);

        meetingsHandler.addMeeting(differentMeeting, differentDate);
        meetingsHandler.updateAvailabilityByDate(differentDate, roomsAvailability);

        assertThat(meetingsHandler.meetingsByDate.size(), equalTo(2));
        assertThat(meetingsHandler.roomsAvailabilityByDate.size(), equalTo(2));

        meetingsHandler.clearAllMeetings();
        assertThat(meetingsHandler.meetingsByDate.size(), equalTo(0));
        assertThat(meetingsHandler.roomsAvailabilityByDate.size(), equalTo(0));
    }

}
