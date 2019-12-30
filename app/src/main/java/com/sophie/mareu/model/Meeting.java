package com.sophie.mareu.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class Meeting implements Serializable {
    private ArrayList<String> mRoomName;
    private ArrayList<String> mParticipants;
    private int mHour;
    private String mTitle;
    private String mSubject;


    public Meeting(ArrayList<String> roomName, ArrayList<String> participants, int hour, String title, String subject) {
        mRoomName = roomName;
        mParticipants = participants;
        mHour = hour;
        mTitle = title;
        mSubject = subject;
    }


}
