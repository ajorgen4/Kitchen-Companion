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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Pantry Tab
public class Tab2 extends Fragment {

    private ListView foodListView;
    // Example items
    private ArrayList<PantryItem> pantryList;
    private FoodAdapter adapter;
    private View view;
    private FloatingActionButton addFoodButton;
    private Button filterButton;
    private HashMap<Integer, FoodType> foodDictionary;
    // Used in FoodType selector
    private ArrayList<FoodType> foodSelectorList;
    private FoodTypeSelectorAdapter foodSelectorListAdapter;
    private Dialog foodTypeSelector;
    // THIS IS WHERE YOU ACCESS THE FOOD SELECTED BY THE FOODTYPE SELECTOR
    private FoodType selectedFood;
    private PantryItemFilterFields filterFields;


    public interface PantryUpdateListener {
        void onPantryUpdated();
    }
    private PantryUpdateListener updateListener;
    // Call this method whenever the pantry is updated
    private void notifyPantryUpdated() {
        if (updateListener != null) {
            updateListener.onPantryUpdated();
        }
    }

    public void setPantryUpdateListener(PantryUpdateListener listener) {
        this.updateListener = listener;
    }

    public Tab2(HashMap<Integer, FoodType> foodDictionary) {
        this.pantryList = new ArrayList<PantryItem>();
        this.foodDictionary = foodDictionary;
        createFoodDictionary(foodDictionary);
        prePopulatePantry();
        this.filterFields = new PantryItemFilterFields();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab2, container, false);

        // The view
        foodListView = view.findViewById(R.id.foodListView);
        // The adapter model. foodList is initial items, empty in final product
        adapter = new FoodAdapter(getContext(), pantryList, this, filterFields);
        // Tie the adapter to the view
        foodListView.setAdapter(adapter);

        addFoodButton = view.findViewById(R.id.addFoodButton);
        addFoodButton.setOnClickListener(v -> showAddFoodDialog());

        filterButton = view.findViewById(R.id.pantryFiltersButton);
        filterButton.setOnClickListener(v -> showFilterDialog());

        EditText pantrySearchBar = view.findViewById(R.id.pantrySearchBar);
        pantrySearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString();
                filterFields.setName(searchText);
                adapter.getFilter().filter(searchText);
            }
        });

        // Filter UI management
        setFilters();

        return view;
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.pantry_filter_dialog, null);

        // Find and set up the views inside the dialog layout
        Button applyButton = dialogView.findViewById(R.id.pantryFilterApplyButton);
        Button resetButton = dialogView.findViewById(R.id.pantryFilterResetButton);
        Button cancelButton = dialogView.findViewById(R.id.pantryFilterCancelButton);
        CheckBox pantryFilterLowBox = dialogView.findViewById(R.id.pantryFilterLowBox);
        CheckBox pantryFilterPrivateBox = dialogView.findViewById(R.id.pantryFilterPrivateBox);
        EditText pantryFilterExpirationText = dialogView.findViewById(R.id.pantryFilterExpirationText);

        ListView foodGroupListView = dialogView.findViewById(R.id.pantryFilterFoodGroups);
        List<Enums.FoodGroup> foodGroups = Arrays.asList(Enums.FoodGroup.values());
        List<Enums.FoodGroup> initialSelection = (filterFields.getFoodGroups() != null) ? filterFields.getFoodGroups() : new ArrayList<>();
        FoodGroupAdapter foodGroupAdapter = new FoodGroupAdapter(requireContext(), foodGroups, initialSelection);
        foodGroupListView.setAdapter(foodGroupAdapter);

        // Update UI if values exist
        pantryFilterLowBox.setChecked(filterFields.getLow());
        pantryFilterPrivateBox.setChecked(filterFields.getPrivate());
        if (filterFields.getExpirationMax() != Integer.MIN_VALUE) {
            pantryFilterExpirationText.setText(String.valueOf(filterFields.getExpirationMax()));
        }

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

        resetButton.setOnClickListener(v -> {
            // Reset UI only. If they click Apply, it will reset values in model. If they Cancel, it won't
            pantryFilterLowBox.setChecked(false);
            pantryFilterPrivateBox.setChecked(false);
            pantryFilterExpirationText.getText().clear();
            pantryFilterExpirationText.setHint("Expires within x days");
            foodGroupAdapter.clearSelection();
        });

        applyButton.setOnClickListener(v -> {
            List<Enums.FoodGroup> selectedFoodGroups = foodGroupAdapter.getSelectedFoodGroups(); // ArrayList
            boolean isLowChecked = pantryFilterLowBox.isChecked();
            boolean isPrivateChecked = pantryFilterPrivateBox.isChecked();
            String expirationTextValue = pantryFilterExpirationText.getText().toString().trim();
            int expirationDays = Integer.MIN_VALUE; // Integer.MIN_VALUE indicates no value in the filter fields class
            if (!expirationTextValue.isEmpty()) {
                expirationDays = Integer.parseInt(expirationTextValue);
            }

            filterFields.setLow(isLowChecked);
            filterFields.setPrivate(isPrivateChecked);
            filterFields.setExpirationMax(expirationDays);
            filterFields.setFoodGroups(selectedFoodGroups);

            adapter.getFilter().filter("Not needed");

            dialog.dismiss();
        });

        dialog.show();
    }

    private void showAddFoodDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_food_dialog, null);

        // Find and set up the views inside the dialog layout
        // Buttons
        Button cancelButton = dialogView.findViewById(R.id.pantryAddFoodCancelButton);
        Button createButton = dialogView.findViewById(R.id.pantryAddFoodCreateButton);

        // Info
        final DatePicker expirationDatePicker = dialogView.findViewById(R.id.pantryAddFoodExpirationDatePicker);
        final TextView foodTypeSelector = dialogView.findViewById(R.id.pantryAddFoodFoodTypeSelector);
        final EditText countEditText = dialogView.findViewById(R.id.pantryAddFoodCountInput);
        final CheckBox privateStorage = dialogView.findViewById(R.id.pantryAddFoodPrivateCheckBox);

        /*
            ########################################################
            ######## FOODTYPE SELECTOR CODE, DO NOT CHANGE! ########
            ########################################################
         */
        // Code adapted from: https://www.youtube.com/watch?v=5iIXg4-Iw3U
        // This initialization must stay here to ensure it remains up to date with foodDictionary
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

                foodSelectorListAdapter = new FoodTypeSelectorAdapter(getContext(), foodSelectorList);
                foodSelectorListView.setAdapter(foodSelectorListAdapter);

                foodSelectorSearchBar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        foodSelectorListAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                foodSelectorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Pass the selected food
                        selectedFood = foodSelectorListAdapter.getItem(position);
                        // Update visuals
                        foodTypeSelector.setText(selectedFood.getItemName());
                        // Update expiration
                        LocalDate defaultDate = LocalDate.now().plusDays(selectedFood.getExpirationPeriod());
                        expirationDatePicker.updateDate(
                                defaultDate.getYear(),
                                defaultDate.getMonthValue() - 1, // DatePicker months are 0 indexed for some reason
                                defaultDate.getDayOfMonth()
                        );
                        // Dismiss dialog
                        Tab2.this.foodTypeSelector.dismiss();
                    }
                });

                // FOODTYPE SELECTOR ADD BUTTON
                Button foodSelectorAddButton = Tab2.this.foodTypeSelector.findViewById(R.id.foodSelectorAddButton);
                foodSelectorAddButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(requireContext());
                        dialog.setContentView(R.layout.add_foodtype_dialog);

                        Button addFoodTypeCancelButton = dialog.findViewById(R.id.addFoodTypeCancelButton);
                        addFoodTypeCancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        Button addFoodTypeCreateButton = dialog.findViewById(R.id.addFoodTypeCreateButton);
                        addFoodTypeCreateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Elements
                                EditText addFoodNameEditText = dialog.findViewById(R.id.addFoodNameEditText);
                                EditText addFoodExpirationEditText = dialog.findViewById(R.id.addFoodExpirationEditText);
                                Spinner addFoodGroupSpinner = dialog.findViewById(R.id.addFoodGroupSpinner);

                                // Data
                                String name = addFoodNameEditText.getText().toString();
                                String expirationPeriodString = addFoodExpirationEditText.getText().toString().trim();
                                Enums.FoodGroup foodGroup = Enums.FoodGroup.values()[addFoodGroupSpinner.getSelectedItemPosition()];

                                boolean isValid = true;

                                // Ensure name field is filled in
                                if (name.isEmpty()) {
                                    addFoodNameEditText.setError("Please enter a name");
                                    isValid = false;
                                } else {
                                    addFoodNameEditText.setError(null);
                                }

                                // Ensure expiration date field is filled in
                                if (expirationPeriodString.isEmpty()) {
                                    addFoodExpirationEditText.setError("Please enter an expiration period");
                                    isValid = false;
                                } else {
                                    addFoodExpirationEditText.setError(null);
                                }

                                if (isValid) {
                                    int expirationPeriod = Integer.parseInt(expirationPeriodString);
                                    FoodType newFoodType = new FoodType(name, foodGroup, expirationPeriod);

                                    boolean exists = false;
                                    for (Map.Entry<Integer, FoodType> entry : foodDictionary.entrySet()) {
                                        FoodType existingFoodType = entry.getValue();
                                        if (existingFoodType.compareTo(newFoodType) == 0) {
                                            exists = true;
                                            addFoodNameEditText.setError("This item already exists");
                                            break;
                                        }
                                    }

                                    if (!exists) {
                                        Tab2.this.foodDictionary.put(newFoodType.getID(), newFoodType);
                                        Tab2.this.foodSelectorList.add(newFoodType);

                                        Tab2.this.foodSelectorListAdapter.notifyDataSetChanged();

                                        dialog.dismiss();
                                    }
                                }
                            }
                        });

                        dialog.show();
                    }
                });
            }
        });
        /*
            ############################################
            ######## FOODTYPE SELECTOR CODE END ########
            ############################################
         */

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

        // Create button functionality
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countString = countEditText.getText().toString();
                boolean privateStorageBool = privateStorage.isChecked();
                LocalDate expirationDate = LocalDate.of(
                        expirationDatePicker.getYear(),
                        expirationDatePicker.getMonth() + 1, // date pickers indexed at 0 for some reason
                        expirationDatePicker.getDayOfMonth());

                // Data cleaning
                boolean isValid = true;

                // ensure FoodType is selected
                if (selectedFood == null) {
                    foodTypeSelector.setError("Please select a food");
                    isValid = false;
                } else {
                    foodTypeSelector.setError(null);
                }

                // Ensure count is filled in
                if (countString.isEmpty()) {
                    countEditText.setError("Please enter a number of items");
                    isValid = false;
                } else {
                    countEditText.setError(null);
                }

                if (isValid) {
                    addItemsInternal(Tab2.this.selectedFood, Integer.parseInt(countString), privateStorageBool, expirationDate);
                    adapter.getFilter().filter("Not needed");
                    selectedFood = null;

                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    // FOR INTERNAL USE ONLY
    // Allows support for custom expiration dates and private storage
    private void addItemsInternal(FoodType foodType, int count, boolean isPrivate, LocalDate expirationDate) {
        if (count <= 0) {
            return;
        }

        boolean exists = false;
        FoodBatch batch = new FoodBatch(foodType, count, expirationDate);
        PantryItem potentialItem = new PantryItem(batch, isPrivate);

        for (PantryItem item : pantryList) {
            if (item.equalTo(potentialItem)) {
                exists = true;
                item.addBatch(batch);
            }
        }

        if (!exists) { // If the proper item wasn't found (it isn't in their pantry)
            pantryList.add(potentialItem);
        }

        adapter.notifyDataSetChanged();
        adapter.getFilter().filter("Not needed");
        notifyPantryUpdated();
    }


    public void addItemsPrivate(FoodType foodType, int count, boolean isPrivate) {
        addItemsInternal(foodType, count, isPrivate, LocalDate.now().plusDays(foodType.getExpirationPeriod()));
    }

    // Shopping list, plus button use this
    public void addItems(FoodType foodType, int count) {
        // Assumed not private, default expiration period
        addItemsInternal(foodType, count, false, LocalDate.now().plusDays(foodType.getExpirationPeriod()));
        notifyPantryUpdated(); // Added this, remove if broken?
    }

    // For anyone reading this, the adapter will handle removing empty items. It doesn't need to be done explicitly.
    public boolean removeItemsInternal(FoodType foodType, int count, boolean isPrivate) {
        FoodBatch batch = new FoodBatch(foodType, count, LocalDate.now());
        PantryItem potentialItem = new PantryItem(batch, isPrivate);

        for (PantryItem item : pantryList) {
            if (item.equalTo(potentialItem)) {
                item.removeItemCount(count);

                adapter.notifyDataSetChanged();
                adapter.getFilter().filter("Not needed");
                notifyPantryUpdated(); // Added to make changes take place instantly

                return true;
            }
        }

        return false;
    }

    // Recipes, minus button use this
    public void removeItems(FoodType foodType, int count) {
        PantryItem item = pantryList.stream()
                .filter(p -> p.getType().equals(foodType))
                .findFirst()
                .orElse(null);

        if (item != null) {
            int pantryCountBefore = item.getCount();
            item.removeItemCount(count);
            int pantryCountAfter = item.getCount();
            //System.out.println("DEBUG - Tab2 - Removing: " + foodType.getItemName() + " (ID: " + foodType.getID() + ") Count: " + count + " Current Pantry Count: " + pantryCountAfter);
            if (pantryCountAfter == 0) {
                pantryList.remove(item);
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }


    /*public void removeItems(FoodType foodType, int count) {
        if (!removeItemsInternal(foodType, count, false)) { // try to remove from public first
            removeItemsInternal(foodType, count, true);
        }
    }*/


    // Only UI filters are handled here. Actual filtering is done in FoodAdapter.java
    private void setFilters() {
        Map<String, LinearLayout> filterButtonMap = new HashMap<>();
        Map<String, TextView> filterTextMap = new HashMap<>();

        // Private filter elements
        filterButtonMap.put("private", view.findViewById(R.id.privateFilterButton));
        filterTextMap.put("private", view.findViewById(R.id.privateFilterText));
        // Proteins filter elements
        filterButtonMap.put("proteins", view.findViewById(R.id.meatsFilterButton));
        filterTextMap.put("proteins", view.findViewById(R.id.proteinsFilterText));
        // Expires filter elements
        filterButtonMap.put("expires", view.findViewById(R.id.expiresFilterButton));
        filterTextMap.put("expires", view.findViewById(R.id.expiresFilterText));
        // Fruits/Vegetables filter elements
        filterButtonMap.put("fruits/vegetables", view.findViewById(R.id.fruitsVegetablesFilterButton));
        filterTextMap.put("fruits/vegetables", view.findViewById(R.id.fruitsVegetablesFilterText));
        // Low filter elements
        filterButtonMap.put("low", view.findViewById(R.id.lowFilterButton));
        filterTextMap.put("low", view.findViewById(R.id.lowFilterText));
        // IF ADDING A NEW FILTER, FIRST ADD BUTTON ELEMENT HERE

        // Create filters
        createFilter("private", filterButtonMap, filterTextMap);
        createFilter("proteins", filterButtonMap, filterTextMap);
        createFilter("expires", filterButtonMap, filterTextMap);
        createFilter("fruits/vegetables", filterButtonMap, filterTextMap);
        createFilter("low", filterButtonMap, filterTextMap);
        // SECOND, ADD A CALL HERE. THEN ADD FUNCTIONALITY IN createFilter()
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

            // Functionality
            if (!isFilterSelected) { // Applying filter
                if (filter.equals("private")) {
                    filterFields.resetLow();
                    filterFields.resetFoodGroups();
                    filterFields.resetExpirationMax();

                    filterFields.setPrivate(true);
                } else if (filter.equals("proteins")) {
                    ArrayList<Enums.FoodGroup> foodGroups = new ArrayList<>();
                    foodGroups.add(Enums.FoodGroup.PROTEIN);

                    filterFields.resetLow();
                    filterFields.resetExpirationMax();
                    filterFields.resetPrivate();

                    filterFields.setFoodGroups(foodGroups);
                } else if (filter.equals("expires")) {
                    filterFields.resetFoodGroups();
                    filterFields.resetLow();
                    filterFields.resetPrivate();

                    filterFields.setExpirationMax(7);
                } else if (filter.equals("fruits/vegetables")) {
                    ArrayList<Enums.FoodGroup> foodGroups = new ArrayList<>();
                    foodGroups.add(Enums.FoodGroup.FRUIT);
                    foodGroups.add(Enums.FoodGroup.VEGETABLE);

                    filterFields.resetLow();
                    filterFields.resetExpirationMax();
                    filterFields.resetPrivate();

                    filterFields.setFoodGroups(foodGroups);
                } else if (filter.equals("low")) {
                    filterFields.resetExpirationMax();
                    filterFields.resetFoodGroups();
                    filterFields.resetPrivate();

                    filterFields.setLow(true);
                }
                // THIRD, ADD FUNCTIONALITY IN AN IF HERE
            } else { // Removing filter
                filterFields.resetExpirationMax();
                filterFields.resetLow();
                filterFields.resetFoodGroups();
                filterFields.resetPrivate();
            }
            adapter.getFilter().filter("Not needed");
        });
    }

    private void createFoodDictionary(HashMap<Integer, FoodType> foodDictionary) {
        FoodType currItem;

        // First items(Apple is ID 0 for reference)
        currItem = new FoodType("Apple", Enums.FoodGroup.FRUIT, 7);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Banana", Enums.FoodGroup.FRUIT, 7);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Orange", Enums.FoodGroup.FRUIT, 7);
        foodDictionary.put(currItem.getID(), currItem);

        // New Items for Oven Baked Risotto(Onion is ID 3 for reference)
        currItem = new FoodType("Onion", Enums.FoodGroup.VEGETABLE, 80);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Olive Oil", Enums.FoodGroup.CONDIMENT, 600);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Rice", Enums.FoodGroup.GRAIN, 600);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Vegetable Broth", Enums.FoodGroup.BEVERAGE, 5);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Parmesan", Enums.FoodGroup.DAIRY, 270);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Parsley", Enums.FoodGroup.HERB, 8000);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Salt", Enums.FoodGroup.CONDIMENT, 10000);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Black Pepper", Enums.FoodGroup.SPICE, 1100);
        foodDictionary.put(currItem.getID(), currItem);

        // New Items for Chicken Noodle Soup(Chicken is ID 11 for reference)
        currItem = new FoodType("Chicken", Enums.FoodGroup.PROTEIN, 6);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Pasta", Enums.FoodGroup.GRAIN, 700);
        foodDictionary.put(currItem.getID(), currItem);
        // Assuming carrots and celery are additional ingredients
        currItem = new FoodType("Carrots", Enums.FoodGroup.VEGETABLE, 24);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Celery", Enums.FoodGroup.VEGETABLE, 30);
        foodDictionary.put(currItem.getID(), currItem);

        // New Items for Chicken Parmesan(Bread Crumbs is ID 15 for reference)
        currItem = new FoodType("Bread Crumbs", Enums.FoodGroup.GRAIN, 365);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Eggs", Enums.FoodGroup.PROTEIN, 30);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Marinara Sauce", Enums.FoodGroup.CONDIMENT, 500);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Mozzarella", Enums.FoodGroup.DAIRY, 35);
        foodDictionary.put(currItem.getID(), currItem);

        // Fruits(Grapes is ID 19 for reference)
        currItem = new FoodType("Grapes", Enums.FoodGroup.FRUIT, 14);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Peach", Enums.FoodGroup.FRUIT, 5);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Plum", Enums.FoodGroup.FRUIT, 14);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Pear", Enums.FoodGroup.FRUIT, 9);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Strawberry", Enums.FoodGroup.FRUIT, 6);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Raspberry", Enums.FoodGroup.FRUIT, 3);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Blueberry", Enums.FoodGroup.FRUIT, 7);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Blackberry", Enums.FoodGroup.FRUIT, 3);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Mango", Enums.FoodGroup.FRUIT, 11);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Pineapple", Enums.FoodGroup.FRUIT, 5);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Watermelon", Enums.FoodGroup.FRUIT, 21);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Cantaloupe", Enums.FoodGroup.FRUIT, 21);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Honeydew", Enums.FoodGroup.FRUIT, 28);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Kiwi", Enums.FoodGroup.FRUIT, 8);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Papaya", Enums.FoodGroup.FRUIT, 14);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Guava", Enums.FoodGroup.FRUIT, 19);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Cherry", Enums.FoodGroup.FRUIT, 6);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Lemon", Enums.FoodGroup.FRUIT, 14);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Lime", Enums.FoodGroup.FRUIT, 19);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Cranberry", Enums.FoodGroup.FRUIT, 30);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Coconut", Enums.FoodGroup.FRUIT, 180);
        foodDictionary.put(currItem.getID(), currItem);

        // Vegetables(Tomato is ID 40 for reference)
        currItem = new FoodType("Tomato", Enums.FoodGroup.VEGETABLE, 10);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Broccoli", Enums.FoodGroup.VEGETABLE, 5);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Spinach", Enums.FoodGroup.VEGETABLE, 14);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Bell Pepper", Enums.FoodGroup.VEGETABLE, 12);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Cucumber", Enums.FoodGroup.VEGETABLE, 14);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Zucchini", Enums.FoodGroup.VEGETABLE, 7);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Lettuce", Enums.FoodGroup.VEGETABLE, 16);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Cabbage", Enums.FoodGroup.VEGETABLE, 45);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Garlic", Enums.FoodGroup.VEGETABLE, 180);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Potato", Enums.FoodGroup.VEGETABLE, 50);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Sweet Potato", Enums.FoodGroup.VEGETABLE, 35);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Eggplant", Enums.FoodGroup.VEGETABLE, 7);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Green Beans", Enums.FoodGroup.VEGETABLE, 6);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Asparagus", Enums.FoodGroup.VEGETABLE, 4);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Peas", Enums.FoodGroup.VEGETABLE, 7);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Corn", Enums.FoodGroup.VEGETABLE, 6);
        foodDictionary.put(currItem.getID(), currItem);

        // Grains(Quinoa is ID 56 for reference)
        currItem = new FoodType("Quinoa", Enums.FoodGroup.GRAIN, 1100);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Oats", Enums.FoodGroup.GRAIN, 700);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Barley", Enums.FoodGroup.GRAIN, 365);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Bread", Enums.FoodGroup.GRAIN, 7);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Couscous", Enums.FoodGroup.GRAIN, 700);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Tortilla", Enums.FoodGroup.GRAIN, 35);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Hard Taco Shell", Enums.FoodGroup.GRAIN, 150);
        foodDictionary.put(currItem.getID(), currItem);

        // Proteins(Beed is ID 63 for reference)
        currItem = new FoodType("Beef", Enums.FoodGroup.PROTEIN, 10);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Pork", Enums.FoodGroup.PROTEIN, 5);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Fish", Enums.FoodGroup.PROTEIN, 4);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Tofu", Enums.FoodGroup.PROTEIN, 90);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Beans", Enums.FoodGroup.PROTEIN, 2000);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Lentils", Enums.FoodGroup.PROTEIN, 900);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Chickpeas", Enums.FoodGroup.PROTEIN, 1500);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Nuts", Enums.FoodGroup.PROTEIN, 270);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Seeds", Enums.FoodGroup.PROTEIN, 365);
        foodDictionary.put(currItem.getID(), currItem);

        // Dairies(Milk is ID 72 for reference)
        currItem = new FoodType("Milk", Enums.FoodGroup.DAIRY, 10);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Cheese", Enums.FoodGroup.DAIRY, 7);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Yogurt", Enums.FoodGroup.DAIRY, 14);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Butter", Enums.FoodGroup.DAIRY, 120);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Cream", Enums.FoodGroup.DAIRY, 180);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Cream Cheese", Enums.FoodGroup.DAIRY, 21);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Sour Cream", Enums.FoodGroup.DAIRY, 21);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Greek Yogurt", Enums.FoodGroup.DAIRY, 11);
        foodDictionary.put(currItem.getID(), currItem);

        // Condiments(Ketchup is ID 80 for reference)
        currItem = new FoodType("Ketchup", Enums.FoodGroup.CONDIMENT, 300);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Mustard", Enums.FoodGroup.CONDIMENT, 210);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Mayonnaise", Enums.FoodGroup.CONDIMENT, 120);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Soy Sauce", Enums.FoodGroup.CONDIMENT, 1000);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Worcestershire Sauce", Enums.FoodGroup.CONDIMENT, 1000);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Hot Sauce", Enums.FoodGroup.CONDIMENT, 700);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("BBQ Sauce", Enums.FoodGroup.CONDIMENT, 300);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Buffalo Sauce", Enums.FoodGroup.CONDIMENT, 600);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Remoulade", Enums.FoodGroup.CONDIMENT, 180);
        foodDictionary.put(currItem.getID(), currItem);

        // Beverages(Water is ID 89 for reference)
        currItem = new FoodType("Water", Enums.FoodGroup.BEVERAGE, 10000);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Coffee", Enums.FoodGroup.BEVERAGE, 1);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Tea", Enums.FoodGroup.BEVERAGE, 2);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Orange Juice", Enums.FoodGroup.BEVERAGE, 10);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Apple Juice", Enums.FoodGroup.BEVERAGE, 700);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Soda", Enums.FoodGroup.BEVERAGE, 400);
        foodDictionary.put(currItem.getID(), currItem);

        // Herbs(Basil is ID 95 for reference)
        currItem = new FoodType("Basil", Enums.FoodGroup.HERB, 700);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Cilantro", Enums.FoodGroup.HERB, 14);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Rosemary", Enums.FoodGroup.HERB, 14);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Thyme", Enums.FoodGroup.HERB, 300);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Mint", Enums.FoodGroup.HERB, 365);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Oregano", Enums.FoodGroup.HERB, 800);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Dill", Enums.FoodGroup.HERB, 14);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Chives", Enums.FoodGroup.HERB, 10);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Coriander", Enums.FoodGroup.HERB, 1300);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Sage", Enums.FoodGroup.HERB, 750);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Bay Leaf", Enums.FoodGroup.HERB, 14);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Tarragon", Enums.FoodGroup.HERB, 14);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Marjoram", Enums.FoodGroup.HERB, 800);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Lemongrass", Enums.FoodGroup.HERB, 7);
        foodDictionary.put(currItem.getID(), currItem);

        // Spices(Cumin is ID 109 for reference)
        currItem = new FoodType("Cumin", Enums.FoodGroup.SPICE, 500);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Paprika", Enums.FoodGroup.SPICE, 500);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Cinnamon", Enums.FoodGroup.SPICE, 1000);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Nutmeg", Enums.FoodGroup.SPICE, 800);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Ginger", Enums.FoodGroup.SPICE, 30);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Cloves", Enums.FoodGroup.SPICE, 1000);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Cardamom", Enums.FoodGroup.SPICE, 1200);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Turmeric", Enums.FoodGroup.SPICE, 800);
        foodDictionary.put(currItem.getID(), currItem);

        // Extra Food Items(Honey is ID 117 for reference)
        currItem = new FoodType("Honey", Enums.FoodGroup.CONDIMENT, 300);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Balsamic Vinegar", Enums.FoodGroup.CONDIMENT,80);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Coconut Milk", Enums.FoodGroup.BEVERAGE, 230);
        foodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Curry Powder", Enums.FoodGroup.SPICE, 325);
        foodDictionary.put(currItem.getID(), currItem);
    }

    public HashMap<Integer, FoodType> getFoodDictionary() {
        return foodDictionary;
    }

    public void prePopulatePantry() {
        this.pantryList.add(new PantryItem(new FoodBatch(foodDictionary.get(0), 5, LocalDate.now().plusDays(5)), false));
        this.pantryList.add(new PantryItem(new FoodBatch(foodDictionary.get(1), 3, LocalDate.now().plusDays(8)), false));
        this.pantryList.add(new PantryItem(new FoodBatch(foodDictionary.get(2), 3, LocalDate.now().plusDays(2)), false));
        this.pantryList.add(new PantryItem(new FoodBatch(foodDictionary.get(3), 1, LocalDate.now().plusDays(24)), true));

        this.pantryList.add(new PantryItem(new FoodBatch(foodDictionary.get(7), 2, LocalDate.now().plusDays(foodDictionary.get(7).getExpirationPeriod())), false));
        this.pantryList.add(new PantryItem(new FoodBatch(foodDictionary.get(50), 6, LocalDate.now().plusDays(foodDictionary.get(50).getExpirationPeriod())), false));
        this.pantryList.add(new PantryItem(new FoodBatch(foodDictionary.get(21), 3, LocalDate.now().plusDays(foodDictionary.get(21).getExpirationPeriod())), false));
        this.pantryList.add(new PantryItem(new FoodBatch(foodDictionary.get(70), 2, LocalDate.now().plusDays(foodDictionary.get(70).getExpirationPeriod())), false));
        this.pantryList.add(new PantryItem(new FoodBatch(foodDictionary.get(55), 1, LocalDate.now().plusDays(foodDictionary.get(55).getExpirationPeriod())), true));
        this.pantryList.add(new PantryItem(new FoodBatch(foodDictionary.get(25), 4, LocalDate.now().plusDays(foodDictionary.get(25).getExpirationPeriod())), false));
        this.pantryList.add(new PantryItem(new FoodBatch(foodDictionary.get(10), 3, LocalDate.now().plusDays(foodDictionary.get(10).getExpirationPeriod())), false));
        this.pantryList.add(new PantryItem(new FoodBatch(foodDictionary.get(28), 7, LocalDate.now().plusDays(foodDictionary.get(28).getExpirationPeriod())), false));
        printAllFoodTypes();
    }

    public ArrayList<PantryItem> getPantryList() {
        return pantryList;
    }

    // Debugging - List all the IDs
    public void printAllFoodTypes() {
        for (Map.Entry<Integer, FoodType> entry : foodDictionary.entrySet()) {
            System.out.println("DEBUGGING: PRINTING FOOD DATABASE");
            System.out.println("ID: " + entry.getKey() + ", Name: " + entry.getValue().getItemName());
        }
    }

}