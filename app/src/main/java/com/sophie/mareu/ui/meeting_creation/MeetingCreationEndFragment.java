package com.sophie.mareu.ui.meeting_creation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.FragmentTransaction;

import com.sophie.mareu.ListMeetingsActivity;
import com.sophie.mareu.R;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.ui.MeetingsApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MeetingCreationEndFragment extends Fragment implements View.OnClickListener {
    private Meeting mMeeting;
    private String mTitle, mHour, mRoomName, mDetailSubject;
    private ArrayList<String> mParticipants = new ArrayList<>();
    private Context mContext;

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
            if (getArguments().containsKey("selected_hour"))
                mHour = getArguments().getString("selected_hour");
            if (getArguments().containsKey("selected_room"))
                mRoomName = getArguments().getString("selected_room");
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
                mEmailContainer.addView(anotherEmail);
                break;
            case R.id.btn_endsetup:
                if (checkIfValid()) {
                    if (getActivity().getClass().getSimpleName().equals("MeetingCreationActivity"))
                        getActivity().finish();
                    else {
                        ListMeetingFragment listMeetingFragment = (ListMeetingFragment) getFragmentManager()
                                .findFragmentById(R.id.frame_listmeetings);

                        if (listMeetingFragment == null) {
                            getActivity().finish();
                        } else {
                            FragmentManager fm = getFragmentManager();
                            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            //to update recyclerview
                            fm.beginTransaction().replace(R.id.frame_listmeetings, new ListMeetingFragment()).commit();
                        }
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
        mMeeting = new Meeting(mTitle, mHour, mRoomName, mParticipants, mDetailSubject);
        MeetingsApi.addMeeting(mMeeting);
    }
}
