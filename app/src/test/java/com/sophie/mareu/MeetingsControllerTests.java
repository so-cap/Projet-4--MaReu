package com.sophie.mareu;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.controller.MeetingsController;
import com.sophie.mareu.controller.RoomsAvailabilityController;
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
public class MeetingsControllerTests {
    private Date date = new Date();
    private Date differentDate = new Date();
    private Meeting meeting, differentMeeting;
    private RoomsAvailabilityController roomsController;
    private MeetingsController meetingsController = DI.getMeetingsController();
    private ArrayList<String> hours = DI.getDummyHoursList();
    private ArrayList<String> rooms = DI.getDummyRoomsList();

    @Before
    public void setup(){
        meetingsController.clearAllMeetings();
        meetingsController.setHoursAndRooms(hours, rooms);

        roomsController = new RoomsAvailabilityController();
        roomsController.initRoomsPerHourList(hours,rooms);
        List<Meeting> meetings = DI.getDummyMeetings();

        meeting = meetings.get(0);
        date = meeting.getDate();

        differentMeeting = meetings.get(1);
        differentDate = differentMeeting.getDate();

    }

    @Test
    public void addMeetingWithSuccess(){
        meetingsController.addMeeting(meeting, date);
        meetingsController.addMeeting(differentMeeting, differentDate);
        assertThat(meetingsController.getMeetings().size(),equalTo(2));

        meetingsController.clearAllMeetings();
    }

    @Test
    public void getMeetingsWithSuccess(){
        assertTrue(meetingsController.getMeetingsByDate(differentDate).isEmpty());

        meetingsController.addMeeting(meeting, date);
        assertTrue(meetingsController.getMeetingsByDate(date).contains(meeting));
        assertThat(meetingsController.getMeetings().size(),equalTo(1));
    }

    @Test
    public void deleteRoomFromListWithSuccess(){
        ArrayList<RoomsPerHour> roomsPerHour = roomsController.getRoomsPerHourList();
        roomsPerHour.get(2).getRooms().remove(2);
        assertThat(roomsPerHour.get(2).getRooms(), not(hasItem("Bowser")));

        // check that the room is still available at another hour
        assertThat(roomsPerHour.get(1).getRooms(), hasItem("Bowser"));
    }

    @Test
    public void deleteMeetingWithSuccess(){
        meetingsController.addMeeting(meeting, date);
        meetingsController.updateAvailabilityByDate(date, roomsController);
        assertThat(meetingsController.getMeetings(), hasItem(meeting));

        meetingsController.deleteMeeting(meeting);
        assertThat(meetingsController.getMeetings(), not(hasItem(meeting)));
    }

    @Test
    public void deleteAllMeetingsAndServicesWithSuccess(){
        meetingsController.addMeeting(meeting, date);
        meetingsController.updateAvailabilityByDate(date, roomsController);

        meetingsController.addMeeting(differentMeeting, differentDate);
        meetingsController.updateAvailabilityByDate(differentDate, roomsController);

        assertThat(meetingsController.meetingsByDate.size(), equalTo(2));
        assertThat(meetingsController.serviceByDate.size(), equalTo(2));

        meetingsController.clearAllMeetings();
        assertThat(meetingsController.meetingsByDate.size(), equalTo(0));
        assertThat(meetingsController.serviceByDate.size(), equalTo(0));
    }

}
