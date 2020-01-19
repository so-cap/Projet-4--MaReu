package com.sophie.mareu.ui.list_meetings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.R;
import com.sophie.mareu.controller.FilterAndSort;
import com.sophie.mareu.controller.AvailabilityByDate;
import com.sophie.mareu.model.Meeting;
import static com.sophie.mareu.Constants.*;

import com.sophie.mareu.service.RoomsAvailabilityByHourImpl;
import com.sophie.mareu.ui.meeting_creation.HomeStartMeetingCreationFragment;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sophie.mareu.model.Meeting.iconSelector;

public class ListMeetingsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {
    private ListMeetingFragment listMeetingFragment = new ListMeetingFragment();
    private DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
    private Fragment listMeetingFrame;
    private Date mSelectedDate = null;
    private String mSelectedRoom = null;
    private Menu menu;

    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.choose_date)
    Button mDateView;
    @BindView(R.id.back_button)
    ImageButton mBackBtn;
    @BindView(R.id.ok_filter)
    Button mOkButton;
    @BindView(R.id.spinner_filter)
    Spinner mRoomsSpinner;
    @BindView(R.id.filter_activity)
    CardView mFilterView;
    @BindView(R.id.filter_selected)
    CardView mFilterSelected;
    @BindView(R.id.deactivate_filter)
    CardView mDeactivateFilter;
    @BindView(R.id.filter_selected_text)
    TextView filterModeText;
    @BindView(R.id.filter_activated)
    View filterActivatedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listmeetings);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        listMeetingFrame = getSupportFragmentManager().findFragmentById(R.id.frame_listmeetings);

        /* Add dummyMeeting for presentation : */
        Meeting dummyMeeting = DI.getDummyMeetings().get(3);
        AvailabilityByDate.addMeeting(dummyMeeting, dummyMeeting.getDate());
        AvailabilityByDate.updateAvailabilityByDate(dummyMeeting.getDate(), new RoomsAvailabilityByHourImpl());

        configureAndShowListMeetingFragment();
        configureAndShowHomeStartMeetingCreationFragment();

        mDeactivateFilter.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mOkButton.setOnClickListener(this);
        mDateView.setOnClickListener(this);
        mRoomsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedRoom = getResources().getStringArray(R.array.room_names)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void configureAndShowListMeetingFragment() {
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        if (listMeetingFrame == null) {
            fm.add(R.id.frame_listmeetings, listMeetingFragment).commit();
        } else // in case we previously were in landscape mode.
            fm.replace(R.id.frame_listmeetings, listMeetingFragment).commit();
    }

    private void configureAndShowHomeStartMeetingCreationFragment() {
        Fragment meetingCreationFrame = getSupportFragmentManager().findFragmentById(R.id.frame_setmeeting);
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        if (findViewById(R.id.frame_setmeeting) != null) {
            if (meetingCreationFrame == null) {
                fm.add(R.id.frame_setmeeting, new HomeStartMeetingCreationFragment()).commit();
            } else if (!(meetingCreationFrame.getClass().equals(HomeStartMeetingCreationFragment.class))) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.replace(R.id.frame_setmeeting, new HomeStartMeetingCreationFragment()).commit();
            }
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        super.onMenuOpened(featureId, menu);
        if (mFilterView.isShown()) {
            mFilterView.setVisibility(View.GONE);
            this.menu.close();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ascending:
                FilterAndSort.sortList(ASCENDING);
                listMeetingFragment.initList(SORTED);
                break;
            case R.id.descending:
                FilterAndSort.sortList(DESCENDING);
                listMeetingFragment.initList(SORTED);
                break;
            case R.id.filter:
                initSpinner();
                mFilterView.setVisibility(View.VISIBLE);
                break;
            case R.id.display_all_meetings:
                displayAllMeetings();
                break;
        }
        return true;
    }

    private void initSpinner() {
        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("");
        spinnerArray.addAll(DI.getNewRoomsList());
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, spinnerArray);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mRoomsSpinner.setAdapter(spinnerAdapter);
    }

    private void showDatePickerDialog() {
        Locale.setDefault(Locale.FRANCE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mSelectedDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
        mDateView.setText(df.format(mSelectedDate));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_date:
                showDatePickerDialog();
                break;
            case R.id.ok_filter:
                if (mSelectedDate != null || !mSelectedRoom.isEmpty()) {
                    FilterAndSort.filterMeetingsList(mSelectedDate, mSelectedRoom);
                    mFilterView.setVisibility(View.GONE);
                    listMeetingFragment.initList(FILTERED);
                    setFilteredLListCardViews();
                    resetFilterView();
                } else
                    Toast.makeText(this, getString(R.string.choose_date_or_room), Toast.LENGTH_LONG).show();
                break;
            case R.id.back_button:
                resetFilterView();
                break;
            case R.id.deactivate_filter:
                displayAllMeetings();
                break;
        }
    }

    private void setFilteredLListCardViews() {
        String text;
        if (mSelectedDate != null && mSelectedRoom.isEmpty())
            text = getString(R.string.filtered_date_only, df.format(mSelectedDate));
        else if (mSelectedDate == null && !mSelectedRoom.isEmpty())
            text = getString(R.string.filtered_room_only, mSelectedRoom);
        else
            text = getString(R.string.filtered_date_and_room, df.format(Objects.requireNonNull(mSelectedDate)), mSelectedRoom);

        filterModeText.setText(text);
        filterActivatedView.setVisibility(View.VISIBLE);
    }

    private void displayAllMeetings() {
        listMeetingFragment.initList(UNCHANGED);
        FilterAndSort.getSortedList().clear();
        FilterAndSort.getFilteredList().clear();
        filterActivatedView.setVisibility(View.GONE);
    }

    private void resetFilterView() {
        mFilterView.setVisibility(View.GONE);
        mDateView.setText(getResources().getString(R.string.select_date));
        mSelectedDate = null;
        mSelectedRoom = "";
        mRoomsSpinner.setSelection(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFilterView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iconSelector = 0;
        AvailabilityByDate.clearAllMeetings();
    }

    @Override
    public void onBackPressed() {
        Fragment detailFragment = getSupportFragmentManager().findFragmentById(R.id.frame_setmeeting);
        if (detailFragment != null && detailFragment.getClass().equals(DetailFragment.class)) {
            if (detailFragment.getFragmentManager() != null)
                detailFragment.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else
            super.onBackPressed();
    }
}
