package com.example.pmu_projekat.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "chest",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                            parentColumns = "id",
                            childColumns = "id_user")
        }
)
public class Chest {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "id_user")
    public long idUser;

    @ColumnInfo(name = "time_to_open")
    public long timeToOpen;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getTimeToOpen() {
        return timeToOpen;
    }

    public void setTimeToOpen(long timeToOpen) {
        this.timeToOpen = timeToOpen;
    }
}
