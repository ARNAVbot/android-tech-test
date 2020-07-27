package com.bridge.androidtechnicaltest.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {
        Pupil.class
}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    public abstract PupilDao getPupilDao();
}