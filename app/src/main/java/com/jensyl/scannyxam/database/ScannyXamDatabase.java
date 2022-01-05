package com.jensyl.scannyxam.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Badging.class}, version = 1, exportSchema = false)
public abstract class ScannyXamDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract BadgingDao badgingDao();
}
