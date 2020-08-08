package com.example.pmu_projekat.objects.chassis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.pmu_projekat.R;
import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.objects.ChassisElement;
import com.example.pmu_projekat.objects.weapon.Chainsaw;
import com.example.pmu_projekat.objects.weapon.Rocket;
import com.example.pmu_projekat.objects.weapon.Stinger;

public class ChassisWhale extends ChassisElement {

    public ChassisWhale (Context context, int x, int y) {
        super(context, x, y);
        this.width = 129;
        this.height = 75;
        this.weaponCenterX = 40;
        this.weaponCenterY = 8;
        this.leftWheelCenterX = 18;
        this.leftWheelCenterY = 65;
        this.rightWheelCenterX = 75;
        this.rightWheelCenterY = 65;
    }

    @Override
    public void draw(Canvas canvas) {
        Drawable d = context.getResources().getDrawable(R.drawable.c_whale, null);
        d.setBounds(x, y, x + (int)(width * factor), y + (int)(height * factor));
        d.draw(canvas);

        if (weapon != null)
        {

            if (weapon instanceof Stinger)
            {
                weapon.setX(x + 30);
                weapon.setY(y - 10);
                weapon.setFactor(1.3);
            }
            else if (weapon instanceof Rocket)
            {
                weapon.setX(x + 25);
                weapon.setY(y - 10);
                weapon.setFactor(0.6);
            }
            else if (weapon instanceof Chainsaw)
            {
                weapon.setX(x + 30);
                weapon.setY(y - 10);
                weapon.setFactor(1.3);
            }

            /*canvas.save();
            canvas.rotate(0, x, y + 10);
            weapon.draw(canvas);
            canvas.restore();*/
            weapon.draw(canvas);
        }

        drawWheels(canvas);
    }

    @Override
    public int getElementType() {
        return Constants.TYPE_CHASSIS;
    }

    @Override
    public int getElementIdentity() {
        return Constants.C_WHALE;
    }
}
