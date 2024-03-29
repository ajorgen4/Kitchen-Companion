/*package com.example.kitchencompanion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Tab4 extends Fragment {
    private TextView dietaryTextView;
    private TextView allergyTextView;
    private List<String> selectedDietaryPreferences = new ArrayList<>();
    private List<String> selectedAllergies = new ArrayList<>();
    private String[] dietaryOptions = {"None", "Vegan", "Vegetarian", "Pescatarian", "Paleo", "Keto", "Low-carb", "Low-fat", "Mediterranean", "Flexitarian"};
    private String[] allergyOptions = {"Gluten", "Dairy", "Nuts", "Shellfish", "Soy", "Eggs", "Fish", "Wheat"};

    public Tab4() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab4, container, false);

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

        return rootView;
    }

    private void showDietaryPreferencesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Dietary Preferences");
        builder.setMultiChoiceItems(dietaryOptions, null, new DialogInterface.OnMultiChoiceClickListener() {
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
        if (!selectedDietaryPreferences.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String preference : selectedDietaryPreferences) {
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
        builder.setMultiChoiceItems(allergyOptions, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    selectedAllergies.add(allergyOptions[which]);
                } else {
                    selectedAllergies.remove(allergyOptions[which]);
                }
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
        if (!selectedAllergies.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String allergy : selectedAllergies) {
                stringBuilder.append(allergy).append(", ");
            }
            // Remove the trailing comma and space
            String allergiesText = stringBuilder.substring(0, stringBuilder.length() - 2);
            allergyTextView.setText(allergiesText);
        } else {
            allergyTextView.setText("None");
        }
    }
}*/

package com.example.kitchencompanion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Tab4 extends Fragment {
    private TextView dietaryTextView;
    private TextView allergyTextView;
    private boolean[] selectedDietaryPreferences;
    private boolean[] selectedAllergies;
    private String[] dietaryOptions = {"Vegan", "Vegetarian", "Pescatarian", "Paleo", "Keto", "Low-carb", "Low-fat", "Mediterranean", "Flexitarian"};
    private String[] allergyOptions = {"Gluten", "Dairy", "Nuts", "Shellfish", "Soy", "Eggs", "Fish", "Wheat"};

    public Tab4() {
        // Required empty public constructor
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



