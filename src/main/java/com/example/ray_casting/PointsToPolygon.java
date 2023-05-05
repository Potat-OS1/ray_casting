package com.example.ray_casting;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class PointsToPolygon {
    public static Shape arrayToPolygon (Point2D[] points, Color c) {
        Polygon polygon = new Polygon();
        polygon.setFill(c);
        ObservableList<Double> list = polygon.getPoints();
        for (Point2D point : points) {
            list.add(point.getX());
            list.add(point.getY());
        }
        return polygon;
    }

    public static Shape arrayListToPolygon (ArrayList<Point2D> points, Color c) {
        return arrayToPolygon(points.toArray(new Point2D[0]), c);
    }
}
