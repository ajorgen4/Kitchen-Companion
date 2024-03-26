package com.example.kitchencompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ShopListAdapter extends BaseAdapter {
    private List<String> shopList;
    private Context context;
    public ShopListAdapter(Context context, List<String> shopList){
        this.context = context;
        this.shopList = shopList;
    }
    @Override
    public int getCount() {
        return shopList.size();
    }

    @Override
    public Object getItem(int position) {
        return shopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.shopping_list_item, null);
        TextView foodName = convertView.findViewById(R.id.shopListFoodName);
        foodName.setText(shopList.get(position));
        return convertView;
    }
}
