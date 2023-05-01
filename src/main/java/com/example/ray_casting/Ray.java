package com.example.ray_casting;

import javafx.scene.shape.Line;

public class Ray {
    Line thisRay;
    double theta;
    double strength;

    Ray (int x, int y, double theta, int strength) {
        this.theta = theta;
        this.strength = strength;
        thisRay = new Line(x, y, x+(strength * Math.cos(Math.toRadians(theta))), y+(strength * Math.sin(Math.toRadians(theta))));
        thisRay.setStrokeWidth(3);
    }

    public Line getRay() {
        return thisRay;
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

    public void updateStrength (double updatedStrength) {
        strength = updatedStrength;
        thisRay.setEndX(thisRay.getEndX() + (strength * Math.cos(Math.toRadians(theta))));
        thisRay.setEndY(thisRay.getEndY() + (strength * Math.sin(Math.toRadians(theta))));
    }
}
