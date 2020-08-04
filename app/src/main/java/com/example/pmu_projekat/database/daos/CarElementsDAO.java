package com.example.pmu_projekat.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pmu_projekat.database.entities.Chassis;
import com.example.pmu_projekat.database.entities.Chest;
import com.example.pmu_projekat.database.entities.Weapon;
import com.example.pmu_projekat.database.entities.Wheel;

import java.util.List;

@Dao
public interface CarElementsDAO {

    @Insert
    long insert(Chassis chassis);

    @Insert
    long insert(Weapon weapon);

    @Insert
    long insert(Wheel wheel);


    @Query("SELECT * FROM chassis")
    LiveData<List<Chassis>> getAllChassisLD();

    @Query("SELECT * FROM chassis")
    List<Chassis> getAllChassis();


    @Query("SELECT * FROM weapon")
    LiveData<List<Weapon>> getAllWeaponsLD();

    @Query("SELECT * FROM weapon")
    List<Weapon> getAllWeapons();


    @Query("SELECT * FROM wheel")
    LiveData<List<Wheel>> getAllWheelsLD();

    @Query("SELECT * FROM wheel")
    List<Wheel> getAllWheels();



    @Query("SELECT * FROM chassis WHERE id = :id")
    Chassis getChassis (long id);

    @Query("SELECT * FROM weapon WHERE id = :id")
    Weapon getWeapon (long id);

    @Query("SELECT * FROM wheel WHERE id = :id")
    Wheel getWheel (long id);
}
