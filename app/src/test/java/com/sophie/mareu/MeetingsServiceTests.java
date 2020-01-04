package com.sophie.mareu;

import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.service.DummyMeetingsGenerator;
import com.sophie.mareu.service.MeetingsService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by SOPHIE on 04/01/2020.
 */
@RunWith(JUnit4.class)
public class MeetingsServiceTests {
    private MeetingsService mService = new MeetingsService();

    @Test
    public void addMeetingWithSuccess(){
        MeetingsService.addMeeting(DummyMeetingsGenerator.getDummyMeetings().get(0));
        MeetingsService.addMeeting(DummyMeetingsGenerator.getDummyMeetings().get(1));

        assertThat(MeetingsService.getMeetingsList().size(),equalTo(2));
    }

}
