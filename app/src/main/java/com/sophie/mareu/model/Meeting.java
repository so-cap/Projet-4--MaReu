package com.sophie.mareu.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class Meeting implements Serializable {
    private String mRoomName;
    private ArrayList<String> mParticipants;
    private String mHour;
    private String mTitle;
    private String mDetailSubject;

    public Meeting(){
    }

    public Meeting(String title,  String hour, String roomName, ArrayList<String> participants, String subject) {
        mTitle = title;
        mHour = hour;
        mRoomName = roomName;
        mParticipants = participants;
        mDetailSubject = subject;
    }

    public String getRoomName() {
        return mRoomName;
    }

    public void setRoomName(String roomName) {
        mRoomName = roomName;
    }

    public String getParticipants() {
        String participants = "";
        for(int i = 0; i < mParticipants.size(); i++) {
            participants += mParticipants.get(i);
            //check if we reach the end of the list
            if ((i + 1) != mParticipants.size())
                participants += ", ";
        }
            return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        mParticipants = participants;
    }

    public String getHour() {
        return mHour;
    }

    public void setHour(String hour) {
        mHour = hour;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSubject() {
        return mDetailSubject;
    }

    public void setSubject(String subject) {
        mDetailSubject = subject;
    }
}
