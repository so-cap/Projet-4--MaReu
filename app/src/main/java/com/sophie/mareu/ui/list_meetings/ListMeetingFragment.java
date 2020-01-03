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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sophie.mareu.R;
import com.sophie.mareu.service.RoomsAvailability;
import com.sophie.mareu.event.DeleteMeetingEvent;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.service.MeetingsApi;
import com.sophie.mareu.ui.meeting_creation.MeetingCreationActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class ListMeetingFragment extends Fragment {
    private ArrayList<Meeting> mMeetings = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Context context;
    private FloatingActionButton mFab;
    private TextView mNoNewMeetings;
    RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list_meetings, container, false);

        RoomsAvailability.initRoomsAndHours();
        context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        if (getActivity() != null) {
            mNoNewMeetings = getActivity().findViewById(R.id.no_new_meetings);
            mFab = getActivity().findViewById(R.id.fab);
        }

        if(mFab != null){
            mFab.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), MeetingCreationActivity.class);
                startActivity(intent);
            });
        }
        return view;
    }

    private void initList() {
        adapter = new ListMeetingsRecyclerViewAdapter(mMeetings, context);
        mMeetings = MeetingsApi.getMeetingsList();
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();


        if(!(mMeetings.isEmpty()))
            mNoNewMeetings.setVisibility(View.GONE);
    }

    @Subscribe
    public void onDeleteMeeting (DeleteMeetingEvent event){
        MeetingsApi.deleteMeeting(event.meeting);
        initList();

        if(mMeetings.isEmpty())
            mNoNewMeetings.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
