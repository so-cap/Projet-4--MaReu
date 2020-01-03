package com.sophie.mareu.ui.meeting_creation;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sophie.mareu.R;
import com.sophie.mareu.controller.AvailabilityPerHour;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.service.MeetingsApi;
import com.sophie.mareu.service.RoomsAvailability;
import com.sophie.mareu.ui.list_meetings.ListMeetingFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MeetingCreationEndFragment extends Fragment implements View.OnClickListener {
    private String mTitle, mHour, mRoomName, mDetailSubject;
    private ArrayList<String> mParticipants = new ArrayList<>();
    private Context mContext;
    private int mRoomPosition, mHourPosition;

    @BindView(R.id.meeting_title_input)
    EditText mTitleView;
    @BindView(R.id.meeting_subjectdetail_input)
    EditText mDetailSubjectView;
    @BindView(R.id.email_one)
    EditText mEmailView;

    @BindView(R.id.email_container)
    LinearLayout mEmailContainer;
    @BindView(R.id.add_more_email)
    ImageButton mAddMoreEmail;

    @BindView(R.id.btn_endsetup)
    Button mBtnEnd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_creation_end, container, false);

        ButterKnife.bind(this, view);
        mContext = getContext();

        if (getArguments() != null) {
            mHour = getArguments().getString("selected_hour");
            mHourPosition = getArguments().getInt("hour_position");
            mRoomName = getArguments().getString("selected_room");
            mRoomPosition = getArguments().getInt("room_position");
        }

        mAddMoreEmail.setOnClickListener(this);
        mBtnEnd.setOnClickListener(this);
        return view;
    }

    private void initParticipantsList() {
        int emailsEntered = mEmailContainer.getChildCount();

        for (int i = 0; i < emailsEntered; i++) {
            if (!(((EditText) mEmailContainer.getChildAt(i)).getText().toString().isEmpty())) {
                String emailAddress;
                emailAddress = ((EditText) mEmailContainer.getChildAt(i)).getText().toString();
                mParticipants.add(emailAddress);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_more_email:
                EditText anotherEmail = new EditText(mContext);
                anotherEmail.setHint(getString(R.string.email_hint));
                anotherEmail.setTextSize(16);
                anotherEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                mEmailContainer.addView(anotherEmail);
                break;
            case R.id.btn_endsetup:
                if (checkIfValid()) {
                    if (getActivity().getClass().equals(MeetingCreationActivity.class))
                        getActivity().finish();
                    else {
                        Fragment listMeetingFragment = getFragmentManager().
                                findFragmentById(R.id.frame_listmeetings);

                        if (listMeetingFragment == null) {
                            getActivity().finish();
                        } else {
                            FragmentManager fm = getFragmentManager();
                            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            //to update recyclerview
                            fm.beginTransaction().replace(R.id.frame_listmeetings, new ListMeetingFragment()).commit();
                        }
                        break;
                    }
                }
        }
    }

    private boolean checkIfValid() {
        mTitle = mTitleView.getText().toString();
        mDetailSubject = mDetailSubjectView.getText().toString();
        initParticipantsList();

        if (mTitle.isEmpty() || mParticipants.isEmpty()) {
            if (mTitle.isEmpty())
                mTitleView.setError("Remplissez ce champs");
            if (mParticipants.isEmpty()) {
                mEmailView.setError("Remplissez ce champs");
            }
            return false;
        }
        setMeeting();
        return true;
    }

    private void setMeeting() {
        Meeting meeting = new Meeting(mTitle, mHour, mRoomName, mParticipants, mDetailSubject);
        MeetingsApi.addMeeting(meeting);
        updateRoomAvailability();
    }

    private void updateRoomAvailability() {
        ArrayList<AvailabilityPerHour> roomsPerHour = RoomsAvailability.getAvailableRoomsPerHour();
        roomsPerHour.get(mHourPosition).getRooms().remove(mRoomPosition);
        RoomsAvailability.updateAvailableHours(roomsPerHour);
    }
}
