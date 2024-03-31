package com.example.kitchencompanion;

public class ShopListItem {

    private String name;
    private int amount;
    private boolean selected;

    public ShopListItem(String name, int amount){
        this.name = name;
        this.amount = amount;
        this.selected = false;
    }

    public void clicked(){selected = !selected;}

    public void addAmount(){amount += 1;}

    public void subtractAmount(){amount -= 1;}
    public boolean getSelected(){return selected;}
    public int getAmount(){return amount;}
    public String getName(){return name;}
}
