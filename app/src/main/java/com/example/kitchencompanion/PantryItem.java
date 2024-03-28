package com.example.kitchencompanion;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

// Tracks items user has across all batches into 1 object per food item
// PantryItem is essentially a shell for a set of FoodBatches.

public class PantryItem {
    private Set<FoodBatch> batches;
    private String itemName;

    public PantryItem(FoodBatch batch) {
        this.batches = new HashSet<>();
        // Add the "founding" batch, the first batch of this item type added to the pantry
        this.batches.add(batch);
        this.itemName = batch.getFoodType().getItemName();
    }

    public void addBatch(FoodBatch batch) {
        this.batches.add(batch);
    }

    // (PantryStorageObject).removeItem looks for the (PantryItem) object with the matching name, if found call (PantryItems)'s removeItemCount to lower count
    public void removeItemCount(int count) {
        FoodBatch nextExpiringBatch = findnextExpiringBatch();
        if (nextExpiringBatch != null && nextExpiringBatch.getItemCount() >= count) {
            System.out.println("Removing " + count + " from batch ID: " + nextExpiringBatch.getBatchId() + " with expiration date: " + nextExpiringBatch.getExpirationDate());
            nextExpiringBatch.removeItemCount(count);
            if (nextExpiringBatch.getItemCount() == 0) {
                System.out.println("Batch ID: " + nextExpiringBatch.getBatchId() + " is now empty and will be removed.");
                batches.remove(nextExpiringBatch);
            }
        }
    }

    // Finds the batch in this Pantry Item with the expiration date closest expiration date
    private FoodBatch findnextExpiringBatch() {
        FoodBatch nextExpiringBatch = null;
        for (FoodBatch batch : batches) {
            if (nextExpiringBatch == null || batch.getExpirationDate().isBefore(nextExpiringBatch.getExpirationDate()) ||
                    (batch.getExpirationDate().isEqual(nextExpiringBatch.getExpirationDate()) && batch.getBatchId() < nextExpiringBatch.getBatchId())) {
                nextExpiringBatch = batch;
            }
        }
        return nextExpiringBatch;
    }

    public int totalCount() {
        int total = 0;
        for (FoodBatch batch : batches) {
            total += batch.getItemCount();
        }
        return total;
    }

    public String getItemName() {
        return itemName;
    }

    public Set<FoodBatch> getBatches() {
        return new HashSet<>(batches);
    }

    public void printBatches() {
        for (FoodBatch batch : batches) {
            System.out.println("Batch ID: " + batch.getBatchId() + " - Count: " + batch.getItemCount() + ", Expiration Date: " + batch.getExpirationDate());
        }
    }
}