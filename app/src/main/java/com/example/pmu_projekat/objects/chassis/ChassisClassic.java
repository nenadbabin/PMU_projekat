package com.example.pmu_projekat.objects.chassis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.pmu_projekat.R;
import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.objects.CarElement;
import com.example.pmu_projekat.objects.ChassisElement;
import com.example.pmu_projekat.objects.weapon.Chainsaw;
import com.example.pmu_projekat.objects.weapon.Rocket;
import com.example.pmu_projekat.objects.weapon.Stinger;

public class ChassisClassic extends ChassisElement {

    public ChassisClassic(Context context, int x, int y) {
        super(context, x, y);
        this.width = 128;
        this.height = 93;

        this.weaponCenterX = 29;
        this.weaponCenterY = 18;
        this.leftWheelCenterX = 34;
        this.leftWheelCenterY = 80;
        this.rightWheelCenterX = 105;
        this.rightWheelCenterY = 80;

        this.leftWheelCenterXReverse = 22;
        this.leftWheelCenterYReverse = 80;
        this.rightWheelCenterXReverse = 93;
        this.rightWheelCenterYReverse = 80;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isReverse)
        {
            Drawable d = context.getResources().getDrawable(R.drawable.c_classic, null);
            d.setBounds(x, y, x + (int)(width * factor), y + (int)(height * factor));
            d.draw(canvas);
        }
        else
        {
            Drawable d = context.getResources().getDrawable(R.drawable.c_classic_reverse, null);
            d.setBounds(x, y, x + (int)(width * factor), y + (int)(height * factor));
            d.draw(canvas);
        }

        if (weapon != null)
        {
            weapon.setX(x);
            weapon.setY(y);

            if (weapon instanceof Stinger || weapon instanceof Chainsaw)
            {
                weapon.setFactor(1.5);

                if (isReverse)
                {
                    weapon.setX(weapon.getX() - 60);
                }
            }
            else if (weapon instanceof Rocket)
            {
                weapon.setFactor(0.6);

                if (isReverse)
                {
                    weapon.setX(weapon.getX() + 80);
                }
            }

            /*canvas.save();
            canvas.rotate(0, x, y + 10);
            weapon.draw(canvas);
            canvas.restore();*/
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
        return Constants.C_CLASSIC;
    }
}
