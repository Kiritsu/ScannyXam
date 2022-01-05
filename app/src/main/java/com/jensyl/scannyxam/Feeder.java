package com.jensyl.scannyxam;

import android.content.Context;

import androidx.room.Room;

import com.jensyl.scannyxam.database.Badging;
import com.jensyl.scannyxam.database.ScannyXamDatabase;
import com.jensyl.scannyxam.database.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class Feeder {
    public static void feed(Context context) {
        ScannyXamDatabase db = Room
                .databaseBuilder(
                        context,
                        ScannyXamDatabase.class,
                        "scanny-xam")
                .build();

        db.userDao().insert(
                new User("1", "ma173017", "Allan", "Mercou"),
                new User("2", "az171092", "Ab", "Wx"),
                new User("3", "vz172472", "Ac", "Wc"),
                new User("4", "qs177717", "Ad", "Wv"),
                new User("5", "dg176734", "Ae", "Wb"),
                new User("6", "hj176237", "Af", "Wn"),
                new User("7", "ps174026", "Ag", "Wp"),
                new User("8", "sd172815", "Ah", "Wo"));

        db.badgingDao().insert(
                new Badging(UUID.randomUUID().toString(), "1", LocalDateTime.now().toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "1", LocalDateTime.now().plusMinutes(61).toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "2", LocalDateTime.now().toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "2", LocalDateTime.now().plusMinutes(82).toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "3", LocalDateTime.now().toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "3", LocalDateTime.now().plusMinutes(83).toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "4", LocalDateTime.now().toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "4", LocalDateTime.now().plusMinutes(68).toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "5", LocalDateTime.now().toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "5", LocalDateTime.now().plusMinutes(66).toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "6", LocalDateTime.now().toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "6", LocalDateTime.now().plusMinutes(74).toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "7", LocalDateTime.now().toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "7", LocalDateTime.now().plusMinutes(82).toString(), "Anglais"),

                new Badging(UUID.randomUUID().toString(), "1", LocalDateTime.now().minusDays(3).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "1", LocalDateTime.now().minusDays(3).plusMinutes(32).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "2", LocalDateTime.now().minusDays(3).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "2", LocalDateTime.now().minusDays(3).plusMinutes(37).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "3", LocalDateTime.now().minusDays(3).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "3", LocalDateTime.now().minusDays(3).plusMinutes(72).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "4", LocalDateTime.now().minusDays(3).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "4", LocalDateTime.now().minusDays(3).plusMinutes(93).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "5", LocalDateTime.now().minusDays(3).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "5", LocalDateTime.now().minusDays(3).plusMinutes(71).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "6", LocalDateTime.now().minusDays(3).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "6", LocalDateTime.now().minusDays(3).plusMinutes(62).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "7", LocalDateTime.now().minusDays(3).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "7", LocalDateTime.now().minusDays(3).plusMinutes(57).toString(), "Programmation Mobile"));
    }
}
