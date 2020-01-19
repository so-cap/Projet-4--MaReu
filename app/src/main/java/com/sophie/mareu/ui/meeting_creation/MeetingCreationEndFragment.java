package com.sophie.mareu.ui.meeting_creation;

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
import com.sophie.mareu.controller.RoomsPerHour;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.controller.AvailabilityByDate;
import com.sophie.mareu.service.RoomsAvailabilityByHourImpl;
import com.sophie.mareu.service.RoomsAvailabilityService;
import com.sophie.mareu.ui.list_meetings.ListMeetingFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sophie.mareu.Constants.*;

public class MeetingCreationEndFragment extends Fragment implements View.OnClickListener {
    private String mTitle, mDetailSubject;
    private ArrayList<String> mParticipants = new ArrayList<>();
    private RoomsAvailabilityService mService;
    private Meeting mMeeting;
    private int mHourPosition;

    @BindView(R.id.meeting_title_input)
    EditText mTitleView;
    @BindView(R.id.meeting_subjectdetail_input)
    EditText mDetailSubjectView;
    @BindView(R.id.email_one)
    EditText mEmailView;
    @BindView(R.id.meeting_end_toolbar)
    Toolbar toolbar;

    @BindView(R.id.email_container)
    LinearLayout mEmailContainer;
    @BindView(R.id.add_more_email)
    ImageButton mAddMoreEmail;
    @BindView(R.id.delete_mail)
    ImageButton mDeleteEmail;
    @BindView(R.id.save_meeting_btn)
    Button mBtnEnd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_creation_end, container, false);

        ButterKnife.bind(this, view);

        if (getActivity() != null)
            toolbar.setNavigationOnClickListener(v -> getActivity().getSupportFragmentManager()
                    .popBackStack());

        if (getArguments() != null) {
            mMeeting = getArguments().getParcelable(ARGUMENT_MEETING);
            mService = (RoomsAvailabilityByHourImpl) getArguments().getSerializable(ARGUMENT_SERVICE);
            mHourPosition = getArguments().getInt(ARGUMENT_HOUR_POSITION);
        }

        setTextChangedListener();
        mAddMoreEmail.setOnClickListener(this);
        mBtnEnd.setOnClickListener(this);
        mDeleteEmail.setOnClickListener(this);
        return view;
    }

    private void setTextChangedListener() {
        mTitleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) mBtnEnd.setVisibility(View.VISIBLE);
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
        mEmailContainer.addView(anotherEmail);
        mDeleteEmail.setVisibility(View.VISIBLE);

        if (mEmailContainer.getChildCount() == 5)
            mAddMoreEmail.setVisibility(View.INVISIBLE);
    }

    private void deleteEmailView() {
        int emailViews = mEmailContainer.getChildCount();
        if (emailViews > 1)
            mEmailContainer.removeViewAt(emailViews - 1);
        if (emailViews == 3)
            mDeleteEmail.setVisibility(View.GONE);
        if (emailViews < 6)
            mAddMoreEmail.setVisibility(View.VISIBLE);
    }

    private boolean checkIfValid() {
        mTitle = mTitleView.getText().toString();
        mDetailSubject = mDetailSubjectView.getText().toString();
        initParticipantsList();

        if ((!emailChecker()) || (mTitle.isEmpty() || mParticipants.isEmpty()) || mDetailSubject.isEmpty()) {
            if (mTitle.isEmpty())
                mTitleView.setError(getResources().getString(R.string.write_in_this_area));
            if (mParticipants.isEmpty())
                mEmailView.setError(getResources().getString(R.string.write_in_this_area));
            if (mDetailSubject.isEmpty())
                mDetailSubjectView.setError(getResources().getString(R.string.write_in_this_area));
            return false;
        }
        setMeeting();
        return true;
    }

    private boolean emailChecker() {
        int emailsNbr = mEmailContainer.getChildCount();
        int errors = 0;
        EditText emailView;

        for (int position = 0; position < emailsNbr; position++) {
            emailView = (EditText) mEmailContainer.getChildAt(position);
            String emptyView = emailView.getText().toString();
            if (!(Patterns.EMAIL_ADDRESS.matcher(emailView.getText().toString())).matches() && (!(emptyView.isEmpty()))) {
                emailView.setError(getResources().getString(R.string.invalid_email));
                mParticipants.clear();
                errors++;
            }
        }
        return errors == 0;
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

    private void backToHomePage() {
        FragmentActivity activity = getActivity();
        FilterAndSort.getFilteredList().clear();
        FilterAndSort.getSortedList().clear();

        if (activity != null) {
            if (activity.getClass().equals(MeetingCreationActivity.class)) {
                activity.finish();
            } else {
                ListMeetingFragment listMeetingFragment = (ListMeetingFragment) activity.getSupportFragmentManager().
                        findFragmentById(R.id.frame_listmeetings);
                FragmentManager fm = activity.getSupportFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                if (listMeetingFragment != null)
                    listMeetingFragment.initList(UNCHANGED);
            }
        }
        Toast.makeText(getContext(), getResources().getString(R.string.meeting_saved), Toast.LENGTH_LONG).show();
    }

    private void setMeeting() {
        mMeeting.setTitle(mTitle);
        mMeeting.setParticipants(mParticipants);
        mMeeting.setSubject(mDetailSubject);
        AvailabilityByDate.addMeeting(mMeeting, mMeeting.getDate());
        updateRoomAvailability();
    }

    private void updateRoomAvailability() {
        ArrayList<RoomsPerHour> roomsPerHour = mService.getRoomsPerHourList();
        roomsPerHour.get(mHourPosition).getRooms().remove(mMeeting.getRoomName());
        mService.updateAvailableHours(roomsPerHour);
        AvailabilityByDate.updateAvailabilityByDate(mMeeting.getDate(), mService);
    }
}
