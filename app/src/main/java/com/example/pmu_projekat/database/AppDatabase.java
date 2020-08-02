package com.example.pmu_projekat.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.pmu_projekat.constants.Constants;
import com.example.pmu_projekat.database.daos.ChestDAO;
import com.example.pmu_projekat.database.daos.UserDAO;
import com.example.pmu_projekat.database.entities.Chassis;
import com.example.pmu_projekat.database.entities.Chest;
import com.example.pmu_projekat.database.entities.User;
import com.example.pmu_projekat.database.entities.WarehouseChassis;
import com.example.pmu_projekat.database.entities.WarehouseWeapon;
import com.example.pmu_projekat.database.entities.WarehouseWheel;
import com.example.pmu_projekat.database.entities.Weapon;
import com.example.pmu_projekat.database.entities.Wheel;

import java.util.Date;

@Database(entities = {Chassis.class, User.class, WarehouseChassis.class,
                      WarehouseWeapon.class, WarehouseWheel.class, Weapon.class,
                      Wheel.class, Chest.class},
          version = 6,
          exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDAO userDao();
    public abstract ChestDAO chestDao();

    private static AppDatabase singleInstance = null;

    public static AppDatabase getDatabase(Context context) {
        if (singleInstance == null) {
            synchronized (AppDatabase.class) {
                singleInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "PMU_Database"
                )       .fallbackToDestructiveMigration()
                        .addCallback(callback)
                        .build();
            }
        }
        return singleInstance;
    }

    private static Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDatabaseAsync(singleInstance).execute();
        }
    };

    private static class PopulateDatabaseAsync extends AsyncTask<Void, Void, Void> {
        private final UserDAO userDAO;
        private final ChestDAO chestDAO;

        public PopulateDatabaseAsync(AppDatabase appdatabase) {
            this.userDAO = appdatabase.userDao();
            this.chestDAO = appdatabase.chestDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            User userNenad = new User("nenad");
            userNenad.setNumberOfBattles(3);
            userNenad.setNumberOfWins(1);
            long insert1 = userDAO.insert(userNenad);

            Chest chest = new Chest();
            chest.setIdUser(insert1);
            Date d = new Date();
            long milis = d.getTime();
            milis += 86400000;
            chest.setTimeToOpen(milis);
            long insert = chestDAO.insert(chest);

            Log.d(Constants.APP_DATABASE_DEBUG_TAG, "insert " + insert);

            return null;
        }
    }
}