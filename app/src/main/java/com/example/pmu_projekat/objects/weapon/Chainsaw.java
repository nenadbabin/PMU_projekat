package com.example.pmu_projekat.objects.weapon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.pmu_projekat.R;
import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.objects.CarElement;

public class Chainsaw extends CarElement {

    public Chainsaw (Context context, int x, int y) {
        super(context, x, y);
        this.width = 144;
        this.height = 56;
    }

    @Override
    public void draw(Canvas canvas) {
        Drawable d = context.getResources().getDrawable(R.drawable.chainsaw, null);
        d.setBounds(x, y, x + (int)(width * factor), y + (int)(height * factor));
        d.draw(canvas);
    }

    @Override
    public int getElementType() {
        return Constants.TYPE_WEAPON;
    }

    @Override
    public int getElementIdentity() {
        return Constants.WPN_CHAINSAW;
    }
}
