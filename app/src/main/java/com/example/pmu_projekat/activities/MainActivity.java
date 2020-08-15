package com.example.pmu_projekat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pmu_projekat.R;
import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.database.AppDatabase;
import com.example.pmu_projekat.database.entities.User;
import com.example.pmu_projekat.shared_preferences.MySharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Constants.SCREEN_HEIGHT = displayMetrics.heightPixels;
        Constants.SCREEN_WIDTH = displayMetrics.widthPixels;

        final Spinner spinner = findViewById(R.id.spinner);

        final AppDatabase appdatabase = AppDatabase.getDatabase(this);

        appdatabase.userDao().getAllLD().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                List<String> usernames = new ArrayList<>();
                usernames.add("Choose player...");

                for (User user : users) {
                    usernames.add(user.getUsername());
                }

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, usernames);

                spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
            }
        });

        Button buttonPlay = findViewById(R.id.btn_play);

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String playerName = spinner.getSelectedItem().toString();
                TextView errorTV = findViewById(R.id.tv_error_msg);

                if (playerName.equals("Choose player..."))
                {
                    errorTV.setText("Choose a player!");
                }
                else
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List<User> users = appdatabase.userDao().getAll();
                            for (int i = 0; i < users.size(); i++)

                            {
                                if (users.get(i).getUsername().equals(playerName))
                                {
                                    MySharedPreferences.save(MainActivity.this, Constants.USERNAME_STRING, users.get(i).getUsername());
                                    MySharedPreferences.save(MainActivity.this, Constants.ID_STRING, (int) users.get(i).getId());
                                    Log.d(Constants.MAIN_ACTIVITY_DEBUG_TAG, "" + MySharedPreferences.getInt(MainActivity.this, Constants.ID_STRING));
                                    Log.d(Constants.MAIN_ACTIVITY_DEBUG_TAG, "" + MySharedPreferences.getString(MainActivity.this, Constants.USERNAME_STRING));
                                    Intent intent = new Intent(MainActivity.this, GarageActivity.class);
                                    startActivity(intent);
                                    break;
                                }
                            }
                        }
                    }).start();
                }
            }
        });

        Button buttonRegister = findViewById(R.id.btn_register);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText usernameET = findViewById(R.id.et_username);
                final TextView errorTV = findViewById(R.id.tv_error_msg);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<User> users = appdatabase.userDao().getAll();
                        if (!usernameET.getText().equals(""))
                        {
                            boolean ok = true;
                            for (int i = 0; i < users.size(); i++)
                            {
                                if (users.get(i).getUsername().equals(usernameET.getText().toString()))
                                {
                                    ok = false;
                                    break;
                                }
                            }

                            if (ok)
                            {
                                final String username = usernameET.getText().toString();
                                errorTV.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        errorTV.setText("New player successfully added!");
                                        usernameET.setText("");
                                    }
                                });
                                appdatabase.userDao().insert(new User(username));
                            }
                            else
                            {
                                errorTV.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        errorTV.setText("Username already exists!");
                                    }
                                });
                            }
                        }
                    }
                }).start();
            }
        });
    }
}
