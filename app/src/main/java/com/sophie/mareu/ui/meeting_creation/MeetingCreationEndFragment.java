package com.sophie.mareu.ui.meeting_creation;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.sophie.mareu.R;
import com.sophie.mareu.controller.RoomsPerHour;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.controller.AvailabilityByDate;
import com.sophie.mareu.service.RoomsAvailabilityByHourImpl;
import com.sophie.mareu.ui.list_meetings.ListMeetingFragment;
import com.sophie.mareu.service.RoomsAvailabilityService;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sophie.mareu.ui.list_meetings.ListMeetingsActivity.UNCHANGED;


public class MeetingCreationEndFragment extends Fragment implements View.OnClickListener {
    private String mTitle, mRoomName, mDetailSubject;
    private AbstractMap.SimpleEntry<Integer, String> mHour;
    private ArrayList<String> mParticipants = new ArrayList<>();
    private Context mContext;
    private int mRoomPosition, mHourPosition;
    private RoomsAvailabilityService mService;
    private Date mSelectedDate;

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
    @BindView(R.id.delete_mail)
    ImageButton mDeleteEmail;

    @BindView(R.id.btn_endsetup)
    Button mBtnEnd;

    private static final String TAG = "LOGGCreationEndFragm";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_creation_end, container, false);

        ButterKnife.bind(this, view);
        mContext = getContext();

        if (getArguments() != null) {
            int key = (getArguments().getInt("selected_hour_key"));
            String hourValue = (getArguments().getString("selected_hour_value"));

            mHour = new AbstractMap.SimpleEntry<>(key, hourValue);
            mRoomName = getArguments().getString("selected_room");
            mHourPosition = getArguments().getInt("hour_position");
            mRoomPosition = getArguments().getInt("room_position");
            mSelectedDate = (Date) getArguments().getSerializable("selected_date");
            mService = (RoomsAvailabilityByHourImpl) getArguments().
                    getSerializable("rooms_availability_service");
        }

        mAddMoreEmail.setOnClickListener(this);
        mBtnEnd.setOnClickListener(this);
        mDeleteEmail.setOnClickListener(this);
        return view;
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
            case R.id.btn_endsetup:
                if (checkIfValid())
                    backToHomePage();
                break;
        }
    }

    private void addEmailView(){
        EditText anotherEmail = new EditText(mContext);

        anotherEmail.setHint(getString(R.string.email_hint));
        anotherEmail.setTextSize(20);
        anotherEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        anotherEmail.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mEmailContainer.addView(anotherEmail);
        mDeleteEmail.setVisibility(View.VISIBLE);

        if(mEmailContainer.getChildCount() == 5)
            mAddMoreEmail.setVisibility(View.INVISIBLE);
    }

    private void deleteEmailView(){
        int emailViews = mEmailContainer.getChildCount();
        if (emailViews > 1)
            mEmailContainer.removeViewAt(emailViews - 1);
        if (emailViews == 2)
            mDeleteEmail.setVisibility(View.GONE);
        if (emailViews < 6)
            mAddMoreEmail.setVisibility(View.VISIBLE);
    }

    private boolean checkIfValid() {
        mTitle = mTitleView.getText().toString();
        mDetailSubject = mDetailSubjectView.getText().toString();
        initParticipantsList();

        if ((!emailChecker()) || (mTitle.isEmpty() || mParticipants.isEmpty())) {
            if (mTitle.isEmpty())
                mTitleView.setError("Veuillez remplir ce champs");
            if (mParticipants.isEmpty())
                mEmailView.setError("Veuillez remplir ce champs");
            return false;
        }
        setMeeting();
        return true;
    }

    private boolean emailChecker(){
        int emailsNbr = mEmailContainer.getChildCount();
        int errors = 0;
        EditText emailView;

        for (int position = 0; position < emailsNbr; position++){
            emailView = (EditText) mEmailContainer.getChildAt(position);
            String emptyView = emailView.getText().toString();
            if (!(Patterns.EMAIL_ADDRESS.matcher(emailView.getText().toString())).matches() && (!(emptyView.isEmpty()))){
                emailView.setError("Addresse email invalide !");
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
        if (activity != null) {
            if (activity.getClass().equals(MeetingCreationActivity.class)) {
                activity.finish();
            } else {
                ListMeetingFragment listMeetingFragment = (ListMeetingFragment) activity.getSupportFragmentManager().
                        findFragmentById(R.id.frame_listmeetings);
              /*  if (listMeetingFragment == null) {
                    activity.finish();
                } else {
               */
                    FragmentManager fm = activity.getSupportFragmentManager();
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                if (listMeetingFragment != null) {
                    listMeetingFragment.initList(UNCHANGED);
                }
                //}
            }
        }
        Toast.makeText(mContext,"Réunion enregistrée !", Toast.LENGTH_LONG).show();
    }

    private void setMeeting() {
        Meeting meeting = new Meeting(mTitle, mHour, mRoomName, mParticipants, mDetailSubject, mSelectedDate);
        AvailabilityByDate.addMeeting(meeting, mSelectedDate);
        updateRoomAvailability();
    }

    private void updateRoomAvailability() {
        ArrayList<RoomsPerHour> roomsPerHour = mService.getRoomsPerHourList();
        roomsPerHour.get(mHourPosition).getRooms().remove(mRoomPosition);
        mService.updateAvailableHours(roomsPerHour);
        AvailabilityByDate.updateAvailabilityByDate(mSelectedDate, mService);
    }
}
