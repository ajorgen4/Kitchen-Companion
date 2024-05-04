package com.example.kitchencompanion;

import android.app.AlertDialog;
import android.graphics.Canvas;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.ViewGroup;
import android.os.Handler;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class Tab1 extends Fragment {
    private FloatingActionButton addRecipeButton;
    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;
    // Reference to the set of all FoodType objects in the app
    // Maps FoodType.getID() to FoodType
    HashMap<Integer, FoodType> foodDictionary;
    private boolean isAddMissingPopupShown = false;

    private List<PantryItem> pantryList;
    private Tab3 tab3;

    public Tab1(HashMap<Integer, FoodType> foodDictionary, RecipeDatabase recipeDatabase, List<PantryItem> pantryList, Tab3 tab3) {
        this.foodDictionary = foodDictionary;
        this.recipeDatabase = recipeDatabase;
        this.pantryList = pantryList;
        this.tab3 = tab3;
    }


    private RecipeDatabase recipeDatabase;

    // General info I used for views: https://developer.android.com/reference/android/app/Fragment
    // OnCreateView definition: https://stackoverflow.com/questions/43780548/how-oncreateview-works
    // YOUTUBE TUTORIAL, SWIPE RECYCLER VIEW: https://www.youtube.com/watch?v=rcSNkSJ624U
    // OnCreateView: fragment layout inflation
    // onCreate: fragment init, no view

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addRecipeButton = view.findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(v -> showAddRecipeDialog());

        // Use shared RecipeDatabase instance across tabs
        Map<Integer, Fragment> fragmentMap = ((MainActivity) getActivity()).getFragmentMap();
        recipeAdapter = new RecipeAdapter(getContext(), recipeDatabase.getRecipes(), recipeDatabase, pantryList, foodDictionary, fragmentMap, tab3);
        recipeRecyclerView.setAdapter(recipeAdapter);

        setUpItemTouchHelper();

        return view;
    }

    //https://developer.android.com/reference/androidx/recyclerview/widget/ItemTouchHelper
    private void setUpItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // don't think we need anything here for now
                // actions in onChildDraw
            }

            // Tutorial for onChildDraw: https://developer.android.com/reference/androidx/recyclerview/widget/ItemTouchHelper.Callback
            // Swipe recyler: https://stackoverflow.com/questions/57353844/how-to-restore-recycler-view-item-after-swipe
            // Swipe click issue? (Scrapped - workaround) https://stackoverflow.com/questions/39189159/recyclerview-swipe-with-a-view-below-not-detecting-click
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float xVal, float yVal, int actionState, boolean isCurrentlyActive) {
                RecipeAdapter.ViewHolder holder = (RecipeAdapter.ViewHolder) viewHolder;
                FrameLayout addMissingLayout = holder.addMissingLayout;
                final View foregroundView = holder.viewForeground;
                float maxSwipeDistance = -addMissingLayout.getWidth();
                float restrictedDX = Math.max(xVal, maxSwipeDistance);
                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, restrictedDX, yVal, actionState, isCurrentlyActive);

                // Check if a dialog is already shown
                if (restrictedDX == maxSwipeDistance && isCurrentlyActive && !((RecipeAdapter) recyclerView.getAdapter()).isDialogShown()) {
                    addMissingLayout.setVisibility(View.VISIBLE);
                    if (!holder.isHandlerRunning && !holder.isPopupShown) {
                        holder.isHandlerRunning = true;
                        new Handler().postDelayed(() -> {
                            if (restrictedDX == maxSwipeDistance && isCurrentlyActive && !holder.isPopupShown) {
                                holder.isPopupShown = true;
                                ((RecipeAdapter) recyclerView.getAdapter()).showAddMissingConfirmation(holder.getAdapterPosition());
                            }
                            holder.isHandlerRunning = false;
                            if (!isCurrentlyActive) {
                                holder.isPopupShown = false;
                            }
                        }, 300); // Adjust delay as needed
                    }
                } else if (restrictedDX != maxSwipeDistance) {
                    addMissingLayout.setVisibility(View.GONE);
                    holder.isHandlerRunning = false;
                    holder.isPopupShown = false;
                }
            }



            // Restoring recycler view(clearView, onSelectedChange): https://stackoverflow.com/questions/57353844/how-to-restore-recycler-view-item-after-swipe
            // https://stackoverflow.com/questions/39189159/recyclerview-swipe-with-a-view-below-not-detecting-click
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View foregroundView = ((RecipeAdapter.ViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().clearView(foregroundView);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    final View foregroundView = ((RecipeAdapter.ViewHolder) viewHolder).viewForeground;
                    getDefaultUIUtil().onSelected(foregroundView);
                }
            }
        }).attachToRecyclerView(recipeRecyclerView);
    }

    // Unimplemented - Horizontal prototype - UI Only
    private void showAddRecipeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_recipe_dialog, null);
        Button cancelButton = dialogView.findViewById(R.id.recipeAddCloseButton);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DisplayMetrics displayMetrics = new DisplayMetrics();  // https://developer.android.com/reference/android/util/DisplayMetrics, Get info about screen size, etc
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // popup =  75% of screen width/height
        dialog.getWindow().setLayout((int) (displayMetrics.widthPixels * 0.75), (int) (displayMetrics.heightPixels * 0.75));
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    // Ensure Tab1 refreshes if we change values for the recipeCard like by Marking Cooked, etc
    // notifyDataSetChanges(): https://stackoverflow.com/questions/2345875/android-notifydatasetchanged
    public void refreshRecipeAdapter() {
        if (recipeAdapter != null) {
            recipeAdapter.notifyDataSetChanged();
        }
    }

    // https://developer.android.com/images/activity_lifecycle.png
    @Override
    public void onResume() {
        super.onResume();
        if (recipeAdapter != null) {
            recipeAdapter.updateRecipes(recipeDatabase.getRecipes());
        }
    }
}