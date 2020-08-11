package com.example.pmu_projekat.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pmu_projekat.database.entities.Chest;

import java.util.List;

@Dao
public interface ChestDAO {
    @Query("SELECT * FROM Chest")
    LiveData<List<Chest>> getAllLD();

    @Query("SELECT * FROM Chest")
    List<Chest> getAll();

    @Query("SELECT * FROM Chest WHERE id_user = :id")
    LiveData<List<Chest>> getChestsForUsernameLD(long id);

    @Query("SELECT * FROM Chest WHERE id_user = :id")
    List<Chest> getChestsForUsername(long id);

    @Insert
    long insert(Chest chest);

    @Query("DELETE FROM chest WHERE id = :id")
    void delete(long id);
}
