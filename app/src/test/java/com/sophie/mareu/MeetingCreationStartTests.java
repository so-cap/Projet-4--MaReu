package com.sophie.mareu;


import com.sophie.mareu.DI.DI;
import com.sophie.mareu.service.RoomsAvailability;
import com.sophie.mareu.ui.meeting_creation.MeetingCreationStartFragment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class MeetingCreationStartTests {
    private MeetingCreationStartFragment mMeetingCreationStartFragment = new MeetingCreationStartFragment();
    private RoomsAvailability mRoomsAvailability = new RoomsAvailability();

    private ArrayList<String>  mHoursList = DI.getNewHoursList();
    private ArrayList<String> mRoomsList = new ArrayList<>(Arrays.asList("8h00", "9h00", "10h00", "19h00"));

    @Test
    public void initSpinnerWithSuccess(){
        String resultStart, resultEnd, resultFalse;
        mRoomsAvailability.initRoomsAndHours();
        mMeetingCreationStartFragment.initSpinner();

        //value at the start of the list and at the end.
        resultStart = mMeetingCreationStartFragment.getSpinnerArray().get(0);
        resultEnd = mMeetingCreationStartFragment.getSpinnerArray().get(11);

        assertThat(resultStart, equalTo(mRoomsList.get(0)));
        assertThat(resultEnd,equalTo(mRoomsList.get(3)));
    }

}
