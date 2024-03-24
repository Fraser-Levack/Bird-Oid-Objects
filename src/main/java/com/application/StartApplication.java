package com.application;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
public class StartApplication extends Application {

    private BirdObject bird;
    private Canvas canvas;
    @Override
    public void start(Stage primaryStage) {
        // Initialize your UI components and set up the scene here
        primaryStage.setTitle("Simple JavaFX Game");

        // Create a canvas
        this.canvas = new Canvas(400, 300);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw a bird object on the canvas

        this.bird = new BirdObject(200, 150, 20, Color.BLACK);
        bird.draw(gc);

        // Create a layout pane and add the canvas to it
        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        // Create a scene and set it on the stage
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            update(); // Update game state
            render(gc); // Render the scene
        }));
        timeline.setCycleCount(Animation.INDEFINITE); // Run indefinitely
        timeline.play();

        // Show the stage
        primaryStage.show();
    }

    private void update() {
        double radius = bird.getRadius();
        double nextX = bird.getCenterX() + bird.getVelocityX();
        double nextY = bird.getCenterY() + bird.getVelocityY();
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        // Check if the next position exceeds the container boundaries
        if (nextX - radius < 0 || nextX + radius > canvasWidth) {
            // Reverse the horizontal velocity to bounce off the left or right wall
            bird.setVelocityX(-bird.getVelocityX());
        }
        if (nextY - radius < 0 || nextY + radius > canvasHeight) {
            // Reverse the vertical velocity to bounce off the top or bottom wall
            bird.setVelocityY(-bird.getVelocityY());
        }

        // Update circle position
        bird.setCenterX(nextX);
        bird.setCenterY(nextY);
    }

    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        // Draw the circle
        bird.draw(gc);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
