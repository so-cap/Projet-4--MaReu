package com.sophie.mareu;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sophie.mareu.RoomsAvailability.AvailabilityPerHour;
import com.sophie.mareu.model.Meeting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by SOPHIE on 02/01/2020.
 */
@RunWith(AndroidJUnit4.class)
public class MeetingCreationStartFragmentTests {
    private Meeting mMeeting;
    private ArrayList<AvailabilityPerHour> mRoomsAndHours;
    private AvailabilityPerHour mAvailabilityPerHour;

    @Before
    public void setup(){


    }


    @Test
    public void updateHoursAvailability(){
        onView(withId(R.id.fab)).perform(click());

        
    }
}
