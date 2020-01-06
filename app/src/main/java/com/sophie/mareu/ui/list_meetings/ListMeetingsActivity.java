package com.sophie.mareu.ui.list_meetings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sophie.mareu.R;
import com.sophie.mareu.controller.SortList;
import com.sophie.mareu.service.AvailabilityByDate;
import com.sophie.mareu.service.MeetingsService;
import com.sophie.mareu.ui.meeting_creation.HomeStartMeetingCreationFragment;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMeetingsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {
    private ListMeetingFragment mListMeetingFragment;
    private Date mSelectedDate = null;
    private String mSelectedName = null;
    private boolean mAscendingDate = true;
    private boolean mFiltered = false;
    Bundle mBundle = new Bundle();


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
    CardView mFilerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listmeetings);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mListMeetingFragment = (ListMeetingFragment) getSupportFragmentManager()
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
            case R.id.sort_by_date:
                SortList.sortByHour(mAscendingDate);
                mAscendingDate = !mAscendingDate;
                break;
            case R.id.filter:
                mFilerView.setVisibility(View.VISIBLE);
                break;
        }
        return true;
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void refreshView() {
        mListMeetingFragment.onStop();
        mListMeetingFragment.onStart();
        mListMeetingFragment.setArguments(mBundle);
        mListMeetingFragment.onResume();
        mFiltered = false;
    }

    private void configureAndShowListMeetingFragment() {
        if (mListMeetingFragment == null) {
            mListMeetingFragment = new ListMeetingFragment();
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.add(R.id.frame_listmeetings, mListMeetingFragment).commit();
        }
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
    protected void onDestroy() {
        super.onDestroy();
        AvailabilityByDate.clearAllMeetings();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String firstDays = "";
        String firstMonths = "";
        month += 1;

        mSelectedDate = new GregorianCalendar(year, month, dayOfMonth).getTime();

        if (dayOfMonth < 10) firstDays = "0" + dayOfMonth;
        if (month < 11) firstMonths = "0" + month;

        mDateView.setText(getString(R.string.date_selected,
                (dayOfMonth < 10? firstDays : (""+dayOfMonth+""))
                ,(month < 11 ? firstMonths : (""+month+"")),year));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choose_date:
                showDatePickerDialog();
                break;
            case R.id.ok_filter:
                if (mSelectedDate != null || !mSelectedName.isEmpty()) {
                    sendFilteredListToRecyclerView();
                    mFiltered = true;
                    mFilerView.setVisibility(View.GONE);
                }
                 else
                    Toast.makeText(this,"Choisissez une date et/ou une salle",Toast.LENGTH_LONG).show();
                break;
            case R.id.cancel_filter:
                mFilerView.setVisibility(View.GONE);
                break;
        }
    }

    private void sendFilteredListToRecyclerView() {
        mBundle.putBoolean("filter_state", mFiltered);
        AvailabilityByDate.filterMeetingsList(mSelectedDate, mSelectedName);
        refreshView();
    }
}
