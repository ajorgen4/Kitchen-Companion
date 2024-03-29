package com.example.kitchencompanion;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ShopListAdapter extends BaseAdapter {
    private List<String> shopList;
    private Context context;
    private boolean shopMode = false;

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
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.shopping_list_item, null);
        }
        /*Creates a reference to every needed item in the tab that is needed*/
        TextView foodName = itemView.findViewById(R.id.shopListFoodName);
        TextView quantity = itemView.findViewById(R.id.shopListQuantityEditText);
        ImageView close = itemView.findViewById(R.id.closeShopListItemButton);
        ImageView push = itemView.findViewById(R.id.pushShopListItemButton);
        ImageView plus = itemView.findViewById(R.id.shopListQuantityPlusButton);
        ImageView minus = itemView.findViewById(R.id.shopListQuantityMinusButton);
        foodName.setText(shopList.get(position));
        quantity.setText("1");/*Replace with the food amount*/
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
        itemView.setOnClickListener(new View.OnClickListener() {
            boolean select = false;
            @Override
            public void onClick(View v) {
                if(shopMode){
                    if(select){
                        select = false;
                        foodName.setPaintFlags(foodName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        push.setVisibility(View.GONE);
                    }
                    else {
                        select = true;
                        foodName.setPaintFlags(foodName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        push.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        return itemView;
    }

    public void changeMode(){
        shopMode = !shopMode;
    }
}
