package com.example.pmu_projekat.objects.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.pmu_projekat.R;
import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.objects.CarElement;

public class Scooter extends CarElement {

    public Scooter (Context context, int x, int y) {
        super(context, x, y);
        this.width = 79;
        this.height = 84;
    }

    @Override
    public void draw (Canvas canvas) {
        Drawable d = context.getResources().getDrawable(R.drawable.scooter, null);
        d.setBounds(x, y, x + (int)(width * factor), y + (int)(height * factor));
        d.draw(canvas);
    }

    @Override
    public int getElementType() {
        return Constants.TYPE_WHEEL;
    }

    @Override
    public int getElementIdentity() {
        return Constants.WHL_SCOOTER;
    }
}
