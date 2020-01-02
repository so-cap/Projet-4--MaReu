package com.sophie.mareu.ui.list_meetings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sophie.mareu.R;
import com.sophie.mareu.controller.RoomsAvailability;
import com.sophie.mareu.event.DeleteMeetingEvent;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.api.MeetingsApi;
import com.sophie.mareu.ui.meeting_creation.MeetingCreationActivity;
import com.sophie.mareu.ui.meeting_creation.MeetingCreationEndFragment;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();

        if (mMeetings == null) {
            AlertDialog.Builder popUp = new AlertDialog.Builder(getContext());
            popUp.setTitle("Pas de nouvelles réunions");
            popUp.setMessage("yo");
            popUp.setPositiveButton("Nouvelle réu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }
            );
                    popUp.show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
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

}
