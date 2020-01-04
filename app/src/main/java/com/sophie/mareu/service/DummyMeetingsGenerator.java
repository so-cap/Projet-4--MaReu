package com.sophie.mareu.service;

import com.sophie.mareu.model.Meeting;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by SOPHIE on 04/01/2020.
 */
public abstract class DummyMeetingsGenerator {

    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting("Réunion A",new AbstractMap.SimpleEntry<>(0, "14h00"), "PEACH",
                    new ArrayList<>(Arrays.asList("maxime@lamzone.com","alex@lamzone.com")), null),
            new Meeting("Réunion B",new AbstractMap.SimpleEntry<>(1, "16h00"), "Mario",
                    new ArrayList<>(Arrays.asList("paul@lamzone.com","viviane@lamzone.com")), null),
            new Meeting("Réunion C",new AbstractMap.SimpleEntry<>(2, "19h00"), "Luigi",
                    new ArrayList<>(Arrays.asList("amandine@lamzone.com","luc@lamzone")), null));

    public static ArrayList<Meeting> getDummyMeetings(){
        return new ArrayList<>(DUMMY_MEETINGS);
    }

}
