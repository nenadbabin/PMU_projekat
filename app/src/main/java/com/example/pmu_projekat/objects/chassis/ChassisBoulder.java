package com.example.pmu_projekat.objects.chassis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.pmu_projekat.R;
import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.objects.ChassisElement;
import com.example.pmu_projekat.objects.weapon.Blade;
import com.example.pmu_projekat.objects.weapon.Chainsaw;
import com.example.pmu_projekat.objects.weapon.Rocket;
import com.example.pmu_projekat.objects.weapon.Stinger;

public class ChassisBoulder extends ChassisElement {

    public ChassisBoulder (Context context, int x, int y) {
        super(context, x, y);
        this.width = 105;
        this.height = 109;

        this.weaponCenterX = 50;
        this.weaponCenterY = 13;
        this.leftWheelCenterX = 20;
        this.leftWheelCenterY = 99;
        this.rightWheelCenterX = 87;
        this.rightWheelCenterY = 99;

        this.leftWheelCenterXReverse = 17;
        this.leftWheelCenterYReverse = 98;
        this.rightWheelCenterXReverse = 84;
        this.rightWheelCenterYReverse = 98;

        this.d = context.getResources().getDrawable(R.drawable.c_boulder, null);
    }

    @Override
    public void draw(Canvas canvas) {

        d.setBounds(x, y, x + (int)(width * factor), y + (int)(height * factor));
        d.draw(canvas);

        if (weapon != null)
        {
            weapon.setX(x + getWidth() / 2);
            weapon.setY(y + getHeight() / 2);

            if (weapon instanceof Stinger)
            {
                weapon.setFactor(1.3);
                weapon.setX(weapon.getX() - weapon.getWidth() / 4);
                weapon.setY(weapon.getY() - weapon.getHeight() / 2);

                if (isReverse)
                {
                    weapon.setX(weapon.getX() - 130);
                }
            }
            else if (weapon instanceof Rocket)
            {
                weapon.setFactor(0.6);
                weapon.setX(weapon.getX() - weapon.getWidth() / 2);
                weapon.setY(weapon.getY() - weapon.getHeight() / 2);

                if (isReverse)
                {
                    weapon.setX(weapon.getX() - 10);
                }
            }
            else if (weapon instanceof Chainsaw)
            {
                weapon.setFactor(1.3);
                weapon.setX(weapon.getX() - weapon.getWidth() / 4);
                weapon.setY(weapon.getY() - weapon.getHeight() / 2);

                if (isReverse)
                {
                    weapon.setX(weapon.getX() - 130);
                }
            }
            else if (weapon instanceof Blade)
            {
                weapon.setFactor(0.8);
                weapon.setX(weapon.getX() - weapon.getWidth() / 10);
                weapon.setY(weapon.getY() - weapon.getHeight() / 2);

                if (isReverse)
                {
                    weapon.setX(weapon.getX() - 140);
                }
            }

            weapon.draw(canvas);
        }

        drawWheels(canvas);

        calculateBounds();
    }

    @Override
    public int getElementType() {
        return Constants.TYPE_CHASSIS;
    }

    @Override
    public int getElementIdentity() {
        return Constants.C_BOULDER;
    }

    @Override
    public void setReverse(boolean reverse) {
        super.setReverse(reverse);
        d = context.getResources().getDrawable(R.drawable.c_boulder_reverse, null);
    }
}
