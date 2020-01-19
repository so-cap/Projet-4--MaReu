package com.sophie.mareu.service;

import com.sophie.mareu.controller.RoomsPerHour;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SOPHIE on 16/01/2020.
 */
public interface RoomsAvailabilityService extends Serializable {
    void initRoomsAndHours();
    ArrayList<RoomsPerHour> getRoomsPerHourList();
    void updateAvailableHours(ArrayList<RoomsPerHour> availableHoursAndRooms);
}
