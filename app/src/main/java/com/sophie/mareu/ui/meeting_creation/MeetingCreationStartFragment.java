package com.sophie.mareu.ui.meeting_creation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.sophie.mareu.R;
import com.sophie.mareu.controller.AvailabilityByDate;
import com.sophie.mareu.service.RoomsAvailabilityByHourImpl;
import com.sophie.mareu.controller.RoomsPerHour;
import com.sophie.mareu.ui.list_meetings.ListMeetingsActivity;
import com.sophie.mareu.service.RoomsAvailabilityService;
import static com.sophie.mareu.Constants.*;

import java.text.DateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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
    private Date mSelectedDate;
    private RoomsAvailabilityService mRoomsAvailabilityService = new RoomsAvailabilityByHourImpl();

    @BindView(R.id.select_date)
    Button mDateView;
    @BindView(R.id.spinner_hour)
    Spinner mSpinner;
    @BindView(R.id.chip_cloud)
    ChipCloud mChipCloud;
    @BindView(R.id.all_meetings_full)
    TextView mMeetingsFull;
    @BindView(R.id.next_page)
    ImageButton mNextPage;
    @BindView(R.id.meeting_start_toolbar)
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_creation_start, container, false);
        mContext = getContext();
        ButterKnife.bind(this, view);

        setUpBackButton();
        mDateView.setOnClickListener(this);
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

    private void setUpBackButton() {
        if (getActivity() != null)
            toolbar.setNavigationOnClickListener(v -> {
                if (getActivity().getClass().equals(ListMeetingsActivity.class))
                    getActivity().getSupportFragmentManager().popBackStack();
                else
                    getActivity().onBackPressed();
            });
    }

    @Override
    public void onResume() {
        super.onResume();
        mRoomPosition = -1;
    }

    private void initSpinner() {
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
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, mSpinnerArray);
            spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mSpinner.setAdapter(spinnerAdapter);
        }
    }

    private void initChipCloud() {
        String[] rooms = mAvailableHoursAndRooms.get(mHourPosition).getRooms().toArray(new String[0]);
        mChipCloud.removeAllViews();
        mChipCloud.addChips(rooms);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_page:
                if (checkIfValid())
                    startNextFragment();
                break;
            case R.id.select_date:
                showDatePickerDialog();
                break;
        }
    }

    private void showDatePickerDialog() {
        Locale.setDefault(Locale.FRANCE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        Button okButton = datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        okButton.setId(R.id.ok_button);
    }

    private boolean checkIfValid() {
        if (mRoomPosition >= 0) {
            mSelectedRoomName = mAvailableHoursAndRooms.get(mHourPosition).getRooms().get(mRoomPosition);
            return true;
        } else
            Toast.makeText(mContext, getString(R.string.choose_a_room), Toast.LENGTH_LONG).show();
        return false;
    }

    private void startNextFragment() {
        MeetingCreationEndFragment meetingCreationEndFragment = new MeetingCreationEndFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGUMENT_HOUR_KEY, mSelectedHour.getKey());
        bundle.putString(ARGUMENT_HOUR_VALUE, mSelectedHour.getValue());
        bundle.putInt(ARGUMENT_HOUR_POSITION, mHourPosition);
        bundle.putString(ARGUMENT_ROOM, mSelectedRoomName);
        bundle.putInt(ARGUMENT_ROOM_POSITION, mRoomPosition);
        bundle.putSerializable(ARGUMENT_DATE, mSelectedDate);
        bundle.putSerializable(ARGUMENT_SERVICE, mRoomsAvailabilityService);
        meetingCreationEndFragment.setArguments(bundle);

        if (getFragmentManager() != null) {
            FragmentTransaction fm = getFragmentManager().beginTransaction();
            fm.replace(R.id.frame_setmeeting, meetingCreationEndFragment).addToBackStack(null).commit();
        }
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
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
        Date newDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
        mDateView.setText(df.format(newDate));
        mDateView.setTextColor(getResources().getColor(R.color.dark_grey));

        if (!(mChipCloud.isShown())) {
            mChipCloud.setVisibility(View.VISIBLE);
            mNextPage.setVisibility(View.VISIBLE);
        }
        updateCurrentService(newDate);
    }

    private void updateCurrentService(Date newDate) {
        mRoomsAvailabilityService = AvailabilityByDate.setCurrentService(newDate);
        if (mRoomsAvailabilityService.getRoomsPerHourList().isEmpty()) {
            mMeetingsFull.setVisibility(View.VISIBLE);
            mChipCloud.setVisibility(View.GONE);
            mNextPage.setVisibility(View.GONE);
        } else {
            mSelectedDate = newDate;
            initSpinner();
            displaySpinner();
            initChipCloud();
        }
    }

    @VisibleForTesting
    public ArrayList<String> getSpinnerArray() {
        return mSpinnerArray;
    }

    @VisibleForTesting
    public void initSpinnerTest() {
        initSpinner();
    }
}
