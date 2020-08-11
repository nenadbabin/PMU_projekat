package com.example.pmu_projekat.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;

import java.util.Stack;

public abstract class CarElement {

    protected Context context;
    protected int x;
    protected int y;
    protected int height;
    protected int width;
    protected double factor = 1.0;

    protected int power = 0;
    protected int health = 0;
    protected int energy = 0;

    protected boolean isMoving = false;

    Stack<Point> positionStack;

    protected long databaseID;

    public abstract void draw (Canvas canvas);
    public abstract int getElementType();
    public abstract int getElementIdentity();

    public CarElement(Context context, int x, int y) {
        this.context = context;
        this.x = x;
        this.y = y;

        positionStack = new Stack<>();
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
        return (int)(height * factor);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return (int)(width * factor);
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

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean isTouched(int x, int y)
    {
        if ((x > this.x) && (x < (this.x + this.getWidth())))
        {
            if ((y > this.y) && (y < (this.y + this.getHeight())))
            {
                return true;
            }
        }

        return false;
    }

    public void positionStackPush(Point p)
    {
        positionStack.push(p);
    }

    public Point positionStackPop()
    {
        if (positionStack.size() > 0)
        {
            return positionStack.pop();
        }
        else
        {
            return null;
        }
    }

    public long getDatabaseID() {
        return databaseID;
    }

    public void setDatabaseID(long databaseID) {
        this.databaseID = databaseID;
    }
}
