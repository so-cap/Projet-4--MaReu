package com.sophie.mareu.controller;

import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.service.MeetingsService;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by SOPHIE on 03/01/2020.
 */
public class SortList {
    private static ArrayList<Meeting> meetingsList = new ArrayList<>();
    private static final String TAG = "LOGGSortList";

    public static void sortByHour(Boolean ascending) {
        meetingsList = MeetingsService.getMeetingsList();

        Collections.sort(meetingsList, (meeting1, meeting2) -> meeting1.getHour().getKey().compareTo(meeting2.getHour().getKey()));
        if (!ascending)
            Collections.reverse(meetingsList);
        MeetingsService.updateListOrder(meetingsList);
    }

    public static void sortByRoomName(Boolean ascending) {
        meetingsList = MeetingsService.getMeetingsList();

        Collections.sort(meetingsList, (meeting1, meeting2) -> meeting1.getRoomName().compareTo(meeting2.getRoomName()));
        if (!ascending)
            Collections.reverse(meetingsList);
        MeetingsService.updateListOrder(meetingsList);
    }
}
