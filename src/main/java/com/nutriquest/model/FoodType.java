package com.nutriquest.model;

public enum FoodType {
    GOOD_FOOD("Healthy & Nutritious", "#2ecc71"),
    BAD_FOOD("Unhealthy (Eat in Moderation)", "#e74c3c");

    private final String displayName;
    private final String colorHex;

    FoodType(String displayName, String colorHex) {
        this.displayName = displayName;
        this.colorHex = colorHex;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColorHex() {
        return colorHex;
    }
}
