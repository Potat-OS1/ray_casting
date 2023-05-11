package com.example.ray_casting;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public abstract class LightSource {
    boolean stationary;
    double originX;
    double originY;
    Color lightColor;
    final int strength;
    ArrayList<Ray> rays = new ArrayList<>();
    protected Image lightDropOff;
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

    public Image getLightDropOff () {
        return lightDropOff;
    }

    abstract void lightDropOffImage ();

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
