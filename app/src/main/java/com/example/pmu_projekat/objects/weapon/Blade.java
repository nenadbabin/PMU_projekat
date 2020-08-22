package com.example.pmu_projekat.objects.weapon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.pmu_projekat.R;
import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.objects.CarElement;

public class Blade extends CarElement {

    private int degree = 0;

    public Blade (Context context, int x, int y) {
        super(context, x, y);
        this.width = 220;
        this.height = 115;

        this.d = context.getResources().getDrawable(R.drawable.blade, null);
    }

    @Override
    public void draw(Canvas canvas) {

        int oldX = x;
        int oldY = y;

        if (degree != 0)
        {
            canvas.save();

            if (!isReverse)
            {
                canvas.translate(x, y + getHeight() / 2);
                canvas.rotate(degree, 0,0);
                x = 0;
                y = - (getHeight() / 2);
            }
            else
            {
                canvas.translate(x + getWidth(), y + getHeight() / 2);
                canvas.rotate(degree, 0,0);
                x = - getWidth();
                y = - (getHeight() / 2);
            }

        }

        d.setBounds(x, y, x + (int)(width * factor), y + (int)(height * factor));
        d.draw(canvas);

        if (degree != 0)
        {
            canvas.restore();
        }

        x = oldX;
        y = oldY;
    }

    @Override
    public int getElementType() {
        return Constants.TYPE_WEAPON;
    }

    @Override
    public int getElementIdentity() {
        return Constants.WPN_BLADE;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    @Override
    public void setReverse(boolean reverse) {
        super.setReverse(reverse);
        d = context.getResources().getDrawable(R.drawable.blade_reverse, null);
    }
}
