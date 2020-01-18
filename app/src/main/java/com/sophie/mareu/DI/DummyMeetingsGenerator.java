package com.sophie.mareu.DI;

import com.sophie.mareu.model.Meeting;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by SOPHIE on 04/01/2020.
 */
abstract class DummyMeetingsGenerator {
    private static Date dateA = Calendar.getInstance().getTime();
    private static Date dateB = new GregorianCalendar(2020, 7, 23).getTime();
    private static Date dateC = new GregorianCalendar(2020, 4, 3).getTime();
    private static Date dateD = new GregorianCalendar(2020, 9, 23).getTime();
    private static Date dummyDate = Calendar.getInstance().getTime();


    // each Integer is at the position where it has been added in ascending order
    // when initialising the hours availability
    private static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting("Réunion A",new AbstractMap.SimpleEntry<>(6, "14h00"), "Peach",
                    new ArrayList<>(Arrays.asList("maxime@lamzone.com","alex@lamzone.com")), getSubject(), dateA),
            new Meeting("Réunion B",new AbstractMap.SimpleEntry<>(8, "16h00"), "Mario",
                    new ArrayList<>(Arrays.asList("paul@lamzone.com","viviane@lamzone.com")), getSubject(), dateB),
            new Meeting("Réunion C",new AbstractMap.SimpleEntry<>(11, "19h00"), "Mario",
                    new ArrayList<>(Arrays.asList("amandine@lamzone.com","luc@lamzone")), getSubject(), dateC),
            new Meeting("Réunion A",new AbstractMap.SimpleEntry<>(7, "15h00"), "Peach",
                    new ArrayList<>(Arrays.asList("maxime@lamzone.com","alex@lamzone.com")), getDummySubject(), dateD));

    static ArrayList<Meeting> getDummyMeetings(){
        return new ArrayList<>(DUMMY_MEETINGS);
    }

    private static String getSubject(){
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
    }

    private static String getDummySubject(){
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit.\n" +
                "Phasellus et nisi suscipit, pulvinar lectus vitae, sagittis tortor.\n" +
                "Nullam sit amet felis euismod, facilisis sem quis.";
    }

}
