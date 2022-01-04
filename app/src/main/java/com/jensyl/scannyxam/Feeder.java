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
                new Badging(UUID.randomUUID().toString(), "ma173017", LocalDateTime.now().toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "ma173017", LocalDateTime.now().plusMinutes(61).toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "az171092", LocalDateTime.now().toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "az171092", LocalDateTime.now().plusMinutes(82).toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "vz172472", LocalDateTime.now().toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "vz172472", LocalDateTime.now().plusMinutes(83).toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "qs177717", LocalDateTime.now().toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "qs177717", LocalDateTime.now().plusMinutes(68).toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "hj176237", LocalDateTime.now().toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "hj176237", LocalDateTime.now().plusMinutes(66).toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "ps174026", LocalDateTime.now().toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "ps174026", LocalDateTime.now().plusMinutes(74).toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "sd172815", LocalDateTime.now().toString(), "Anglais"),
                new Badging(UUID.randomUUID().toString(), "sd172815", LocalDateTime.now().plusMinutes(82).toString(), "Anglais"),

                new Badging(UUID.randomUUID().toString(), "ma173017", LocalDateTime.now().minusDays(3).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "ma173017", LocalDateTime.now().minusDays(3).plusMinutes(32).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "az171092", LocalDateTime.now().minusDays(3).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "az171092", LocalDateTime.now().minusDays(3).plusMinutes(37).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "vz172472", LocalDateTime.now().minusDays(3).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "vz172472", LocalDateTime.now().minusDays(3).plusMinutes(72).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "qs177717", LocalDateTime.now().minusDays(3).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "qs177717", LocalDateTime.now().minusDays(3).plusMinutes(93).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "hj176237", LocalDateTime.now().minusDays(3).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "hj176237", LocalDateTime.now().minusDays(3).plusMinutes(71).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "ps174026", LocalDateTime.now().minusDays(3).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "ps174026", LocalDateTime.now().minusDays(3).plusMinutes(62).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "sd172815", LocalDateTime.now().minusDays(3).toString(), "Programmation Mobile"),
                new Badging(UUID.randomUUID().toString(), "sd172815", LocalDateTime.now().minusDays(3).plusMinutes(57).toString(), "Programmation Mobile"));
    }
}
