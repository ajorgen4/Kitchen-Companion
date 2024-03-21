package com.example.kitchencompanion;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    NavigationBarView bottomNavigationView;

    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();

    // Nav bar code adapted from: https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        fragmentMap.put(R.id.recipes, new Tab1());
        fragmentMap.put(R.id.pantry, new Tab2());
        fragmentMap.put(R.id.shopping, new Tab3());
        fragmentMap.put(R.id.settings, new Tab4());

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
}