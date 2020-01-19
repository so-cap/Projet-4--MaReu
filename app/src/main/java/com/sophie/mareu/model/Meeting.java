package com.sophie.mareu.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.sophie.mareu.R;

import java.text.DateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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
    private int mIcon;
    public static int iconSelector = 0;

    public Meeting() {
        mIcon = (int) getIconList().get(iconSelector++);
    }

    public Meeting(Meeting meeting) {
        mTitle = meeting.getTitle();
        mHour = meeting.getHour();
        mRoomName = meeting.getRoomName();
        mParticipants = meeting.getParticipantsArray();
        mDetailSubject = meeting.getSubject();
        mDate = meeting.getDate();
        mIcon = meeting.getIcon();
    }

    public Meeting(String title, AbstractMap.SimpleEntry<Integer, String> hour,
                   String roomName, ArrayList<String> participants, String subject, Date date) {
        mIcon = (int) getIconList().get(iconSelector++);
        mTitle = title;
        mHour = new AbstractMap.SimpleEntry<>(hour.getKey(), hour.getValue());
        mRoomName = roomName;
        mParticipants = participants;
        mDetailSubject = subject;
        mDate = date;
    }

    protected Meeting(Parcel in) {
        mRoomName = in.readString();
        mParticipants = in.createStringArrayList();
        mTitle = in.readString();
        mDetailSubject = in.readString();
        mIcon = in.readInt();
        mHour = new AbstractMap.SimpleEntry<>(in.readInt(), in.readString());
        mDate = new Date(in.readLong());
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

    private ArrayList getIconList(){
        if (iconSelector == 3)
            iconSelector = 0;
        return new ArrayList<>(Arrays.asList(R.drawable.ic_lightpink, R.drawable.ic_lightgreen,
                R.drawable.ic_darkergreen));
    }

    public String getRoomName() {
        return mRoomName;
    }

    public void setRoomName(String roomName) {
        mRoomName = roomName;
    }

    public int getIcon() {
        return mIcon;
    }

    public String getParticipantsInOneString() {
        StringBuilder participants = new StringBuilder();
        for (int i = 0; i < mParticipants.size(); i++) {
            participants.append(mParticipants.get(i));
            //check if we reach the end of the list
            if ((i + 1) != mParticipants.size())
                participants.append(", ");
        }
        return participants.toString();
    }

    public ArrayList<String> getParticipantsArray() {
        return mParticipants;
    }

    public void setParticipants(ArrayList<String> participants) {
        mParticipants = participants;
    }

    public AbstractMap.SimpleEntry<Integer, String> getHour() {
        return mHour;
    }

    public void setHour(Integer key, String hour) {
        mHour = new AbstractMap.SimpleEntry<>(key, hour);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Date getDate() {
        return mDate;
    }

    public String getDateInStringFormat(Date date) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE);
        return dateFormat.format(date);
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
        dest.writeInt(mHour.getKey());
        dest.writeString(mHour.getValue());
        dest.writeLong(mDate.getTime());
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof Meeting)) return false;

        Meeting meeting = (Meeting) o;
        return Objects.equals(mTitle, meeting.getTitle()) &&
                Objects.equals(mDate, meeting.getDate()) &&
                Objects.equals(mRoomName, meeting.getRoomName()) &&
                Objects.equals(mParticipants, meeting.getParticipantsArray()) &&
                Objects.equals(mDetailSubject, meeting.getSubject()) &&
                Objects.equals(mIcon, meeting.getIcon()) &&
                Objects.equals(mHour, meeting.getHour());
    }

    @Override
    public int hashCode() {
        return Objects.hash(mRoomName, mParticipants, mTitle, mDetailSubject, mIcon, mHour);

    }
}
