package com.sophie.mareu.controller;

import com.sophie.mareu.DI.DI;
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
public class MeetingsController {
    public HashMap<Date, ArrayList<Meeting>> meetingsByDate = new HashMap<>();
    public HashMap<Date, RoomsAvailabilityController> serviceByDate = new HashMap<>();
    private ArrayList<String> hours;
    private ArrayList<String> rooms;

    public RoomsAvailabilityController getCurrentRoomsAvailabilityService(Date date) {
        for (int position = 0; position < serviceByDate.size(); position++) {
            if (serviceByDate.containsKey(date))
                return serviceByDate.get(date);
        }
        RoomsAvailabilityController roomsController = DI.getNewRoomsAvailabilityController();
        roomsController.initRoomsPerHourList(hours, rooms);
        return roomsController;
    }

    public void setHoursAndRooms(ArrayList<String> hours, ArrayList<String> rooms){
        this.hours = hours;
        this.rooms = rooms;
    }

    public void updateAvailabilityByDate(Date date, RoomsAvailabilityController roomsAvailability) {
        serviceByDate.remove(date);
        serviceByDate.put(date, roomsAvailability);
    }

    public void addMeeting(Meeting meeting, Date date) {
        if (meetingsByDate.get(date) != null)
            Objects.requireNonNull(meetingsByDate.get(date)).add(meeting);
        else {
            ArrayList<Meeting> newMeetingList = new ArrayList<>();
            newMeetingList.add(meeting);
            meetingsByDate.put(date, newMeetingList);
        }
    }

    public ArrayList<Meeting> getMeetings() {
        ArrayList<Meeting> meetings = new ArrayList<>();
        for (Map.Entry<Date, ArrayList<Meeting>> entry : meetingsByDate.entrySet()) {
            meetings.addAll(entry.getValue());
        }
        return meetings;
    }

    public ArrayList<Meeting> getMeetingsByDate(Date date) {
        if (meetingsByDate.get(date) != null) {
            return meetingsByDate.get(date);
        } else
            return new ArrayList<>();
    }

    public void clearAllMeetings() {
        serviceByDate.clear();
        meetingsByDate.clear();
        FilterAndSort.clearLists();
    }

    public void deleteMeeting(Meeting meeting) {
        RoomsAvailabilityController currentRoomsController = serviceByDate.get(meeting.getDate());
        if (currentRoomsController != null) {
            ArrayList<RoomsPerHour> roomsPerHourList = currentRoomsController.getRoomsPerHourList();
            Integer meetingHourPosition = meeting.getHour().getKey();
            // make hour available again if it wasn't anymore
            if (roomsPerHourList.size() < meetingHourPosition ||
                    !roomsPerHourList.get(meetingHourPosition).getHour().getKey().equals(meetingHourPosition)) {
                RoomsPerHour roomsPerHour = new RoomsPerHour();
                roomsPerHour.setHour(meetingHourPosition, meeting.getHour().getValue());
                if (roomsPerHourList.size() > meetingHourPosition)
                    roomsPerHourList.add(meetingHourPosition,roomsPerHour);
                else
                    roomsPerHourList.add(roomsPerHour);
            }
            // make room available again
            if (roomsPerHourList.size() > meetingHourPosition)
                roomsPerHourList.get(meeting.getHour().getKey()).addRoom(meeting.getRoomName());
            else
                roomsPerHourList.get(roomsPerHourList.size()-1).addRoom(meeting.getRoomName());

            // finally, delete meeting from lists
            Objects.requireNonNull(meetingsByDate.get(meeting.getDate())).remove(meeting);
            FilterAndSort.removeMeeting(meeting);

            if (meetingsByDate.get(meeting.getDate()) == null)
                meetingsByDate.remove(meeting.getDate());

            // update service
            currentRoomsController.updateAvailableHours(roomsPerHourList);
        }
    }

    public ArrayList<String> getHours() {
        return hours;
    }

    public ArrayList<String> getRooms() {
        return rooms;
    }

}
