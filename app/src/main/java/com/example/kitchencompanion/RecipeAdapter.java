package com.example.kitchencompanion;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Some links(cite code more):
// https://www.codevscolor.com/android-kotlin-delete-item-recyclerview
// https://www.digitalocean.com/community/tutorials/android-recyclerview-swipe-to-delete-undo
// https://www.androidhive.info/2016/01/android-working-with-recycler-view/
// https://www.javatpoint.com/android-recyclerview-list-example
// https://abhiandroid.com/materialdesign/recyclerview#gsc.tab=0
// https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.Adapter
// https://stackoverflow.com/questions/34383763/how-to-open-a-popup-window-from-an-adapter-class
// https://stackoverflow.com/questions/36661775/android-popup-window-with-variable-text
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private List<Recipe> recipes;
    private Context context;

    private RecipeDatabase recipeDatabase;
    private List<PantryItem> pantryList;
    private HashMap<Integer, FoodType> foodDictionary;
    private Map<Integer, Fragment> fragmentMap;
    private int lastSwipedPosition = -1;

    private ShopListAdapter shoppingList;

    public RecipeAdapter(Context context, List<Recipe> recipes, RecipeDatabase recipeDatabase, List<PantryItem> pantryList, HashMap<Integer, FoodType> foodDictionary, Map<Integer, Fragment> fragmentMap,ShopListAdapter shoppingList) {    this.context = context;
        this.recipes = recipes;
        this.recipeDatabase = recipeDatabase;
        this.pantryList = pantryList;
        this.foodDictionary = foodDictionary;
        this.fragmentMap = fragmentMap;
        this.shoppingList = shoppingList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        // Set warning icon visibility based on dietary attributes here if required
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        // Col 1 text
        holder.recipeName.setText(recipe.getName());
        holder.recipeCookTime.setText("Cook Time: " + recipe.getCookTime());
        holder.recipeCalories.setText("Calories: " + String.valueOf(recipe.getCalories()));
        holder.recipeDifficulty.setText("Difficulty: " + recipe.getDifficulty());

        // Required Ingredients
        int availableIngredientsCount = calculateAvailableIngredients(recipe);
        int totalIngredientsCount = recipe.getTotalIngredientCount();
        String requiredIngredientsText = "Required Ingredients: " + availableIngredientsCount + "/" + totalIngredientsCount;
        holder.recipeRequiredIngredients.setText(requiredIngredientsText);

        // Favorite Heart
        holder.favoriteIcon.setImageResource(recipe.isFavorited() ? R.drawable.heart_solid : R.drawable.heart_outline);

        holder.favoriteIcon.setOnClickListener(v -> {
            recipe.toggleFavorite();
            notifyItemChanged(position);
        });

        holder.itemView.setOnClickListener(v -> {
            showDescPopup(context, recipe, position);
        });

        holder.closeButton.setOnClickListener(v -> {
            showConfirmationDialog(position);
        });

        int imageResourceId = context.getResources().getIdentifier(recipe.getImageFile(), "drawable", context.getPackageName());
        holder.recipeImage.setImageResource(imageResourceId > 0 ? imageResourceId : R.drawable.ic_launcher_background);

        // Warning Icon
        // Debug print statements to check dietary attributes
        if (!recipe.getCommonFoodAllergies().isEmpty()) {
            holder.warningIcon.setVisibility(View.VISIBLE);
        } else {
            holder.warningIcon.setVisibility(View.GONE);
        }

        //System.out.println("Icon Visibility for Recipe ID: " + recipe.getRecipeId() + ": " + holder.warningIcon.getVisibility());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName, recipeCalories, recipeCookTime, recipeDifficulty, recipeRequiredIngredients;
        ImageView closeButton, favoriteIcon, recipeImage, warningIcon;
        FrameLayout addMissingLayout;
        View viewForeground;
        Button addMissingButton;

        boolean isHandlerRunning = false;
        boolean isPopupShown = false;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize UI components
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeCalories = itemView.findViewById(R.id.recipeCalories);
            recipeCookTime = itemView.findViewById(R.id.recipeCookTime);
            recipeDifficulty = itemView.findViewById(R.id.recipeDifficulty);
            recipeRequiredIngredients = itemView.findViewById(R.id.recipeRequiredIngredients);
            closeButton = itemView.findViewById(R.id.closeButtonRecipes);
            favoriteIcon = itemView.findViewById(R.id.favoriteRecipeItemButton);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            warningIcon = itemView.findViewById(R.id.warningIcon);
            addMissingLayout = itemView.findViewById(R.id.addMissingLayout);
            viewForeground = itemView.findViewById(R.id.recipeItemLayout);

            // Button for adding missing ingredients - click listener is now removed
            addMissingButton = itemView.findViewById(R.id.addMissingButton);
        }
    }

    private void showDescPopup(Context context, Recipe recipe, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.popup_recipe_desc, null);

        // Find TextViews
        TextView attributesTextView = dialogView.findViewById(R.id.attributesTextView);
        TextView ingredientsTextView = dialogView.findViewById(R.id.ingredientsTextView);
        TextView descriptionTextView = dialogView.findViewById(R.id.descriptionTextView);

        // Set 1st textview in popup_recipe_desc
        StringBuilder attributesBuilder = new StringBuilder();

                // Recipe Dietary Attributes
        for (Enums.DietaryAttribute attribute : recipe.getDietaryAttributes()) {
            if (attributesBuilder.length() > 0) attributesBuilder.append(", ");
            attributesBuilder.append(attribute.toString());
        }
                // Recipe Allergens
        for (Enums.CommonFoodAllergy allergy : recipe.getCommonFoodAllergies()) {
            if (attributesBuilder.length() > 0) attributesBuilder.append(", ");
            attributesBuilder.append("Allergen-" + allergy.toString());
        }

        // Set the textview 1 text with bold and underline for the label
        String labelAttributes = "Recipe Dietary Attributes/Allergens: \n";
        String attributes = attributesBuilder.toString();
        SpannableString spannableAttributes = new SpannableString(labelAttributes + attributes);
        spannableAttributes.setSpan(new StyleSpan(Typeface.BOLD), 0, labelAttributes.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableAttributes.setSpan(new UnderlineSpan(), 0, labelAttributes.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        attributesTextView.setText(spannableAttributes);

        // Convert ingredient IDs to names and display them
        StringBuilder ingredientsBuilder = new StringBuilder();
        for (Map.Entry<Integer, Integer> ingredient : recipe.getRecipe_Requirements().entrySet()) {
            FoodType foodType = this.foodDictionary.get(ingredient.getKey());
            String ingredientName = (foodType != null) ? foodType.getItemName() : "Unknown Ingredient";
            ingredientsBuilder.append(ingredient.getValue()).append(" ").append(ingredientName).append(", ");
        }
        // Remove the trailing comma and space from the ingredients list
        String ingredientsText = ingredientsBuilder.length() > 0 ? ingredientsBuilder.substring(0, ingredientsBuilder.length() - 2) : "";

        // Set the Ingredients text with bold and underline for the label
        String labelIngredients = "Ingredients: \n";
        SpannableString spannableIngredients = new SpannableString(labelIngredients + ingredientsText);
        spannableIngredients.setSpan(new StyleSpan(Typeface.BOLD), 0, labelIngredients.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableIngredients.setSpan(new UnderlineSpan(), 0, labelIngredients.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ingredientsTextView.setText(spannableIngredients);

        // Set the Recipe Instructions text with bold and underline for the label
        String labelInstructions = "Recipe Instructions: \n";
        String instructionsText = recipe.getDescription();
        SpannableString spannableInstructions = new SpannableString(labelInstructions + instructionsText);
        spannableInstructions.setSpan(new StyleSpan(Typeface.BOLD), 0, labelInstructions.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableInstructions.setSpan(new UnderlineSpan(), 0, labelInstructions.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        descriptionTextView.setText(spannableInstructions);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Close popup button
        dialogView.findViewById(R.id.rightCloseButton).setOnClickListener(v -> dialog.dismiss());

        // Mark cooked button logic
        dialogView.findViewById(R.id.markCookedButton).setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) context;
            Tab2 tab2 = mainActivity.getTab2();
            Map<Integer, Integer> ingredients = recipe.getRecipe_Requirements();

            System.out.println("DEBUG - Marking as cooked for Recipe: " + recipe.getName());

            for (Map.Entry<Integer, Integer> ingredient : ingredients.entrySet()) {
                int foodTypeId = ingredient.getKey();
                int requiredAmount = ingredient.getValue();
                FoodType foodType = tab2.getFoodDictionary().get(foodTypeId);

                if (foodType != null) {
                    int pantryCount = tab2.getPantryList().stream()
                            .filter(item -> item.getType().getID() == foodTypeId)
                            .findFirst()
                            .map(PantryItem::getCount)
                            .orElse(0);
                    int amountToRemove = Math.min(pantryCount, requiredAmount);
                    tab2.removeItems(foodType, amountToRemove);

                    System.out.println("DEBUG - Recipe: " + recipe.getName() + " - Removing: " + foodType.getItemName() + " (ID: " + foodTypeId + ") Count: " + amountToRemove + ", Current Pantry Count: " + (pantryCount - amountToRemove));
                }
            }

            // Update the adapter immediately after modifications
            updateRecipes(recipeDatabase.getRecipes());
            dialog.dismiss(); // Close the dialog after marking as cooked
        });

        dialog.show();
    }


    private void showConfirmationDialog(int position) {
        Recipe recipe = recipes.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.confirmation_dialog, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.confirmDeleteButton).setOnClickListener(v -> {
            int recipeId = recipe.getRecipeId();
            recipeDatabase.removeRecipe(recipeId);
            // Remove using recipeId, not position, to avoid index issues
            recipes.removeIf(r -> r.getRecipeId() == recipeId);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, recipes.size() - position);
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.cancelDeleteButton).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    public void updateRecipes(List<Recipe> updatedRecipes) {
        this.recipes = updatedRecipes;
        notifyDataSetChanged();
    }
    private RecyclerView recyclerView;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    // Popup appears when swiped out on recipe card to allow user to add all required ingredients for recipe not in pantry currently
    // Have to do this way since clicking the Add Missing button wasn't working since only detecting swipes.

    public void showAddMissingConfirmation(int position) {
        Recipe recipe = recipes.get(position);
        MainActivity mainActivity = (MainActivity) context;
        Tab2 tab2 = mainActivity.getTab2();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.confirmation_add_missing, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.confirmAddMissingButton).setOnClickListener(v -> {
            for (Map.Entry<Integer, Integer> entry : recipe.getRecipe_Requirements().entrySet()) {
                int foodTypeId = entry.getKey();
                int requiredAmount = entry.getValue();
                PantryItem pantryItem = tab2.getPantryList().stream()
                        .filter(item -> item.getType().getID() == foodTypeId)
                        .findFirst()
                        .orElse(null);

                int currentAmountInPantry = pantryItem != null ? pantryItem.getCount() : 0;
                int amountToAdd = requiredAmount - currentAmountInPantry;

                if (amountToAdd > 0) {
                    tab2.addItems(foodDictionary.get(foodTypeId), amountToAdd);
                    System.out.println("DEBUG - Adding missing for Recipe: " + recipe.getName() + " - Item: " + foodDictionary.get(foodTypeId).getItemName() + ", Amount Added: " + amountToAdd + ", Current Pantry Count: " + (currentAmountInPantry + amountToAdd));
                }
            }

            updateRecipes(recipeDatabase.getRecipes());
            dialog.dismiss();
            resetSwipeView(position); // Reset the swipe view if confirm button
            lastSwipedPosition = -1;
        });

        dialogView.findViewById(R.id.cancelAddMissingButton).setOnClickListener(v -> {
            dialog.dismiss();
            resetSwipeView(position); // Reset the swipe view if cancel button
            lastSwipedPosition = -1;
        });

        if (lastSwipedPosition != -1 && lastSwipedPosition != position) {
            // Reset previous swipe before new swipe
            resetSwipeView(lastSwipedPosition);
        }

        lastSwipedPosition = position;
        dialog.show();
    }

    private void closeOpenSwipeViews() {
        if (lastSwipedPosition != -1) {
            resetSwipeView(lastSwipedPosition);
        }
        lastSwipedPosition = -1;
    }

    public void resetSwipeView(int position) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder != null) {
            View foregroundView = viewHolder.itemView.findViewById(R.id.recipeItemLayout);
            if (foregroundView != null) {
                foregroundView.animate().translationX(0).setDuration(300).start();
            }
        }
    }

    private int calculateAvailableIngredients(Recipe recipe) {
        int availableIngredients = 0;
        Map<Integer, Integer> recipeRequirements = recipe.getRecipe_Requirements();

        System.out.println("Calculating Required Ingredients for Recipe: " + recipe.getName() + " (ID: " + recipe.getRecipeId() + ")");

        for (Map.Entry<Integer, Integer> requirement : recipeRequirements.entrySet()) {
            int ingredientId = requirement.getKey();
            int requiredAmount = requirement.getValue();
            int currentAmountInPantry = 0;

            // Find this ingredient in the pantry
            for (PantryItem item : pantryList) {
                if (item.getType().getID() == ingredientId) {
                    currentAmountInPantry = item.totalCount();
                    // Count the available amount, not exceeding the required amount
                    availableIngredients += Math.min(currentAmountInPantry, requiredAmount);
                    break; // Found the current ingredient, move to next
                }
            }

            System.out.println("Ingredient ID: " + ingredientId +
                    ", In Pantry: " + currentAmountInPantry +
                    ", Required: " + requiredAmount);
        }

        return availableIngredients; // Return the count of available ingredients
    }


    private void addMissingIngredients(Recipe recipe) {
        Map<Integer, Integer> recipeRequirements = recipe.getRecipe_Requirements();
        System.out.println("DEBUG: Adding Missing Items for Recipe ID " + recipe.getRecipeId() + " '" + recipe.getName() + "'");
        for (Map.Entry<Integer, Integer> requirement : recipeRequirements.entrySet()) {
            int foodTypeId = requirement.getKey();
            int requiredAmount = requirement.getValue();
            int availableAmount = 0;
            for (PantryItem item : pantryList) {
                if (item.getType().getID() == foodTypeId) {
                    availableAmount += item.totalCount();
                }
            }
            int missingAmount = requiredAmount - availableAmount;
            if (missingAmount > 0) {
                System.out.println("DEBUG: Adding item ID " + foodTypeId + " '" + pantryList.stream()
                        .filter(item -> item.getType().getID() == foodTypeId)
                        .findFirst()
                        .map(item -> item.getType().getItemName())
                        .orElse("Unknown") + "', " + missingAmount + " (Current pantry count: " + availableAmount + ")");
                ((MainActivity) context).addItemsToPantry(foodTypeId, missingAmount);
            }
        }
    }

    public void updateRequiredIngredients() {
        for (int i = 0; i < recipes.size(); i++) {
            notifyItemChanged(i, "ingredientsUpdate");
        }
    }

    public void refreshTab1Adapter() {
        Tab1 tab1 = (Tab1) fragmentMap.get(R.id.recipes);
        if (tab1 != null) {
            tab1.refreshRecipeAdapter();
        }
    }
}