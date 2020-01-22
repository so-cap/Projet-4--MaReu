package com.sophie.mareu.service;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.controller.FilterAndSort;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.model.RoomsPerHour;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by SOPHIE on 05/01/2020.
 */
public class MeetingsApiServiceImpl implements MeetingsApiService{
    public static HashMap<Date, ArrayList<Meeting>> mMeetingsByDate = new HashMap<>();
    public static HashMap<Date, RoomsAvailabilityApiService> serviceByDate = new HashMap<>();
    private static ArrayList<String> hours;
    private static ArrayList<String> rooms;

    public static RoomsAvailabilityApiService getCurrentRoomsPerHourService(Date date) {
        for (int position = 0; position < serviceByDate.size(); position++) {
            if (serviceByDate.containsKey(date))
                return serviceByDate.get(date);
        }
        RoomsAvailabilityApiService service = DI.getNewRoomsAvailabilityService();
        service.initRoomsAndHours(hours, rooms);
        return service;
    }

    public void setHoursAndRooms(ArrayList<String> pHours, ArrayList<String> pRooms){
        hours = pHours;
        rooms = pRooms;
    }

    public static void updateAvailabilityByDate(Date date, RoomsAvailabilityApiService roomsAvailabilityApiService) {
        serviceByDate.remove(date);
        serviceByDate.put(date, roomsAvailabilityApiService);
    }

    public static void addMeeting(Meeting meeting, Date date) {
        if (mMeetingsByDate.get(date) != null)
            Objects.requireNonNull(mMeetingsByDate.get(date)).add(meeting);
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

    public static ArrayList<Meeting> getMeetingsByDate(Date date) {
        if (MeetingsApiServiceImpl.mMeetingsByDate.get(date) != null) {
            return MeetingsApiServiceImpl.mMeetingsByDate.get(date);
        } else
            return new ArrayList<>();
    }

    public static void clearAllMeetings() {
        serviceByDate.clear();
        mMeetingsByDate.clear();
        FilterAndSort.clearLists();
    }

    public static void deleteMeeting(Meeting meeting) {
        RoomsAvailabilityApiService currentService = serviceByDate.get(meeting.getDate());
        if (currentService != null) {
            ArrayList<RoomsPerHour> roomsPerHourList = currentService.getRoomsPerHourList();
            Integer meetingPosition = meeting.getHour().getKey();
            // make hour available again if it wasn't anymore
            if (!roomsPerHourList.get(meetingPosition).getHour().getKey().equals(meetingPosition)) {
                RoomsPerHour roomsPerHour = new RoomsPerHour();
                roomsPerHour.setHour(meetingPosition, meeting.getHour().getValue());
                roomsPerHourList.add(meetingPosition,roomsPerHour);
            }
            // make room available again
            roomsPerHourList.get(meeting.getHour().getKey()).addRoom(meeting.getRoomName());

            // finally, delete meeting from lists
            Objects.requireNonNull(mMeetingsByDate.get(meeting.getDate())).remove(meeting);
            FilterAndSort.removeMeeting(meeting);

            if (mMeetingsByDate.get(meeting.getDate()) == null)
                mMeetingsByDate.remove(meeting.getDate());

            // update service
            currentService.updateAvailableHours(roomsPerHourList);
        }
    }
}
