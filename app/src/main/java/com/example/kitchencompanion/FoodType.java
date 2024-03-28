package com.example.kitchencompanion;

import java.util.Set;

// Like an interface. Represents a type of food

public class FoodType {
    private static int totalFoods = 0;
    private int itemId;
    private String itemName;
    private Enums.Category category;
    private Set<String> allergens; // Not used for now, I combined with dietary attributes
    private Set<Enums.DietaryAttribute> dietaryAttributes;
    private int calories;

    public FoodType(String itemName, Enums.Category category, Set<String> allergens,
                    Set<Enums.DietaryAttribute> dietaryAttributes, int calories) {
        this.itemId = totalFoods++;
        this.itemName = itemName;
        this.category = category;
        this.allergens = allergens;
        this.dietaryAttributes = dietaryAttributes;
        this.calories = calories;
    }

    public String getItemName() {
        return itemName;
    }

    public Enums.Category getCategory() {
        return category;
    }

    public Set<String> getAllergens() {
        return allergens;
    }

    public Set<Enums.DietaryAttribute> getDietaryAttributes() {
        return dietaryAttributes;
    }

    public int getCalories() {
        return calories;
    }
}