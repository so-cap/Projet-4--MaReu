package com.sophie.mareu.event;

import com.sophie.mareu.model.Meeting;

/**
 * Created by SOPHIE on 02/01/2020.
 */
public class DeleteMeetingEvent {
    public Meeting meeting;

    public DeleteMeetingEvent(Meeting meeting){
        this.meeting = meeting;
    }
}
