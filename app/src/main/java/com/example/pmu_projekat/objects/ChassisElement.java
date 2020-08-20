package com.example.pmu_projekat.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.example.pmu_projekat.constants.Constants;

public abstract class ChassisElement extends CarElement {

    protected CarElement weapon = null;
    protected CarElement wheelLeft = null;
    protected CarElement wheelRight = null;

    protected int weaponCenterX;
    protected int weaponCenterY;

    protected int leftWheelCenterX;
    protected int leftWheelCenterY;
    protected int rightWheelCenterX;
    protected int rightWheelCenterY;

    protected int leftWheelCenterXReverse;
    protected int leftWheelCenterYReverse;
    protected int rightWheelCenterXReverse;
    protected int rightWheelCenterYReverse;

    protected int boundLeft, boundTop, boundRight, boundBottom;

    protected void drawWheels(Canvas canvas)
    {
        if (!isReverse)
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
        else
        {
            if (wheelLeft != null)
            {
                wheelLeft.setX(x + (int)(leftWheelCenterXReverse * factor) - (wheelLeft.getWidth()/2));
                wheelLeft.setY(y + (int)(leftWheelCenterYReverse * factor) - (wheelLeft.getHeight()/2));
                wheelLeft.draw(canvas);
            }

            if (wheelRight != null)
            {
                wheelRight.setX(x + (int)(rightWheelCenterXReverse * factor) - (wheelRight.getWidth()/2));
                wheelRight.setY(y + (int)(rightWheelCenterYReverse * factor) - (wheelRight.getHeight()/2));
                wheelRight.draw(canvas);
            }
        }
    }

    public ChassisElement(Context context, int x, int y) {
        super(context, x, y);
        factor = 1.5;
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

    public void calculateBounds()
    {
        //Calculating left margin
        int tmp = x;

        if (weapon != null)
        {
            if (weapon.getX() < tmp)
            {
                tmp = weapon.getX();
            }
        }

        if (wheelLeft != null)
        {
            if (wheelLeft.getX() < tmp)
            {
                tmp = wheelLeft.getX();
            }
        }

        if (wheelRight != null)
        {
            if (wheelRight.getX() < tmp)
            {
                tmp = wheelRight.getX();
            }
        }

        boundLeft = tmp;

        // Calculating top margin
        tmp = y;

        if (weapon != null)
        {
            if (weapon.getY() < tmp)
            {
                tmp = weapon.getY();
            }
        }

        if (wheelLeft != null)
        {
            if (wheelLeft.getY() < tmp)
            {
                tmp = wheelLeft.getY();
            }
        }

        if (wheelRight != null)
        {
            if (wheelRight.getY() < tmp)
            {
                tmp = wheelRight.getY();
            }
        }

        boundTop = tmp;

        // Calculating right margin
        tmp = x + getWidth();

        if (weapon != null)
        {
            if (weapon.getX() + weapon.getWidth() > tmp)
            {
                tmp = weapon.getX() + weapon.getWidth();
            }
        }

        if (wheelLeft != null)
        {
            if (wheelLeft.getX() + wheelLeft.getWidth() > tmp)
            {
                tmp = wheelLeft.getX() + wheelLeft.getWidth();
            }
        }

        if (wheelRight != null)
        {
            if (wheelRight.getX() + wheelRight.getWidth() > tmp)
            {
                tmp = wheelRight.getX() + wheelRight.getWidth();
            }
        }

        boundRight = tmp;

        // Calculating bottom margin
        tmp = y + getHeight();

        if (weapon != null)
        {
            if (weapon.getY() + weapon.getHeight() > tmp)
            {
                tmp = weapon.getY() + weapon.getHeight();
            }
        }

        if (wheelLeft != null)
        {
            if (wheelLeft.getY() + wheelLeft.getHeight() > tmp)
            {
                tmp = wheelLeft.getY() + wheelLeft.getHeight();
            }
        }

        if (wheelRight != null)
        {
            if (wheelRight.getY() + wheelRight.getHeight() > tmp)
            {
                tmp = wheelRight.getY() + wheelRight.getHeight();
            }
        }

        boundBottom = tmp;

        //Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "" + boundTop + " " + boundBottom);
    }

    public int getBoundLeft() {
        return boundLeft;
    }

    public int getBoundTop() {
        return boundTop;
    }

    public int getBoundRight() {
        return boundRight;
    }

    public int getBoundBottom() {
        return boundBottom;
    }

    public int getCarHealth()
    {
        int carHealth = getHealth();

        if (weapon != null)
        {
            carHealth += weapon.getHealth();
        }

        if (wheelLeft != null)
        {
            carHealth += wheelLeft.getHealth();
        }

        if (wheelRight != null)
        {
            carHealth += wheelRight.getHealth();
        }

        return carHealth;
    }
}
