package com.example.pmu_projekat.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.pmu_projekat.objects.ChassisElement;
import com.example.pmu_projekat.objects.chassis.ChassisBoulder;
import com.example.pmu_projekat.objects.weapon.Rocket;

public class CustomView extends View {

    ChassisElement classic;

    public CustomView(Context context) {
        super(context);
        init(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context)
    {
        classic = new ChassisBoulder(context, 100, 300);
        classic.setWeapon(new Rocket(context, 0 , 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        classic.draw(canvas);
    }
}
