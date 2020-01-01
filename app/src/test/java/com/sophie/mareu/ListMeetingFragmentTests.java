package com.sophie.mareu;

import com.sophie.mareu.RoomsAvailability.AvailabilityPerHour;
import com.sophie.mareu.model.Meeting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class ListMeetingFragmentTests {
    private Meeting mMeeting = new Meeting();
    private RoomsAvailability mRoomsAvailability = new RoomsAvailability();
    private ArrayList<AvailabilityPerHour> mRoomsPerHour = new ArrayList<>();
    private ArrayList<String> mParticipantsList = new ArrayList<>(Arrays.asList("pop@gmail.com", "kikou@swag.com", "yessai@email.fr"));

    @Before
    public void setup(){
        mRoomsAvailability.initRoomsAndHours();
        mRoomsPerHour = RoomsAvailability.getAvailableRoomsPerHour();
        mMeeting.setRoomName(mRoomsPerHour.get(0).getRooms().get(4));
        mMeeting.setHour("14h00");
        mMeeting.setParticipants(mParticipantsList);
    }

    @Test
    public void getStringFromParticipantsListWithSuccess(){
            mMeeting.getParticipants();
            assertThat(mMeeting.getParticipants(), equalTo("pop@gmail.com, kikou@swag.com, yessai@email.fr"));
    }
}
