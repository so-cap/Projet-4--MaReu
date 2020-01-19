package com.sophie.mareu.DI;

import com.sophie.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by SOPHIE on 31/12/2019.
 */
public class DI {

    // TODO: Ecrire la liste dans un fichier String.xml puis recupérer les string Arrays
    private static List<String> mInitialHoursList = Arrays.asList("8h00","9h00","10h00","11h00","12h00","13h00","14h00",
           "15h00","16h00","17h00","18h00","19h00");
    private static List<String> mInitialRoomsList = Arrays.asList("Peach","Luigi","Mario","Bowser","Waluigi","Daisy",
            "Wario","Rosalina","Toad","Yoshi");
    private static List<Meeting> dummyMeetings = DummyMeetingsGenerator.getDummyMeetings();

    public static ArrayList<String> getNewHoursList(){
        return new ArrayList<>(mInitialHoursList);
    }

    public static ArrayList<String> getNewRoomsList(){
        return new ArrayList<>(mInitialRoomsList);
    }

    public static List<Meeting> getDummyMeetings(){
        return dummyMeetings;
    }
}
