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
    private List<String> shopList;

    private boolean shoppingMode = false;
    public Tab3(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tab3, container, false);

        shopList = new ArrayList<>();
        shopList.add("Apple");
        shopList.add("Banana");
        shopList.add("Orange");

        shopListView = view.findViewById(R.id.shopListView);
        adapter = new ShopListAdapter(getContext(), shopList);
        shopListView.setAdapter(adapter);
        Button submitShop = view.findViewById(R.id.submitListButton);
        Button shopMode = view.findViewById(R.id.buyModeButton);
        shopMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingMode = !shoppingMode;
                adapter.changeMode();
                if(shoppingMode){
                    shopMode.setText("Edit List");
                    submitShop.setVisibility(View.VISIBLE);
                }
                else{
                    shopMode.setText("Shopping");
                    submitShop.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }
}