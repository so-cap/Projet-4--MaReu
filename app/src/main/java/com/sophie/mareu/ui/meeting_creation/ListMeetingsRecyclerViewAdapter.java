package com.sophie.mareu.ui.meeting_creation;

import android.content.Context;
import android.content.res.Resources;
import android.provider.ContactsContract;
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
import com.sophie.mareu.ui.MeetingsApi;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SOPHIE on 28/12/2019.
 */

public class ListMeetingsRecyclerViewAdapter extends RecyclerView.Adapter<ListMeetingsRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Meeting> mMeetings;
    private Context mContext;
    private static int iconSelector;
    private ArrayList iconList = new ArrayList<>(Arrays.asList(R.drawable.ic_lightpink, R.drawable.ic_lightgreen,R.drawable.ic_darkergreen));

    public ListMeetingsRecyclerViewAdapter(ArrayList<Meeting> meetings, Context context) {
        mMeetings = meetings;
        mContext = context;
    }

    @NonNull
    @Override
    public ListMeetingsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_meeting, parent, false);
        iconSelector++;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListMeetingsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(mMeetings.get(position));
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleView)
        TextView mTitle;
        @BindView(R.id.participants)
        TextView mParticipants;
        @BindView(R.id.room_icon)
        ImageView mIcon;
        @BindView(R.id.ic_delete)
        ImageButton mDeleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Meeting meeting) {
            Resources res = mContext.getResources();
            if(iconSelector == 3)
                iconSelector = 0;

            mIcon.setImageDrawable(res.getDrawable((int)iconList.get(iconSelector)));
            mTitle.setText(res.getString(R.string.title_hour_room, meeting.getTitle(), meeting.getHour(), meeting.getRoomName()));
            mParticipants.setText(meeting.getParticipants());
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new DeleteMeetingEvent(meeting));
                }
            });
        }
    }
}
