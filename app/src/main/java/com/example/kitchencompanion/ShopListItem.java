package com.example.kitchencompanion;

public class ShopListItem {

    private FoodType foodType;
    private int amount;
    private boolean selected;

    public ShopListItem(FoodType foodType, int amount){
        this.foodType = foodType;
        this.amount = amount;
        this.selected = false;
    }

    public void clicked(){selected = !selected;}

    public void addAmount(){amount += 1;}

    public void subtractAmount(){amount -= 1;}
    public boolean getSelected(){return selected;}
    public int getAmount(){return amount;}
    public String getName(){return foodType.getItemName();}
    public FoodType getFood(){return foodType;}
}
