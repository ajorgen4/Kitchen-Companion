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

public class MainActivity extends AppCompatActivity implements Tab2.PantryUpdateListener, Tab4.HouseNameUpdateListener {
    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();

    NavigationBarView bottomNavigationView;
    Settings settings;
    private RecipeDatabase recipeDatabase;

    public Map<Integer, Fragment> getFragmentMap() {
        return fragmentMap;
    }

    // Nav bar code adapted from: https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        HashMap<Integer, FoodType> foodDictionary = new HashMap<>();
        recipeDatabase = new RecipeDatabase(foodDictionary);

        // Tab2
        Tab2 tab2 = new Tab2(foodDictionary);
        tab2.setPantryUpdateListener(this);

        // Tab3
        Tab3 tab3 = new Tab3(foodDictionary, tab2);

        // Tab1
        Tab1 tab1 = new Tab1(tab2.getFoodDictionary(), recipeDatabase, tab2.getPantryList(), tab3.getAdapter());

        // Tab4
        Tab4 tab4 = new Tab4(foodDictionary);

        fragmentMap.put(R.id.recipes, tab1);
        fragmentMap.put(R.id.pantry, tab2);
        fragmentMap.put(R.id.shopping, tab3);
        fragmentMap.put(R.id.settings, tab4);

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

    public Tab2 getTab2() {
        return (Tab2) fragmentMap.get(R.id.pantry);
    }


    @Override
    public void onUpdateHouseName(String newName) {
        TextView houseNameTextView = findViewById(R.id.pantryAddFoodTitle);
        houseNameTextView.setText(newName);
    }


    public void addItemsToPantry(int foodTypeId, int count) {
        FoodType foodType = ((Tab2) fragmentMap.get(R.id.pantry)).getFoodDictionary().get(foodTypeId);
        if (foodType != null) {
            ((Tab2) fragmentMap.get(R.id.pantry)).addItems(foodType, count);
        }
    }

    public void refreshTab1Adapter() {
        Tab1 tab1 = (Tab1) fragmentMap.get(R.id.recipes);
        if (tab1 != null) {
            tab1.refreshRecipeAdapter();
        }
    }

    @Override
    public void onPantryUpdated() {
        Tab1 tab1 = (Tab1) fragmentMap.get(R.id.recipes);
        if (tab1 != null) {
            tab1.refreshRecipeAdapter();
        }
    }
}