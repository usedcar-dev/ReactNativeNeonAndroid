package com.gaadi.neon.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class DemoView extends View {
    public DemoView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //Draw rectangle;
        Path rect = new  Path();
        rect.addRect(0, 0,250, 150, Path.Direction.CW);
        Paint cpaint = new Paint();
        cpaint.setColor(Color.GREEN);
        canvas.drawPath(rect, cpaint);
    }
}
