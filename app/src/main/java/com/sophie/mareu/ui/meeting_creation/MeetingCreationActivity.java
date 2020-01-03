package com.sophie.mareu.ui.meeting_creation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sophie.mareu.R;
import com.sophie.mareu.ui.list_meetings.ListMeetingFragment;

/**
 * Created by SOPHIE on 31/12/2019.
 */
public class MeetingCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_creation);

        configureAndShowMeetingCreationStartFragment();
        configureAndShowListMeetingFragment();
    }

    private void configureAndShowMeetingCreationStartFragment() {
        Fragment meetingCreationStartFragment = getSupportFragmentManager().findFragmentById(R.id.frame_setmeeting);

        if(meetingCreationStartFragment == null && findViewById(R.id.frame_setmeeting) != null){
                meetingCreationStartFragment = new MeetingCreationStartFragment();
                FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
                fm.add(R.id.frame_setmeeting, meetingCreationStartFragment).commit();
            }
    }

    // to go back to the main activity display when in land mode
    private void configureAndShowListMeetingFragment() {
        ListMeetingFragment listMeetingFragment = (ListMeetingFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frame_listmeetings);
        if(listMeetingFragment == null && findViewById(R.id.frame_listmeetings) != null) {
            finish();
        }
    }
}
