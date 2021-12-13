package com.example.covidapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.covidapp.model.Country;

import java.util.List;

@Dao
public interface CountryDao {
    @Insert
    void insertFavoriteCountry(Country country);

    @Query("SELECT * FROM country")
    List<Country> getFavoriteCountries();

    @Query("SELECT * FROM country WHERE id = :id")
    Country getFavoriteCountryById(int id);

    @Query("DELETE FROM country WHERE id = :id")
    int deleteCountryById(int id);

}
