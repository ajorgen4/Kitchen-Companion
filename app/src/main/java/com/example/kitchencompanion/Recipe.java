package com.example.kitchencompanion;
import java.util.List;

// Class definition for Recipies
public class Recipe {
    private String name;
    private String description; // Recipie Text
    private List<Integer> ingredientIds; // Item IDs
    private int recipeId; // Recipie ID Unique
    private int servings; // Amount served
    private boolean isFavorited; // Yes or No favorited
    private String cookTime; // ex. 45 mins"
    private int calories; // ex. 650
    private String difficulty; // ex. "Medium"

    public Recipe(int recipeId, String name, String description, List<Integer> ingredientIds, int servings, boolean isFavorited, String cookTime, int calories, String difficulty) {
        this.recipeId = recipeId;
        this.name = name;
        this.description = description;
        this.ingredientIds = ingredientIds;
        this.servings = servings;
        this.isFavorited = isFavorited;
        this.cookTime = cookTime;
        this.calories = calories;
        this.difficulty = difficulty;
    }

    public boolean toggleFavorite() {
        this.isFavorited = !this.isFavorited;
        return isFavorited;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public String getCookTime() {
        return cookTime;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getDescription() {
        return difficulty;
    }

    public Boolean isFavorited() {
        return isFavorited;
    }
}
