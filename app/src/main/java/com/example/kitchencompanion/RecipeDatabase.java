package com.example.kitchencompanion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// This file stores the recipes to be shown int he Recipe Tab(Tab1)
public class RecipeDatabase {
    private List<Recipe> recipes;

    public RecipeDatabase() {
        recipes = new ArrayList<>();
        recipes.add(new Recipe(1, "Oven-Baked Risotto", desc1, new ArrayList<>(), 4, true, "45 mins", 500, "Easy", Arrays.asList(Enums.DietaryAttribute.VEGAN, Enums.DietaryAttribute.ORGANIC), "recipe_image_risotto"));
        recipes.add(new Recipe(2, "Chicken Noodle Soup", "Lorem ipsum...", new ArrayList<>(), 2, false, "25 mins", 90, "Easy", new ArrayList<>() ,"invalid_image"));
        recipes.add(new Recipe(3, "Chicken Parmesan", "Lorem ipsum...", new ArrayList<>(), 2, false, "45 mins", 650, "Medium", new ArrayList<>(),"recipe_image_parm"));
        recipes.add(new Recipe(4, "Test Recipe", "Lorem ipsum...", new ArrayList<>(), 2, false, "1 hour", 100, "Medium", new ArrayList<>(), ""));
        for (Recipe recipe : recipes) {
            System.out.println("TESTING Recipe ID: " + recipe.getRecipeId() + " Dietary Attributes: " + recipe.getDietaryAttributes());
        }
    }

    private String desc1 = "" +
            "\n" + "1. Preheat oven to 375°F.\n"
            + "2. Heat olive oil under medium heat in an oven safe pot.\n"
            + "3. Add diced onions, cook until translucent.\n"
            + "4. Stir in rice and cook for 2 more minutes.\n"
            + "5. Pour in vegetable broth and stir.\n"
            + "6. Cover the put and place into the oven.\n"
            + "7. Bake 25-30 minutes or until liquid absorbed.\n"
            + "8. Add grated parmesean cheese and parsely to the top and stir.\n"
            + "9. Season with salt and pepper.\n";

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
