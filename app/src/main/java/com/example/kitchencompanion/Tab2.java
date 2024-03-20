package com.example.kitchencompanion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Tab2 extends Fragment {

    private ListView foodListView;
    private List<String> foodList;
    private FoodAdapter adapter;
    View view;

    public Tab2(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab2, container, false);

        // Initialize the list of food items
        foodList = new ArrayList<>();
        foodList.add("Apple");
        foodList.add("Banana");
        foodList.add("Orange");

        foodListView = view.findViewById(R.id.foodListView);
        adapter = new FoodAdapter(getContext(), foodList);
        foodListView.setAdapter(adapter);

        return view;
    }
}