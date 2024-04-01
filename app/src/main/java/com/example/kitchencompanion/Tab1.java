package com.example.kitchencompanion;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

public class Tab1 extends Fragment {
    private FloatingActionButton addRecipeButton;
    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;

    public Tab1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addRecipeButton = view.findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(v -> showAddRecipeDialog());

        RecipeDatabase recipeDatabase = new RecipeDatabase();
        recipeAdapter = new RecipeAdapter(getContext(), recipeDatabase.getRecipes());
        recipeRecyclerView.setAdapter(recipeAdapter);

        setFilters(view);
        setUpItemTouchHelper();

        return view;
    }

    private void setUpItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((RecipeAdapter.ViewHolder) viewHolder).viewForeground;
                View addMissingLayout = viewHolder.itemView.findViewById(R.id.addMissingLayout);

                float maxSwipeDistance = -addMissingLayout.getWidth();
                float restrictedDX = Math.max(dX, maxSwipeDistance);

                if (restrictedDX <= maxSwipeDistance) {
                    addMissingLayout.setVisibility(View.VISIBLE);
                } else {
                    addMissingLayout.setVisibility(View.GONE);
                }

                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, restrictedDX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View foregroundView = ((RecipeAdapter.ViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().clearView(foregroundView);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    final View foregroundView = ((RecipeAdapter.ViewHolder) viewHolder).viewForeground;
                    getDefaultUIUtil().onSelected(foregroundView);
                }
            }
        }).attachToRecyclerView(recipeRecyclerView);
    }

    private void setFilters(View view) {
        Map<String, LinearLayout> filterButtonMap = new HashMap<>();
        Map<String, TextView> filterTextMap = new HashMap<>();
        filterButtonMap.put("favorites", view.findViewById(R.id.favoriteFilterButton));
        filterTextMap.put("favorites", view.findViewById(R.id.favoritesFilterText));
        filterButtonMap.put("make now", view.findViewById(R.id.makeNowFilterButton));
        filterTextMap.put("make now", view.findViewById(R.id.makeNowFilterText));
        filterButtonMap.put("low calorie", view.findViewById(R.id.lowCalorieFilterButton));
        filterTextMap.put("low calorie", view.findViewById(R.id.lowCalorieFilterText));
        filterButtonMap.put("breakfast", view.findViewById(R.id.breakfastFilterButton));
        filterTextMap.put("breakfast", view.findViewById(R.id.breakfastFilterText));
        createFilter("favorites", filterButtonMap, filterTextMap);
        createFilter("make now", filterButtonMap, filterTextMap);
        createFilter("low calorie", filterButtonMap, filterTextMap);
        createFilter("breakfast", filterButtonMap, filterTextMap);
    }

    private void createFilter(String filter, Map<String, LinearLayout> filterButtonMap, Map<String, TextView> filterTextMap) {
        Drawable selected = ContextCompat.getDrawable(getContext(), R.drawable.filter_background_selected);
        Drawable unselected = ContextCompat.getDrawable(getContext(), R.drawable.filter_background_unselected);
        filterButtonMap.get(filter).setOnClickListener(v -> {
            Drawable currentBackground = filterButtonMap.get(filter).getBackground();
            boolean isFilterSelected = currentBackground != null && currentBackground.getConstantState().equals(selected.getConstantState());
            filterButtonMap.get(filter).setBackground(isFilterSelected ? unselected : selected);
        });
    }

    private void showAddRecipeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_recipe_dialog, null);
        Button cancelButton = dialogView.findViewById(R.id.addRecipeCancelButton);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels * 0.75);
        int height = (int) (displayMetrics.heightPixels * 0.75);
        dialog.getWindow().setLayout(width, height);
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
