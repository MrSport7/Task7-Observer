package com.example.observer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ComponentThree implements IObserver {
    private Timeline colorAnimation;
    private TranslateTransition moveAnimation;
    private Rectangle animatedRectangle;
    private Label animationStatusLabel;

    public ComponentThree(Rectangle rectangle, Label animationStatusLabel) {
        this.animatedRectangle = rectangle;
        this.animationStatusLabel = animationStatusLabel;
        createAnimations();
    }

    private void createAnimations() {
        colorAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> animatedRectangle.setFill(Color.BLUE)),
                new KeyFrame(Duration.seconds(1), e -> animatedRectangle.setFill(Color.RED)),
                new KeyFrame(Duration.seconds(10), e -> animatedRectangle.setFill(Color.BLUE))
        );
        colorAnimation.setCycleCount(Timeline.INDEFINITE);

        moveAnimation = new TranslateTransition(Duration.seconds(10), animatedRectangle);
        moveAnimation.setFromX(0);
        moveAnimation.setToX(300);
        moveAnimation.setCycleCount(Timeline.INDEFINITE);
        moveAnimation.setAutoReverse(true);
    }

    @Override
    public void update(Subject subject) {
        TimeServer timeServer = (TimeServer) subject;

        if (timeServer.getState() % 10 == 0) {
            String message = "Анимация перезапущена в " + timeServer.getState() + " секунд";

            Platform.runLater(() -> {
                animationStatusLabel.setText(message);
            });
            if (colorAnimation.getStatus() != Timeline.Status.RUNNING) {
                colorAnimation.play();
            }
            if (moveAnimation.getStatus() != Timeline.Status.RUNNING) {
                moveAnimation.play();
            }
        }
    }

    public void stopAnimation() {
        if (colorAnimation.getStatus() == Timeline.Status.RUNNING) {
            colorAnimation.stop();
        }
        if (moveAnimation.getStatus() == Timeline.Status.RUNNING) {
            moveAnimation.stop();
            Platform.runLater(() -> {
                animatedRectangle.setTranslateX(0);
                animationStatusLabel.setText("Анимация остановлена");
            });
        }
    }
}
