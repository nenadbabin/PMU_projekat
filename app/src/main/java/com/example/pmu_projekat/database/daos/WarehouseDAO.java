package com.example.pmu_projekat.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pmu_projekat.database.entities.Chassis;
import com.example.pmu_projekat.database.entities.WarehouseChassis;
import com.example.pmu_projekat.database.entities.WarehouseWeapon;
import com.example.pmu_projekat.database.entities.WarehouseWheel;
import com.example.pmu_projekat.database.entities.Weapon;
import com.example.pmu_projekat.database.entities.Wheel;

import java.util.List;

@Dao
public interface WarehouseDAO {

    @Insert
    long insert(WarehouseChassis chassis);

    @Insert
    long insert(WarehouseWeapon weapon);

    @Insert
    long insert(WarehouseWheel wheel);

    @Query("SELECT * FROM warehouse_chassis")
    LiveData<List<WarehouseChassis>> getAllChassisLD();

    @Query("SELECT * FROM warehouse_chassis")
    List<WarehouseChassis> getAllChassis();


    @Query("SELECT * FROM warehouse_weapon")
    LiveData<List<WarehouseWeapon>> getAllWeaponsLD();

    @Query("SELECT * FROM warehouse_weapon")
    List<WarehouseWeapon> getAllWeapons();


    @Query("SELECT * FROM warehouse_wheel")
    LiveData<List<WarehouseWheel>> getAllWheelsLD();

    @Query("SELECT * FROM warehouse_wheel")
    List<WarehouseWheel> getAllWheels();
}
