package com.example.kitchencompanion;

import android.content.Context;
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


public class RecipeAdapter extends RecyclerView.Adapter < RecipeAdapter.ViewHolder > {

    private List < Recipe > recipes;
    private Context context;

    public RecipeAdapter(Context context, List < Recipe > recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);
    }

    // https://www.javatpoint.com/android-recyclerview-list-example
    // https://abhiandroid.com/materialdesign/recyclerview#gsc.tab=0
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.recipeName.setText(recipe.getName());
        holder.recipeCookTime.setText(recipe.getCookTime());
        holder.recipeCalories.setText(String.valueOf(recipe.getCalories()));
        holder.recipeDifficulty.setText(recipe.getDifficulty());

        holder.favoriteIcon.setImageResource(recipe.isFavorited() ?
                android.R.drawable.btn_star_big_on :
                android.R.drawable.btn_star_big_off);

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
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName, recipeCalories, recipeCookTime, recipeDifficulty;
        ImageView closeButton, favoriteIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeCalories = itemView.findViewById(R.id.recipeCalories);
            recipeCookTime = itemView.findViewById(R.id.recipeCookTime);
            recipeDifficulty = itemView.findViewById(R.id.recipeDifficulty);
            closeButton = itemView.findViewById(R.id.closeButton);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
        }
    }
}