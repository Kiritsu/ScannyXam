package com.jensyl.scannyxam.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity
public class Badging {
    @PrimaryKey
    public String idBadging;

    public String idUserBadging;

    public Date date;

    public String exam;
}
