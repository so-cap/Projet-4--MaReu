package com.sophie.mareu;

import android.content.pm.ActivityInfo;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.controller.service.MeetingsController;
import com.sophie.mareu.controller.service.RoomsAvailabilityServiceImpl;
import com.sophie.mareu.model.RoomsPerHour;
import com.sophie.mareu.controller.service.RoomsAvailabilityApiService;
import com.sophie.mareu.ui.list_meetings.ListMeetingsActivity;
import com.sophie.mareu.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.sophie.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by SOPHIE on 02/01/2020.
 */
@RunWith(AndroidJUnit4.class)
public class MeetingCreationTests {
    private ListMeetingsActivity mActivity;
    private ArrayList<RoomsPerHour> roomsAndHours = new ArrayList<>();
    private ArrayList<String> rooms;
    private RoomsAvailabilityApiService service;
    private MeetingsController meetingsController = DI.getMeetingsController();
    private char A;

    @Rule
    public ActivityTestRule<ListMeetingsActivity> mActivityRule =
            new ActivityTestRule<>(ListMeetingsActivity.class);

    @Before
    public void setup() {
        A = 'A';
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ArrayList<String> hours = new ArrayList<>(Arrays.asList(mActivity.getResources().
                getStringArray(R.array.hour_list)));
        rooms = new ArrayList<>(Arrays.asList(mActivity.getResources().
                getStringArray(R.array.room_names)));
        meetingsController.setHoursAndRooms(hours, rooms);
        service = new RoomsAvailabilityServiceImpl();
    }

    // All tests take into account the default dummyMeeting
    // Therefore the expected values += 1 .
    @Test
    public void addNewMeetingWithSuccess() {
        addMeeting(0);
        onView(ViewMatchers.withId(R.id.meetings_list)).check(withItemCount(2));
    }

    @Test
    public void addNewMeetingInLandscapeModeWithSuccess() {
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        meetingsController.clearAllMeetings();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addMeeting(0);
        onView(ViewMatchers.withId(R.id.meetings_list)).check(withItemCount(2));
    }

    @Test
    public void deleteMeetingWithSuccess() {
        for (int position = 3; position != 0; position--)
            addMeeting(position);

        onView(ViewMatchers.withId(R.id.meetings_list)).check(withItemCount(4));
        onView(ViewMatchers.withId(R.id.meetings_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        onView(ViewMatchers.withId(R.id.meetings_list)).check(withItemCount(3));
    }

    @Test
    public void openDetailsWithSuccess_OnClickOnMeeting() {
        onView(ViewMatchers.withId(R.id.meetings_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.detail_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void deleteAllMeetingsOnPhoneRotationWithSuccess() throws InterruptedException {
        for (int position = 2; position != 0; position--)
            addMeeting(position);
        onView(ViewMatchers.withId(R.id.meetings_list)).check(withItemCount(3));
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        meetingsController.clearAllMeetings();
        Thread.sleep(2000);
        onView(ViewMatchers.withId(R.id.meetings_list)).check(withItemCount(1));
    }

    @Test
    public void updateHoursAvailabilityDummy_MustShowUnavailabilityWithSuccess() {
        int initialHoursAvailable = 2;

        ArrayList<String> dummyRooms = new ArrayList<>(Arrays.asList("Peach", "Luigi"));
        roomsAndHours.add(new RoomsPerHour("8h00", new ArrayList<>(dummyRooms)));
        roomsAndHours.add(new RoomsPerHour("9h00", new ArrayList<>(dummyRooms)));
        service.updateAvailableHours(roomsAndHours);
        Date today = getTodaysDateWithoutTime();
        meetingsController.updateAvailabilityByDate(today, service);

        for (int i = 0; i < initialHoursAvailable; i++) {
            int initialRoomsCount = dummyRooms.size() - 1;
            while (initialRoomsCount >= 0) {
                addMeeting(initialRoomsCount);
                initialRoomsCount--;
            }
        }
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.select_date)).perform(click());
        onView(withId(R.id.ok_button)).perform(click());
        onView(withId(R.id.all_meetings_full)).check(matches(isDisplayed()));
    }

    public void addMeeting(int roomPosition) {
        if (mActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            onView(withId(R.id.fab)).perform(click());
        else
            onView(withId(R.id.home_btn)).perform(click());
        onView(withId(R.id.select_date)).perform(click());
        onView(withId(R.id.ok_button)).perform(click());
        onView(withText(rooms.get(roomPosition))).perform(click());
        onView(withId(R.id.next_page)).perform(click());
        onView(withId(R.id.meeting_title_input)).perform(typeText("Reunion " + A++));
        onView(withId(R.id.meeting_subject_input)).perform(scrollTo()).perform(replaceText("Sujet de réunion"));
        onView(withId(R.id.email_one)).perform(scrollTo()).perform(typeText("email@address.com"));
        onView(withId(R.id.save_meeting_btn)).perform(click());
    }

    // To be able to test correctly no matter the time of running the test.
    public static Date getTodaysDateWithoutTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
