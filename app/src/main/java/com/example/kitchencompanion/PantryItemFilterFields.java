package com.example.kitchencompanion;

import java.util.List;

public class PantryItemFilterFields {
    /*
        Note: booleans are only considered if they are true.
        A false boolean will not filter to things only not private or only not low
        Integer.MIN_VALUE for ints and null for objects means they aren't considered.
     */
    private String name;
    private boolean low;
    private boolean isPrivate;
    private int expirationMax;
    private List<Enums.FoodGroup> foodGroups;

    public PantryItemFilterFields() {
        name = null;
        low = false;
        isPrivate = false;
        expirationMax = Integer.MIN_VALUE;
        foodGroups = null;
    }

    public boolean matchName(String name) {
        if (this.name == null) { // Not considered
            return true;
        }

        return name.toLowerCase().trim().contains(this.name.toLowerCase().trim());
    }

    public boolean matchLow(boolean low) {
        if (this.low == false) { // Not considered
            return true;
        }
        return this.low == low;
    }

    public boolean matchPrivate(boolean isPrivate) {
        if (this.isPrivate == false) { // Not considered
            return true;
        }
        return this.isPrivate == isPrivate;
    }

    public boolean matchExpirationMax(int expirationMax) {
        if (this.expirationMax == Integer.MIN_VALUE) { // Not considered
            return true;
        }
        return expirationMax <= this.expirationMax;
    }

    public boolean matchFoodGroup(Enums.FoodGroup foodGroup) {
        if (this.foodGroups == null) { // Not considered
            return true;
        }

        for (Enums.FoodGroup group : this.foodGroups) {
            if (group == foodGroup) {
                return true;
            }
        }

        return false;
    }

    public void setName(String name) {
        this.name = (name.isEmpty()) ? null : name;
    }

    public String getName() {
        return name;
    }

    public void resetName() {
        name = null;
    }

    public void setLow(boolean low) {
        this.low = low;
    }

    public boolean getLow() {
        return low;
    }

    public void resetLow() {
        low = false;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public boolean getPrivate() {
        return isPrivate;
    }

    public void resetPrivate() {
        isPrivate = false;
    }

    public void setExpirationMax(int expirationMax) {
        this.expirationMax = expirationMax;
    }

    public int getExpirationMax() {
        return expirationMax;
    }

    public void resetExpirationMax() {
        expirationMax = Integer.MIN_VALUE;
    }

    public void setFoodGroups(List<Enums.FoodGroup> foodGroups) {
        this.foodGroups = (foodGroups.size() > 0) ? foodGroups : null;
    }

    public List<Enums.FoodGroup> getFoodGroups() {
        return foodGroups;
    }

    public void resetFoodGroups() {
        foodGroups = null;
    }
}
