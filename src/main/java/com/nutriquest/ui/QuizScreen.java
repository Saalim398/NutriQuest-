package com.nutriquest.ui;

import com.nutriquest.App;
import com.nutriquest.model.GameSession;
import com.nutriquest.model.Question;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.ContentDisplay;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QuizScreen extends BorderPane {
    private final App controller;
    private final GameSession session;

    private Label levelLabel;
    private Label scoreLabel;
    private Label streakLabel;
    private HBox heartsContainer;
    private ProgressBar progressBar;

    private VBox foodCard;
    private Label questionLabel;
    private ImageView foodImageView;
    private StackPane imageContainer; // fallback if image is missing
    private VBox optionsContainer;
    private final List<Button> optionButtons = new ArrayList<>();

    // Feedback Overlay
    private VBox feedbackCard;
    private Label feedbackStatusLabel;
    private Label triviaLabel;
    private Button continueBtn;

    public QuizScreen(App controller, GameSession session) {
        this.controller = controller;
        this.session = session;
        initialize();
        loadQuestion();
    }

    private void initialize() {
        // --- TOP BAR (STATUS) ---
        VBox topBar = new VBox(10);
        topBar.setPadding(new Insets(15, 20, 10, 20));
        topBar.getStyleClass().add("glass-card");
        topBar.setStyle("-fx-background-radius: 0 0 16px 16px; -fx-border-width: 0 0 1px 0;");

        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);

        levelLabel = new Label("Level " + session.getCurrentLevel());
        levelLabel.getStyleClass().add("badge");
        levelLabel.getStyleClass().add("badge-level");

        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        streakLabel = new Label("🔥 Streak: 0");
        streakLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #f1c40f;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        heartsContainer = new HBox(5);
        heartsContainer.setAlignment(Pos.CENTER_LEFT);
        updateHearts();

        statsRow.getChildren().addAll(levelLabel, scoreLabel, streakLabel, spacer, heartsContainer);

        progressBar = new ProgressBar(0.0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setPrefHeight(10);

        topBar.getChildren().addAll(statsRow, progressBar);
        this.setTop(topBar);

        // --- CENTER AREA (QUIZ CARD & OPTIONS) ---
        VBox centerBox = new VBox(18);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20, 30, 20, 30));

        // Food Card
        foodCard = new VBox(12);
        foodCard.getStyleClass().add("glass-card");
        foodCard.setMaxWidth(700);
        foodCard.setAlignment(Pos.CENTER);
        foodCard.setPadding(new Insets(20));

        questionLabel = new Label();
        questionLabel.getStyleClass().add("text-question");
        questionLabel.setAlignment(Pos.CENTER);
        questionLabel.setWrapText(true);
        questionLabel.setMaxWidth(650);

        foodImageView = new ImageView();
        foodImageView.setFitWidth(130);
        foodImageView.setFitHeight(130);
        foodImageView.setPreserveRatio(true);

        imageContainer = new StackPane(foodImageView);
        imageContainer.setMinSize(130, 130);

        foodCard.getChildren().addAll(imageContainer, questionLabel);

        // Options Stack (vertical list)
        optionsContainer = new VBox(10);
        optionsContainer.setAlignment(Pos.CENTER);
        optionsContainer.setMaxWidth(700);

        // Feedback Card (below options)
        feedbackCard = new VBox(10);
        feedbackCard.setMaxWidth(700);
        feedbackCard.setAlignment(Pos.CENTER);
        feedbackCard.setManaged(false);
        feedbackCard.setVisible(false);

        feedbackStatusLabel = new Label();
        feedbackStatusLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        triviaLabel = new Label();
        triviaLabel.getStyleClass().add("text-trivia");
        triviaLabel.setAlignment(Pos.CENTER);
        triviaLabel.setWrapText(true);
        triviaLabel.setMaxWidth(650);

        continueBtn = new Button("Continue");
        continueBtn.getStyleClass().addAll("btn-base", "btn-primary");
        continueBtn.setOnAction(e -> handleContinue());

        feedbackCard.getChildren().addAll(feedbackStatusLabel, triviaLabel, continueBtn);

        centerBox.getChildren().addAll(foodCard, optionsContainer, feedbackCard);

        // Wrap in a ScrollPane so everything is reachable
        ScrollPane scrollPane = new ScrollPane(centerBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.setCenter(scrollPane);
    }

    private void updateHearts() {
        heartsContainer.getChildren().clear();
        int lives = session.getLives();
        for (int i = 0; i < 3; i++) {
            Label heart = new Label(i < lives ? "❤️" : "🖤");
            heart.setStyle("-fx-font-size: 18px;");
            heartsContainer.getChildren().add(heart);
        }
    }

    private void loadQuestion() {
        Question q = session.getCurrentQuestion();
        if (q == null) return;

        // Reset feedback card
        feedbackCard.setVisible(false);
        feedbackCard.setManaged(false);

        // Update stats
        levelLabel.setText("Level " + session.getCurrentLevel());
        scoreLabel.setText("Score: " + session.getScore());
        streakLabel.setText("🔥 Streak: " + session.getStreak());
        updateHearts();

        // Update progress bar
        double progress = (double) (session.getCurrentQuestionIndex()) / session.getQuestionsInLevelCount();
        progressBar.setProgress(progress);

        // Load Question UI details
        questionLabel.setText(q.getQuery());

        // Load food image with defensive fallback
        foodImageView.setImage(null);
        imageContainer.getChildren().clear();

        boolean imageLoaded = false;
        if (q.getImagePath() != null && !q.getImagePath().isEmpty()) {
            try (InputStream is = getClass().getResourceAsStream("/com/nutriquest/images/" + q.getImagePath())) {
                if (is != null) {
                    foodImageView.setImage(new Image(is));
                    imageLoaded = true;
                }
            } catch (Exception e) {
                System.err.println("Could not load image: " + q.getImagePath());
            }
        }

        if (!imageLoaded) {
            // Draw a beautiful circular badge as fallback
            Circle circle = new Circle(60);
            circle.setFill(Color.web(q.getFoodType().getColorHex()));
            circle.setStroke(Color.web("#ffffff", 0.2));
            circle.setStrokeWidth(2);

            String firstLetter = q.getQuery().replaceAll("^Is an |^What is the main superpower nutrient in |^Carrots are famous for helping ", "")
                                           .replaceAll("^Which mineral in |^What key mineral does |^Salmon contains |^Eggs contain ", "")
                                           .trim().substring(0, 1).toUpperCase();

            Label textLabel = new Label(firstLetter);
            textLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: white;");

            imageContainer.getChildren().addAll(circle, textLabel);
        } else {
            imageContainer.getChildren().add(foodImageView);
        }

        // Render Options Stack
        optionsContainer.getChildren().clear();
        optionButtons.clear();

        List<String> choices = q.getChoices();
        for (int i = 0; i < choices.size(); i++) {
            final int index = i;
            Button btn = new Button(choices.get(i));
            btn.getStyleClass().add("option-btn");
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setWrapText(true);
            btn.setContentDisplay(ContentDisplay.LEFT);
            btn.setOnAction(e -> handleOptionSelected(index));
            optionButtons.add(btn);
            optionsContainer.getChildren().add(btn);
        }
    }

    private void handleOptionSelected(int index) {
        // Disable all option buttons
        for (Button btn : optionButtons) {
            btn.setDisable(true);
        }

        Question q = session.getCurrentQuestion();
        if (q == null) return;

        boolean correct = session.answerCurrentQuestion(index);

        // Highlight correct/incorrect answers visually
        for (int i = 0; i < optionButtons.size(); i++) {
            Button btn = optionButtons.get(i);
            if (i == q.getCorrectIndex()) {
                btn.setStyle("-fx-border-color: #2ecc71; -fx-background-color: rgba(46,204,113,0.15);");
            } else if (i == index && !correct) {
                btn.setStyle("-fx-border-color: #e74c3c; -fx-background-color: rgba(231,76,60,0.15);");
            }
        }

        // Configure Feedback Card
        feedbackCard.getStyleClass().removeAll("feedback-card-correct", "feedback-card-incorrect");
        if (correct) {
            feedbackStatusLabel.setText("Correct! 🎉");
            feedbackStatusLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 22px; -fx-font-weight: bold;");
            feedbackCard.getStyleClass().add("feedback-card-correct");
        } else {
            feedbackStatusLabel.setText("Incorrect... 😢");
            feedbackStatusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 22px; -fx-font-weight: bold;");
            feedbackCard.getStyleClass().add("feedback-card-incorrect");
        }

        triviaLabel.setText(q.getTrivia());

        // Update stats top bar immediately
        scoreLabel.setText("Score: " + session.getScore());
        streakLabel.setText("🔥 Streak: " + session.getStreak());
        updateHearts();

        // Slide up animation for feedback
        feedbackCard.setManaged(true);
        feedbackCard.setVisible(true);
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), feedbackCard);
        tt.setFromY(100);
        tt.setToY(0);
        tt.play();
    }

    private void handleContinue() {
        if (session.isGameOver()) {
            controller.showGameOverScreen(false);
            return;
        }

        if (session.nextQuestion()) {
            loadQuestion();
        } else {
            // Level completed!
            boolean hasNext = session.nextLevel();
            if (hasNext) {
                // Show celebration transition or load next level
                loadQuestion();
            } else {
                // Game victory!
                controller.showGameOverScreen(true);
            }
        }
    }
}
