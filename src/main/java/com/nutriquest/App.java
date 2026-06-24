package com.nutriquest;

import com.nutriquest.model.GameSession;
import com.nutriquest.ui.*;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {
    private Stage stage;
    private Scene scene;
    private StackPane rootPane;
    private GameSession session;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.stage.setTitle("NutriQuest - Nutrition Education Game");

        // Use a StackPane as the scene root to support smooth overlays and transitions
        rootPane = new StackPane();
        scene = new Scene(rootPane, 1050, 750);

        // Load Stylesheet
        try {
            String cssPath = getClass().getResource("/com/nutriquest/style.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Could not load stylesheet style.css: " + e.getMessage());
        }

        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(900);
        stage.setMinHeight(700);
        stage.show();

        // Show Start Screen initially
        showStartScreen();
    }

    private void changeScreen(Parent newScreen) {
        if (rootPane.getChildren().isEmpty()) {
            rootPane.getChildren().add(newScreen);
            fadeNodeIn(newScreen);
        } else {
            Parent oldScreen = (Parent) rootPane.getChildren().get(0);
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), oldScreen);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> {
                rootPane.getChildren().clear();
                rootPane.getChildren().add(newScreen);
                newScreen.setOpacity(0.0);
                fadeNodeIn(newScreen);
            });
            fadeOut.play();
        }
    }

    private void fadeNodeIn(Parent node) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), node);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    public void showStartScreen() {
        StartScreen startScreen = new StartScreen(this);
        changeScreen(startScreen);
    }

    public void showInstructionScreen() {
        InstructionScreen instructionScreen = new InstructionScreen(this);
        changeScreen(instructionScreen);
    }

    public void startNewGame() {
        session = new GameSession();
        showQuizScreen();
    }

    public void showQuizScreen() {
        if (session == null) {
            startNewGame();
            return;
        }
        QuizScreen quizScreen = new QuizScreen(this, session);
        changeScreen(quizScreen);
    }

    public void showFoodExplorerScreen() {
        FoodExplorerScreen explorerScreen = new FoodExplorerScreen(this);
        changeScreen(explorerScreen);
    }

    public void showGameOverScreen(boolean victory) {
        if (session == null) {
            showStartScreen();
            return;
        }
        GameOverScreen gameOverScreen = new GameOverScreen(this, session, victory);
        changeScreen(gameOverScreen);
    }

    public static void main(String[] args) {
        launch(args);
    }
}













