package com.sophie.mareu.ui.list_meetings;

import android.content.Context;
import android.content.Intent;
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
import com.sophie.mareu.event.DeleteMeetingEvent;
import com.sophie.mareu.model.Meeting;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SOPHIE on 28/12/2019.
 */

public class ListMeetingsRecyclerViewAdapter extends RecyclerView.Adapter<ListMeetingsRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Meeting> mMeetings;
    private Context mContext;

    ListMeetingsRecyclerViewAdapter(ArrayList<Meeting> meetings, Context context) {
        mMeetings = meetings;
        mContext = context;
    }

    @NonNull
    @Override
    public ListMeetingsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_meeting, parent, false);
        return new ViewHolder(view);
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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Meeting meeting) {
            Resources res = mContext.getResources();

            mIcon.setImageDrawable(res.getDrawable(meeting.getIcon()));
            mTitle.setText(res.getString(R.string.title_hour_room, meeting.getTitle(), meeting.getHour().getValue(), meeting.getRoomName()));
            mParticipants.setText(meeting.getParticipants());
            mDeleteButton.setOnClickListener(v -> EventBus.getDefault().post(new DeleteMeetingEvent(meeting)));
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra("meeting", mMeetings.get(getAdapterPosition()));
            mContext.startActivity(intent);
        }
    }
}
