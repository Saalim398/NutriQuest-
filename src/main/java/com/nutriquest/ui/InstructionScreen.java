package com.nutriquest.ui;

import com.nutriquest.App;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class InstructionScreen extends BorderPane {
    private final App controller;

    public InstructionScreen(App controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        VBox mainBox = new VBox(25);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(30));

        // Title
        Label titleLabel = new Label("How to Play");
        titleLabel.getStyleClass().add("text-title");
        titleLabel.setStyle("-fx-font-size: 36px;");

        // Instructions Card
        VBox card = new VBox(15);
        card.getStyleClass().add("glass-card");
        card.setMaxWidth(700);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(25));

        // Content
        addRule(card, "🍎 Eat Right!", "Learn to choose healthy foods over sugary/unhealthy treats to complete Level 1.");
        addRule(card, "⚡ Nutrient Superpowers!", "Identify minerals and vitamins (like Vitamin C, Iron, and Calcium) in Level 2.");
        addRule(card, "❤️ Watch Your Health!", "You start with 3 Hearts. Lose a heart for each wrong answer. Don't let them hit zero!");
        addRule(card, "🔥 Streak Bonuses!", "Answer correctly in a row to build a Streak Multiplier and earn massive score boosts!");
        addRule(card, "🏆 Badges!", "Unlock cool achievements like 'Nutrition Rookie', 'Vitamin Master', and 'Healthy Hero'.");
        addRule(card, "🔍 Food Explorer!", "Use the search engine in the menu to search *any* food online and check its healthy benefits!");

        // Back Button
        Button backBtn = new Button("Back to Main Menu");
        backBtn.getStyleClass().addAll("btn-base", "btn-primary");
        backBtn.setPrefWidth(220);
        backBtn.setOnAction(e -> controller.showStartScreen());

        backBtn.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), backBtn);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        backBtn.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), backBtn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        mainBox.getChildren().addAll(titleLabel, card, backBtn);

        ScrollPane scrollPane = new ScrollPane(mainBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setCenter(scrollPane);
    }

    private void addRule(VBox container, String title, String description) {
        VBox ruleBox = new VBox(3);
        Label ruleTitle = new Label(title);
        ruleTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2ecc71;");
        ruleTitle.setWrapText(true);

        Label ruleDesc = new Label(description);
        ruleDesc.getStyleClass().add("text-trivia");
        ruleDesc.setStyle("-fx-font-size: 14px;");
        ruleDesc.setWrapText(true);
        ruleDesc.setMaxWidth(650);

        ruleBox.getChildren().addAll(ruleTitle, ruleDesc);
        container.getChildren().add(ruleBox);
    }
}
