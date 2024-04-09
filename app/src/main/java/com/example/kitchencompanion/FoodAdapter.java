package com.example.kitchencompanion;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Maybe change this to extend ArrayAdapter<Type> like in Phase 1.2?
public class FoodAdapter extends BaseAdapter implements Filterable {
    private class PantryItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<PantryItem> filteredList = new ArrayList<>();

            for (PantryItem item : foodList) {
                if (filterFields.matchName(item.getItemName())
                && filterFields.matchLow(item.getLow())
                && filterFields.matchPrivate(item.getIsPrivate())
                && filterFields.matchExpirationMax((int) LocalDate.now().until(item.nextExpiration(), ChronoUnit.DAYS))
                && filterFields.matchFoodGroup(item.getType().getFoodGroup())) { // constraints here
                    filteredList.add(item);
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (List<PantryItem>) results.values;
            notifyDataSetChanged();
        }
    }

    private List<PantryItem> foodList;
    private List<PantryItem> filteredData;
    private Context context;
    private Tab2 tab2;
    private PantryItemFilter pantryItemFilter = new PantryItemFilter();
    PantryItemFilterFields filterFields;


    public FoodAdapter(Context context, List<PantryItem> foodList, Tab2 tab2, PantryItemFilterFields filterFields) {
        this.foodList = foodList;
        this.filteredData = foodList;
        this.context = context;
        this.tab2 = tab2;
        this.filterFields = filterFields;
    }

    // Accessors
    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int itemPosition = position;
        FoodItemView foodItemView;
        PantryItem item = filteredData.get(itemPosition);

        if (convertView == null) {
            foodItemView = new FoodItemView(context);
        } else {
            foodItemView = (FoodItemView) convertView;
        }

        foodItemView.setFoodName(item.getItemName());
        foodItemView.setFoodCount(item.totalCount());

        ImageView deleteButton = foodItemView.findViewById(R.id.closePantryItemButton);
        // If they click the delete button, remove the item from the list
        deleteButton.setOnClickListener(v -> {
            // TODO: needs to remove from filteredData and foodList. Don't know the correct foodList index.
            foodList.remove(itemPosition);
            notifyDataSetChanged();
        });

        ImageView plusButton = foodItemView.findViewById(R.id.pantryQuantityPlusButton);
        plusButton.setOnClickListener(v -> {
            tab2.addItemsPrivate(item.getType(), 1, item.getIsPrivate());
        });

        ImageView minusButton = foodItemView.findViewById(R.id.pantryQuantityMinusButton);
        minusButton.setOnClickListener(v -> {
            tab2.removeItemsInternal(item.getType(), 1, item.getIsPrivate());
            if (item.totalCount() == 0) {
                // TODO: needs to remove from filteredData and foodList. Don't know the correct foodList index.
                foodList.remove(itemPosition);
                notifyDataSetChanged();
            }
        });

        ImageView privateIcon = foodItemView.findViewById(R.id.privateIcon);
        privateIcon.setVisibility(item.getIsPrivate() ? convertView.VISIBLE : convertView.INVISIBLE);
        /*
        if (!item.getIsPrivate()) { // Hide if not private
            privateIcon.setVisibility(convertView.INVISIBLE);
        }*/

        ImageView lowIcon = foodItemView.findViewById(R.id.lowIcon);
        CheckBox lowBox = foodItemView.findViewById(R.id.pantryItemLowCheckBox);
        lowBox.setChecked(item.getLow());
        lowIcon.setVisibility(item.getLow() ? convertView.VISIBLE : convertView.INVISIBLE);
        lowBox.setOnClickListener(v -> {
            if (lowBox.isChecked()) {
                lowIcon.setVisibility(v.VISIBLE);
                item.setLow(true);
            } else {
                lowIcon.setVisibility(v.INVISIBLE);
                item.setLow(false);
            }
        });

        TextView expirationDisplay = foodItemView.findViewById(R.id.expirationDisplay);
        LocalDate expirationDate = filteredData.get(itemPosition).nextExpiration();
        expirationDisplay.setText("Expiration Date: " + expirationDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        expirationDisplay.setTextColor(ContextCompat.getColor(context, (ChronoUnit.DAYS.between(LocalDate.now(), expirationDate) > 7) ? R.color.green : (((ChronoUnit.DAYS.between(LocalDate.now(), expirationDate) > 3) ? R.color.light_orange : R.color.red))));

        ImageView expireIcon = foodItemView.findViewById(R.id.expireIcon);
        expireIcon.setVisibility((ChronoUnit.DAYS.between(LocalDate.now(), expirationDate) > 7) ? convertView.INVISIBLE : convertView.VISIBLE);

        return foodItemView;
    }

    @Override
    public Filter getFilter() {
        return pantryItemFilter;
    }
}