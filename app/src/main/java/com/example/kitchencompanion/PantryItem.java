package com.example.kitchencompanion;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

// Tracks items user has across all batches into 1 object per food item
// PantryItem is essentially a shell for a set of FoodBatches.

public class PantryItem {
    private Set<FoodBatch> batches;
    private String itemName;
    private boolean low;
    private boolean isPrivate;
    private FoodType type;

    public PantryItem(FoodBatch batch, boolean isPrivate) {
        this.batches = new HashSet<>();
        // Add the "founding" batch, the first batch of this item type added to the pantry
        this.batches.add(batch);
        this.itemName = batch.getFoodType().getItemName();
        this.low = false; // by default, assumed to not be low
        this.type = batch.getFoodType();
        this.isPrivate = isPrivate;
    }

    // Removes items from batches in order of expiration date
    public void removeItemCount(int count) {
        do {
            FoodBatch nextExpiringBatch = findNextExpiringBatch();

            if (nextExpiringBatch != null) {
                count -= nextExpiringBatch.removeItemCount(count);
                if (count != 0 || nextExpiringBatch.getItemCount() == 0) {
                    batches.remove(nextExpiringBatch);
                }
            } else {
                // We have run out of items to remove
                break;
            }
        } while (count != 0); // repeat until all items removed
    }

    // Finds the batch in this Pantry Item with the closest expiration date
    private FoodBatch findNextExpiringBatch() {
        FoodBatch nextExpiringBatch = null;

        for (FoodBatch batch : batches) {
            if (nextExpiringBatch == null || batch.getExpirationDate().isBefore(nextExpiringBatch.getExpirationDate()) ||
                    (batch.getExpirationDate().isEqual(nextExpiringBatch.getExpirationDate()) && batch.getBatchId() < nextExpiringBatch.getBatchId())) {
                nextExpiringBatch = batch;
            }
        }

        return nextExpiringBatch;
    }

    public LocalDate nextExpiration() {
        return findNextExpiringBatch().getExpirationDate();
    }

    public int totalCount() {
        int total = 0;
        for (FoodBatch batch : batches) {
            total += batch.getItemCount();
        }
        return total;
    }

    public int getCount() {
        return totalCount();
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

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public boolean equalTo(PantryItem other) {
        return (this.type == other.type) && (this.isPrivate == other.isPrivate) && (this.low == other.low);
    }

    public void setLow(boolean low) {
        this.low = low;
    }

    public boolean getLow() {
        return low;
    }
}