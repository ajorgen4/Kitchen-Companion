package com.example.kitchencompanion;

import java.time.LocalDate;

// FoodBatch is the actual food. It represents a physical set of items with a shared type of food and expiration date in the user's pantry.
// Aggregates items Added during the same time into a singular batch to keep track of expiration date

public class FoodBatch {
    private static int nextBatchId = 1000;  // Start from 1000, arbitrary choice
    private int batchId;
    private FoodType food;
    int itemCount;
    private LocalDate expirationDate;

    public FoodBatch(FoodType food, int itemCount, LocalDate expirationDate) {
        this.batchId = nextBatchId++;  // For each new batch, assign sequentially the batch ID
        this.food = food;
        this.itemCount = itemCount;
        this.expirationDate = expirationDate;
    }

    public int getBatchId() {
        return batchId;
    }

    public FoodType getFoodType() {
        return food;
    }

    public int getItemCount() {
        return itemCount;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void removeItemCount(int count) {
        this.itemCount = Math.max(0, this.itemCount - count); //Item count >= 0
    }
}