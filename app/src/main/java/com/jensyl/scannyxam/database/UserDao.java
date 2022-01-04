package com.jensyl.scannyxam.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface UserDao {
    @Transaction
    @Query("SELECT * FROM user")
    List<UserWithBadgings> getUsersWithBadgings();

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE idUser = (:idUser)")
    User getById(String idUser);

    @Insert
    void insert(User... users);
}
