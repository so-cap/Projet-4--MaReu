package com.sophie.mareu.ui.list_meetings;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sophie.mareu.R;
import com.sophie.mareu.model.Meeting;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SOPHIE on 28/12/2019.
 */

public class ListMeetingsRecyclerViewAdapter extends RecyclerView.Adapter<ListMeetingsRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Meeting> meetings;
    private OnDeleteMeetingListener onDeleteMeetingListener;
    private OnMeetingClickListener onMeetingClickListener;

    ListMeetingsRecyclerViewAdapter(ArrayList<Meeting> meetings) {
        this.meetings = meetings;
    }

    @NonNull
    @Override
    public ListMeetingsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_meeting, parent, false);
        return new ViewHolder(view, onDeleteMeetingListener, onMeetingClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListMeetingsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(meetings.get(position));
    }

    @Override
    public int getItemCount() {
        if (meetings == null)
            return 0;
        else
            return meetings.size();
    }

    void setOnDeleteMeetingListener(OnDeleteMeetingListener onDeleteMeetingListener){
        this.onDeleteMeetingListener = onDeleteMeetingListener;
    }

    void setOnMeetingClickListener(OnMeetingClickListener onMeetingClickListener){
        this.onMeetingClickListener = onMeetingClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.titleView)
        TextView title;
        @BindView(R.id.participants)
        TextView participants;
        @BindView(R.id.meeting_icon)
        ImageView icon;
        @BindView(R.id.delete_meeting_btn)
        ImageButton deleteButton;

        OnDeleteMeetingListener onDeleteMeetingListener;
        OnMeetingClickListener onMeetingClickListener;


        ViewHolder(@NonNull View itemView, OnDeleteMeetingListener onDeleteMeetingListener, OnMeetingClickListener onMeetingClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            this.onDeleteMeetingListener = onDeleteMeetingListener;
            this.onMeetingClickListener = onMeetingClickListener;
        }

        void bind(Meeting meeting) {
            Resources res = itemView.getResources();
            icon.setImageDrawable(res.getDrawable(meeting.getIcon()));
            title.setText(res.getString(R.string.title_hour_room, meeting.getTitle(), meeting.getHour().getValue(), meeting.getRoomName()));
            participants.setText(meeting.getParticipantsInOneString());
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.delete_meeting_btn)
                onDeleteMeetingListener.onDeleteMeetingClick(meetings.get(getAdapterPosition()));
            else
                onMeetingClickListener.onMeetingClick(meetings.get(getAdapterPosition()));
        }
    }


    public interface OnDeleteMeetingListener {
        void onDeleteMeetingClick(Meeting meeting);
    }

    public interface OnMeetingClickListener {
        void onMeetingClick(Meeting meeting);
    }
}
