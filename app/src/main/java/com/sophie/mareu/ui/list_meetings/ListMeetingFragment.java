package com.sophie.mareu.ui.list_meetings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

import static com.sophie.mareu.Constants.*;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class ListMeetingFragment extends Fragment implements View.OnClickListener,
        ListMeetingsRecyclerViewAdapter.OnDeleteMeetingListener, ListMeetingsRecyclerViewAdapter.OnMeetingClickListener {
    private ArrayList<Meeting> mMeetings = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Context context;
    private int listCurrentState = -1;

    @BindView(R.id.no_new_meetings)
    TextView mNoNewMeetings;
    @Nullable
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.filter_activated)
    View filterActivatedView;
    @BindView(R.id.filter_activity)
    CardView mFilterView;

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

        if (getActivity() != null)
            ButterKnife.bind(this, getActivity());

        if (mFab != null) {
            mFab.setOnClickListener(this);
            mFab.show();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        filterActivatedView.setVisibility(View.GONE);
        if (FilterAndSort.getFilteredList().isEmpty() && FilterAndSort.getSortedList().isEmpty())
            initList(UNCHANGED);
        else if(!FilterAndSort.getSortedList().isEmpty() && FilterAndSort.getFilteredList().isEmpty())
            initList(SORTED);
        else
            initList(FILTERED);
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
        else {
            mMeetings = AvailabilityByDate.getMeetings();
            filterActivatedView.setVisibility(View.GONE);
        }

        mRecyclerView.setAdapter(new ListMeetingsRecyclerViewAdapter(mMeetings));
        ((ListMeetingsRecyclerViewAdapter) Objects.requireNonNull(mRecyclerView.getAdapter())).setOnDeleteMeetingListener(this);
        ((ListMeetingsRecyclerViewAdapter) mRecyclerView.getAdapter()).setOnMeetingClickListener(this);

        if (!(mMeetings.isEmpty())) mNoNewMeetings.setVisibility(View.GONE);
        else mNoNewMeetings.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDeleteMeetingClick(Meeting meeting) {
        AvailabilityByDate.deleteMeeting(meeting);
        initList(listCurrentState);
        if (mMeetings.isEmpty()) mNoNewMeetings.setVisibility(View.VISIBLE);

        Fragment detailFragment = null;
        if (getFragmentManager() != null)
            detailFragment = getFragmentManager().findFragmentById(R.id.frame_setmeeting);

        // To hide only the detailview of the deleted meeting.
        if (detailFragment != null && detailFragment.getClass() == DetailFragment.class)
            if (detailFragment.getFragmentManager() != null) {
                if (detailFragment.getArguments() != null
                        && Objects.equals(detailFragment.getArguments().getParcelable(ARGUMENT_MEETING), meeting))
                    detailFragment.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
    }

    @Override
    public void onMeetingClick(Meeting meeting) {
        FragmentTransaction fm;
        AppCompatActivity activity = (AppCompatActivity) context;

        filterActivatedView.setVisibility(View.GONE);
        mFilterView.setVisibility(View.GONE);

        if (getFragmentManager() != null) {
            fm = getFragmentManager().beginTransaction();

            DetailFragment detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(ARGUMENT_MEETING, meeting);
            detailFragment.setArguments(bundle);

            if (activity!= null)
            if (activity.findViewById(R.id.frame_setmeeting) == null && !getString(R.string.screen_type).equals("tablet")) {
                fm.replace(R.id.frame_listmeetings, detailFragment).addToBackStack(null).commit();
            } else fm.replace(R.id.frame_setmeeting, detailFragment).addToBackStack(null).commit();
        }
    }
}