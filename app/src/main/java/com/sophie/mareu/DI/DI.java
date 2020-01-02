package com.sophie.mareu.DI;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by SOPHIE on 31/12/2019.
 */
public class DI {
    private static ArrayList<String> mHoursList = new ArrayList<>(Arrays.asList("8h00","9h00","10h00","11h00","12h00","13h00","14h00",
            "15h00","16h00","17h00","18h00","19h00"));
    private static ArrayList<String> mRoomsList = new ArrayList<>(Arrays.asList("PEACH","LUIGI","MARIO","BOWSER","WALUIGI","DAISY","WARIO","ROSALINA","TOAD","YOSHI"));


    public static ArrayList<String> getNewHoursList(){
        return new ArrayList<>(mHoursList);
    }

    public static ArrayList<String> getNewRoomsList(){
        return new ArrayList<>(mRoomsList);
    }
}
