package com.example.ray_casting;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

import java.util.ArrayList;

public class Update extends AnimationTimer {
    private long lastUpdate;
    double x1, x2, y1, y2;
    double x3, x4, y3, y4;
    double denom;
    double u, t;
    double[] points = new double[2];
    ArrayList<ArrayList<Point2D>> lightPointList = new ArrayList<>();
    RadialGradient gradient1;
    BoxBlur bb, bb2;

    @Override
    public void start() {
        super.start();
        bb = new BoxBlur();
        bb.setIterations(5);
        bb2 = new BoxBlur();
        bb2.setHeight(300);
        bb2.setWidth(300);
        bb2.setIterations(10);


        //Controller.top.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
    }

    @Override
    public void handle(long now) {
        if (now - lastUpdate >= 1_00_00) {
            lightPointList.clear();
            Controller.top.getChildren().clear();

            updateLights();
            updateShadows();

            lastUpdate = now;
        }
    }

    private void updateShadows () {
        Controller.shadows = new Rectangle(1400, 1400);
        Controller.shadows.setLayoutX(-200);
        Controller.shadows.setLayoutY(-200);

        Pane lights = new Pane();
        lights.setMinSize(1000,1000);
        lights.getChildren().add(Controller.shadows);
        Shape cutout = Controller.shadows;
        Shape commonCutout = new Circle(0);
        ArrayList<Shape> listOfLights = new ArrayList<>();
        for (Light l : Controller.Lights) {
            Obstactle obs = new Obstactle(lightPointList.get(Controller.Lights.indexOf(l)).toArray(new Point2D[0]), Color.BLUEVIOLET);
            obs.getObs().setOpacity(.3);

            Shape s = obs.getObs();
            s.setFill(l.getLightColor());
            listOfLights.add(s);

            Circle c = new Circle(l.getRays()[0].strength / 1.5);
            c.setLayoutX(l.getX());
            c.setLayoutY(l.getY());

            commonCutout = Shape.union(commonCutout, c);

            Controller.shadows = Shape.subtract(Controller.shadows, c);
            cutout = Shape.subtract(cutout, obs.getObs());
        }

        for (Shape s : listOfLights) {
            Color temp = (Color) s.getFill();
            s = Shape.subtract(s, commonCutout);
            s.setBlendMode(BlendMode.ADD);
            s.setFill(temp);
            Bloom bloom = new Bloom();
            bloom.setThreshold(.1);
            bb2.setInput(bloom);
            s.setEffect(bb2);
            lights.getChildren().add(s);
        }



        //Controller.shadows = Shape.subtract(Controller.shadows, cutout);
        Controller.shadows.setEffect(bb2);
        lights.getChildren().remove(0);
        Controller.top.getChildren().addAll(Controller.shadows, lights);
    }

    private void updateLights () {
        for (Light light : Controller.Lights) {
            if (light.stationary) {
                x1 = light.getX();
                y1 = light.getY();
            }
            else {
                x1 = Controller.mousex;
                y1 = Controller.mousey;
                light.setX(x1);
                light.setY(y1);
            }
            updateRays(light.getRays());
        }
    }

    private void updateRays (Ray[] rays) {
        ArrayList<Point2D> lightPoints = new ArrayList<>();

        for (int c = 0; c < rays.length; c++) {
            rays[c].updateRayOrigin(x1, y1);

            x2 = rays[c].getRay().getEndX();
            y2 = rays[c].getRay().getEndY();
            for (Obstactle ob : Controller.obstacles) {
                double a;
                double b;
                points[0] = 0;
                points[1] = 0;
                for (Line l : ob.getDetectionLines()) {
                    x3 = l.getStartX();
                    y3 = l.getStartY();
                    x4 = l.getEndX();
                    y4 = l.getEndY();

                    denom = ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
                    if (denom == 0) {
                        continue;
                    }
                    t = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
                    u = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;

                    if (!(t < 0 || t > 1 || u < 0 || u > 1)) {
                        a = x1 + t * (x2 -x1);
                        b = y1 + t * (y2 -y1);
                        if (Math.sqrt(Math.pow(a-x1, 2) + Math.pow(b-y1, 2)) < Math.sqrt(Math.pow(points[0]-x1, 2) + Math.pow(points[1]-y1, 2))) {
                            points[0] = a;
                            points[1] = b;
                            rays[c].updateRayEndPoints(points[0], points[1]);
                        }
                    }
                    else {
                        points[0] = rays[c].getRay().getEndX();
                        points[1] = rays[c].getRay().getEndY();
                    }
                }
            }
            lightPoints.add(new Point2D(points[0], points[1]));
        }
        //lightPoints.remove(0);
        lightPointList.add(lightPoints);
    }
}
