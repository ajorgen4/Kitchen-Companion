package com.example.kitchencompanion;

import java.util.Set;

// Like an interface. Represents a type of food

public class FoodType {
    private static int totalFoods = 0;
    private int itemId;
    private String itemName;
    private Enums.FoodGroup foodGroup;
    private int expirationPeriod;

    public FoodType(String itemName, Enums.FoodGroup foodGroup, int expirationPeriod) {
        this.itemId = totalFoods++;
        this.itemName = itemName;
        this.foodGroup = foodGroup;
        this.expirationPeriod = expirationPeriod;
    }

    public int getID() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public Enums.FoodGroup getCategory() {
        return foodGroup;
    }

    public int getExpirationPeriod() {
        return expirationPeriod;
    }
}