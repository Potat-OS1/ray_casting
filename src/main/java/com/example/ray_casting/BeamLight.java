package com.example.ray_casting;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BeamLight extends LightSource{
    double theta;
    double width;
    double deltaX;
    double deltaY;

    double x;
    double y;
    double cos;
    double sin;

    BeamLight(int rayCount, double originX, double originY, Color c, boolean stationary, int strength, double theta, double width) {
        super(rayCount, originX, originY, c, stationary, strength);
        this.theta = theta;
        this.width = width;
        calculateDeltas(rayCount);

        Circle d = new Circle(strength);
        Circle center = new Circle(5);
        d.setOpacity(.3);
        Controller.p.getChildren().addAll(d, center);
        d.setLayoutX(originX);
        d.setLayoutY(originY);
        center.setLayoutX(originX);
        center.setLayoutY(originY);

        for (int a = 0; a < rayCount/2; a++) {
            cos = Math.cos(Math.toRadians(theta + 90));
            sin = Math.sin(Math.toRadians(theta + 90));
            x = cos * deltaX * (a*2) + originX;
            y = sin * deltaY * (a*2) + originY;

            Circle circle = new Circle(2);
            circle.setFill(Color.PURPLE);
            circle.setLayoutX(x);
            circle.setLayoutY(y);
            Controller.p.getChildren().add(circle);

            rays.add(new Ray(x, y, theta, strength));
            x = cos * deltaX * (a*2) + originX * -1;
            y = sin * deltaY * (a*2) + originY * -1;

            Circle circle2 = new Circle(2);
            circle2.setFill(Color.PURPLE);
            circle2.setLayoutX(-x);
            circle2.setLayoutY(-y);
            Controller.p.getChildren().add(circle2);


            rays.add(new Ray((-x), (-y), theta, strength));
        }
        rays.remove(0);
        for (Ray ray : rays) {
            Controller.p.getChildren().add(ray.getRay());
        }
    }

    private void calculateDeltas (int rayCount) {
        deltaX = ((width / rayCount));
        deltaY = ((width / rayCount));
    }

    public void changeAngle (double theta) {
        this.theta = theta;
        for (int a = 0; a < rayCount-1; a++) {
            rays.get(a).setTheta(theta + 90 + 1);
            rays.get(a).updateRayDirection(theta + 90 + 1);
//            rays.get(a).setTheta(theta+90);
//            cos = Math.cos(Math.toRadians(theta + 90));
//            sin = Math.sin(Math.toRadians(theta + 90));
//            x = cos * deltaX * a + originX;
//            y = sin * deltaY * a + originY;
//            rays.get(a).updateRayOrigin(x, y);
//            cos = Math.cos(Math.toRadians(theta + 90));
//            sin = Math.sin(Math.toRadians(theta + 90));
//            x = cos * deltaX * a + originX;
//            y = sin * deltaY * a + originY;
//
//            rays.add(new Ray(x, y, theta, strength));
//            x = cos * deltaX * a + originX * -1;
//            y = sin * deltaY * a + originY * -1;
//            rays.add(new Ray((-x), (-y), theta, strength));
        }
    }

    @Override
    void lightDropOffImage() {

    }
}
