package com.example.pmu_projekat.objects;

import android.content.Context;
import android.graphics.Canvas;

public abstract class ChassisElement extends CarElement {

    protected CarElement weapon = null;
    protected CarElement wheelLeft = null;
    protected CarElement wheelRight = null;
    protected double factor = 1.5;

    protected int weaponCenterX;
    protected int weaponCenterY;

    protected int leftWheelCenterX;
    protected int leftWheelCenterY;
    protected int rightWheelCenterX;
    protected int rightWheelCenterY;

    protected void drawWheels(Canvas canvas)
    {
        if (wheelLeft != null)
        {
            wheelLeft.setX(x + (int)(leftWheelCenterX * factor) - (wheelLeft.getWidth()/2));
            wheelLeft.setY(y + (int)(leftWheelCenterY * factor) - (wheelLeft.getHeight()/2));
            wheelLeft.draw(canvas);
        }

        if (wheelRight != null)
        {
            wheelRight.setX(x + (int)(rightWheelCenterX * factor) - (wheelRight.getWidth()/2));
            wheelRight.setY(y + (int)(rightWheelCenterY * factor) - (wheelRight.getHeight()/2));
            wheelRight.draw(canvas);
        }
    }

    public ChassisElement(Context context, int x, int y) {
        super(context, x, y);
    }

    public CarElement getWeapon() {
        return weapon;
    }

    public void setWeapon(CarElement weapon) {
        this.weapon = weapon;
    }

    public CarElement getWheelLeft() {
        return wheelLeft;
    }

    public void setWheelLeft(CarElement wheelLeft) {
        this.wheelLeft = wheelLeft;
    }

    public CarElement getWheelRight() {
        return wheelRight;
    }

    public void setWheelRight(CarElement wheelRight) {
        this.wheelRight = wheelRight;
    }
}
