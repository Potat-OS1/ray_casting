package com.example.ray_casting;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class Tools {


    // this is modified method and the one below this is by Patrick on Math.stackexchange @ https://math.stackexchange.com/questions/978642/how-to-sort-vertices-of-a-polygon-in-counter-clockwise-order.
    // they use some custom Point class while i use the Javafx Point2D.
    public static Point2D findCentroid (ArrayList<Point2D> points) {
        int x = 0;
        int y = 0;

        for (Point2D point : points) {
            x += point.getX();
            y += point.getY();
        }

        return new Point2D(x/(points.size()-0.01), y/(points.size()-0.01));
    }

    public static ArrayList<Point2D> sortVertices (ArrayList<Point2D> points) {
        Point2D center = findCentroid(points);
        try {
            points.sort((a, b) -> {
                double a1 = (Math.toDegrees(Math.atan2(a.getX() - center.getX(), a.getY() - center.getY())) + 360) % 360;
                double a2 = (Math.toDegrees(Math.atan2(b.getX() - center.getX(), b.getY() - center.getY())) + 360) % 360;
                return (int) (a2 - a1);
            });
        }
        catch (Exception ignored) {

        }

        return points;
    }

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

    public static void recalculateEndsBeam( double x1, double y1, double x2, double y2,
                                        double x3, double y3, double x4, double y4,
                                        Ray ray, double[] points, double[] beamStartPoints) {

        double denom, t, u, a, b;

        denom = ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
        if (denom == 0) {
        }
        t = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        u = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
        if (!(t < 0 || t > 1 || u < 0 || u > 1)) {
            a = x1 + t * (x2 - x1);
            b = y1 + t * (y2 - y1);
            if (Math.sqrt(Math.pow(a - x1, 2) + Math.pow(b - y1, 2)) < Math.sqrt(Math.pow(points[0] - x1, 2) + Math.pow(points[1] - y1, 2))) {
                points[0] = a;
                points[1] = b;
                beamStartPoints[0] = x1;
                beamStartPoints[1] = y1;
                ray.updateRayEndPoints(points[0], points[1]);
            }
        }
        else {
            beamStartPoints[0] = x1;
            beamStartPoints[1] = y1;
            points[0] = ray.getRay().getEndX();
            points[1] = ray.getRay().getEndY();
        }
    }

    // onLine, direction, isIntersect and checkInside are all modified code by Potta Lokesh
    // from this: https://www.geeksforgeeks.org/how-to-check-if-a-given-point-lies-inside-a-polygon/
    // i mainly changed how it was formatted to fit my needs.
    private static int onLine (Line l, double x, double y) {
        double lx1 = l.getStartX();
        double lx2 = l.getEndX();
        double ly1 = l.getStartY();
        double ly2 = l.getEndY();
        if (x <= Math.max(lx1, lx2) &&
            x <= Math.min(lx1, lx2) &&
            y <= Math.max(ly1, ly2) &&
            y <= Math.min(ly1, ly2)) {
            return 1;
        }
        return 0;
    }

    private static int direction (double x1, double y1, double x2, double y2, double x3, double y3) {
        double val = (y2 - y1) * (x3 - x2) - (x2 - x1) * (y3 - y2);
        if (val == 0) {
            return 0;
        }
        else if (val < 0) {
            return 2;
        }
        return 1;
    }

    private static int isIntersect (Line l1, Line l2) {
        int dir1 = direction(l1.getStartX(), l1.getStartY(), l1.getEndX(), l1.getEndY(), l2.getStartX(), l2.getStartY());
        int dir2 = direction(l1.getStartX(), l1.getStartY(), l1.getEndX(), l1.getEndY(), l2.getEndX(), l2.getEndY());
        int dir3 = direction(l2.getStartX(), l2.getStartY(), l2.getEndX(), l2.getEndY(), l1.getStartX(), l1.getStartY());
        int dir4 = direction(l2.getStartX(), l2.getStartY(), l2.getEndX(), l2.getEndY(), l1.getEndX(), l1.getEndY());
        if (dir1 != dir2 && dir3 != dir4) {
            return 1;
        }
        if (dir1 == 0 && onLine(l1, l2.getStartX(), l2.getStartY()) == 1) {
            return 1;
        }
        if (dir2 == 0 && onLine(l1, l2.getEndX(), l2.getEndY()) == 1) {
            return 1;
        }
        if (dir3 == 0 && onLine(l2, l1.getStartX(), l1.getStartY()) == 1) {
            return 1;
        }
        if (dir4 == 0 && onLine(l2, l1.getEndX(), l1.getEndY()) == 1) {
            return 1;
        }
        return 0;
    }

    public static int checkInside (Obstacle obs, Point2D point) {
        Point2D pt = new Point2D(9999, point.getY());
        Line exLine = new Line(point.getX(), point.getY(), pt.getX(), pt.getY());
        int count = 0;
        int i = 0;

        do {
            Line l = obs.getDetectionLines().get(i);
            if (isIntersect(l, exLine) == 1) {
                if (direction(l.getStartX(), l.getStartY(), point.getX(), point.getY(), l.getEndX(), l.getEndY()) == 0) {
                    return onLine(l, point.getX(), point.getY());
                }
                count++;
            }
            i = (i+1) % obs.getDetectionLines().size();
        }
        while (i != 0);

        return count & 1;
    }
    // end of taken code


    // chatGPT made this and it makes me want to die inside as i had similar code but this was slightly less tempermental
    public static Point2D getShortestIntersection(Line line, ArrayList<Line> otherLines) {
        Point2D shortestIntersection = null;
        double shortestDistance = Double.POSITIVE_INFINITY;

        for (Line otherLine : otherLines) {
            if (line != otherLine) {
                // Do not compare line with itself
                Point2D intersection = getIntersection(line, otherLine);
                if (intersection != null) {
                    // Lines intersect
                    double distance = line.getStartX() - intersection.getX();
                    if (distance < 0) {
                        // Intersection is not on the line segment
                        distance = line.getEndX() - intersection.getX();
                    }
                    if (distance >= 0 && distance < shortestDistance) {
                        // Shortest intersection so far
                        shortestIntersection = intersection;
                        shortestDistance = distance;
                    }
                }
            }
        }

        return shortestIntersection;
    }

    private static Point2D getIntersection(Line line1, Line line2) {
        double x1 = line1.getStartX();
        double y1 = line1.getStartY();
        double x2 = line1.getEndX();
        double y2 = line1.getEndY();
        double x3 = line2.getStartX();
        double y3 = line2.getStartY();
        double x4 = line2.getEndX();
        double y4 = line2.getEndY();

        double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (denom == 0) { // Lines are parallel
            return null;
        }

        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
        if (ua < 0 || ua > 1 || ub < 0 || ub > 1) { // Intersection is not on line segments
            return null;
        }

        double x = x1 + ua * (x2 - x1);
        double y = y1 + ua * (y2 - y1);

        return new Point2D(x, y);
    }

}
