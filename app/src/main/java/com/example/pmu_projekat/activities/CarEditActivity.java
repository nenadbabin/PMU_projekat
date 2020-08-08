package com.example.pmu_projekat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmu_projekat.R;
import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.database.AppDatabase;
import com.example.pmu_projekat.database.entities.Chassis;
import com.example.pmu_projekat.database.entities.WarehouseChassis;
import com.example.pmu_projekat.database.entities.WarehouseWeapon;
import com.example.pmu_projekat.database.entities.WarehouseWheel;
import com.example.pmu_projekat.database.entities.Weapon;
import com.example.pmu_projekat.database.entities.Wheel;
import com.example.pmu_projekat.objects.CarElement;
import com.example.pmu_projekat.objects.ChassisElement;
import com.example.pmu_projekat.objects.chassis.ChassisBoulder;
import com.example.pmu_projekat.objects.chassis.ChassisClassic;
import com.example.pmu_projekat.objects.chassis.ChassisWhale;
import com.example.pmu_projekat.objects.weapon.Chainsaw;
import com.example.pmu_projekat.objects.weapon.Rocket;
import com.example.pmu_projekat.objects.weapon.Stinger;
import com.example.pmu_projekat.objects.wheel.Knob;
import com.example.pmu_projekat.objects.wheel.Scooter;
import com.example.pmu_projekat.objects.wheel.Tyre;
import com.example.pmu_projekat.shared_preferences.MySharedPreferences;
import com.example.pmu_projekat.views.CarEditView;

import java.util.ArrayList;
import java.util.List;

public class CarEditActivity extends AppCompatActivity {

    private int id;
    private String username;

    private List<CarElement> availableComponents;
    private List<CarElement> usedComponents;

    private TextView carPowerTV;
    private TextView carHealthTV;
    private TextView carEnergyTV;
    private TextView componentPowerTV;
    private TextView componentHealthTV;
    private TextView componentEnergyTV;

    private int position = 0;

    private int frameX0;
    private int frameX1;
    private int frameY0;
    private int frameY1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_edit);

        availableComponents = new ArrayList<>();
        usedComponents = new ArrayList<>();

        final AppDatabase appDatabase = AppDatabase.getDatabase(this);

        id = MySharedPreferences.getInt(this, Constants.ID_STRING);
        username = MySharedPreferences.getString(this, Constants.USERNAME_STRING);

        ImageView leftArrowIV = findViewById(R.id.arrow_left_iv);
        ImageView rightArrowIV = findViewById(R.id.arrow_right_iv);

        final CarEditView carEditView = findViewById(R.id.car_edit_view);

        carPowerTV = findViewById(R.id.car_power_tv);
        carHealthTV = findViewById(R.id.car_health_tv);
        carEnergyTV = findViewById(R.id.car_energy_tv);

        componentPowerTV = findViewById(R.id.comp_power_tv);
        componentHealthTV = findViewById(R.id.comp_health_tv);
        componentEnergyTV = findViewById(R.id.comp_energy_tv);

        carPowerTV.setText("0");
        carHealthTV.setText("0");
        carEnergyTV.setText("0");
        componentPowerTV.setText("0");
        componentHealthTV.setText("0");
        componentEnergyTV.setText("0");

        final ImageView carFrame = findViewById(R.id.car_frame);

        carFrame.post(new Runnable() {
            @Override
            public void run() {
                frameX0 = (int) carFrame.getX();
                frameX1 = (int) (carFrame.getX() + carFrame.getMeasuredWidth());
                frameY0 = (int) carFrame.getY();
                frameY1 = (int) (carFrame.getY() + carFrame.getMeasuredHeight());

                carEditView.setFrame(frameX0, frameY0, frameX1, frameY1);
                carEditView.invalidate();
                Log.d(Constants.CAR_EDIT_ACTIVITY_DEBUG_TAG, "" + frameX0 + " " + frameX1 + " " + frameY0 + " " + frameY1);
            }
        });

        leftArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (availableComponents.size() > 3)
                {
                    if (position > 0)
                    {
                        position--;
                        CarElement carElements [] = updateComponentWindow(position);
                        carEditView.setCarElements(carElements);
                        carEditView.invalidate();
                    }
                }

            }
        });

        rightArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (availableComponents.size() > 3)
                {
                    if (position < availableComponents.size() - 3)
                    {
                        position++;
                        CarElement carElements [] = updateComponentWindow(position);
                        carEditView.setCarElements(carElements);
                        carEditView.invalidate();
                    }
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                // Create owner's car
                {
                    WarehouseChassis activeChassis = appDatabase.warehouseDao().getActiveChassisForUser(CarEditActivity.this.id);

                    if (activeChassis != null) {

                        ChassisElement chassisElement;
                        CarElement weaponElement = null;
                        CarElement leftWheelElement = null;
                        CarElement rightWheelElement = null;

                        Chassis chassis = appDatabase.carElementsDao().getChassis(activeChassis.getIdChassis());

                        chassisElement = createChassis(carEditView.getContext(), chassis);

                        chassisElement.setHealth(chassis.getHealth());
                        chassisElement.setEnergy(chassis.getEnergy());
                        usedComponents.add(chassisElement);

                        WarehouseWeapon activeWeapon = appDatabase.warehouseDao().getActiveWeaponForUser(CarEditActivity.this.id);

                        if (activeWeapon != null) {
                            Weapon weapon = appDatabase.carElementsDao().getWeapon(activeWeapon.getIdWeapon());

                            weaponElement = createWeapon(carEditView.getContext(), weapon);

                            weaponElement.setPower(weapon.getPower());
                            weaponElement.setHealth(weapon.getHealth());
                            weaponElement.setEnergy(weapon.getEnergy());

                            chassisElement.setWeapon(weaponElement);

                            usedComponents.add(weaponElement);

                        }

                        WarehouseWheel activeWheel = appDatabase.warehouseDao().getActiveWheelForUser(CarEditActivity.this.id);

                        if (activeWheel != null) {
                            Wheel wheel = appDatabase.carElementsDao().getWheel(activeWheel.getIdWheel());

                            leftWheelElement = createWheel(carEditView.getContext(), wheel);
                            rightWheelElement = createWheel(carEditView.getContext(), wheel);

                            leftWheelElement.setHealth(wheel.getHealth());
                            rightWheelElement.setHealth(wheel.getHealth());

                            chassisElement.setWheelLeft(leftWheelElement);
                            chassisElement.setWheelRight(rightWheelElement);

                            usedComponents.add(leftWheelElement);
                        }

                        int health = 0;

                        if (chassisElement != null) {
                            final ChassisElement finalChassisElement = chassisElement;
                            carEnergyTV.post(new Runnable() {
                                @Override
                                public void run() {
                                    carEnergyTV.setText("" + finalChassisElement.getEnergy());
                                }
                            });
                            health += chassisElement.getHealth();
                        }

                        if (weaponElement != null) {
                            final CarElement finalWeaponElement = weaponElement;
                            carPowerTV.post(new Runnable() {
                                @Override
                                public void run() {
                                    carPowerTV.setText("" + finalWeaponElement.getPower());
                                }
                            });
                            health += weaponElement.getHealth();
                        }

                        if (leftWheelElement != null) {
                            health += leftWheelElement.getHealth();
                        }

                        if (rightWheelElement != null) {
                            health += rightWheelElement.getHealth();
                        }

                        final int finalHealth = health;
                        carEnergyTV.post(new Runnable() {
                            @Override
                            public void run() {
                                carHealthTV.setText("" + finalHealth);
                            }
                        });

                        carEditView.setCar(chassisElement);
                        Log.d(Constants.CAR_EDIT_ACTIVITY_DEBUG_TAG, "Used components: " + usedComponents.size());
                    }
                } // end of create owner's car

                // Get other elements
                {
                    List<WarehouseChassis> chassisForUser = appDatabase.warehouseDao().getChassisForUser(CarEditActivity.this.id);
                    for (int i = 0; i < chassisForUser.size(); i++)
                    {
                        long idChassis = chassisForUser.get(i).getIdChassis();
                        Chassis chassis = appDatabase.carElementsDao().getChassis(idChassis);
                        ChassisElement chassisElement = createChassis(carEditView.getContext(), chassis);
                        chassisElement.setHealth(chassis.getHealth());
                        chassisElement.setEnergy(chassis.getEnergy());
                        availableComponents.add(chassisElement);
                    }

                    List<WarehouseWeapon> weaponForUser = appDatabase.warehouseDao().getWeaponForUser(CarEditActivity.this.id);
                    for (int i = 0; i < weaponForUser.size(); i++)
                    {
                        long idWeapon = weaponForUser.get(i).getIdWeapon();
                        Weapon weapon = appDatabase.carElementsDao().getWeapon(idWeapon);
                        CarElement weaponElement = createWeapon(carEditView.getContext(), weapon);
                        weaponElement.setPower(weapon.getPower());
                        weaponElement.setHealth(weapon.getHealth());
                        weaponElement.setEnergy(weapon.getEnergy());
                        availableComponents.add(weaponElement);
                    }

                    List<WarehouseWheel> wheelForUser = appDatabase.warehouseDao().getWheelForUser(CarEditActivity.this.id);
                    for (int i = 0; i < wheelForUser.size(); i++)
                    {
                        long idWheel = wheelForUser.get(i).getIdWheel();
                        Wheel wheel = appDatabase.carElementsDao().getWheel(idWheel);
                        CarElement wheelElement = createWheel(carEditView.getContext(), wheel);
                        wheelElement.setHealth(wheel.getHealth());
                        availableComponents.add(wheelElement);
                    }

                    Log.d(Constants.CAR_EDIT_ACTIVITY_DEBUG_TAG, "Avail components: " + availableComponents.size());
                }

                // add elements to CarEditView
                int limit = availableComponents.size() > 3 ? 3 : availableComponents.size();
                CarElement carElements [] = {null, null, null};

                for (int i = 0; i < limit; i++)
                {
                    carElements[i] = availableComponents.get(i);
                }

                carEditView.setCarElements(carElements);
                carEditView.invalidate();

            }
        }).start();

        carEditView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getActionMasked())
                {
                    case MotionEvent.ACTION_DOWN : {
                        checkIfSomeComponentIsTouched(x, y);
                        //Log.d(Constants.CAR_EDIT_ACTIVITY_DEBUG_TAG, "x: " + x + " y: " + y);
                        break;
                    }
                    case MotionEvent.ACTION_MOVE : {
                        checkIfSomeComponentIsMoved(x, y);
                        carEditView.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP : {
                        processAvailableElements();
                        carEditView.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

    }

    private ChassisElement createChassis (Context context, Chassis chassis)
    {
        ChassisElement chassisElement = null;
        switch (chassis.getName()) {
            case "classic": {
                chassisElement = new ChassisClassic(context, 0, 0);
                break;
            }
            case "whale": {
                chassisElement = new ChassisWhale(context, 0, 0);
                break;
            }
            case "boulder": {
                chassisElement = new ChassisBoulder(context, 0, 0);
                break;
            }
        }

        return chassisElement;
    }

    private CarElement createWeapon (Context context, Weapon weapon)
    {
        CarElement weaponElement = null;

        switch (weapon.getName()) {
            case "stringer": {
                weaponElement = new Stinger(context, 0, 0);
                break;
            }
            case "chainsaw": {
                weaponElement = new Chainsaw(context, 0, 0);
                break;
            }
            case "rocket": {
                weaponElement = new Rocket(context, 0, 0);
                break;
            }
        }

        return weaponElement;
    }

    private CarElement createWheel (Context context, Wheel wheel)
    {
        CarElement wheelElement = null;

        switch (wheel.getName()) {
            case "knob": {
                wheelElement = new Knob(context, 0, 0);
                break;
            }
            case "scooter": {
                wheelElement = new Scooter(context, 0, 0);
                break;
            }
            case "tyre": {
                wheelElement = new Tyre(context, 0, 0);
                break;
            }
        }

        return wheelElement;
    }

    private CarElement [] updateComponentWindow (int position)
    {
        CarElement carElements [] = {null, null, null};

        carElements[0] = availableComponents.get(position);
        carElements[1] = availableComponents.get(position + 1);
        carElements[2] = availableComponents.get(position + 2);

        return carElements;
    }

    private void checkIfSomeComponentIsTouched(int x, int y)
    {
        if (availableComponents.size() > 0)
        {
            CarElement carElement = availableComponents.get(position);
            //Log.d(Constants.CAR_EDIT_ACTIVITY_DEBUG_TAG, "" + carElement.getX() + " " + carElement.getY() + " " + carElement.getWidth() + " " + carElement.getHeight());
            //Log.d(Constants.CAR_EDIT_ACTIVITY_DEBUG_TAG, "" + carElement.isTouched(x, y));
            if (carElement.isTouched(x, y))
            {
                updateComponentTextViews(carElement);
                carElement.setMoving(true);
                carElement.positionStackPush(new Point(carElement.getX(), carElement.getY()));
                return;
            }
        }

        if (availableComponents.size() > 1)
        {
            CarElement carElement = availableComponents.get(position + 1);
            //Log.d(Constants.CAR_EDIT_ACTIVITY_DEBUG_TAG, "" + carElement.getX() + " " + carElement.getY() + " " + carElement.getWidth() + " " + carElement.getHeight());
            //Log.d(Constants.CAR_EDIT_ACTIVITY_DEBUG_TAG, "" + carElement.isTouched(x, y));
            if (carElement.isTouched(x, y))
            {
                updateComponentTextViews(carElement);
                carElement.setMoving(true);
                carElement.positionStackPush(new Point(carElement.getX(), carElement.getY()));
                return;
            }
        }

        if (availableComponents.size() >= 2)
        {
            CarElement carElement = availableComponents.get(position + 2);
            //Log.d(Constants.CAR_EDIT_ACTIVITY_DEBUG_TAG, "" + carElement.getX() + " " + carElement.getY() + " " + carElement.getWidth() + " " + carElement.getHeight());
            //Log.d(Constants.CAR_EDIT_ACTIVITY_DEBUG_TAG, "" + carElement.isTouched(x, y));
            if (carElement.isTouched(x, y))
            {
                updateComponentTextViews(carElement);
                carElement.setMoving(true);
                carElement.positionStackPush(new Point(carElement.getX(), carElement.getY()));
                return;
            }
        }
    }

    private void checkIfSomeComponentIsMoved(int x, int y)
    {
        if (availableComponents.size() > 0)
        {
            CarElement carElement = availableComponents.get(position);
            if (carElement.isTouched(x, y) && carElement.isMoving())
            {
                carElement.setX(x - (carElement.getWidth() / 2));
                carElement.setY(y - (carElement.getHeight() / 2));
                return;
            }
        }

        if (availableComponents.size() > 1)
        {
            CarElement carElement = availableComponents.get(position + 1);
            if (carElement.isTouched(x, y) && carElement.isMoving())
            {
                carElement.setX(x - (carElement.getWidth() / 2));
                carElement.setY(y - (carElement.getHeight() / 2));
                return;
            }
        }

        if (availableComponents.size() >= 2)
        {
            CarElement carElement = availableComponents.get(position + 2);
            if (carElement.isTouched(x, y) && carElement.isMoving())
            {
                carElement.setX(x - (carElement.getWidth() / 2));
                carElement.setY(y - (carElement.getHeight() / 2));
                return;
            }
        }
    }

    private void processAvailableElements()
    {
        if (availableComponents.size() > 0)
        {
            CarElement carElement = availableComponents.get(position);
            if (carElement.isMoving())
            {
                Point p = carElement.positionStackPop();
                if (p != null)
                {
                    if (carElement.getX() > frameX0 && carElement.getX() < frameX1)
                    {
                        if (carElement.getY() > frameY0 && carElement.getY() < frameY1)
                        {
                            Toast.makeText(this, "IN", Toast.LENGTH_SHORT).show();
                        }
                    }
                    carElement.setX(p.x);
                    carElement.setY(p.y);
                    carElement.setMoving(false);
                }
                return;
            }
        }

        if (availableComponents.size() > 1)
        {
            CarElement carElement = availableComponents.get(position + 1);
            if (carElement.isMoving())
            {
                Point p = carElement.positionStackPop();
                if (p != null)
                {
                    if (carElement.getX() > frameX0 && carElement.getX() < frameX1)
                    {
                        if (carElement.getY() > frameY0 && carElement.getY() < frameY1)
                        {
                            Toast.makeText(this, "IN", Toast.LENGTH_SHORT).show();
                        }
                    }
                    carElement.setX(p.x);
                    carElement.setY(p.y);
                    carElement.setMoving(false);
                }
                return;
            }
        }

        if (availableComponents.size() >= 2)
        {
            CarElement carElement = availableComponents.get(position + 2);
            if (carElement.isMoving())
            {
                Point p = carElement.positionStackPop();
                if (p != null)
                {
                    if (carElement.getX() > frameX0 && carElement.getX() < frameX1)
                    {
                        if (carElement.getY() > frameY0 && carElement.getY() < frameY1)
                        {
                            Toast.makeText(this, "IN", Toast.LENGTH_SHORT).show();
                        }
                    }
                    carElement.setX(p.x);
                    carElement.setY(p.y);
                    carElement.setMoving(false);
                }
                return;
            }
        }
    }

    private void updateComponentTextViews(CarElement carElement)
    {
        componentPowerTV.setText("" + carElement.getPower());
        componentEnergyTV.setText("" + carElement.getEnergy());
        componentHealthTV.setText("" + carElement.getHealth());
    }
}