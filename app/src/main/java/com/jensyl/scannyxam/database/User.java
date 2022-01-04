package com.jensyl.scannyxam.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    public String idUser;

    @NonNull
    public String idUniversity;

    @NonNull
    public String firstName;

    @NonNull
    public String lastName;

    public User(
            @NonNull String idUser,
            @NonNull String idUniversity,
            @NonNull String firstName,
            @NonNull String lastName) {
        this.idUser = idUser;
        this.idUniversity = idUniversity;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
