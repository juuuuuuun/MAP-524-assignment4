package com.example.covidapp.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "country")
public class Country implements Parcelable, Comparable<Country> {

    @PrimaryKey()
    private int id;

    private String covidCountry, todayCases, deaths, todayDeaths, recovered, todayRecovered, flag;
    private int cases;
    @Ignore
    private boolean saved;

    public Country(String covidCountry, int cases, String todayCases, String deaths, String todayDeaths, String recovered, String todayRecovered, String flag) {
        this.covidCountry = covidCountry;
        this.cases = cases;
        this.todayCases = todayCases;
        this.deaths = deaths;
        this.todayDeaths = todayDeaths;
        this.recovered = recovered;
        this.todayRecovered = todayRecovered;
        this.flag = flag;
        this.saved = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Country(Parcel in) {
        id = in.readInt();
        covidCountry = in.readString();
        cases = in.readInt();
        todayCases = in.readString();
        deaths = in.readString();
        todayDeaths = in.readString();
        recovered = in.readString();
        todayRecovered = in.readString();
        flag = in.readString();
        saved = in.readBoolean();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCovidCountry() {
        return covidCountry;
    }

    public void setCovidCountry(String covidCountry) {
        this.covidCountry = covidCountry;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public String getTodayCases() {
        return todayCases;
    }

    public void setTodayCases(String todayCases) {
        this.todayCases = todayCases;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getTodayDeaths() {
        return todayDeaths;
    }

    public void setTodayDeaths(String todayDeaths) {
        this.todayDeaths = todayDeaths;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String mRecovered) {
        this.recovered = mRecovered;
    }

    public String getTodayRecovered() {
        return todayRecovered;
    }

    public void setTodayRecovered(String todayRecovered) {
        this.todayRecovered = todayRecovered;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(covidCountry);
        dest.writeInt(cases);
        dest.writeString(todayCases);
        dest.writeString(deaths);
        dest.writeString(todayDeaths);
        dest.writeString(recovered);
        dest.writeString(todayRecovered);
        dest.writeString(flag);
        dest.writeBoolean(saved);
    }

    @Override
    public int compareTo(Country o) {
        return cases - o.cases;
    }
}
