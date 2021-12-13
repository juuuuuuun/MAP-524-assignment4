package com.example.covidapp.ui.country;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.covidapp.R;
import com.example.covidapp.db.DatabaseClient;
import com.example.covidapp.model.Country;

public class CountryDetail extends AppCompatActivity {

    TextView tvDetailCountryName, tvDetailTotalCases, tvDetailTodayCases, tvDetailTotalDeaths, tvDetailTodayDeaths, tvDetailTotalRecovered, tvDetailTodayRecovered;
    ImageView ivCountryFlag, ivSavedButton;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);

        tvDetailCountryName = findViewById(R.id.tvDetailCountryName);
        tvDetailTotalCases = findViewById(R.id.tvTotalCases);
        tvDetailTodayCases = findViewById(R.id.tvTodayCases);
        tvDetailTotalDeaths = findViewById(R.id.tvTotalDeaths);
        tvDetailTodayDeaths = findViewById(R.id.tvTodayDeaths);
        tvDetailTotalRecovered = findViewById(R.id.tvTotalRecovered);
        tvDetailTodayRecovered = findViewById(R.id.tvTodayRecovered);
        ivCountryFlag = findViewById(R.id.ivCountryFlag);
        ivSavedButton = findViewById(R.id.ivSavedButton);

        Country country = getIntent().getParcelableExtra("EXTRA_COVID");

        tvDetailCountryName.setText(country.getCovidCountry());
        tvDetailTotalCases.setText(Integer.toString(country.getCases()));
        tvDetailTodayCases.setText(country.getTodayCases());
        tvDetailTotalDeaths.setText(country.getDeaths());
        tvDetailTodayDeaths.setText(country.getTodayDeaths());
        tvDetailTotalRecovered.setText(country.getRecovered());
        tvDetailTodayRecovered.setText(country.getTodayRecovered());

        if(country.isSaved()) {
            ivSavedButton.setBackgroundResource(R.drawable.country_saved_selector2);
        }

        Glide
                .with(getApplicationContext())
                .load(country.getFlag())
                .apply(new RequestOptions().override(600, 520))
                .into(ivCountryFlag);

        activity = this;
        ivSavedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView ivSaved = (ImageView) v;
                DatabaseClient.databaseWriteExecutor.execute(() -> {
                    Country countryByDB = DatabaseClient.getAppDatabase().countryDao().getFavoriteCountryById(country.getId());
                    if(countryByDB == null) {
                        DatabaseClient.insertFavoriteCountry(country);
                    } else {
                        DatabaseClient.getAppDatabase().countryDao().deleteCountryById(country.getId());
                    }
                });
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (!country.isSaved()) {
                            ivSaved.setBackgroundResource(R.drawable.country_saved_selector2);
                            country.setSaved(true);
                            Toast.makeText(activity, "Saved!", Toast.LENGTH_SHORT).show();
                        } else {
                            ivSaved.setBackgroundResource(R.drawable.country_saved_selector);
                            country.setSaved(false);
                            Toast.makeText(activity, "Removed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}