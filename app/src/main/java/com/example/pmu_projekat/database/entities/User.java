package com.example.pmu_projekat.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user"
)
public class User {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "number_of_battles")
    public int numberOfBattles;

    @ColumnInfo(name = "number_of_wins")
    public int numberOfWins;

    @ColumnInfo(name = "music")
    public boolean music;

    @ColumnInfo(name = "user_control")
    public boolean userControl;

    public User(String username) {
        this.username = username;
        this.numberOfBattles = 0;
        this.numberOfWins = 0;
        this.music = true;
        this.userControl = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumberOfBattles() {
        return numberOfBattles;
    }

    public void setNumberOfBattles(int numberOfBattles) {
        this.numberOfBattles = numberOfBattles;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public void setNumberOfWins(int numberOfGames) {
        this.numberOfWins = numberOfGames;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean isUserControl() {
        return userControl;
    }

    public void setUserControl(boolean userControl) {
        this.userControl = userControl;
    }
}
