package com.example.pmu_projekat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Date;
import java.util.List;

public class GarageActivity extends AppCompatActivity implements SettingsReturnValue {

    private String username;
    private int id;
    private User user;

    private Thread chestThread;

    private AppDatabase appDatabase;

    private List<ChestUtility> chestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);

        appDatabase = AppDatabase.getDatabase(this);

        this.id = MySharedPreferences.getInt(this, Constants.ID_STRING);
        this.username = MySharedPreferences.getString(this, Constants.USERNAME_STRING);

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Chest> chestsForUsername = appDatabase.chestDao().getChestsForUsername(id);
                int numberOfChests = chestsForUsername.size();

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
                                            // TO DO
                                            // izaberi slucajnu kompomentu
                                            // dodaj je u bazu za tog korisnika
                                            // obrisi iz baze ovaj kovceg
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

                if (numberOfChests >= 1)
                {
                    ImageView imageView = findViewById(R.id.chest_1_iv);
                    imageView.setImageResource(R.drawable.toolbox_attack);
                }

                if (numberOfChests >= 2)
                {
                    ImageView imageView = findViewById(R.id.chest_2_iv);
                    imageView.setImageResource(R.drawable.toolbox_attack);
                }

                if (numberOfChests == 3)
                {
                    ImageView imageView = findViewById(R.id.chest_3_iv);
                    imageView.setImageResource(R.drawable.toolbox_attack);
                }
            }
        }).start();

        ImageView chest0IV = findViewById(R.id.chest_1_iv);
        ImageView chest1IV = findViewById(R.id.chest_2_iv);
        ImageView chest2IV = findViewById(R.id.chest_3_iv);

        chest0IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chestList.size() >= 1)
                {
                    if (chestList.get(0).isReady())
                    {
                        Toast.makeText(GarageActivity.this, "OPEN!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(GarageActivity.this, "OPEN!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(GarageActivity.this, "OPEN!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(GarageActivity.this, "Wait! This chest is not ready yet.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        final CustomView carView = findViewById(R.id.car_view);

        new Thread(new Runnable() {
            @Override
            public void run() {
                WarehouseChassis activeChassis = appDatabase.warehouseDao().getActiveChassisForUser(GarageActivity.this.id);

                if (activeChassis != null)
                {
                    ChassisElement chassisElement = null;

                    Chassis chassis = appDatabase.carElementsDao().getChassis(activeChassis.getIdChassis());

                    switch (chassis.getName())
                    {
                        case "classic" :
                        {
                            chassisElement = new ChassisClassic(carView.getContext(), 200, 200);
                            break;
                        }
                        case "whale" :
                        {
                            chassisElement = new ChassisWhale(carView.getContext(), 200, 200);
                            break;
                        }
                        case "boulder" :
                        {
                            chassisElement = new ChassisBoulder(carView.getContext(), 200, 200);
                            break;
                        }
                    }

                    WarehouseWeapon activeWeapon = appDatabase.warehouseDao().getActiveWeaponForUser(GarageActivity.this.id);

                    if (activeWeapon != null)
                    {
                        Weapon weapon = appDatabase.carElementsDao().getWeapon(activeWeapon.getIdWeapon());

                        CarElement weaponElement = null;

                        switch (weapon.getName())
                        {
                            case "stringer" :
                            {
                                weaponElement = new Stinger(carView.getContext(), 0, 0);
                                break;
                            }
                            case "chainsaw" :
                            {
                                weaponElement = new Chainsaw(carView.getContext(), 0, 0);
                                break;
                            }
                            case "rocket" :
                            {
                                weaponElement = new Rocket(carView.getContext(), 0, 0);
                                break;
                            }
                        }

                        chassisElement.setWeapon(weaponElement);
                    }

                    WarehouseWheel activeWheel = appDatabase.warehouseDao().getActiveWheelForUser(GarageActivity.this.id);

                    if (activeWheel != null)
                    {
                        Wheel wheel = appDatabase.carElementsDao().getWheel(activeWheel.getIdWheel());

                        CarElement leftWheelElement = null;
                        CarElement rightWheelElement = null;

                        switch (wheel.getName())
                        {
                            case "knob" :
                            {
                                leftWheelElement = new Knob(carView.getContext(), 0, 0);
                                rightWheelElement = new Knob(carView.getContext(), 0, 0);
                                break;
                            }
                            case "scooter" :
                            {
                                leftWheelElement = new Scooter(carView.getContext(), 0, 0);
                                rightWheelElement = new Scooter(carView.getContext(), 0, 0);
                                break;
                            }
                            case "tyre" :
                            {
                                leftWheelElement = new Tyre(carView.getContext(), 0, 0);
                                rightWheelElement = new Tyre(carView.getContext(), 0, 0);
                                break;
                            }
                        }

                        chassisElement.setWheelLeft(leftWheelElement);
                        chassisElement.setWheelRight(rightWheelElement);
                    }

                    carView.setCar(chassisElement);
                    carView.invalidate();

                }
            }
        }).start();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (GarageActivity.this.user != null && GarageActivity.this.user.isMusic() == true) {
            Intent intent = new Intent(this, MyMusicPlayer.class);
            stopService(intent);
        }
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

    private void makeAGift()
    {

    }
}