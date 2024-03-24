package com.example.kitchencompanion;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
// Some links(cite code more):
// https://www.codevscolor.com/android-kotlin-delete-item-recyclerview
// https://www.digitalocean.com/community/tutorials/android-recyclerview-swipe-to-delete-undo
// https://www.androidhive.info/2016/01/android-working-with-recycler-view/

public class Tab1 extends Fragment {

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;
    private View view;

    public Tab1() {

    }

    // Code for below copied from Tab2.java and edited
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab1, container, false);

        // Recipe list
        recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get recipes from database
        RecipeDatabase recipeDatabase = new RecipeDatabase();
        recipeAdapter = new RecipeAdapter(getContext(), recipeDatabase.getRecipes());
        recipeRecyclerView.setAdapter(recipeAdapter);

        // Filter UI management
        setFilters();

        return view;
    }

    // Only UI filters are handled here. Actual filtering is done in FoodAdapter.java
    private void setFilters() {
        Map<String, LinearLayout> filterButtonMap = new HashMap<>();
        Map<String, TextView> filterTextMap = new HashMap<>();

        // Private filter elements
        filterButtonMap.put("favorites", view.findViewById(R.id.favoriteFilterButton));
        filterTextMap.put("favorites", view.findViewById(R.id.favoritesFilterText));
        // Meats filter elements
        filterButtonMap.put("make now", view.findViewById(R.id.makeNowFilterButton));
        filterTextMap.put("make now", view.findViewById(R.id.makeNowFilterText));
        // Expires filter elements
        filterButtonMap.put("low calorie", view.findViewById(R.id.lowCalorieFilterButton));
        filterTextMap.put("low calorie", view.findViewById(R.id.lowCalorieFilterText));
        // Fruits/Vegetables filter elements
        filterButtonMap.put("breakfast", view.findViewById(R.id.breakfastFilterButton));
        filterTextMap.put("breakfast", view.findViewById(R.id.breakfastFilterText));

        // Create filters
        createFilter("favorites", filterButtonMap, filterTextMap);
        createFilter("make now", filterButtonMap, filterTextMap);
        createFilter("low calorie", filterButtonMap, filterTextMap);
        createFilter("breakfast", filterButtonMap, filterTextMap);
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
                        filterTextMap.get(key).setTextColor(Color.WHITE);
                    }
                }
            }

            filterButtonMap.get(filter).setBackground(isFilterSelected ? unselected : selected);
            filterTextMap.get(filter).setTextColor(isFilterSelected ? Color.WHITE : Color.BLACK);
            // Here, we will need to make a call to actually apply the filter in the backend
        });
    }
}