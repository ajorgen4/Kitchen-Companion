package com.example.kitchencompanion;

// Like an interface. Represents a type of food

public class FoodType implements Comparable<FoodType> {
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

    public Enums.FoodGroup getFoodGroup() {
        return foodGroup;
    }

    public int getExpirationPeriod() {
        return expirationPeriod;
    }

    @Override
    public int compareTo(FoodType other) {
        // compare item name primarily
        int nameComparison = this.itemName.compareTo(other.itemName);
        if (nameComparison != 0) {
            return nameComparison;
        }

        // If names match, compare food groups
        return this.foodGroup.compareTo(other.foodGroup);
    }
}