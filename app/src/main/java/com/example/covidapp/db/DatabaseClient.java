package com.example.covidapp.db;

import android.content.Context;

import androidx.room.Room;

import com.example.covidapp.model.Country;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseClient {

    private static CountryRoomDatabase mCountryRoomDatabase;
    private static DatabaseClient mInstance;
    private static Context mCtx;

    private static final int NUMBER_OF_THREADS=4;
    public static final ExecutorService databaseWriteExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        mCountryRoomDatabase = Room.databaseBuilder(mCtx, CountryRoomDatabase.class, "Country").build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public static CountryRoomDatabase getAppDatabase() {
        return mCountryRoomDatabase;
    }

    public static void insertFavoriteCountry(Country task){
        DatabaseClient.databaseWriteExecutor.execute(() -> {
            getAppDatabase().countryDao().insertFavoriteCountry(task);
        });
    }

}

