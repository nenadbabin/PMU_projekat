package com.example.pmu_projekat.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.objects.ChassisElement;

import java.util.ArrayList;
import java.util.List;

public class BattleView extends View {

    private boolean userControl = false;

    private ChassisElement carP1;
    private ChassisElement carP2;

    private int bottomLine = (int) (Constants.SCREEN_HEIGHT * 0.8);

    private boolean isYCalculated = false;

    public BattleView(Context context) {
        super(context);
    }

    public BattleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BattleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isUserControl() {
        return userControl;
    }

    public void setUserControl(boolean userControl) {
        this.userControl = userControl;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (carP1 != null)
        {
            carP1.draw(canvas);
        }

        if (carP2 != null)
        {
            carP2.draw(canvas);
        }

        if (!isYCalculated)
        {
            setY();
        }
    }

    public ChassisElement getCarP1() {
        return carP1;
    }

    public void setCarP1(ChassisElement car) {
        this.carP1 = car;
        carP1.setX((int) (Constants.SCREEN_WIDTH * 0.1));
        carP1.setY(bottomLine - carP1.getHeight());
    }

    public ChassisElement getCarP2() {
        return carP2;
    }

    public void setCarP2(ChassisElement car) {
        this.carP2 = car;
        carP2.setX((int) (Constants.SCREEN_WIDTH * 0.8));
        carP2.setY(bottomLine - carP2.getHeight());
    }

    public void update()
    {

        if (!doCarsCollide())
        {
            carP1.setX(carP1.getX() + 2);
            carP2.setX(carP2.getX() - 2);
        }
        else
        {
            if (carP1.getEnergy() > carP2.getEnergy())
            {
                int step = /*(carP1.getEnergy() - carP2.getEnergy()) / 2*/ 1;
                carP1.setX(carP1.getX() + step);
                carP2.setX(carP2.getX() + step);
            }

            if (carP1.getEnergy() < carP2.getEnergy())
            {
                int step = /*(carP2.getEnergy() - carP1.getEnergy()) / 2*/ 1;
                carP1.setX(carP1.getX() - step);
                carP2.setX(carP2.getX() - step);
            }

        }
    }

    public void setY()
    {
        if (carP1 != null)
        {
            carP1.setY(bottomLine - (carP1.getBoundBottom() - carP1.getBoundTop()));
            if (carP1.getBoundBottom() != 0)
            {
                isYCalculated = true;
            }
            //Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "c1: " + carP1.getBoundTop() + " " + carP1.getBoundBottom());
        }

        if (carP2 != null)
        {
            carP2.setY(bottomLine - (carP2.getBoundBottom() - carP2.getBoundTop()));
            //Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "c2: " + carP2.getBoundTop() + " " + carP2.getBoundBottom());
        }

        //Log.d(Constants.BATTLE_ACTIVITY_DEBUG_TAG, "bottom line: " + bottomLine);
    }

    private boolean doCarsCollide()
    {
        List<Rect> carElemList1 = new ArrayList<>();
        List<Rect> carElemList2 = new ArrayList<>();

        Rect chassisRect1 = new Rect(carP1.getX(), carP1.getY(), carP1.getX() + carP1.getWidth(), carP1.getY() + carP1.getHeight());
        Rect chassisRect2 = new Rect(carP2.getX(), carP2.getY(), carP2.getX() + carP2.getWidth(), carP2.getY() + carP2.getHeight());

        carElemList1.add(chassisRect1);
        carElemList2.add(chassisRect2);


        /*if (carP1.getWeapon() != null)
        {
            Rect weaponRect1 = new Rect(carP1.getWeapon().getX(), carP1.getWeapon().getY(), carP1.getWeapon().getX() + carP1.getWeapon().getWidth(), carP1.getWeapon().getY() + carP1.getWeapon().getHeight());
            carElemList1.add(weaponRect1);
        }

        if (carP2.getWeapon() != null)
        {
            Rect weaponRect2 = new Rect(carP2.getWeapon().getX(), carP2.getWeapon().getY(), carP2.getWeapon().getX() + carP2.getWeapon().getWidth(), carP2.getWeapon().getY() + carP2.getWeapon().getHeight());
            carElemList2.add(weaponRect2);
        }*/



        if (carP1.getWheelLeft() != null)
        {
            Rect wheelLeftRect1 = new Rect(carP1.getWheelLeft().getX(), carP1.getWheelLeft().getY(), carP1.getWheelLeft().getX() + carP1.getWheelLeft().getWidth(), carP1.getWheelLeft().getY() + carP1.getWheelLeft().getHeight());
            carElemList1.add(wheelLeftRect1);
        }

        if (carP2.getWheelLeft() != null)
        {
            Rect wheelLeftRect2 = new Rect(carP2.getWheelLeft().getX(), carP2.getWheelLeft().getY(), carP2.getWheelLeft().getX() + carP2.getWheelLeft().getWidth(), carP2.getWheelLeft().getY() + carP2.getWheelLeft().getHeight());
            carElemList2.add(wheelLeftRect2);
        }



        if (carP1.getWheelRight() != null)
        {
            Rect wheelRightRect1 = new Rect(carP1.getWheelRight().getX(), carP1.getWheelRight().getY(), carP1.getWheelRight().getX() + carP1.getWheelRight().getWidth(), carP1.getWheelRight().getY() + carP1.getWheelRight().getHeight());
            carElemList1.add(wheelRightRect1);
        }

        if (carP2.getWheelRight() != null)
        {
            Rect wheelRightRect2 = new Rect(carP2.getWheelRight().getX(), carP2.getWheelRight().getY(), carP2.getWheelRight().getX() + carP2.getWheelRight().getWidth(), carP2.getWheelRight().getY() + carP2.getWheelRight().getHeight());
            carElemList2.add(wheelRightRect2);
        }


        for (int i = 0; i < carElemList1.size(); i++)
        {
            for (int j = 0; j < carElemList2.size(); j++)
            {
                Rect r1 = carElemList1.get(i);
                Rect r2 = carElemList2.get(j);

                if (r1.intersect(r2))
                {
                    return true;
                }

                if (r2.intersect(r1))
                {
                    return true;
                }
            }
        }

        return false;
    }


}
