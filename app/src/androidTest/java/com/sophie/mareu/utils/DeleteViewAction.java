package com.sophie.mareu.utils;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.sophie.mareu.R;

import org.hamcrest.Matcher;

/**
 * Created by SOPHIE on 17/01/2020.
 */
public class DeleteViewAction implements ViewAction {
    @Override
    public Matcher<View> getConstraints() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Click on specific button";
    }

    @Override
    public void perform(UiController uiController, View view) {
        ImageButton button = view.findViewById(R.id.delete_meeting_btn);
        button.performClick();
    }
}
