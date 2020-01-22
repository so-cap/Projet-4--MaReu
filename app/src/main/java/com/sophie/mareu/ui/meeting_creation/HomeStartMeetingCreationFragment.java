package com.sophie.mareu.ui.meeting_creation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.sophie.mareu.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SOPHIE on 01/01/2020.
 */
public class HomeStartMeetingCreationFragment extends Fragment implements View.OnClickListener {
    private CardView filterView;
    @BindView(R.id.home_btn)
    Button fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_start_meeting_creation, container, false);
        ButterKnife.bind(this, view);

        if (getActivity() != null) filterView = getActivity().findViewById(R.id.filter_activity);
        fab.setOnClickListener(this);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        filterView.setVisibility(View.GONE);
        if (v == fab) {
            if (getFragmentManager() != null) {
                FragmentTransaction fm = getFragmentManager().beginTransaction();
                fm.replace(R.id.frame_setmeeting, new MeetingCreationStartFragment()).addToBackStack(null).commit();
            }
        }
    }
}
