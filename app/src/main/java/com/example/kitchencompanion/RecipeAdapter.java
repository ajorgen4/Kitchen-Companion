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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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

    public RecipeAdapter(Context context, List<Recipe> recipes, RecipeDatabase recipeDatabase, List<PantryItem> pantryList) {
        this.context = context;
        this.recipes = recipes;
        this.recipeDatabase = recipeDatabase;
        this.pantryList = pantryList;
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
        String requiredIngredientsText = "Required Ingredients: " + availableIngredientsCount + "/" + recipe.getTotalIngredientCount();
        holder.recipeRequiredIngredients.setText(requiredIngredientsText);

        // Favorite Heart
        holder.favoriteIcon.setImageResource(recipe.isFavorited() ? R.drawable.heart_solid : R.drawable.heart_outline);


        holder.favoriteIcon.setOnClickListener(v -> {
            recipe.toggleFavorite();
            notifyItemChanged(position);
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
        TextView recipeName, recipeCalories, recipeCookTime, recipeDifficulty, recipeRequiredIngredients; // Added recipeRequiredIngredients
        ImageView closeButton, favoriteIcon, recipeImage, warningIcon;
        View viewForeground;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeCalories = itemView.findViewById(R.id.recipeCalories);
            recipeCookTime = itemView.findViewById(R.id.recipeCookTime);
            recipeDifficulty = itemView.findViewById(R.id.recipeDifficulty);
            closeButton = itemView.findViewById(R.id.closeButtonRecipes);
            favoriteIcon = itemView.findViewById(R.id.favoriteRecipeItemButton);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            warningIcon = itemView.findViewById(R.id.warningIcon);
            viewForeground = itemView.findViewById(R.id.recipeItemLayout);
            recipeRequiredIngredients = itemView.findViewById(R.id.recipeRequiredIngredients);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Recipe recipe = recipes.get(position);
                    showDescPopup(context, recipe);
                }
            });
        }
    }

    private void showDescPopup(Context context, Recipe recipe) {
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

        // Tutorial Used for 3 textviews: https://stackoverflow.com/questions/37661755/how-to-have-bold-and-normal-text-in-same-textview-in-android
        // Set the Attributes
        String label = "Recipe Dietary Attributes/Allergens: \n";
        String attributes = attributesBuilder.toString();
        SpannableString spannable = new SpannableString(label + attributes);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, label.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new UnderlineSpan(), 0, label.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        attributesTextView.setText(spannable);
        String ingredientsLabel = "Ingredients: \n";
        String ingredientsText = recipeDatabase.printIngredientsForRecipe(recipe.getRecipeId());
        SpannableString ingredientsSpannable = new SpannableString(ingredientsLabel + ingredientsText);
        ingredientsSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, ingredientsLabel.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ingredientsSpannable.setSpan(new UnderlineSpan(), 0, ingredientsLabel.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ingredientsTextView.setText(ingredientsSpannable);
        String instructionsLabel = "Recipe Instructions: ";
        String instructionsText = recipe.getDescription();
        SpannableString instructionsSpannable = new SpannableString(instructionsLabel + instructionsText);

        instructionsSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, instructionsLabel.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        instructionsSpannable.setSpan(new UnderlineSpan(), 0, instructionsLabel.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        descriptionTextView.setText(instructionsSpannable);


        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Close popup button
        dialogView.findViewById(R.id.rightCloseButton).setOnClickListener(v -> dialog.dismiss());

        // Mark cooked button
        dialogView.findViewById(R.id.markCookedButton).setOnClickListener(v -> {
            // Implement later
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
        Map<Integer, Integer> recipeRequirements = recipe.getRecipe_Requirements();

        for (Map.Entry<Integer, Integer> requirement : recipeRequirements.entrySet()) {
            int ingredientId = requirement.getKey();
            int requiredAmount = requirement.getValue();

            // Find this ingredient in pantry
            for (PantryItem item : pantryList) {
                if (item.getType().getID() == ingredientId) {
                    //  ensure count <= required amount
                    availableIngredients += Math.min(item.totalCount(), requiredAmount);
                    break; // skip to next ingredient
                }
            }
        }
        return availableIngredients;
    }


}
