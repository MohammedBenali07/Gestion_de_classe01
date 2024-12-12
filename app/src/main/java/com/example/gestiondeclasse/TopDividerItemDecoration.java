package com.example.gestiondeclasse;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class TopDividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Paint paint;

    public TopDividerItemDecoration(Context context) {
        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.grey)); // Set your desired color
        paint.setStrokeWidth(1); // Set your desired width
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getTop() - params.topMargin;
            canvas.drawLine(left, top, right, top, paint);
        }
    }
}

