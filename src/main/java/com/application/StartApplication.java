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
        int numberOfBirds = 20;
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

            // bird to move away from other birds average position if they are too close
            List<BirdObject> nearbyBirds = getNearbyBirds(bird);
            if (!nearbyBirds.isEmpty()) {
                List<double[]> positions = getPositions(nearbyBirds);
                double[] averagePosition = getAveragePosition(positions);
                setVelocityAwayFromTarget(bird, averagePosition);
            }

            // bird to move towards the average position of other far away birds
            List<BirdObject> farBirds = getFarBirds(bird);
            if (!farBirds.isEmpty()) {
                List<double[]> positions = getPositions(farBirds);
                double[] averagePosition = getAveragePosition(positions);
                setVelocityTowardsTarget(bird, averagePosition);
            }

            // bird to change direction towards average trajectory angle of other birds
            double averageAngle = getAverageAngle(nearbyBirds);
            setVelocityTowardsAverageAngle(bird, averageAngle);

            bird.setCenterX(nextX);
            bird.setCenterY(nextY);
        }
    }

    // to get other birds in the canvas around a small radius
    private List<BirdObject> getNearbyBirds(BirdObject bird) {
        List<BirdObject> nearbyBirds = new ArrayList<>();
        for (BirdObject otherBird : birds) {
            if (bird != otherBird) {
                double distance = Math.sqrt(Math.pow(bird.getCenterX() - otherBird.getCenterX(), 2) +
                        Math.pow(bird.getCenterY() - otherBird.getCenterY(), 2));
                if (distance < 100) {
                    nearbyBirds.add(otherBird);
                }
            }
        }
        return nearbyBirds;
    }

    // to get other birds in the canvas around a large radius
    private List<BirdObject> getFarBirds(BirdObject bird) {
        List<BirdObject> farBirds = new ArrayList<>();
        for (BirdObject otherBird : birds) {
            if (bird != otherBird) {
                double distance = Math.sqrt(Math.pow(bird.getCenterX() - otherBird.getCenterX(), 2) +
                        Math.pow(bird.getCenterY() - otherBird.getCenterY(), 2));
                if (distance < 300) {
                    farBirds.add(otherBird);
                }
            }
        }
        return farBirds;
    }

    // returning list of positions of a list of birds
    private List<double[]> getPositions(List<BirdObject> birds) {
        List<double[]> positions = new ArrayList<>();
        for (BirdObject bird : birds) {
            positions.add(new double[]{bird.getCenterX(), bird.getCenterY()});
        }
        return positions;
    }

    // returning a calculation of the average position of a list of positions
    private double[] getAveragePosition(List<double[]> positions) {
        double sumX = 0;
        double sumY = 0;
        for (double[] position : positions) {
            sumX += position[0];
            sumY += position[1];
        }
        return new double[]{sumX / positions.size(), sumY / positions.size()};
    }

    // get average angle of birds from a list of birds
    private double getAverageAngle(List<BirdObject> birds) {
        double sumX = 0;
        double sumY = 0;
        for (BirdObject bird : birds) {
            sumX += bird.getVelocityX();
            sumY += bird.getVelocityY();
        }
        return Math.atan2(sumY, sumX);
    }

    // setting birds velocity to slowly move towards the same direction as other birds
    private void setVelocityTowardsAverageAngle(BirdObject bird, double averageAngle) {
        double angle = Math.atan2(bird.getVelocityY(), bird.getVelocityX());
        bird.setVelocityX(bird.getVelocityX() + Math.cos(averageAngle - angle) * 0.2);
        bird.setVelocityY(bird.getVelocityY() + Math.sin(averageAngle - angle) * 0.2);
    }


    // setting a birds velocity to increment towards a target position
    private void setVelocityTowardsTarget(BirdObject bird, double[] target) {
        double angle = Math.atan2(target[1] - bird.getCenterY(), target[0] - bird.getCenterX());
        bird.setVelocityX(bird.getVelocityX() + Math.cos(angle) * 0.5);
        bird.setVelocityY(bird.getVelocityY() + Math.sin(angle) * 0.5);
    }

    // setting a birds velocity to increment away from a target position
    private void setVelocityAwayFromTarget(BirdObject bird, double[] target) {
        double angle = Math.atan2(target[1] - bird.getCenterY(), target[0] - bird.getCenterX());
        bird.setVelocityX(bird.getVelocityX() - Math.cos(angle) * 0.3);
        bird.setVelocityY(bird.getVelocityY() - Math.sin(angle) * 0.3);
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
