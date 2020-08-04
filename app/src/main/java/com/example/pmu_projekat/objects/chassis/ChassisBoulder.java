package com.example.pmu_projekat.objects.chassis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.pmu_projekat.R;
import com.example.pmu_projekat.objects.ChassisElement;
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
    }

    @Override
    public void draw(Canvas canvas) {
        Drawable d = context.getResources().getDrawable(R.drawable.c_boulder, null);
        d.setBounds(x, y, x + (int)(width * factor), y + (int)(height * factor));
        d.draw(canvas);

        if (weapon != null)
        {
            weapon.setX(x + 30);
            weapon.setY(y);

            if (weapon instanceof Stinger)
            {
                weapon.setX(x + 30);
                weapon.setY(y - 10);
                weapon.setFactor(1.3);
            }
            else if (weapon instanceof Rocket)
            {
                weapon.setX(x + 40);
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
}
