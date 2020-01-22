package com.sophie.mareu;

import android.content.pm.ActivityInfo;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.sophie.mareu.DI.DI;
import com.sophie.mareu.model.MeetingsHandler;
import com.sophie.mareu.model.RoomsAvailabilityHandler;
import com.sophie.mareu.model.RoomsPerHour;
import com.sophie.mareu.ui.list_meetings.ListMeetingsActivity;
import com.sophie.mareu.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
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
    private ListMeetingsActivity activity;
    private ArrayList<RoomsPerHour> roomsAndHours = new ArrayList<>();
    private ArrayList<String> rooms;
    private RoomsAvailabilityHandler roomsAvailability;
    private MeetingsHandler meetingsHandler = DI.getMeetingsHandler();
    private char A;

    @Rule
    public ActivityTestRule<ListMeetingsActivity> activityRule =
            new ActivityTestRule<>(ListMeetingsActivity.class);

    @Before
    public void setup() {
        A = 'A';
        activity = activityRule.getActivity();
        assertThat(activity, notNullValue());
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ArrayList<String> hours = new ArrayList<>(Arrays.asList(activity.getResources().
                getStringArray(R.array.hour_list)));
        rooms = new ArrayList<>(Arrays.asList(activity.getResources().
                getStringArray(R.array.room_names)));
        meetingsHandler.setHoursAndRooms(hours, rooms);
        roomsAvailability = new RoomsAvailabilityHandler();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // All tests take into account the 3 default dummyMeetings added for presentation
    // Therefore the expected values += 3 .
    @Test
    public void addNewMeetingWithSuccess() {
        addMeeting(0);
        onView(ViewMatchers.withId(R.id.meetings_list)).check(withItemCount(4));
    }

    @Test
    public void addNewMeetingInLandscapeModeWithSuccess() {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        meetingsHandler.clearAllMeetings();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addMeeting(0);
        onView(ViewMatchers.withId(R.id.meetings_list)).check(withItemCount(4));
    }

    @Test
    public void deleteMeetingWithSuccess() {
        onView(ViewMatchers.withId(R.id.meetings_list)).check(withItemCount(3));
        onView(ViewMatchers.withId(R.id.meetings_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        onView(ViewMatchers.withId(R.id.meetings_list)).check(withItemCount(2));
    }

    @Test
    public void openDetailsWithSuccess_OnClickOnMeeting() {
        addMeeting(0);
        onView(ViewMatchers.withId(R.id.meetings_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.detail_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void deleteAllMeetingsOnPhoneRotationWithSuccess() throws InterruptedException {
        addMeeting(0);
        onView(ViewMatchers.withId(R.id.meetings_list)).check(withItemCount(4));
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        meetingsHandler.clearAllMeetings();
        Thread.sleep(2000);
        onView(ViewMatchers.withId(R.id.meetings_list)).check(withItemCount(3));
    }

    @Test
    public void updateHoursAvailabilityDummy_MustShowUnavailabilityWithSuccess() {
        int initialHoursAvailable = 2;

        ArrayList<String> dummyRooms = new ArrayList<>(Arrays.asList("Peach", "Luigi"));
        roomsAndHours.add(new RoomsPerHour("8h00", new ArrayList<>(dummyRooms)));
        roomsAndHours.add(new RoomsPerHour("9h00", new ArrayList<>(dummyRooms)));
        roomsAvailability.updateAvailableHoursAndRooms(roomsAndHours);
        Date today = DI.getTodaysDateWithoutTime();
        meetingsHandler.updateAvailabilityByDate(today, roomsAvailability);

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
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
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

}
