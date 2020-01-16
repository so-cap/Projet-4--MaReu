package com.sophie.mareu.controller;

import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.service.RoomsAvailabilityByHourImpl;
import com.sophie.mareu.service.RoomsAvailabilityService;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by SOPHIE on 05/01/2020.
 */
public class AvailabilityByDate {
    public static HashMap<Date, ArrayList<Meeting>> mMeetingsByDate = new HashMap<>();
    public static HashMap<Date, RoomsAvailabilityService> serviceByDate = new HashMap<>();

    private static final String TAG = "LOGIAvailabilityByDate";

    public static RoomsAvailabilityService getRoomsAvailabilityService(Date date) {
        for (int position = 0; position < serviceByDate.size(); position++) {
            if (serviceByDate.containsKey(date))
                return serviceByDate.get(date);
        }
        return getNewService();
    }

    private static RoomsAvailabilityService getNewService() {
        return new RoomsAvailabilityByHourImpl();
    }

    public static void updateAvailabilityByDate(Date date, RoomsAvailabilityService roomsAvailabilityService) {
        serviceByDate.remove(date);
        serviceByDate.put(date, roomsAvailabilityService);
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
        for (Map.Entry<Date, ArrayList<Meeting>> entry : mMeetingsByDate.entrySet()) {
            meetings.addAll(entry.getValue());
        }
        return meetings;
    }

    public static ArrayList<Meeting> getMeetings(Date date) {
        if (AvailabilityByDate.mMeetingsByDate.get(date) != null) {
            return AvailabilityByDate.mMeetingsByDate.get(date);
        } else
            return new ArrayList<>();
    }

    public static void clearAllMeetings() {
        serviceByDate.clear();
        mMeetingsByDate.clear();
    }

    public static void deleteMeeting(Meeting meeting) {
        RoomsAvailabilityService currentService = serviceByDate.get(meeting.getDate());
        if (currentService != null) {
            ArrayList<RoomsPerHour> updateRooms = currentService.getRoomsPerHourList();
            Integer meetingPosition = meeting.getHour().getKey();
            // make hour available again if it wasn't anymore
            if (!updateRooms.get(meetingPosition).getHour().getKey().equals(meetingPosition)) {
                RoomsPerHour roomsPerHour = new RoomsPerHour();
                roomsPerHour.setHour(meetingPosition, meeting.getHour().getValue());
                updateRooms.add(meetingPosition,roomsPerHour);
            }
            // make room available again
            updateRooms.get(meeting.getHour().getKey()).addRoom(meeting.getRoomName());

            // finally, delete meeting from lists
            Objects.requireNonNull(mMeetingsByDate.get(meeting.getDate())).remove(meeting);
            FilterAndSort.getFilteredList().remove(meeting);
            FilterAndSort.getSortedList().remove(meeting);

            if (mMeetingsByDate.get(meeting.getDate()) == null)
                mMeetingsByDate.remove(meeting.getDate());

            // update service
            currentService.updateAvailableHours(updateRooms);
        }
    }
}
