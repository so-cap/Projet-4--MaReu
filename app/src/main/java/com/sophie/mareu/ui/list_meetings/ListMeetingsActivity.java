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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.R;
import com.sophie.mareu.controller.FilterAndSort;
import com.sophie.mareu.service.AvailabilityByDate;
import com.sophie.mareu.ui.meeting_creation.HomeStartMeetingCreationFragment;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMeetingsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {
    private ListMeetingFragment listMeetingFragment = new ListMeetingFragment();
    private Fragment listMeetingFrame;
    private Date mSelectedDate = null;
    private String mSelectedName = null;
    public static final int ASCENDING = 0;
    public static final int DESCENDING = 1;
    public static final int FILTERED = 0;
    public static final int SORTED = 1;
    public static final int UNCHANGED = -1;

    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.choose_date)
    TextView mDateView;
    @BindView(R.id.cancel_filter)
    Button mBackBtn;
    @BindView(R.id.ok_filter)
    Button mOkButton;
    @BindView(R.id.spinner_filter)
    Spinner mRoomsSpinner;
    @BindView(R.id.filter_activity)
    CardView mFilterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listmeetings);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        listMeetingFrame = getSupportFragmentManager()
                .findFragmentById(R.id.frame_listmeetings);
        configureAndShowListMeetingFragment();
        configureAndShowHomeStartMeetingCreationFragment();

        mBackBtn.setOnClickListener(this);
        mOkButton.setOnClickListener(this);
        mDateView.setOnClickListener(this);
        mRoomsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedName = getResources().getStringArray(R.array.room_names)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    private static final String TAG = "LOGGListMeeActivity";

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
                listMeetingFragment.initList(UNCHANGED);
                FilterAndSort.getSortedList().clear();
                FilterAndSort.getFilteredList().clear();
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.onContentChanged();
        datePickerDialog.show();
    }

    private void configureAndShowListMeetingFragment() {
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        if (listMeetingFrame == null) {
            fm.add(R.id.frame_listmeetings, listMeetingFragment).commit();
        } else // in case we previously were in landscape mode.
            fm.replace(R.id.frame_listmeetings, listMeetingFragment).commit();
    }

    private void configureAndShowHomeStartMeetingCreationFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_setmeeting);
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();

        if (findViewById(R.id.frame_setmeeting) != null) {
            if (fragment == null)
                fm.add(R.id.frame_setmeeting, new HomeStartMeetingCreationFragment()).commit();
            else if (!(fragment.getClass().equals(HomeStartMeetingCreationFragment.class))) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.replace(R.id.frame_setmeeting, new HomeStartMeetingCreationFragment()).commit();
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String firstDays = "";
        String firstMonths = "";

        mSelectedDate = new GregorianCalendar(year, month, dayOfMonth).getTime();

        if (dayOfMonth < 10) firstDays = "0" + dayOfMonth;
        if (month < 11) firstMonths = "0" + (month + 1);

        mDateView.setText(getString(R.string.date_selected,
                (dayOfMonth < 10 ? firstDays : ("" + dayOfMonth + ""))
                , (month < 11 ? firstMonths : ("" + month + "")), year));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_date:
                showDatePickerDialog();
                break;
            case R.id.ok_filter:
                if (mSelectedDate != null || !mSelectedName.isEmpty()) {
                    FilterAndSort.filterMeetingsList(mSelectedDate, mSelectedName);
                    mFilterView.setVisibility(View.GONE);
                    listMeetingFragment.initList(FILTERED);
                    resetFilterView();
                } else
                    Toast.makeText(this, "Choisissez une date et/ou une salle", Toast.LENGTH_LONG).show();
                break;
            case R.id.cancel_filter:
                mFilterView.setVisibility(View.GONE);
                resetFilterView();
                break;
        }
    }

    private void resetFilterView() {
        mDateView.setText(getResources().getString(R.string.select));
        mSelectedDate = null;
        mSelectedName = "";
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
        AvailabilityByDate.clearAllMeetings();
    }
}

