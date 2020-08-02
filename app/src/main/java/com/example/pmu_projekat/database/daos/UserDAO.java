package com.example.pmu_projekat.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pmu_projekat.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllLD();

    @Query("SELECT * FROM User")
    List<User> getAll();

    @Query("SELECT * FROM User WHERE id = :id")
    LiveData<User> getUserById (int id);

    @Insert
    long insert(User user);

    @Query("UPDATE User SET music = :music, user_control = :userControl WHERE id = :id")
    void updateUserPreferences (int id, boolean music, boolean userControl);
}
