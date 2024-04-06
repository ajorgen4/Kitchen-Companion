package com.example.kitchencompanion;
import java.util.List;
import java.util.ArrayList;
public class Settings {

    // Other settings fields...

    private List<String> allergies;

    public Settings() {
        allergies = new ArrayList<>();
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
}
