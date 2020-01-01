package com.sophie.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.sophie.mareu.ui.meeting_creation.HomeStartMeetingCreationFragment;
import com.sophie.mareu.ui.meeting_creation.MeetingCreationStartFragment;
import com.sophie.mareu.ui.meeting_creation.ListMeetingFragment;

public class ListMeetingsActivity extends AppCompatActivity {
    private ListMeetingFragment mListMeetingFragment;
    private HomeStartMeetingCreationFragment mHomeStartMeetingCreationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listmeetings);

        configureAndShowListMeetingFragment();
        configureAndShowHomeStartMeetingCreationFragment();
    }

    private void configureAndShowListMeetingFragment() {
        mListMeetingFragment = (ListMeetingFragment) getSupportFragmentManager().findFragmentById(R.id.frame_listmeetings);
        if(mListMeetingFragment == null) {
            mListMeetingFragment = new ListMeetingFragment();
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.add(R.id.frame_listmeetings, mListMeetingFragment).commit();
        }
    }

    private void configureAndShowHomeStartMeetingCreationFragment() {
        mHomeStartMeetingCreationFragment = (HomeStartMeetingCreationFragment) getSupportFragmentManager().findFragmentById(R.id.frame_setmeeting);
        if(mHomeStartMeetingCreationFragment == null && findViewById(R.id.frame_setmeeting) != null){
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.add(R.id.frame_setmeeting, new HomeStartMeetingCreationFragment()).addToBackStack(null).commit();
        }
    }
}
