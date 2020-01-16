package com.sophie.mareu;


import com.sophie.mareu.DI.DI;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.ui.meeting_creation.MeetingCreationStartFragment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class UiFocusedUnitTests {
    private MeetingCreationStartFragment mMeetingCreationStartFragment = new MeetingCreationStartFragment();
    private ArrayList<String> initialHoursList = DI.getNewHoursList();
    private List<Meeting> dummyMeetings = DI.getDummyMeetings();

    @Test
    public void getStringFromParticipantsListWithSuccess(){
        ArrayList<String> initialList = new ArrayList<>(Arrays.asList("amandine@lamzone.com","luc@lamzone","alex@lamzone.com"));
        dummyMeetings.get(3).setParticipants(initialList);

        String result = dummyMeetings.get(3).getParticipantsInOneString();
        String expectedString = "amandine@lamzone.com, luc@lamzone, alex@lamzone.com";
        assertThat(result, equalTo(expectedString));
    }

    @Test
    public void initSpinnerWithSuccess(){
        String resultStart, resultEnd;
        mMeetingCreationStartFragment.initSpinnerTest();
        //value at the start of the list and at the end.
        resultStart = mMeetingCreationStartFragment.getSpinnerArray().get(0);
        resultEnd = mMeetingCreationStartFragment.getSpinnerArray().get(11);

        assertThat(resultStart, equalTo(initialHoursList.get(0)));
        assertThat(resultEnd,equalTo(initialHoursList.get(11)));
    }
}
