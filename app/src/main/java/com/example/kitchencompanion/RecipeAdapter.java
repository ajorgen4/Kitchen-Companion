package com.example.kitchencompanion;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
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

    public RecipeAdapter(Context context, List<Recipe> recipes, RecipeDatabase recipeDatabase, List<PantryItem> pantryList, HashMap<Integer, FoodType> foodDictionary) {
        this.context = context;
        this.recipes = recipes;
        this.recipeDatabase = recipeDatabase;
        this.pantryList = pantryList;
        this.foodDictionary = foodDictionary;
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
        if (recipe.getDietaryAttributes() != null && !recipe.getDietaryAttributes().isEmpty()) {
            holder.warningIcon.setVisibility(View.VISIBLE);
        } else {
            holder.warningIcon.setVisibility(View.GONE);
        }

        // Additional log for icon visibility
        System.out.println("Icon Visibility for Recipe ID: " + recipe.getRecipeId() + ": " + holder.warningIcon.getVisibility());
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
            viewForeground = itemView.findViewById(R.id.recipeItemLayout);
            addMissingLayout = itemView.findViewById(R.id.addMissingLayout);

            addMissingButton = itemView.findViewById(R.id.addMissingButton); // Initialize the "Add Missing" button

            addMissingButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onAddMissingClicked(position);
                }
            });
        }

        private void onAddMissingClicked(int position) {
            Recipe recipe = recipes.get(position);
            System.out.println("DEBUG: onAddMissingClicked called for Recipe: " + recipe.getName());
            addMissingIngredients(recipe);
        }
    }

    private void showDescPopup(Context context, Recipe recipe, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.popup_recipe_desc, null);

        // Find TextViews
        TextView attributesTextView = dialogView.findViewById(R.id.attributesTextView);
        TextView ingredientsTextView = dialogView.findViewById(R.id.ingredientsTextView);
        TextView descriptionTextView = dialogView.findViewById(R.id.descriptionTextView);

        // Set the attributes
        StringBuilder attributesBuilder = new StringBuilder();
        for (Enums.DietaryAttribute attribute : recipe.getDietaryAttributes()) {
            if (attributesBuilder.length() > 0) attributesBuilder.append(", ");
            attributesBuilder.append(attribute.toString());
        }
        // Set the Attributes text with bold and underline for the label
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
            Map<Integer, Integer> ingredients = recipe.getRecipe_Requirements();
            Tab2 tab2 = ((MainActivity) context).getTab2(); // Assumes getTab2() is implemented in MainActivity
            for (Map.Entry<Integer, Integer> ingredient : ingredients.entrySet()) {
                int foodTypeId = ingredient.getKey();
                int requiredAmount = ingredient.getValue();
                FoodType foodType = tab2.getFoodDictionary().get(foodTypeId);
                if (foodType != null) {
                    tab2.removeItems(foodType, requiredAmount);
                }
            }
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

    private int calculateAvailableIngredients(Recipe recipe) {
        int availableIngredients = 0;
        int totalRequiredIngredients = 0;
        Map<Integer, Integer> recipeRequirements = recipe.getRecipe_Requirements();

        for (Map.Entry<Integer, Integer> requirement : recipeRequirements.entrySet()) {
            int ingredientId = requirement.getKey();
            int requiredAmount = requirement.getValue();
            totalRequiredIngredients += requiredAmount; // Sum up all required ingredient amounts

            // Find this ingredient in pantry
            for (PantryItem item : pantryList) {
                if (item.getType().getID() == ingredientId) {
                    // Count the available amount, not exceeding the required amount
                    availableIngredients += Math.min(item.totalCount(), requiredAmount);
                    break; // Skip to the next ingredient since we've found the current one
                }
            }
        }

        // The caller should use the return value to update the UI
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
}