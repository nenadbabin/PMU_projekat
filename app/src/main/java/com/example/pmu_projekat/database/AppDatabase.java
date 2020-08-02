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
import com.example.pmu_projekat.database.daos.CarElementsDAO;
import com.example.pmu_projekat.database.daos.ChestDAO;
import com.example.pmu_projekat.database.daos.UserDAO;
import com.example.pmu_projekat.database.daos.WarehouseDAO;
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
    public abstract CarElementsDAO carElementsDao();
    public abstract WarehouseDAO warehouseDao();

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
        private final CarElementsDAO carElementsDAO;
        private final WarehouseDAO warehouseDAO;

        public PopulateDatabaseAsync(AppDatabase appdatabase) {
            this.userDAO = appdatabase.userDao();
            this.chestDAO = appdatabase.chestDao();
            this.carElementsDAO = appdatabase.carElementsDao();
            this.warehouseDAO = appdatabase.warehouseDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            User userNenad = new User("nenad");
            userNenad.setNumberOfBattles(3);
            userNenad.setNumberOfWins(1);
            long userID = userDAO.insert(userNenad);

            Chest chest = new Chest();
            chest.setIdUser(userID);
            Date d = new Date();
            long milis = d.getTime();
            milis += 60000;
            chest.setTimeToOpen(milis);
            long insert = chestDAO.insert(chest);

            Log.d(Constants.APP_DATABASE_DEBUG_TAG, "insert " + insert);

            Weapon stinger = new Weapon("stringer", 5, 5, 10);
            long stringerID = carElementsDAO.insert(stinger);
            Weapon chainsaw = new Weapon("chainsaw", 10, 6, 15);
            long chainsawID = carElementsDAO.insert(chainsaw);
            Weapon rocket = new Weapon("rocket", 15, 8, 20);
            long rocketID = carElementsDAO.insert(rocket);

            Chassis classic = new Chassis("classic", 80, 6);
            long classicID = carElementsDAO.insert(classic);
            Chassis whale = new Chassis("whale", 100, 8);
            long whaleID = carElementsDAO.insert(whale);
            Chassis boulder = new Chassis("boulder", 120, 10);
            long boulderID = carElementsDAO.insert(boulder);

            Wheel knob = new Wheel("knob", 10);
            long knobID = carElementsDAO.insert(knob);
            Wheel scooter = new Wheel("scooter", 20);
            long scooterID = carElementsDAO.insert(scooter);
            Wheel tyre = new Wheel("tyre", 30);
            long tyreID = carElementsDAO.insert(tyre);

            WarehouseChassis wc1 = new WarehouseChassis(userID, classicID, false);
            warehouseDAO.insert(wc1);
            WarehouseChassis wc2 = new WarehouseChassis(userID, boulderID, true);
            warehouseDAO.insert(wc2);
            WarehouseWeapon wwe1 = new WarehouseWeapon(userID, stringerID, false);
            warehouseDAO.insert(wwe1);
            WarehouseWeapon wwe2 = new WarehouseWeapon(userID, rocketID, true);
            warehouseDAO.insert(wwe2);
            WarehouseWheel wwh1 = new WarehouseWheel(userID, knobID, false);
            warehouseDAO.insert(wwh1);
            WarehouseWheel wwh2 = new WarehouseWheel(userID, tyreID, true);
            warehouseDAO.insert(wwh2);

            return null;
        }
    }
}