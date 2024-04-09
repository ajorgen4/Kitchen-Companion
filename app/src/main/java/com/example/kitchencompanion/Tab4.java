package com.example.kitchencompanion;

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

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Tab4 extends Fragment {
    private TextView dietaryTextView;
    private TextView allergyTextView;
    private boolean[] selectedDietaryPreferences;
    private boolean[] selectedAllergies;
    private String[] dietaryOptions = {"Vegan", "Vegetarian", "Pescatarian", "Paleo", "Keto", "Low-carb", "Low-fat", "Mediterranean", "Flexitarian"};
    private String[] allergyOptions = {"Gluten", "Dairy", "Nuts", "Shellfish", "Soy", "Eggs", "Fish", "Wheat"};
    private EditText houseNameEditText; // Add EditText for house name

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
        View rootView = inflater.inflate(R.layout.fragment_tab4, container, false);

        selectedDietaryPreferences = new boolean[dietaryOptions.length];
        selectedAllergies = new boolean[allergyOptions.length];

        // Dietary Preferences TextView
        dietaryTextView = rootView.findViewById(R.id.dietaryTextView);
        dietaryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDietaryPreferencesDialog();
            }
        });

        // Allergy TextView
        allergyTextView = rootView.findViewById(R.id.allergyTextView);
        allergyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllergiesDialog();
            }
        });
        houseNameEditText = rootView.findViewById(R.id.houseNameEditText); // Initialize EditText
        Button updateHouseNameButton = rootView.findViewById(R.id.updateHouseNameButton);
        updateHouseNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHouseName();
            }
        });

        return rootView;
    }

    private void showDietaryPreferencesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Dietary Preferences");
        builder.setMultiChoiceItems(dietaryOptions, selectedDietaryPreferences, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                selectedDietaryPreferences[which] = isChecked;
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
        List<String> selectedPreferences = new ArrayList<>();
        for (int i = 0; i < selectedDietaryPreferences.length; i++) {
            if (selectedDietaryPreferences[i]) {
                selectedPreferences.add(dietaryOptions[i]);
            }
        }
        if (!selectedPreferences.isEmpty()) {
            for (String preference : selectedPreferences) {
                stringBuilder.append(preference).append(", ");
            }
            // Remove the trailing comma and space
            String preferencesText = stringBuilder.substring(0, stringBuilder.length() - 2);
            dietaryTextView.setText(preferencesText);
        } else {
            dietaryTextView.setText("None");
        }
    }

    private void showAllergiesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Allergies");
        builder.setMultiChoiceItems(allergyOptions, selectedAllergies, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                selectedAllergies[which] = isChecked;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateAllergiesTextView();
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateAllergiesTextView() {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> selectedAllergyList = new ArrayList<>();
        for (int i = 0; i < selectedAllergies.length; i++) {
            if (selectedAllergies[i]) {
                selectedAllergyList.add(allergyOptions[i]);
            }
        }
        if (!selectedAllergyList.isEmpty()) {
            for (String allergy : selectedAllergyList) {
                stringBuilder.append(allergy).append(", ");
            }
            // Remove the trailing comma and space
            String allergiesText = stringBuilder.substring(0, stringBuilder.length() - 2);
            allergyTextView.setText(allergiesText);
        } else {
            allergyTextView.setText("None");
        }
    }
}


