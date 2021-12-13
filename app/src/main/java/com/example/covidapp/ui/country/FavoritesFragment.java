package com.example.covidapp.ui.country;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidapp.R;
import com.example.covidapp.db.DatabaseClient;
import com.example.covidapp.model.Country;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private static final String TAG = CountryFragment.class.getSimpleName();

    RecyclerView rvCovidCountry;
    ProgressBar pbProgressBar;
    CountryListAdapter countryListAdapter;

    List<Country> covidCountries;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_country, container, false);

        setHasOptionsMenu(true);

        rvCovidCountry = root.findViewById(R.id.rvCovidCountry);
        pbProgressBar = root.findViewById(R.id.progress_circular_country);
        rvCovidCountry.setLayoutManager(new LinearLayoutManager(getActivity()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCovidCountry.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.line_divider));
        rvCovidCountry.addItemDecoration(dividerItemDecoration);

        covidCountries = new ArrayList<>();

        getDataFromDB(QuerySortType.CASES);
        return root;
    }

    private void getDataFromDB(QuerySortType type) {
        pbProgressBar.setVisibility(View.GONE);
        DatabaseClient.databaseWriteExecutor.execute(() -> {
            covidCountries = DatabaseClient.getAppDatabase().countryDao().getFavoriteCountries();
        });
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if(type.equals(QuerySortType.CASES)) {
                    Collections.sort(covidCountries, new Comparator<Country>() {
                        @Override
                        public int compare(Country o1, Country o2) {
                            if(o1.compareTo(o2) == 0) {
                                return 0;
                            }
                            return o1.compareTo(o2) > 0 ? -1 : 1;
                        }
                    });
                }

                showRecyclerView();
            }
        });
    }

    private void showRecyclerView() {
        countryListAdapter = new CountryListAdapter(covidCountries, getContext());
        rvCovidCountry.setAdapter(countryListAdapter);

        ItemClickSupport.addTo(rvCovidCountry).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedCovidCountry(covidCountries.get(position));
            }
        });
    }

    private void showSelectedCovidCountry(Country country) {
        Intent covidCountryDetail = new Intent(getActivity(), CountryDetail.class);
        covidCountryDetail.putExtra("EXTRA_COVID", country);
        startActivity(covidCountryDetail);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.country_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(getActivity());
        searchView.setQuery("", false);
        searchView.setQueryHint("Search...");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (countryListAdapter != null) {
                    countryListAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        searchMenuItem.setActionView(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_character:
                Toast.makeText(getContext(), "Sort Alphabetically", Toast.LENGTH_SHORT).show();
                covidCountries.clear();
                pbProgressBar.setVisibility(View.VISIBLE);
                getDataFromDB(QuerySortType.ALPHABET);
                return true;
            case R.id.action_sort_cases:
                Toast.makeText(getContext(), "Sort by total cases", Toast.LENGTH_SHORT).show();
                covidCountries.clear();
                pbProgressBar.setVisibility(View.VISIBLE);
                getDataFromDB(QuerySortType.CASES);
        }
        return super.onOptionsItemSelected(item);
    }
}