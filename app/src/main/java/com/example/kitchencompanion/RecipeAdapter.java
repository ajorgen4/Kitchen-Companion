package com.example.kitchencompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final List<Recipe> recipes;
    private final LayoutInflater inflater;

    RecipeAdapter(Context context, List<Recipe> recipes) {
        this.inflater = LayoutInflater.from(context);
        this.recipes = recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName;
        TextView recipeCalories;
        TextView recipeCookTime;
        TextView recipeDifficulty;
        TextView ingredientsCount;
        ImageView favoriteIcon;
        ImageView closeButton;

        RecipeViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeCalories = itemView.findViewById(R.id.recipeCalories);
            recipeCookTime = itemView.findViewById(R.id.recipeCookTime);
            recipeDifficulty = itemView.findViewById(R.id.recipeDifficulty);
            ingredientsCount = itemView.findViewById(R.id.ingredientsCount);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
            closeButton = itemView.findViewById(R.id.closeButton);
        }

        void bind(Recipe recipe) {
            recipeName.setText(recipe.getName());
            recipeCalories.setText(String.format("%d Calories", recipe.getCalories()));
            recipeCookTime.setText(String.format("%s", recipe.getCookTime()));
            recipeDifficulty.setText(String.format("%s", recipe.getDifficulty()));
            ingredientsCount.setText("Ingredients Placeholder");
            favoriteIcon.setImageResource(R.drawable.heart_outline);  //add logic for ifFavorited()
            closeButton.setImageResource(R.drawable.x_icon);
        }
    }

}
