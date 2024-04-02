package com.example.kitchencompanion;

public class Enums {

    // Overarching Classes foods can be in
    public enum FoodGroup {
        FRUIT, VEGETABLE, GRAIN, PROTEIN, DAIRY, MISC
    }

    // Attributes per food/Recipe item
    public enum DietaryAttribute {
        VEGAN, ORGANIC, CONTAINS_NUTS
    }
}