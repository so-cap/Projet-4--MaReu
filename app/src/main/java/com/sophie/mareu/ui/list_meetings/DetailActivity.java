package com.sophie.mareu.ui.list_meetings;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.sophie.mareu.R;
import com.sophie.mareu.model.Meeting;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity {
    private Meeting meeting;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (getIntent().hasExtra("meeting")) {
            meeting = getIntent().getParcelableExtra("meeting");
            initMeetingPage();
        }

    }

    private void initMeetingPage() {
        Drawable icon;
        if (meeting.getIcon() == R.drawable.ic_lightgreen)
            icon = getDrawable(R.drawable.gradient_lightgreen);
        else if (meeting.getIcon() == R.drawable.ic_darkergreen)
            icon = getDrawable(R.drawable.gradient_darkgreen);
        else
            icon = getDrawable(R.drawable.gradient_pink);

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

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
