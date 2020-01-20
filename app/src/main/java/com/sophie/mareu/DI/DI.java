package com.sophie.mareu.DI;

import android.content.res.Resources;

import com.sophie.mareu.R;
import com.sophie.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by SOPHIE on 31/12/2019.
 */
public class DI {
    private static Resources res;

    public static void setResources(Resources resources){
        res = resources;
    }

    private static List<Meeting> dummyMeetings = DummyMeetingsGenerator.getDummyMeetings();

    public static ArrayList<String> getNewHoursList() {
        return new ArrayList<>(Arrays.asList(res.getStringArray(R.array.hour_list)));
    }

    public static ArrayList<String> getNewRoomsList(){
        return new ArrayList<>(Arrays.asList(res.getStringArray(R.array.room_names)));
    }

    public static List<Meeting> getDummyMeetings(){
        return dummyMeetings;
    }
}
