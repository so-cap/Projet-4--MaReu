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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.sophie.mareu.R;
import com.sophie.mareu.model.Meeting;

import static com.sophie.mareu.Constants.*;


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

    public void setOnDeleteMeetingListener(OnDeleteMeetingListener onDeleteMeetingListener){
        mOnDeleteMeetingListener = onDeleteMeetingListener;
    }

    public void setOnMeetingClickListener(OnMeetingClickListener onMeetingClickListener){
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

        //TODO: deplacer la méthode dans ListMeetingFragment , envoyer l'interface
     /*   private void startDetailFragment() {
            AppCompatActivity activity = (AppCompatActivity) mContext;
            FragmentTransaction fm = activity.getSupportFragmentManager().beginTransaction();
            DetailFragment detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(ARGUMENT_MEETING, mMeetings.get(getAdapterPosition()));
            detailFragment.setArguments(bundle);

            if ((activity.findViewById((R.id.frame_setmeeting)) == null && !res.getString(R.string.screen_type).equals("tablet"))) {
                fm.replace(R.id.frame_listmeetings, detailFragment).addToBackStack(null).commit();
            } else fm.replace(R.id.frame_setmeeting, detailFragment).addToBackStack(null).commit();
        }

      */
    }


    public interface OnDeleteMeetingListener {
        void onDeleteMeetingClick(Meeting meeting);
    }

    public interface OnMeetingClickListener {
        void onMeetingClick(Meeting meeting);
    }
}
