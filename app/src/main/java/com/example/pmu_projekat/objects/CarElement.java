package com.example.pmu_projekat.objects;

import android.content.Context;
import android.graphics.Canvas;

public abstract class CarElement {

    protected Context context;
    protected int x;
    protected int y;
    protected int height;
    protected int width;
    protected double factor = 1.0;

    public abstract void draw (Canvas canvas);

    public CarElement(Context context, int x, int y) {
        this.context = context;
        this.x = x;
        this.y = y;
    }

    public void moveX (int dx)
    {
        x += dx;
    }

    public void moveY (int dy)
    {
        y += dy;
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }
}
