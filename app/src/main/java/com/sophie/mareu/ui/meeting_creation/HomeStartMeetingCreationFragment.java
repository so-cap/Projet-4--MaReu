package com.sophie.mareu.ui.meeting_creation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sophie.mareu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SOPHIE on 01/01/2020.
 */
public class HomeStartMeetingCreationFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.fab_home)
    FloatingActionButton mFab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_start_meeting_creation, container, false);
        ButterKnife.bind(this, view);

        mFab.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
            FragmentTransaction fm = getFragmentManager().beginTransaction();
            fm.replace(R.id.frame_setmeeting, new MeetingCreationStartFragment()).addToBackStack(null).commit();
    }

}
