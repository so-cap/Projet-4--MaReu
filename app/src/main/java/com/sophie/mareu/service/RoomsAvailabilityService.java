package com.sophie.mareu.service;

import com.sophie.mareu.controller.RoomsPerHour;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SOPHIE on 16/01/2020.
 */
public abstract class RoomsAvailabilityService implements Serializable {
    public abstract void initRoomsAndHours();
    public abstract ArrayList<RoomsPerHour> getRoomsPerHourList();
    public abstract void updateAvailableHours(ArrayList<RoomsPerHour> availableHoursAndRooms);
}
