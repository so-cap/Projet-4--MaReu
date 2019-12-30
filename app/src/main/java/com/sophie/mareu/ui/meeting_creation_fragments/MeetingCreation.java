package com.sophie.mareu.ui.meeting_creation_fragments;

import androidx.fragment.app.Fragment;

import com.sophie.mareu.model.Meeting;

public class MeetingCreation extends Fragment {
    Meeting mMeeting = new Meeting();
    String mHour, mRoomName;






    private void setHourAndRoom(String hour, String roomName){
        mMeeting.setHour(hour);
        mMeeting.setRoomName(roomName);
    }


    public String getSelectedRoom(){
        return mMeeting.getRoomName();
    }
    public String getSelectedHour(){
        return mMeeting.getHour();
    }

}
