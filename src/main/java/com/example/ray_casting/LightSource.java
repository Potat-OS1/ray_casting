package com.example.ray_casting;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class LightSource {
    boolean stationary;
    double originX;
    double originY;
    Color lightColor;
    final int strength;
    ArrayList<Ray> rays = new ArrayList<>();
    RadialLight iris;
    int rayCount;

    LightSource (int rayCount, double originX, double originY, Color c, boolean stationary, int strength) {
        this.strength = strength;
        this.lightColor = c;
        this.stationary = stationary;
        this.rayCount = rayCount;

        if (stationary) {
            this.originX = originX;
            this.originY = originY;
        }
    }

    public RadialLight getIris () {
        if (iris != null) {
            return iris;
        }
        else {
            return null;
        }
    }

    public ArrayList<Ray> getRays () {
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

    public int getStrength () {
        return strength;
    }
}
