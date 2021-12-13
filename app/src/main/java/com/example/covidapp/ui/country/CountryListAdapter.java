package com.example.covidapp.ui.country;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.covidapp.R;
import com.example.covidapp.model.Country;

import java.util.ArrayList;
import java.util.List;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> implements Filterable {

    private List<Country> countryList;
    private List<Country> countryAllList;
    private Context context;

    public CountryListAdapter(List<Country> countryList, Context context) {
        this.countryList = countryList;
        this.countryAllList = new ArrayList<>(countryList);
        this.context = context;
    }

    @NonNull
    @Override
    public CountryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_country, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryListAdapter.ViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.tvTotalCases.setText(Integer.toString(country.getCases()));
        holder.tvCountryName.setText(country.getCovidCountry());

        Glide
                .with(context)
                .load(country.getFlag())
                .apply(new RequestOptions().override(240, 160))
                .into(holder.ivCountryFlag);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    private Filter covidCountriesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Country> filteredCountry = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredCountry.addAll(countryAllList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Country country : countryAllList) {
                    if (country.getCovidCountry().toLowerCase().contains(filterPattern)) {
                        filteredCountry.add(country);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredCountry;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            countryList.clear();
            countryList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return covidCountriesFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTotalCases, tvCountryName;
        ImageView ivCountryFlag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTotalCases = itemView.findViewById(R.id.tvTotalCases);
            tvCountryName = itemView.findViewById(R.id.tvCountryName);
            ivCountryFlag = itemView.findViewById(R.id.ivCountryFlag);

        }
    }
}
