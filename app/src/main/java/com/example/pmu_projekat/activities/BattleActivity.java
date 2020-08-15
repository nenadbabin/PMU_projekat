package com.example.pmu_projekat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.example.pmu_projekat.R;
import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.database.AppDatabase;
import com.example.pmu_projekat.database.entities.Chassis;
import com.example.pmu_projekat.database.entities.Weapon;
import com.example.pmu_projekat.database.entities.Wheel;
import com.example.pmu_projekat.game_loop.GameLoop;
import com.example.pmu_projekat.objects.CarElement;
import com.example.pmu_projekat.objects.ChassisElement;
import com.example.pmu_projekat.objects.chassis.ChassisClassic;
import com.example.pmu_projekat.objects.chassis.ChassisWhale;
import com.example.pmu_projekat.objects.weapon.Chainsaw;
import com.example.pmu_projekat.objects.weapon.Rocket;
import com.example.pmu_projekat.objects.weapon.Stinger;
import com.example.pmu_projekat.objects.wheel.Knob;
import com.example.pmu_projekat.objects.wheel.Scooter;
import com.example.pmu_projekat.shared_preferences.MySharedPreferences;
import com.example.pmu_projekat.views.BattleView;

import java.util.List;

public class BattleActivity extends AppCompatActivity {

    private int id;
    private String username;
    private AppDatabase appDatabase;
    private ChassisElement car;
    private BattleView battleView;
    private GameLoop gameLoop;

    private TextView p1HealthTV;
    private TextView p2HealthTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        this.id = MySharedPreferences.getInt(this, Constants.ID_STRING);
        this.username = MySharedPreferences.getString(this, Constants.USERNAME_STRING);
        this.appDatabase = AppDatabase.getDatabase(this);
        this.battleView = findViewById(R.id.battle_view);

        this.p1HealthTV = findViewById(R.id.tv_health_p1);
        this.p2HealthTV = findViewById(R.id.tv_health_p2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                BattleActivity.this.car = GarageActivity.createCar(BattleActivity.this.battleView, BattleActivity.this.id, BattleActivity.this.appDatabase);
                BattleActivity.this.battleView.setCarP1(BattleActivity.this.car);

                Stinger chainsaw = new Stinger(BattleActivity.this.battleView.getContext(), 0,0);
                chainsaw.setReverse(true);
                Scooter knobL = new Scooter(BattleActivity.this.battleView.getContext(), 0,0);
                Scooter knobR = new Scooter(BattleActivity.this.battleView.getContext(), 0,0);

                final ChassisClassic chassisWhale = new ChassisClassic(BattleActivity.this.battleView.getContext(), 0, 0);
                chassisWhale.setReverse(true);
                chassisWhale.setWeapon(chainsaw);
                chassisWhale.setWheelLeft(knobL);
                chassisWhale.setWheelRight(knobR);

                BattleActivity.this.battleView.setCarP2(chassisWhale);

                BattleActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        p1HealthTV.setText("" + BattleActivity.this.car.getHealth());
                        p2HealthTV.setText("" + chassisWhale.getHealth());
                    }
                });

                BattleActivity.this.gameLoop = new GameLoop();
                BattleActivity.this.gameLoop.setGameView(BattleActivity.this.battleView);
                gameLoop.start();
            }
        }).start();
    }
}