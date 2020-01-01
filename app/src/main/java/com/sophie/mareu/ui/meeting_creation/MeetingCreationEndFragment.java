package com.sophie.mareu.ui.meeting_creation;

import android.content.Context;
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
import androidx.fragment.app.FragmentTransaction;

import com.sophie.mareu.R;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.ui.MeetingsApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MeetingCreationEndFragment extends Fragment implements View.OnClickListener {
    private Meeting mMeeting;
    private String mTitle, mHour, mRoomName, mDetailSubject;
    private ArrayList<String> mParticipants;
    private Context mContext;

    @BindView(R.id.meeting_title_input)
    EditText mTitleView;
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

        if (this.getArguments() != null) {
            if (this.getArguments().containsKey("selected_hour"))
                mHour = this.getArguments().getString("selected_hour");
            if (this.getArguments().containsKey("selected_room"))
                mRoomName = this.getArguments().getString("selected_room");
        }

        mAddMoreEmail.setOnClickListener(this);
        mBtnEnd.setOnClickListener(this);

        initParticipantsList();
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
                    setMeeting();
                    if (getActivity().getClass().getSimpleName().equals("ListMeetingsActivity"))
                        getActivity().finish();
                    else {
                        FragmentTransaction fm = getFragmentManager().beginTransaction();
                        fm.replace(R.id.frame_setmeeting, new HomeStartMeetingCreationFragment());
                        fm.commit();
                    }
                }

        }
    }

    private boolean checkIfValid() {
        if (mTitle.isEmpty()){
            mTitleView.setError("Remplissez ce champs");
            return false;
        }
        if (mParticipants.isEmpty()) {
            mEmailView.setError("Remplissez ce champs");
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
