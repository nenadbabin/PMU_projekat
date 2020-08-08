package com.example.pmu_projekat.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.pmu_projekat.objects.CarElement;
import com.example.pmu_projekat.objects.ChassisElement;

public class CarEditView extends View {

    private CarElement carElements[] = {null, null, null};
    private int xCoordinates [] = {200, 450, 700};
    private int yCoordinates [] = {50, 50, 50};

    private ChassisElement car = null;
    private int frameX0;
    private int frameX1;
    private int frameY0;
    private int frameY1;

    public CarEditView(Context context) {
        super(context);
    }

    public CarEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CarEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < 3; i++)
        {
            if (carElements[i] != null)
            {
                carElements[i].draw(canvas);
            }
        }

        if (car != null)
        {
            int middleX = (frameX1 + frameX0) / 2;
            int middleY = (frameY1 + frameY0) / 2;
            car.setX(middleX - (car.getWidth() / 2) - 20);
            car.setY(middleY - (car.getHeight() / 2) - 20);
            car.draw(canvas);
        }

    }

    public void setCarElements(CarElement[] carElements) {
        this.carElements = carElements;

        for (int i = 0; i < 3; i++)
        {
            if (carElements[i] != null)
            {
                carElements[i].setX(xCoordinates[i]);
                carElements[i].setY(yCoordinates[i]);
            }
        }
    }

    public ChassisElement getCar() {
        return car;
    }

    public void setCar(ChassisElement car) {
        this.car = car;
    }

    public void setFrame (int left, int top, int right, int bottom)
    {
        this.frameX0 = left;
        this.frameX1 = right;
        this.frameY0 = top;
        this.frameY1 = bottom;
    }
}
