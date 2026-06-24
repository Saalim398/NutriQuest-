package com.nutriquest.model;

import java.util.List;

public class Question {
    private final String query;
    private final List<String> choices;
    private final int correctIndex;
    private final String imagePath;
    private final String trivia;
    private final int level;
    private final FoodType foodType;

    public Question(String query, List<String> choices, int correctIndex, String imagePath, String trivia, int level, FoodType foodType) {
        this.query = query;
        this.choices = choices;
        this.correctIndex = correctIndex;
        this.imagePath = imagePath;
        this.trivia = trivia;
        this.level = level;
        this.foodType = foodType;
    }

    public String getQuery() {
        return query;
    }

    public List<String> getChoices() {
        return choices;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getTrivia() {
        return trivia;
    }

    public int getLevel() {
        return level;
    }

    public FoodType getFoodType() {
        return foodType;
    }
}
