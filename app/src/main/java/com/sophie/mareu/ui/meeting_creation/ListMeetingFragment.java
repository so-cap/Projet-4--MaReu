package com.sophie.mareu.ui.meeting_creation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sophie.mareu.R;
import com.sophie.mareu.RoomsAvailability;
import com.sophie.mareu.event.DeleteMeetingEvent;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.ui.MeetingsApi;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class ListMeetingFragment extends Fragment {
    private RoomsAvailability mRoomsAvailability = new RoomsAvailability();
    private MeetingCreationEndFragment mMeetingCreationEndFragment = new MeetingCreationEndFragment();
    private ArrayList<Meeting> mMeetings = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Context context;
    private FloatingActionButton mFab;

    private static final String TAG = "ListMeetingFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list_meetings, container, false);

        mRoomsAvailability.initRoomsAndHours();
        context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        mFab = getActivity().findViewById(R.id.fab);
        if(mFab != null){
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MeetingCreationActivity.class);
                    startActivity(intent);
                }
            });
        }
        return view;
    }

    private void initList() {
        mMeetings = MeetingsApi.getMeetingsList();
        mRecyclerView.setAdapter(new ListMeetingsRecyclerViewAdapter(mMeetings, context));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        if (mFab != null)
            mFab.show();

        Log.d(TAG, "LOGGonStart: START");
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();

        Log.d(TAG, "LOGGonResume:  RESUME");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "LOGGonPause: PAUSE");
    }

    @Subscribe
    public void onDeleteMeeting (DeleteMeetingEvent event){
        MeetingsApi.deleteMeeting(event.meeting);
        initList();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "LOGGonDestroy: DESTROY");
    }
}
