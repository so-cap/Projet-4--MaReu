package com.sophie.mareu.ui.list_meetings;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sophie.mareu.R;
import com.sophie.mareu.model.Meeting;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity {
    private Meeting mMeeting;

    // to remove
    @BindView(R.id.textview)
    TextView txt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        if (getIntent().hasExtra("meeting")){
            mMeeting = getIntent().getParcelableExtra("meeting");
            txt.setText(mMeeting.getRoomName());
        }

    }

    //
    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
