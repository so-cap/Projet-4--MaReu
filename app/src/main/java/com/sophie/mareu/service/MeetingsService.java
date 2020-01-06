package com.sophie.mareu.service;

import com.sophie.mareu.model.Meeting;

import java.util.ArrayList;

public class MeetingsService {
    private static ArrayList<Meeting> mMeetings = new ArrayList<>();

    public static void addMeeting(Meeting meeting){
        mMeetings.add(meeting);
    }

    public static ArrayList<Meeting> getMeetingsList(){
        return mMeetings;
    }

    public static void updateListOrder(ArrayList<Meeting> meetings){
        mMeetings = meetings;
    }

}
