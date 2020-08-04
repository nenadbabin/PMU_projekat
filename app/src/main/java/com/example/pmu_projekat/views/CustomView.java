package com.example.pmu_projekat.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.pmu_projekat.objects.ChassisElement;

public class CustomView extends View {

    ChassisElement car;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChassisElement getCar() {
        return car;
    }

    public void setCar(ChassisElement car) {
        this.car = car;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (car != null)
        {
            car.draw(canvas);
        }
    }
}
