package com.sophie.mareu.ui.meeting_creation;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.sophie.mareu.R;

/**
 * Created by SOPHIE on 31/12/2019.
 */
public class MeetingCreationActivity extends AppCompatActivity {
    private MeetingCreationStartFragment mMeetingCreationStartFragment;
    private ListMeetingFragment mListMeetingFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_creation);

        configureAndShowMeetingCreationStartFragment();
    }

    private void configureAndShowMeetingCreationStartFragment() {
        mMeetingCreationStartFragment = (MeetingCreationStartFragment) getSupportFragmentManager().findFragmentById(R.id.frame_setmeeting);
        if(mMeetingCreationStartFragment == null && findViewById(R.id.frame_setmeeting) != null){
                mMeetingCreationStartFragment = new MeetingCreationStartFragment();
                FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
                fm.add(R.id.frame_setmeeting, mMeetingCreationStartFragment).commit();
            }
    }

    private void configureAndShowListMeetingFragment() {
        // to go back to the main activity display in land mode
        mListMeetingFragment = (ListMeetingFragment) getSupportFragmentManager().findFragmentById(R.id.frame_listmeetings);
        if(mListMeetingFragment == null && findViewById(R.id.frame_listmeetings) != null) {
            finish();
        }
    }
}
