package com.sophie.mareu.service;

import com.sophie.mareu.model.Meeting;

import java.util.ArrayList;
// STAND BY
public class MeetingsApi {
    private static ArrayList<Meeting> mMeetings = new ArrayList<>();

    public static void addMeeting(Meeting meeting){
        mMeetings.add(meeting);
    }

    public static ArrayList<Meeting> getMeetingsList(){
        return mMeetings;
    }

    public static void deleteMeeting(Meeting meeting){
        mMeetings.remove(meeting);
    }

    public static void clearMeetingList(){
        mMeetings.clear();
    }

}
