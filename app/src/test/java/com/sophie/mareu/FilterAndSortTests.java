package com.sophie.mareu;

import com.sophie.mareu.di.DI;
import com.sophie.mareu.helper.MeetingsHandler;
import com.sophie.mareu.helper.FilterAndSort;
import com.sophie.mareu.model.Meeting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.sophie.mareu.Constants.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by SOPHIE on 16/01/2020.
 */

@RunWith(JUnit4.class)
public class FilterAndSortTests {
    private Meeting meetingA = DI.getDummyMeetings().get(0);
    private Meeting meetingB = DI.getDummyMeetings().get(1);
    private Meeting meetingC = DI.getDummyMeetings().get(2);
    private Meeting meetingD = DI.getDummyMeetings().get(3);
    private List<Meeting> meetings = DI.getDummyMeetings();
    private ArrayList<Meeting> expectedList = new ArrayList<>();
    private ArrayList<Meeting> result = new ArrayList<>();
    private MeetingsHandler meetingsHandler = DI.getNewMeetingsHandler();

    @Before
    public void setup(){
        FilterAndSort.clearLists();
        FilterAndSort.setMeetingsHandler(meetingsHandler);

        for (Meeting entry : meetings){
            meetingsHandler.addMeeting(entry, entry.getDate());
        }
    }

    @Test
    public void filterListByRoomWithSuccess(){
        String room = "Mario";
        expectedList = new ArrayList<>(Arrays.asList(meetingC,meetingB));

        FilterAndSort.filterMeetingsList(null, room);
        result = FilterAndSort.getFilteredList();

        System.out.println(result.size());
        assertTrue(result.containsAll(expectedList));
        assertThat(result.size(), equalTo(2));
    }

    @Test
    public void filterListByDateWithSuccess(){
        Date date = meetingD.getDate();

        expectedList.add(meetingD);
        FilterAndSort.filterMeetingsList(date, "");
        result = FilterAndSort.getFilteredList();

        assertTrue(result.containsAll(expectedList));
        assertThat(result.size(), equalTo(1));
    }

    @Test
    public void filterListByDateAndRoomWithSuccess(){
        String room = meetingC.getRoomName();
        Date date = meetingC.getDate();

        Date differentDate = meetingD.getDate();

        expectedList.add(meetingC);
        FilterAndSort.filterMeetingsList(date, room);
        result = FilterAndSort.getFilteredList();
        assertTrue(result.containsAll(expectedList));

        expectedList.clear();
        FilterAndSort.filterMeetingsList(differentDate, room);
        result = FilterAndSort.getFilteredList();
        assertEquals(expectedList, result);
    }

    @Test
    public void sortListInAscendingHourOrderWithSuccess(){
        expectedList = new ArrayList<>(Arrays.asList(meetingA, meetingD, meetingC, meetingB));

        FilterAndSort.sortList(ASCENDING);
        result = FilterAndSort.getSortedList();
        assertEquals(expectedList, result);
    }

    @Test
    public void sortListInDescendingHourOrderWithSuccess(){
        expectedList = new ArrayList<>(Arrays.asList(meetingB, meetingC, meetingD, meetingA));

        FilterAndSort.sortList(DESCENDING);
        result = FilterAndSort.getSortedList();
        assertEquals(expectedList, result);
    }
}
