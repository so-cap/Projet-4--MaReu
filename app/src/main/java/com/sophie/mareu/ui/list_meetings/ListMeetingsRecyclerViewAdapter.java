package com.sophie.mareu.ui.list_meetings;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private Context mContext;
    private OnDeleteMeetingListener mOnDeleteMeetingListener;

    ListMeetingsRecyclerViewAdapter(ArrayList<Meeting> meetings, Context context,
                                    OnDeleteMeetingListener onDeleteMeetingListener) {
        mMeetings = meetings;
        mContext = context;
        mOnDeleteMeetingListener = onDeleteMeetingListener;
    }

    @NonNull
    @Override
    public ListMeetingsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_meeting, parent, false);
        return new ViewHolder(view, mOnDeleteMeetingListener);
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.titleView)
        TextView mTitle;
        @BindView(R.id.participants)
        TextView mParticipants;
        @BindView(R.id.meeting_icon)
        ImageView mIcon;
        @BindView(R.id.ic_delete)
        ImageButton mDeleteButton;

        OnDeleteMeetingListener onDeleteMeetingListener;

        ViewHolder(@NonNull View itemView, OnDeleteMeetingListener onDeleteMeetingListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            this.onDeleteMeetingListener = onDeleteMeetingListener;
        }

        void bind(Meeting meeting) {
            Resources res = mContext.getResources();
            mIcon.setImageDrawable(res.getDrawable(meeting.getIcon()));
            mTitle.setText(res.getString(R.string.title_hour_room, meeting.getTitle(), meeting.getHour().getValue(), meeting.getRoomName()));
            mParticipants.setText(meeting.getParticipantsInOneString());
            mDeleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ic_delete)
                onDeleteMeetingListener.onDeleteMeetingClick(mMeetings.get(getAdapterPosition()));
            else {
                startDetailFragment();
            }
        }

        private void startDetailFragment() {
            AppCompatActivity activity = (AppCompatActivity) mContext;
            FragmentTransaction fm = activity.getSupportFragmentManager().beginTransaction();
            Fragment detailFragmentFrame = activity.getSupportFragmentManager()
                    .findFragmentById(R.id.frame_setmeeting);
            DetailFragment detailFragment = new DetailFragment();
            FloatingActionButton fab = activity.findViewById(R.id.fab);

            Bundle bundle = new Bundle();
            bundle.putParcelable("meeting", mMeetings.get(getAdapterPosition()));
            detailFragment.setArguments(bundle);

            if (detailFragmentFrame == null || fab != null)
                fm.replace(R.id.frame_listmeetings, detailFragment).addToBackStack(null).commit();
            else fm.replace(R.id.frame_setmeeting, detailFragment).addToBackStack(null).commit();
        }
    }

    public interface OnDeleteMeetingListener {
        void onDeleteMeetingClick(Meeting meeting);
    }
}
