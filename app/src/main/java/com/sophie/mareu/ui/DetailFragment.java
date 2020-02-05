package com.sophie.mareu.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sophie.mareu.R;
import com.sophie.mareu.model.Meeting;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sophie.mareu.ui.meeting_creation.MeetingCreationStartFragment.ARGUMENT_MEETING;

public class DetailFragment extends Fragment {
    private Meeting meeting;
    private FragmentActivity activity;

    @BindView(R.id.detail_date_hour)
    TextView dateAndHour;
    @BindView(R.id.detail_meeting_title)
    TextView meetingTitle;
    @BindView(R.id.detail_room)
    TextView room;
    @BindView(R.id.detail_subject)
    TextView subject;
    @BindView(R.id.detail_emails)
    TextView participantsEmails;
    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        displayMainToolbar();

        if (getArguments() != null) {
            meeting = getArguments().getParcelable(ARGUMENT_MEETING);
            initMeetingPage();
        }
        toolbar.setNavigationOnClickListener(v -> activity.getSupportFragmentManager()
                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE));
        return view;
    }

    private void displayMainToolbar() {
        activity = getActivity();
        if (activity != null) {
            Toolbar mainToolbar = activity.findViewById(R.id.my_toolbar);
            FloatingActionButton fabMain = activity.findViewById(R.id.fab);
            if (fabMain != null) {
                fabMain.hide();
                mainToolbar.setVisibility(View.GONE);
            } else
                mainToolbar.setVisibility(View.VISIBLE);
        }
    }

    private void initMeetingPage() {
        Drawable icon;
        if (meeting.getIcon() == R.drawable.ic_lightgreen)
            icon = activity.getDrawable(R.drawable.gradient_lightgreen);
        else if (meeting.getIcon() == R.drawable.ic_darkergreen)
            icon = activity.getDrawable(R.drawable.gradient_darkgreen);
        else
            icon = activity.getDrawable(R.drawable.gradient_pink);

        toolbar.setBackground(icon);
        dateAndHour.setText(getString(R.string.detail_date_hour, meeting.getDateInStringFormat(meeting.getDate()),
                meeting.getHour().getValue()));
        meetingTitle.setText(meeting.getTitle());
        room.setText(getString(R.string.detail_room, meeting.getRoomName()));
        subject.setText(meeting.getSubject());

        StringBuilder emailsList = new StringBuilder();
        for (String emails : meeting.getParticipantsArray()) {
            emailsList.append(emails).append("\n");
        }
        participantsEmails.setText(emailsList);
    }
}
