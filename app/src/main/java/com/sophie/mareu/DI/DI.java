package com.sophie.mareu.DI;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by SOPHIE on 31/12/2019.
 */
public class DI {
    private static ArrayList<Integer> mHoursList = new ArrayList<>(Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19));
    private static ArrayList<String> mRoomsList = new ArrayList<>(Arrays.asList("PEACH","LUIGI","MARIO","BOWSER","WALUIGI","DAISY","WARIO","ROSALINA","TOAD","YOSHI"));


    public static ArrayList<Integer> getNewHoursList(){
        return mHoursList;
    }

    public static ArrayList<String> getNewRoomsList(){
        return mRoomsList;
    }
}
