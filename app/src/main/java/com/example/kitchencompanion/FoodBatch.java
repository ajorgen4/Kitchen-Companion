package com.example.kitchencompanion;

import java.time.LocalDate;

// FoodBatch is the actual food. It represents a physical set of items with a shared type of food and expiration date in the user's pantry.
// Aggregates items Added during the same time into a singular batch to keep track of expiration date

public class FoodBatch {
    private static int nextBatchId = 0;
    private int batchId;
    private FoodType food;
    private int itemCount;
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

    public int removeItemCount(int count) {
        int prev = this.itemCount;
        this.itemCount = Math.max(0, this.itemCount - count); //Item count >= 0
        return (itemCount == 0) ? prev : count; // Return the number of items removed
    }
}