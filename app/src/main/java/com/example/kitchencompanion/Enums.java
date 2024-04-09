package com.example.kitchencompanion;

public class Enums {

    // Overarching Classes foods can be in
    public enum FoodGroup {
        FRUIT, VEGETABLE, GRAIN, PROTEIN, DAIRY, CONDIMENT, BEVERAGE, HERB, SPICE, MISC, TEST
    }

    // Attributes per food/Recipe item
    public enum DietaryAttribute {
        VEGAN, ORGANIC, CONTAINS_NUTS, TEST
    }

    public enum CommonFoodAllergy {
        GLUTEN, DAIRY, NUTS, SHELLFISH, SOY, EGGS, FISH, WHEAT
    }
    public static final CommonFoodAllergy[] commonFoodAllergies = CommonFoodAllergy.values();
}