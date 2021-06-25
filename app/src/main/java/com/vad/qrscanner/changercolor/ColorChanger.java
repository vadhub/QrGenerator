package com.vad.qrscanner.changercolor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

public class ColorChanger extends View {

    private int color;
    private Paint mPaint;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public ColorChanger(Context context) {
        super(context);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(color==0){
            color = Color.RED;
        }
        mPaint.setColor(color);
        float cx, cy;
        float radius = getHeight()/2;
        cx = (float) (radius*2);
        cy = radius;

        canvas.drawCircle(cx, cy, radius, mPaint);
        invalidate();
    }
}
