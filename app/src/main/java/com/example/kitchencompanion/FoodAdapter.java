package com.example.kitchencompanion;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

// Maybe change this to extend ArrayAdapter<Type> like in Phase 1.2?
public class FoodAdapter extends BaseAdapter {

    private List<String> foodList;
    private Context context;

    public FoodAdapter(Context context, List<String> foodList) {
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
        FoodItemView foodItemView;

        if (convertView == null) {
            foodItemView = new FoodItemView(context);
        } else {
            foodItemView = (FoodItemView) convertView;
        }

        foodItemView.setFoodName(foodList.get(position));

        return foodItemView;
    }
}