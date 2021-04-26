package com.example.racibo.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.racibo.model.Person;

@Database(entities = {Person.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    private static final String LOG_TAG=AppDataBase.class.getSimpleName();
    private static final Object LOCK=new Object();
    private static final String DATABASE_NAME="personlist";
    private static AppDataBase sInstance;

    public static AppDataBase getInstance(Context context){
        if(sInstance==null){
            synchronized (LOCK){
               Log.d(LOG_TAG,"Creating new database instance");
               sInstance= Room.databaseBuilder(context.getApplicationContext(),
                       AppDataBase.class,AppDataBase.DATABASE_NAME).build();

            }
        }
        Log.d(LOG_TAG,"Getting the database instance");
        return sInstance;
    }
    public abstract PersonDao personDao();
}
