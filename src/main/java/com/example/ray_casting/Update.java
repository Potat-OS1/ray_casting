package com.example.ray_casting;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
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
    ArrayList<ArrayList<Point2D>> irisPointList = new ArrayList<>();
    BoxBlur bb, bb2;

    @Override
    public void start() {
        super.start();
        bb = new BoxBlur();
        bb.setIterations(3);
        bb2 = new BoxBlur();
        bb2.setIterations(10);
        bb2.setWidth(1000);
        bb2.setHeight(1000);
    }

    @Override
    public void handle(long now) {
        if (now - lastUpdate >= 1_00_00) {
            lightPointList.clear();
            irisPointList.clear();
            Controller.top.getChildren().clear();

            updateLights();
            updateShadows();

            lastUpdate = now;
        }
    }

    private void updateShadows () {
        Controller.shadows = new Rectangle(Controller.sceneX*4, Controller.sceneY*4);
        Controller.shadows.setLayoutX(-Controller.sceneX);
        Controller.shadows.setLayoutY(-Controller.sceneY);

        Pane lights = new Pane();
        lights.setMinSize(1000,1000);
        lights.getChildren().add(Controller.shadows);
        Shape cutout = Controller.shadows;
        Shape commonCutout = new Circle(0);
        ArrayList<Shape> listOfLights = new ArrayList<>();
        ArrayList<Integer> strengths = new ArrayList<>();
        int b = 0;
        for (LightSource l : Controller.Lights) {
            Shape s = PointsToPolygon.arrayListToPolygon(lightPointList.get(b), Color.WHITE);
            s.setFill(l.getLightColor());
            listOfLights.add(s);
            strengths.add(l.getStrength());

            Shape c = new Circle(0);
            if (irisPointList == null) {
                c = PointsToPolygon.arrayListToPolygon(irisPointList.get(b), Color.WHITE);
            }

            commonCutout = Shape.union(commonCutout, c);

            Controller.shadows = Shape.subtract(Controller.shadows, c);
            cutout = Shape.subtract(cutout, s);
            b++;
        }

        int a = 0;
        for (Shape s : listOfLights) {
            Color temp = (Color) s.getFill();
            s = Shape.subtract(s, commonCutout);
            s.setBlendMode(BlendMode.ADD);
            s.setFill(temp);
            Bloom bloom = new Bloom();
            bloom.setThreshold(.1);
            bb.setInput(bloom);
            bb.setWidth(Math.pow(strengths.get(a), 1.5));
            bb.setHeight(Math.pow(strengths.get(a), 1.5));
            s.setEffect(bb);
            lights.getChildren().add(s);
            a++;
        }

        Controller.shadows.setEffect(bb2);
        lights.getChildren().remove(0);
        //Controller.top.getChildren().addAll(Controller.shadows, lights);
        Controller.top.getChildren().add(lights);
    }

    private void updateLights () {
        for (LightSource light : Controller.Lights) {
            if (light.stationary) {
                x1 = light.getX();
                y1 = light.getY();
            }
            else {
                x1 = Controller.mousex;
                y1 = Controller.mousey;
                light.setX(x1);
                light.setY(y1);
                light.getIris().setX(x1);
                light.getIris().setY(y1);
            }
            lightPointList.add(updateRays(light.getRays()));
            if (light.getIris() != null) {
                irisPointList.add(updateRays(light.getIris().getRays()));
            }
        }
    }

    private ArrayList<Point2D> updateRays (Ray[] rays) {
        ArrayList<Point2D> lightPoints = new ArrayList<>();
        for (Ray ray : rays) {
            ray.updateRayOrigin(x1, y1);

            x2 = ray.getRay().getEndX();
            y2 = ray.getRay().getEndY();
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
                        a = x1 + t * (x2 - x1);
                        b = y1 + t * (y2 - y1);
                        if (Math.sqrt(Math.pow(a - x1, 2) + Math.pow(b - y1, 2)) < Math.sqrt(Math.pow(points[0] - x1, 2) + Math.pow(points[1] - y1, 2))) {
                            points[0] = a;
                            points[1] = b;
                            ray.updateRayEndPoints(points[0], points[1]);
                        }
                    } else {
                        points[0] = ray.getRay().getEndX();
                        points[1] = ray.getRay().getEndY();
                    }
                }
            }
            lightPoints.add(new Point2D(points[0], points[1]));
        }
        return lightPoints;
        //lightPointList.add(lightPoints);
    }
}
