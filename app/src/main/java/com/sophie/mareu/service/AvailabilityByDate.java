package com.sophie.mareu.service;

import com.sophie.mareu.model.Meeting;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public static Date getDate(){
        return mDate;
    }

    public void setDate(Date date){
        mDate = date;
    }

    public static RoomsAvailabilityService getRoomsAvailabilityService(Date date){
        for(int position = 0; position < mAvailabilityByDateList.size(); position++){
            if (mAvailabilityByDateList.get(date) != null)
        return mAvailabilityByDateList.get(date);
        }
        return new RoomsAvailabilityService();
    }

    public static void updateAvailabilityByDate(Date date, RoomsAvailabilityService roomsAvailabilityService){
        mAvailabilityByDateList.remove(date);
        mAvailabilityByDateList.put(date,roomsAvailabilityService);
    }

    public static void addMeeting(Meeting meeting, Date date){
        ArrayList<Meeting> newMeetingList = new ArrayList<>();

        if (mMeetingsByDate.get(date) != null)
            mMeetingsByDate.get(date).add(meeting);
        else {
            newMeetingList.add(meeting);
            mMeetingsByDate.put(date, newMeetingList);
        }
    }

    public static ArrayList<Meeting> getMeetings(Date date){
        if (mMeetingsByDate.get(date) != null)
            return mMeetingsByDate.get(date);
        else
            return new ArrayList<>();
    }

    public static ArrayList<Meeting> filterMeetingsList(Date date, String roomName) {
        Date today = Calendar.getInstance().getTime();

        if (date == today && roomName.isEmpty()) {
            mFilteredList = AvailabilityByDate.getMeetings(date);
        } else if (!(roomName.isEmpty()) && date == null) {
            for (Map.Entry<Date, ArrayList<Meeting>> meetings : mMeetingsByDate.entrySet()) {
                for (int i = 0; i < meetings.getValue().size(); i++) {
                    if (meetings.getValue().get(i).getRoomName().equals(roomName))
                        mFilteredList.add(meetings.getValue().get(i));
                }
            }
        } else

    }


    public static ArrayList<Meeting>  getFilteredList(){
        return mFilteredList;
    }
}
