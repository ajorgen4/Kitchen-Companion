package com.example.kitchencompanion;

import com.example.kitchencompanion.Enums;
import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;
import android.widget.ArrayAdapter;
import android.text.TextUtils;
import android.widget.SearchView;


import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class Tab4 extends Fragment {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String DIETARY_PREFS_KEY = "DietaryPrefs";
    private SharedPreferences sharedPreferences;
    private TextView dietaryTextView;
    private Set<String> selectedDietaryPreferences;

    private View rootView;

    //private TextView allergyTextView;
    //private List<FoodType> foodTypes;

    private static final String SELECTED_ALLERGIES_KEY = "SelectedAllergies";

    private String[] dietaryOptions = {"Vegan", "Vegetarian", "Pescatarian", "Paleo", "Keto", "Low-carb", "Low-fat", "Mediterranean", "Flexitarian"};

    private EditText houseNameEditText; // Add EditText for house name

    private ArrayList<String> selectedAllergies = new ArrayList<>();
    private ArrayList<String> filteredAllergyList = new ArrayList<>();

    // Interface for communicating house name updates to the activity
    public interface HouseNameUpdateListener {
        void onUpdateHouseName(String newName);
    }

    // Reference to the listener
    private HouseNameUpdateListener houseNameUpdateListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Check if the context (activity) implements the interface
        if (context instanceof HouseNameUpdateListener) {
            houseNameUpdateListener = (HouseNameUpdateListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HouseNameUpdateListener");
        }
    }

    // Method to update the house name
    private void updateHouseName() {
        // Get the new house name from EditText
        String newHouseName = houseNameEditText.getText().toString();
        // Pass the new house name to the activity
        houseNameUpdateListener.onUpdateHouseName(newHouseName);
    }

    // Reference to the set of all FoodType objects in the app
    // Maps FoodType.getID() to FoodType
    HashMap<Integer, FoodType> foodDictionary;

    //for house name
    private String currentHouseName = "Default House"; // Add a default value

    public Tab4(HashMap<Integer, FoodType> foodDictionary) {
        this.foodDictionary = foodDictionary;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tab4, container, false);

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        SearchView searchView = rootView.findViewById(R.id.searchView);
        ListView listView = rootView.findViewById(R.id.listView);




        // Load selected allergies from SharedPreferences
        Set<String> savedSelectedAllergies = sharedPreferences.getStringSet(SELECTED_ALLERGIES_KEY, new HashSet<>());
        selectedAllergies.clear();
        selectedAllergies.addAll(savedSelectedAllergies);
        updateSelectedAllergiesTextView();

        // Create an ArrayList of common allergy items, will try to make it filter fooditems instead of creating seperate entities.
        ArrayList<String> allergyList = new ArrayList<>();
        allergyList.add("Milk");
        allergyList.add("Eggs");
        allergyList.add("Peanuts");
        allergyList.add("Tree Nuts");
        allergyList.add("Soy");
        allergyList.add("Wheat");
        allergyList.add("Fish");
        allergyList.add("Shellfish");
        allergyList.add("shells test");

        filteredAllergyList.addAll(allergyList);

        // Create an ArrayAdapter to bind the allergyList to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, filteredAllergyList);
        listView.setAdapter(adapter);




        // Set up the search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredAllergyList.clear();
                if (newText.isEmpty()) {
                    filteredAllergyList.addAll(allergyList);
                } else {
                    String filterPattern = newText.toLowerCase().trim();
                    for (String allergy : allergyList) {
                        if (allergy.toLowerCase().contains(filterPattern)) {
                            filteredAllergyList.add(allergy);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });


        // Dietary Preferences TextView
        dietaryTextView = rootView.findViewById(R.id.dietaryTextView);

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        Button saveButton = rootView.findViewById(R.id.saveButton);

        selectedDietaryPreferences = sharedPreferences.getStringSet(DIETARY_PREFS_KEY, new HashSet<>());
        updateDietaryPreferencesTextView();



        dietaryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDietaryPreferencesDialog();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedAllergy = filteredAllergyList.get(position);
                if (selectedAllergies.contains(selectedAllergy)) {
                    selectedAllergies.remove(selectedAllergy);
                } else {
                    selectedAllergies.add(selectedAllergy);
                }
                updateSelectedAllergiesTextView();
                saveSelectedAllergies(); // Save selected allergies to SharedPreferences
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDietaryPreferences();
            }
        });

        houseNameEditText = rootView.findViewById(R.id.houseNameEditText); // Initialize EditText
        Button updateHouseNameButton = rootView.findViewById(R.id.updateHouseNameButton);
        updateHouseNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHouseName();
                houseNameEditText.clearFocus();
            }
        });

        return rootView;
    }

    private void showDietaryPreferencesDialog() {
        final boolean[] checkedItems = new boolean[dietaryOptions.length];
        for (int i = 0; i < dietaryOptions.length; i++) {
            checkedItems[i] = selectedDietaryPreferences.contains(dietaryOptions[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Dietary Preferences");
        builder.setMultiChoiceItems(dietaryOptions, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    selectedDietaryPreferences.add(dietaryOptions[which]);
                } else {
                    selectedDietaryPreferences.remove(dietaryOptions[which]);
                }
            }

        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateDietaryPreferencesTextView();

            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateDietaryPreferencesTextView() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!selectedDietaryPreferences.isEmpty()) {
            for (String preference : selectedDietaryPreferences) {
                stringBuilder.append(preference).append(", ");
            }
            String preferencesText = stringBuilder.substring(0, stringBuilder.length() - 2);
            dietaryTextView.setText(preferencesText);
        } else {
            dietaryTextView.setText("None");
        }
    }

    private void saveDietaryPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(DIETARY_PREFS_KEY, selectedDietaryPreferences);
        editor.apply();
    }

    private void updateSelectedAllergiesTextView() {
        TextView selectedAllergiesTextView = rootView.findViewById(R.id.selectedAllergiesTextView);
        if (selectedAllergies.isEmpty()) {
            selectedAllergiesTextView.setText("None");
        } else {
            String allergiesText = TextUtils.join(", ", selectedAllergies);
            selectedAllergiesTextView.setText(allergiesText);
        }
    }
    //used for persisting allergy changes across tabs
    private void saveSelectedAllergies() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(SELECTED_ALLERGIES_KEY, new HashSet<>(selectedAllergies));
        editor.apply();
    }

    private void filterItems(String query, ListView listView) {
        // Implement the logic to filter the items in the ListView based on the search query
        //will add later if needed

    }
}



