package com.example.kitchencompanion;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

// Tracks items user has across all batches into 1 object per food item
// PantryItem is essentially a shell for a set of FoodBatches.

public class PantryItem {
    private Set<FoodBatch> batches;
    private String itemName;
    private boolean low;
    private FoodType type;

    public PantryItem(FoodBatch batch) {
        this.batches = new HashSet<>();
        // Add the "founding" batch, the first batch of this item type added to the pantry
        this.batches.add(batch);
        this.itemName = batch.getFoodType().getItemName();
        this.low = false; // by default, assumed to not be low
        this.type = batch.getFoodType();
    }

    // Removes items from batches in order of expiration date
    public void removeItemCount(int count) {
        do {
            FoodBatch nextExpiringBatch = findnextExpiringBatch();

            if (nextExpiringBatch != null) {
                count -= nextExpiringBatch.removeItemCount(count);
                if (count != 0) {
                    batches.remove(nextExpiringBatch);
                }
            } else {
                // We have run out of items to remove
                break;
            }
        } while (count != 0); // repeat until all items removed
    }

    // Finds the batch in this Pantry Item with the closest expiration date
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

    public void addBatch(FoodBatch batch) {
        this.batches.add(batch);
    }

    public FoodType getType() {
        return type;
    }

    public void printBatches() {
        for (FoodBatch batch : batches) {
            System.out.println("Batch ID: " + batch.getBatchId() + " - Count: " + batch.getItemCount() + ", Expiration Date: " + batch.getExpirationDate());
        }
    }

    public void setLow(boolean low) {
        this.low = low;
    }
}