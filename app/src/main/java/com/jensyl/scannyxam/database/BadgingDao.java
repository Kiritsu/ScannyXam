package com.jensyl.scannyxam.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BadgingDao {
    @Query("SELECT * FROM badging")
    List<Badging> getAll();

    @Query("SELECT * FROM badging WHERE idBadging = (:idBadging)")
    Badging getById(String idBadging);

    @Query("SELECT * FROM badging WHERE idUserBadging = (:idUser)")
    List<Badging> getAllByIdUser(String idUser);

    @Insert
    void insert(Badging... badgings);

    @Delete
    void delete(Badging badging);
}
