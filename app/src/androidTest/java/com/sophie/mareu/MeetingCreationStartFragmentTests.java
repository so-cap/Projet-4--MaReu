package com.sophie.mareu;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.sophie.mareu.controller.RoomsAvailability;
import com.sophie.mareu.controller.AvailabilityPerHour;
import com.sophie.mareu.ui.list_meetings.ListMeetingsActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by SOPHIE on 02/01/2020.
 */
@RunWith(AndroidJUnit4.class)
public class MeetingCreationStartFragmentTests {
    private ListMeetingsActivity mActivity;
    private RoomsAvailability mRoomsAvailability = new RoomsAvailability();
    private ArrayList<AvailabilityPerHour> mRoomsAndHours = new ArrayList<>();
    private ArrayList<String> mRooms;

    @Rule
    public ActivityTestRule<ListMeetingsActivity> mActivityRule =
            new ActivityTestRule<>(ListMeetingsActivity.class);

    @Before
    public void setup(){
      /*  mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());

        mRooms = new ArrayList<>(Arrays.asList("PEACH","MARIO","WARIO"));
        mRoomsAndHours.add(new AvailabilityPerHour("8h00", new ArrayList<>(mRooms)));
        mRoomsAndHours.add(new AvailabilityPerHour("9h00",new ArrayList<>(mRooms)));
        mRoomsAndHours.add(new AvailabilityPerHour("10h00",new ArrayList<>(mRooms)));

        RoomsAvailability.updateAvailableHours(mRoomsAndHours);

       */
      mRoomsAvailability.initRoomsAndHours();
      mRoomsAndHours = RoomsAvailability.getAvailableRoomsPerHour();

    }

    @Test
    public void updateHoursAvailability(){
        int initialHoursAvailable = 3;
        char A = 'A';

        for (int i = 0; i < initialHoursAvailable; i++) {
            int initialRoomsCount = 3-1;
            while (initialRoomsCount >= 0) {
                onView(withId(R.id.fab)).perform(click());
                onView(withText(mRooms.get(initialRoomsCount))).perform(click());
                onView(withText("SUIVANT")).perform(click());
                onView(withId(R.id.meeting_title_input)).perform(typeText("Reunion " + A));
                onView(withId(R.id.email_one)).perform(typeText("email@address.com"));
                onView(withText("VALIDER")).perform(scrollTo()).perform(click());
                initialRoomsCount--;
            }
        }
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.all_meetings_full)).check(matches(isDisplayed()));
    }

    @Test
    public void updateHoursAvailabilityReal(){
        int initialHoursAvailable = 12;
        char a = 'a';

        for (int i = 0; i < initialHoursAvailable; i++) {
            int initialRoomsCount = 10 - 1;
            while (initialRoomsCount >= 0) {
                onView(withId(R.id.fab)).perform(click());
                onView(withText(mRoomsAndHours.get(i).getRooms().get(initialRoomsCount))).perform(click());
                onView(withText("SUIVANT")).perform(click());
                onView(withId(R.id.meeting_title_input)).perform(typeText("Reunion " + (a++)));
                onView(withId(R.id.email_one)).perform(typeText("email@address.com"));
                onView(withText("VALIDER")).perform(scrollTo()).perform(click());
                initialRoomsCount--;
                if(a == 'Z')
                    a = 'a';
            }
        }
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.all_meetings_full)).check(matches(isDisplayed()));
    }
}
