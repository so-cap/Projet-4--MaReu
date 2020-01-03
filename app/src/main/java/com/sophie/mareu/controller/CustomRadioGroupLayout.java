package com.sophie.mareu.controller;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class CustomRadioGroupLayout implements View.OnClickListener {
    private List<RadioButton> radios = new ArrayList<>();

    public CustomRadioGroupLayout() {
    }

    public void addRadioButtonToTracker(ArrayList<RadioButton> radioButton) {
        for (RadioButton rb : radioButton) {
            this.radios.add(rb);
            rb.setOnClickListener(this);
        }
    }

    public int getCheckedRadioButtonId() {
        int checkedId = -1;

        for (RadioButton rb : radios) {
            if (rb.isChecked()) {
                return rb.getId();
            }
        }
        return checkedId;
    }

    @Override
    public void onClick(View v) {
        // deselect all RadioButtons in group
        for (RadioButton rb : radios) {
            rb.setChecked(false);
        }
        // select currently clicked RadioButton
        RadioButton rb = (RadioButton) v;
        rb.setChecked(true);
    }

    public void unselectButton(int checkedRadioButtonId) {
        int i = 0;

        if (checkedRadioButtonId >= 0) {
            for (RadioButton rb : radios) {
                if (radios.get(i++) == radios.get(checkedRadioButtonId)) {
                    rb.setChecked(false);
                }
            }
        }
    }
}