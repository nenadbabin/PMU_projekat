package com.example.pmu_projekat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmu_projekat.ChestUtility;
import com.example.pmu_projekat.R;
import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.database.AppDatabase;
import com.example.pmu_projekat.database.entities.Chassis;
import com.example.pmu_projekat.database.entities.Chest;
import com.example.pmu_projekat.database.entities.User;
import com.example.pmu_projekat.database.entities.WarehouseChassis;
import com.example.pmu_projekat.database.entities.WarehouseWeapon;
import com.example.pmu_projekat.database.entities.WarehouseWheel;
import com.example.pmu_projekat.database.entities.Weapon;
import com.example.pmu_projekat.database.entities.Wheel;
import com.example.pmu_projekat.dialogs.SettingsDialog;
import com.example.pmu_projekat.dialogs.SettingsReturnValue;
import com.example.pmu_projekat.dialogs.StatisticsDialog;
import com.example.pmu_projekat.media_player.MyMusicPlayer;
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
import com.example.pmu_projekat.views.CustomView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GarageActivity extends AppCompatActivity implements SettingsReturnValue {

    private String username;
    private int id;
    private User user;

    private Thread chestThread;

    private AppDatabase appDatabase;

    private List<ChestUtility> chestList;

    private List<CarElement> carElementList;

    private ImageView chest0IV;
    private ImageView chest1IV;
    private ImageView chest2IV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);

        appDatabase = AppDatabase.getDatabase(this);

        this.id = MySharedPreferences.getInt(this, Constants.ID_STRING);
        this.username = MySharedPreferences.getString(this, Constants.USERNAME_STRING);

        this.carElementList = new ArrayList<>();

        Button btnPlay = findViewById(R.id.btn_play);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GarageActivity.this, BattleActivity.class);
                startActivity(intent);
            }
        });

        appDatabase.userDao().getUserById(id).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                GarageActivity.this.user = user;

                Button settingButton = findViewById(R.id.btn_setting);

                settingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SettingsDialog sd = new SettingsDialog(new boolean[]{GarageActivity.this.user.isMusic(), GarageActivity.this.user.isUserControl()});
                        sd.show(getSupportFragmentManager(), "Settings fragment");
                    }
                });

                Button statisticsButton = findViewById(R.id.btn_statistics);

                statisticsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StatisticsDialog sd = new StatisticsDialog(GarageActivity.this.user.getNumberOfBattles(), GarageActivity.this.user.getNumberOfWins());
                        sd.show(getSupportFragmentManager(), "Statistics fragment");
                    }
                });

                if (GarageActivity.this.user.isMusic() == true)
                {
                    Intent intent = new Intent(GarageActivity.this, MyMusicPlayer.class);
                    intent.putExtra("action", MyMusicPlayer.STOP_TRACK);
                    startService(intent);

                    intent = new Intent(GarageActivity.this, MyMusicPlayer.class);
                    intent.putExtra("action", MyMusicPlayer.PLAY_TRACK);
                    startService(intent);
                }

                int number_of_wins = GarageActivity.this.user.getNumberOfWins();
                ProgressBar progressBar = findViewById(R.id.progressBar);

                if (number_of_wins % 3 == 0)
                {
                    progressBar.setProgress(0);
                }
                else if (number_of_wins % 3 == 1)
                {
                    progressBar.setProgress(33);
                }
                else if (number_of_wins % 3 == 2)
                {
                    progressBar.setProgress(66);
                }
            }
        });

        // dohvatanje svih elementata
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Chassis> allChassis = appDatabase.carElementsDao().getAllChassis();
                for (int i = 0; i < allChassis.size(); i++)
                {
                    ChassisElement chassis = CarEditActivity.createChassis(null, allChassis.get(i));
                    chassis.setDatabaseID(allChassis.get(i).getId());
                    carElementList.add(chassis);
                }

                List<Weapon> allWeapons = appDatabase.carElementsDao().getAllWeapons();
                for (int i = 0; i < allWeapons.size(); i++)
                {
                    CarElement weapon = CarEditActivity.createWeapon(null, allWeapons.get(i));
                    weapon.setDatabaseID(allWeapons.get(i).getId());
                    carElementList.add(weapon);
                }

                List<Wheel> allWheels = appDatabase.carElementsDao().getAllWheels();
                for (int i = 0; i < allWheels.size(); i++)
                {
                    CarElement wheel = CarEditActivity.createWheel(null, allWheels.get(i));
                    wheel.setDatabaseID(allWheels.get(i).getId());
                    carElementList.add(wheel);
                }
            }
        }).start();

        chest0IV = findViewById(R.id.chest_1_iv);
        chest1IV = findViewById(R.id.chest_2_iv);
        chest2IV = findViewById(R.id.chest_3_iv);

        chest0IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chestList.size() >= 1)
                {
                    if (chestList.get(0).isReady())
                    {
                        openBox(chestList.get(0), 0);
                    }
                    else
                    {
                        Toast.makeText(GarageActivity.this, "Wait! This chest is not ready yet.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        chest1IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chestList.size() >= 2)
                {
                    if (chestList.get(1).isReady())
                    {
                        openBox(chestList.get(1), 1);
                    }
                    else
                    {
                        Toast.makeText(GarageActivity.this, "Wait! This chest is not ready yet.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        chest2IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chestList.size() >= 3)
                {
                    if (chestList.get(2).isReady())
                    {
                        openBox(chestList.get(2), 1);
                    }
                    else
                    {
                        Toast.makeText(GarageActivity.this, "Wait! This chest is not ready yet.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        final CustomView carView = findViewById(R.id.car_view);

        carView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GarageActivity.this, CarEditActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean makeAGift(CarElement ce1)
    {
        switch (ce1.getElementType())
        {
            case Constants.TYPE_CHASSIS : {
                List<WarehouseChassis> chassisForUser = appDatabase.warehouseDao().getChassisForUser(GarageActivity.this.id);
                boolean ok = true;

                for (int p = 0; p < chassisForUser.size(); p++)
                {
                    if (chassisForUser.get(p).getIdChassis() == ce1.getDatabaseID())
                    {
                        ok = false;
                    }
                }

                if (ok == true)
                {
                    WarehouseChassis wc1 = new WarehouseChassis(GarageActivity.this.id, ce1.getDatabaseID(), false);
                    appDatabase.warehouseDao().insert(wc1);
                }

                return ok;
            }
            case Constants.TYPE_WEAPON : {
                List<WarehouseWeapon> weaponForUser = appDatabase.warehouseDao().getWeaponForUser(GarageActivity.this.id);
                boolean ok = true;
                for (int p = 0; p < weaponForUser.size(); p++)
                {
                    if (weaponForUser.get(p).getIdWeapon() == ce1.getDatabaseID())
                    {
                        ok = false;
                    }
                }
                if (ok == true)
                {
                    WarehouseWeapon wc1 = new WarehouseWeapon(GarageActivity.this.id, ce1.getDatabaseID(), false);
                    appDatabase.warehouseDao().insert(wc1);
                }
                return ok;
            }
            case Constants.TYPE_WHEEL : {
                List<WarehouseWheel> wheelForUser = appDatabase.warehouseDao().getWheelForUser(GarageActivity.this.id);
                boolean ok = true;
                for (int p = 0; p < wheelForUser.size(); p++)
                {
                    if (wheelForUser.get(p).getIdWheel() == ce1.getDatabaseID())
                    {
                        ok = false;
                    }
                }
                if (ok == true)
                {
                    WarehouseWheel wc1 = new WarehouseWheel(GarageActivity.this.id, ce1.getDatabaseID(), false);
                    appDatabase.warehouseDao().insert(wc1);
                }
                return ok;
            }
        }

        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        /*if (GarageActivity.this.user != null && GarageActivity.this.user.isMusic() == true) {
            Intent intent = new Intent(this, MyMusicPlayer.class);
            stopService(intent);
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        chestThread.interrupt();

        if (GarageActivity.this.user != null && GarageActivity.this.user.isMusic() == true) {
            Intent intent = new Intent(this, MyMusicPlayer.class);
            stopService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (GarageActivity.this.user != null && GarageActivity.this.user.isMusic() == true)
        {
            Intent intent = new Intent(this, MyMusicPlayer.class);
            intent.putExtra("action", MyMusicPlayer.STOP_TRACK);
            startService(intent);

            intent = new Intent(GarageActivity.this, MyMusicPlayer.class);
            intent.putExtra("action", MyMusicPlayer.PLAY_TRACK);
            startService(intent);
        }

        // dohvatanje svih kovcega
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Chest> chestsForUsername = appDatabase.chestDao().getChestsForUsername(id);
                final int numberOfChests = chestsForUsername.size();

                chestList = new ArrayList<>();

                for (int i = 0; i < numberOfChests; i++)
                {
                    ChestUtility chestUtility = new ChestUtility();
                    if (chestsForUsername.get(i).getTimeToOpen() <= new Date().getTime())
                    {
                        chestUtility.setTime(0);
                    }
                    else
                    {
                        chestUtility.setTime(chestsForUsername.get(i).getTimeToOpen() - new Date().getTime());
                    }
                    chestUtility.setDatabaseID(chestsForUsername.get(i).getId());
                    chestUtility.setInterfacePosition(i);
                    chestList.add(chestUtility);
                }

                chestThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (true)
                            {
                                Thread.sleep(1000);
                                for (int i = 0; i < chestList.size(); i++) {
                                    ChestUtility chest = chestList.get(i);

                                    if (chest.getTime() > 0) {
                                        long time = chest.getTime() - 1000;
                                        chest.setTime(time);

                                        int interfacePosition = chest.getInterfacePosition();

                                        final int seconds = (int) (time / 1000) % 60 ;
                                        final int minutes = (int) ((time / (1000 * 60)) % 60);
                                        final int hours   = (int) ((time / (1000 * 60 * 60)) % 24);

                                        final TextView tv;
                                        switch (interfacePosition) {
                                            case 0: {
                                                tv = findViewById(R.id.tv_chest_1);
                                                break;
                                            }
                                            case 1: {
                                                tv = findViewById(R.id.tv_chest_2);
                                                break;
                                            }
                                            case 2: {
                                                tv = findViewById(R.id.tv_chest_3);
                                                break;
                                            }
                                            default: {
                                                tv = null;
                                            }
                                        }

                                        tv.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                tv.setText(hours + ":" + minutes + ":" + seconds);
                                            }
                                        });

                                        if (chest.isReady()) {

                                        }
                                    }
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });

                chestThread.start();

                {
                    GarageActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ImageView imageView = findViewById(R.id.chest_1_iv);
                            imageView.setImageResource(R.drawable.toolbox_empty);
                            imageView = findViewById(R.id.chest_2_iv);
                            imageView.setImageResource(R.drawable.toolbox_empty);
                            imageView = findViewById(R.id.chest_3_iv);
                            imageView.setImageResource(R.drawable.toolbox_empty);
                            TextView tv = findViewById(R.id.tv_chest_1);
                            tv.setText("00:00:00");
                            tv = findViewById(R.id.tv_chest_2);
                            tv.setText("00:00:00");
                            tv = findViewById(R.id.tv_chest_3);
                            tv.setText("00:00:00");

                            if (numberOfChests >= 1)
                            {
                                ImageView iv = findViewById(R.id.chest_1_iv);
                                iv.setImageResource(R.drawable.toolbox_attack);
                            }

                            if (numberOfChests >= 2)
                            {
                                ImageView iv = findViewById(R.id.chest_2_iv);
                                iv.setImageResource(R.drawable.toolbox_attack);
                            }

                            if (numberOfChests == 3)
                            {
                                ImageView iv = findViewById(R.id.chest_3_iv);
                                iv.setImageResource(R.drawable.toolbox_attack);
                            }
                        }
                    });
                }

            }
        }).start();

        final CustomView carView = findViewById(R.id.car_view);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ChassisElement chassisElement = createCar(carView, GarageActivity.this.id, appDatabase);
                carView.setCar(chassisElement);
                carView.invalidate();
            }
        }).start();
    }


    @Override
    public void onReturn(final boolean isMusic, final boolean isUserControl) {
        user.setMusic(isMusic);
        user.setUserControl(isUserControl);

        if (isMusic == true)
        {
            Intent intent = new Intent(this, MyMusicPlayer.class);
            intent.putExtra("action", MyMusicPlayer.STOP_TRACK);
            startService(intent);

            intent = new Intent(this, MyMusicPlayer.class);
            intent.putExtra("action", MyMusicPlayer.PLAY_TRACK);
            startService(intent);
        }
        else
        {
            Intent intent = new Intent(this, MyMusicPlayer.class);
            intent.putExtra("action", MyMusicPlayer.STOP_TRACK);
            startService(intent);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.userDao().updateUserPreferences(GarageActivity.this.id, isMusic, isUserControl);
            }
        }).start();
    }

    public static ChassisElement createCar(View carView, int id, AppDatabase database)
    {
        WarehouseChassis activeChassis = database.warehouseDao().getActiveChassisForUser(id);

        if (activeChassis != null) {
            ChassisElement chassisElement = null;

            Chassis chassis = database.carElementsDao().getChassis(activeChassis.getIdChassis());

            switch (chassis.getName()) {
                case "classic": {
                    chassisElement = new ChassisClassic(carView.getContext(), 150, 0);
                    break;
                }
                case "whale": {
                    chassisElement = new ChassisWhale(carView.getContext(), 150, 0);
                    break;
                }
                case "boulder": {
                    chassisElement = new ChassisBoulder(carView.getContext(), 150, 0);
                    break;
                }
            }

            chassisElement.setHealth(chassis.getHealth());
            chassisElement.setEnergy(chassis.getEnergy());

            WarehouseWeapon activeWeapon = database.warehouseDao().getActiveWeaponForUser(id);

            if (activeWeapon != null) {
                Weapon weapon = database.carElementsDao().getWeapon(activeWeapon.getIdWeapon());

                CarElement weaponElement = null;

                switch (weapon.getName()) {
                    case "stringer": {
                        weaponElement = new Stinger(carView.getContext(), 0, 0);
                        break;
                    }
                    case "chainsaw": {
                        weaponElement = new Chainsaw(carView.getContext(), 0, 0);
                        break;
                    }
                    case "rocket": {
                        weaponElement = new Rocket(carView.getContext(), 0, 0);
                        break;
                    }
                }

                weaponElement.setPower(weapon.getPower());
                weaponElement.setHealth(weapon.getHealth());
                weaponElement.setEnergy(weapon.getEnergy());

                chassisElement.setWeapon(weaponElement);
            }

            WarehouseWheel activeWheel = database.warehouseDao().getActiveWheelForUser(id);

            if (activeWheel != null) {
                Wheel wheel = database.carElementsDao().getWheel(activeWheel.getIdWheel());

                CarElement leftWheelElement = null;
                CarElement rightWheelElement = null;

                switch (wheel.getName()) {
                    case "knob": {
                        leftWheelElement = new Knob(carView.getContext(), 0, 0);
                        rightWheelElement = new Knob(carView.getContext(), 0, 0);
                        break;
                    }
                    case "scooter": {
                        leftWheelElement = new Scooter(carView.getContext(), 0, 0);
                        rightWheelElement = new Scooter(carView.getContext(), 0, 0);
                        break;
                    }
                    case "tyre": {
                        leftWheelElement = new Tyre(carView.getContext(), 0, 0);
                        rightWheelElement = new Tyre(carView.getContext(), 0, 0);
                        break;
                    }
                }

                leftWheelElement.setHealth(wheel.getHealth());
                rightWheelElement.setHealth(wheel.getHealth());

                chassisElement.setWheelLeft(leftWheelElement);
                chassisElement.setWheelRight(rightWheelElement);
            }

            return chassisElement;
        }

        return null;
    }

    private void openBox(final ChestUtility chest, final int boxPosition)
    {
        Collections.shuffle(carElementList);

        final CarElement ce1 = carElementList.get(0);
        CarElement ce2  = null;

        while (ce2 == null)
        {
            Collections.shuffle(carElementList);
            ce2 = carElementList.get(0);

            if (ce2.getElementIdentity() == ce1.getElementIdentity() || ce2.getElementType() == ce1.getElementType())
            {
                ce2 = null;
            }
        }

        Log.d(Constants.GARAGE_ACTIVITY_DEBUG_TAG, "Elements: " + ce1.getElementIdentity() + " " + ce2.getElementIdentity());

        final CarElement finalCe = ce2;
        new Thread(new Runnable() {
            @Override
            public void run() {
                makeAGift(ce1);
                makeAGift(finalCe);
                appDatabase.chestDao().delete(chest.getDatabaseID());
                chestList.remove(chest);
            }
        }).start();

        final CarElement finalCe2 = ce2;
        GarageActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (boxPosition)
                {
                    case 0: {
                        chest0IV.setImageResource(R.drawable.toolbox_empty);
                        break;
                    }
                    case 1: {
                        chest1IV.setImageResource(R.drawable.toolbox_empty);
                        break;
                    }
                    case 2: {
                        chest2IV.setImageResource(R.drawable.toolbox_empty);
                        break;
                    }
                }

                Toast.makeText(GarageActivity.this, "Chassis opened! You got " + getGiftName(ce1) + " and " + getGiftName(finalCe2), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getGiftName(CarElement carElement)
    {
        switch (carElement.getElementIdentity())
        {
            case Constants.C_BOULDER: return "Chassis Boulder";
            case Constants.C_CLASSIC: return  "Chassis Classic";
            case Constants.C_WHALE: return "Chassis Whale";
            case Constants.WPN_CHAINSAW: return "Chainsaw";
            case Constants.WPN_ROCKET: return "Rocket";
            case Constants.WPN_STINGER: return "Stinger";
            case Constants.WHL_KNOB: return "Wheel Knob";
            case Constants.WHL_SCOOTER: return "Wheel Scooter";
            case Constants.WHL_TYRE: return "Wheel Tyre";
            default: return "";
        }
    }
}