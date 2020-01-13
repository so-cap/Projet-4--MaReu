package com.sophie.mareu.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.sophie.mareu.R;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by SOPHIE on 30/12/2019.
 */
public class Meeting implements Parcelable {
    private String mRoomName;
    private ArrayList<String> mParticipants;
    private AbstractMap.SimpleEntry<Integer, String> mHour;
    private String mTitle;
    private String mDetailSubject;
    private Date mDate;
    private ArrayList mIconList =
            new ArrayList<>(Arrays.asList(R.drawable.ic_lightpink, R.drawable.ic_lightgreen, R.drawable.ic_darkergreen));
    private int mIcon;
    private static int iconSelector;


    public Meeting() {
    }

    public Meeting(String title, AbstractMap.SimpleEntry<Integer, String> hour,
                   String roomName, ArrayList<String> participants, String subject, Date date) {
        if(iconSelector == 3)
            iconSelector = 0;

        mTitle = title;
        mHour = new AbstractMap.SimpleEntry<>(hour.getKey(), hour.getValue());
        mRoomName = roomName;
        mParticipants = participants;
        mDetailSubject = subject;
        mDate = date;
        mIcon = (int) mIconList.get(iconSelector++);
    }

    protected Meeting(Parcel in) {
        mRoomName = in.readString();
        mParticipants = in.createStringArrayList();
        mTitle = in.readString();
        mDetailSubject = in.readString();
        mIcon = in.readInt();
    }

    public static final Creator<Meeting> CREATOR = new Creator<Meeting>() {
        @Override
        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };

    public String getRoomName() {
        return mRoomName;
    }

    public void setRoomName(String roomName) {
        mRoomName = roomName;
    }

    public int getIcon() {
        return mIcon;
    }

    public String getParticipants() {
        StringBuilder participants = new StringBuilder();
        for (int i = 0; i < mParticipants.size(); i++) {
            participants.append(mParticipants.get(i));
            //check if we reach the end of the list
            if ((i + 1) != mParticipants.size())
                participants.append(", ");
        }
        return participants.toString();
    }

    public void setParticipants(ArrayList<String> participants) {
        mParticipants = participants;
    }

    public AbstractMap.SimpleEntry<Integer, String> getHour() {
        return mHour;
    }

    public void setHour(String hour) {
        mHour.setValue(hour);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Date getDate(){
        return mDate;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSubject() {
        return mDetailSubject;
    }

    public void setSubject(String subject) {
        mDetailSubject = subject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRoomName);
        dest.writeStringList(mParticipants);
        dest.writeString(mTitle);
        dest.writeString(mDetailSubject);
        dest.writeInt(mIcon);
    }
}
