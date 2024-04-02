package com.example.kitchencompanion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Pantry Tab
public class Tab2 extends Fragment {

    private ListView foodListView;
    // Example items
    private List<PantryItem> foodList;
    private FoodAdapter adapter;
    private View view;
    private FloatingActionButton addFoodButton;
    private Button filterButton;

    // FoodType selector stuff
    HashMap<Integer, FoodType> foodDictionary;
    // Used in FoodType selector
    ArrayList<FoodType> foodSelectorList;
    Dialog foodTypeSelector;


    public Tab2() {
        foodList = new ArrayList<PantryItem>();
        foodDictionary = createFoodDictionary();

        // Right now, initial expiration dates hardcoded. Change this to be today + expirationPeriod when the database is done
        foodList.add(new PantryItem(new FoodBatch(foodDictionary.get(0), 5, LocalDate.now().plusDays(7))));
        foodList.add(new PantryItem(new FoodBatch(foodDictionary.get(1), 3, LocalDate.now().plusDays(7))));
        foodList.add(new PantryItem(new FoodBatch(foodDictionary.get(2), 3, LocalDate.now().plusDays(7))));
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
        DatePicker expirationDatePicker = dialogView.findViewById(R.id.pantryAddFoodExpirationDatePicker);
        TextView foodTypeSelector = dialogView.findViewById(R.id.pantryAddFoodFoodTypeSelector);

        // Set the default date.
        // TODO: Update this to use the FoodType selected's expiration period
        LocalDate defaultDate = LocalDate.now().plusDays(7);
        expirationDatePicker.updateDate(
                defaultDate.getYear(),
                defaultDate.getMonthValue() - 1, // DatePicker months are 0 indexed for some reason
                defaultDate.getDayOfMonth()
        );

        // textView = foodTypeSelector
        // arrayList = foodSelectorList
        // dialog = foodSelector
        // xml element text_view = pantryAddFoodFoodTypeSelector
        // dialog_searchable_spinner = food_type_selector
        // Code adapted from: https://www.youtube.com/watch?v=5iIXg4-Iw3U

        // This must stay here to ensure it remains up to date with foodDictionary
        foodSelectorList = new ArrayList<FoodType>(foodDictionary.values());
        foodTypeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tab2.this.foodTypeSelector = new Dialog(requireContext());
                Tab2.this.foodTypeSelector.setContentView(R.layout.food_type_selector);
                Tab2.this.foodTypeSelector.getWindow().setLayout(650, 800);
                Tab2.this.foodTypeSelector.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Tab2.this.foodTypeSelector.show();

                EditText foodSelectorSearchBar = Tab2.this.foodTypeSelector.findViewById(R.id.foodSelectorSearchBar);
                ListView foodSelectorListView = Tab2.this.foodTypeSelector.findViewById(R.id.foodSelectorListView);

                FoodTypeSelectorAdapter adapter = new FoodTypeSelectorAdapter(getContext(), foodSelectorList);
                foodSelectorListView.setAdapter(adapter);

                foodSelectorSearchBar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                foodSelectorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        foodTypeSelector.setText(adapter.getItem(position).getItemName());
                        Tab2.this.foodTypeSelector.dismiss();
                    }
                });
            }
        });

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Set the dialog to occupy ~75% of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels * 0.75);
        int height = (int) (displayMetrics.heightPixels * 0.75);
        dialog.getWindow().setLayout(width, height);

        // Dim the background
        dialog.getWindow().setDimAmount(0.5f);

        // Cancel button functionality
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

    private HashMap<Integer, FoodType> createFoodDictionary() {
        HashMap<Integer, FoodType> tempFoodDictionary = new HashMap<Integer, FoodType>();
        FoodType currItem;

        currItem = new FoodType("Apple", Enums.FoodGroup.FRUIT, 7);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Banana", Enums.FoodGroup.FRUIT, 7);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Orange", Enums.FoodGroup.FRUIT, 7);
        tempFoodDictionary.put(currItem.getID(), currItem);

        return tempFoodDictionary;
    }

    public HashMap<Integer, FoodType> getFoodDictionary() {
        return foodDictionary;
    }
}