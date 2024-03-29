package com.example.kitchencompanion;

import java.util.Set;

// Like an interface. Represents a type of food

public class FoodType {
    private static int totalFoods = 0;
    private int itemId;
    private String itemName;
    private Enums.Category category;
    private Set<Enums.DietaryAttribute> dietaryAttributes;
    private int expirationPeriod;

    public FoodType(String itemName, Enums.Category category, Set<Enums.DietaryAttribute> dietaryAttributes, int expirationPeriod) {
        this.itemId = totalFoods++;
        this.itemName = itemName;
        this.category = category;
        this.dietaryAttributes = dietaryAttributes;
        this.expirationPeriod = expirationPeriod;
    }

    public String getItemName() {
        return itemName;
    }

    public Enums.Category getCategory() {
        return category;
    }

    public Set<Enums.DietaryAttribute> getDietaryAttributes() {
        return dietaryAttributes;
    }

    public int getExpirationPeriod() {
        return expirationPeriod;
    }
}