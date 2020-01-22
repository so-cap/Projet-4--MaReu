package com.sophie.mareu.model;

import com.sophie.mareu.DI.DI;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by SOPHIE on 05/01/2020.
 */
public class MeetingsHandler {
    public HashMap<Date, ArrayList<Meeting>> meetingsByDate = new HashMap<>();
    public HashMap<Date, RoomsAvailabilityHandler> roomsAvailabilityByDate = new HashMap<>();
    private ArrayList<String> hours;
    private ArrayList<String> rooms;

    public RoomsAvailabilityHandler getCurrentRoomsAvailabilityController(Date date) {
        for (int position = 0; position < roomsAvailabilityByDate.size(); position++) {
            if (roomsAvailabilityByDate.containsKey(date))
                return roomsAvailabilityByDate.get(date);
        }
        RoomsAvailabilityHandler roomsHandler = DI.getNewRoomsAvailabilityHandler();
        roomsHandler.initRoomsPerHourList(hours, rooms);
        return roomsHandler;
    }

    public void setHoursAndRooms(ArrayList<String> hours, ArrayList<String> rooms){
        this.hours = hours;
        this.rooms = rooms;
    }

    public void updateAvailabilityByDate(Date date, RoomsAvailabilityHandler roomsAvailability) {
        roomsAvailabilityByDate.remove(date);
        roomsAvailabilityByDate.put(date, roomsAvailability);
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
        roomsAvailabilityByDate.clear();
        meetingsByDate.clear();
        FilterAndSort.clearLists();
    }

    public void deleteMeeting(Meeting meeting) {
        RoomsAvailabilityHandler currentRoomsController = roomsAvailabilityByDate.get(meeting.getDate());
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
            currentRoomsController.updateAvailableHoursAndRooms(roomsPerHourList);
        }
    }

    ArrayList<String> getHours() {
        return hours;
    }

    public ArrayList<String> getRooms() {
        return rooms;
    }

}
