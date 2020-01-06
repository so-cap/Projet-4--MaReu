package com.sophie.mareu.service;
import android.util.Log;

import com.sophie.mareu.controller.RoomsPerHour;
import com.sophie.mareu.model.Meeting;


import java.util.ArrayList;
import java.util.Calendar;
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
    private static HashMap<Date, ArrayList<Meeting>> mMeetingsByDate = new HashMap<>();
    private static ArrayList<Meeting> mMeetings = new ArrayList<>();
    private static ArrayList<Meeting> mFilteredList = new ArrayList<>();
    private static Meeting mMeeting;

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
        ArrayList<Meeting> newMeetingList = new ArrayList<>();

        if (mMeetingsByDate.get(date) != null)
            mMeetingsByDate.get(date).add(meeting);
        else {
            newMeetingList.add(meeting);
            mMeetingsByDate.put(date, newMeetingList);
        }
    }

    public static ArrayList<Meeting> getMeetings(Date date) {
        if (mMeetingsByDate.get(date) != null)
            return mMeetingsByDate.get(date);
        else
            return new ArrayList<>();
    }

    public static void filterMeetingsList(Date date, String roomName) {
        mFilteredList.clear();
        if (date != null && roomName.isEmpty()) {
            mFilteredList = AvailabilityByDate.getMeetings(date);
        } else if (!(roomName.isEmpty()) && date == null) {
            for (Map.Entry<Date, ArrayList<Meeting>> meetings : mMeetingsByDate.entrySet()) {
                for (int i = 0; i < meetings.getValue().size(); i++) {
                    if (meetings.getValue().get(i).getRoomName().equals(roomName)) {
                        mFilteredList.add(meetings.getValue().get(i));
                    }
                }
            }
        } else
            for (int i = 0; i < AvailabilityByDate.getMeetings(date).size(); i++) {
                if (AvailabilityByDate.getMeetings(date).get(i).getRoomName().equals(roomName))
                    mFilteredList.add(AvailabilityByDate.getMeetings(date).get(i));

            }
    }

    public static ArrayList<Meeting> getFilteredList() {
        return mFilteredList;
    }

    public static void clearAllMeetings() {
        mAvailabilityByDateList.clear();
        mMeetingsByDate.clear();
    }

    public static void deleteMeeting(Meeting meeting) {
        RoomsAvailabilityService currentService = mAvailabilityByDateList.get(meeting.getDate());
        if (currentService != null) {
            ArrayList<RoomsPerHour> updateRooms = currentService.getRoomsPerHourList();

            // make hour available for new meeting again
            if (meeting.getHour().getKey() > updateRooms.size() ||
                    !updateRooms.get(meeting.getHour().getKey()).getHour().getKey().equals(meeting.getHour().getKey())) {
                RoomsPerHour roomsPerHour = new RoomsPerHour();
                roomsPerHour.setHour(meeting.getHour().getKey(), meeting.getHour().getValue());
                updateRooms.add(roomsPerHour);
                Log.d(TAG, "deleteMeeting: NO");
                Log.d(TAG, "deleteMeeting: " +meeting.getHour().getKey());
                Log.d(TAG, "deleteMeeting: NOT EQUAL" +updateRooms.get(meeting.getHour().getKey()).getHour().getKey());

            }

            // make room available again
            updateRooms.get(meeting.getHour().getKey()).getRooms().add(meeting.getRoomName());

            // finally, delete meeting from lists
            Objects.requireNonNull(mMeetingsByDate.get(meeting.getDate())).remove(meeting);
            mFilteredList.remove(meeting);

            // update service
            currentService.updateAvailableHours(updateRooms);
        }
    }

}
