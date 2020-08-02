package com.example.pmu_projekat.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "warehouse_chassis",
        primaryKeys = {"id_user", "id_chassis"},
        indices = {
                @Index("id_user"),
                @Index("id_chassis"),
        },
        foreignKeys = {
                @ForeignKey(entity = Chassis.class,
                        parentColumns = "id",
                        childColumns = "id_chassis"),
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "id_user")
        }
)
public class WarehouseChassis {
    @ColumnInfo(name = "id_user")
    public long idUser;

    @ColumnInfo(name = "id_chassis")
    public long idChassis;

    @ColumnInfo(name = "is_active")
    public boolean isActive;

    public WarehouseChassis(long idUser, long idChassis, boolean isActive) {
        this.idUser = idUser;
        this.idChassis = idChassis;
        this.isActive = isActive;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdChassis() {
        return idChassis;
    }

    public void setIdChassis(long idChassis) {
        this.idChassis = idChassis;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
