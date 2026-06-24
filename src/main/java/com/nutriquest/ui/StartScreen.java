package com.nutriquest.ui;

import com.nutriquest.App;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.InputStream;

public class StartScreen extends BorderPane {
    private final App controller;

    public StartScreen(App controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        // Center Container
        VBox centerBox = new VBox(25);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPrefWidth(600);

        // Logo
        ImageView logoView = new ImageView();
        logoView.setFitWidth(280);
        logoView.setFitHeight(280);
        logoView.setPreserveRatio(true);

        boolean logoLoaded = false;
        try (InputStream is = getClass().getResourceAsStream("/com/nutriquest/images/logo.png")) {
            if (is != null) {
                logoView.setImage(new Image(is));
                logoLoaded = true;
            }
        } catch (Exception e) {
            System.err.println("Could not load logo image: " + e.getMessage());
        }

        VBox titleContainer = new VBox(5);
        titleContainer.setAlignment(Pos.CENTER);

        if (!logoLoaded) {
            // Text Fallback if image fails to load
            Label fallbackTitle = new Label("NutriQuest");
            fallbackTitle.getStyleClass().add("text-title");
            fallbackTitle.setStyle("-fx-font-size: 48px;");
            titleContainer.getChildren().add(fallbackTitle);
        } else {
            titleContainer.getChildren().add(logoView);
        }

        Label subtitle = new Label("Interactive Nutrition Education Game");
        subtitle.getStyleClass().add("text-subtitle");
        titleContainer.getChildren().add(subtitle);

        centerBox.getChildren().add(titleContainer);

        // Menu Card
        VBox menuCard = new VBox(15);
        menuCard.setAlignment(Pos.CENTER);
        menuCard.getStyleClass().add("glass-card");
        menuCard.setMaxWidth(380);

        // Buttons
        Button playBtn = createMenuButton("Start Quest", "btn-primary");
        playBtn.setOnAction(e -> controller.startNewGame());

        Button explorerBtn = createMenuButton("Food Explorer", "btn-secondary");
        explorerBtn.setOnAction(e -> controller.showFoodExplorerScreen());

        Button instructionsBtn = createMenuButton("How to Play", "btn-secondary");
        instructionsBtn.setOnAction(e -> controller.showInstructionScreen());

        Button exitBtn = createMenuButton("Exit", "btn-secondary");
        exitBtn.setOnAction(e -> System.exit(0));

        menuCard.getChildren().addAll(playBtn, explorerBtn, instructionsBtn, exitBtn);
        centerBox.getChildren().add(menuCard);

        this.setCenter(centerBox);
    }

    private Button createMenuButton(String text, String styleClass) {
        Button btn = new Button(text);
        btn.getStyleClass().addAll("btn-base", styleClass);
        btn.setPrefWidth(260);

        // Micro-animations on hover
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
