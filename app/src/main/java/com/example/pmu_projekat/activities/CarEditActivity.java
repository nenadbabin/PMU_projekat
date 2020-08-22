package com.example.pmu_projekat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
import com.example.pmu_projekat.media_player.MyMusicPlayer;
import com.example.pmu_projekat.objects.CarElement;
import com.example.pmu_projekat.objects.ChassisElement;
import com.example.pmu_projekat.objects.chassis.ChassisBoulder;
import com.example.pmu_projekat.objects.chassis.ChassisClassic;
import com.example.pmu_projekat.objects.chassis.ChassisWhale;
import com.example.pmu_projekat.objects.weapon.Blade;
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

    private TextView carPowerTV;
    private TextView carHealthTV;
    private TextView carEnergyTV;
    private TextView componentPowerTV;
    private TextView componentHealthTV;
    private TextView componentEnergyTV;

    private CarEditView carEditView;

    private int position = 0;

    private int frameX0;
    private int frameX1;
    private int frameY0;
    private int frameY1;

    private boolean music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_edit);

        availableComponents = new ArrayList<>();

        final AppDatabase appDatabase = AppDatabase.getDatabase(this);

        id = MySharedPreferences.getInt(this, Constants.ID_STRING);
        username = MySharedPreferences.getString(this, Constants.USERNAME_STRING);

        ImageView leftArrowIV = findViewById(R.id.arrow_left_iv);
        ImageView rightArrowIV = findViewById(R.id.arrow_right_iv);

        carEditView = findViewById(R.id.car_edit_view);

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

        /*Intent intent = getIntent();
        music = intent.getBooleanExtra("music", false);*/

        /*if (MyMusicPlayer.getTokens() == 1)
        {
            Intent myIntent = new Intent(CarEditActivity.this, MyMusicPlayer.class);
            myIntent.putExtra("action", MyMusicPlayer.PLAY_TRACK);
            startService(myIntent);
        }*/

        Button saveButton = findViewById(R.id.btn_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        appDatabase.warehouseDao().clearActiveChassisForUser(CarEditActivity.this.id);
                        appDatabase.warehouseDao().clearActiveWeaponForUser(CarEditActivity.this.id);
                        appDatabase.warehouseDao().clearActiveWheelForUser(CarEditActivity.this.id);

                        ChassisElement car = carEditView.getCar();

                        if (car != null)
                        {
                            long chassisID = car.getDatabaseID();
                            appDatabase.warehouseDao().updateActiveChassisForUser(CarEditActivity.this.id, chassisID);

                            if (car.getWeapon() != null)
                            {
                                CarElement weapon = car.getWeapon();
                                appDatabase.warehouseDao().updateActiveWeaponForUser(CarEditActivity.this.id, weapon.getDatabaseID());
                            }

                            if (car.getWheelLeft() != null)
                            {
                                CarElement wheel = car.getWheelLeft();
                                appDatabase.warehouseDao().updateActiveWheelForUser(CarEditActivity.this.id, wheel.getDatabaseID());
                            }
                        }

                        CarEditActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CarEditActivity.this, "Car successfully saved", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).start();
            }
        });

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

                        chassisElement.setDatabaseID(chassis.getId());

                        chassisElement.setHealth(chassis.getHealth());
                        chassisElement.setEnergy(chassis.getEnergy());

                        WarehouseWeapon activeWeapon = appDatabase.warehouseDao().getActiveWeaponForUser(CarEditActivity.this.id);

                        if (activeWeapon != null) {
                            Weapon weapon = appDatabase.carElementsDao().getWeapon(activeWeapon.getIdWeapon());

                            weaponElement = createWeapon(carEditView.getContext(), weapon);

                            weaponElement.setDatabaseID(weapon.getId());

                            weaponElement.setPower(weapon.getPower());
                            weaponElement.setHealth(weapon.getHealth());
                            weaponElement.setEnergy(weapon.getEnergy());

                            chassisElement.setWeapon(weaponElement);

                        }

                        WarehouseWheel activeWheel = appDatabase.warehouseDao().getActiveWheelForUser(CarEditActivity.this.id);

                        if (activeWheel != null) {
                            Wheel wheel = appDatabase.carElementsDao().getWheel(activeWheel.getIdWheel());

                            leftWheelElement = createWheel(carEditView.getContext(), wheel);
                            rightWheelElement = createWheel(carEditView.getContext(), wheel);

                            leftWheelElement.setDatabaseID(wheel.getId());
                            rightWheelElement.setDatabaseID(wheel.getId());

                            leftWheelElement.setHealth(wheel.getHealth());
                            rightWheelElement.setHealth(wheel.getHealth());

                            chassisElement.setWheelLeft(leftWheelElement);
                            chassisElement.setWheelRight(rightWheelElement);

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
                        chassisElement.setDatabaseID(chassis.getId());
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
                        weaponElement.setDatabaseID(weapon.getId());
                        weaponElement.setPower(weapon.getPower());
                        weaponElement.setHealth(weapon.getHealth());
                        weaponElement.setEnergy(weapon.getEnergy());

                        if (weaponElement.getElementIdentity() == Constants.WPN_BLADE)
                        {
                            weaponElement.setFactor(0.8);
                        }

                        if (weaponElement.getElementIdentity() == Constants.WPN_ROCKET)
                        {
                            weaponElement.setFactor(0.8);
                        }

                        availableComponents.add(weaponElement);
                    }

                    List<WarehouseWheel> wheelForUser = appDatabase.warehouseDao().getWheelForUser(CarEditActivity.this.id);
                    for (int i = 0; i < wheelForUser.size(); i++)
                    {
                        long idWheel = wheelForUser.get(i).getIdWheel();
                        Wheel wheel = appDatabase.carElementsDao().getWheel(idWheel);
                        CarElement wheelElement = createWheel(carEditView.getContext(), wheel);
                        wheelElement.setDatabaseID(wheel.getId());
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
                        checkIfCarIsTouched(x, y);
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

    public static ChassisElement createChassis (Context context, Chassis chassis)
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

    public static CarElement createWeapon (Context context, Weapon weapon)
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
            case "blade": {
                weaponElement = new Blade(context, 0, 0);
                break;
            }
        }

        return weaponElement;
    }

    public static CarElement createWheel (Context context, Wheel wheel)
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
                            updateCar(carElement);
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
                            updateCar(carElement);
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
                            updateCar(carElement);
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

    private void updateCar(CarElement carElement)
    {
        if (carElement.getElementType() == Constants.TYPE_WEAPON)
        {

            if (carEditView.getCar() == null)
            {
                Toast.makeText(this, "You need chassis first", Toast.LENGTH_SHORT).show();
                return;
            }

            CarElement ce = null;
            switch (carElement.getElementIdentity())
            {
                case Constants.WPN_CHAINSAW : {
                    ce = new Chainsaw(carEditView.getContext(), 0,0);
                    break;
                }
                case Constants.WPN_ROCKET : {
                    ce = new Rocket(carEditView.getContext(), 0,0);
                    break;
                }
                case Constants.WPN_STINGER : {
                    ce = new Stinger(carEditView.getContext(), 0,0);
                    break;
                }
                case Constants.WPN_BLADE : {
                    ce = new Blade(carEditView.getContext(), 0,0);
                    break;
                }
            }
            ce.setDatabaseID(carElement.getDatabaseID());
            ce.setPower(carElement.getPower());
            ce.setHealth(carElement.getHealth());
            ce.setEnergy(carElement.getEnergy());

            if (carEditView.getCar().getEnergy() < ce.getEnergy())
            {
                Toast.makeText(this, "Chassis doesn't have enough energy to support this weapon", Toast.LENGTH_SHORT).show();
                return;
            }

            carEditView.getCar().setWeapon(ce);
        }
        else if (carElement.getElementType() == Constants.TYPE_WHEEL)
        {
            if (carEditView.getCar() == null)
            {
                Toast.makeText(this, "You need chassis first", Toast.LENGTH_SHORT).show();
                return;
            }

            CarElement wl = null;
            CarElement wr = null;
            switch (carElement.getElementIdentity())
            {
                case Constants.WHL_KNOB : {
                    wl = new Knob(carEditView.getContext(), 0,0);
                    wr = new Knob(carEditView.getContext(), 0,0);
                    break;
                }
                case Constants.WHL_SCOOTER : {
                    wl = new Scooter(carEditView.getContext(), 0,0);
                    wr = new Scooter(carEditView.getContext(), 0,0);
                    break;
                }
                case Constants.WHL_TYRE : {
                    wl = new Tyre(carEditView.getContext(), 0,0);
                    wr = new Tyre(carEditView.getContext(), 0,0);
                    break;
                }
            }
            wl.setDatabaseID(carElement.getDatabaseID());
            wr.setDatabaseID(carElement.getDatabaseID());
            wl.setHealth(carElement.getHealth());
            wr.setHealth(carElement.getHealth());
            carEditView.getCar().setWheelLeft(wl);
            carEditView.getCar().setWheelRight(wr);
        }
        else if (carElement.getElementType() == Constants.TYPE_CHASSIS)
        {
            ChassisElement ce = null;
            switch (carElement.getElementIdentity())
            {
                case Constants.C_BOULDER : {
                    ce = new ChassisBoulder(carEditView.getContext(), 0, 0);
                    break;
                }
                case Constants.C_CLASSIC : {
                    ce = new ChassisClassic(carEditView.getContext(), 0, 0);
                    break;
                }
                case Constants.C_WHALE : {
                    ce = new ChassisWhale(carEditView.getContext(), 0, 0);
                    break;
                }
            }

            ChassisElement oldCar = carEditView.getCar();
            if (oldCar != null)
            {
                ce.setWeapon(oldCar.getWeapon());
                ce.setWheelLeft(oldCar.getWheelLeft());
                ce.setWheelRight(oldCar.getWheelRight());
            }

            ce.setDatabaseID(carElement.getDatabaseID());
            ce.setHealth(carElement.getHealth());
            ce.setEnergy(carElement.getEnergy());

            if (oldCar != null)
            {
                if (oldCar.getWeapon() != null)
                {
                    if (ce.getEnergy() < oldCar.getWeapon().getEnergy())
                    {
                        Toast.makeText(this, "Chassis doesn't have enough energy to support this weapon", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            carEditView.setCar(ce);
        }

        ChassisElement car = carEditView.getCar();
        updateCarAbility(car);
    }

    private void checkIfCarIsTouched(int x, int y)
    {
        ChassisElement car = carEditView.getCar();
        if (car != null)
        {
            if (car.getWheelLeft() != null && car.getWheelRight() != null)
            {
                if (car.getWheelLeft().isTouched(x, y) || car.getWheelRight().isTouched(x, y))
                {
                    car.setWheelLeft(null);
                    car.setWheelRight(null);
                    updateCarAbility(car);
                    return;
                }
            }
            if (car.getWeapon() != null)
            {
                if (car.getWeapon().isTouched(x, y))
                {
                    car.setWeapon(null);
                    updateCarAbility(car);
                    return;
                }
            }

            if (car.getWeapon() == null && car.getWheelLeft() == null && car.getWheelRight() == null)
            {
                if (car.isTouched(x,y))
                {
                    carEditView.setCar(null);
                    updateCarAbility(null);
                    return;
                }
            }
        }
    }

    private void updateCarAbility(ChassisElement car)
    {
        carEnergyTV.setText("0");
        carPowerTV.setText("0");
        carHealthTV.setText("0");

        if (car != null)
        {
            int health = 0;
            carEnergyTV.setText("" + car.getEnergy());
            health += car.getHealth();

            if (car.getWeapon() != null) {
                carPowerTV.setText("" + car.getWeapon().getPower());
                health += car.getWeapon().getHealth();
            }

            if (car.getWheelLeft() != null) {
                health += car.getWheelLeft().getHealth();
            }

            if (car.getWheelRight() != null) {
                health += car.getWheelRight().getHealth();
            }
            carHealthTV.setText("" + health);
        }
    }

    private void updateComponentTextViews(CarElement carElement)
    {
        componentPowerTV.setText("" + carElement.getPower());
        componentEnergyTV.setText("" + carElement.getEnergy());
        componentHealthTV.setText("" + carElement.getHealth());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (MyMusicPlayer.getTokens() == 1)
        {
            Intent intent = new Intent(this, MyMusicPlayer.class);
            intent.putExtra("action", MyMusicPlayer.STOP_TRACK);
            startService(intent);

            intent = new Intent(this, MyMusicPlayer.class);
            intent.putExtra("action", MyMusicPlayer.RESET_PLAYER);
            startService(intent);

            intent = new Intent(CarEditActivity.this, MyMusicPlayer.class);
            intent.putExtra("action", MyMusicPlayer.PLAY_TRACK);
            startService(intent);

            MyMusicPlayer.addToken();
        }

        Log.d(Constants.MAIN_ACTIVITY_DEBUG_TAG, "CAE : onResume() " + MyMusicPlayer.getTokens());
    }

    @Override
    protected void onPause() {
        super.onPause();

        MyMusicPlayer.removeToken();

        if (MyMusicPlayer.getTokens() == 1)
        {
            Intent intent = new Intent(this, MyMusicPlayer.class);
            stopService(intent);
        }

        Log.d(Constants.MAIN_ACTIVITY_DEBUG_TAG, "CAE : onPause() " + MyMusicPlayer.getTokens());
    }

    @Override
    public void onBackPressed() {
        if (MyMusicPlayer.getTokens() > 0)
        {
            MyMusicPlayer.addToken();
        }

        Log.d(Constants.MAIN_ACTIVITY_DEBUG_TAG, "CAE : onBackPressed() " + MyMusicPlayer.getTokens());

        finish();
    }


}