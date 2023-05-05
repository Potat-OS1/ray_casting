package com.example.ray_casting;

import javafx.scene.paint.Color;

public class BeamLight extends LightSource{
    double theta;
    double width;
    double m;
    double deltaX;

    BeamLight(int rayCount, double originX, double originY, Color c, boolean stationary, int strength, double theta, double width) {
        super(rayCount, originX, originY, c, stationary, strength);
        this.theta = theta;
        this.width = width;
        calculateSlope();
        calculateDeltaX(rayCount/2);
        System.out.println(m + "   " + deltaX);

        double x;
        for (int a = 0; a < rayCount; a++) {
            x = deltaX * a;
            rays[a] = new Ray(x + originX - (width/2),(m * x) + originY,theta, strength);
            Controller.p.getChildren().add(rays[a].getRay());
            System.out.println(rays[a].getRay().getStartX() + "  " + rays[a].getRay().getStartY());
            System.out.println(rays[a].getRay().getEndX() + "  " + rays[a].getRay().getEndY());
        }
    }

    private void calculateSlope () {
        m = -(Math.cos(Math.toRadians(theta))/Math.sin(Math.toRadians(theta)));
        if (Double.isInfinite(m)) {
            m=0;
        }
    }

    private void calculateDeltaX (int rayCount) {
        deltaX = Math.cos(Math.toRadians(theta)) * width / rayCount;
    }
}
