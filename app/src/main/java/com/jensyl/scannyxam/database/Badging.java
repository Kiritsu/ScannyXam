package com.jensyl.scannyxam.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Badging {
    @PrimaryKey
    @NonNull
    public String idBadging;

    @NonNull
    public String idUserBadging;

    @NonNull
    public String date;

    @NonNull
    public String exam;

    public Badging(
            @NonNull String idBadging,
            @NonNull String idUserBadging,
            @NonNull String date,
            @NonNull String exam) {
        this.idBadging = idBadging;
        this.idUserBadging = idUserBadging;
        this.date = date;
        this.exam = exam;
    }
}
