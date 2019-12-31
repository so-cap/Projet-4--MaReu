package com.sophie.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sophie.mareu.ui.meeting_creation_fragments.MeetingCreationStartFragment;
import com.sophie.mareu.ui.meeting_list.MeetingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMeetingsActivity extends AppCompatActivity {
    private MeetingFragment mMeetingFragment;
    private MeetingCreationStartFragment mMeetingCreationStartFragment;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listmeetings);

        ButterKnife.bind(this);

        configureAndShowMeetingFragment();
        configureAndShowMeetingCreationFragment();
    }

    private void configureAndShowMeetingFragment() {
        mMeetingFragment = (MeetingFragment) getSupportFragmentManager().findFragmentById(R.id.frame_listmeetings);

        if(mMeetingFragment == null) {
            mMeetingFragment = new MeetingFragment();
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.add(R.id.frame_listmeetings, mMeetingFragment).commit();
        }

        if(mMeetingCreationStartFragment == null){
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
                    fm.replace(R.id.frame_listmeetings, new MeetingCreationStartFragment())
                            .addToBackStack(null).commit();
                }
            });
        }
    }

    private void configureAndShowMeetingCreationFragment() {
        mMeetingCreationStartFragment = (MeetingCreationStartFragment) getSupportFragmentManager().findFragmentById(R.id.frame_setmeeting);

        if(mMeetingCreationStartFragment == null && findViewById(R.id.frame_setmeeting) != null){
            mMeetingCreationStartFragment = new MeetingCreationStartFragment();
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();

            if // frame exist 
            fm.add(R.id.frame_setmeeting, mMeetingCreationStartFragment).commit();
        }
    }
}
