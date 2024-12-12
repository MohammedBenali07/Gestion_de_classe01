package com.example.gestiondeclasse;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

public class CustomCalendarView extends CalendarView {

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCalendarView(Context context) {
        super(context);
        init();
    }

    private void init() {
        // Access and style switchers
        int prevButtonId = Resources.getSystem().getIdentifier("prev", "id", "android");
        int nextButtonId = Resources.getSystem().getIdentifier("next", "id", "android");

        ImageButton prevButton = findViewById(prevButtonId);
        ImageButton nextButton = findViewById(nextButtonId);

        if (prevButton != null) {
            prevButton.setBackgroundResource(R.drawable.background_inputs);
        }

        if (nextButton != null) {
            nextButton.setBackgroundResource(R.drawable.background_inputs);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // Access and customize calendar title and days here
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof TextView) {
                TextView dayTextView = (TextView) child;
                dayTextView.setTextColor(Color.BLACK); // Change text color
                dayTextView.setTextSize(22); // Increase text size, change value as needed
            }
        }
    }
}
