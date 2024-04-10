package com.example.kitchencompanion;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Tab1 extends Fragment {
    private FloatingActionButton addRecipeButton;
    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;
    // Reference to the set of all FoodType objects in the app
    // Maps FoodType.getID() to FoodType
    HashMap<Integer, FoodType> foodDictionary;
    private boolean isAddMissingPopupShown = false;

    private List<PantryItem> pantryList;
    private Tab3 tab3;

    public Tab1(HashMap<Integer, FoodType> foodDictionary, RecipeDatabase recipeDatabase, List<PantryItem> pantryList, Tab3 tab3) {
        this.foodDictionary = foodDictionary;
        this.recipeDatabase = recipeDatabase;
        this.pantryList = pantryList;
        this.tab3 = tab3;
    }


    private RecipeDatabase recipeDatabase;

    // General info I used for views: https://developer.android.com/reference/android/app/Fragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addRecipeButton = view.findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(v -> showAddRecipeDialog());

        // Use shared RecipeDatabase instance across tabs
        Map<Integer, Fragment> fragmentMap = ((MainActivity) getActivity()).getFragmentMap();
        recipeAdapter = new RecipeAdapter(getContext(), recipeDatabase.getRecipes(), recipeDatabase, pantryList, foodDictionary, fragmentMap, tab3);
        recipeRecyclerView.setAdapter(recipeAdapter);

        setFilters(view);
        setUpItemTouchHelper();

        return view;
    }

    //https://developer.android.com/reference/androidx/recyclerview/widget/ItemTouchHelper
    private void setUpItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // don't think we need anything here for now
            }

            // Tutorial for onChildDraw: https://developer.android.com/reference/androidx/recyclerview/widget/ItemTouchHelper.Callback
            // Swipe recyler: https://stackoverflow.com/questions/57353844/how-to-restore-recycler-view-item-after-swipe
            // Swipe click issue? (Scrapped - workaround) https://stackoverflow.com/questions/39189159/recyclerview-swipe-with-a-view-below-not-detecting-click
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float xVal, float yVal, int actionState, boolean isCurrentlyActive) {
                RecipeAdapter.ViewHolder holder = (RecipeAdapter.ViewHolder) viewHolder;
                FrameLayout addMissingLayout = holder.addMissingLayout;
                final View foregroundView = holder.viewForeground;
                float maxSwipeDistance = -addMissingLayout.getWidth();
                float restrictedDX = Math.max(xVal, maxSwipeDistance);
                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, restrictedDX, yVal, actionState, isCurrentlyActive);

                if (restrictedDX == maxSwipeDistance && isCurrentlyActive) {
                    if (addMissingLayout.getVisibility() != View.VISIBLE) {
                        addMissingLayout.setVisibility(View.VISIBLE);
                    }

                    if (!holder.isHandlerRunning && !holder.isPopupShown) {
                        holder.isHandlerRunning = true;
                        new Handler().postDelayed(() -> {
                            if (restrictedDX == maxSwipeDistance && isCurrentlyActive && !holder.isPopupShown) {
                                holder.isPopupShown = true;
                                ((RecipeAdapter) recyclerView.getAdapter()).showAddMissingConfirmation(holder.getAdapterPosition());
                            }
                            holder.isHandlerRunning = false;
                            if (!isCurrentlyActive) {
                                holder.isPopupShown = false;
                            }
                        }, 0); //change delay if needed, time before popup appears
                    }
                } else if (restrictedDX != maxSwipeDistance) {
                    if (addMissingLayout.getVisibility() != View.GONE) {
                        addMissingLayout.setVisibility(View.GONE);
                    }
                    holder.isHandlerRunning = false;
                    holder.isPopupShown = false;
                }
            }



            // Restoring recycler view: https://stackoverflow.com/questions/57353844/how-to-restore-recycler-view-item-after-swipe
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
            // Unimplemented - Horizontal Prototype - UI Only
        });
    }

    // Unimplemented - Horizontal prototype - UI Only
    private void showAddRecipeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_recipe_dialog, null);
        Button cancelButton = dialogView.findViewById(R.id.recipeAddCloseButton);
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

    // Ensure Tab1 refreshes if we change values for the recipeCard like by Marking Cooked, etc
    // notifyDataSetChanges(): https://stackoverflow.com/questions/2345875/android-notifydatasetchanged
    public void refreshRecipeAdapter() {
        if (recipeAdapter != null) {
            recipeAdapter.notifyDataSetChanged();
        }
    }

    // https://developer.android.com/images/activity_lifecycle.png
    @Override
    public void onResume() {
        super.onResume();
        if (recipeAdapter != null) {
            recipeAdapter.updateRecipes(recipeDatabase.getRecipes());
        }
    }
}