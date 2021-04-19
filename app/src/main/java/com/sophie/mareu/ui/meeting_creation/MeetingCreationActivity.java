package com.sophie.mareu.ui.meeting_creation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.sophie.mareu.R;
import com.sophie.mareu.ui.list_meetings.ListMeetingsFragment;

/**
 * Created by SOPHIE on 31/12/2019.
 */
public class MeetingCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet ing_creation);

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

    // to go back to the main activity display when rotating into land mode
    private void configureAndShowListMeetingFragment() {
        ListMeetingsFragment listMeetingsFragment = (ListMeetingsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frame_listmeetings);
        if(listMeetingsFragment == null && findViewById(R.id.frame_listmeetings) != null) {
            finish();
        }
    }
}
