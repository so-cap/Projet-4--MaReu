package com.sophie.mareu.ui.meeting_creation_fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sophie.mareu.R;
import com.sophie.mareu.RoomsAvailability;
import com.sophie.mareu.RoomsAvailability.AvailabilityPerHour;
import com.sophie.mareu.model.Meeting;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingCreationStartFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private ArrayList<AvailabilityPerHour> mAvailableHoursAndRooms;
    private Meeting mMeeting = new Meeting();
    private ArrayList<String> mSpinnerArray;
    private ArrayList<String> mParticipants = new ArrayList<>();
    private String mSelectedRoomName, mSelectedHour;
    private int mHourPosition;
    private Context mContext;
    private FloatingActionButton mFab;

    private static final String TAG = "MeetingCreationStartFra";

    @BindView(R.id.spinner_hour)
    Spinner mSpinner;

    @BindView(R.id.linearlayout_column1)
    LinearLayout mRadioGrpColumn1;
    @BindView(R.id.linearlayout_column2)
    LinearLayout mRadioGrpColumn2;
    @BindView(R.id.linearlayout_column3)
    LinearLayout mRadioGrpColumn3;
    @BindView(R.id.radio_group_rooms)
    RadioGroup mRadioGroup;

    @BindView(R.id.email_container)
    LinearLayout mEmailContainer;
    @BindView(R.id.add_more_email)
    ImageButton mAddMoreEmail;
    @BindView(R.id.next_page)
    Button mNextPage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_creation_start, container, false);

        mContext = getContext();
        ButterKnife.bind(this, view);

        mFab = getActivity().findViewById(R.id.fab);
        mFab.hide();

        initSpinner();
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mHourPosition = position;
                    mSelectedHour = mAvailableHoursAndRooms.get(mHourPosition).getHour() + "h00";
                    initRadioGroup();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mAddMoreEmail.setOnClickListener(this);
        mNextPage.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);

        initParticipantsList();
        return view;
    }

    private void initParticipantsList() {
        int emailsEntered = mEmailContainer.getChildCount();

        for (int i = 0; i < emailsEntered; i++ ){
            if(!(((EditText)mEmailContainer.getChildAt(i)).getText().toString().isEmpty())) {
                String emailAddress;
                emailAddress =((EditText)mEmailContainer.getChildAt(i)).getText().toString();
                mParticipants.add(emailAddress);
            }
            Log.d(TAG, "initParticipantsList: " + mParticipants.toString());
        }
    }

    public void initSpinner() {
        mAvailableHoursAndRooms = RoomsAvailability.getAvailableRoomsPerHour();
        mSpinnerArray = new ArrayList<>();
        String mHour;

        for (int position = 0; position < mAvailableHoursAndRooms.size(); position++) {
            mHour = Integer.toString(mAvailableHoursAndRooms.get(position).getHour());
            mSpinnerArray.add(mHour + "h00");
        }
        displaySpinner(mSpinnerArray);
    }

    private void displaySpinner(ArrayList<String> spinnerArray) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, spinnerArray);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
    }

    private void initRadioGroup() {
        int roomsAvailable = mAvailableHoursAndRooms.get(mHourPosition).getRooms().size();
        int i = 0;

        clearChildViews();

        while(i < roomsAvailable){
            while(i < 4){
                RadioButton radioButton = new RadioButton(mContext);
                radioButton.setId(i);
                radioButton.setText(mAvailableHoursAndRooms.get(mHourPosition).getRooms().get(i));
                mRadioGrpColumn1.addView(radioButton);
                i++;
            }
            while (i >= 4 && i < 8){
                RadioButton radioButton = new RadioButton(mContext);
                radioButton.setId(i);
                radioButton.setText(mAvailableHoursAndRooms.get(mHourPosition).getRooms().get(i));
                mRadioGrpColumn2.addView(radioButton);
                i++;
            }
            while (i >= 8 && i < roomsAvailable ){
                RadioButton radioButton = new RadioButton(mContext);
                radioButton.setId(i);
                radioButton.setText(mAvailableHoursAndRooms.get(mHourPosition).getRooms().get(i));
                mRadioGrpColumn3.addView(radioButton);
                i++;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int selectedRoomPosition = mRadioGroup.getCheckedRadioButtonId();
        mSelectedRoomName = mAvailableHoursAndRooms.get(mHourPosition).getRooms().get(selectedRoomPosition);
    }

    private void clearChildViews(){
        int nbrOfChildViews = mRadioGrpColumn1.getChildCount();
        mRadioGrpColumn3.removeAllViews();

        if (nbrOfChildViews != 0){
            mRadioGrpColumn1.removeAllViews();
            mRadioGrpColumn2.removeAllViews();
            mRadioGrpColumn3.removeAllViews();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_more_email:
                EditText anotherEmail = new EditText(mContext);
                anotherEmail.setHint(getString(R.string.email_hint));
                mEmailContainer.addView(anotherEmail);
                break;
            case R.id.next_page:
                checkIfValid();
                FragmentTransaction fm = getFragmentManager().beginTransaction();
                fm.replace(R.id.frame_listmeetings, new MeetingCreationEndFragment())
                        .addToBackStack(null).commit();

        }
    }

    public String getSelectedRoom() { return mSelectedRoomName; }

    public String getSelectedHour() { return mSelectedHour; }

    public ArrayList<String> getSpinnerArray() { return mSpinnerArray; }
}
