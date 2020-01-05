package com.sophie.mareu.service;

import com.sophie.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by SOPHIE on 05/01/2020.
 */
public class AvailabilityByDate {
    private static Date mDate;
    private static HashMap<Date, RoomsAvailabilityService> mAvailabilityByDateList = new HashMap<>();
    private static HashMap<Date, ArrayList<Meeting>> mMeetingsByDate = new HashMap<>();
    private static ArrayList<Meeting> mMeetings = new ArrayList<>();
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
            return null;
    }
}
