package com.example.kitchencompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class FoodTypeSelectorAdapter extends ArrayAdapter<FoodType> implements Filterable {
    private class FoodTypeFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<FoodType> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // No filter, return the original data
                filteredList.addAll(foodTypes);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (FoodType foodType : foodTypes) {
                    if (foodType.getItemName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(foodType);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (List<FoodType>) results.values;
            notifyDataSetChanged();
        }
    }
    private Context context;
    private List<FoodType> foodTypes;
    private List<FoodType> filteredData;
    private FoodTypeFilter foodTypeFilter = new FoodTypeFilter();

    public FoodTypeSelectorAdapter(Context context, List<FoodType> foodTypes) {
        super(context, 0, foodTypes);
        this.context = context;
        this.foodTypes = foodTypes;
        this.filteredData = foodTypes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        FoodType currentFoodType;
        List<FoodType> dataToUse = filteredData != null ? filteredData : foodTypes;
        currentFoodType = dataToUse.get(position);

        TextView nameTextView = listItem.findViewById(android.R.id.text1);
        nameTextView.setText(currentFoodType.getItemName());
        nameTextView.setTextSize(20);

        return listItem;
    }

    @Override
    public Filter getFilter() {
        return foodTypeFilter;
    }

    @Override
    public int getCount() {
        return filteredData != null ? filteredData.size() : foodTypes.size();
    }

    @Override
    public FoodType getItem(int position) {
        // Return the item from the filteredData list if it's not null
        if (filteredData != null) {
            return filteredData.get(position);
        } else {
            // If filteredData is null, return the item from the original foodTypes list
            return foodTypes.get(position);
        }
    }
}