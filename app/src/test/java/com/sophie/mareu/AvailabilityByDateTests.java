package com.sophie.mareu;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.controller.RoomsPerHour;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.controller.AvailabilityByDate;
import com.sophie.mareu.service.RoomsAvailabilityByHourImpl;
import com.sophie.mareu.service.RoomsAvailabilityService;

import org.junit.Assert;
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
public class AvailabilityByDateTests {
    private Date date = new Date();
    private Date differentDate = new Date();
    private Meeting meeting, differentMeeting;
    private RoomsAvailabilityService mService;

    @Before
    public void setup(){
        AvailabilityByDate.clearAllMeetings();
        mService = new RoomsAvailabilityByHourImpl();
        List<Meeting> meetings = DI.getDummyMeetings();

        meeting = meetings.get(0);
        date = meeting.getDate();

        differentMeeting = meetings.get(1);
        differentDate = differentMeeting.getDate();

    }

    @Test
    public void addMeetingWithSuccess(){
        AvailabilityByDate.addMeeting(meeting, date);
        AvailabilityByDate.addMeeting(differentMeeting, differentDate);
        assertThat(AvailabilityByDate.getMeetings().size(),equalTo(2));

        AvailabilityByDate.clearAllMeetings();
    }

    @Test
    public void getMeetingsWithSuccess(){
        assertTrue(AvailabilityByDate.getMeetingsByDate(differentDate).isEmpty());

        AvailabilityByDate.addMeeting(meeting, date);
        assertTrue(AvailabilityByDate.getMeetingsByDate(date).contains(meeting));
        assertThat(AvailabilityByDate.getMeetings().size(),equalTo(1));
    }

    @Test
    public void deleteRoomFromListWithSuccess(){
        ArrayList<RoomsPerHour> roomsPerHour = mService.getRoomsPerHourList();
        roomsPerHour.get(3).getRooms().remove(3);
        Assert.assertThat(roomsPerHour.get(3).getRooms(), not(hasItem("Bowser")));

        // check that the room is still available at another hour
        Assert.assertThat(roomsPerHour.get(1).getRooms(), hasItem("Bowser"));
    }

    @Test
    public void deleteMeetingWithSuccess(){
        AvailabilityByDate.addMeeting(meeting, date);
        AvailabilityByDate.updateAvailabilityByDate(date, mService);
        assertThat(AvailabilityByDate.getMeetings(), hasItem(meeting));

        AvailabilityByDate.deleteMeeting(meeting);
        assertThat(AvailabilityByDate.getMeetings(), not(hasItem(meeting)));
    }

    @Test
    public void deleteAllMeetingsAndServicesWithSuccess(){
        AvailabilityByDate.addMeeting(meeting, date);
        AvailabilityByDate.updateAvailabilityByDate(date, mService);

        AvailabilityByDate.addMeeting(differentMeeting, differentDate);
        AvailabilityByDate.updateAvailabilityByDate(differentDate, mService);

        assertThat(AvailabilityByDate.mMeetingsByDate.size(), equalTo(2));
        assertThat(AvailabilityByDate.serviceByDate.size(), equalTo(2));

        AvailabilityByDate.clearAllMeetings();
        assertThat(AvailabilityByDate.mMeetingsByDate.size(), equalTo(0));
        assertThat(AvailabilityByDate.serviceByDate.size(), equalTo(0));
    }

}
