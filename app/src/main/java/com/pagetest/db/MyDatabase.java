package com.pagetest.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.pagetest.githubsupport.RepoModel;


@Database(entities = {Note.class, RepoModel.class}, version = 2, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    private static final String NAME = "MyDataBase";
    private static MyDatabase INSTANCE;

    public static MyDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, NAME)
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    public abstract NoteDao noteDao();

    public abstract RepoDao repoDao();


}
