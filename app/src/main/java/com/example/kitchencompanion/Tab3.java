package com.example.kitchencompanion;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

// Shopping tab
public class Tab3 extends Fragment {

    private ListView shopListView;
    private View view;
    private ShopListAdapter adapter;
    private List<ShopListItem> shopList;
    // Reference to the set of all FoodType objects in the app.
    // Maps FoodType.getID() to FoodType
    HashMap<Integer, FoodType> foodDictionary;

    private boolean shoppingMode = false;
    public Tab3(HashMap<Integer, FoodType> foodDictionary){
        this.foodDictionary = foodDictionary;

        shopList = new ArrayList<ShopListItem>();
        shopList.add(new ShopListItem("Chicken Legs", 3));
        shopList.add(new ShopListItem("Banana", 4));
        shopList.add(new ShopListItem("Donuts", 2));
        shopList.add(new ShopListItem("Kale", 1));
        shopList.add(new ShopListItem("Pasta", 3));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tab3, container, false);

        shopListView = view.findViewById(R.id.shopListView);
        adapter = new ShopListAdapter(getContext(), shopList);
        shopListView.setAdapter(adapter);
        Button submitShop = view.findViewById(R.id.submitListButton);
        Button shopMode = view.findViewById(R.id.buyModeButton);
        FloatingActionButton addbutton = view.findViewById(R.id.addShopListFoodButton);
        shopMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!adapter.anySelected()) {
                    shoppingMode = !shoppingMode;
                    adapter.changeMode();
                    if (shoppingMode) {
                        shopMode.setText("Edit List");
                        submitShop.setVisibility(View.VISIBLE);
                    } else {
                        shopMode.setText("Shopping");
                        submitShop.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }
                else{
                    /*do a popup or smtn here for things are selected*/
                }
            }
        });
        submitShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getAndRemoveSelectedItems();

                /*add pushing to pantry functionality here*/
            }
        });
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_shoplist_dialog, null);

                // Find and set up the views inside the dialog layout
                // ...
                Button cancelButton = dialogView.findViewById(R.id.shopListAddFoodCancelButton);
                Button addButton = dialogView.findViewById(R.id.shopListAddFoodCreateButton);
                EditText amount = dialogView.findViewById(R.id.shopListAddFoodCountInput);


                builder.setView(dialogView);

                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                // Set the dialog to occupy approximately 75% of the screen
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = (int) (displayMetrics.widthPixels * 0.75);
                int height = (int) (displayMetrics.heightPixels * 0.75);
                dialog.getWindow().setLayout(width, height);

                // Dim the background
                dialog.getWindow().setDimAmount(0.5f);

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*adapter.addShopListItem(new ShopListItem(,amount));*/
                    }
                });

                dialog.show();
            }
        });
        return view;
    }
}