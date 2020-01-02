package com.sophie.mareu.controller;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

public class CustomRadioGroupLayout {
    private List<RadioButton> radios = new ArrayList<>();

    private static final String TAG = "LOGGCustomRadioGroup";

    public CustomRadioGroupLayout(){
    }

    public void addRadioButtonToTracker(ArrayList<RadioButton> radioButton) {
        for (RadioButton rb : radioButton) {
            this.radios.add(rb);
            rb.setOnClickListener(onClick);
        }
    }

    public int getCheckedRadioButtonId() {
        int checkedId = -1;

        for (RadioButton rb : radios){
            if (rb.isChecked()){
                return rb.getId(); }
        }
        return checkedId;
    }

    /**
     * This occurs everytime when one of RadioButtons is clicked,
     * and deselects all others in the group.
     */
    private OnClickListener onClick = new OnClickListener() {

        @Override
        public void onClick(View v) {

            // let's deselect all radios in group
            for (RadioButton rb : radios) {
                    rb.setChecked(false);
            }

            // now let's select currently clicked RadioButton
            if (v.getClass().equals(RadioButton.class)) {
                RadioButton rb = (RadioButton) v;
                rb.setChecked(true);
            }

        }
    };
}

