package com.example.pmu_projekat.objects;

import android.content.Context;

public abstract class ChassisElement extends CarElement {

    protected CarElement weapon = null;
    protected CarElement wheelLeft = null;
    protected CarElement wheelRight = null;
    protected double factor = 1.5;

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
