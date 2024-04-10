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

                // RecipeS IN DATABASE
        // Recipe 1: Oven Baked Risotto
        Map<Integer, Integer> rec1Ingred = new HashMap<>();
        rec1Ingred.put(3, 2); // Onion
        rec1Ingred.put(4, 1); // Olive Oil
        rec1Ingred.put(5, 3); // Rice
        rec1Ingred.put(6, 5); // Vegetable Broth
        rec1Ingred.put(7, 2); // Parmesan
        rec1Ingred.put(8, 1); // Parsley
        rec1Ingred.put(9, 1); // Salt
        rec1Ingred.put(10, 1); // Pepper

        // Recipe 2: Chicken Noodle Soup
        Map<Integer, Integer> recipe2Ingred = new HashMap<>();
        recipe2Ingred.put(11, 3); // Chicken
        recipe2Ingred.put(12, 2); // Noodles
        recipe2Ingred.put(13, 2); // Carrots
        recipe2Ingred.put(14, 2); // Celery
        recipe2Ingred.put(6, 4);  // Vegetable Broth

        // Recipe 3: Chicken Parm
        Map<Integer, Integer> recipe3Ingred = new HashMap<>();
        recipe3Ingred.put(11, 2); // Chicken
        recipe3Ingred.put(15, 2); // Bread Crumbs
        recipe3Ingred.put(16, 3); // Eggs
        recipe3Ingred.put(17, 2); // Marinara Sauce
        recipe3Ingred.put(18, 2); // Mozzarella
        recipe3Ingred.put(7, 1);  // Parmesan

        // Recipe 4: Vegetarian Stir-Fry
        Map<Integer, Integer> rec4Ingred = new HashMap<>();
        rec4Ingred.put(41, 2); // Tomato
        rec4Ingred.put(42, 1); // Broccoli
        rec4Ingred.put(43, 1); // Spinach
        rec4Ingred.put(44, 1); // Bell Pepper
        rec4Ingred.put(49, 1); // Garlic
        rec4Ingred.put(9, 1); // Salt
        rec4Ingred.put(10, 1); // Pepper

        // Recipe 5: Tomato Basil Pasta
        Map<Integer, Integer> rec5Ingred = new HashMap<>();
        rec5Ingred.put(41, 2); // Tomato
        rec5Ingred.put(3, 1); // Onion
        rec5Ingred.put(12, 1); // Pasta
        rec5Ingred.put(8, 1); // Basil
        rec5Ingred.put(9, 1); // Salt
        rec5Ingred.put(10, 1); // Pepper

        // Recipe 6: Spinach Cheese Omelette
        Map<Integer, Integer> rec6Ingred = new HashMap<>();
        rec6Ingred.put(42, 1); // Spinach
        rec6Ingred.put(73, 1); // Cheese
        rec6Ingred.put(16, 2); // Eggs
        rec6Ingred.put(48, 1); // Garlic
        rec6Ingred.put(9, 1); // Salt
        rec6Ingred.put(10, 1); // Pepper

        // Recipe 7: Quinoa Salad
        Map<Integer, Integer> rec7Ingred = new HashMap<>();
        rec7Ingred.put(56, 2); // Quinoa
        rec7Ingred.put(41, 1); // Tomato
        rec7Ingred.put(44, 1); // Cucumber
        rec7Ingred.put(8, 1); // Parsley
        rec7Ingred.put(9, 1); // Salt
        rec7Ingred.put(10, 1); // Pepper

        // Recipe 8: Garlic Lemon Chicken
        Map<Integer, Integer> rec8Ingred = new HashMap<>();
        rec8Ingred.put(49, 2); // Garlic
        rec8Ingred.put(11, 2); // Chicken
        rec8Ingred.put(93, 1); // Lemon Juice
        rec8Ingred.put(4, 1); // Olive Oil
        rec8Ingred.put(9, 1); // Salt
        rec8Ingred.put(10, 1); // Pepper

        // Recipe 9: Beef Stir-Fry
        Map<Integer, Integer> rec9Ingred = new HashMap<>();
        rec9Ingred.put(73, 1); // Cheese
        rec9Ingred.put(15, 2); // Bread Crumbs
        rec9Ingred.put(75, 1); // Butter
        rec9Ingred.put(76, 1); // Cream
        rec9Ingred.put(77, 1); // Cream Cheese

        // Recipe 10: Mixed Berry Smoothie
        Map<Integer, Integer> rec10Ingred = new HashMap<>();
        rec10Ingred.put(19, 1); // Grapes
        rec10Ingred.put(24, 1); // Strawberry
        rec10Ingred.put(25, 1); // Raspberry
        rec10Ingred.put(26, 1); // Blueberry
        rec10Ingred.put(38, 1); // Cranberry
        rec10Ingred.put(39, 1); // Coconut
        rec10Ingred.put(119, 1); // Coconut Milk

        // Recipe 11: Mozzarella and Tomato Salad
        Map<Integer, Integer> rec11Ingred = new HashMap<>();
        rec11Ingred.put(41, 2); // Tomato
        rec11Ingred.put(18, 1); // Mozzarella
        rec11Ingred.put(8, 1); // Parsley
        rec11Ingred.put(9, 1); // Salt
        rec11Ingred.put(10, 1); // Pepper

        // Recipe 12: Tofu and Veggie Curry
        Map<Integer, Integer> rec12Ingred = new HashMap<>();
        rec12Ingred.put(66, 1); // Tofu
        rec12Ingred.put(12, 2); // Rice
        rec12Ingred.put(42, 1); // Broccoli
        rec12Ingred.put(44, 1); // Cucumber
        rec12Ingred.put(8, 1); // Parsley
        rec12Ingred.put(9, 1); // Salt
        rec12Ingred.put(10, 1); // Pepper

        // Recipe 13: Primavera Pasta
        Map<Integer, Integer> rec13Ingred = new HashMap<>();
        rec13Ingred.put(41, 2); // Tomato
        rec13Ingred.put(12, 1); // Pasta
        rec13Ingred.put(42, 1); // Broccoli
        rec13Ingred.put(43, 1); // Spinach
        rec13Ingred.put(49, 1); // Garlic
        rec13Ingred.put(9, 1); // Salt
        rec13Ingred.put(10, 1); // Pepper

        // Recipe 14: Baked Lemon Pepper Fish
        Map<Integer, Integer> rec14Ingred = new HashMap<>();
        rec14Ingred.put(65, 1); // Fish )
        rec14Ingred.put(93, 1); // Lemon Juice
        rec14Ingred.put(10, 1); // Black Pepper
        rec14Ingred.put(9, 1); // Salt
        rec14Ingred.put(10, 1); // Pepper

        // Recipe 15: Chickpea Salad
        Map<Integer, Integer> rec15Ingred = new HashMap<>();
        rec15Ingred.put(69, 1); // Chickpeas
        rec15Ingred.put(44, 1); // Cucumber
        rec15Ingred.put(41, 1); // Tomato
        rec15Ingred.put(8, 1); // Parsley
        rec15Ingred.put(9, 1); // Salt
        rec15Ingred.put(10, 1); // Pepper

        // Recipe ATTRIBUTES
        List<Enums.DietaryAttribute> rec1Attributes = Arrays.asList(Enums.DietaryAttribute.VEGAN, Enums.DietaryAttribute.ORGANIC);
        List<Enums.DietaryAttribute> rec2Attributes = Arrays.asList(Enums.DietaryAttribute.GLUTEN_FREE, Enums.DietaryAttribute.LOW_CALORIE);
        List<Enums.DietaryAttribute> rec3Attributes = Arrays.asList(Enums.DietaryAttribute.LOW_SODIUM, Enums.DietaryAttribute.ORGANIC);
        List<Enums.DietaryAttribute> rec4Attributes = Arrays.asList(Enums.DietaryAttribute.VEGETARIAN, Enums.DietaryAttribute.LOW_FAT);
        List<Enums.DietaryAttribute> rec5Attributes = Arrays.asList(Enums.DietaryAttribute.ORGANIC, Enums.DietaryAttribute.LOW_CHOLESTEROL);
        List<Enums.DietaryAttribute> rec6Attributes = Arrays.asList(Enums.DietaryAttribute.VEGETARIAN, Enums.DietaryAttribute.LOW_CARB);
        List<Enums.DietaryAttribute> rec7Attributes = Arrays.asList(Enums.DietaryAttribute.ORGANIC, Enums.DietaryAttribute.LOW_SUGAR);
        List<Enums.DietaryAttribute> rec8Attributes = Arrays.asList(Enums.DietaryAttribute.GLUTEN_FREE, Enums.DietaryAttribute.LOW_SODIUM);
        List<Enums.DietaryAttribute> rec9Attributes = Arrays.asList(Enums.DietaryAttribute.LOW_FAT, Enums.DietaryAttribute.LOW_CALORIE);
        List<Enums.DietaryAttribute> rec10Attributes = Arrays.asList(Enums.DietaryAttribute.VEGAN, Enums.DietaryAttribute.LOW_CHOLESTEROL);
        List<Enums.DietaryAttribute> rec11Attributes = Arrays.asList(Enums.DietaryAttribute.ORGANIC, Enums.DietaryAttribute.LOW_SUGAR);
        List<Enums.DietaryAttribute> rec12Attributes = Arrays.asList(Enums.DietaryAttribute.VEGETARIAN, Enums.DietaryAttribute.LOW_CARB);
        List<Enums.DietaryAttribute> rec13Attributes = Arrays.asList(Enums.DietaryAttribute.GLUTEN_FREE, Enums.DietaryAttribute.LOW_FAT);
        List<Enums.DietaryAttribute> rec14Attributes = Arrays.asList(Enums.DietaryAttribute.LOW_SODIUM, Enums.DietaryAttribute.ORGANIC);
        List<Enums.DietaryAttribute> rec15Attributes = Arrays.asList(Enums.DietaryAttribute.LOW_CALORIE, Enums.DietaryAttribute.LOW_CHOLESTEROL);

        // Recipe ALLERGENS
        List<Enums.CommonFoodAllergy> rec1Allergies = Arrays.asList();
        List<Enums.CommonFoodAllergy> rec2Allergies = Arrays.asList(Enums.CommonFoodAllergy.GLUTEN);
        List<Enums.CommonFoodAllergy> rec3Allergies = Arrays.asList(Enums.CommonFoodAllergy.EGGS, Enums.CommonFoodAllergy.DAIRY);
        List<Enums.CommonFoodAllergy> rec4Allergies = Arrays.asList();
        List<Enums.CommonFoodAllergy> rec5Allergies = Arrays.asList(Enums.CommonFoodAllergy.GLUTEN);
        List<Enums.CommonFoodAllergy> rec6Allergies = Arrays.asList(Enums.CommonFoodAllergy.EGGS, Enums.CommonFoodAllergy.DAIRY);
        List<Enums.CommonFoodAllergy> rec7Allergies = Arrays.asList();
        List<Enums.CommonFoodAllergy> rec8Allergies = Arrays.asList();
        List<Enums.CommonFoodAllergy> rec9Allergies = Arrays.asList(Enums.CommonFoodAllergy.DAIRY);
        List<Enums.CommonFoodAllergy> rec10Allergies = Arrays.asList();
        List<Enums.CommonFoodAllergy> rec11Allergies = Arrays.asList(Enums.CommonFoodAllergy.DAIRY);
        List<Enums.CommonFoodAllergy> rec12Allergies = Arrays.asList();
        List<Enums.CommonFoodAllergy> rec13Allergies = Arrays.asList(Enums.CommonFoodAllergy.GLUTEN);
        List<Enums.CommonFoodAllergy> rec14Allergies = Arrays.asList();
        List<Enums.CommonFoodAllergy> rec15Allergies = Arrays.asList();

        // Recipe DESCRIPTIONS
        String rec1Desc = "\n" + "1. Preheat oven to 375°F.\n" +
                "2. Heat olive oil under medium heat in an oven safe pot.\n" +
                "3. Add diced onions, cook until translucent.\n" +
                "4. Stir in rice and cook for 2 more minutes.\n" +
                "5. Pour in vegetable broth and stir.\n" +
                "6. Cover the pot and place into the oven.\n" +
                "7. Bake 25-30 minutes or until liquid absorbed.\n" +
                "8. Add grated parmesan cheese and parsley to the top and stir.\n" +
                "9. Season with salt and pepper.\n"
                ;

        String rec2Desc = "Fill a large pot with water and 4 cups of Vegetable broth over the stove on Medium heat. Add 3 shredded chicken breasts. Add 2 packets of pasta. Then add 2 carrots sliced to desired size. Add 2 stalks of celery cut into small pieces. Cook for 15 mins";
        String rec3Desc = "Note to self: visit (https://www.bonappetit.com/recipe/bas-best-chicken-parm) for the recipe. I don't like garlic so avoid that.";
        String rec4Desc = "In a wok or similar pot, add 1 diced potato, 2 brocolli stalks separated into individual pieces, 1 tsp salt, 1 pack of spinach leaves, 1 tsp of black pepper, 1 bell pepper shredded, and 1 cucumber shredded. Mix well.";
        String rec5Desc = "In a medium sized pot over medium heat, add these chopped ingredients: 1 onion, 1 pack of parsley, 2 brocolli stalks, 1 tsp salt, 1 tsp black pepper, 1 cup of pasta. Mix well";
        String rec6Desc = "<No Description Provided>";
        String rec7Desc = "<No Description Provided>";
        String rec8Desc = "<No Description Provided>";
        String rec9Desc = "<No Description Provided>";
        String rec10Desc = "<No Description Provided>";
        String rec11Desc = "<No Description Provided>";
        String rec12Desc = "<No Description Provided>";
        String rec13Desc = "<No Description Provided>";
        String rec14Desc = "<No Description Provided>";
        String rec15Desc = "<No Description Provided>";

        // Recipes. First item below is ID 1, second item is ID 2, etc
        addRecipe("Oven-Baked Risotto", rec1Desc, rec1Ingred, 4, false, "45 mins", 350, "Medium", rec1Attributes, rec1Allergies, "recipe_image_risotto");
        addRecipe("Chicken Noodle Soup", rec2Desc, recipe2Ingred, 3, false, "30 mins", 250, "Easy", rec2Attributes, rec2Allergies, "recipe_image_chickennoodlesoup");
        addRecipe("Chicken Parm", rec3Desc, recipe3Ingred, 2, false, "50 mins", 450, "Hard", rec3Attributes, rec3Allergies, "recipe_image_chickenparm");
        addRecipe("Vegetarian Stir-Fry", rec4Desc, rec4Ingred, 4, true, "20 mins", 300, "Easy", rec4Attributes, rec4Allergies, "recipe_image_vegestirfry");
        addRecipe("Tomato Basil Pasta", rec5Desc, rec5Ingred, 2, false, "25 mins", 400, "Medium", rec5Attributes, rec5Allergies, "recipe_image_tomatobasilpasta");
        addRecipe("Spinach Cheese Omelette", rec6Desc, rec6Ingred, 1, false, "15 mins", 200, "Easy", rec6Attributes, rec6Allergies, "recipe_image_spinachcheeseomelette");
        addRecipe("Quinoa Salad", rec7Desc, rec7Ingred, 3, false, "30 mins", 350, "Easy", rec7Attributes, rec7Allergies, "recipe_image_quinsalad");
        addRecipe("Garlic Lemon Chicken", rec8Desc, rec8Ingred, 2, false, "40 mins", 400, "Medium", rec8Attributes, rec8Allergies, "recipe_image_garliclemonchicken");
        addRecipe("Beef Stir-Fry", rec9Desc, rec9Ingred, 2, false, "35 mins", 500, "Hard", rec9Attributes, rec9Allergies, "recipe_image_beefstirfry");
        addRecipe("Mixed Berry Smoothie", rec10Desc, rec10Ingred, 2, false, "10 mins", 150, "Easy", rec10Attributes, rec10Allergies, "recipe_image_mixedberrysmoothie");
        addRecipe("Mozzarella and Tomato Salad", rec11Desc, rec11Ingred, 2, false, "15 mins", 250, "Easy", rec11Attributes, rec11Allergies, "recipe_image_mozztomatosalad");
        addRecipe("Tofu and Veggie Curry", rec12Desc, rec12Ingred, 4, false, "45 mins", 350, "Medium", rec12Attributes, rec12Allergies, "recipe_image_tofuvegecurry");
        addRecipe("Primavera Pasta", rec13Desc, rec13Ingred, 3, false, "40 mins", 400, "Medium", rec13Attributes, rec13Allergies, "recipe_image_primaverapasta");
        addRecipe("Baked Lemon Pepper Fish", rec14Desc, rec14Ingred, 2, false, "30 mins", 300, "Easy", rec14Attributes, rec14Allergies, "recipe_image_bakedlemonpepperfish");
        addRecipe("Chickpea Salad", rec15Desc, rec15Ingred, 2, false, "20 mins", 250, "Easy", rec15Attributes, rec15Allergies, "recipe_image_chickpeasalad");
        //printIngredientsForRecipe(1); //testing
    }


    // Add a recipe to the database
    private void addRecipe(String name, String description, Map<Integer, Integer> ingredients, int servings, boolean isFavorited, String cookTime, int calories, String difficulty, List<Enums.DietaryAttribute> dietaryAttributes, List<Enums.CommonFoodAllergy> commonFoodAllergies, String imageFile) {
        Recipe recipe = new Recipe(nextRecipeId, name, description, ingredients, servings, isFavorited, cookTime, calories, difficulty, dietaryAttributes, commonFoodAllergies, imageFile);
        recipes.add(recipe);
        idToRecipeMap.put(nextRecipeId, recipe);
        nextRecipeId++;
    }

    // remove a recipe from the database
    public void removeRecipe(int recipeId) {
        Recipe recipeToRemove = idToRecipeMap.get(recipeId);
        if (recipeToRemove != null) {
            System.out.println("DEBUG: Removing recipe: " + recipeToRemove.getName() + " Recipe ID: " + recipeId);
            recipes.removeIf(recipe -> recipe.getRecipeId() == recipeId);
            idToRecipeMap.remove(recipeId);

            // After removal, print current recipes
            printRecipeDatabase();
        }
    }

    // get all recipes in the database
    public List<Recipe> getRecipes() {
        return recipes;
    }

    // debugging - print the database
    public void printRecipeDatabase() {
        System.out.println("DEBUG: Current recipes in the database:");
        for (Recipe recipe : recipes) {
            System.out.println("Recipe ID: " + recipe.getRecipeId() + ", Name: " + recipe.getName());
        }
    }

    // debugging - print ingredients for recipe
    public String printIngredientsForRecipe(int recipeId) {
        Recipe recipe = idToRecipeMap.get(recipeId);
        if (recipe != null) {
            return recipe.getIngredientString(foodDictionary);
        } else {
            return "Recipe ID " + recipeId + " not found.";
        }
    }
}
