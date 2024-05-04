package com.example.kitchencompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ShopListPushPresetAdapter extends BaseAdapter {
    private List<ShopListItem> shopList;
    private Context context;
    public ShopListPushPresetAdapter(Context context){
        this.context = context;
        this.shopList = new ArrayList<ShopListItem>();
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

    public void changeList(List<ShopListItem> shopList){
        this.shopList = shopList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.shoplist_preset_list_item, parent,false);
        }
        TextView itemAmount = itemView.findViewById(R.id.shopListPresetAmount);
        TextView itemName = itemView.findViewById(R.id.shopListPresetFoodName);
        itemAmount.setText(String.valueOf(shopList.get(position).getAmount()));
        itemName.setText(shopList.get(position).getName());
        return itemView;
    }
}
