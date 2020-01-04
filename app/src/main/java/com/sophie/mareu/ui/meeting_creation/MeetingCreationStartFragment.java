package com.sophie.mareu.ui.meeting_creation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sophie.mareu.controller.CustomRadioGroupLayout;
import com.sophie.mareu.R;
import com.sophie.mareu.service.RoomsAvailabilityService;
import com.sophie.mareu.controller.RoomsPerHour;
import com.sophie.mareu.ui.list_meetings.ListMeetingsActivity;

import java.util.AbstractMap;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingCreationStartFragment extends Fragment implements View.OnClickListener {
    private ArrayList<RoomsPerHour> mAvailableHoursAndRooms;
    private ArrayList<String> mSpinnerArray;
    private AbstractMap.SimpleEntry<Integer, String> mSelectedHour;
    private String mSelectedRoomName;
    private int mHourPosition;
    private Context mContext;
    private CustomRadioGroupLayout mCustomRadioGroup;
    private int mRoomPosition;

    @BindView(R.id.spinner_hour)
    Spinner mSpinner;

    @BindView(R.id.linearlayout_column1)
    LinearLayout mRadioGrpRow1;
    @BindView(R.id.linearlayout_column2)
    LinearLayout mRadioGrpRow2;
    @BindView(R.id.linearlayout_column3)
    LinearLayout mRadioGrpRow3;

    @BindView(R.id.all_meetings_full)
    TextView mMeetingsFull;
    @BindView(R.id.next_page)
    Button mNextPage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_creation_start, container, false);

        mContext = getContext();
        ButterKnife.bind(this, view);

        initSpinner();
        displaySpinner();
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mHourPosition = position;
                mSelectedHour = mAvailableHoursAndRooms.get(mHourPosition).getHour();
                initRadioGroup();
                mCustomRadioGroup.unselectButton(mCustomRadioGroup.getCheckedRadioButtonId());
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
        mAvailableHoursAndRooms = RoomsAvailabilityService.getRoomsPerHourList();
        mSpinnerArray = new ArrayList<>();
        String mHour;

        for (int position = 0; position < mAvailableHoursAndRooms.size(); position++) {
            mHour = (mAvailableHoursAndRooms.get(position).getHour().getValue());
            mSpinnerArray.add(mHour);
        }
    }

    private void displaySpinner() {
        if (!(mSpinnerArray.isEmpty())) {
            ArrayAdapter<String> spinnerAdapter =
                    new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, mSpinnerArray);
            spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mSpinner.setAdapter(spinnerAdapter);
        } else {
            mMeetingsFull.setVisibility(View.VISIBLE);
            mNextPage.setText(getString(R.string.previous_page));
        }
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
                mRadioGrpRow1.addView(radioButton);
                i++;
            }
            while (i >= 4 && i < 7 && i < roomsAvailable) {
                RadioButton radioButton = new RadioButton(mContext);
                radioButton.setId(i);
                radioButton.setText(mAvailableHoursAndRooms.get(mHourPosition).getRooms().get(i));
                mRadioGrpRow2.addView(radioButton);
                i++;
            }
            while (i >= 7 && i < roomsAvailable) {
                RadioButton radioButton = new RadioButton(mContext);
                radioButton.setId(i);
                radioButton.setText(mAvailableHoursAndRooms.get(mHourPosition).getRooms().get(i));
                mRadioGrpRow3.addView(radioButton);
                i++;
            }
        }
        initClickOnRadioButton();
    }


    private void clearChildViews() {
        int nbrOfChildViews = mRadioGrpRow1.getChildCount();

        if (nbrOfChildViews != 0) {
            mRadioGrpRow1.removeAllViews();
            mRadioGrpRow2.removeAllViews();
            mRadioGrpRow3.removeAllViews();
        }
    }

    private void initClickOnRadioButton() {
        ArrayList<RadioButton> radioButtons = new ArrayList<>();
        int childViews1 = mRadioGrpRow1.getChildCount();
        int childViews2 = mRadioGrpRow2.getChildCount();
        int childViews3 = mRadioGrpRow3.getChildCount();
        int nbrOfRows;

        if (childViews2 != 0)
            nbrOfRows = 2;
        else
            nbrOfRows = 3;

        if (childViews1 != 0) {
            for (int i = 0; i < nbrOfRows; i++) {
                int k = 0;
                while (k < childViews1) {
                    radioButtons.add((RadioButton) mRadioGrpRow1.getChildAt(k));
                    k++;
                }
                int j = 0;
                while (j < childViews2) {
                    radioButtons.add((RadioButton) mRadioGrpRow2.getChildAt(j));
                    j++;
                }
                int y = 0;
                while (y < childViews3) {
                    radioButtons.add((RadioButton) mRadioGrpRow3.getChildAt(y));
                    y++;
                }
                i++;
            }
        }
        mCustomRadioGroup.addRadioButtonToTracker(radioButtons);
    }

    @Override
    public void onClick(View v) {
        if (checkIfValid() && mSpinnerArray != null) {
            MeetingCreationEndFragment meetingCreationEndFragment = new MeetingCreationEndFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("selected_hour_key", mSelectedHour.getKey());
            bundle.putString("selected_hour_value", mSelectedHour.getValue());
            bundle.putString("selected_room", mSelectedRoomName);
            bundle.putInt("hour_position", mHourPosition);
            bundle.putInt("room_position", mRoomPosition);
            meetingCreationEndFragment.setArguments(bundle);

            FragmentTransaction fm = getFragmentManager().beginTransaction();
            fm.replace(R.id.frame_setmeeting, meetingCreationEndFragment)
                    .addToBackStack(null).commit();
        } else if (mSpinnerArray == null) {
            if (getActivity().getClass().equals(ListMeetingsActivity.class))
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            else
                getActivity().finish();
        }
    }

    private boolean checkIfValid() {
        mRoomPosition = mCustomRadioGroup.getCheckedRadioButtonId();

        if (mRoomPosition >= 0) {
            mSelectedRoomName = mAvailableHoursAndRooms.get(mHourPosition).getRooms().get(mRoomPosition);
            return true;
        } else
            Toast.makeText(mContext, getString(R.string.choose_a_room), Toast.LENGTH_LONG).show();
        return false;

    }

    @VisibleForTesting
    public ArrayList<String> getSpinnerArray() {
        return mSpinnerArray;
    }
}
