package com.example.kitchencompanion;

import java.util.ArrayList;
import java.util.List;

// This file stores the recipes to be shown int he Recipe Tab(Tab1)
public class RecipeDatabase {
    private List<Recipe> recipes;

    public RecipeDatabase() {
        recipes = new ArrayList<>();
        recipes.add(new Recipe(1, "Oven-Baked Risotto", desc1, new ArrayList<>(), 4, false, "45 mins", 500, "Easy", "recipe_image_risotto"));
        recipes.add(new Recipe(2, "Chicken Noodle Soup", "Lorem ipsum...", new ArrayList<>(), 2, false, "25 mins", 90, "Easy", "invalid_image"));
        recipes.add(new Recipe(3, "Chicken Parmesan", "Lorem ipsum...", new ArrayList<>(), 2, false, "45 mins", 650, "Medium", "recipe_image_parm"));
        recipes.add(new Recipe(3, "Test Recipe", "Lorem ipsum...", new ArrayList<>(), 2, false, "1 hour", 100, "Medium", ""));
    }

    private String desc1 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";


    public List<Recipe> getRecipes() {
        return recipes;
    }
}
