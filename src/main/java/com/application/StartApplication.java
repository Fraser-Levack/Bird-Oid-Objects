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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class StartApplication extends Application {

    private List<BirdObject> birds;

    private Canvas canvas;
    @Override
    public void start(Stage primaryStage) {
        // Initialize your UI components and set up the scene here
        primaryStage.setTitle("Boids");

        // Create a canvas
        this.canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw a birds objects on the canvas

        this.birds = new ArrayList<>();
        Random random = new Random();
        int numberOfBirds = 10;
        for (int i = 0; i < numberOfBirds; i++) {
            BirdObject bird = new BirdObject(random.nextInt(20,780), random.nextInt(20,580), 20, Color.WHITE);
            birds.add(bird);
        }


        // Create a layout pane and add the canvas to it
        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        // Create a scene and set it on the stage
        Scene scene = new Scene(root, 800, 600);
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
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        for (BirdObject bird : birds) {
            double radius = bird.getRadius();
            double nextX = bird.getCenterX() + bird.getVelocityX();
            double nextY = bird.getCenterY() + bird.getVelocityY();

            if (nextX - radius < 0 || nextX + radius > canvasWidth) {
                bird.setVelocityX(-bird.getVelocityX());
            }
            if (nextY - radius < 0 || nextY + radius > canvasHeight) {
                bird.setVelocityY(-bird.getVelocityY());
            }

            bird.setCenterX(nextX);
            bird.setCenterY(nextY);
        }
    }
    private void render(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        for (BirdObject bird : birds) {
            bird.draw(gc);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
