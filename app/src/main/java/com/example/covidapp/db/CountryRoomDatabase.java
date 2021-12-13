package com.example.covidapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.covidapp.model.Country;

@Database(entities = {Country.class}, version = 1)
public abstract class CountryRoomDatabase extends RoomDatabase {
    public abstract CountryDao countryDao();
}