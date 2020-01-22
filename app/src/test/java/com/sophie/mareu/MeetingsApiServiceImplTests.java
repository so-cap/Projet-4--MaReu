package com.sophie.mareu;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.model.RoomsPerHour;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.service.MeetingsApiServiceImpl;
import com.sophie.mareu.service.RoomsAvailabilityServiceImpl;
import com.sophie.mareu.service.RoomsAvailabilityApiService;

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
public class MeetingsApiServiceImplTests {
    private Date date = new Date();
    private Date differentDate = new Date();
    private Meeting meeting, differentMeeting;
    private RoomsAvailabilityApiService mService;

    @Before
    public void setup(){
        MeetingsApiServiceImpl.clearAllMeetings();
        mService = new RoomsAvailabilityServiceImpl();
        List<Meeting> meetings = DI.getDummyMeetings();

        meeting = meetings.get(0);
        date = meeting.getDate();

        differentMeeting = meetings.get(1);
        differentDate = differentMeeting.getDate();

    }

    @Test
    public void addMeetingWithSuccess(){
        MeetingsApiServiceImpl.addMeeting(meeting, date);
        MeetingsApiServiceImpl.addMeeting(differentMeeting, differentDate);
        assertThat(MeetingsApiServiceImpl.getMeetings().size(),equalTo(2));

        MeetingsApiServiceImpl.clearAllMeetings();
    }

    @Test
    public void getMeetingsWithSuccess(){
        assertTrue(MeetingsApiServiceImpl.getMeetingsByDate(differentDate).isEmpty());

        MeetingsApiServiceImpl.addMeeting(meeting, date);
        assertTrue(MeetingsApiServiceImpl.getMeetingsByDate(date).contains(meeting));
        assertThat(MeetingsApiServiceImpl.getMeetings().size(),equalTo(1));
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
        MeetingsApiServiceImpl.addMeeting(meeting, date);
        MeetingsApiServiceImpl.updateAvailabilityByDate(date, mService);
        assertThat(MeetingsApiServiceImpl.getMeetings(), hasItem(meeting));

        MeetingsApiServiceImpl.deleteMeeting(meeting);
        assertThat(MeetingsApiServiceImpl.getMeetings(), not(hasItem(meeting)));
    }

    @Test
    public void deleteAllMeetingsAndServicesWithSuccess(){
        MeetingsApiServiceImpl.addMeeting(meeting, date);
        MeetingsApiServiceImpl.updateAvailabilityByDate(date, mService);

        MeetingsApiServiceImpl.addMeeting(differentMeeting, differentDate);
        MeetingsApiServiceImpl.updateAvailabilityByDate(differentDate, mService);

        assertThat(MeetingsApiServiceImpl.mMeetingsByDate.size(), equalTo(2));
        assertThat(MeetingsApiServiceImpl.serviceByDate.size(), equalTo(2));

        MeetingsApiServiceImpl.clearAllMeetings();
        assertThat(MeetingsApiServiceImpl.mMeetingsByDate.size(), equalTo(0));
        assertThat(MeetingsApiServiceImpl.serviceByDate.size(), equalTo(0));
    }

}
