package com.example.pmu_projekat.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "warehouse_weapon",
        primaryKeys = {"id_user", "id_weapon"},
        indices = {
                @Index("id_user"),
                @Index("id_weapon"),
        },
        foreignKeys = {
                @ForeignKey(entity = Weapon.class,
                        parentColumns = "id",
                        childColumns = "id_weapon"),
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "id_user")
        }
)
public class WarehouseWeapon {
    @ColumnInfo(name = "id_user")
    public long idUser;

    @ColumnInfo(name = "id_weapon")
    public long idWeapon;

    @ColumnInfo(name = "is_active")
    public boolean isActive;

    public WarehouseWeapon(long idUser, long idWeapon, boolean isActive) {
        this.idUser = idUser;
        this.idWeapon = idWeapon;
        this.isActive = isActive;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdWeapon() {
        return idWeapon;
    }

    public void setIdWeapon(long idWeapon) {
        this.idWeapon = idWeapon;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
