package com.sophie.mareu.ui.list_meetings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.sophie.mareu.R;
import com.sophie.mareu.api.MeetingsApi;
import com.sophie.mareu.ui.meeting_creation.HomeStartMeetingCreationFragment;

public class ListMeetingsActivity extends AppCompatActivity {
    private Fragment homeFragment, listMeetingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listmeetings);

        configureAndShowListMeetingFragment();
        configureAndShowHomeStartMeetingCreationFragment();
    }

    private void configureAndShowListMeetingFragment() {
        listMeetingFragment = getSupportFragmentManager().findFragmentById(R.id.frame_listmeetings);
        if(listMeetingFragment == null) {
            listMeetingFragment = new ListMeetingFragment();
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.add(R.id.frame_listmeetings, listMeetingFragment).commit();
        }
    }

    private void configureAndShowHomeStartMeetingCreationFragment() {
        homeFragment = getSupportFragmentManager().findFragmentById(R.id.frame_setmeeting);
        if(homeFragment == null && findViewById(R.id.frame_setmeeting) != null){
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.add(R.id.frame_setmeeting, new HomeStartMeetingCreationFragment()).commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MeetingsApi.clearMeetingList();
    }
}
