package com.example.kitchencompanion;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShopListAdapter extends BaseAdapter {
    private List<ShopListItem> shopList;
    private Context context;
    private boolean shopMode;

    public ShopListAdapter(Context context, List<ShopListItem> shopList, boolean shopMode){
        this.context = context;
        this.shopList = shopList;
        this.shopMode = shopMode;
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
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.shopping_list_item, parent,false);
        }
        /*Creates a reference to every needed item in the tab that is needed*/
        TextView foodName = itemView.findViewById(R.id.shopListFoodName);
        TextView quantity = itemView.findViewById(R.id.shopListQuantityEditText);
        ImageView close = itemView.findViewById(R.id.closeShopListItemButton);
        ImageView pushIcon = itemView.findViewById(R.id.pushShopListItemButton);
        ImageView plus = itemView.findViewById(R.id.shopListQuantityPlusButton);
        ImageView minus = itemView.findViewById(R.id.shopListQuantityMinusButton);
        foodName.setText(shopList.get(position).getName());
        quantity.setText(String.valueOf(shopList.get(position).getAmount()));
        if(shopMode){
            close.setVisibility(View.GONE);
            plus.setVisibility(View.INVISIBLE);
            minus.setVisibility(View.INVISIBLE);
        }
        else{
            close.setVisibility(View.VISIBLE);
            plus.setVisibility(View.VISIBLE);
            minus.setVisibility(View.VISIBLE);
        }
        if(shopList.get(position).getSelected()){
            foodName.setPaintFlags(foodName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            pushIcon.setVisibility(View.VISIBLE);
        }
        else{
            foodName.setPaintFlags(foodName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            pushIcon.setVisibility(View.GONE);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shopMode){
                    shopList.get(position).clicked();
                    notifyDataSetChanged();
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (plus.getVisibility() == View.VISIBLE) {
                    shopList.get(position).addAmount();
                    quantity.setText(String.valueOf(shopList.get(position).getAmount()));
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minus.getVisibility() == View.VISIBLE) {
                    if(shopList.get(position).getAmount() <= 1){
                        shopList.remove(position);
                        notifyDataSetChanged();
                    }
                    else {
                        shopList.get(position).subtractAmount();
                        quantity.setText(String.valueOf(shopList.get(position).getAmount()));
                    }
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minus.getVisibility() == View.VISIBLE) {
                    shopList.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        return itemView;
    }

    public void changeMode(){shopMode = !shopMode;}
    public boolean anySelected(){
        boolean anyItemSelected = false;
        for(int i = 0; i < shopList.size(); i++){
            if(shopList.get(i).getSelected()){
                anyItemSelected = true;
            }
        }
        return anyItemSelected;
    }
    public List<ShopListItem> getAndRemoveSelectedItems(){
        List<ShopListItem> selectedItems = new ArrayList<ShopListItem>();
        for(int i = 0; i < shopList.size(); i++){
            if(shopList.get(i).getSelected()){
                selectedItems.add(shopList.get(i));
            }
        }
        shopList.removeAll(selectedItems);
        notifyDataSetChanged();
        return selectedItems;
    }

    public void addShopListItem(ShopListItem item){
        boolean exists = false;
        for(ShopListItem compared : shopList){
            if(item.getName().equals(compared.getName())){
                exists = true;
                compared.setAmount(item.getAmount());
            }
        }
        if(!exists) {
            shopList.add(item);
        }
        notifyDataSetChanged();
    }

    public void addShopListItemBatch(List<ShopListItem> list){
        for(ShopListItem item:list) {
            boolean exists = false;
            for(ShopListItem compared : shopList){
                if(item.getName().equals(compared.getName())){
                    exists = true;
                    compared.setAmount(item.getAmount() + compared.getAmount());
                }
            }
            if(!exists) {
                shopList.add(item);
            }
        }
        notifyDataSetChanged();
    }
}
