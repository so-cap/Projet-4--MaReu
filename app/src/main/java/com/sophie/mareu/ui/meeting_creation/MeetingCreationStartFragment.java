package com.sophie.mareu.ui.meeting_creation;

import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sophie.mareu.CustomRadioGroupLayout;
import com.sophie.mareu.R;
import com.sophie.mareu.RoomsAvailability;
import com.sophie.mareu.RoomsAvailability.AvailabilityPerHour;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingCreationStartFragment extends Fragment implements View.OnClickListener {
    private ArrayList<AvailabilityPerHour> mAvailableHoursAndRooms;
    private ArrayList<String> mSpinnerArray;
    private String mSelectedRoomName, mSelectedHour;
    private int mHourPosition;
    private Context mContext;
    private CustomRadioGroupLayout mCustomRadioGroup;

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

    @BindView(R.id.next_page)
    Button mNextPage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_creation_start, container, false);

        mContext = getContext();
        ButterKnife.bind(this, view);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        if (fab != null)
            fab.hide();

        initSpinner();
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mHourPosition = position;
                mSelectedHour = mAvailableHoursAndRooms.get(mHourPosition).getHour();
                initRadioGroup();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mNextPage.setOnClickListener(this);

        mCustomRadioGroup = new CustomRadioGroupLayout();
        return view;
    }

    public void initSpinner() {
        mAvailableHoursAndRooms = RoomsAvailability.getAvailableRoomsPerHour();
        mSpinnerArray = new ArrayList<>();
        String mHour;

        for (int position = 0; position < mAvailableHoursAndRooms.size(); position++) {
            mHour = (mAvailableHoursAndRooms.get(position).getHour());
            mSpinnerArray.add(mHour);
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

        while (i < roomsAvailable) {
            while (i < 4 && i < roomsAvailable) {
                RadioButton radioButton = new RadioButton(mContext);
                radioButton.setId(i);
                radioButton.setText(mAvailableHoursAndRooms.get(mHourPosition).getRooms().get(i));
                mRadioGrpColumn1.addView(radioButton);
                i++;
            }
            while (i >= 4 && i < 8 && i < roomsAvailable) {
                RadioButton radioButton = new RadioButton(mContext);
                radioButton.setId(i);
                radioButton.setText(mAvailableHoursAndRooms.get(mHourPosition).getRooms().get(i));
                mRadioGrpColumn2.addView(radioButton);
                i++;
            }
            while (i >= 8 && i < roomsAvailable) {
                RadioButton radioButton = new RadioButton(mContext);
                radioButton.setId(i);
                radioButton.setText(mAvailableHoursAndRooms.get(mHourPosition).getRooms().get(i));
                mRadioGrpColumn3.addView(radioButton);
                i++;
            }
        }
        initClickOnRadioButton();
    }

    private void initClickOnRadioButton() {
        ArrayList<RadioButton> radioButtons = new ArrayList<>();
        int childViews1 = mRadioGrpColumn1.getChildCount();
        int childViews2 = mRadioGrpColumn2.getChildCount();
        int childViews3 = mRadioGrpColumn3.getChildCount();
        int nbrOfColumns;

        if (childViews2 != 0)
            nbrOfColumns = 2;
        else
            nbrOfColumns = 3;

        if (childViews1 != 0) {
            for (int i = 0; i < nbrOfColumns; i++) {
                int k = 0;
                while (k < childViews1) {
                    radioButtons.add((RadioButton) mRadioGrpColumn1.getChildAt(k));
                    k++;
                }
                int j = 0;
                while (j < childViews2) {
                    radioButtons.add((RadioButton) mRadioGrpColumn2.getChildAt(j));
                    j++;
                }
                int y = 0;
                while (y < childViews3) {
                    radioButtons.add((RadioButton) mRadioGrpColumn3.getChildAt(y));
                    y++;
                }
                i++;
            }
        }
        mCustomRadioGroup.addRadioButtonToTracker(radioButtons);
    }

    private void clearChildViews() {
        int nbrOfChildViews = mRadioGrpColumn1.getChildCount();
        mRadioGrpColumn3.removeAllViews();

        if (nbrOfChildViews != 0) {
            mRadioGrpColumn1.removeAllViews();
            mRadioGrpColumn2.removeAllViews();
            mRadioGrpColumn3.removeAllViews();
        }
    }

    @Override
    public void onClick(View v) {
        if (checkIfValid()) {
            MeetingCreationEndFragment meetingCreationEndFragment = new MeetingCreationEndFragment();

            Bundle bundle = new Bundle();
            bundle.putString("selected_hour", mSelectedHour);
            bundle.putString("selected_room", mSelectedRoomName);
            meetingCreationEndFragment.setArguments(bundle);

            FragmentTransaction fm = getFragmentManager().beginTransaction();
            fm.replace(R.id.frame_setmeeting, meetingCreationEndFragment)
                    .addToBackStack(null).commit();
        }
    }


    private boolean checkIfValid() {
        int selectedRoomPosition = mCustomRadioGroup.getCheckedRadioButtonId();

        if (selectedRoomPosition >= 0) {
            mSelectedRoomName = mAvailableHoursAndRooms.get(mHourPosition).getRooms().get(selectedRoomPosition);
            //delete room availability
            mAvailableHoursAndRooms.get(mHourPosition).getRooms().remove(selectedRoomPosition);
            RoomsAvailability.updateAvailableHours(mAvailableHoursAndRooms);
            return true;
        } else
            Toast.makeText(mContext, "Choisissez votre salle de réunion!", Toast.LENGTH_LONG).show();
        return false;

    }

    public ArrayList<String> getSpinnerArray() {
        return mSpinnerArray;
    }
}
