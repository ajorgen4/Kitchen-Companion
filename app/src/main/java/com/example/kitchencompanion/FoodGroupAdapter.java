package com.example.kitchencompanion;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FoodGroupAdapter extends ArrayAdapter<Enums.FoodGroup> {
    private List<Enums.FoodGroup> foodGroups;
    private List<Enums.FoodGroup> selectedFoodGroups = new ArrayList<>();

    public FoodGroupAdapter(Context context, List<Enums.FoodGroup> foodGroups, List<Enums.FoodGroup> initialSelection) {
        super(context, 0, foodGroups);
        this.foodGroups = foodGroups;
        this.selectedFoodGroups.addAll(initialSelection);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_group_item, parent, false);
        }

        CheckBox checkBox = convertView.findViewById(R.id.food_group_checkbox);
        TextView textView = convertView.findViewById(R.id.food_group_text);

        Enums.FoodGroup foodGroup = getItem(position);
        textView.setText(foodGroup.toString());
        checkBox.setChecked(selectedFoodGroups.contains(foodGroup));

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedFoodGroups.add(foodGroup);
            } else {
                selectedFoodGroups.remove(foodGroup);
            }
        });

        return convertView;
    }

    public void clearSelection() {
        selectedFoodGroups.clear();
        notifyDataSetChanged();
    }

    public List<Enums.FoodGroup> getSelectedFoodGroups() {
        return selectedFoodGroups;
    }
}