package com.jensyl.scannyxam.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithBadgings {
    @Embedded
    public User user;

    @Relation(
            parentColumn = "idUser",
            entityColumn = "idUserBadging"
    )
    public List<Badging> Badgings;
}
