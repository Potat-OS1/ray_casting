package com.example.ray_casting;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class Obstactle {
    private final ArrayList<Line> detectionLines = new ArrayList<>();
    private final Shape obs;
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
        obs = PointsToPolygon.arrayToPolygon(points, c);
    }

    public Shape getObs () {
        return obs;
    }

    public ArrayList<Line> getDetectionLines () {
        return detectionLines;
    }
}
