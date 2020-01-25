package com.sophie.mareu;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.sophie.mareu.ui.list_meetings.ListMeetingsActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.sophie.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class FilterAndSortUITests {
    @Rule
    public ActivityTestRule<ListMeetingsActivity> activityRule =
            new ActivityTestRule<>(ListMeetingsActivity.class);
    private char D;

    @Before
    public void setup() {
        ListMeetingsActivity activity = activityRule.getActivity();
        assertThat(activity, notNullValue());
        D = 'D';
    }

    @Test
    public void filterListByNameWithSuccess() {
        onView(withId(R.id.meetings_list)).check(withItemCount(3));
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Filtrer les réunions")).perform(click());
        onView(withId(R.id.rooms_spinner_filter)).perform(click());
        onView(withText("Mario")).perform(click());
        onView(withId(R.id.ok_filter)).perform(click());
        onView(withId(R.id.meetings_list)).check(withItemCount(2));
    }

    @Test
    public void filterListByDateWithSuccess() {
        addMeeting("Peach");

        onView(withId(R.id.meetings_list)).check(withItemCount(4));
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Filtrer les réunions")).perform(click());
        onView(withId(R.id.choose_date)).perform(click());
        onView(withId(R.id.calendar_ok_button)).perform(click());
        onView(withId(R.id.ok_filter)).perform(click());
        onView(withId(R.id.meetings_list)).check(withItemCount(1));

    }

    @Test
    public void filterListByDateAndRoomWithSuccess() {
        addMeeting("Mario");

        onView(withId(R.id.meetings_list)).check(withItemCount(4));
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("Filtrer les réunions")).perform(click());
        //choosing today's date
        onView(withId(R.id.choose_date)).perform(click());
        onView(withId(R.id.calendar_ok_button)).perform(click());
        //choosing a room
        onView(withId(R.id.rooms_spinner_filter)).perform(click());
        onView(withText("Mario")).perform(click());
        onView(withId(R.id.ok_filter)).perform(click());
        onView(withId(R.id.meetings_list)).check(withItemCount(1));
    }

    public void addMeeting(String room){
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.select_date)).perform(click());
        onView(withId(R.id.calendar_ok_button)).perform(click());
        onView(withText(room)).perform(click());
        onView(withId(R.id.next_page)).perform(click());
        onView(withId(R.id.meeting_title_input)).perform(typeText("Reunion " + D++));
        onView(withId(R.id.meeting_subject_input)).perform(scrollTo()).perform(replaceText("Sujet de réunion"));
        onView(withId(R.id.email_one)).perform(scrollTo()).perform(typeText("email@address.com"));
        onView(withId(R.id.save_meeting_btn)).perform(click());
    }
}

