package com.example.covidapp.ui.home;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class HomeFragment extends Fragment {

    private TextView tvTotalConfirmed, tvTotalDeaths, tvTotalRecovered, tvLastUpdated;
    private ProgressBar pbProgressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        tvTotalConfirmed = root.findViewById(R.id.tvTotalConfirmed);
        tvTotalDeaths = root.findViewById(R.id.tvTotalDeaths);
        tvTotalRecovered = root.findViewById(R.id.tvTotalRecovered);
        tvLastUpdated = root.findViewById(R.id.tvLastUpdated);

        pbProgressBar = root.findViewById(R.id.progress_circular_home);

        getActivity().setTitle("Covid19 Info.");

        getData();
        return root;
    }

    @SuppressLint("NewApi")
    private String getDate() {
        return LocalDateTime.now().toString();
    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String url = "https://corona.lmao.ninja/v3/covid-19/all";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbProgressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    tvTotalConfirmed.setText(format(jsonObject.getString("cases")));
                    tvTotalDeaths.setText(format(jsonObject.getString("deaths")));
                    tvTotalRecovered.setText(format(jsonObject.getString("recovered")));
                    tvLastUpdated.setText("Last Updated\n" + getDate());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbProgressBar.setVisibility(View.GONE);
                Log.d("Error Response: ", error.toString());
            }
        });

        queue.add(stringRequest);
    }

    public String format(String strValue) {
        final NavigableMap<Long, String> suffixes = new TreeMap<>();
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");

        Long value = Long.parseLong(strValue);
        if (value == Long.MIN_VALUE) return format(String.valueOf(Long.MIN_VALUE + 1));
        if (value < 0) return "-" + format(String.valueOf(-value));
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }
}