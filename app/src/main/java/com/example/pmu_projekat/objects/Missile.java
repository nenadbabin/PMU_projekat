package com.example.pmu_projekat.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Missile {

    private int x;
    private int y;

    public static final int height = 20;
    public static final int width = 30;

    private Rect rect;
    private Paint paint;

    public Missile(int x, int y) {
        this.x = x;
        this.y = y;

        rect = new Rect(x, y, x + width, y + height);
        paint = new Paint();
        paint.setColor(Color.GRAY);
    }

    public void draw (Canvas canvas)
    {
        rect.set(x, y, x + width, y + height);
        canvas.drawRect(rect, paint);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rect getRect() {
        return rect;
    }
}
