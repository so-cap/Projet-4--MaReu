package com.sophie.mareu.ui.meeting_creation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.sophie.mareu.R;
import com.sophie.mareu.service.AvailabilityByDate;
import com.sophie.mareu.service.RoomsAvailabilityService;
import com.sophie.mareu.controller.RoomsPerHour;
import com.sophie.mareu.ui.list_meetings.ListMeetingsActivity;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingCreationStartFragment extends Fragment implements View.OnClickListener, ChipListener, DatePickerDialog.OnDateSetListener {
    private ArrayList<RoomsPerHour> mAvailableHoursAndRooms;
    private ArrayList<String> mSpinnerArray;
    private AbstractMap.SimpleEntry<Integer, String> mSelectedHour;
    private String mSelectedRoomName;
    private int mHourPosition;
    private Context mContext;
    private int mRoomPosition = -1;
    private Date mSelectedDate = Calendar.getInstance().getTime();
    private RoomsAvailabilityService mRoomsAvailabilityService;
    int i = 0;


    @BindView(R.id.select_date)
    TextView mDateView;
    @BindView(R.id.spinner_hour)
    Spinner mSpinner;
    @BindView(R.id.chip_cloud)
    ChipCloud mChipCloud;

    @BindView(R.id.all_meetings_full)
    TextView mMeetingsFull;
    @BindView(R.id.next_page)
    Button mNextPage;

    private static final String TAG = "LOGGCreationStart";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_creation_start, container, false);
        mContext = getContext();
        ButterKnife.bind(this, view);

        mRoomsAvailabilityService = AvailabilityByDate.getRoomsAvailabilityService(mSelectedDate);

        mDateView.setOnClickListener(this);
        initSpinner();
        displaySpinner();
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mHourPosition = position;
                mSelectedHour = mAvailableHoursAndRooms.get(mHourPosition).getHour();
                initChipCloud();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mChipCloud.setChipListener(this);
        mNextPage.setOnClickListener(this);
        return view;
    }

    public void initSpinner() {
        mAvailableHoursAndRooms = mRoomsAvailabilityService.getRoomsPerHourList();
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

    private void initChipCloud() {
        ArrayList<String> rooms = mAvailableHoursAndRooms.get(mHourPosition).getRooms();
        //chipCloud only takes [] format
        String[] roomsList = rooms.toArray(new String[0]);
        mChipCloud.removeAllViews();
        mChipCloud.addChips(roomsList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_page:
                if (checkIfValid() && mSpinnerArray != null) {
                    startNextFragment();
                } else if (mSpinnerArray == null) {
                    if (getActivity().getClass().equals(ListMeetingsActivity.class))
                        getActivity().getSupportFragmentManager().popBackStack(null,
                                FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    else
                        getActivity().finish();
                }
                break;
            case R.id.select_date:
                showDatePickerDialog();
                break;
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private boolean checkIfValid() {
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

    private void startNextFragment() {
        MeetingCreationEndFragment meetingCreationEndFragment = new MeetingCreationEndFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("selected_hour_key", mSelectedHour.getKey());
        bundle.putString("selected_hour_value", mSelectedHour.getValue());
        bundle.putString("selected_room", mSelectedRoomName);
        bundle.putInt("hour_position", mHourPosition);
        bundle.putInt("room_position", mRoomPosition);
        bundle.putSerializable("selected_date", mSelectedDate);
        bundle.putSerializable("rooms_availability_service", mRoomsAvailabilityService);
        meetingCreationEndFragment.setArguments(bundle);

        FragmentTransaction fm = getFragmentManager().beginTransaction();
        fm.replace(R.id.frame_setmeeting, meetingCreationEndFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void chipSelected(int index) {
        mRoomPosition = index;
    }

    @Override
    public void chipDeselected(int index) {
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String firstDays = "";
        String firstMonths = "";

        Date newDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
        updateCurrentService(newDate);

        if (dayOfMonth < 10) firstDays = "0" + dayOfMonth;
        if (month < 12) firstMonths = "0" + (month+1);

        mDateView.setText(getString(R.string.date_selected,
                (dayOfMonth < 10? firstDays : (""+dayOfMonth+""))
                ,(month < 11 ? firstMonths : (""+month+"")),year));

        if (!(mChipCloud.isShown())) {
            mChipCloud.setVisibility(View.VISIBLE);
            mNextPage.setVisibility(View.VISIBLE);
        }
    }

    private void updateCurrentService(Date newDate) {
        mRoomsAvailabilityService = AvailabilityByDate.getRoomsAvailabilityService(newDate);
        mSelectedDate = newDate;
        initSpinner();
        displaySpinner();
        initChipCloud();
    }
}
