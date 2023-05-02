package com.example.ray_casting;

import javafx.scene.paint.Color;

public class Light {
    Ray[] rays;
    boolean stationary;
    double originX;
    double originY;
    Color lightColor;

    Light (int rayCount, int originX, int originY, boolean stationary, Color lightColor, int strength) {
        rays = new Ray[rayCount];

        for (int a = 0; a < rayCount; a++) {
            Ray r = new Ray(originX, originY, (360.0/rayCount)*a, strength);
            rays[a] = r;
            r.getRay().setOpacity(0.1);
            //Controller.p.getChildren().add(r.getRay());
        }

        this.lightColor = lightColor;
        this.stationary = stationary;
        if (stationary) {
            this.originX = originX;
            this.originY = originY;
        }
    }

    public Ray[] getRays () {
        return rays;
    }

    public double getX () {
        return originX;
    }

    public double getY () {
        return originY;
    }

    public void setX (double coord) {
        originX = coord;
    }

    public void setY (double coord) {
        originY = coord;
    }

    public Color getLightColor () {
        return lightColor;
    }

}
