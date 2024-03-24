package com.example.kitchencompanion;

import java.util.ArrayList;
import java.util.List;

// This file stores the recipes to be shown int he Recipe Tab(Tab1)
public class RecipeDatabase {
    private List<Recipe> recipes;

    public RecipeDatabase() {
        recipes = new ArrayList<>();
        recipes.add(new Recipe(1, "Oven-Baked Risotto", "Lorem ipsum...", new ArrayList<>(), 4, false, "45 mins", 500, "Easy"));
        recipes.add(new Recipe(2, "Chicken Noodle Soup", "Lorem ipsum...", new ArrayList<>(), 2, false, "25 mins", 90, "Easy"));
        recipes.add(new Recipe(3, "Chicken Parmesan", "Lorem ipsum...", new ArrayList<>(), 2, false, "45 mins", 650, "Medium"));
        recipes.add(new Recipe(4, "Test Recipe", "Lorem ipsum...", new ArrayList<>(), 2, false, "1 hour", 500, "Medium"));
        recipes.add(new Recipe(5, "Test Recipe", "Lorem ipsum...", new ArrayList<>(), 2, false, "1 hour", 500, "Medium"));
        recipes.add(new Recipe(6, "Test Recipe", "Lorem ipsum...", new ArrayList<>(), 2, false, "1 hour", 500, "Medium"));
        recipes.add(new Recipe(7, "Test Recipe", "Lorem ipsum...", new ArrayList<>(), 2, false, "1 hour", 500, "Medium"));
        recipes.add(new Recipe(8, "Test Recipe", "Lorem ipsum...", new ArrayList<>(), 2, false, "1 hour", 500, "Medium"));
        recipes.add(new Recipe(9, "Test Recipe", "Lorem ipsum...", new ArrayList<>(), 2, false, "1 hour", 500, "Medium"));
        recipes.add(new Recipe(10, "Test Recipe", "Lorem ipsum...", new ArrayList<>(), 2, true, "1 hour", 500, "Medium"));
        recipes.add(new Recipe(11, "Test Recipe", "Lorem ipsum...", new ArrayList<>(), 2, false, "1 hour", 500, "Medium"));
        recipes.add(new Recipe(12, "Test Recipe", "Lorem ipsum...", new ArrayList<>(), 2, false, "1 hour", 500, "Medium"));
        recipes.add(new Recipe(13, "Test Recipe", "Lorem ipsum...", new ArrayList<>(), 2, false, "1 hour", 500, "Medium"));
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
