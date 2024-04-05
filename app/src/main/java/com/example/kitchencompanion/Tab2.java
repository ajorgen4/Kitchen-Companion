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
    private HashMap<Integer, FoodType> foodDictionary;
    // Used in FoodType selector
    private ArrayList<FoodType> foodSelectorList;
    private FoodTypeSelectorAdapter foodSelectorListAdapter;
    private Dialog foodTypeSelector;
    // THIS IS WHERE YOU ACCESS THE FOOD SELECTED BY THE FOODTYPE SELECTOR
    private FoodType selectedFood;



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
                        // TODO: FILTER BUG: it selects by i. If you filter down to 1 item, it just takes apple every time. Ask Claude.
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
                // Create new item
                /*
                DatePicker expirationDatePicker = dialogView.findViewById(R.id.pantryAddFoodExpirationDatePicker);
                FoodType selectedFood;
                EditText countEditText = dialogView.findViewById(R.id.pantryAddFoodCountInput);
                CheckBox privateStorage = dialogView.findViewById(R.id.pantryAddFoodPrivateCheckBox);
                 */
                // TODO: implement create button
                // TODO: verify they have entered all fields first
                int count = Integer.parseInt(countEditText.getText().toString());
                boolean privateStorageBool = privateStorage.isChecked();
                LocalDate expirationDate = LocalDate.of(
                        expirationDatePicker.getYear(),
                        expirationDatePicker.getMonth(),
                        expirationDatePicker.getDayOfMonth());
                addItemInternal(Tab2.this.selectedFood, count, privateStorageBool, expirationDate);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // FOR INTERNAL USE ONLY
    // Allows support for custom expiration dates and private storage
    public void addItemInternal(FoodType foodType, int count, boolean privateStorage, LocalDate expirationDate) {

    }

    // Shopping list uses this
    public void addItems(FoodType foodType, int count) {

    }

    // Recipes uses this
    public void removeItems(FoodType foodType, int count) {

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
        HashMap<Integer, FoodType> tempFoodDictionary = new HashMap<>();
        FoodType currItem;

        // Existing items
        currItem = new FoodType("Apple", Enums.FoodGroup.FRUIT, 7);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Banana", Enums.FoodGroup.FRUIT, 7);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Orange", Enums.FoodGroup.FRUIT, 7);
        tempFoodDictionary.put(currItem.getID(), currItem);

        // New Items for Oven Baked Risotto
        currItem = new FoodType("Onion", Enums.FoodGroup.VEGETABLE, 14);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Olive Oil", Enums.FoodGroup.CONDIMENT, 90);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Rice", Enums.FoodGroup.GRAIN, 180);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Vegetable Broth", Enums.FoodGroup.BEVERAGE, 30);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Parmesan", Enums.FoodGroup.DAIRY, 60);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Parsley", Enums.FoodGroup.HERB, 10);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Salt", Enums.FoodGroup.CONDIMENT, 180);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Pepper", Enums.FoodGroup.SPICE, 180);
        tempFoodDictionary.put(currItem.getID(), currItem);

        // New Items for Chicken Noodle Soup
        currItem = new FoodType("Chicken", Enums.FoodGroup.PROTEIN, 7);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Noodles", Enums.FoodGroup.GRAIN, 30);
        tempFoodDictionary.put(currItem.getID(), currItem);
        // Assuming carrots and celery are additional ingredients
        currItem = new FoodType("Carrots", Enums.FoodGroup.VEGETABLE, 30);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Celery", Enums.FoodGroup.VEGETABLE, 30);
        tempFoodDictionary.put(currItem.getID(), currItem);

        // New Items for Chicken Parmesan
        currItem = new FoodType("Bread Crumbs", Enums.FoodGroup.GRAIN, 90);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Eggs", Enums.FoodGroup.PROTEIN, 21);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Marinara Sauce", Enums.FoodGroup.CONDIMENT, 60);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("Mozzarella", Enums.FoodGroup.DAIRY, 21);
        tempFoodDictionary.put(currItem.getID(), currItem);
        currItem = new FoodType("TEST ITEM", Enums.FoodGroup.DAIRY, 21);
        tempFoodDictionary.put(currItem.getID(), currItem);
        return tempFoodDictionary;
    }

    public HashMap<Integer, FoodType> getFoodDictionary() {
        return foodDictionary;
    }

    // Debugging - List all the IDs
    public void printAllFoodTypes() {
        for (Map.Entry<Integer, FoodType> entry : foodDictionary.entrySet()) {
            System.out.println("ID: " + entry.getKey() + ", Name: " + entry.getValue().getItemName());
        }
    }
}