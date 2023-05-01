package com.example.ray_casting;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

public class Obstactle {
    private ArrayList<Line> detectionLines = new ArrayList<>();
    private Polygon obs;
    Obstactle (Point2D[] points, Color c) {
        int a = 0;
        while(true) {
            try {
                detectionLines.add(new Line(points[a].getX(), points[a].getY(), points[a+1].getX(), points[a+1].getY()));
                a++;
            }
            catch (Exception ignored) {
                detectionLines.add(new Line(points[a].getX(), points[a].getY(), points[0].getX(), points[0].getY()));
                break;
            }
        }
        obs = new Polygon();
        obs.setFill(c);
        ObservableList<Double> list = obs.getPoints();
        for (int b = 0; b < points.length; b++) {
            list.add(points[b].getX());
            list.add(points[b].getY());
        }
    }

    public Polygon getObs () {
        return obs;
    }

    public ArrayList<Line> getDetectionLines () {
        return detectionLines;
    }
}
