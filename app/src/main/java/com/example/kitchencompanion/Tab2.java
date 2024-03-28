package com.example.kitchencompanion;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Pantry Tab
public class Tab2 extends Fragment {

    private ListView foodListView;
    // Example items
    private List<String> foodList;
    private FoodAdapter adapter;
    private View view;
    private FloatingActionButton addFoodButton;
    private Button filterButton;


    public Tab2(){
        foodList = new ArrayList<>();
        foodList.add("Apple");
        foodList.add("Banana");
        foodList.add("Orange");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab2, container, false);

        // The view
        foodListView = view.findViewById(R.id.foodListView);
        // The adapter model. foodList is initial items, empty in final product
        adapter = new FoodAdapter(getContext(), foodList);
        // Tie the adapter to the view
        foodListView.setAdapter(adapter);

        addFoodButton = view.findViewById(R.id.addFoodButton);
        addFoodButton.setOnClickListener(v -> showAddFoodDialog());

        filterButton = view.findViewById(R.id.pantryFiltersButton);
        filterButton.setOnClickListener(v -> showFilterDialog());

        // Filter UI management
        setFilters();

        return view;
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.pantry_filter_dialog, null);

        // Find and set up the views inside the dialog layout
        // ...
        Button cancelButton = dialogView.findViewById(R.id.pantryFilterCancelButton);

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Set the dialog to occupy approximately 75% of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels * 0.75);
        int height = (int) (displayMetrics.heightPixels * 0.75);
        dialog.getWindow().setLayout(width, height);

        // Dim the background
        dialog.getWindow().setDimAmount(0.5f);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showAddFoodDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_food_dialog, null);

        // Find and set up the views inside the dialog layout
        // ...
        Button cancelButton = dialogView.findViewById(R.id.pantryAddFoodCancelButton);

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Set the dialog to occupy approximately 75% of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels * 0.75);
        int height = (int) (displayMetrics.heightPixels * 0.75);
        dialog.getWindow().setLayout(width, height);

        // Dim the background
        dialog.getWindow().setDimAmount(0.5f);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // Only UI filters are handled here. Actual filtering is done in FoodAdapter.java
    private void setFilters() {
        Map<String, LinearLayout> filterButtonMap = new HashMap<>();
        Map<String, TextView> filterTextMap = new HashMap<>();

        // Private filter elements
        filterButtonMap.put("private", view.findViewById(R.id.privateFilterButton));
        filterTextMap.put("private", view.findViewById(R.id.privateFilterText));
        // Meats filter elements
        filterButtonMap.put("meats", view.findViewById(R.id.meatsFilterButton));
        filterTextMap.put("meats", view.findViewById(R.id.meatsFilterText));
        // Expires filter elements
        filterButtonMap.put("expires", view.findViewById(R.id.expiresFilterButton));
        filterTextMap.put("expires", view.findViewById(R.id.expiresFilterText));
        // Fruits/Vegetables filter elements
        filterButtonMap.put("fruits/vegetables", view.findViewById(R.id.fruitsVegetablesFilterButton));
        filterTextMap.put("fruits/vegetables", view.findViewById(R.id.fruitsVegetablesFilterText));

        // Create filters
        createFilter("private", filterButtonMap, filterTextMap);
        createFilter("meats", filterButtonMap, filterTextMap);
        createFilter("expires", filterButtonMap, filterTextMap);
        createFilter("fruits/vegetables", filterButtonMap, filterTextMap);
    }

    // In the future (when the backend model is made), these will need to pass filter instructions to FoodAdapter.java
    private void createFilter(String filter, Map<String, LinearLayout> filterButtonMap, Map<String, TextView> filterTextMap) {
        Drawable selected = ContextCompat.getDrawable(getContext(), R.drawable.filter_background_selected);
        Drawable unselected = ContextCompat.getDrawable(getContext(), R.drawable.filter_background_unselected);

        filterButtonMap.get(filter).setOnClickListener(v -> {
            Drawable currentBackground = filterButtonMap.get(filter).getBackground();
            // If true, we are unselecting. If false, we are selecting
            boolean isFilterSelected = currentBackground != null && currentBackground.getConstantState().equals(selected.getConstantState());

            // If we are activating a new filter, deactivate the previous one
            if (!isFilterSelected) {
                // For all filters that are not filter
                for (String key: filterButtonMap.entrySet().stream().filter(e -> !e.getKey().equals(filter)).map(Map.Entry::getKey).collect(Collectors.toList())) {
                    Drawable keyBackground = filterButtonMap.get(key).getBackground();
                    // If filter key is selected, deselect it
                    if (keyBackground != null && keyBackground.getConstantState().equals(selected.getConstantState())) {
                        filterButtonMap.get(key).setBackground(unselected);
                    }
                }
            }

            filterButtonMap.get(filter).setBackground(isFilterSelected ? unselected : selected);
            // Here, we will need to make a call to actually apply the filter in the backend
        });
    }
}