package com.example.kitchencompanion;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FoodItemView extends RelativeLayout implements View.OnClickListener {

    private TextView foodNameTextView;
    private LinearLayout expandView;

    // Constructors
    public FoodItemView(Context context) {
        super(context);
        initializeViewsAndSetListener(context);
    }

    public FoodItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViewsAndSetListener(context);
    }

    public FoodItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViewsAndSetListener(context);
    }

    private void initializeViewsAndSetListener(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.pantry_list_item, this, true);

        // FoodNameTextView is the default view
        foodNameTextView = findViewById(R.id.foodName);
        // expandView has the extra details
        expandView = findViewById(R.id.expandView);
        // It should be collapsed by default
        expandView.setVisibility(GONE);

        setOnClickListener(this);
    }

    public void setFoodName(String foodName) {
        foodNameTextView.setText(foodName);
    }

    @Override
    public void onClick(View v) {
        // Consider trying to make an animation
        if (expandView.getVisibility() == VISIBLE) { // Collapse if tapped when open
            expandView.setVisibility(GONE);
        } else { // Expand if tapped when closed
            expandView.setVisibility(VISIBLE);
        }
    }
}