package com.example.kitchencompanion;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

// Maybe change this to extend ArrayAdapter<Type> like in Phase 1.2?
public class FoodAdapter extends BaseAdapter {

    private List<PantryItem> foodList;
    private Context context;

    public FoodAdapter(Context context, List<PantryItem> foodList) {
        this.foodList = foodList;
        this.context = context;
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

        if (convertView == null) {
            foodItemView = new FoodItemView(context);
        } else {
            foodItemView = (FoodItemView) convertView;
        }

        foodItemView.setFoodName(foodList.get(position).getItemName());

        ImageView deleteButton = foodItemView.findViewById(R.id.closePantryItemButton);
        // If they click the delete button, remove the item from the list
        deleteButton.setOnClickListener(v -> {
            foodList.remove(itemPosition);
            notifyDataSetChanged();
        });

        return foodItemView;
    }
}