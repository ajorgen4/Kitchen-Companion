package com.example.kitchencompanion;

public class Enums {

    // Overarching Classes foods can be in
    public enum FoodGroup {
        FRUIT, VEGETABLE, GRAIN, PROTEIN, DAIRY, CONDIMENT, BEVERAGE, HERB, SPICE, MISC, LEFTOVERS, SUGARS
    }

    // Attributes per food/Recipe item
    public enum DietaryAttribute {
        VEGAN, GLUTEN_FREE, LOW_CALORIE, VEGETARIAN, LOW_SODIUM, LOW_FAT, LOW_CHOLESTEROL, LOW_SUGAR, LOW_CARB, ORGANIC
    }

    public enum CommonFoodAllergy {
        GLUTEN, DAIRY, NUTS, SHELLFISH, SOY, EGGS, FISH, WHEAT
    }
    public static final CommonFoodAllergy[] commonFoodAllergies = CommonFoodAllergy.values();
}