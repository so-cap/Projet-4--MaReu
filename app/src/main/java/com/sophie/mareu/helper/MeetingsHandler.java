package com.sophie.mareu.helper;

import com.sophie.mareu.di.DI;
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
public class MeetingsHandler {
    public HashMap<Date, ArrayList<Meeting>> meetingsByDate = new HashMap<>();
    public HashMap<Date, RoomsAvailabilityHandler> roomsAvailabilityByDate = new HashMap<>();
    private ArrayList<String> hours;
    private ArrayList<String> rooms;

    public void setHoursAndRooms(ArrayList<String> hours, ArrayList<String> rooms) {
        this.hours = hours;
        this.rooms = rooms;
    }

    public RoomsAvailabilityHandler getCurrentRoomsAvailabilityHandler(Date date) {
        for (int position = 0; position < roomsAvailabilityByDate.size(); position++) {
            if (roomsAvailabilityByDate.containsKey(date))
                return roomsAvailabilityByDate.get(date);
        }
        RoomsAvailabilityHandler roomsHandler = DI.getNewRoomsAvailabilityHandler();
        roomsHandler.initRoomsPerHourList(hours, rooms);
        return roomsHandler;
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

    public void deleteMeeting(Meeting meeting) {
        RoomsAvailabilityHandler currentRoomsHandler = roomsAvailabilityByDate.get(meeting.getDate());
        if (currentRoomsHandler != null) {
            ArrayList<RoomsPerHour> roomsPerHourList = currentRoomsHandler.getRoomsPerHourList();
            Integer meetingHourPosition = meeting.getHour().getKey();
            // check if the hour of the meeting exists in the list
            int hourAvailable = 0;
            for (int i = 0; i < roomsPerHourList.size(); i++)
                if (roomsPerHourList.get(i).getHour().getKey().equals(meetingHourPosition)){
                    hourAvailable++;
                    break;
                }
            // make hour available again if it wasn't anymore
            if (hourAvailable == 0) {
                RoomsPerHour roomsPerHour = new RoomsPerHour();
                roomsPerHour.setHour(meetingHourPosition, meeting.getHour().getValue());
                if (roomsPerHourList.size() > meetingHourPosition)
                    roomsPerHourList.add(meetingHourPosition, roomsPerHour);
                else
                    roomsPerHourList.add(roomsPerHour);
            }
            // make room available again
            if (roomsPerHourList.size() > meetingHourPosition)
                roomsPerHourList.get(meeting.getHour().getKey()).addRoom(meeting.getRoomName());
            else
                roomsPerHourList.get(roomsPerHourList.size() - 1).addRoom(meeting.getRoomName());

            // finally, delete meeting from lists
            Objects.requireNonNull(meetingsByDate.get(meeting.getDate())).remove(meeting);
            FilterAndSort.removeMeeting(meeting);

            if (meetingsByDate.get(meeting.getDate()) == null)
                meetingsByDate.remove(meeting.getDate());

            // update handlers
            currentRoomsHandler.updateAvailableHoursAndRooms(roomsPerHourList);
        }
    }

    public void clearAllMeetings() {
        roomsAvailabilityByDate.clear();
        meetingsByDate.clear();
        FilterAndSort.clearLists();
    }

    public ArrayList<String> getHours() {
        return hours;
    }

    public ArrayList<String> getRooms() {
        return rooms;
    }

}
