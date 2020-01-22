package com.sophie.mareu.controller.service;

import com.sophie.mareu.model.RoomsPerHour;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SOPHIE on 16/01/2020.
 */
public interface RoomsAvailabilityApiService extends Serializable {
    void initRoomsPerHourList(ArrayList<String> hours, ArrayList<String> rooms);
    ArrayList<RoomsPerHour> getRoomsPerHourList();
    void updateAvailableHours(ArrayList<RoomsPerHour> availableHoursAndRooms);
}
