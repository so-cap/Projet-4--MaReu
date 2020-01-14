package com.sophie.mareu.controller;

import android.util.Log;

import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.service.AvailabilityByDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static com.sophie.mareu.ui.list_meetings.ListMeetingsActivity.ASCENDING;
import static com.sophie.mareu.ui.list_meetings.ListMeetingsActivity.DESCENDING;

/**
 * Created by SOPHIE on 13/01/2020.
 */
public class FilterAndSort {
    private static ArrayList<Meeting> mFilteredList = new ArrayList<>();
    private static ArrayList<Meeting> mSortedList = new ArrayList<>();

    public static void filterMeetingsList(Date date, String roomName) {
        mFilteredList.clear();
        if (date != null && roomName.isEmpty()) {
            mFilteredList = new ArrayList<>(getMeetings(date));
        } else if (!(roomName.isEmpty()) && date == null) {
            for (Map.Entry<Date, ArrayList<Meeting>> meetings : AvailabilityByDate.mMeetingsByDate.entrySet()) {
                for (int i = 0; i < meetings.getValue().size(); i++) {
                    if (meetings.getValue().get(i).getRoomName().equals(roomName)) {
                        mFilteredList.add(new Meeting(meetings.getValue().get(i)));
                    }
                }
            }
        } else
            for (int i = 0; i < getMeetings(date).size(); i++) {
                if (getMeetings(date).get(i).getRoomName().equals(roomName)) {
                    mFilteredList.add(new Meeting(getMeetings(date).get(i)));
                }
            }
    }

    private static ArrayList<Meeting> getMeetings(Date date) {
        if (AvailabilityByDate.mMeetingsByDate.get(date) != null) {
            return AvailabilityByDate.mMeetingsByDate.get(date);
        } else
            return new ArrayList<>();
    }

    public static void sortList(int listOrder) {
        if (listOrder == ASCENDING || listOrder == DESCENDING) {
            if (mFilteredList.isEmpty())
                mSortedList = AvailabilityByDate.getMeetings();
            else
                mSortedList = mFilteredList;
            Collections.sort(mSortedList, (meeting1, meeting2) -> meeting1.getHour().getKey().compareTo(meeting2.getHour().getKey()));
        }
        if (listOrder == DESCENDING)
            Collections.reverse(mSortedList);
    }

    public static ArrayList<Meeting> getSortedList() {
        return mSortedList;
    }

    public static ArrayList<Meeting> getFilteredList() {
        return mFilteredList;
    }
}
