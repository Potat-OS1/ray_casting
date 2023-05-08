package com.example.ray_casting;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Ray {
    Line thisRay;
    double theta;
    double strength;

    Ray (double x, double y, double theta, int strength) {
        this.theta = theta;
        this.strength = strength;
        thisRay = new Line(x, y, x+(strength * Math.cos(Math.toRadians(theta))), y+(strength * Math.sin(Math.toRadians(theta))));
        thisRay.setStrokeWidth(3);
        thisRay.setOpacity(.5);
        thisRay.setFill(Color.WHITE);
    }

    public Line getRay() {
        return thisRay;
    }

    public void setTheta (double theta) {
        this.theta = theta;
    }

    public void updateRayDirection (double theta) {
        thisRay.setEndX(thisRay.getStartX() + (strength * Math.cos(Math.toRadians(theta))));
        thisRay.setEndY(thisRay.getStartY() + (strength * Math.sin(Math.toRadians(theta))));
    }

    public void updateRayOrigin (double x1, double y1) {
        thisRay.setStartX(x1);
        thisRay.setStartY(y1);
        thisRay.setEndX(x1 + (strength * Math.cos(Math.toRadians(theta))));
        thisRay.setEndY(y1 + (strength * Math.sin(Math.toRadians(theta))));
    }

    public void updateRayEndPoints (double x2, double y2) {
        thisRay.setEndX(x2);
        thisRay.setEndY(y2);
    }
}
