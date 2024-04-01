package com.example.kitchencompanion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
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
        holder.recipeName.setText(recipe.getName());
        holder.recipeCookTime.setText("Cook Time: " + recipe.getCookTime());
        holder.recipeCalories.setText("Calories: " + String.valueOf(recipe.getCalories()));
        holder.recipeDifficulty.setText("Difficulty: " + recipe.getDifficulty());
        holder.favoriteIcon.setImageResource(recipe.isFavorited() ? R.drawable.heart_solid : R.drawable.heart_outline);

        holder.favoriteIcon.setOnClickListener(v -> {
            recipe.toggleFavorite();
            notifyItemChanged(position);
        });

        holder.closeButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            recipes.remove(currentPosition);
            notifyItemRemoved(currentPosition);
            notifyItemRangeChanged(currentPosition, recipes.size());
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
        TextView recipeName, recipeCalories, recipeCookTime, recipeDifficulty;
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

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                Recipe recipe = recipes.get(position);
                showDescPopup(context, recipe.getDescription(), recipe.getDietaryAttributes());
            });
        }
    }

    private void showDescPopup(Context context, String description, List<Enums.DietaryAttribute> attributes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.popup_recipe_desc, null);

        TextView attributesTextView = dialogView.findViewById(R.id.attributesTextView); // Ensure this TextView is in popup_recipe_desc.xml
        TextView descriptionTextView = dialogView.findViewById(R.id.descriptionTextView);

        // Convert attributes to String
        StringBuilder attributesBuilder = new StringBuilder();
        for(Enums.DietaryAttribute attribute : attributes) {
            if(attributesBuilder.length() > 0) attributesBuilder.append(", ");
            attributesBuilder.append(attribute.toString());
        }
        attributesTextView.setText("Attributes: " + attributesBuilder.toString());
        descriptionTextView.setText("Steps: " + description);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels * 0.75);
        int height = (int) (displayMetrics.heightPixels * 0.75);
        dialog.getWindow().setLayout(width, height);

        dialog.show();
    }
}
