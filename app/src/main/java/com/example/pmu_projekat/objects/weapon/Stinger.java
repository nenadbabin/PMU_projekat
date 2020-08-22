package com.example.pmu_projekat.objects.weapon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.pmu_projekat.R;
import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.objects.CarElement;

public class Stinger extends CarElement {

    public Stinger (Context context, int x, int y) {
        super(context, x, y);
        this.width = 150;
        this.height = 39;

        this.d = context.getResources().getDrawable(R.drawable.stinger, null);
    }

    @Override
    public void draw(Canvas canvas) {
        d.setBounds(x, y, x + (int)(width * factor), y + (int)(height * factor));
        d.draw(canvas);
    }

    @Override
    public int getElementType() {
        return Constants.TYPE_WEAPON;
    }

    @Override
    public int getElementIdentity() {
        return Constants.WPN_STINGER;
    }

    @Override
    public void setReverse(boolean reverse) {
        super.setReverse(reverse);
        d = context.getResources().getDrawable(R.drawable.stinger_reverse, null);
    }
}
