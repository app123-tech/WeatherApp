package com.appdevelopers.weatherapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdevelopers.weatherapp.Adapter.tenDaysWeatherAdapter;
import com.appdevelopers.weatherapp.Model.tenDaysWeatherItemModel;
import com.appdevelopers.weatherapp.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentTenDays extends Fragment {
    private RecyclerView recyclerView;
    private tenDaysWeatherAdapter adapter;
    private List<tenDaysWeatherItemModel> itemModels;

    public FragmentTenDays() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_ten_days, container, false);
      recyclerView = view.findViewById(R.id.tenDaysWeatherRecyclerView);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      itemModels = new ArrayList<>();
    }
}