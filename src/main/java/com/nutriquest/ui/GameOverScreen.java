package com.nutriquest.ui;

import com.nutriquest.App;
import com.nutriquest.model.GameSession;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;

public class GameOverScreen extends BorderPane {
    private final App controller;
    private final GameSession session;
    private final boolean victory;

    public GameOverScreen(App controller, GameSession session, boolean victory) {
        this.controller = controller;
        this.session = session;
        this.victory = victory;
        initialize();
    }

    private void initialize() {
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        // Title
        Label titleLabel = new Label(victory ? "Quest Complete! 🏆" : "Game Over! 😢");
        titleLabel.getStyleClass().add("text-title");
        titleLabel.setStyle(victory ? "-fx-text-fill: #2ecc71; -fx-font-size: 38px;" : "-fx-text-fill: #e74c3c; -fx-font-size: 38px;");

        // Info Card
        VBox card = new VBox(15);
        card.getStyleClass().add("glass-card");
        card.setMaxWidth(600);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));

        Label feedbackText = new Label(victory ?
                "Congratulations! You are a certified Nutrition Hero!" :
                "Don't worry! Every step is a learning experience. Try again!");
        feedbackText.setStyle("-fx-font-size: 15px; -fx-text-fill: #bdc3c7;");
        feedbackText.setWrapText(true);
        feedbackText.setMaxWidth(550);
        feedbackText.setAlignment(Pos.CENTER);

        Label finalScore = new Label("Final Score: " + session.getScore());
        finalScore.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label maxStreak = new Label("Longest Streak: " + session.getMaxStreak() + "🔥");
        maxStreak.setStyle("-fx-font-size: 16px; -fx-text-fill: #f1c40f;");

        card.getChildren().addAll(feedbackText, finalScore, maxStreak);

        // Badges Earned Section
        List<String> badges = session.getBadges();
        if (!badges.isEmpty()) {
            VBox badgeSection = new VBox(10);
            badgeSection.setAlignment(Pos.CENTER);
            badgeSection.setPadding(new Insets(10, 0, 0, 0));

            Label badgeTitle = new Label("Your Achievements:");
            badgeTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #9b59b6;");

            FlowPane badgeContainer = new FlowPane(10, 10);
            badgeContainer.setAlignment(Pos.CENTER);
            badgeContainer.setMaxWidth(500);

            for (String badge : badges) {
                Label bLabel = new Label("🏅 " + badge);
                bLabel.getStyleClass().add("badge-earned");
                badgeContainer.getChildren().add(bLabel);
            }

            badgeSection.getChildren().addAll(badgeTitle, badgeContainer);
            card.getChildren().add(badgeSection);
        }

        // Action Buttons
        Button replayBtn = createActionButton("Play Again", "btn-primary");
        replayBtn.setOnAction(e -> controller.startNewGame());

        Button menuBtn = createActionButton("Main Menu", "btn-secondary");
        menuBtn.setOnAction(e -> controller.showStartScreen());

        VBox btnContainer = new VBox(12);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().addAll(replayBtn, menuBtn);

        layout.getChildren().addAll(titleLabel, card, btnContainer);

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setCenter(scrollPane);
    }

    private Button createActionButton(String text, String styleClass) {
        Button btn = new Button(text);
        btn.getStyleClass().addAll("btn-base", styleClass);
        btn.setPrefWidth(220);

        btn.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        btn.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        return btn;
    }
}
