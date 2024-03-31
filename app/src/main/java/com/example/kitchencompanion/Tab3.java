package com.example.kitchencompanion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

// Shopping tab
public class Tab3 extends Fragment {

    private ListView shopListView;
    private View view;
    private ShopListAdapter adapter;
    private List<ShopListItem> shopList;

    private boolean shoppingMode = false;
    public Tab3(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tab3, container, false);

        shopList = new ArrayList<ShopListItem>();
        shopList.add(new ShopListItem("Chicken Legs", 3));
        shopList.add(new ShopListItem("Banana", 4));
        shopList.add(new ShopListItem("Donuts", 2));
        shopList.add(new ShopListItem("Kale", 1));
        shopList.add(new ShopListItem("Pasta", 3));

        shopListView = view.findViewById(R.id.shopListView);
        adapter = new ShopListAdapter(getContext(), shopList);
        shopListView.setAdapter(adapter);
        Button submitShop = view.findViewById(R.id.submitListButton);
        Button shopMode = view.findViewById(R.id.buyModeButton);
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
        return view;
    }
}