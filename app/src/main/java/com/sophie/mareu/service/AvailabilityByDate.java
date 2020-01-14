package com.sophie.mareu.service;

import android.util.Log;

import com.sophie.mareu.controller.FilterAndSort;
import com.sophie.mareu.controller.RoomsPerHour;
import com.sophie.mareu.model.Meeting;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by SOPHIE on 05/01/2020.
 */
public class AvailabilityByDate {
    private static Date mDate;
    private static HashMap<Date, RoomsAvailabilityService> mAvailabilityByDateList = new HashMap<>();
    public static HashMap<Date, ArrayList<Meeting>> mMeetingsByDate = new HashMap<>();

    public static Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    private static final String TAG = "LOGGAvailabilityByDate";

    public static RoomsAvailabilityService getRoomsAvailabilityService(Date date) {
        for (int position = 0; position < mAvailabilityByDateList.size(); position++) {
            if (mAvailabilityByDateList.containsKey(date))
                return mAvailabilityByDateList.get(date);
        }
        return new RoomsAvailabilityService();
    }

    public static void updateAvailabilityByDate(Date date, RoomsAvailabilityService roomsAvailabilityService) {
        mAvailabilityByDateList.remove(date);
        mAvailabilityByDateList.put(date, roomsAvailabilityService);
    }

    public static void addMeeting(Meeting meeting, Date date) {
        if (mMeetingsByDate.get(date) != null)
            mMeetingsByDate.get(date).add(meeting);
        else {
            ArrayList<Meeting> newMeetingList = new ArrayList<>();
            newMeetingList.add(meeting);
            mMeetingsByDate.put(date, newMeetingList);
        }
    }

    public static ArrayList<Meeting> getMeetings() {
        ArrayList<Meeting> meetings = new ArrayList<>();
        Log.d(TAG, "getMeetings: "+ mMeetingsByDate.size());
        for (Map.Entry<Date, ArrayList<Meeting>> entry : mMeetingsByDate.entrySet()) {
            meetings.addAll(entry.getValue());
            Log.d(TAG, "getMeetings:SIZE "+ entry.getValue().size());
        }
        return meetings;
    }

    public static void clearAllMeetings() {
        mAvailabilityByDateList.clear();
        mMeetingsByDate.clear();
    }


    // TODO: deal with this
    public static void deleteMeeting(Meeting meeting) {
        RoomsAvailabilityService currentService = mAvailabilityByDateList.get(meeting.getDate());
        if (currentService != null) {
            ArrayList<RoomsPerHour> updateRooms = currentService.getRoomsPerHourList();
            Integer meetingPosition = meeting.getHour().getKey();

            // make hour available for a new meeting
            if (!updateRooms.get(meetingPosition).getHour().getKey().equals(meetingPosition)) {
                RoomsPerHour roomsPerHour = new RoomsPerHour();
                roomsPerHour.setHour(meetingPosition, meeting.getHour().getValue());
                updateRooms.add(meetingPosition,roomsPerHour);
            }

            // make room available again
            updateRooms.get(meeting.getHour().getKey()).addRoom(meeting.getRoomName());

            // finally, delete meeting from lists
            for (Map.Entry<Date, ArrayList<Meeting>> entry : mMeetingsByDate.entrySet()) {
                        entry.getValue().remove(meeting);
            }
            Log.d(TAG, " HAS MEETING SIZE" + mMeetingsByDate.get(meeting.getDate()).size());
            Log.d(TAG, "deleteMeeting: index" +mMeetingsByDate.get(meeting.getDate()).indexOf(meeting));
            FilterAndSort.getFilteredList().remove(meeting);
            FilterAndSort.getSortedList().remove(meeting);

            if (mMeetingsByDate.get(meeting.getDate()) == null)
                mMeetingsByDate.remove(meeting.getDate());

            // update service
            currentService.updateAvailableHours(updateRooms);
        }
    }

}
