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
    private ArrayList<Meeting> mMeetings;
    private OnDeleteMeetingListener mOnDeleteMeetingListener;
    private OnMeetingClickListener mOnMeetingClickListener;

    ListMeetingsRecyclerViewAdapter(ArrayList<Meeting> meetings) {
        mMeetings = meetings;
    }

    @NonNull
    @Override
    public ListMeetingsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_meeting, parent, false);
        return new ViewHolder(view, mOnDeleteMeetingListener, mOnMeetingClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListMeetingsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(mMeetings.get(position));
    }

    @Override
    public int getItemCount() {
        if (mMeetings == null)
            return 0;
        else
            return mMeetings.size();
    }

    void setOnDeleteMeetingListener(OnDeleteMeetingListener onDeleteMeetingListener){
        mOnDeleteMeetingListener = onDeleteMeetingListener;
    }

    void setOnMeetingClickListener(OnMeetingClickListener onMeetingClickListener){
        mOnMeetingClickListener = onMeetingClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.titleView)
        TextView mTitle;
        @BindView(R.id.participants)
        TextView mParticipants;
        @BindView(R.id.meeting_icon)
        ImageView mIcon;
        @BindView(R.id.delete_meeting_btn)
        ImageButton mDeleteButton;

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
            mIcon.setImageDrawable(res.getDrawable(meeting.getIcon()));
            mTitle.setText(res.getString(R.string.title_hour_room, meeting.getTitle(), meeting.getHour().getValue(), meeting.getRoomName()));
            mParticipants.setText(meeting.getParticipantsInOneString());
            mDeleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.delete_meeting_btn)
                onDeleteMeetingListener.onDeleteMeetingClick(mMeetings.get(getAdapterPosition()));
            else
                onMeetingClickListener.onMeetingClick(mMeetings.get(getAdapterPosition()));
        }
    }


    public interface OnDeleteMeetingListener {
        void onDeleteMeetingClick(Meeting meeting);
    }

    public interface OnMeetingClickListener {
        void onMeetingClick(Meeting meeting);
    }
}
