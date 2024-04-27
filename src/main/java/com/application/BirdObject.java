package com.application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.stream.IntStream;

public class BirdObject {
    private double centerX;
    private double centerY;
    private double radius;
    private Color color;
    private double velocityX;
    private double velocityY;
    private Polygon birdShape;

    public BirdObject(double centerX, double centerY, double radius, Color color) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.color = color;
        this.velocityX = 4;
        this.velocityY = 3;

        birdShape = new Polygon();
        birdShape.getPoints().addAll(
                0.0, 0.0,
                20.0, 10.0,
                0.0, 20.0
        );

        birdShape.setFill(color);
        birdShape.setStroke(color);
        birdShape.setStrokeWidth(1.0);
        // Position the bird at the initial center position
        birdShape.setLayoutX(centerX);
        birdShape.setLayoutY(centerY);
    }

    public void update(double deltaTime) {
        // Update bird based on velocity
        centerX += velocityX * deltaTime;
        centerY += velocityY * deltaTime;

        // Update bird position and rotation
        birdShape.setLayoutX(centerX);
        birdShape.setLayoutY(centerY);
        birdShape.setRotate(Math.toDegrees(Math.atan2(velocityY, velocityX)));
    }

    public void draw(GraphicsContext gc) {
        gc.save(); // Save current graphics context state

        // Translate to bird center
        gc.translate(centerX, centerY);

        // Rotate bird
        gc.rotate(Math.toDegrees(Math.atan2(velocityY, velocityX)));

        // Set fill color
        gc.setFill(color);

        // Get x and y coordinates of the polygon points
        double[] xPoints = IntStream.range(0, birdShape.getPoints().size())
                .filter(i -> i % 2 == 0)
                .mapToDouble(i -> birdShape.getPoints().get(i))
                .toArray();
        double[] yPoints = IntStream.range(0, birdShape.getPoints().size())
                .filter(i -> i % 2 != 0)
                .mapToDouble(i -> birdShape.getPoints().get(i))
                .toArray();
        // Draw the polygon
        gc.fillPolygon(xPoints, yPoints, xPoints.length);

        gc.restore(); // Restore graphics context state
    }


    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public double getRadius() {
        return radius;
    }

    public double getVelocityX(){
        return velocityX;
    }

    public double getVelocityY(){
        return velocityY;
    }

    public void setVelocityX(double velocityX){
        // ensure velocity is within a certain range
        if (velocityX > 10) {
            velocityX = 10;
        } else if (velocityX < -10) {
            velocityX = -10;
        }
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY){
        // ensure velocity is within a certain range
        if (velocityY > 10) {
            velocityY = 10;
        } else if (velocityY < -10) {
            velocityY = -10;
        }
        this.velocityY = velocityY;
    }
}
