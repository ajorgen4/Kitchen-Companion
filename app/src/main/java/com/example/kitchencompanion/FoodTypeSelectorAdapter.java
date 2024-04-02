package com.example.kitchencompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

public class FoodTypeSelectorAdapter extends ArrayAdapter<FoodType> implements Filterable {
    private Context context;
    private List<FoodType> foodTypes;

    public FoodTypeSelectorAdapter(Context context, List<FoodType> foodTypes) {
        super(context, 0, foodTypes);
        this.context = context;
        this.foodTypes = foodTypes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        FoodType currentFoodType = foodTypes.get(position);

        TextView nameTextView = listItem.findViewById(android.R.id.text1);
        nameTextView.setText(currentFoodType.getItemName());

        return listItem;
    }
}