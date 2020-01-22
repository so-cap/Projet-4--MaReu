package com.sophie.mareu.view.meeting_creation;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.sophie.mareu.R;
import com.sophie.mareu.controller.FilterAndSort;
import com.sophie.mareu.model.RoomsPerHour;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.service.MeetingsApiServiceImpl;
import com.sophie.mareu.service.RoomsAvailabilityServiceImpl;
import com.sophie.mareu.service.RoomsAvailabilityApiService;
import com.sophie.mareu.view.list_meetings.ListMeetingsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sophie.mareu.Constants.*;

public class MeetingCreationEndFragment extends Fragment implements View.OnClickListener {
    private String title, subject;
    private ArrayList<String> participants = new ArrayList<>();
    private RoomsAvailabilityApiService service;
    private Meeting meeting;
    private int hourPosition;

    @BindView(R.id.meeting_title_input)
    EditText titleView;
    @BindView(R.id.meeting_subject_input)
    EditText subjectView;
    @BindView(R.id.email_one)
    EditText emailView;
    @BindView(R.id.meeting_end_toolbar)
    Toolbar toolbar;

    @BindView(R.id.email_container)
    LinearLayout emailContainer;
    @BindView(R.id.add_more_email)
    ImageButton addMoreEmail;
    @BindView(R.id.delete_mail)
    ImageButton deleteEmail;
    @BindView(R.id.save_meeting_btn)
    Button saveMeetingBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_creation_end, container, false);

        ButterKnife.bind(this, view);

        if (getActivity() != null)
            toolbar.setNavigationOnClickListener(v -> getActivity().getSupportFragmentManager()
                    .popBackStack());

        if (getArguments() != null) {
            meeting = getArguments().getParcelable(ARGUMENT_MEETING);
            service = (RoomsAvailabilityServiceImpl) getArguments().getSerializable(ARGUMENT_SERVICE);
            hourPosition = getArguments().getInt(ARGUMENT_HOUR_POSITION);
        }

        setTextChangedListener();
        addMoreEmail.setOnClickListener(this);
        saveMeetingBtn.setOnClickListener(this);
        deleteEmail.setOnClickListener(this);
        return view;
    }

    private void setTextChangedListener() {
        titleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) saveMeetingBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_more_email:
                addEmailView();
                break;
            case R.id.delete_mail:
                deleteEmailView();
                break;
            case R.id.save_meeting_btn:
                if (checkIfValid())
                    backToHomePage();
                break;
        }
    }

    private void addEmailView() {
        EditText anotherEmail = new EditText(getContext());

        anotherEmail.setHint(getString(R.string.email_hint));
        anotherEmail.setTextSize(20);
        anotherEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        anotherEmail.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        emailContainer.addView(anotherEmail);
        deleteEmail.setVisibility(View.VISIBLE);

        if (emailContainer.getChildCount() == 5)
            addMoreEmail.setVisibility(View.INVISIBLE);
    }

    private void deleteEmailView() {
        int emailViews = emailContainer.getChildCount();
        if (emailViews > 1)
            emailContainer.removeViewAt(emailViews - 1);
        if (emailViews == 3)
            deleteEmail.setVisibility(View.GONE);
        if (emailViews < 6)
            addMoreEmail.setVisibility(View.VISIBLE);
    }

    private boolean checkIfValid() {
        title = titleView.getText().toString();
        subject = subjectView.getText().toString();
        initParticipantsList();

        if ((!emailChecker()) || (title.isEmpty() || participants.isEmpty()) || subject.isEmpty()) {
            if (title.isEmpty())
                titleView.setError(getResources().getString(R.string.write_in_this_area));
            if (participants.isEmpty())
                emailView.setError(getResources().getString(R.string.write_in_this_area));
            if (subject.isEmpty())
                subjectView.setError(getResources().getString(R.string.write_in_this_area));
            return false;
        }
        setMeeting();
        return true;
    }

    private boolean emailChecker() {
        int emailsNbr = emailContainer.getChildCount();
        int errors = 0;
        EditText emailView;

        for (int position = 0; position < emailsNbr; position++) {
            emailView = (EditText) emailContainer.getChildAt(position);
            String emptyView = emailView.getText().toString();
            if (!(Patterns.EMAIL_ADDRESS.matcher(emailView.getText().toString())).matches() && (!(emptyView.isEmpty()))) {
                emailView.setError(getResources().getString(R.string.invalid_email));
                participants.clear();
                errors++;
            }
        }
        return errors == 0;
    }

    private void initParticipantsList() {
        int emailsEntered = emailContainer.getChildCount();

        for (int i = 0; i < emailsEntered; i++) {
            if (!(((EditText) emailContainer.getChildAt(i)).getText().toString().isEmpty())) {
                String emailAddress;
                emailAddress = ((EditText) emailContainer.getChildAt(i)).getText().toString();
                participants.add(emailAddress);
            }
        }
    }

    private void backToHomePage() {
        FragmentActivity activity = getActivity();
        FilterAndSort.getFilteredList().clear();
        FilterAndSort.getSortedList().clear();

        if (activity != null) {
            if (activity.getClass().equals(MeetingCreationActivity.class)) {
                activity.finish();
            } else {
                ListMeetingsFragment listMeetingsFragment = (ListMeetingsFragment) activity.getSupportFragmentManager().
                        findFragmentById(R.id.frame_listmeetings);
                FragmentManager fm = activity.getSupportFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                if (listMeetingsFragment != null)
                    listMeetingsFragment.initList(UNCHANGED);
            }
        }
        Toast.makeText(getContext(), getResources().getString(R.string.meeting_saved), Toast.LENGTH_LONG).show();
    }

    private void setMeeting() {
        meeting.setTitle(title);
        meeting.setParticipants(participants);
        meeting.setSubject(subject);
        MeetingsApiServiceImpl.addMeeting(meeting, meeting.getDate());
        updateRoomAvailability();
    }

    private void updateRoomAvailability() {
        ArrayList<RoomsPerHour> roomsPerHour = service.getRoomsPerHourList();
        roomsPerHour.get(hourPosition).getRooms().remove(meeting.getRoomName());
        service.updateAvailableHours(roomsPerHour);
        MeetingsApiServiceImpl.updateAvailabilityByDate(meeting.getDate(), service);
    }
}
