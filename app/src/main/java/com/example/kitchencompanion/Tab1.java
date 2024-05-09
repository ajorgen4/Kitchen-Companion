package com.example.kitchencompanion;

import android.app.AlertDialog;
import android.graphics.Canvas;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.RangeSlider;
import android.view.ViewGroup;
import android.os.Handler;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class Tab1 extends Fragment {
    private FloatingActionButton addRecipeButton;
    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;

    // Dialog Elements
    private RangeSlider cookTimeRangeSlider, caloriesRangeSlider;
    private TextView cookTimeLabel, caloriesLabel;

    private Button easyButton, mediumButton, hardButton;
    private String difficultySelected = "Easy";

    // Reference to the set of all FoodType objects in the app
    // Maps FoodType.getID() to FoodType
    HashMap<Integer, FoodType> foodDictionary;
    private List<PantryItem> pantryList;
    private Tab3 tab3;
    private RecipeDatabase recipeDatabase;

    public Tab1(HashMap<Integer, FoodType> foodDictionary, RecipeDatabase recipeDatabase, List<PantryItem> pantryList, Tab3 tab3) {
        this.foodDictionary = foodDictionary;
        this.recipeDatabase = recipeDatabase;
        this.pantryList = pantryList;
        this.tab3 = tab3;
    }

    // Inflate layout and set up views
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        // Initialize RecyclerView
        recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addRecipeButton = view.findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(v -> showAddRecipeDialog());

        // Use shared RecipeDatabase instance
        Map<Integer, Fragment> fragmentMap = ((MainActivity) getActivity()).getFragmentMap();
        recipeAdapter = new RecipeAdapter(getContext(), recipeDatabase.getRecipes(), recipeDatabase, pantryList, foodDictionary, fragmentMap, tab3);
        recipeRecyclerView.setAdapter(recipeAdapter);

        // Set up ItemTouchHelper
        setUpItemTouchHelper();

        // Button to open the filter dialog
        Button filterButton = view.findViewById(R.id.recipeFiltersButton);
        filterButton.setOnClickListener(v -> showFilterDialog());

        return view;
    }

    // Dialog to show filters
// Full method to handle dialogs and button appearance updates
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_filter_dialog, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Initialize Difficulty Buttons
        easyButton = dialogView.findViewById(R.id.easyButton);
        mediumButton = dialogView.findViewById(R.id.mediumButton);
        hardButton = dialogView.findViewById(R.id.hardButton);

        // TextView labels
        cookTimeLabel = dialogView.findViewById(R.id.cookTimeLabel);
        caloriesLabel = dialogView.findViewById(R.id.caloriesLabel);

        // Set up the difficulty buttons and their appearance
        View.OnClickListener difficultyClickListener = v -> {
            if (v == easyButton) {
                updateDifficultyButtonAppearance("Easy");
            } else if (v == mediumButton) {
                updateDifficultyButtonAppearance("Medium");
            } else if (v == hardButton) {
                updateDifficultyButtonAppearance("Hard");
            }
        };

        easyButton.setOnClickListener(difficultyClickListener);
        mediumButton.setOnClickListener(difficultyClickListener);
        hardButton.setOnClickListener(difficultyClickListener);

        // Set the initial appearance of the difficulty buttons
        updateDifficultyButtonAppearance(difficultySelected);

        // Initialize Range Sliders
        cookTimeRangeSlider = dialogView.findViewById(R.id.cookTimeRangeSlider);
        caloriesRangeSlider = dialogView.findViewById(R.id.caloriesRangeSlider);

        setUpRangeSliders();

        // Add dialog handlers for allergen and attributes lists
        Button allergenDropdownButton = dialogView.findViewById(R.id.allergenDropdownButton);
        Button attributesDropdownButton = dialogView.findViewById(R.id.attributesDropdownButton);

        allergenDropdownButton.setOnClickListener(v -> showAllergenDialog());
        attributesDropdownButton.setOnClickListener(v -> showAttributesDialog());

        // Dialog action buttons
        Button applyButton = dialogView.findViewById(R.id.recipeFilterApplyButton);
        Button resetButton = dialogView.findViewById(R.id.recipeFilterResetButton);
        Button cancelButton = dialogView.findViewById(R.id.recipeFilterCancelButton);

        applyButton.setOnClickListener(v -> dialog.dismiss());
        resetButton.setOnClickListener(v -> dialog.dismiss());
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // Set up difficulty buttons
    private void setUpDifficultyButtons() {
        View.OnClickListener difficultyClickListener = v -> {
            if (v == easyButton) {
                difficultySelected = "Easy";
                setButtonAppearance(easyButton, true);
                setButtonAppearance(mediumButton, false);
                setButtonAppearance(hardButton, false);
            } else if (v == mediumButton) {
                difficultySelected = "Medium";
                setButtonAppearance(easyButton, false);
                setButtonAppearance(mediumButton, true);
                setButtonAppearance(hardButton, false);
            } else if (v == hardButton) {
                difficultySelected = "Hard";
                setButtonAppearance(easyButton, false);
                setButtonAppearance(mediumButton, false);
                setButtonAppearance(hardButton, true);
            }
        };

        easyButton.setOnClickListener(difficultyClickListener);
        mediumButton.setOnClickListener(difficultyClickListener);
        hardButton.setOnClickListener(difficultyClickListener);

        // Initialize the default selected difficulty
        setButtonAppearance(easyButton, true);
        setButtonAppearance(mediumButton, false);
        setButtonAppearance(hardButton, false);
    }

    // Utility to set the appearance of difficulty buttons
    private void updateDifficultyButtonAppearance(String selectedDifficulty) {
        difficultySelected = selectedDifficulty;
        setButtonAppearance(easyButton, "Easy".equals(selectedDifficulty));
        setButtonAppearance(mediumButton, "Medium".equals(selectedDifficulty));
        setButtonAppearance(hardButton, "Hard".equals(selectedDifficulty));
    }

    // Utility to modify button appearance based on selection
    private void setButtonAppearance(Button button, boolean selected) {
        if (selected) {
            button.setBackgroundResource(android.R.drawable.btn_default); // Highlight
        } else {
            button.setBackgroundResource(android.R.color.transparent); // Normal
        }
    }

    // Re-add the method to show the allergen list dialog
    private void showAllergenDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Allergens");
        String[] allergens = {"GLUTEN", "DAIRY", "NUTS", "SHELLFISH", "SOY", "EGGS", "FISH", "WHEAT"};
        boolean[] checkedItems = new boolean[allergens.length];
        builder.setMultiChoiceItems(allergens, checkedItems, (dialog, which, isChecked) -> checkedItems[which] = isChecked);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Re-add the method to show the attributes list dialog
    private void showAttributesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Attributes");
        String[] attributes = {"VEGAN", "GLUTEN_FREE", "LOW_CALORIE", "VEGETARIAN", "LOW_SODIUM", "LOW_FAT", "LOW_CHOLESTEROL", "LOW_SUGAR", "LOW_CARB", "ORGANIC"};
        boolean[] checkedItems = new boolean[attributes.length];
        builder.setMultiChoiceItems(attributes, checkedItems, (dialog, which, isChecked) -> checkedItems[which] = isChecked);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
    // Set up the range sliders again to show the dynamic ranges
    private void setUpRangeSliders() {
        cookTimeRangeSlider.setValues(0f, 300f);
        cookTimeRangeSlider.setStepSize(5);
        cookTimeRangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            int minMinutes = Math.round(slider.getValues().get(0));
            int maxMinutes = Math.round(slider.getValues().get(1));
            String maxMinutesText = maxMinutes == 300 ? maxMinutes + "+" : String.valueOf(maxMinutes);
            cookTimeLabel.setText(minMinutes + " m - " + maxMinutesText + " m");
        });

        caloriesRangeSlider.setValues(0f, 2000f);
        caloriesRangeSlider.setStepSize(50);
        caloriesRangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            int minCalories = Math.round(slider.getValues().get(0));
            int maxCalories = Math.round(slider.getValues().get(1));
            String maxCaloriesText = maxCalories == 2000 ? maxCalories + "+" : String.valueOf(maxCalories);
            caloriesLabel.setText(minCalories + " cal - " + maxCaloriesText + " cal");
        });
    }

    // Other utility methods
    private void setUpItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Swipe actions handled in `onChildDraw`
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float xVal, float yVal, int actionState, boolean isCurrentlyActive) {
                RecipeAdapter.ViewHolder holder = (RecipeAdapter.ViewHolder) viewHolder;
                FrameLayout addMissingLayout = holder.addMissingLayout;
                final View foregroundView = holder.viewForeground;
                float maxSwipeDistance = -addMissingLayout.getWidth();
                float restrictedDX = Math.max(xVal, maxSwipeDistance);
                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, restrictedDX, yVal, actionState, isCurrentlyActive);

                // Check if a dialog is already shown
                if (restrictedDX == maxSwipeDistance && isCurrentlyActive && !((RecipeAdapter) recyclerView.getAdapter()).isDialogShown()) {
                    addMissingLayout.setVisibility(View.VISIBLE);
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
                        }, 300);
                    }
                } else if (restrictedDX != maxSwipeDistance) {
                    addMissingLayout.setVisibility(View.GONE);
                    holder.isHandlerRunning = false;
                    holder.isPopupShown = false;
                }
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

    private void showAddRecipeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_recipe_dialog, null);
        Button cancelButton = dialogView.findViewById(R.id.recipeAddCloseButton);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dialog.getWindow().setLayout((int) (displayMetrics.widthPixels * 0.75), (int) (displayMetrics.heightPixels * 0.75));
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void refreshRecipeAdapter() {
        if (recipeAdapter != null) {
            recipeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recipeAdapter != null) {
            recipeAdapter.updateRecipes(recipeDatabase.getRecipes());
        }
    }
}
