package com.example.kitchencompanion;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Tab4.HouseNameUpdateListener {

    NavigationBarView bottomNavigationView;
    Settings settings;
    private RecipeDatabase recipeDatabase;

    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();

    // Nav bar code adapted from: https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        HashMap<Integer, FoodType> foodDictionary = new HashMap<>();
        recipeDatabase = new RecipeDatabase(foodDictionary);

        Tab2 tab2 = new Tab2();
        fragmentMap.put(R.id.pantry, tab2);
        // Updated to pass both foodDictionary and recipeDatabase
        fragmentMap.put(R.id.recipes, new Tab1(foodDictionary, recipeDatabase));
        fragmentMap.put(R.id.shopping, new Tab3(tab2.getFoodDictionary()));
        fragmentMap.put(R.id.settings, new Tab4(tab2.getFoodDictionary()));

        settings = new Settings();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (fragmentMap.containsKey(itemId)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flFragment, fragmentMap.get(itemId))
                            .commitNow();
                    return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.recipes);
    }


    @Override
    public void onUpdateHouseName(String newName) {
        TextView houseNameTextView = findViewById(R.id.pantryAddFoodTitle);
        houseNameTextView.setText(newName);
    }
}