package com.sophie.mareu.service;

import com.sophie.mareu.controller.RoomsPerHour;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SOPHIE on 16/01/2020.
 */

// TODO: mettre une interface à la place du abstract
public interface RoomsAvailabilityService extends Serializable {
    void initRoomsAndHours();
    abstract ArrayList<RoomsPerHour> getRoomsPerHourList();
    abstract void updateAvailableHours(ArrayList<RoomsPerHour> availableHoursAndRooms);
}
