package com.nutriquest.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameSession {
    private int score = 0;
    private int lives = 3;
    private int streak = 0;
    private int maxStreak = 0;
    private int currentLevel = 1;
    private int currentQuestionIndex = 0;
    private final List<String> badges = new ArrayList<>();
    private List<Question> currentQuestions = new ArrayList<>();

    private static final List<Question> QUESTION_BANK = Arrays.asList(
        // Level 1: Healthy or Not?
        new Question(
            "Is an Apple a healthy snack or an unhealthy treat?",
            Arrays.asList("Healthy Snack", "Unhealthy Treat"),
            0,
            "apple.png",
            "Apples contain lots of fiber that keeps your tummy happy, and they can float in water because 25% of their volume is actually air!",
            1,
            FoodType.GOOD_FOOD
        ),
        new Question(
            "How should we eat fast-food burgers?",
            Arrays.asList("Every single day", "Only once in a while (Moderation)", "Never ever", "For breakfast, lunch, and dinner"),
            1,
            "burger.png",
            "Fast-food burgers are high in sodium and saturated fats. Eating them too often can make you feel sluggish instead of energized.",
            1,
            FoodType.BAD_FOOD
        ),
        new Question(
            "Bananas are a great natural snack. What do they give you?",
            Arrays.asList("A quick sugar crash", "Instant energy and Potassium", "No nutrients at all", "Bad breath"),
            1,
            "banana.png",
            "Bananas are packed with potassium, which helps your muscles work. A bunch of bananas is called a 'hand', and each individual banana is a 'finger'!",
            1,
            FoodType.GOOD_FOOD
        ),
        new Question(
            "Donuts are sweet, but why are they considered an 'unhealthy treat'?",
            Arrays.asList("They have too many vitamins", "They are deep-fried and high in added sugars", "They make you run too fast", "They are made of vegetables"),
            1,
            "donut.png",
            "Donuts are high in added sugar and fats. They give you a quick burst of energy followed by a tired 'sugar crash'!",
            1,
            FoodType.BAD_FOOD
        ),

        // Level 2: Nutrient Superpowers!
        new Question(
            "What is the main 'superpower' nutrient in Broccoli that helps protect you from getting sick?",
            Arrays.asList("Vitamin C", "Sugar", "Sodium", "Healthy Fats"),
            0,
            "broccoli.png",
            "Broccoli contains more Vitamin C than oranges! It is also packed with Calcium for strong bones and teeth.",
            2,
            FoodType.GOOD_FOOD
        ),
        new Question(
            "Carrots are famous for helping which part of your body?",
            Arrays.asList("Your hair", "Your ears", "Your eyes (Eyesight)", "Your fingernails"),
            2,
            "carrot.png",
            "Carrots are loaded with beta-carotene, which your body turns into Vitamin A—a super nutrient that gives you great night vision!",
            2,
            FoodType.GOOD_FOOD
        ),
        new Question(
            "Which mineral in milk helps you grow super strong bones and teeth?",
            Arrays.asList("Iron", "Calcium", "Sodium", "Potassium"),
            1,
            "milk.png",
            "Milk is full of calcium. To get the same calcium as one cup of milk, you'd have to eat 10 cups of raw spinach in one sitting!",
            2,
            FoodType.GOOD_FOOD
        ),
        new Question(
            "What key mineral does spinach have that keeps your blood healthy and helps carry oxygen?",
            Arrays.asList("Iron", "Calcium", "Sugar", "Fats"),
            0,
            "spinach.png",
            "Spinach is packed with Iron for healthy blood and Vitamin K for strong bones. It was once called the 'prince of vegetables'!",
            2,
            FoodType.GOOD_FOOD
        ),
        new Question(
            "Salmon contains Omega-3 fatty acids. What do these acids do?",
            Arrays.asList("Make you sleepy", "Help your brain grow and concentrate", "Make your bones weak", "Turn your skin pink"),
            1,
            "salmon.png",
            "Salmon is rich in Omega-3 fats and high-quality protein. Omega-3s act as fuel for your brain, helping you focus and learn.",
            2,
            FoodType.GOOD_FOOD
        ),
        new Question(
            "Eggs contain choline. What does this nutrient help build?",
            Arrays.asList("Strong bones", "Brain cells and memory", "Fingernails", "Hair color"),
            1,
            "egg.png",
            "Eggs are nature's multivitamins. They contain choline, which boosts memory and brain power, and complete protein to build muscles.",
            2,
            FoodType.GOOD_FOOD
        ),

        // Level 3: Balanced Bites!
        new Question(
            "Avocados are unique fruits. What healthy nutrient are they high in?",
            Arrays.asList("Added sugar", "Healthy fats", "Sodium", "Cholesterol"),
            1,
            "avocado.png",
            "Avocados are actually single-seeded berries! They are loaded with 'good fats' that help your body absorb vitamins from other vegetables.",
            3,
            FoodType.GOOD_FOOD
        ),
        new Question(
            "Why is soda/cola a poor drink choice compared to water or milk?",
            Arrays.asList("It has no vitamins and about 10 teaspoons of added sugar", "It has too much calcium", "It makes your bones too strong", "It contains real fruit juice"),
            0,
            "soda.png",
            "One can of soda has about 10 teaspoons of sugar—more than a child should have in a whole day! It can also cause cavities.",
            3,
            FoodType.BAD_FOOD
        )
    );

    public GameSession() {
        loadLevelQuestions();
    }

    public void loadLevelQuestions() {
        currentQuestions = new ArrayList<>();
        for (Question q : QUESTION_BANK) {
            if (q.getLevel() == currentLevel) {
                currentQuestions.add(q);
            }
        }
        // Shuffle the questions in this level for variety
        Collections.shuffle(currentQuestions);
        currentQuestionIndex = 0;
    }

    public boolean answerCurrentQuestion(int indexSelected) {
        Question current = getCurrentQuestion();
        if (current == null) return false;

        boolean correct = current.getCorrectIndex() == indexSelected;
        if (correct) {
            streak++;
            if (streak > maxStreak) {
                maxStreak = streak;
            }
            // Score formula includes a streak bonus!
            score += 100 * streak;

            // Earn streak badge
            if (streak == 3 && !badges.contains("Streak Scholar")) {
                badges.add("Streak Scholar");
            } else if (streak == 5 && !badges.contains("Streak Legend")) {
                badges.add("Streak Legend");
            }
        } else {
            streak = 0;
            lives--;
        }
        return correct;
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < currentQuestions.size()) {
            return currentQuestions.get(currentQuestionIndex);
        }
        return null;
    }

    public boolean nextQuestion() {
        currentQuestionIndex++;
        return currentQuestionIndex < currentQuestions.size();
    }

    public boolean hasMoreQuestions() {
        return currentQuestionIndex < currentQuestions.size();
    }

    public boolean nextLevel() {
        if (currentLevel < 3) {
            // Award completion badges
            if (currentLevel == 1 && !badges.contains("Nutrition Rookie")) {
                badges.add("Nutrition Rookie");
            } else if (currentLevel == 2 && !badges.contains("Vitamin Master")) {
                badges.add("Vitamin Master");
            }

            currentLevel++;
            loadLevelQuestions();
            return true;
        } else {
            // Completed Level 3
            if (!badges.contains("Healthy Hero")) {
                badges.add("Healthy Hero");
            }
            return false; // Game finished!
        }
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getStreak() {
        return streak;
    }

    public int getMaxStreak() {
        return maxStreak;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public int getQuestionsInLevelCount() {
        return currentQuestions.size();
    }

    public List<String> getBadges() {
        return badges;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }
}
