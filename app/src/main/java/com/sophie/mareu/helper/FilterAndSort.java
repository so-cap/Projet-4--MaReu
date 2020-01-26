package com.sophie.mareu.helper;

import androidx.annotation.VisibleForTesting;

import com.sophie.mareu.di.DI;
import com.sophie.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static com.sophie.mareu.Constants.*;

/**
 * Created by SOPHIE on 13/01/2020.
 */
public class FilterAndSort {
    private static ArrayList<Meeting> mFilteredList = new ArrayList<>();
    private static ArrayList<Meeting> mSortedList = new ArrayList<>();
    private static MeetingsHandler meetingsHandler = DI.getMeetingsHandler();

    public static void filterMeetingsList(Date date, String roomName) {
        mFilteredList.clear();
        if (date != null && roomName.isEmpty())
            mFilteredList = new ArrayList<>(meetingsHandler.getMeetingsByDate(date));
         else if (!(roomName.isEmpty()) && date == null) {
            for (Map.Entry<Date, ArrayList<Meeting>> meetings : meetingsHandler.meetingsByDate.entrySet()) {
                for (int i = 0; i < meetings.getValue().size(); i++) {
                    if (meetings.getValue().get(i).getRoomName().equals(roomName)) {
                        mFilteredList.add(new Meeting(meetings.getValue().get(i)));
                    }
                }
            }
        } else
            for (Meeting entry : meetingsHandler.getMeetingsByDate(date)) {
                if (entry.getRoomName().equals(roomName) && entry.getDate().equals(date)) {
                    mFilteredList.add(new Meeting(entry));
                }
            }
    }

    public static void sortList(int listOrder) {
        if (listOrder == ASCENDING || listOrder == DESCENDING) {
            if (mFilteredList.isEmpty())
                mSortedList = meetingsHandler.getMeetings();
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

    public static void clearLists(){
        mFilteredList.clear();
        mSortedList.clear();
    }

    static void removeMeeting(Meeting meeting){
        FilterAndSort.getFilteredList().remove(meeting);
        FilterAndSort.getSortedList().remove(meeting);
    }

    @VisibleForTesting
    public static void setMeetingsHandler(MeetingsHandler pMeetingsHandler){
        meetingsHandler = pMeetingsHandler;
    }
}
