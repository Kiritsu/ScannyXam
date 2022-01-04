package com.jensyl.scannyxam.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public String idUser;

    public String idUniversity;

    public String firstName;

    public String lastName;
}
