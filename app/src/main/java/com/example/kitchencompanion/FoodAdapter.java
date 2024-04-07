package com.example.kitchencompanion;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Maybe change this to extend ArrayAdapter<Type> like in Phase 1.2?
public class FoodAdapter extends BaseAdapter {

    private List<PantryItem> foodList;
    private Context context;
    private Tab2 tab2;

    public FoodAdapter(Context context, List<PantryItem> foodList, Tab2 tab2) {
        this.foodList = foodList;
        this.context = context;
        this.tab2 = tab2;
    }

    // Accessors
    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int itemPosition = position;
        FoodItemView foodItemView;
        PantryItem item = foodList.get(itemPosition);

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
        LocalDate expirationDate = foodList.get(itemPosition).nextExpiration();
        expirationDisplay.setText("Expiration Date: " + expirationDate.format(DateTimeFormatter.ofPattern("MM/dd")));
        expirationDisplay.setTextColor(ContextCompat.getColor(context, (ChronoUnit.DAYS.between(LocalDate.now(), expirationDate) > 7) ? R.color.green : (((ChronoUnit.DAYS.between(LocalDate.now(), expirationDate) > 3) ? R.color.light_orange : R.color.red))));

        ImageView expireIcon = foodItemView.findViewById(R.id.expireIcon);
        expireIcon.setVisibility((ChronoUnit.DAYS.between(LocalDate.now(), expirationDate) > 7) ? convertView.INVISIBLE : convertView.VISIBLE);

        return foodItemView;
    }
}