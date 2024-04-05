package com.example.kitchencompanion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This file stores the recipes to be shown in the Recipe Tab (Tab1)
public class RecipeDatabase {
    private List<Recipe> recipes;
    private int nextRecipeId;
    private HashMap<Integer, FoodType> foodDictionary;
    private HashMap<Integer, Recipe> idToRecipeMap;

    public RecipeDatabase(HashMap<Integer, FoodType> foodDictionary) {
        this.foodDictionary = foodDictionary;
        this.recipes = new ArrayList<>();
        this.idToRecipeMap = new HashMap<>();
        this.nextRecipeId = 1; // Initialize ID to prevent issues like overwriting old ID

        // This is just test code, the user should add recipeItems in the Tab1 addRecipe button which would take their values and call addRecipe from this class to add an item.

        // TEST INGREDIENTS
        // Key is the FoodType ID, count is value
        // If you want to test with other preset ones, see getFoodDictionary output from Tab2.java
        Map<Integer, Integer> rec1_Ingred = new HashMap<>();
        rec1_Ingred.put(3, 2); // Onion
        rec1_Ingred.put(4, 1); // Olive Oil
        rec1_Ingred.put(5, 3); // Rice
        rec1_Ingred.put(6, 5); // Vegetable Broth
        rec1_Ingred.put(7, 2); // Parmesan
        rec1_Ingred.put(8, 1); // Parsley
        rec1_Ingred.put(9, 1); // Salt
        rec1_Ingred.put(10, 1); // Pepper

        Map<Integer, Integer> recipe2_Ingredients = new HashMap<>();
        recipe2_Ingredients.put(11, 3); // Chicken
        recipe2_Ingredients.put(12, 2); // Noodles
        recipe2_Ingredients.put(13, 2); // Carrots
        recipe2_Ingredients.put(14, 2); // Celery
        recipe2_Ingredients.put(6, 4);  // Vegetable Broth

        Map<Integer, Integer> recipe3_Ingredients = new HashMap<>();
        recipe3_Ingredients.put(11, 2); // Chicken
        recipe3_Ingredients.put(15, 2); // Bread Crumbs
        recipe3_Ingredients.put(16, 3); // Eggs
        recipe3_Ingredients.put(17, 2); // Marinara Sauce
        recipe3_Ingredients.put(18, 2); // Mozzarella
        recipe3_Ingredients.put(7, 1);  // Parmesan

        Map<Integer, Integer> recipe4_Ingredients = new HashMap<>();
        recipe4_Ingredients.put(0, 2);  // Apple
        recipe4_Ingredients.put(1, 3);  // Banana
        recipe4_Ingredients.put(2, 1);  // Orange
        recipe4_Ingredients.put(3, 1);  // Onion
        recipe4_Ingredients.put(5, 2);  // Rice

        // TEST ATTRIBUTES
        List<Enums.DietaryAttribute> rec1Attr = Arrays.asList(Enums.DietaryAttribute.VEGAN, Enums.DietaryAttribute.ORGANIC,
                Enums.DietaryAttribute.TEST, Enums.DietaryAttribute.TEST, Enums.DietaryAttribute.TEST, Enums.DietaryAttribute.TEST,
                Enums.DietaryAttribute.TEST, Enums.DietaryAttribute.TEST, Enums.DietaryAttribute.TEST, Enums.DietaryAttribute.TEST,
                Enums.DietaryAttribute.TEST, Enums.DietaryAttribute.TEST, Enums.DietaryAttribute.TEST, Enums.DietaryAttribute.TEST,
                Enums.DietaryAttribute.TEST ) ;

        // TEST DESCRIPTIONS

        String rec1Desc = "" +
                "\n" + "1. Preheat oven to 375°F.\n" +
                "2. Heat olive oil under medium heat in an oven safe pot.\n" +
                "3. Add diced onions, cook until translucent.\n" +
                "4. Stir in rice and cook for 2 more minutes.\n" +
                "5. Pour in vegetable broth and stir.\n" +
                "6. Cover the pot and place into the oven.\n" +
                "7. Bake 25-30 minutes or until liquid absorbed.\n" +
                "8. Add grated parmesan cheese and parsley to the top and stir.\n" +
                "9. Season with salt and pepper.\n" +
                "BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n"+
                "BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n"+
                "BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n"+
                "BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n"+
                "BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n"+
                "BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n"+
                "BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n"+
                "BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n"+
                "BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n"+
                "BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n"+
                "BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n"+
                "BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n BLA BLA BLA BLA \n"
                ;
        
        // TEST RECIPES
        addRecipe("Oven-Baked Risotto", rec1Desc, rec1_Ingred, 4, true, "45 mins", 500, "Easy", rec1Attr , "recipe_image_risotto");
        addRecipe("Chicken Noodle Soup", "Lorem ipsum...", recipe2_Ingredients, 2, false, "25 mins", 90, "Easy", new ArrayList<>() ,"invalid_image");
        addRecipe("Chicken Parmesan", "Lorem ipsum...", recipe3_Ingredients, 2, false, "45 mins", 650, "Medium", new ArrayList<>(),"recipe_image_parm");
        addRecipe("Test Recipe", "Lorem ipsum...", recipe4_Ingredients, 2, false, "1 hour", 100, "Medium", new ArrayList<>(), "");


        //printIngredientsForRecipe(1); //testing
    }
    

    private void addRecipe(String name, String description, Map<Integer, Integer> ingredients, int servings, boolean isFavorited, String cookTime, int calories, String difficulty, List<Enums.DietaryAttribute> dietaryAttributes, String imageFile) {
        Recipe recipe = new Recipe(nextRecipeId, name, description, ingredients, servings, isFavorited, cookTime, calories, difficulty, dietaryAttributes, imageFile);
        recipes.add(recipe);
        idToRecipeMap.put(nextRecipeId, recipe);
        nextRecipeId++;
    }

    public void removeRecipe(int recipeId) {
        recipes.removeIf(recipe -> recipe.getRecipeId() == recipeId);
        idToRecipeMap.remove(recipeId);
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public String printIngredientsForRecipe(int recipeId) {
        Recipe recipe = idToRecipeMap.get(recipeId);
        if (recipe != null) {
            return recipe.getIngredientString(foodDictionary);
        } else {
            return "Recipe ID " + recipeId + " not found.";
        }
    }
}
