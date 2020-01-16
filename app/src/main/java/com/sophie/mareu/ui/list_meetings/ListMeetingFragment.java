package com.sophie.mareu.ui.list_meetings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sophie.mareu.R;
import com.sophie.mareu.controller.FilterAndSort;
import com.sophie.mareu.controller.AvailabilityByDate;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.ui.meeting_creation.MeetingCreationActivity;

import java.util.ArrayList;
import java.util.Objects;

import static com.sophie.mareu.ui.list_meetings.ListMeetingsActivity.FILTERED;
import static com.sophie.mareu.ui.list_meetings.ListMeetingsActivity.SORTED;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class ListMeetingFragment extends Fragment implements View.OnClickListener, ListMeetingsRecyclerViewAdapter.OnDeleteMeetingListener {
    private ArrayList<Meeting> mMeetings = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Context context;
    private FloatingActionButton mFab;
    private TextView mNoNewMeetings;
    private int listCurrentState = -1;

    private static final String TAG = "LOGGListMeetingFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list_meetings, container, false);

        Toolbar mainToolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.my_toolbar);
        mainToolbar.setVisibility(View.VISIBLE);
        context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        if (getActivity() != null) {
            mNoNewMeetings = getActivity().findViewById(R.id.no_new_meetings);
            mFab = getActivity().findViewById(R.id.fab);
        }
        if (mFab != null) {
            mFab.setOnClickListener(this);
            mFab.show();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initList(listCurrentState);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), MeetingCreationActivity.class);
        startActivity(intent);
    }

    public void initList(int listCurrentState) {
        this.listCurrentState = listCurrentState;

        if (listCurrentState == FILTERED)
            mMeetings = FilterAndSort.getFilteredList();
        else if (listCurrentState == SORTED)
            mMeetings = FilterAndSort.getSortedList();
        else
            mMeetings = AvailabilityByDate.getMeetings();
        mRecyclerView.setAdapter(new ListMeetingsRecyclerViewAdapter(mMeetings, context, this));

        if (!(mMeetings.isEmpty())) mNoNewMeetings.setVisibility(View.GONE);
        else mNoNewMeetings.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDeleteMeetingClick(Meeting meeting) {
        AvailabilityByDate.deleteMeeting(meeting);
        initList(listCurrentState);
        if (mMeetings.isEmpty()) mNoNewMeetings.setVisibility(View.VISIBLE);
    }
}