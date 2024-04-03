package com.example.kitchencompanion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Represents a Recipe with relevant details
public class Recipe {
    private int recipeId;
    private String name;
    private String description;
    //private List<Integer> ingredientIds;
    private Map<Integer, Integer> ingredients; // Map of FoodType ID to count
    private int servings;
    private boolean isFavorited;
    private String cookTime;
    private int calories;
    private String difficulty;
    private List<Enums.DietaryAttribute> dietaryAttributes;
    private String imageFile;
    public Recipe(int recipeId, String name, String description, Map<Integer, Integer> ingredients,
                  int servings, boolean isFavorited, String cookTime, int calories, String difficulty,
                  List<Enums.DietaryAttribute> dietaryAttributes, String imageFile) {
        this.recipeId = recipeId;
        this.name = name;
        this.description = description;
        //this.ingredientIds = ingredientIds;
        this.ingredients = new HashMap<>(ingredients);
        this.servings = servings;
        this.isFavorited = isFavorited;
        this.cookTime = cookTime;
        this.calories = calories;
        this.difficulty = difficulty;
        this.dietaryAttributes = new ArrayList<>(dietaryAttributes);
        this.imageFile = imageFile;
        // Debug print statement
        System.out.println("In Constructor - Recipe ID: " + recipeId + " Dietary Attributes: " + dietaryAttributes);
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

    //public List<Integer> getIngredientIds() {
    //    return ingredientIds;
    //}

    public Map<Integer, Integer> getRecipe_Requirements() {
        return new HashMap<>(ingredients);
    }

    public int getRequiredCount(int foodTypeId) {
        return ingredients.getOrDefault(foodTypeId, 0);
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

    public List<Enums.DietaryAttribute> getDietaryAttributes() { return dietaryAttributes; }

    public String getImageFile() { return imageFile;}

    // String for all the required items and counts for a recipe using the Hashmap values.
    public String getIngredientString(HashMap<Integer, FoodType> foodDictionary) {
        StringBuilder ingredientString = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : ingredients.entrySet()) {
            String foodName = foodDictionary.get(entry.getKey()).getItemName();
            ingredientString.append(entry.getValue()).append(" ").append(foodName).append(", ");
        }
        if (ingredientString.length() > 0) {
            ingredientString.setLength(ingredientString.length() - 2);
        }
        return ingredientString.toString();
    }

    ////////////
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

    //public void setIngredientIds(List<Integer> ingredientIds) {
    //    this.ingredientIds = ingredientIds;
    //}

    // Add a new foodtype by ID with its count, the requirements needed to make this recipe.
    public void addNewRequirement(int foodTypeId, int count) {
        ingredients.put(foodTypeId, count);
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

    public void setDietaryAttributes(List<Enums.DietaryAttribute> dietaryAttributes) {
        this.dietaryAttributes = dietaryAttributes;
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
