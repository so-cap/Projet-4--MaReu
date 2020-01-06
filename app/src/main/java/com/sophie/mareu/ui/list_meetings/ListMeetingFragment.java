package com.sophie.mareu.ui.list_meetings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sophie.mareu.R;
import com.sophie.mareu.service.AvailabilityByDate;
import com.sophie.mareu.service.RoomsAvailabilityService;
import com.sophie.mareu.event.DeleteMeetingEvent;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.service.MeetingsService;
import com.sophie.mareu.ui.meeting_creation.MeetingCreationActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class ListMeetingFragment extends Fragment implements View.OnClickListener {
    private ArrayList<Meeting> mMeetings = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Context context;
    private FloatingActionButton mFab;
    private TextView mNoNewMeetings;
    private Date mSelectedDate = Calendar.getInstance().getTime();
    private boolean mFiltered = false;

    private static final String TAG = "LOGGListMeetingFragment";

    // to get data when in portrait mode
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK)
                if (data != null)
                    mSelectedDate = (Date) data.getSerializableExtra("selected_date");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list_meetings, container, false);

        context = view.getContext();

        AvailabilityByDate.getRoomsAvailabilityService(mSelectedDate);

        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        if (getActivity() != null) {
            mNoNewMeetings = getActivity().findViewById(R.id.no_new_meetings);
            mFab = getActivity().findViewById(R.id.fab);
        }

        if (mFab != null) mFab.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), MeetingCreationActivity.class);
        startActivityForResult(intent, 1);
    }

    private void initList() {
        if (!mFiltered) mMeetings = AvailabilityByDate.getMeetings(mSelectedDate);
        else mMeetings = AvailabilityByDate.getFilteredList();
        mRecyclerView.setAdapter(new ListMeetingsRecyclerViewAdapter(mMeetings, context));

        if (!(mMeetings.isEmpty())) mNoNewMeetings.setVisibility(View.GONE);
        else mNoNewMeetings.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mSelectedDate = (Date) getArguments().getSerializable("selected_date");
            mFiltered = getArguments().getBoolean("filter_state");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        AvailabilityByDate.deleteMeeting(event.meeting);
        initList();
        if (mMeetings.isEmpty()) mNoNewMeetings.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}