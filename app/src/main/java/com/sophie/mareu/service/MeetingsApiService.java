package com.sophie.mareu.service;

import java.util.Date;

/**
 * Created by SOPHIE on 22/01/2020.
 */
public interface MeetingsApiService {
    RoomsAvailabilityApiService getCurrentRoomsPerHourService(Date date);

}
