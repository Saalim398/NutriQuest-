package com.nutriquest.ui;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class PenguinMascot extends Pane {
    private Ellipse body;
    private Ellipse belly;
    private Circle leftEye;
    private Circle rightEye;
    private Circle leftPupil;
    private Circle rightPupil;
    private Polygon beak;
    private Ellipse leftFlipper;
    private Ellipse rightFlipper;
    private Ellipse leftFoot;
    private Ellipse rightFoot;
    private Polygon hat;
    private Circle hatPom;

    // Sad teardrops
    private Ellipse leftTeardrop;
    private Ellipse rightTeardrop;

    public enum Pose {
        NEUTRAL,
        CORRECT,
        INCORRECT
    }

    public PenguinMascot() {
        setPrefSize(140, 180);
        setMinSize(140, 180);
        setMaxSize(140, 180);
        drawPenguin();
        setPose(Pose.NEUTRAL);
    }

    private void drawPenguin() {
        // Feet (Orange)
        leftFoot = new Ellipse(50, 155, 18, 8);
        leftFoot.setFill(Color.web("#f39c12"));
        leftFoot.setStroke(Color.web("#d35400"));
        leftFoot.setStrokeWidth(1.5);

        rightFoot = new Ellipse(90, 155, 18, 8);
        rightFoot.setFill(Color.web("#f39c12"));
        rightFoot.setStroke(Color.web("#d35400"));
        rightFoot.setStrokeWidth(1.5);

        // Flippers (Dark Slate)
        leftFlipper = new Ellipse(22, 100, 9, 30);
        leftFlipper.setFill(Color.web("#2c3e50"));
        leftFlipper.setStroke(Color.web("#1a252f"));
        leftFlipper.setStrokeWidth(1);
        // Set pivot point for left flipper rotation
        leftFlipper.setCenterX(22);
        leftFlipper.setCenterY(90);

        rightFlipper = new Ellipse(118, 100, 9, 30);
        rightFlipper.setFill(Color.web("#2c3e50"));
        rightFlipper.setStroke(Color.web("#1a252f"));
        rightFlipper.setStrokeWidth(1);
        // Set pivot point for right flipper rotation
        rightFlipper.setCenterX(118);
        rightFlipper.setCenterY(90);

        // Body (Dark Slate)
        body = new Ellipse(70, 95, 45, 62);
        body.setFill(Color.web("#34495e"));
        body.setStroke(Color.web("#2c3e50"));
        body.setStrokeWidth(2);

        // Belly (White)
        belly = new Ellipse(70, 105, 30, 45);
        belly.setFill(Color.WHITE);

        // Eyes
        leftEye = new Circle(54, 60, 8, Color.WHITE);
        rightEye = new Circle(86, 60, 8, Color.WHITE);
        leftPupil = new Circle(55, 60, 4, Color.web("#2c3e50"));
        rightPupil = new Circle(85, 60, 4, Color.web("#2c3e50"));

        // Beak (Yellow-Orange)
        beak = new Polygon();
        beak.getPoints().addAll(new Double[]{
            63.0, 68.0,
            77.0, 68.0,
            70.0, 78.0
        });
        beak.setFill(Color.web("#f1c40f"));
        beak.setStroke(Color.web("#e67e22"));
        beak.setStrokeWidth(1);

        // Explorer Hat
        hat = new Polygon();
        hat.getPoints().addAll(new Double[]{
            35.0, 42.0,
            105.0, 42.0,
            70.0, 18.0
        });
        hat.setFill(Color.web("#27ae60"));
        hat.setStroke(Color.web("#219653"));
        hat.setStrokeWidth(1.5);

        hatPom = new Circle(70, 18, 5, Color.web("#f1c40f"));

        // Sad teardrops (hidden by default)
        leftTeardrop = new Ellipse(54, 75, 3, 6);
        leftTeardrop.setFill(Color.web("#3498db"));
        leftTeardrop.setVisible(false);

        rightTeardrop = new Ellipse(86, 75, 3, 6);
        rightTeardrop.setFill(Color.web("#3498db"));
        rightTeardrop.setVisible(false);

        // Add to pane hierarchy
        getChildren().addAll(
            leftFoot, rightFoot,
            leftFlipper, rightFlipper,
            body, belly,
            leftEye, rightEye,
            leftPupil, rightPupil,
            beak,
            hat, hatPom,
            leftTeardrop, rightTeardrop
        );
    }

    public void setPose(Pose pose) {
        // Reset properties
        setTranslateY(0);
        setRotate(0);
        leftTeardrop.setVisible(false);
        rightTeardrop.setVisible(false);

        switch (pose) {
            case NEUTRAL:
                // Flippers pointing down
                leftFlipper.setRotate(15);
                rightFlipper.setRotate(-15);
                leftPupil.setRadius(4);
                rightPupil.setRadius(4);
                leftPupil.setCenterX(55);
                rightPupil.setCenterX(85);
                break;

            case CORRECT:
                // Flippers waving up!
                leftFlipper.setRotate(135);
                rightFlipper.setRotate(-135);
                leftPupil.setRadius(5);
                rightPupil.setRadius(5);
                leftPupil.setCenterX(54);
                rightPupil.setCenterX(86);

                // Celebrate jump animation
                TranslateTransition jump = new TranslateTransition(Duration.millis(180), this);
                jump.setByY(-22);
                jump.setCycleCount(4);
                jump.setAutoReverse(true);
                jump.play();
                break;

            case INCORRECT:
                // Flippers dropped low
                leftFlipper.setRotate(-35);
                rightFlipper.setRotate(35);
                leftPupil.setRadius(3);
                rightPupil.setRadius(3);
                leftPupil.setCenterX(55);
                rightPupil.setCenterX(85);

                // Show crying teardrops
                leftTeardrop.setVisible(true);
                rightTeardrop.setVisible(true);

                // Sad body shake animation
                RotateTransition shake = new RotateTransition(Duration.millis(80), this);
                shake.setByAngle(8);
                shake.setCycleCount(6);
                shake.setAutoReverse(true);
                shake.play();
                break;
        }
    }
}
