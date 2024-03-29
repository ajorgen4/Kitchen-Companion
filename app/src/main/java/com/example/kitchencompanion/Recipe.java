package com.example.kitchencompanion;

import java.util.List;

// Represents a Recipe with relevant details
public class Recipe {
    private int recipeId;
    private String name;
    private String description;
    private List<Integer> ingredientIds;
    private int servings;
    private boolean isFavorited;
    private String cookTime;
    private int calories;
    private String difficulty;

    private String imageFile;
    public Recipe(int recipeId, String name, String description, List<Integer> ingredientIds,
                  int servings, boolean isFavorited, String cookTime, int calories, String difficulty, String imageFile) {
        this.recipeId = recipeId;
        this.name = name;
        this.description = description;
        this.ingredientIds = ingredientIds;
        this.servings = servings;
        this.isFavorited = isFavorited;
        this.cookTime = cookTime;
        this.calories = calories;
        this.difficulty = difficulty;
        this.imageFile = imageFile;
    }

    // favorite
    public void toggleFavorite() {
        this.isFavorited = !this.isFavorited;
    }

    // GET AND SET METHODS
    public int getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Integer> getIngredientIds() {
        return ingredientIds;
    }

    public int getServings() {
        return servings;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public String getCookTime() {
        return cookTime;
    }

    public int getCalories() {
        return calories;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getImageFile() { return imageFile;}

    public void setImageFile(String name) {
        this.imageFile = name;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredientIds(List<Integer> ingredientIds) {
        this.ingredientIds = ingredientIds;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
