package com.example.pmu_projekat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmu_projekat.R;
import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.database.AppDatabase;
import com.example.pmu_projekat.database.entities.Chassis;
import com.example.pmu_projekat.database.entities.User;
import com.example.pmu_projekat.database.entities.Weapon;
import com.example.pmu_projekat.database.entities.Wheel;
import com.example.pmu_projekat.game_loop.GameLoop;
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

        battleView.setBattleActivity(BattleActivity.this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                BattleActivity.this.car = GarageActivity.createCar(BattleActivity.this.battleView, BattleActivity.this.id, BattleActivity.this.appDatabase);
                BattleActivity.this.battleView.setCarP1(BattleActivity.this.car);

                User userByIdNLD = appDatabase.userDao().getUserByIdNLD(BattleActivity.this.id);

                if (userByIdNLD.isUserControl())
                {
                    BattleActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ImageView buttonLeftIV = findViewById(R.id.button_left_iv);
                            buttonLeftIV.setImageResource(R.drawable.left_arrow);
                            ImageView buttonRightIV = findViewById(R.id.button_right_iv);
                            buttonRightIV.setImageResource(R.drawable.right_arrow);
                            ImageView buttonAttackIV = findViewById(R.id.button_attack_iv);
                            buttonAttackIV.setImageResource(R.drawable.attack_btn);

                            BattleActivity.this.battleView.setUserControl(true);

                            buttonLeftIV.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    switch (event.getActionMasked())
                                    {
                                        case MotionEvent.ACTION_DOWN : {
                                            BattleActivity.this.battleView.setMoveLeft(true);
                                            break;
                                        }
                                        case MotionEvent.ACTION_UP: {
                                            BattleActivity.this.battleView.setMoveLeft(false);
                                            break;
                                        }
                                    }

                                    return true;
                                }
                            });

                            buttonRightIV.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    switch (event.getActionMasked())
                                    {
                                        case MotionEvent.ACTION_DOWN : {
                                            BattleActivity.this.battleView.setMoveRight(true);
                                            break;
                                        }
                                        case MotionEvent.ACTION_UP: {
                                            BattleActivity.this.battleView.setMoveRight(false);
                                            break;
                                        }
                                    }

                                    return true;
                                }
                            });

                            buttonAttackIV.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    switch (event.getActionMasked())
                                    {
                                        case MotionEvent.ACTION_DOWN : {
                                            BattleActivity.this.battleView.setAttack(true);
                                            break;
                                        }
                                        case MotionEvent.ACTION_UP: {
                                            BattleActivity.this.battleView.setAttack(false);
                                            break;
                                        }
                                    }

                                    return true;
                                }
                            });
                        }
                    });
                }

                Chainsaw chainsaw = new Chainsaw(BattleActivity.this.battleView.getContext(), 0,0);
                chainsaw.setHealth(5);
                chainsaw.setEnergy(5);
                chainsaw.setPower(10);
                chainsaw.setReverse(true);
                Scooter knobL = new Scooter(BattleActivity.this.battleView.getContext(), 0,0);
                knobL.setHealth(10);
                Scooter knobR = new Scooter(BattleActivity.this.battleView.getContext(), 0,0);
                knobR.setHealth(10);

                final ChassisWhale chassisWhale = new ChassisWhale(BattleActivity.this.battleView.getContext(), 0, 0);
                chassisWhale.setHealth(80);
                chassisWhale.setEnergy(5);
                chassisWhale.setReverse(true);
                chassisWhale.setWeapon(chainsaw);
                chassisWhale.setWheelLeft(knobL);
                chassisWhale.setWheelRight(knobR);

                BattleActivity.this.battleView.setCarP2(chassisWhale);
                BattleActivity.this.battleView.setP1Health(BattleActivity.this.car.getCarHealth());
                BattleActivity.this.battleView.setP2Health(chassisWhale.getCarHealth());

                BattleActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        p1HealthTV.setText("" + BattleActivity.this.car.getCarHealth());
                        p2HealthTV.setText("" + chassisWhale.getCarHealth());
                    }
                });

                BattleActivity.this.gameLoop = new GameLoop();
                BattleActivity.this.gameLoop.setGameView(BattleActivity.this.battleView);
                gameLoop.start();
            }
        }).start();
    }

    public void setP1Health(final int health)
    {
        BattleActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                p1HealthTV.setText("" + health);
            }
        });
    }

    public void setP2Health(final int health)
    {
        BattleActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                p2HealthTV.setText("" + health);
            }
        });
    }

    public void toast(final String text)
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BattleActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }

}