package com.sophie.mareu.ui.meeting_list;

import android.content.Context;
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

import com.sophie.mareu.R;
import com.sophie.mareu.RoomsAvailability;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.ui.MeetingsApi;
import com.sophie.mareu.ui.meeting_creation_fragments.MeetingCreationEnd;

import java.util.ArrayList;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class MeetingFragment extends Fragment {
    private RoomsAvailability mRoomsAvailability = new RoomsAvailability();
    private MeetingCreationEnd mMeetingCreationEnd = new MeetingCreationEnd();
    private ArrayList<Meeting> mMeetings = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Context context;

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

        initList();
        return view;
    }

    private void initList(){
        mMeetings = MeetingsApi.getMeetingsList();
        mRecyclerView.setAdapter(new ListMeetingsRecyclerViewAdapter(mMeetings, context));
    }

    @Override
    public void onStart() {
        super.onStart();
        initList();
    }
}
