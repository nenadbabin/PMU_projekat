package com.example.pmu_projekat.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "warehouse_wheel",
        primaryKeys = {"id_user", "id_wheel"},
        indices = {
                @Index("id_user"),
                @Index("id_wheel"),
        },
        foreignKeys = {
                @ForeignKey(entity = Chassis.class,
                        parentColumns = "id",
                        childColumns = "id_wheel"),
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "id_user")
        }
)
public class WarehouseWheel {
    @ColumnInfo(name = "id_user")
    public long idUser;

    @ColumnInfo(name = "id_wheel")
    public long idWheel;

    @ColumnInfo(name = "is_active")
    public boolean isActive;

    public WarehouseWheel(long idUser, long idWheel, boolean isActive) {
        this.idUser = idUser;
        this.idWheel = idWheel;
        this.isActive = isActive;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdWheel() {
        return idWheel;
    }

    public void setIdWheel(long idWheel) {
        this.idWheel = idWheel;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
