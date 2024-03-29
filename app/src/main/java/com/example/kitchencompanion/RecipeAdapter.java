package com.example.kitchencompanion;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
    // https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.Adapter
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.recipeName.setText(recipe.getName());
        holder.recipeCookTime.setText(recipe.getCookTime());
        holder.recipeCalories.setText(String.valueOf(recipe.getCalories()));
        holder.recipeDifficulty.setText(recipe.getDifficulty());

        holder.favoriteIcon.setImageResource(recipe.isFavorited() ?
                R.drawable.heart_solid :
                R.drawable.heart_outline);

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

        // Load image for recipe
        int imageResourceId = context.getResources().getIdentifier(
                recipe.getImageFile(),
                "drawable",
                context.getPackageName()
        );

        if (imageResourceId > 0) { // Range check
            holder.recipeImage.setImageResource(imageResourceId);
        } else { // If fail, sue default
            holder.recipeImage.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName, recipeCalories, recipeCookTime, recipeDifficulty;
        ImageView closeButton, favoriteIcon, recipeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeCalories = itemView.findViewById(R.id.recipeCalories);
            recipeCookTime = itemView.findViewById(R.id.recipeCookTime);
            recipeDifficulty = itemView.findViewById(R.id.recipeDifficulty);
            closeButton = itemView.findViewById(R.id.closeButtonRecipes);
            favoriteIcon = itemView.findViewById(R.id.favoriteRecipeItemButton);
            recipeImage = itemView.findViewById(R.id.recipeImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Recipe recipe = recipes.get(position);
                    showDescPopup(context, recipe.getDescription());
                }
            });
        }
    }

    // https://stackoverflow.com/questions/34383763/how-to-open-a-popup-window-from-an-adapter-class
    // https://stackoverflow.com/questions/36661775/android-popup-window-with-variable-text
    private void showDescPopup(Context context, String description) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_recipe_desc, null, false);
        PopupWindow popupWindow = new PopupWindow(layout, 300, 190, true);
        TextView descriptionTextView = layout.findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(description);
        ImageView closeButton = layout.findViewById(R.id.closeButtonRecipePopup);
        closeButton.setOnClickListener(v -> popupWindow.dismiss());
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }


}