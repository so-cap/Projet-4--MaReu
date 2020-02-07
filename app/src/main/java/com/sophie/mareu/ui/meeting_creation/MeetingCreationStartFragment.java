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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sophie.mareu.R;
import com.sophie.mareu.di.DI;
import com.sophie.mareu.helper.MeetingsHandler;
import com.sophie.mareu.helper.RoomsAvailability;
import com.sophie.mareu.model.Meeting;
import com.sophie.mareu.model.RoomsPerHour;
import com.sophie.mareu.ui.list_meetings.ListMeetingsActivity;

import java.text.DateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingCreationStartFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    public static final String ARGUMENT_MEETING = "selected meeting";
    static final String ARGUMENT_ROOMS_AVAILABILITY = "rooms availability service";
    static final String ARGUMENT_HOUR_POSITION = "hour position";
    private ArrayList<RoomsPerHour> roomsPerHourList;
    private ArrayList<String> spinnerArray;
    private AbstractMap.SimpleEntry<Integer, String> selectedHour;
    private String selectedRoom;
    private int hourPosition;
    private Context context;
    private int roomPosition = -1;
    private Date selectedDate;
    private RoomsAvailability roomsAvailability = new RoomsAvailability();

    @BindView(R.id.select_date)
    Button dateView;
    @BindView(R.id.spinner_hour)
    Spinner spinner;
    @BindView(R.id.chip_group)
    ChipGroup chipGroup;
    @BindView(R.id.all_meetings_full)
    TextView meetingsFull;
    @BindView(R.id.next_page)
    Button nextPage;
    @BindView(R.id.meeting_start_toolbar)
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_creation_start, container, false);
        context = getContext();
        ButterKnife.bind(this, view);

        setUpBackButton();
        dateView.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
        nextPage.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        roomPosition = -1;
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_date:
                showDatePickerDialog();
                break;
            case R.id.next_page:
                if (checkIfValid())
                    startNextFragment();
                break;
        }
    }

    private void showDatePickerDialog() {
        Locale.setDefault(Locale.FRANCE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

        Button okButton = datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        okButton.setId(R.id.calendar_ok_button);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
        Date newDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
        dateView.setText(df.format(newDate));
        dateView.setTextColor(getResources().getColor(R.color.dark_grey));

        if (!(chipGroup.isShown())) {
            chipGroup.setVisibility(View.VISIBLE);
            nextPage.setVisibility(View.VISIBLE);
        }
        updateCurrentRoomsAvailabilityHandler(newDate);
    }

    private void updateCurrentRoomsAvailabilityHandler(Date newDate) {
        MeetingsHandler meetingsHandler = DI.getMeetingsHandler();
        roomsAvailability = meetingsHandler.getCurrentRoomsAvailabilityHandler(newDate);
        if (!roomsAvailability.getRoomsPerHourList().isEmpty()) {
            selectedDate = newDate;
            initSpinner();
            displaySpinner();
            initChipCloud();
        } else {
            meetingsFull.setVisibility(View.VISIBLE);
            chipGroup.setVisibility(View.GONE);
            nextPage.setVisibility(View.GONE);
        }
    }

    private void initSpinner() {
        roomsPerHourList = roomsAvailability.getRoomsPerHourList();
        spinnerArray = new ArrayList<>();
        String mHour;

        for (int position = 0; position < roomsPerHourList.size(); position++) {
            mHour = (roomsPerHourList.get(position).getHour().getValue());
            spinnerArray.add(mHour);
        }
    }

    private void displaySpinner() {
        if (!(spinnerArray.isEmpty())) {
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, spinnerArray);
            spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        hourPosition = position;
        selectedHour = roomsPerHourList.get(hourPosition).getHour();
        initChipCloud();
        //In case the user chooses a room, then changes the hour afterwards.
        roomPosition = -1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void initChipCloud() {
        String[] rooms = roomsPerHourList.get(hourPosition).getRooms().toArray(new String[0]);
        chipGroup.removeAllViews();

        for (int i = 0; i < rooms.length ; i++) {
            Chip chip = new Chip(chipGroup.getContext());
            chip.setText(rooms[i]);
            chip.setClickable(true);
            chip.setCheckable(true);
            chip.setTextSize(24);
            chip.setId(i);
            chipGroup.addView(chip);
        }
    }

    private boolean checkIfValid() {
        roomPosition = chipGroup.getCheckedChipId();
        if (roomPosition >= 0) {
            selectedRoom = roomsPerHourList.get(hourPosition).getRooms().get(roomPosition);
            return true;
        } else
            Toast.makeText(context, getString(R.string.choose_a_room), Toast.LENGTH_LONG).show();
        return false;
    }

    private void startNextFragment() {
        MeetingCreationEndFragment meetingCreationEndFragment = new MeetingCreationEndFragment();
        Meeting meeting = new Meeting();
        meeting.setHour(selectedHour.getKey(), selectedHour.getValue());
        meeting.setRoomName(selectedRoom);
        meeting.setDate(selectedDate);

        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_MEETING, meeting);
        bundle.putSerializable(ARGUMENT_ROOMS_AVAILABILITY, roomsAvailability);
        bundle.putInt(ARGUMENT_HOUR_POSITION, hourPosition);
        meetingCreationEndFragment.setArguments(bundle);

        if (getFragmentManager() != null) {
            FragmentTransaction fm = getFragmentManager().beginTransaction();
            fm.replace(R.id.frame_setmeeting, meetingCreationEndFragment).addToBackStack(null).commit();
        }
    }
}
