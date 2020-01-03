package com.sophie.mareu.controller;

import android.util.Log;

import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.service.MeetingsApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by SOPHIE on 03/01/2020.
 */
public class SortList {
    private static ArrayList<Meeting> meetingsList = new ArrayList<>();
    private static final String TAG = "LOGGSortList";

    public static void sortByHour(Boolean ascending) {
        meetingsList = MeetingsApi.getMeetingsList();
        if (ascending) {
            Collections.sort(meetingsList, new Comparator<Meeting>() {
                @Override
                public int compare(Meeting meeting1, Meeting meeting2) {
                    return meeting1.getHour().compareTo(meeting2.getHour());
                }
            });
        } else {
            Collections.reverse(meetingsList);
        }

        MeetingsApi.updateListOrder(meetingsList);
    }

    public static void sortByRoomName(Boolean ascending) {
        meetingsList = MeetingsApi.getMeetingsList();
        if (ascending) {
            Collections.sort(meetingsList, new Comparator<Meeting>() {
                @Override
                public int compare(Meeting meeting1, Meeting meeting2) {
                    return meeting1.getRoomName().compareTo(meeting2.getRoomName());
                }
            });
        } else {
            Collections.reverse(meetingsList);
        }
        MeetingsApi.updateListOrder(meetingsList);
    }
}
