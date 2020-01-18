package com.sophie.mareu.controller;

import com.sophie.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static com.sophie.mareu.controller.AvailabilityByDate.getMeetings;
import static com.sophie.mareu.Constants.*;

/**
 * Created by SOPHIE on 13/01/2020.
 */
public class FilterAndSort {
    private static ArrayList<Meeting> mFilteredList = new ArrayList<>();
    private static ArrayList<Meeting> mSortedList = new ArrayList<>();

    public static void filterMeetingsList(Date date, String roomName) {
        mFilteredList.clear();
        if (date != null && roomName.isEmpty())
            mFilteredList = new ArrayList<>(getMeetings(date));
         else if (!(roomName.isEmpty()) && date == null) {
            for (Map.Entry<Date, ArrayList<Meeting>> meetings : AvailabilityByDate.mMeetingsByDate.entrySet()) {
                for (int i = 0; i < meetings.getValue().size(); i++) {
                    if (meetings.getValue().get(i).getRoomName().equals(roomName)) {
                        mFilteredList.add(new Meeting(meetings.getValue().get(i)));
                    }
                }
            }
        } else
            for (Meeting entry : getMeetings(date)) {
                if (entry.getRoomName().equals(roomName) && entry.getDate().equals(date)) {
                    mFilteredList.add(new Meeting(entry));
                }
            }
    }

    public static void sortList(int listOrder) {
        if (listOrder == ASCENDING || listOrder == DESCENDING) {
            if (mFilteredList.isEmpty())
                mSortedList = getMeetings();
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
