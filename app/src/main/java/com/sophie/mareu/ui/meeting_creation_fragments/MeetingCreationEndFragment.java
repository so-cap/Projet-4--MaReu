package com.sophie.mareu.ui.meeting_creation_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sophie.mareu.model.Meeting;

import java.util.ArrayList;


public class MeetingCreationEndFragment extends Fragment {
    private Meeting mMeeting;
    private MeetingCreationStartFragment mMeetingCreationStart;
    private String mTitle, mHour, mRoomName, mDetailSubject;
    private ArrayList<String> mParticipants;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoomName = mMeetingCreationStart.getSelectedRoom();
        mHour = mMeetingCreationStart.getSelectedHour();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setMeeting();
        return null;
    }


    public void setMeeting(){
        mMeeting = new Meeting(mTitle, mHour, mRoomName, mParticipants, mDetailSubject);
    }

    // once all OK :  meeting
    // end , onClick : MeetingApi.addMeeting(mMeeting);
}
