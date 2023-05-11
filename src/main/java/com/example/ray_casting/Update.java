package com.example.ray_casting;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

import java.util.ArrayList;

public class Update extends AnimationTimer {
    private long lastUpdate;
    double x1, x2, y1, y2;
    double[] points = new double[2];
    ArrayList<ArrayList<Point2D>> lightPointList = new ArrayList<>();
    ArrayList<ArrayList<Point2D>> irisPointList = new ArrayList<>();
    ArrayList<Shape> lightShapes = new ArrayList<>();
    ArrayList<Image> lightImages = new ArrayList<>();
    BoxBlur bb, bb2;
    Image shadowImage;
    double shadowOpacity = 0.7;

    WritableImage lightImage = new WritableImage(800, 800);
    PixelWriter lightPixelWriter = lightImage.getPixelWriter();

    @Override
    public void start() {
        super.start();
        WritableImage image = new WritableImage(Controller.sceneX, Controller.sceneY);
        PixelWriter pw = image.getPixelWriter();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pw.setColor(x, y, Color.BLACK);
            }
        }
        shadowImage = image;

//        bb = new BoxBlur();
//        bb.setIterations(3);
//        bb2 = new BoxBlur();
//        bb2.setIterations(10);
//        bb2.setWidth(100);
//        bb2.setHeight(100);
    }

    @Override
    public void handle(long now) {
        if (now - lastUpdate >= 1_00_00) {
            lightPointList.clear();
            lightImages.clear();
            lightImage = new WritableImage(800, 800);
//            irisPointList.clear();
            Controller.top.getChildren().clear();
            lightShapes.clear();
//
            updateLights();
            updateShadows();

            lastUpdate = now;
        }
    }

    private void updateShadows () {
        int a = 0;
        Pane p = new Pane();

        for (LightSource light : Controller.Lights) {
            lightImages.add(light.getLightDropOff());
            lightShapes.add(Tools.arrayListToPolygon(lightPointList.get(a), Color.WHITE));
            if (light instanceof RadialLight) {
                updateRadialShadow(light);
            }
            a++;
        }
        p.getChildren().add(new ImageView(lightImage));
        Controller.top.getChildren().add(p);
//        WritableImage wi = new WritableImage(Controller.sceneX, Controller.sceneY);
//        SnapshotParameters sp = new SnapshotParameters();
//        sp.setFill(Color.TRANSPARENT);
//
//        for (int b = 0; b < lightImages.size(); b++) {
//            lightShapes.get(b).setFill(new ImagePattern(lightImages.get(b), lightShapes.get(b).getBoundsInParent().getMinX(),lightShapes.get(b).getBoundsInParent().getMinY(),lightShapes.get(b).getBoundsInParent().getWidth(),lightShapes.get(b).getBoundsInParent().getHeight(),false));
//            wi = lightShapes.get(b).snapshot(sp, wi);
//        }
//        ImageView iv = new ImageView(wi);
//        p.getChildren().add(iv);
//        Controller.top.getChildren().addAll(lightShapes);


    }

    private void updateRadialShadow (LightSource light) {
        Circle c = new Circle(light.getStrength());
        c.setFill(new ImagePattern(light.getLightDropOff()));
        c.setFill(Color.WHITE);
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.BLACK);
        WritableImage wi = c.snapshot(sp, null);

        lightImage = (WritableImage) combineImages(wi, (int) light.getX(), (int) light.getY());


//        for (int b = (int) light.getX(); b < (light.getX() + wi.getWidth()-1); b++) {
//            for (int d = (int) light.getY(); d < (light.getY() + wi.getHeight()-1); d++) {
//                try {
//                    System.out.println((int) light.getX());
//                    red = Math.max(wi.getPixelReader().getColor(x, y).getRed(), lightImage.getPixelReader().getColor(x, y).getRed());
//                    blue = Math.max(wi.getPixelReader().getColor(x, y).getBlue(), lightImage.getPixelReader().getColor(x, y).getBlue());
//                    green = Math.max(wi.getPixelReader().getColor(x, y).getGreen(), lightImage.getPixelReader().getColor(x, y).getGreen());
//                    alpha = (wi.getPixelReader().getColor(x, y).getOpacity() + lightImage.getPixelReader().getColor(x, y).getOpacity())/2.0;
//                    lightPixelWriter.setColor(d, b, new Color(red,green, blue, alpha));
//                }
//                catch (Exception e) {
//
//                }
////                red = Math.max(wi.getPixelReader().getColor(x, y).getRed(), lightImage.getPixelReader().getColor(d, b).getRed());
////                blue = Math.max(wi.getPixelReader().getColor(x, y).getBlue(), lightImage.getPixelReader().getColor(d, b).getBlue());
////                green = Math.max(wi.getPixelReader().getColor(x, y).getGreen(), lightImage.getPixelReader().getColor(d, b).getGreen());
////                alpha = (wi.getPixelReader().getColor(x, y).getOpacity() + lightImage.getPixelReader().getColor(d, b).getOpacity())/2.0;
////                lightPixelWriter.setColor(d, b, new Color(red,green, blue, alpha));
//                x++;
//                y++;
//            }
//        }
    }

    public Image combineImages(Image overlayImage, double x, double y) {
        int baseWidth = (int) lightImage.getWidth();
        int baseHeight = (int) lightImage.getHeight();

        // Create a new writable image with the same dimensions as the base image
        WritableImage resultImage = new WritableImage(baseWidth, baseHeight);

        // Get the pixel reader for the base image
        PixelReader baseReader = lightImage.getPixelReader();

        // Get the pixel reader for the overlay image
        PixelReader overlayReader = overlayImage.getPixelReader();

        // Get the pixel writer for the result image
        PixelWriter writer = resultImage.getPixelWriter();

        double red;
        double blue;
        double green;
        double alpha;
        int overlayCol;
        int overlayRow;

        // Iterate over each pixel of the base image
        for (int a = 0; a < baseHeight; a++) {
            for (int b = 0; b < baseWidth; b++) {
                // Calculate the corresponding position in the overlay image
                 overlayCol = (int) (b + x);
                 overlayRow = (int) (a + y);

                // Check if the overlay position is within the overlay image bounds
                if (overlayCol >= 0 && overlayCol < overlayImage.getWidth() && overlayRow >= 0 && overlayRow < overlayImage.getHeight()) {

                    // Get the color of the current pixel in the base image
//                    int baseArgb = baseReader.getArgb(col, row);
//
//                    // Get the color of the corresponding pixel in the overlay image
//                    int overlayArgb = overlayReader.getArgb(overlayCol, overlayRow);
//
//                    // Combine the base and overlay colors
//                    int combinedArgb = combineColors(baseArgb, overlayArgb);

                    // Write the combined color to the result image
                    red = Math.max(overlayReader.getColor(b, a).getRed(), baseReader.getColor(overlayCol, overlayRow).getRed());
                    blue = Math.max(overlayReader.getColor(b, a).getBlue(), baseReader.getColor(overlayCol, overlayRow).getBlue());
                    green = Math.max(overlayReader.getColor(b, a).getGreen(), baseReader.getColor(overlayCol, overlayRow).getGreen());
                    alpha = (overlayReader.getColor(b, a).getOpacity() + baseReader.getColor(overlayCol, overlayRow).getOpacity())/2.0;
                    writer.setColor(b, a, Color.RED);
                }
                else {
                    // If the overlay position is outside the overlay image bounds, use the base color
                    writer.setColor(b, a, new Color(0.0, 0.0, 0.0, shadowOpacity));
                }
            }
        }

        // Return the combined image
        return resultImage;
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
            }
            lightPointList.add(updateRays(light.getRays()));
        }
    }

    private ArrayList<Point2D> updateRays (ArrayList<Ray> rays) {
        ArrayList<Point2D> lightPoints = new ArrayList<>();
        for (Ray ray : rays) {
            ray.updateRayOrigin(x1, y1);

            x2 = ray.getRay().getEndX();
            y2 = ray.getRay().getEndY();
            for (Obstacle ob : Controller.obstacles) {
                double a;
                double b;
                points[0] = 0;
                points[1] = 0;
                Point2D point = Tools.getShortestIntersection(ray.getRay(), ob.getDetectionLines());
                if (point != null) {
                    points[0] = point.getX();
                    points[1] = point.getY();
                    ray.updateRayEndPoints(point.getX(), point.getY());
                }
                else {
                    points[0] = ray.getRay().getEndX();
                    points[1] = ray.getRay().getEndY();
                }
            }
            lightPoints.add(new Point2D(points[0], points[1]));
        }
        return lightPoints;
        //lightPointList.add(lightPoints);
    }
}

//    private void updateShadows () {
//        Controller.shadows = new Rectangle(Controller.sceneX*4, Controller.sceneY*4);
//        Controller.shadows.setLayoutX(-Controller.sceneX);
//        Controller.shadows.setLayoutY(-Controller.sceneY);
//
//        Pane lights = new Pane();
//        lights.setMinSize(1000,1000);
//        lights.getChildren().add(Controller.shadows);
//        Shape cutout = Controller.shadows;
//        Shape commonCutout = new Circle(0);
//        ArrayList<Shape> listOfLights = new ArrayList<>();
//        ArrayList<Integer> strengths = new ArrayList<>();
//
//        int b = 0;
//        for (LightSource l : Controller.Lights) {
//            Shape s = Tools.arrayListToPolygon(lightPointList.get(b), Color.WHITE);
//            s.setFill(l.getLightColor());
//            listOfLights.add(s);
//            strengths.add(l.getStrength());
//
//            Shape c = new Circle(0);
//            if (irisPointList != null) {
//                c = Tools.arrayListToPolygon(irisPointList.get(b), Color.WHITE);
//            }
//
//            commonCutout = Shape.union(commonCutout, c);
//
//            Controller.shadows = Shape.subtract(Controller.shadows, c);
//            cutout = Shape.subtract(cutout, s);
//            s.setScaleX(1.005);
//            s.setScaleY(1.005);
//            b++;
//        }
//
//        int a = 0;
//        for (Shape s : listOfLights) {
//            Color temp = (Color) s.getFill();
//            s = Shape.subtract(s, commonCutout);
//            System.out.println(s.getBoundsInParent());
//            //s.setBlendMode(BlendMode.ADD);
//
////            RadialGradient gradient = new RadialGradient(
////                    0, 0, 0.5, 0.5, 0.5, true, null,
////                    new Stop(0, Color.TRANSPARENT),
////                    new Stop(1, Color.BLACK));
////
////            RadialGradient gradient2 = new RadialGradient(
////                    0, 0, 0.5, 0.5, 0.5, true, null,
////                    new Stop(0, Color.TRANSPARENT),
////                    new Stop(1, Color.BLUE));
//
//            s.setFill(temp);
//            Bloom bloom = new Bloom();
//            bloom.setThreshold(.1);
//            bb.setInput(bloom);
//            bb.setWidth(Math.pow(strengths.get(a), .95));
//            bb.setHeight(Math.pow(strengths.get(a), .95));
//            //s.setEffect(bb);
//
//            lights.getChildren().add(s);
//            a++;
//        }
//
//        //Controller.shadows.setEffect(bb2);
//        lights.getChildren().remove(0);
//        //Controller.top.getChildren().addAll(Controller.shadows, lights);
//        Controller.top.getChildren().addAll(lights, cutout);
//    }
//

//

//}


//package com.example.ray_casting;
//
//import javafx.animation.AnimationTimer;
//import javafx.collections.ObservableList;
//import javafx.geometry.Point2D;
//import javafx.scene.effect.Bloom;
//import javafx.scene.effect.BoxBlur;
//import javafx.scene.layout.Pane;
//import javafx.scene.paint.*;
//import javafx.scene.shape.*;
//
//import java.util.ArrayList;
//import java.util.Collections;
//
//public class Update extends AnimationTimer {
//    private long lastUpdate;
//    double x1, x2, y1, y2;
//
//
//    double[] points = new double[2];
//    double[] beamStartPoints = new double[2];
//    ArrayList<ArrayList<Point2D>> lightPointList = new ArrayList<>();
//    ArrayList<ArrayList<Point2D>> irisPointList = new ArrayList<>();
//    ArrayList<Shape> listOfLights = new ArrayList<>();
//    ArrayList<Integer> strengths = new ArrayList<>();
//    Pane lights = new Pane();
//    BoxBlur bb, bb2;
//
//    double x3, x4, y3, y4;
//
//    @Override
//    public void start() {
//        super.start();
//        bb = new BoxBlur();
//        bb.setIterations(3);
//        bb2 = new BoxBlur();
//        bb2.setIterations(10);
//        bb2.setWidth(100);
//        bb2.setHeight(100);
//    }
//
//    @Override
//    public void handle(long now) {
//        if (now - lastUpdate >= 10_000_000) {
//            clearLists();
//            updateLights();
//            //updateShadows();
//
//            lastUpdate = now;
//        }
//    }
//
//    private void updateShadows () {
//        Controller.shadows = new Rectangle(Controller.sceneX*4, Controller.sceneY*4);
//        Controller.shadows.setLayoutX(-Controller.sceneX);
//        Controller.shadows.setLayoutY(-Controller.sceneY);
//
//        lights.setMinSize(1000,1000);
//        lights.getChildren().add(Controller.shadows);
//        Shape cutout = Controller.shadows;
//        Shape commonCutout = new Circle(0);
//
//        int b = 0;
//        for (LightSource l : Controller.Lights) {
//            Shape s = Tools.arrayListToPolygon(lightPointList.get(b), Color.WHITE);
//            lights.getChildren().add(s);
////            s.setFill(l.getLightColor());
////            listOfLights.add(s);
////            strengths.add(l.getStrength());
////
////            Shape c = new Circle(0);
////            if (irisPointList == null) {
////                c = Tools.arrayListToPolygon(irisPointList.get(b), Color.WHITE);
////            }
////
////            commonCutout = Shape.union(commonCutout, c);
////
////            Controller.shadows = Shape.subtract(Controller.shadows, c);
////            cutout = Shape.subtract(cutout, s);
////            b++;
////
////            shapeEffects(s, commonCutout, l.getStrength(), lights);
//        }
//
//        Controller.shadows.setEffect(bb2);
//        //lights.getChildren().remove(0);
//        //Controller.top.getChildren().addAll(Controller.shadows, lights);
//        Controller.top.getChildren().add(lights);
//    }
//
//    private void shapeEffects (Shape s, Shape commonCutout, int strength, Pane lights) {
//        Color temp = (Color) s.getFill();
//        s = Shape.subtract(s, commonCutout);
//        //s.setBlendMode(BlendMode.ADD);
//
//        s.setFill(temp);
//        Bloom bloom = new Bloom();
//        bloom.setThreshold(.1);
//        bb.setInput(bloom);
//        bb.setWidth(Math.pow(strength, 1.25));
//        bb.setHeight(Math.pow(strength, 1.25));
//        //s.setEffect(bb);
//
//        lights.getChildren().add(s);
//    }
//
//    private void updateLights () {
//        for (LightSource light : Controller.Lights) {
//            if (light.stationary) {
//                x1 = light.getX();
//                y1 = light.getY();
//            }
//            else {
//                x1 = Controller.mousex;
//                y1 = Controller.mousey;
//                light.setX(x1);
//                light.setY(y1);
//                light.getIris().setX(x1);
//                light.getIris().setY(y1);
//            }
//            lightPointList.add(updateRays(light));
//            if (light.getIris() != null) {
//                irisPointList.add(updateRays(light.getIris()));
//            }
//        }
//    }
//
//    private ArrayList<Point2D> updateRays (LightSource light) {
//        ArrayList<Point2D> lightPoints = new ArrayList<>();
//        ArrayList<Point2D> lightEndPoints = new ArrayList<>();
//
////        if (light instanceof BeamLight) {
////            for (int a = light.getRays().size()-1; a > 1; a--) {
////                //check if point is inside a polygon
////                lightPoints.add(new Point2D(light.getRays().get(a).getRay().getStartX(), light.getRays().get(a).getRay().getStartY()));
////            }
////        }
//        if (light instanceof BeamLight) {
//            ((BeamLight) light).changeAngle(((BeamLight) light).theta+1);
//        }
//
//        for (Ray ray : light.getRays()) {
//            //ray.setTheta(ray.theta + 1);
//            if (light.stationary) {
//                x1 = ray.getRay().getStartX();
//                y1 = ray.getRay().getStartY();
//            }
//            ray.updateRayOrigin(x1, y1);
//
//            x2 = ray.getRay().getEndX();
//            y2 = ray.getRay().getEndY();
//
//            for (Obstacle ob : Controller.obstacles) {
//                Point2D point = test.getShortestIntersection(ray.getRay(), ob.getDetectionLines());
//                if (point != null) {
//                    lightEndPoints.add(point);
//                    lightPoints.add(new Point2D(x1, y1));
//                    ray.updateRayEndPoints(point.getX(), point.getY());
//                }
//                lightEndPoints.add(new Point2D(x2, y2));
//            }
//
////            boolean recordValue = true;
////            for (Obstacle ob : Controller.obstacles) {
////                double a;
////                double b, u, t, denom;
////                points[0] = 0;
////                points[1] = 0;
////
////                int collisions = 0;
////
////                if (Tools.checkInside(ob, new Point2D(x1, y1)) == 1) {
////                    recordValue = false;
////                    ray.getRay().setStroke(Color.RED);
////                }
////
////
////                }
////                for (Line l : ob.getDetectionLines()) {
////                    x3 = l.getStartX();
////                    y3 = l.getStartY();
////                    x4 = l.getEndX();
////                    y4 = l.getEndY();
////
////                    denom = ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
////                    if (denom == 0) {
////                        beamStartPoints[0] = x1;
////                        beamStartPoints[1] = y1;
////                        continue;
////                    }
////                    t = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
////                    u = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
////
////                    if (!(t < 0 || t > 1 || u < 0 || u > 1)) {
////                        collisions++;
////                        a = x1 + t * (x2 - x1);
////                        b = y1 + t * (y2 - y1);
////                        if (Math.sqrt(Math.pow(a - x1, 2) + Math.pow(b - y1, 2)) < Math.sqrt(Math.pow(points[0] - x1, 2) + Math.pow(points[1] - y1, 2))) {
////                            beamStartPoints[0] = x1;
////                            beamStartPoints[1] = y1;
////                            points[0] = a;
////                            points[1] = b;
////                            ray.updateRayEndPoints(a, b);
////                            System.out.println(collisions);
////                        }
////                    }
////                    else {
////                        beamStartPoints[0] = x1;
////                        beamStartPoints[1] = y1;
////                        points[0] = ray.getRay().getEndX();
////                        points[1] = ray.getRay().getEndY();
////                    }
//                    //Tools.recalculateEndsBeam(x1, y1, x2, y2, x3, y3, x4, y4, ray, points, beamStartPoints);
////                }
////            }
////            if (recordValue) {
////                lightEndPoints.add(new Point2D(beamStartPoints[0], beamStartPoints[1]));
////                lightPoints.add(new Point2D(points[0], points[1]));
////            }
//        }
//        if (light instanceof BeamLight) {
//            //Collections.reverse(lightEndPoints);
//            lightPoints.addAll(lightEndPoints);
//            lightPoints = Tools.sortVertices(lightPoints);
//        }
//        return lightPoints;
//    }
//
//    private void clearLists () {
//        lights.getChildren().clear();
//        lightPointList.clear();
//        irisPointList.clear();
//        listOfLights.clear();
//        strengths.clear();
//        Controller.top.getChildren().clear();
//    }
//}
