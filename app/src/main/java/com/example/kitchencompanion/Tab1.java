package com.example.kitchencompanion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
// Some links(cite code more):
// https://www.codevscolor.com/android-kotlin-delete-item-recyclerview
// https://www.digitalocean.com/community/tutorials/android-recyclerview-swipe-to-delete-undo
// https://www.androidhive.info/2016/01/android-working-with-recycler-view/

public class Tab1 extends Fragment {

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;

    public Tab1() {

    }

    // Code for below copied from Tab2.java and edited
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        // Recipe list
        recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get recipes from database
        RecipeDatabase recipeDatabase = new RecipeDatabase();
        recipeAdapter = new RecipeAdapter(getContext(), recipeDatabase.getRecipes());
        recipeRecyclerView.setAdapter(recipeAdapter);

        return view;
    }
}