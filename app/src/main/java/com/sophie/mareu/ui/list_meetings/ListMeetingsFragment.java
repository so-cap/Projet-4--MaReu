package com.sophie.mareu.ui.list_meetings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sophie.mareu.R;
import com.sophie.mareu.di.DI;
import com.sophie.mareu.helper.FilterAndSort;
import com.sophie.mareu.helper.ListOrder;
import com.sophie.mareu.helper.MeetingsHandler;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.ui.DetailFragment;
import com.sophie.mareu.ui.meeting_creation.MeetingCreationActivity;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sophie.mareu.ui.meeting_creation.MeetingCreationStartFragment.ARGUMENT_MEETING;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class ListMeetingsFragment extends Fragment implements View.OnClickListener,
        ListMeetingsRecyclerViewAdapter.OnDeleteMeetingListener, ListMeetingsRecyclerViewAdapter.OnMeetingClickListener {
    private ArrayList<Meeting> meetings = new ArrayList<>();
    private MeetingsHandler meetingsHandler = DI.getMeetingsHandler();
    private RecyclerView recyclerView;
    private int listCurrentState = -1;

    @BindView(R.id.no_new_meetings)
    TextView mNoNewMeetings;
    @Nullable
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.filter_activity)
    CardView filterSelectionView;
    @BindView(R.id.filter_activated)
    View filterActivatedView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list_meetings, container, false);

        bindViews();
        initRecyclerView(view);

        // If the user is in portrait mode.
        if (fab != null) {
            fab.setOnClickListener(this);
            fab.show();
        }
        return view;
    }

    private void bindViews() {
        Toolbar mainToolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.my_toolbar);
        mainToolbar.setVisibility(View.VISIBLE);

        if (getActivity() != null)
            ButterKnife.bind(this, getActivity());
    }

    private void initRecyclerView(View view) {
        Context context = view.getContext();
        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), MeetingCreationActivity.class);
        startActivity(intent);
    }

    //In case of clicking on the details of a meeting, the list will keep it's previous state.
    //If creating a new meeting, the list will be updated without any filter applied to it.
    @Override
    public void onResume() {
        super.onResume();
        filterActivatedView.setVisibility(View.VISIBLE);
        if (FilterAndSort.getFilteredList().isEmpty() && FilterAndSort.getSortedList().isEmpty())
            initList(ListOrder.UNCHANGED);
        else if (!FilterAndSort.getSortedList().isEmpty() && FilterAndSort.getFilteredList().isEmpty())
            initList(ListOrder.SORTED);
        else
            initList(ListOrder.FILTERED);
    }

    public void initList(int listCurrentState) {
        this.listCurrentState = listCurrentState;

        if (listCurrentState == ListOrder.FILTERED)
            meetings = FilterAndSort.getFilteredList();
        else if (listCurrentState == ListOrder.SORTED)
            meetings = FilterAndSort.getSortedList();
        else {
            meetings = meetingsHandler.getMeetings();
            filterActivatedView.setVisibility(View.GONE);
        }

        ListMeetingsRecyclerViewAdapter adapter = new ListMeetingsRecyclerViewAdapter(meetings);
        adapter.setOnMeetingClickListener(this);
        adapter.setOnDeleteMeetingListener(this);
        recyclerView.setAdapter(adapter);

        if (!(meetings.isEmpty())) mNoNewMeetings.setVisibility(View.GONE);
        else mNoNewMeetings.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDeleteMeetingClick(Meeting meeting) {
        meetingsHandler.deleteMeeting(meeting);
        if (meetingsHandler.getMeetings().contains(meeting))
            Toast.makeText(getContext(), "ERREUR: La réunion n'a pû étre supprimée.", Toast.LENGTH_LONG).show();

        initList(listCurrentState);
        if (meetings.isEmpty()) mNoNewMeetings.setVisibility(View.VISIBLE);

        // If the user is in landscape mode and deletes a meeting
        Fragment detailFragment = null;
        if (getFragmentManager() != null)
            detailFragment = getFragmentManager().findFragmentById(R.id.frame_setmeeting);

        // To pop only the detailFragment of the deleted meeting.
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

        filterSelectionView.setVisibility(View.GONE);
        if (getFragmentManager() != null) {
            fm = getFragmentManager().beginTransaction();

            DetailFragment detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(ARGUMENT_MEETING, meeting);
            detailFragment.setArguments(bundle);

            if (getActivity() != null)
                if (getActivity().findViewById(R.id.frame_setmeeting) == null && !getString(R.string.screen_type).equals("tablet")) {
                    fm.replace(R.id.frame_listmeetings, detailFragment).addToBackStack(null).commit();
                    filterActivatedView.setVisibility(View.GONE);
                } else
                    fm.replace(R.id.frame_setmeeting, detailFragment).addToBackStack(null).commit();
        }
    }
}